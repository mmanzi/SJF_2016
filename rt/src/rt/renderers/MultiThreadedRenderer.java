package rt.renderers;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import rt.Scene;
import rt.util.ProgressBar;

/**
 * A multi threaded renderer that divides the output image into blocks and renders
 * these blocks with multiple threads. The execution order goes from the bottom
 * left of the image to the top right, rendering the rows first.
 */
public class MultiThreadedRenderer extends Renderer {

	/**
	 * The size of a single task in pixels. Blocks of this size will be rendered in one task.
	 */
	private final int taskSize = 64;
	
	/**
	 * Number of threads to be used, by default the number of available processors.
	 */
	private final int nrThreads = Runtime.getRuntime().availableProcessors();

	public MultiThreadedRenderer(Scene scene) {
		super(scene);
	}
	
	protected void renderInternally() throws InterruptedException, ExecutionException {
		int width = scene.getFilm().getWidth();
		int height = scene.getFilm().getHeight();
		int nTasks = (int)(Math.ceil(width/(double)taskSize) * Math.ceil(height/(double)taskSize));
		ExecutorService executor = Executors.newFixedThreadPool(nrThreads);
		
		// Make render tasks, split image into blocks to be rendered by the tasks
		ArrayList<Future<?>> futures = new ArrayList<>(nTasks);
		for(int j=0; j < Math.ceil(height/(float)taskSize); j++) {
			for(int i=0; i < Math.ceil(width/(float)taskSize); i++) {
				RenderTask task = new RenderTask(scene, i*taskSize, Math.min((i+1)*taskSize, width), j*taskSize, 
																	Math.min((j+1)*taskSize, height));
				futures.add(executor.submit(task));
			}
		}
		ProgressBar progressBar = new ProgressBar(nTasks);
		// Wait for threads to end
		executor.shutdown();
		for (Future<?> f: futures) {
			f.get();
			progressBar.increment();
		}
	}
	
	protected String rendererMessage() {
		return String.format("with %s threads", nrThreads);
	}
}
