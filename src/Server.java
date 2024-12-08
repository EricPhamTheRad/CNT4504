import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 4451;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started and listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected to client: " + clientSocket.getRemoteSocketAddress());

                // Spawn a new thread for each client
                Thread clientThread = new Thread(() -> handleClientRequest(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    private static void handleClientRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            int requestType = Integer.parseInt(in.readLine());
            System.out.println("Processing request type: " + requestType);

            String response;
            switch (requestType) {
                case 1:
                    response = executeCommand("date");
                    break;
                case 2:
                    response = executeCommand("uptime");
                    break;
                case 3:
                    response = executeCommand("free -m");
                    break;
                case 4:
                    response = executeCommand("netstat");
                    break;
                case 5:
                    response = executeCommand("who");
                    break;
                case 6:
                    response = executeCommand("ps -e");
                    break;
                default:
                    response = "Invalid request type: " + requestType;
                    break;
            }

            out.println(response);
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    private static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            output.append("Error executing command: ").append(e.getMessage());
        }
        return output.toString();
    }
}
