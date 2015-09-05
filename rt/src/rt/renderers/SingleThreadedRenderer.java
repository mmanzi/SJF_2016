package rt.renderers;

import java.util.concurrent.ExecutionException;

import rt.Scene;

/**
 * Only uses a single thread for rendering.
 */
public class SingleThreadedRenderer extends Renderer {

	public SingleThreadedRenderer(Scene scene) {
		super(scene);
	}

	@Override
	protected void renderInternally() throws InterruptedException,
			ExecutionException {
		int width = scene.getFilm().getWidth();
		int height = scene.getFilm().getHeight();
		RenderTask wholeImageTask = new RenderTask(scene, 0, width, 0, height);
		wholeImageTask.run();
	}
	
	protected String rendererMessage() {
		return "with a single thread";
	}
}
