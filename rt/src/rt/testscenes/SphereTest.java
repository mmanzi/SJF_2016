package rt.testscenes;

import javax.vecmath.Point3f;

import rt.*;
import rt.cameras.*;
import rt.films.*;
import rt.integrators.*;
import rt.intersectables.*;
import rt.samplers.*;
import rt.tonemappers.*;

/**
 * A very simple scene for testing sphere intersections.
 *
 */
public class SphereTest extends Scene {

	public SphereTest()
	{
		// Output file name
		outputFilename = new String("../output/testscenes/SphereTest");
		
		// Image width and height in pixels
		width = 512;
		height = 512;
		
		// Number of samples per pixel
		SPP = 1;
		
		// Specify which camera, film, and tonemapper to use
		camera = new FixedCamera(100, 100);
		film = new BoxFilterFilm(width, height);
		tonemapper = new ClampTonemapper();
		
		// Specify which integrator and sampler to use
		integratorFactory = new TrivialIntegratorFactory();
		samplerFactory = new OneSamplerFactory();
					
		root = new Sphere(new Point3f(0, 0, 0), 2f);
	}

}
