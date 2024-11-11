import java.io.*;
import java.net.*;

public class Client implements Runnable {
    private final String ipAddress;
    private final int port;
    private boolean exit = false;

    public Client(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void run() {
        while (!exit) {
            try (Socket socket = new Socket(ipAddress, port)) {
                System.out.println("Connected to server");
                BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                while (!exit) {
                    String input = MultiClient.getCurrentInput();
                    //System.out.println("Client sending input: " + input);

                    long startTime;
                    if ("7".equals(input)) {
                        writer.println("stop");
                        exit = true;
                        break;
                    } else {
                        startTime = System.currentTimeMillis();
                        writer.println(input);
                    }
                    String serverResponse;
                    while ((serverResponse = receive.readLine()) != null) {
                        if (serverResponse.equals("stop")) {
                            break;
                        }
                        System.out.println(serverResponse);
                    }
                    long endTime = System.currentTimeMillis();
                    System.out.println(endTime - startTime + "ms");
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
        }
    }
}

