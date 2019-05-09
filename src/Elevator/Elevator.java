package Elevator;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Floor.Floor;

class ElevatorView extends JPanel {
	private JButton []floorButton = new JButton[Floor.totalFloor+1];
	private JLabel currentState;
	private JLabel currentFloor;
	private Elevator elevator;
	
	public ElevatorView(Elevator e) {
		elevator = e;
		this.setLayout(new GridLayout((Floor.totalFloor+1)/2 + 1, 2, 4, 4));
		
		currentFloor = new JLabel("1");
		Font font = currentFloor.getFont();
		currentFloor.setHorizontalAlignment(JLabel.CENTER);
		currentFloor.setFont(new Font(font.getFontName(), font.getStyle(), 25));
		currentFloor.setForeground(Color.blue);
		currentState = new JLabel("-");
		currentState.setHorizontalAlignment(JLabel.CENTER);
		currentState.setFont(new Font(font.getFontName(), font.getStyle(), 25));
		currentState.setForeground(Color.blue);
		
		this.add(currentState);this.add(currentFloor);
		for (int i = 1; i <= Floor.totalFloor; ++i) {
			floorButton[i] = new JButton(String.valueOf(i));
			floorButton[i].setMargin(new Insets(1,1,1,1));
			floorButton[i].setFocusPainted(false);
			floorButton[i].setBackground(Color.white);
			floorButton[i].setFont(new Font(font.getFontName(), font.getStyle(), 15));
			floorButton[i].addActionListener(floorButtonListener);
			this.add(floorButton[i]);
		}
	}
	
	ActionListener floorButtonListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton)e.getSource();
			int floor = Integer.parseInt(button.getText());
			elevator.addJob(floor);
		}
	};
	
	public void buttonPressed(int i) {
		floorButton[i].setEnabled(false);
		floorButton[i].setBackground(new Color(176, 196, 222));
	}
	
	public void changeFloor(int f) {
		currentFloor.setText(String.valueOf(f));
	}
	
	public void changeState(int s) {
		switch (s) {
		case Elevator.free:
			currentState.setText("-");
			break;
		case Elevator.up:
			currentState.setText("↑");
			break;
		case Elevator.down:
			currentState.setText("↓");
			break;
		default:
			currentState.setText("open");
			break;
		}
	}
	
	public void arriveFloor(int f) {
		floorButton[f].setEnabled(true);
		floorButton[f].setBackground(Color.white);
	}
}

public class Elevator {
	public static final int totalElevator = 5;
	public static final int free = 0;
	public static final int up = 1;
	public static final int down = 2;
	public static final int doorOpen = 3;
	
	private ElevatorView view = new ElevatorView(this);
	private boolean []floorButtonPressed = new boolean[Floor.totalFloor + 1];
	private int []outJobDirection = new int[Floor.totalFloor + 1];
	private Timer timer;
	private TimerTask timerTask;
	private Floor floorView;
	private int state;
	// 电梯接受外部任务后order和state可能不一致
	private int order;
	private int tempState;
	private boolean changeOrder;
	private int floor;
	private int maxJob;
	private int minJob;
	private boolean arriveFloor;
	
	private void restartTimer() {
		timer.cancel();
		timerTask.cancel();
		timer = null;
		timerTask = null;
		timer = new Timer(true);
		timerTask = new TimerTask() {
			@Override
			public void run() {
				start();
			}
		};
		timer.schedule(timerTask, 1800, 1800);
	}
	
	public Elevator(Floor f) {
		floorView = f;
		order = state = free;
		changeOrder = false;
		floor = 1;
		maxJob = 0;
		minJob = Floor.totalFloor + 1;
		arriveFloor = false;
		for (int i = 0; i <= Floor.totalFloor; ++i) {
			floorButtonPressed[i] = false;
			outJobDirection[i] = free;
		}
		timer = new Timer(true);
		timerTask = new TimerTask() {
			@Override
			public void run() {
				start();
			}
		};
		timer.schedule(timerTask, 1800, 1800);
	}
	
	public final int getFloor() {
		return floor;
	}
	
	public final int getState() {
		return state;
	}
	
	public final int getOrder() {
		return order;
	}
	
	public final boolean isPause() {
		return arriveFloor;
	}
	
	public void addOutJob(int f, int d) {
		if (order == free) {
			order = d;
		}
		outJobDirection[f] = d;
		addJob(f);
	}
	
	public void addJob(int f) {
		floorButtonPressed[f] = true;
		view.buttonPressed(f);
		int tempState = state;
		if (state == free || (f == floor && arriveFloor) ) {
			if (f > floor) {
				state = up;
			} else if (f < floor) {
				state = down;
			} else {
				finishJob();
				restartTimer();
			}
			if(!arriveFloor) {
				// 重启定时器
				restartTimer();
				view.changeState(state);
			}
		}
		if (order == tempState) {
			order = state;
		}
	}
	
	public void start() {
		if (arriveFloor) {
			if (changeOrder) {
				state = order = tempState;
			}
			view.changeState(state);
			arriveFloor = false;
		} else {
			switch (state) {
			case up:
				upFloor();
				break;
			case down:
				downFloor();
				break;
			default:
				break;
			}
		}
	}
	
	public void upFloor() {
		floor += 1;
		view.changeFloor(floor);
		if (floorButtonPressed[floor]) {
			finishJob();
		}
	}
	
	public void downFloor() {
		floor -= 1;
		view.changeFloor(floor);
		if (floorButtonPressed[floor]) {
			finishJob();
		}
	}
	
	private void finishJob() {
		view.changeState(doorOpen);
		arriveFloor = true;
		
		// 更新minJob和maxJob
		maxJob = 0;
		minJob = Floor.totalFloor + 1;
		for (int i = 1; i <= Floor.totalFloor; ++i) {
			if (floorButtonPressed[i]) {
				if (i < minJob)
					minJob = i;
				if (i > maxJob)
					maxJob = i;
			}
		}
		
		int tempState = state;
		// 上行到最高任务楼层
		if (state == up && maxJob <= floor) {
			// 更新state
			if (minJob < floor) {
				state = down;
			} else {
				state = free;
			}
	    // 下行到最低任务楼层
		} else if (state == down && minJob >= floor) {
			// 更新state
			if (maxJob > floor) {
				state = up;
			} else {
				state = free;
			}
		}
		floorButtonPressed[floor] = false;
		if (order == tempState) {
			order = state;
		}
		
		// 修改按钮可用
		view.arriveFloor(floor);
		if (outJobDirection[floor] != free) {
			if (tempState != order) {
				changeOrder = true;
				int temp = tempState;
				tempState = state;
				state = temp;
			}
			floorView.finishJob(floor, outJobDirection[floor]);
			outJobDirection[floor] = free;
		}
	}
	
	public void add(JPanel panel) {
		panel.add(this.view);
	}
}