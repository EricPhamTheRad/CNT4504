import java.util.Scanner;

public class MultiClient {
    private static volatile String currentInput; // Shared input variable
    private static final Object lock = new Object(); // Lock for synchronizing input access
    private static boolean exit = false;
    private static volatile boolean serverResponded = false; // Flag to control prompt timing

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get server details
        System.out.println("Please input IP address:");
        String ipAddress = scanner.nextLine();
        System.out.println("Please input Port:");
        int port = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("IP Address: " + ipAddress + " Port: " + port);
        /*
         Failed multiclient code.
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

         */

        // Start Client thread
        //Runnable client = new Client("139.62.210.155", 2222);
        Runnable client = new Client(ipAddress, port);

        Thread clientThread1 = new Thread(client, "1");
        clientThread1.start();

        // Main input loop
        while (!exit) {
            synchronized (lock) {
                // Wait for the client to consume the previous input
                printInputRequest();
                boolean stop = false;
                while(!stop) {
                    currentInput = scanner.nextLine();
                    if (isNumber(currentInput)){
                        stop = true;
                    }
                }
                lock.notifyAll(); // Notify Client thread of new input

                while (!serverResponded) {
                    try {
                        lock.wait(); // Wait until the client has responded
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

    }

    public static String getCurrentInput() {
        synchronized (lock) {
            while (currentInput == null) { // Check if there's new input
                try {
                    lock.wait(); // Wait for new input
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            String input = currentInput;
            currentInput = null; // Reset for next input
            return input;
        }
    }

    private static void printInputRequest() {
        System.out.println("Type a number to select a request: ");
        System.out.println("1: Date and Time on Server");
        System.out.println("2: Server Uptime");
        System.out.println("3: Server Memory Usage");
        System.out.println("4: Netstat");
        System.out.println("5: Current Users on the Server");
        System.out.println("6: Running Processes");
        System.out.println("7: End session");
    }
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
}
