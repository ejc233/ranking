import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Top-level class that accepts input and output as command-line arguments and
 * handles them appropriately.
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
			ArrayList<String> ranked = new ArrayList<String>();
			int count = 0;

			while ((line = inputReader.readLine()) != null) {
				count++;
				ranked.add(count + ". " + line);
			}
			System.out.println("Ranks built.");

			inputReader.close();
			writeFile(outputFile, ranked);
			System.out.println("File written.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(String output, ArrayList<String> ranked) throws IOException {
		File fout = new File(output);
		FileOutputStream fos = new FileOutputStream(fout);

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

		for (int i = 0; i < ranked.size(); i++) {
			writer.write(ranked.get(i));
			writer.newLine();
		}

		writer.close();
	}

}