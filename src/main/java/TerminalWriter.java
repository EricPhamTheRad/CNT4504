import java.io.PrintWriter;
import java.util.Scanner;

public class TerminalWriter implements Runnable {
    private boolean end = false;

    PrintWriter writer;
    public TerminalWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(!end){
            String input = scanner.next();
            if (input.contains("end")){
                end = true;
                writer.println("end");
            }
            else if(input.equals("request")){
                writer.println("requesting");
            }
            writer.println("requesting");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Thread Ended");
        scanner.close();
    }
}
