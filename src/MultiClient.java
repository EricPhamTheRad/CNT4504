import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class MultiClient {
    /*
    private static volatile String currentInput; // Shared input variable
    private static final Object lock = new Object(); // Lock for synchronizing input access
    private static boolean exit = false;
    private static volatile boolean serverResponded = false; // Flag to control prompt timing
    */

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get server details
        System.out.println("Please input IP address:");
        String ipAddress = scanner.nextLine();
        System.out.println("Please input Port:");
        int port = Integer.parseInt(scanner.nextLine());
        //scanner.nextLine(); // Consume newline

        //prompt for the number of clients
        System.out.println("Please input number of clients (1, 5, 10, 15, 20, 25): ");
        int numClients = Integer.parseInt(scanner.nextLine());
        /*

        System.out.println("IP Address: " + ipAddress + " Port: " + port);

        Failed multi client code.
        System.out.print("Enter the number of clients: ");
        int clientNumber = scanner.nextInt();
        List<Client> clients = new ArrayList<>();
        List<Thread> clientThreads = new ArrayList<>();
        for (int i = 0; i < clientNumber; i++) {
            Client client = new Client("139.62.210.155", 2222, i);
            clients.add(client);
            Thread thread = new Thread(client, "" + i);
            clientThreads.add(thread);
        }



        // Start Client thread
        //Runnable client = new Client("139.62.210.155", 2222);
        Runnable client = new Client(ipAddress, port);

        Thread clientThread1 = new Thread(client, "1");
        clientThread1.start();
        */

        printInputRequest();
        String operation = scanner.nextLine();
        //validate operation input
        while (!isValidOperation(operation)) {
            System.out.println("Invalid input. Please enter a number between 1 and 6.");
            printInputRequest();
            operation = scanner.nextLine();
        }

        //create and start threads
        List<Thread> clients = new ArrayList<>();
        for (int i = 0; i < numClients; i++) {
            Client client = new Client(ipAddress, port, operation, i +1);
            Thread clienThread = new Thread(client);
            clients.add(clienThread);
            clienThread.start();
        }

        // Wait for all client threads to finish
        for (Thread thread : clients) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

        //after all clients have finished, calculate total and average turnaround times
        long totalTurnaroundTime = Client.getTotalTurnaroundTime();
        double averageTurnaroundTime = totalTurnaroundTime / (double) numClients;

        System.out.println("\nTotal Turnaround Time: " + totalTurnaroundTime + " ms");
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime + " ms");

        scanner.close();
    }

    /*
    // Main input loop
        while (!exit) {
            synchronized (lock) {
                // Wait for the client to consume the previous input
                printInputRequest();
                currentInput = scanner.nextLine();
                lock.notifyAll(); // Notify all client threads of a new input

                while(!serverResponded) {
                    try{
                        lock.wait(); // Wait until clients have responded
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                serverResponded = false; // Reset flag for the next iteration
            }
            if ("7".equals(currentInput)) {
                exit = true;
            }
        }

        scanner.close();
    }



    public static String getCurrentInput() {
        synchronized (lock) {
            String input = currentInput;
            currentInput = null; // Reset for next input
            return input;
        }
    }
    */

    private static void printInputRequest() {
        System.out.println("Type a number to select a request: ");
        System.out.println("1: Date and Time on Server");
        System.out.println("2: Server Uptime");
        System.out.println("3: Server Memory Usage");
        System.out.println("4: Netstat");
        System.out.println("5: Current Users on the Server");
        System.out.println("6: Running Processes");
    }

    /*
    // Method to be called by Client after it finishes printing the server response
    public static void notifyServerResponse() {
        synchronized (lock) {
            serverResponded = true;
            lock.notifyAll(); // Wake up the main thread
        }
    }
    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

     */

    private static boolean isValidOperation(String input) {
        try {
            int op = Integer.parseInt(input);
            return op >= 1 && op <= 6;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

