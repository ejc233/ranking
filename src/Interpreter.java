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
			OrderedTree tree = new OrderedTree();
			ArrayList<String> orderedList = new ArrayList<String>();
			int count = 0;

			while ((line = inputReader.readLine()) != null) {
				count++;
				orderedList.add(count + ". " + line);
			}

			inputReader.close();

			// ranked = tree.toList();

			// Write the ranked list into the output file
			writeFile(orderedList, outputFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write the ordered list to a file.
	 * 
	 * @param orderedList
	 *            the ordered list
	 * @param output
	 *            the output file
	 * @throws IOException
	 */
	public static void writeFile(ArrayList<String> orderedList, String output) throws IOException {
		File fout = new File(output);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

		// Write each string in the ranked list one by one
		for (int i = 0; i < orderedList.size(); i++) {
			writer.write(orderedList.get(i));
			writer.newLine();
		}

		writer.close();
	}

}