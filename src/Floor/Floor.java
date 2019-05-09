package Floor;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Elevator.Elevator;

public class Floor extends JPanel {
	public static final int totalFloor = 20;
	
	private JButton []upButton = new JButton[totalFloor + 1];
	private JButton []downButton = new JButton[totalFloor + 1];
	private Elevator []elevators;
	private LinkedList<Integer> noDispatchedJob = new LinkedList<Integer>();
	
	public Floor(Elevator []es) {
		elevators = es;
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
		
		Timer timer = new Timer(true);
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				lookUpJobDispatch();
			}
		};
		timer.schedule(timerTask, 1000, 1000);

	}
	
	private void lookUpJobDispatch() {
		ArrayList<Integer> delList = new ArrayList<Integer>();
		for (int i : noDispatchedJob) {
			int direction;
			int f = i;
			if (i > totalFloor) {
				f -= totalFloor;
				direction = Elevator.down;
			} else {
				direction = Elevator.up;
			}
			if (schedule(f, direction, true)) {
				delList.add(new Integer(i));
			}
		}
		noDispatchedJob.removeAll(delList);
	}
	
	private boolean schedule(int floor, int direction, boolean fromLookUp) {
		int adverseDirec = Elevator.up + Elevator.down - direction;
		int distance = totalFloor + 1;
		int elevatorId = -1;
		// 寻找最合适点电梯接受该任务
		for (int i = 0; i < Elevator.totalElevator; ++i) {
			int d2 = Math.abs(floor - elevators[i].getFloor());
			// 如果电梯已经上行/下行至该楼层且不是停靠状态跳过该电梯
			// 如果电梯为非free状态且其所在楼层在要求楼层与direction相反的方向跳过该电梯
			// 如果电梯运行方向与需求方向相反则跳过该电梯
			if ( (d2 == 0 && (elevators[i].getState() == direction && !elevators[i].isPause()))
				|| (elevators[i].getState() == Elevator.up && floor < elevators[i].getFloor())
				|| (elevators[i].getState() == Elevator.down && floor > elevators[i].getFloor())
				|| elevators[i].getState() == adverseDirec
				|| elevators[i].getOrder() == adverseDirec ) {
				continue;
			}
			// 选距离更小者
			if (d2 < distance) {
				distance = d2;
				elevatorId = i;
			// 若距离相等 选择非free状态的电梯
			} else if (d2 == distance) {
				if (elevators[i].getState() == direction) {
					elevatorId = i;
				}
			}
		}
		// 若没找到合适的电梯则将其加入任务列表
		if (elevatorId == -1) {
			if (!fromLookUp) {
				switch (direction) {
				case Elevator.up:
					noDispatchedJob.add(floor);
					break;
				case Elevator.down:
					noDispatchedJob.add(floor + totalFloor);
					break;
				}
			}
			return false;
		} else {
		// 若找到了合适的电梯让其接受该任务
			elevators[elevatorId].addOutJob(floor, direction);
			return true;
		}
	}
	
	public void finishJob(int floor, int direction) {
		switch (direction) {
		case Elevator.up:
			upButton[floor].setEnabled(true);
			upButton[floor].setBackground(Color.white);
			break;
		case Elevator.down:
			downButton[floor].setEnabled(true);
			downButton[floor].setBackground(Color.white);
			break;
		}
	}
	
	ActionListener upButtonListrner = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton)e.getSource();
			button.setEnabled(false);
			button.setBackground(new Color(176, 196, 222));
			String text = button.getText();
			int floor = Integer.parseInt(text.substring(0, text.length()-1));
			schedule(floor, Elevator.up, false);
		}
	};
	
	ActionListener downButtonListrner = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton)e.getSource();
			button.setEnabled(false);
			button.setBackground(new Color(176, 196, 222));
			String text = button.getText();
			int floor = Integer.parseInt(text.substring(0, text.length()-1));
			schedule(floor, Elevator.down, false);
		}
	};
}
