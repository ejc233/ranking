import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Top-level class that accepts input and output as command-line arguments and
 * handles them appropriately.
 * 
 * @author echan
 */
public class Interpreter {
	public static String inputFile;
	public static String outputFile;
	public static boolean leftPicked = false;
	public static boolean buttonPressed = false;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		inputFile = args[0];
		outputFile = args[1];
		try {
			BufferedReader inputReader = new BufferedReader(new FileReader(inputFile));

			String line = null;
			LinkedList orderedList = new LinkedList();

			Scanner sc = new Scanner(System.in);

			try {
				while (!(line = inputReader.readLine()).equals("START")) {
					orderedList.add(line);
				}

			} catch (NullPointerException e) {
				System.out.println("No START found.");
				inputReader = new BufferedReader(new FileReader(inputFile));
			}

			while ((line = inputReader.readLine()) != null) {
				updateList(orderedList, line, sc);
			}

			inputReader.close();
			sc.close();

			// Write the ranked list into the output file
			writeFile(orderedList, outputFile);

			System.out.println("Write complete.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update the list with a new line.
	 * 
	 * @param orderedList
	 *            the list being updated
	 * @param line
	 *            the line being added to the list
	 * @param sc
	 * @throws InterruptedException
	 */
	private static void updateList(LinkedList orderedList, String line, Scanner sc) throws InterruptedException {
		if (!orderedList.isEmpty()) {
			int low = 0;
			int high = orderedList.getSize() - 1;

			while (low <= high) {
				int mid = low + (high - low) / 2;

				Selector sel = new Selector(orderedList.get(mid), line);

				// old line is preferred
				if (sel.getLeftPicked()) {
					low = mid + 1;

				} else {
					high = mid - 1;
				}
			}

			orderedList.add(line, low);
			System.out.println("The ranking now has " + orderedList.getSize() + " items.");

		} else {
			orderedList.add(line);
			System.out.println("The ranking now has 1 item.");

		}
	}

	/**
	 * Write the ordered list to a file.
	 * 
	 * @param orderedList
	 *            the ordered list
	 * @param output
	 *            the output file path
	 * @throws IOException
	 */
	public static void writeFile(LinkedList orderedList, String output) throws IOException {
		File fout = new File(output);
		FileOutputStream fos = new FileOutputStream(fout);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

		// Write each string in the ranked list one by one
		for (int i = 0; i < orderedList.getSize(); i++) {
			writer.write(orderedList.get(i));
			if (i != orderedList.getSize() - 1) {
				writer.newLine();
			}
		}

		writer.close();
	}

}