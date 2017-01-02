import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Top-level class that accepts inputdir and outdir as command-line arguments
 * and handles them appropriately.
 * 
 * @author echan
 */
public class Interpreter {
	public static String inputFile;
	public static String outputFile;

	public static void main(String[] args) {
		inputFile = args[0];
		outputFile = args[1];
		try {
			BufferedReader inputReader = new BufferedReader(new FileReader(inputFile));

			String line = null;
			while ((line = inputReader.readLine()) != null) {
				System.out.println(line + " hi");
			}

			inputReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}