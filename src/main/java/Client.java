
import java.io.*;
import java.net.*;

import com.pi4j.Pi4J;
import com.pi4j.io.exception.IOException;

import com.pi4j.io.spi.SpiBus;
import com.pi4j.io.spi.SpiChipSelect;
import com.pi4j.library.pigpio.PiGpio;
import com.pi4j.plugin.pigpio.provider.spi.PiGpioSpiProvider;


public class Client {
    public static void main(String[] args) throws InterruptedException {


        //use to terminate program
        boolean exit = false;

        var piGpio = PiGpio.newNativeInstance();
        var pi4j = Pi4J.newContextBuilder()
                .noAutoDetect()
                .add(
                        PiGpioSpiProvider.newInstance(piGpio)
                )
                .build();

        SpiChipSelect chipSelect = SpiChipSelect.CS_0;
        SpiBus spiBus = SpiBus.BUS_0;

        LIS3DH test = new LIS3DH(pi4j, spiBus, chipSelect);


        while (!exit) {
            //try to access server at IP and socket
            System.out.println("Attempting to Connect");

            System.out.println("10.253.5.167");
            try (Socket socket = new Socket("10.253.5.167", 2222)) {
                //Sets up reading the socket if successful
                System.out.println("Connected");
                BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                //code to terminate program remotely un comment to activate

                while (!exit) {
                    //read data sent
                    //if(test.isDataReady()) {
                    test.isDataReady();
                        test.readAccelerometerData();
                    //}
                    //String data = receive.readLine();
                    //Help
                    //if (data.contains("end")) {
                     //   exit = true;
                    //}
                    //else if(data.equals("requesting")){
                        //System.out.println("request received, sending data");
                        //writer.println("2048");
                    //}
                    Thread.sleep(1000);
                }
                writer.println("closed");
                writer.close();
                receive.close();
            } catch (UnknownHostException ex) {

                System.out.println("Server not found: " + ex.getMessage());

            } catch (IOException | java.io.IOException ex) {

                System.out.println("I/O error: " + ex.getMessage());
            }
            Thread.sleep(1000);
        }

    }


}