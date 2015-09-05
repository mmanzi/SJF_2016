package rt.util;

import java.util.Date;

/**
 * A utility class to help us run benchmarks.
 *
 * @author Oscar.Nierstrasz@acm.org
 * @version 1.0 1998-11-25
 */
public class Timer {
	long _startTime;
	
	/**
	 * You can either create a new instance whenever
	 * you want to time something, or you can reset()
	 * an existing instance.
	 */
	public Timer() { this.reset(); }
	
	public void reset() {
		_startTime = this.timeNow();
	}
	
	/**
	 * How many milliseconds have elapsed since
	 * the last reset()?  NB: does not reset the timer!
	 */
	public long timeElapsed() {
		return this.timeNow() - _startTime;
	}

	protected long timeNow() {
		return new Date().getTime();
	}
	
	public void printElapsedTimeToConsole(String message) {
		long time_ms = timeElapsed();
		long time_s = time_ms / 1000;
		long time_min =  time_s / 60;
		String timing_output = String.format("%s %d ms = %d min, %d sec.\n", message, time_ms, time_min, time_s - time_min*60);
		System.out.print(timing_output);
	}
}