package rt;

import rt.basicscenes.*;
import rt.renderers.MultiThreadedRenderer;
import rt.renderers.Renderer;
import rt.renderers.SingleThreadedRenderer;
import rt.testscenes.*;

import java.io.*;
import java.util.concurrent.ExecutionException;

/**
 * The main rendering loop. Provides multi-threading support. The {@link Main#scene} to be rendered
 * is hard-coded here, so you can easily change it. The {@link Main#scene} contains 
 * all configuration information for the renderer.
 */
public class Main {

	/** 
	 * The scene to be rendered.
	 */
	public static Scene scene = new CameraTestScene();
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, FileNotFoundException, UnsupportedEncodingException
	{			
		scene.prepare();
		Renderer renderer = new SingleThreadedRenderer(scene);
		renderer.render();
		renderer.writeImageToFile();
	}
	
}
