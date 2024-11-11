import java.io.*;
import java.net.*;
//import java.util.Date;
public class Server {
    public static void main(String[] args) {
        //Create socket for server at port 2222
        boolean exit = false;
        System.out.println("Server is listening on port 2222");
        try (ServerSocket serverSocket = new ServerSocket(2222)) {
            //waits for client to connect
            Socket socket = serverSocket.accept();
            System.out.println("Connected");
            //set up way to send data to client
            BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            String data;

            if(!socket.isClosed()){
                System.out.println("Socket Open");
            }
            while(!exit) {
                //Read data from  to client
                //sets up stream to send data
                //send text
                if (receive.ready() ) {
                    //System.out.println("I about to read data");
                    data = receive.readLine();

                    if (!data.isEmpty()) {
                        int selection = Integer.parseInt(data);
                        System.out.println(data);
                        switch (selection) {
                            case 1:
                                for (int i = 0; i < 10; i++) {
                                    writer.println("Data and Time");
                                }
                                writer.println("stop");
                                break;
                            case 2:
                                writer.println("Uptime");
                                writer.println("stop");
                                break;
                            case 3:
                                writer.println("Memory Use");
                                writer.println("stop");
                                break;
                            case 4:
                                writer.println("Netstat");
                                writer.println("stop");
                                break;
                            case 5:
                                writer.println("Current User");
                                writer.println("stop");
                                break;
                            case 6:
                                writer.println("Running Process");
                                writer.println("stop");
                                break;
                            default:
                                writer.println("Invalid request");
                                writer.println("stop");
                                break;
                        }
                        //wait 1 second
                        Thread.sleep(500);
                    }
                    if (socket.isClosed()) {
                        System.out.println("Socket Closed");
                        exit = true;
                    }
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

