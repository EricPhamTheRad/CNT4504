import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 4351; //server port
        System.out.println("Server is listening on port: " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connected to client");

                handleClient(socket); //handle each client in an iterative manner
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
                    executeAndSendCommand(writer, "netstat");
                    break;
                case 5:
                    executeAndSendCommand(writer, "who");
                    break;
                case 6:
                    executeAndSendCommand(writer, "ps -aux");
                    break;
                default:
                    writer.println("Invalid request. Please enter a number between 1 and 6.");
                    writer.println("stop");
                    break;
            }
            socket.close(); //close the client connection after handling the request
        } catch (IOException e) {
            System.out.println("Server Exception: " + e.getMessage());
        }
    }

    private static void executeAndSendCommand(PrintWriter writer, String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                writer.println(line);
            }

            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = error.readLine()) != null) {
                writer.println("Error: " + line);
            }
        } catch (IOException e) {
            writer.println("Command execution error: " + e.getMessage());
        } finally {
            writer.println("stop"); //indicate end of response
        }
    }
}
