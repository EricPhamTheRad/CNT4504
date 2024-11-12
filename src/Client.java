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
        try (Socket socket = new Socket(ipAddress, port);
             BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Client " + clientId + " connected to server.");

            long startTime = System.currentTimeMillis();
            writer.println(operation); //send operation to server

            //collect server response
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

            //update total turnaround time safely
            addToTotalTurnaroundTime(turnaroundTime);

            //display client-specific response and time
            System.out.println("Client " + clientId + " received response:\n" + response);
            System.out.println("Client " + clientId + " Turnaround Time: " + turnaroundTime + " ms");

        } catch (IOException e) {
            System.out.println("Client " + clientId + " connection error: " + e.getMessage());
        }
    }

    private static synchronized void addToTotalTurnaroundTime(long turnaroundTime) {
        totalTurnaroundTime += turnaroundTime;
    }

    public static long getTotalTurnaroundTime() {
        return totalTurnaroundTime;
    }
}
