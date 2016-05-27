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

	/**
	 * Constructor for the timerPanel
	 * @throws HeadlessException
	 */
	public TimerPanel() throws HeadlessException {
		super();
		setupLabel();
		thread.start();
	}

	/**
	 * Gets the current time of the game
	 * @return the current time
	 */
	private long getCurrTime() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * Starts the timer
	 */
	public void startTimer() {
		if (thread.stopped) {
			pauseCount += (getCurrTime() - pauseStart);
			thread.stopped = false;
		}
	}
	
	/**
	 * Sets the start time for the timer
	 * @param prevTime the previous time that is being set to the start time
	 */
	public void setStartTime(long prevTime) {
		
		pauseStart = prevTime;
		pauseCount = 0;
	}
	
	/**
	 * Returns the time of the current game to be saved
	 * @return currentTime - startTime - pausedTime
	 */
	public long toSaveTime(){
		
		return getCurrTime() - programStart - pauseCount;
	}
	
	/**
	 * Pauses the timer
	 */
	public void pauseTimer() {
		if (!thread.stopped) { // timmer not stop yet
			pauseStart = getCurrTime();
			thread.stopped = true; // stop the timer
		}

	}

	/**
	 * Saves the JLabel which contains the current elapsed time
	 * @return JLavel containing current elapsed time
	 */
	public JLabel saveTimer() {
		thread.stopped = true;
		long elapsed = getCurrTime() - programStart - pauseCount;

		return new JLabel(format(elapsed));

	}

	/**
	 * Calculates the total time
	 * @return total time 
	 */
	public String totalTime() {
		thread.stopped = true;
		long elapsed = getCurrTime() - programStart - pauseCount;
		return format(elapsed);
	}

	/**
	 * Calculates the total time in seconds
	 * @return total time in seconds
	 */
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

	/**
	 * Resets the timer to 0
	 */
	public void clearTimer() {
		pauseStart = programStart;
		pauseCount = 0;
		thread.stopped = true;
		label.setText(INITIAL_LABEL_TEXT);
	}

	/**
	 * Sets up the format for the timer
	 */
	private void setupLabel() {
		label.setFont(new Font("Arial", Font.BOLD, 15));
		this.setLayout(new GridLayout(1, 1));
		this.add(label);
	}

	private class CountingThread extends Thread {

		public boolean stopped = true;

		/**
		 * Constructor for CountingThread
		 */
		private CountingThread() {
			setDaemon(true);
		}

		/**
		 * Runs the timer
		 */
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

	/**
	 * Converts time in seconds to 00:00:00
	 * @param elapsed current time elapsed in seconds
	 * @return time in format 00:00:00
	 */
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