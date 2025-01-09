import java.io.*;
import java.net.*;
import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * This program is the application of the tension sensing system
 * Handles GUI through a future class file, communication with Raspberry Pi clients
 */

public class Server {
    /**
     * Main function of the program
     * @param args if command line arguments are used in the future they will be stored here
     */
    public static void main(String[] args) {

        //System.out.println(InetAddress.getLocalHost());
        //Flag for exiting main loop
        boolean exit = false;
        //String holds data received
        String data;
        FileHandler filehandler = new FileHandler();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        //Create socket for server at port 2222
        System.out.println("Server is listening on port 2222");
        try (ServerSocket serverSocket = new ServerSocket(2222)) {
            //waits for client to connect
            Socket socket = serverSocket.accept();
            System.out.println("Connected");
            //set up way to send data to client
            BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            //TerminalWriter to allowing user to type in terminal and read the terminal at the same time
            //May not be need in the future
            //Uses multithreading to accomplished
            //Runnable r  = new TerminalWriter(writer);
            //new Thread(r).start();

            //Checks if the connection is made
            //TODO: create code to wait for multiple clients to connect and wait for the user to start collection
            //TODO: Create GUI, lot of work for the future, more important to get data collection and transmission working
            if(!socket.isClosed()){
                System.out.println("Socket Open");
            }

            //main while loop
            while(!exit) {
                //Checks if the client closed unexpected
                if(socket.isClosed()){
                    System.out.println("Socket Closed");
                }
                //Request data from Raspberry Pi, in future spin off to separate class
                System.out.println("requesting data");
                writer.println("requesting");
                //wait 1 second
                Thread.sleep(1000);
                //Read terminal for string data
                data = receive.readLine();
                //checks if closed on command closed
                if(data.equals("closed")){
                    exit = true;
                }
                //Prints what is received
                //TODO: Put data into objects for GUI
                //TODO: Setup data storage in CSV or other file type
                else {
                    System.out.print("Data received: ");
                    System.out.println(data);
                    filehandler.writeResults(dateFormat.format(new Date())+","+data);
                }
            }

            socket.close();
           //Error handling
        } catch (IOException e){
            System.out.println("Server Exception: " + e.getMessage());
       } catch (InterruptedException e) {
           throw new RuntimeException(e);
        }

    }
}

