package rt.testscenes;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.*;
import rt.cameras.*;
import rt.films.*;
import rt.integrators.*;
import rt.intersectables.*;
import rt.lightsources.*;
import rt.materials.Diffuse;
import rt.samplers.*;
import rt.tonemappers.*;

/**
 * Test scene for pinhole camera specifications.
 */
public class CameraTestScene extends Scene {

	public CameraTestScene()
	{
		// Output file name
		outputFilename = new String("../output/testscenes/Camera");
		
		// Image width and height in pixels
		width = 1280;
		height = 720;
		
		// Number of samples per pixel
		SPP = 10;
		
		// Specify which camera, film, and tonemapper to use
		//Point3f eye = new Point3f(0.5f, 0.5f, 3.f);
		//Point3f lookAt = new Point3f(0.5f, 0.f, 0.f);
		//Vector3f up = new Vector3f(0.2f, 1.f, 0.f);
		float fov = 60.f;
		float aspect = 16.f/9.f;
		
		Point3f eye = new Point3f(0.5f, .5f, 3.f);
		Point3f lookAt = new Point3f(0.f, 0.f, 0.f);
		Vector3f up = new Vector3f(0.0f, 1.f, 0.f);
		
		//camera = new FixedCamera(600, 600);
		camera = new PinholeCamera(eye, lookAt, up, fov, aspect, width, height);
		film = new BoxFilterFilm(width, height);
		tonemapper = new ClampTonemapper();
		
		// Specify which integrator and sampler to use
<<<<<<< HEAD
		integratorFactory = new TrivialIntegratorFactory();
	//	samplerFactory = new OneSamplerFactory();
		samplerFactory = new RandomSamplerFactory();	
=======
		integratorFactory = new PointLightIntegratorFactory();
		samplerFactory = new OneSamplerFactory();
	//	samplerFactory = new RandomSamplerFactory();	
>>>>>>> a5f0f0ab63911104c69f8a9b0f533bea5de4e26f
		
		// Define some objects to be added to the scene. 
		// 5 planes can be used to define a box (with never ending walls).
		Plane p1 = new Plane(new Vector3f(1.f, 0.f, 0.f), 1.f);
		p1.material = new Diffuse(new Spectrum(0f, 0f, 1f));
		Plane p2 = new Plane(new Vector3f(-1.f, 0.f, 0.f), 1.f);
		p2.material = new Diffuse(new Spectrum(1f, 0f, 0f));
		Plane p3 = new Plane(new Vector3f(0.f, 1.f, 0.f), 1.f);
	//	Plane p4 = new Plane(new Vector3f(0.f, -1.f, 0.f), 1.f);
	//	Plane p5 = new Plane(new Vector3f(0.f, 0.f, 1.f), 1.f);
		
		Sphere s1 = new Sphere(new Point3f(0.f, 0.f, 0f), 0.5f);
		s1.material = new Diffuse(new Spectrum(1f, 1f, 0.f));
		
		IntersectableList iList = new IntersectableList();
		// Some planes are left out
		iList.add(p1);
		iList.add(p2);
		iList.add(p3);
		iList.add(s1);;
		//iList.add(p4);
		//iList.add(p5);
		
		this.root = iList;
		
		// Light sources
		LightGeometry pointLight = new PointLight(new Vector3f(0.f, 0.f, 3.f), new Spectrum(10.f, 10.f, 10.f));
		lightList = new LightList();
		lightList.add(pointLight);
	}
}
