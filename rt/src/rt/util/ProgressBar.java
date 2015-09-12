package rt.util;

/**
 * A very simple progress bar. During use, the progress bar should be the only one to use console output.
 */
public class ProgressBar {

	private int nTasks;
	private int printed;
	private int nDoneTasks;

	/**
	 * Initializes the progress bar and immediately dumps the headers of the progress bar to console. 
	 * @param nTasks, the number of tasks that this progressbar tracks. Once <code>increment</code> has bin called nTask times, 
	 * the progressbar will reach 100%.
	 */
	public ProgressBar(int nTasks) {
		this.nTasks = nTasks;
		this.nDoneTasks = 0;
		this.printed = 0;
		System.out.println("0%                                                50%                                           100%");
		System.out.println("|---------|---------|---------|---------|---------|---------|---------|---------|---------|--------|");
	}
	
	/**
	 * Call this method whenever a task has finished computing.
	 */
	public void increment() {
		int toPrint = (int) (++nDoneTasks/(float)nTasks*100);
		for (; printed < toPrint; printed++) {
			System.out.print("*");
		}
	}
}
