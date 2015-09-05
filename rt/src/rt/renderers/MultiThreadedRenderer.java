package rt.renderers;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import rt.Scene;
import rt.util.ProgressBar;

public class MultiThreadedRenderer extends Renderer {

	private final int taskSize;
	private final int nrThreads;

	public MultiThreadedRenderer(Scene scene) {
		super(scene);
		this.taskSize = 64;	// Each task renders a square image block of this size
		this.nrThreads = Runtime.getRuntime().availableProcessors();	// Number of threads to be used for rendering
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
