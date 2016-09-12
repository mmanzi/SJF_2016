package rt.testscenes;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.*;
import rt.cameras.*;
import rt.films.*;
import rt.integrators.*;
import rt.intersectables.*;
import rt.lightsources.PointLight;
import rt.materials.Diffuse;
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
		camera = new FixedCamera(600, 600);
		film = new BoxFilterFilm(width, height);
		tonemapper = new ClampTonemapper();
		
		// Specify which integrator and sampler to use
		//integratorFactory = new TrivialIntegratorFactory();
		integratorFactory = new PointLightIntegratorFactory();
		samplerFactory = new OneSamplerFactory();
					
		IntersectableList iList = new IntersectableList();
		
		Sphere s1 = new Sphere(new Point3f(0, 0.f, 0.75f), 2.f);
		s1.material = new Diffuse(new Spectrum(1f, 1f, 0.f));
		Plane p1 = new Plane(new Vector3f(0.f, 1.f, 0.f), 1.f);
		iList.add(s1);
	//	iList.add(p1);
		
		this.root = iList;
		
		LightGeometry pointLight = new PointLight(new Vector3f(0.f, 0.f, 2.5f), new Spectrum(2.f, 2.f, 2.f));
		lightList = new LightList();
		lightList.add(pointLight);
	}

}
