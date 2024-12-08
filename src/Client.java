import java.io.*;
import java.net.Socket;

public class Client {
    private final Socket socket;
    private final int clientId;
    private final int requestType;

    public Client(Socket socket, int clientId, int requestType) {
        this.socket = socket;
        this.clientId = clientId;
        this.requestType = requestType;
    }

    public long runClient() {
        long turnaroundTime = 0;

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Client " + clientId + " connected to server.");

            long startTime = System.currentTimeMillis();

            out.println(requestType);

            String response;
            System.out.println("Client " + clientId + " received response:");
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }

            long endTime = System.currentTimeMillis();
            turnaroundTime = endTime - startTime;

        } catch (IOException e) {
            System.err.println("Error with client " + clientId + ": " + e.getMessage());
        }

        return turnaroundTime;
    }
}
