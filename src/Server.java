import java.io.*;
import java.net.*;
public class Server {
    public static void main(String[] args) throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(2222)) {
            System.out.println("Server is listening on port 2222");
            while(true) {
                Socket socket = serverSocket.accept();

                //BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //String data = receive.readLine();
                //System.out.println(data);

                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("This is a message sent from the server");
                Thread.sleep(1000);
            }
        } catch (IOException e){
            System.out.println("Server Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
