import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class MultiClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //get server details
        System.out.println("Please input IP address:");
        String ipAddress = scanner.nextLine();
        System.out.println("Please input Port:");
        int port = Integer.parseInt(scanner.nextLine());

        //prompt for number of clients
        System.out.println("Please input number of clients (1, 5, 10, 15, 20, 25): ");
        int numClients = Integer.parseInt(scanner.nextLine());

        while (true) {
            printInputRequest();
            String operation = scanner.nextLine();

            if ("0".equals(operation)) {
                System.out.println("Exiting program.");
                break;
            }

            //validate operation input
            while (!isValidOperation(operation)) {
                System.out.println("Invalid input. Please enter a number between 1 and 6.");
                printInputRequest();
                operation = scanner.nextLine();
            }

            //create and start threads
            List<Thread> clients = new ArrayList<>();
            for (int i = 0; i < numClients; i++) {
                Client client = new Client(ipAddress, port, operation, i + 1);
                Thread clientThread = new Thread(client);
                clients.add(clientThread);
                clientThread.start();
            }

            //wait for all client threads to finish
            for (Thread thread : clients) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }

            //display total and average turnaround times
            long totalTurnaroundTime = Client.getTotalTurnaroundTime();
            double averageTurnaroundTime = totalTurnaroundTime / (double) numClients;
            System.out.println("\nTotal Turnaround Time: " + totalTurnaroundTime + " ms");
            System.out.println("Average Turnaround Time: " + averageTurnaroundTime + " ms");
        }

        scanner.close();
    }

    private static void printInputRequest() {
        System.out.println("Type a number to select a request: ");
        System.out.println("1: Date and Time on Server");
        System.out.println("2: Server Uptime");
        System.out.println("3: Server Memory Usage");
        System.out.println("4: Netstat");
        System.out.println("5: Current Users on the Server");
        System.out.println("6: Running Processes");
        System.out.println("0: Exit");
    }

    private static boolean isValidOperation(String input) {
        try {
            int op = Integer.parseInt(input);
            return op >= 1 && op <= 6;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
