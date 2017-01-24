import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Frame class that represents two-button window for picking preferred object.
 * 
 * @author echan
 */
@SuppressWarnings("serial")
public class Selector extends JFrame implements ActionListener {
	JButton leftButton;
	JButton rightButton;
	boolean buttonPressed = false;
	boolean leftPicked = false;

	/**
	 * Constructor.
	 * 
	 * @param mid
	 *            the ranked of the already ranked object
	 * @param left
	 *            the already ranked object
	 * @param right
	 *            the object being added to the ranking
	 */
	public Selector(int mid, String left, String right) {
		this.setTitle("Which do you prefer?");
		this.setVisible(true);
		this.setSize(400, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		this.add(panel);

		leftButton = new JButton(mid + 1 + ". " + left);
		panel.add(leftButton);
		leftButton.addActionListener(this);
		rightButton = new JButton(right);
		panel.add(rightButton);
		rightButton.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == leftButton) {
			leftPicked = true;
		}

		buttonPressed = true;
	}

	/**
	 * Get whether the left object was picked.
	 * 
	 * @return whether the left object was picked
	 * 
	 * @throws InterruptedException
	 */
	public boolean getLeftPicked() throws InterruptedException {
		while (!buttonPressed) {
			Thread.sleep(200);
		}

		this.setVisible(false);
		this.dispose();
		return leftPicked;
	}

}