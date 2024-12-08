import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MultiClient {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Please Input IP Address:"); //prompt for server ip
            String ipAddress = scanner.nextLine(); //read server ip

            System.out.println("please input port:"); //prompt for server port
            int port = scanner.nextInt(); //read server port

            int numClients;
            while (true) {
                System.out.println("Please Input Number of Clients (1, 5, 10, 15, 20, 25, 100)::"); //prompt for number of clients
                numClients = scanner.nextInt(); //read client count
                if (numClients >= 1 && numClients <= 100) {
                    break; //exit loop if valid input
                }
                System.out.println("invalid number of clients. enter a number between 1 and 100."); //handle invalid input
            }

            long[] turnaroundTimes = new long[numClients]; //array to store turnaround times
            Thread[] threads = new Thread[numClients];
            for (int i = 0; i < numClients; i++) {
                final int clientId = i + 1; //assign client id

                System.out.println("\nsetting up client " + clientId); //log client setup
                System.out.println("type a number to select a request:"); //prompt for request type
                System.out.println("1: Date and Time on Server");
                System.out.println("2: Server Uptime");
                System.out.println("3: Server Memory Usage");
                System.out.println("4: Netstat");
                System.out.println("5: Current Users on The Server");
                System.out.println("6: Running Processes");

                int requestType = scanner.nextInt(); //read request type

                threads[i] = new Thread(() -> {
                    try {
                        Socket socket = new Socket(ipAddress, port);
                        Client client = new Client(socket, clientId, requestType);
                        long turnaroundTime = client.runClient();
                        turnaroundTimes[clientId - 1] = turnaroundTime;
                    } catch (IOException e) {
                        System.out.println("client " + clientId + " failed to connect: " + e.getMessage());
                    }
                });
                threads[i].start();
            }

            try {
                Thread.sleep(5000); //wait for all threads to complete
            } catch (InterruptedException e) {
                e.printStackTrace(); //handle interruption
            }

            long totalTurnaroundTime = 0; //initialize total time
            for (long time : turnaroundTimes) {
                totalTurnaroundTime += time; //sum turnaround times
            }
            double averageTurnaroundTime = (double) totalTurnaroundTime / numClients; //calculate average turnaround time

            System.out.println("\nTotal Turnaround Time: " + totalTurnaroundTime + " ms"); //display total time
            System.out.println("Average Turnaround Time: " + averageTurnaroundTime + " ms"); //display average time
        }
    }
}
