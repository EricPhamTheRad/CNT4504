import java.io.*;
import java.net.*;
// For reading user input
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
                //Currently uses manual typing to request data
                //TODO: uses loop to send request and wait for data to be receive
                if(socket.isClosed()){
                    System.out.println("Socket Closed");
                }
                //wait 1 second
                System.out.println("requesting data");
                writer.println("requesting");
                Thread.sleep(1000);
                //Read terminal for string data
                data = receive.readLine();
                //checks if Client closed
                if(data.equals("closed")){
                    exit = true;
                }
                //Prints what is received
                //TODO: Put data into objects for GUI
                //TODO: Setup data storage in CSV or other file type
                else {
                    System.out.print("Data received: ");
                    System.out.println(data);
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

