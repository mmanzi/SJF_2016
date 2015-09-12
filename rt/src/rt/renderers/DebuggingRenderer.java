package rt.renderers;

import java.util.concurrent.ExecutionException;

import rt.Scene;

/**
 * This renderer only renders a single pixel. Pass coordinates of the pixel you are interested
 * in via the constructor. Beware that the coordinate system starts at the bottom left of the image.
 */
public class DebuggingRenderer extends Renderer {

	private final int x, y;

	/**
	 * @param scene, The scene to be rendered.
	 * @param x, the x coordinate of the pixel to be rendered.
	 * @param y, the y coordinate of the pixel to be rendered.
	 */
	public DebuggingRenderer(Scene scene, int x, int y) {
		super(scene);
		this.x = x;
		this.y = y;
	}

	@Override
	protected void renderInternally() throws InterruptedException,
			ExecutionException {
		RenderTask wholeImageTask = new RenderTask(scene, x, x+1, y, y+1);
		wholeImageTask.run();
	}
	
	protected String rendererMessage() {
		return "only the pixel (" + x + ", " + y + ")";
	}

}
