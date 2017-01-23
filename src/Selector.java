import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Selector extends JFrame implements ActionListener {
	JButton leftButton;
	JButton rightButton;
	boolean buttonPressed = false;
	boolean leftPicked = false;

	public Selector(String left, String right) {
		this.setTitle("Which do you prefer?");
		this.setVisible(true);
		this.setSize(400, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		this.add(panel);

		leftButton = new JButton(left);
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

	public boolean getLeftPicked() throws InterruptedException {
		while (!buttonPressed) {
			Thread.sleep(200);
		}

		this.setVisible(false);
		this.dispose();
		return leftPicked;
	}

}