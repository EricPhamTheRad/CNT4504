import java.io.*;
import java.net.*;
//import java.util.Date;
public class Server {
    public static void main(String[] args) {

        //create socket for server at port ""
        int port = 4351;
        System.out.println("Server is listening on port: " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            //waits for client to connect
            while(true) {
                //waits for client to connect
                Socket socket = serverSocket.accept();
                System.out.println("Connected to client");

                //handle client in a separate thread (if you want to handle multiple clients concurrently)
                //for an iterative server, we handle one client at a time in this loop
                handleClient(socket);
            }
        } catch (IOException e) {
            System.out.println("Server Exception: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) {
        try (
                BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String data = receive.readLine();
            int selection;
            try {
                selection = Integer.parseInt(data);
            } catch (NumberFormatException e) {
                writer.println("Invalid input. Please enter a number between 1 and 6");
                writer.println("stop");
                return;
            }

            System.out.println("Received request: " + data);

            //command execution based on user input
            switch (selection) {
                case 1:
                    executeAndSendCommand(writer, "date");
                    break;
                case 2:
                    executeAndSendCommand(writer, "uptime");
                    break;
                case 3:
                    executeAndSendCommand(writer, "free -m");
                    break;
                case 4:
                    executeAndSendCommand(writer,"netstat");
                    break;
                case 5:
                    executeAndSendCommand(writer,"who");
                    break;
                case 6:
                    executeAndSendCommand(writer,"ps -aux");
                    break;
                default:
                    writer.println("Invalid request. Please enter a number between 1 and 6.");
                    writer.println("stop");
                    break;
            }

            //close the socket after handling the client
            socket.close();
        } catch (IOException e) {
            System.out.println("Server Exception: " + e.getMessage());
        }
    }

    //method to execute the system commands and return output as String
    private static void executeAndSendCommand(PrintWriter writer, String command) {
        //StringBuilder output = new StringBuilder();
        try{
            Process process = Runtime.getRuntime().exec(command);

            //read the command input
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

            //read the input
            String line;
            while ((line = input.readLine()) != null){
                writer.println(line);
            }

            //read any errors
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while((line = error.readLine()) != null){
                writer.println("Error: " + line);
            }
        } catch (IOException e) {
            writer.println("Command execution error: " + e.getMessage());
        }finally {
            writer.println("stop");
        }
    }
}

