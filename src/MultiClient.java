import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MultiClient {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter server IP:");
            String ipAddress = scanner.nextLine();

            System.out.println("Enter server port:");
            int port = scanner.nextInt();

            int numClients;
            while (true) {
                System.out.println("Enter number of clients (1 to 100):");
                numClients = scanner.nextInt();
                if (numClients >= 1 && numClients <= 100) break;
                System.out.println("Invalid input. Please enter a number between 1 and 100.");
            }

            long[] turnaroundTimes = new long[numClients];
            Thread[] threads = new Thread[numClients];

            for (int i = 0; i < numClients; i++) {
                final int clientId = i + 1;
                System.out.println("Enter request type for client " + clientId + " (1 to 6):");
                int requestType = scanner.nextInt();

                threads[i] = new Thread(() -> {
                    try (Socket socket = new Socket(ipAddress, port)) {
                        Client client = new Client(socket, clientId, requestType);
                        turnaroundTimes[clientId - 1] = client.runClient();
                    } catch (IOException e) {
                        System.err.println("Client " + clientId + " failed: " + e.getMessage());
                    }
                });
                threads[i].start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                try {
                    if (thread != null) thread.join();
                } catch (InterruptedException e) {
                    System.err.println("Error waiting for client thread: " + e.getMessage());
                }
            }

            // Calculate total and average turnaround time
            long totalTurnaroundTime = 0;
            for (long time : turnaroundTimes) {
                totalTurnaroundTime += time;
            }
            double averageTurnaroundTime = (double) totalTurnaroundTime / numClients;

            System.out.println("Total Turnaround Time: " + totalTurnaroundTime + " ms");
            System.out.println("Average Turnaround Time: " + averageTurnaroundTime + " ms");
        }
    }
}
