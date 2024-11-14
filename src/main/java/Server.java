import java.io.*;
import java.net.*;
//import java.util.Date;
//import java.util.Scanner;  // For reading user input
public class Server {
    public static void main(String[] args) throws UnknownHostException {
        //Create socket for server at port 2222
        System.out.println(InetAddress.getLocalHost());
        boolean exit = false;
        String data;
        System.out.println("Server is listening on port 2222");
        try (ServerSocket serverSocket = new ServerSocket(2222)) {
            //waits for client to connect
            Socket socket = serverSocket.accept();
            System.out.println("Connected");
            //set up way to send data to client
            BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            //code to allowing user to terminate client and server, uncomment to activate
            Runnable r  = new Test(writer);
            new Thread(r).start();

            //main while loop
            //AHHH
            if(!socket.isClosed()){
                System.out.println("Socket Open");
            }
            while(!exit) {
                //Read data from  to client
                //sets up stream to send data
                //send text
                if(socket.isClosed()){
                    System.out.println("Socket Closed");
                }
                //What
                //wait 1 second
                Thread.sleep(500);
                data = receive.readLine();
                if(data.equals("closed")){
                    exit = true;
                }
                else {
                    System.out.print("Data received: ");
                    System.out.println(data);
                }
            }

            socket.close();

        } catch (IOException e){
            System.out.println("Server Exception: " + e.getMessage());
       } catch (InterruptedException e) {
           throw new RuntimeException(e);
        }

    }
}

