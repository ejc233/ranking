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
import javax.swing.JLabel;
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
	 */
	private static void updateList(LinkedList orderedList, String line, Scanner sc) {
		if (!orderedList.isEmpty()) {
			int low = 0;
			int high = orderedList.getSize() - 1;

			while (low <= high) {
				int mid = low + (high - low) / 2;

				JFrame frame = new JFrame("Which do you prefer?");
				frame.setVisible(true);
				frame.setSize(400, 100);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				JPanel panel = new JPanel();
				frame.add(panel);
				JButton leftButton = new JButton(orderedList.get(mid));
				panel.add(leftButton);
				leftButton.addActionListener(new Action1());
				JButton rightButton = new JButton(line);
				panel.add(rightButton);
				rightButton.addActionListener(new Action2());

				System.out.println(
						"Do you prefer " + orderedList.get(mid) + " or " + line + "? (1 for left, 2 for right)");

				String choice = sc.nextLine();

				// old line is preferred
				if (choice.equals("1")) {
					low = mid + 1;

				} else if (choice.equals("2")) {
					high = mid - 1;
				} else {
					System.out.println("Invalid input. Please try again.");
				}
			}

			orderedList.add(line, low);

		} else {
			orderedList.add(line);
		}
	}

	static class Action1 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFrame frame2 = new JFrame("Clicked");
			frame2.setVisible(true);
			frame2.setSize(200, 200);
			JLabel label = new JLabel("you clicked me");
			JPanel panel = new JPanel();
			frame2.add(panel);
			panel.add(label);
		}
	}

	static class Action2 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFrame frame3 = new JFrame("OKNO 3");
			frame3.setVisible(true);
			frame3.setSize(200, 200);

			JLabel label = new JLabel("kliknales");
			JPanel panel = new JPanel();
			frame3.add(panel);
			panel.add(label);
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