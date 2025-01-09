import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The Class FileHandler.
 */
public class FileHandler {

    /** The survey file name. */
    private static String dataFile = "DataResults.csv";

    /** The file output object. */
    private static FileWriter fileOutput;

    /** The print writer object. */
    private static PrintWriter printWriter;

    /**
     * Instantiates a new file handler.
     */
    public FileHandler(){
        try {
            fileOutput = new FileWriter(dataFile);
            printWriter = new PrintWriter(fileOutput);
            printWriter.println("DateTime,X-Axis,Y-Axis,Z-Axis");
            printWriter.close();
        } catch (IOException e) {
            System.out.println("File Not Found or can not access or create file");
        }

    }

    /**
     * Appends results to a csv file.
     *
     * @param surveyData is the data to be written to the csv file, data should be comma separated
     */
    public void writeResults(String surveyData) {
        try {
            fileOutput = new FileWriter(dataFile, true);
        } catch (IOException e) {
            System.out.println("File Not Found or can not access or create file");
        }
        printWriter = new PrintWriter(fileOutput);
        printWriter.println(surveyData);
        printWriter.close();

    }
}
