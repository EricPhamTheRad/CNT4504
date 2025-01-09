import java.io.*;
import java.net.*;
import com.pi4j.Pi4J;
import com.pi4j.io.exception.IOException;
import com.pi4j.io.spi.SpiBus;
import com.pi4j.io.spi.SpiChipSelect;
import com.pi4j.library.pigpio.PiGpio;
import com.pi4j.plugin.pigpio.provider.spi.PiGpioSpiProvider;

/**
 * Client code for Raspberry Pi
 * Handles communication with server and setting up SPI
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        //use to terminate program
        boolean exit = false;
        //Configures SPI for the Raspberry PI
        var piGpio = PiGpio.newNativeInstance();
        var pi4j = Pi4J.newContextBuilder()
                .noAutoDetect()
                .add(
                        PiGpioSpiProvider.newInstance(piGpio)
                )
                .build();
        //Selects Pin 8 for the Chip select
        SpiChipSelect chipSelectLIS3DH = SpiChipSelect.CS_1;
        //Creates buss for the SPI
        SpiBus spiBus = SpiBus.BUS_0;
        //Creates the object to handles SPI communication and reading of acceleration from LIS3DH
        LIS3DH test = new LIS3DH(pi4j, spiBus, chipSelectLIS3DH);

        //To Do: Set up class and object for SPI communication for ADC value from MCP3008

        //Main loop, should exit when
        while (!exit) {
            //try to access server at IP and socket
            System.out.println("Attempting to Connect");
            //System.out.println("10.253.5.167");
            System.out.println("10.253.5.167");
            //Currently have to update IP address uses if change location or the router reassigns
            //For Final version use Braindrip's main computer static IP address.
            try (Socket socket = new Socket("10.253.5.167", 2222)) {
                //Sets up reading the socket if successful
                System.out.println("Connected");
                //Wrapper around the socket input to make reading and writing easier
                BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                //code to terminate program remotely un comment to activate
                String data;
                float[] values;
                //Loop current in
                while (!exit) {
                    //read data sent
                    data = receive.readLine();
                    //Checks if the server wants to end data collection
                    if (data.contains("end")) {
                        exit = true;
                    }
                    //if the server request data polls for Acceleration data and ADC in the future
                    if(data.equals("requesting")){
                        System.out.println("request received, sending data");
                        //Checks if Acceleration data is ready, here to test function, will move to the class file
                        /*
                        if(test.isDataReady()){
                            System.out.println("data ready works!");
                        }
                        */
                        //Polls data, currently only prints to client terminal for testing function
                        values = test.readAccelerometerData();
                        //Send data to server in a readable way, temporary
                        writer.printf("%.2f, %.2f, %.2f%n",values[0],values[1],values[2]);
                    }
                    //Slows loop
                    Thread.sleep(1000);
                }
                //closes client
                writer.println("closed");
                writer.close();
                receive.close();
            } catch (UnknownHostException ex) {

                System.out.println("Server not found: " + ex.getMessage());

            } catch (IOException | java.io.IOException ex) {

                System.out.println("I/O error: " + ex.getMessage());
            }
            //Checks every second for the server to open
            Thread.sleep(1000);
        }
    }
}