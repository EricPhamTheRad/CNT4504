import java.io.PrintWriter;
import java.util.Scanner;

public class Test implements Runnable {
    private boolean end = false;

    PrintWriter writerer;
    public Test(PrintWriter writer) {
        writerer = writer;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(!end){
            String input = scanner.next();
            if (input.contains("end")){
                end = true;
                writerer.println("end");
            }
            else if(input.equals("request")){
                writerer.println("requesting");
            }
        }
        System.out.println("Thread Ended");
        scanner.close();
    }
}
