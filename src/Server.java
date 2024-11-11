import java.io.*;
import java.net.*;
//import java.util.Date;
public class Server {
    public static void main(String[] args) {

        //create socket for server at port 2222
        boolean exit = false;
        System.out.println("Server is listening on port 2222");

        try (ServerSocket serverSocket = new ServerSocket(2222)) {

            //waits for client to connect
            while (!exit) {
                Socket socket = serverSocket.accept();
                System.out.println("Connected");

                //set up way to send data to client
                BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                String data;
                while ((data = receive.readLine()) != null) {
                    int selection;
                    try {
                        selection = Integer.parseInt(data);
                    } catch (NumberFormatException e) {
                        writer.println("Invalid input. Please enter a number between 1 and 6");
                        writer.println("Stop");
                        continue;
                    }

                    System.out.println("Received request: " + data);

                    //command execution based on user input
                    switch (selection) {
                        case 1:
                            writer.println((executeCommand("date")));
                            writer.println("stop");
                            break;
                        case 2:
                            writer.println((executeCommand("uptime")));
                            writer.println("stop");
                            break;
                        case 3:
                            writer.println(executeCommand("free -m"));
                            writer.println("stop");
                            break;
                        case 4:
                            writer.println(executeCommand("netstat"));
                            writer.println("stop");
                            break;
                        case 5:
                            writer.println(executeCommand("who"));
                            writer.println("stop");
                            break;
                        case 6:
                            writer.println(executeCommand("ps aux"));
                            writer.println("stop");
                            break;
                        default:
                            writer.println("Invalid request. Please enter a number between 1 and 6.");
                            writer.println("stop");
                            break;
                    }

                    //add a small delay between requests. wait 1 second
                    Thread.sleep(500);

                    if (socket.isClosed()) {
                        System.out.println("Socket Closed");
                        exit = true;
                        break;
                    }
                }
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Server Exception: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Server Interrupted: " + e.getMessage());
        }
    }

    //method to execute the system commands and return output as String
    private static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        try{
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            //read the input
            String line;
            while ((line = input.readLine()) != null){
                output.append(line).append("\n");
            }

            //read all the errors in case there are
            while((line = error.readLine()) != null){
                output.append("Error").append(line).append("\n");
            }
        } catch (IOException e) {
            output.append("Command execution error: ").append(e.getMessage()).append("\n");
        }
        return output.toString();
    }
}

