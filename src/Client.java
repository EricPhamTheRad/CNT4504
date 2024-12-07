import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket; //socket instance for communication
    private int clientId; //unique client id
    private int requestType; //type of request

    public Client(Socket socket, int clientId, int requestType) {
        this.socket = socket; //initialize socket
        this.clientId = clientId; //initialize client id
        this.requestType = requestType; //initialize request type
    }

    public long runClient() {
        long turnaroundTime = 0; //initialize turnaround time

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("client " + clientId + " connected to server."); //log connection

            long startTime = System.currentTimeMillis(); //record start time

            out.println(requestType); //send request to server

            String response; //variable to store response
            System.out.println("client " + clientId + " received response:"); //log response reception
            while ((response = in.readLine()) != null) { //read response
                System.out.println(response); //print response
            }

            long endTime = System.currentTimeMillis(); //record end time
            turnaroundTime = endTime - startTime; //calculate turnaround time

        } catch (IOException e) {
            System.out.println("error with client " + clientId + ": " + e.getMessage()); //log error
        }

        return turnaroundTime; //return turnaround time
    }
}
