import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        //use to terminate program
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        String input;
        while (!exit) {
            //try to access server at IP and socket
            try (Socket socket = new Socket("139.62.210.155", 2222)) {
                //Sets up reading the socket if sucsessful
                System.out.println("Connected");
                BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                //code to terminate program remotely un comment to activate
                while (!exit) {
                    //read data sent from server
                    input = scanner.nextLine();
                    writer.println(input);
                    if(input.contains("end")){
                        exit = true;
                    }
                    System.out.println(receive.readLine());
                }
                writer.println("closed");
                writer.close();
                receive.close();
            } catch (UnknownHostException ex) {

                System.out.println("Server not found: " + ex.getMessage());

            } catch (IOException ex) {

                System.out.println("I/O error: " + ex.getMessage());
            }
            Thread.sleep(1000);
        }

    }
}
