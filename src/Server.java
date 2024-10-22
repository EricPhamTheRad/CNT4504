import java.io.*;
import java.net.*;
//import java.util.Date;
import java.util.Scanner;  // For reading user input
public class Server {
    public static void main(String[] args) {
        //Create socket for server at port 2222
        boolean exit = false;
        try (ServerSocket serverSocket = new ServerSocket(2222)) {
            System.out.println("Server is listening on port 2222");
            //waits for client to connect
            Socket socket = serverSocket.accept();
            //set up way to send data to client
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            //code to allowing user to terminate client and server, uncomment to activate
            //Runnable r  = new Test(writer,exit);
            //new Thread(r).start();

            //main while loop
            while(!exit) {
                //Read data from  to client
                //BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //String data = receive.readLine();
                //System.out.println(data);

                //sets up stream to send data
                //send text
                writer.println("This message was sent from the server");

                //wait 1 second
                Thread.sleep(500);
            }
            socket.close();
        } catch (IOException e){
            System.out.println("Server Exception: " + e.getMessage());
            e.printStackTrace();
       } catch (InterruptedException e) {
           throw new RuntimeException(e);
        }

    }
}

