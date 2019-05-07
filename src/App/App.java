package App;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.Timer;
import java.util.TimerTask;

import Elevator.Elevator;
import Floor.Floor;

public class App extends JFrame {
	private JPanel panel = new JPanel();
	private Floor floor = new Floor();
	Timer []timers = new Timer[Elevator.totalElevator];
	Elevator []elevator = new Elevator[Elevator.totalElevator];
	
    public App() {
    	panel.setLayout(new GridLayout(1, Elevator.totalElevator+1, 20, 0));
    	panel.add(floor);
    	
    	for (int i = 0; i < Elevator.totalElevator; ++i) {
    		elevator[i] = new Elevator();
    		elevator[i].add(panel);
    		timers[i] = new Timer(true);
    	    final int ii = i;
    		timers[i].schedule(new TimerTask() {
				@Override
				public void run() {
					elevator[ii].run();
				}
			}, 0, 1000);
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
