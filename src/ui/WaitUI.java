package ui;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;

public class WaitUI extends JDialog {
	private static final long serialVersionUID = 3240251848681989560L;
	private JLabel lblInfo;

	public WaitUI() {
		setUndecorated(true);
		setBounds(100, 100, 450, 250);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setModal(true);
		
		JLabel lblNewLabel = new JLabel("hwFeed");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
		lblNewLabel.setForeground(Color.PINK);
		lblNewLabel.setBounds(10, 11, 120, 43);
		getContentPane().add(lblNewLabel);
		
		lblInfo = new JLabel("");
		lblInfo.setForeground(Color.PINK);
		lblInfo.setBounds(10, 225, 430, 14);
		getContentPane().add(lblInfo);
		
		JLabel lblBackground = new JLabel(new ImageIcon(this.getClass().getClassLoader().getResource("wait-ui-bg.jpg")));
		lblBackground.setBounds(0, 0, 450, 250);
		getContentPane().add(lblBackground);

	}

	public void setInformation(String s) {
		this.lblInfo.setText(s);
	}
}
