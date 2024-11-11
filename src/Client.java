import java.io.*;
import java.net.*;

public class Client implements Runnable {
    private final String ipAddress;
    private final int port;
    private final String operation;
    private final int clientId;
    private static long totalTurnaroundTime = 0;

    public Client(String ipAddress, int port, String operation, int clientId) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.operation = operation;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        /*while (!exit) {
            try (Socket socket = new Socket(ipAddress, port)) {
                BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                System.out.println("Connected to server");

                while (!exit) {
                    String input = MultiClient.getCurrentInput();
                    //System.out.println("Client sending input: " + input);

                    long startTime = System.currentTimeMillis();
                    if ("7".equals(input)) {
                        writer.println("stop");
                        exit = true;
                        break;
                    } else {
                        writer.println(input);
                    }

                    String serverResponse;
                    while ((serverResponse = receive.readLine()) != null) {
                        if ("stop".equals(serverResponse)) {
                            break;
                        }
                        System.out.println(serverResponse);
                    }

                    long endTime = System.currentTimeMillis();
                    System.out.println("Turnaround Time: " + (endTime - startTime + "ms"));

                    MultiClient.notifyServerResponse();
                }

                writer.println("closed");
            } catch (IOException e) {
                System.out.println("Connection error: " + e.getMessage());
            }

            try {
                Thread.sleep(1000); // Delay before retrying connection
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }*/
        try (Socket socket = new Socket(ipAddress, port);
             BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Client " + clientId + " connected to server.");

            long startTime = System.currentTimeMillis();

            // Send the operation request to the server
            writer.println(operation);

            // Read the server response
            StringBuilder response = new StringBuilder();
            String serverResponse;
            while ((serverResponse = receive.readLine()) != null) {
                if ("stop".equalsIgnoreCase(serverResponse)) {
                    break;
                }
                response.append(serverResponse).append("\n");
            }

            long endTime = System.currentTimeMillis();
            long turnaroundTime = endTime - startTime;

            // Update total turnaround time (synchronized to prevent race conditions)
            addToTotalTurnaroundTime(turnaroundTime);

            // Display the server response and turnaround time
            System.out.println("Client " + clientId + " received response:\n" + response);
            System.out.println("Client " + clientId + " Turnaround Time: " + turnaroundTime + " ms");

        } catch (IOException e) {
            System.out.println("Client " + clientId + " connection error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String ipAddress = "139.62.210.155";
        int port = 3311;
        String operation = "1";

        // Create a Client instance and run it in a thread
        Client client = new Client(ipAddress, port, operation, 1);
        new Thread(client).start();
    }

    // Synchronized method to safely update total turnaround time
    private static synchronized void addToTotalTurnaroundTime(long turnaroundTime) {
        totalTurnaroundTime += turnaroundTime;
    }

    // Getter for total turnaround time
    public static long getTotalTurnaroundTime() {
        return totalTurnaroundTime;
    }
}
