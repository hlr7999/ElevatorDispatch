package App;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Elevator.Elevator;
import Floor.Floor;

public class App extends JFrame {
	private JPanel panel = new JPanel();
	private Elevator []elevator = new Elevator[Elevator.totalElevator];
	private Floor floor = new Floor(elevator);
	
    public App() {
    	panel.setLayout(new GridLayout(1, Elevator.totalElevator+1, 20, 0));
    	panel.add(floor);
    	
    	for (int i = 0; i < Elevator.totalElevator; ++i) {
    		elevator[i] = new Elevator(floor);
    		elevator[i].add(panel);
    	}
    	
    	this.add(panel);
    	setTitle("Elevator Dispatch");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setSize(1000, 750);
    	setResizable(false);
    	setVisible(true);
    }
    
    public static void main(String[] args) {
    	new App();
    }
}
