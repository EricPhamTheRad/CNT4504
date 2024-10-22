import java.io.*;
import java.net.*;
public class Client {
    public static void main(String[] args) throws IOException {

        try(Socket socket = new Socket("139.62.210.155", 2222)) {
            while(true) {
                BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String data = receive.readLine();
                System.out.println(data);
            }
        }
    }
}
