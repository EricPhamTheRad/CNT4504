import java.io.*;
import java.net.*;
public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        //use to terminate program
        boolean exit = false;
        while (!exit) {
            //try to access server at IP and socket
            try (Socket socket = new Socket("139.62.210.155", 2222)) {
                //Sets up reading the socket if sucsessful
                BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //code to terminate program remotely un comment to activate
                //while (!exit) {
                //read data sent from server
                    String data = receive.readLine();
                    //if (data.contains("1")) {
                       // exit = true;
                    //}
                    System.out.println(data);
                //}

            } catch (UnknownHostException ex) {

                System.out.println("Server not found: " + ex.getMessage());

            } catch (IOException ex) {

                System.out.println("I/O error: " + ex.getMessage());
            }
            Thread.sleep(1000);
        }

    }
}
