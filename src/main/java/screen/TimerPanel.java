package screen;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class TimerPanel extends JPanel {

	private static final String INITIAL_LABEL_TEXT = "00:00:00";

	private CountingThread thread = new CountingThread();

	private long programStart = getCurrTime();

	private long pauseStart = programStart;

	private long pauseCount = 0;

	private JLabel label = new JLabel(INITIAL_LABEL_TEXT, JLabel.CENTER);

	// CONSTRUCTOR
	public TimerPanel() throws HeadlessException {
		super();
		setupLabel();
		thread.start();
	}

	private long getCurrTime() {
		return System.currentTimeMillis() / 1000;
	}

	public void startTimer() {
		if (thread.stopped) {
			pauseCount += (getCurrTime() - pauseStart);
			thread.stopped = false;
		}
	}
	
	public void setStartTime(long prevTime) {
		
		pauseStart = prevTime;
		pauseCount = 0;
	}
	

	public long toSaveTime(){
		
		return getCurrTime() - programStart - pauseCount;
	}
	
	public void pauseTimer() {
		if (!thread.stopped) { // timmer not stop yet
			pauseStart = getCurrTime();
			thread.stopped = true; // stop the timer
		}

	}

	public JLabel saveTimer() {
		thread.stopped = true;
		long elapsed = getCurrTime() - programStart - pauseCount;

		return new JLabel(format(elapsed));

	}

	public String totalTime() {
		thread.stopped = true;
		long elapsed = getCurrTime() - programStart - pauseCount;
		return format(elapsed);
	}

	public int totalTimeSecond() {

		thread.stopped = true;
		long elapsed = getCurrTime() - programStart - pauseCount;

		int hour, minute, second;

		second = (int) (elapsed % 60);
		elapsed = elapsed / 60;

		minute = (int) (elapsed % 60);
		elapsed = elapsed / 60;

		hour = (int) (elapsed % 60);
		return second;

	}

	public void clearTimer() {
		pauseStart = programStart;
		pauseCount = 0;
		thread.stopped = true;
		label.setText(INITIAL_LABEL_TEXT);
	}

	private void setupLabel() {
		label.setFont(new Font("Arial", Font.BOLD, 15));
		this.setLayout(new GridLayout(1, 1));
		this.add(label);
	}

	private class CountingThread extends Thread {

		public boolean stopped = true;

		private CountingThread() {
			setDaemon(true);
		}

		@Override
		public void run() {
			while (true) {
				if (!stopped) {
					long elapsed = getCurrTime() - programStart - pauseCount;
					label.setText(format(elapsed));
				}

				try {
					sleep(17);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
	}

	// reset time
	public String format(long elapsed) {
		int hour, minute, second;

		second = (int) (elapsed % 60);
		elapsed = elapsed / 60;

		minute = (int) (elapsed % 60);
		elapsed = elapsed / 60;

		hour = (int) (elapsed % 60);
		return String.format("%02d:%02d:%02d", hour, minute, second);

	}
}