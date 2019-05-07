package Floor;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Floor extends JPanel {
	public static final int totalFloor = 20;
	
	private JButton []upButton = new JButton[totalFloor + 1];
	private JButton []downButton = new JButton[totalFloor + 1];
	
	public Floor() {
		this.setLayout(new GridLayout(totalFloor, 3, 2, 2));
		
		for (int i = totalFloor; i >= 1; --i) {
			JLabel floorLabel = new JLabel(i + "F");
			floorLabel.setHorizontalAlignment(JLabel.CENTER);
			Font font = floorLabel.getFont();
			floorLabel.setFont(new Font(font.getFontName(), font.getStyle(), 15));
			this.add(floorLabel);
			
			upButton[i] = new JButton(i + "↑");
			upButton[i].setMargin(new Insets(1, 1, 1, 1));
			upButton[i].setFocusPainted(false);
			upButton[i].setBackground(Color.white);
			upButton[i].setFont(new Font(font.getFontName(), font.getStyle(), 15));
			upButton[i].addActionListener(upButtonListrner);
			if (i == totalFloor) {
				upButton[i].setEnabled(false);
			}
			this.add(upButton[i]);
			
			downButton[i] = new JButton(i + "↓");
			downButton[i].setMargin(new Insets(1, 1, 1, 1));
			downButton[i].setFocusPainted(false);
			downButton[i].setBackground(Color.white);
			downButton[i].setFont(new Font(font.getFontName(), font.getStyle(), 15));
			downButton[i].addActionListener(downButtonListrner);
			if (i == 1) {
				downButton[i].setEnabled(false);
			}
			this.add(downButton[i]);
		}
	}
	
	ActionListener upButtonListrner = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
	};
	
	ActionListener downButtonListrner = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
	};
}
