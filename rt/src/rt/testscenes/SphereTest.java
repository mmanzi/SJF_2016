package rt.testscenes;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.*;
import rt.cameras.*;
import rt.films.*;
import rt.integrators.*;
import rt.intersectables.*;
import rt.lightsources.PointLight;
import rt.materials.*;
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
		width = 1080;
		height = 1080;
		
		// Number of samples per pixel
		SPP = 1;
		
		// Specify which camera, film, and tonemapper to use
		float fov = 60.f;
		float aspect = 9.f/9.f;
		
		Point3f eye = new Point3f(0.f, 0.f, 1.f);
		Point3f lookAt = new Point3f(0.f, 0.f, 0.f);
		Vector3f up = new Vector3f(0.0f, -1.f, 0.f);
		
		camera = new FixedCamera(600, 600);
		camera = new PinholeCamera(eye, lookAt, up, fov, aspect, width, height);
		
		film = new BoxFilterFilm(width, height);
		tonemapper = new ClampTonemapper();
		
		// Specify which integrator and sampler to use
		//integratorFactory = new TrivialIntegratorFactory();
		integratorFactory = new PointLightIntegratorFactory();
		samplerFactory = new OneSamplerFactory();
					
		IntersectableList iList = new IntersectableList();
		
		Sphere s1 = new Sphere(new Point3f(0.f, 0.f, -3.f), 0.5f);
		s1.material = new Phong(new Spectrum(1f, 1f, 0.f),new Spectrum(0.5f, 0.5f, 0.5f),new Spectrum(0f, 0f, 0.f), 64);
		Plane p1 = new Plane(new Vector3f(0.f, 0.f, 1.f), -1.f);
		p1.material = new Diffuse(new Spectrum(1f, 0.f, 0.f));
		//iList.add(s1);
		iList.add(p1);
		
		this.root = iList;
		
		LightGeometry pointLight = new PointLight(new Vector3f(0.f, 1.f, -4f), new Spectrum(1000.f, 1000.f, 10.f));
		lightList = new LightList();
		lightList.add(pointLight);
	}

}
