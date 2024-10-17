import java.io.*;
import java.net.*;
public class Server {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(2222);
        Socket socket = serverSocket.accept();

        BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String data = receive.readLine();
        System.out.println(data);

        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println("This is a message sent to the server");

    }
}
