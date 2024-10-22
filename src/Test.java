import java.io.PrintWriter;
import java.util.Scanner;

public class Test implements Runnable {
    private boolean end = false;
    private boolean exit = false;

    PrintWriter writerer;
    public Test(PrintWriter writer, boolean exit) {
        writerer = writer;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(!end){
            if (scanner.next().contains("1")){
                end = true;
                exit = true;
                writerer.println("1");
            }
        }
    }
}
