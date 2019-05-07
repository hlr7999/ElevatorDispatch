package Floor;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Floor extends JPanel {
	public static final int totalFloor = 20;
	public static JButton []upButton = new JButton[totalFloor];
	public static JButton []downButton = new JButton[totalFloor];
	public Floor() {
		this.setLayout(new GridLayout(totalFloor, 3, 2, 2));
		for (int i = totalFloor-1; i >= 0; --i) {
			JLabel floorLabel = new JLabel((i+1)+"F");
			floorLabel.setHorizontalAlignment(JLabel.CENTER);
			Font font = floorLabel.getFont();
			floorLabel.setFont(new Font(font.getFontName(), font.getStyle(), 15));
			this.add(floorLabel);
			upButton[i] = new JButton((i+1)+"↑");
			upButton[i].setMargin(new Insets(1, 1, 1, 1));
			upButton[i].setBackground(Color.white);
			upButton[i].setFont(new Font(font.getFontName(), font.getStyle(), 15));
			if (i == totalFloor-1) {
				upButton[i].setEnabled(false);
			}
			this.add(upButton[i]);
			downButton[i] = new JButton((i+1)+"↓");
			downButton[i].setMargin(new Insets(1, 1, 1, 1));
			downButton[i].setBackground(Color.white);
			downButton[i].setFont(new Font(font.getFontName(), font.getStyle(), 15));
			floorLabel.setFont(new Font(floorLabel.getFont().getFontName(), 
                    floorLabel.getFont().getStyle(), 15));
			if (i == 0) {
				downButton[i].setEnabled(false);
			}
			this.add(downButton[i]);
		}
	}
}
