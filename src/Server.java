import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 4451;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server Started and Listening on Port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected To Client:" + clientSocket.getRemoteSocketAddress()); //log connection to cleint

                new Thread(() -> handleClientRequest(clientSocket)).start(); //start new thread to handle request
            }
        } catch (IOException e) {
            e.printStackTrace(); //print exception stack trace
        }
    }

    private static void handleClientRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            int request = Integer.parseInt(in.readLine()); //read the request from client
            System.out.println("processing request type: " + request); //log the request type
            String response;

            switch (request) {
                case 1: response = executeCommand("date"); break; //execute date command
                case 2: response = executeCommand("uptime"); break; //execute uptime command
                case 3: response = executeCommand("free -m"); break; //execute memory usage command
                case 4: response = executeCommand("netstat"); break; //execute network stats command
                case 5: response = executeCommand("who"); break; //execute who command
                case 6: response = executeCommand("ps -e"); break; //execute process list command
                default: response = "Invalid Request Type: " + request; break; //handle invalid request
            }

            out.println(response); //send the response to client
            System.out.println("Response Sent to Client: " + clientSocket.getRemoteSocketAddress()); //log response sent
        } catch (IOException e) {
            System.out.println("Error Handling Client Request: " + e.getMessage()); //log error
        } finally {
            try {
                clientSocket.close(); //close the client socket
            } catch (IOException e) {
                System.out.println("Error Closing Client Socket: " + e.getMessage()); //log socket close error
            }
            System.out.println("Client Disconnected: " + clientSocket.getRemoteSocketAddress()); //log cleint disconnection
        }
    }

    private static String executeCommand(String command) {
        StringBuilder output = new StringBuilder(); //initialize string builder for command output
        try {
            Process process = Runtime.getRuntime().exec(command); //execute system command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); //read command output
            String line;
            while ((line = reader.readLine()) != null) { //loop through output lines
                output.append(line).append("\n"); //append each line to output
            }
        } catch (IOException e) {
            e.printStackTrace(); //print exception stack trace
        }
        return output.toString(); //retun the collected command output
    }
}
