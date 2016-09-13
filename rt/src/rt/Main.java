package rt;

import rt.basicscenes.*;
import rt.testscenes.*;
import rt.renderers.*;

import java.io.*;
import java.util.concurrent.ExecutionException;

public class Main {

	/** 
	 * The scene to be rendered. Change at will.
	 */

	public static Scene scene = new SphereTest();
	//public static Scene scene = new TriangleTest();
	//public static Scene scene = new CameraTestScene();
	

	public static void main(String[] args) throws InterruptedException, ExecutionException, FileNotFoundException, UnsupportedEncodingException
	{			
		scene.prepare();
		// There are multiple renderer implementations, check out the package 'rt.renderers'.
		//Renderer renderer = new SingleThreadedRenderer(scene);
		Renderer renderer = new MultiThreadedRenderer(scene);
		renderer.render();
		renderer.writeImageToFile();
	}
	
}
