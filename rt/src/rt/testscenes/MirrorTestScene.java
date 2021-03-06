package rt.testscenes;

import java.io.IOException;

import rt.*;
import rt.accelerators.BSPAccelerator;
import rt.cameras.*;
import rt.films.*;
import rt.integrators.*;
import rt.intersectables.*;
import rt.lightsources.*;
import rt.samplers.*;
import rt.tonemappers.*;
import rt.materials.*;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.omg.CORBA.TRANSACTION_MODE;

/**
 * Test scene for instancing and rendering triangle meshes.
 */
public class MirrorTestScene extends Scene {

	public IntersectableList objects;

	private Point3f CalculateLookAt(Point3f origin, Point3f lookAt) 
	{
		Point3f rtn = new Point3f();
		rtn.sub(lookAt, origin);
		return rtn;
	}
	/**
	 * Timing: 8.5 sec on 12 core Xeon 2.5GHz, 24 threads
	 */
	public MirrorTestScene()
	{	
		outputFilename = new String("../output/testscenes/teapotShadow");
		
		// Specify integrator to be used
//		integratorFactory = new WhittedSamplingIntegratorFactory();
		integratorFactory = new WhittedIntegratorFactory();
		
		// Specify pixel sampler to be used
		//samplerFactory = new OneSamplerFactory();
		
		SPP = 16;
		samplerFactory = new GridSamplerFactory();
		
		// Make camera and film
		Point3f eye = new Point3f(0.f,0.5f,1.f);
		//Point3f lookAt = CalculateLookAt(eye, new Point3f(0.f,0.f,0.f));
		Point3f lookAt = new Point3f(0.f,0.f,0.f);
		Vector3f up = new Vector3f(0.f,1f,0.f);
		float fov = 90.f;
		int width = 512;
		int height = 512;
		float aspect = (float)width/(float)height;
		camera = new PinholeCamera(eye, lookAt, up, fov, aspect, width, height);
		film = new BoxFilterFilm(width, height);						
		tonemapper = new ClampTonemapper();
		
		// List of objects
		objects = new IntersectableList();	
				
		// Box
		Plane plane = new Plane(new Vector3f(0.f, 1.f, 0.f), 1.f);
		plane.material = new Diffuse(new Spectrum(0.f, 0.8f, 0.8f));
		objects.add(plane);		
		
		plane = new Plane(new Vector3f(0.f, 0.f, 1.f), 1.f);
		plane.material = new Procedural(new Spectrum(0.8f, 0.8f, 0.8f),new Spectrum(1.f, 0.f, 0.f), 3, 20);
		objects.add(plane);
		
		plane = new Plane(new Vector3f(-1.f, 0.f, 0.f), 1.f);
		plane.material = new Diffuse(new Spectrum(1.f, 0.8f, 0.8f));
		objects.add(plane);
		
		plane = new Plane(new Vector3f(1.f, 0.f, 0.f), 1.f);
		plane.material = new Diffuse(new Spectrum(0.f, 0.8f, 0.0f));
		objects.add(plane);
		
		plane = new Plane(new Vector3f(0.f, -1.f, 0.f), 1.f);
		plane.material = new Diffuse(new Spectrum(0.8f, 0.8f, 0.8f));
		objects.add(plane);
		
		plane = new Plane(new Vector3f(0.f, 0.f, -1.f), 10.f);
		plane.material = new Diffuse(new Spectrum(100.8f, 0.8f, 0.8f));
	//	objects.add(plane);
		
		// Add objects
		Mesh mesh;
		try
		{
			mesh = ObjReader.read("../obj/teapot.obj", 0.7f);
			mesh.material=new Schlick(new Spectrum(1.f,0.5f,0.5f), 1.3f);
		} catch(IOException e) 
		{
			System.out.printf("Could not read .obj file\n");
			return;
		}
		
		Sphere s = new Sphere(new Point3f(0.f,0.0f,0f), 0.4f);
		s.material = new Schlick(new Spectrum(0.8f, 0.8f, 0.8f),1.1f);
	//	s.material = new Diffuse(new Spectrum(0.8f, 0.8f, 0.8f));
	//    objects.add(s);
		
		
		
		BSPAccelerator accel = new BSPAccelerator(mesh);
		objects.add(accel);	
	//	objects.add(mesh);	
		
		root = objects;
		
		// List of lights
		lightList = new LightList();
		
		LightGeometry light = new PointLight(new Vector3f(0.f,0.8f,0.8f), new Spectrum(3.f, 3.f, 3.f));
		lightList.add(light);
		
		light = new PointLight(new Vector3f(-0.8f,0.2f,1.f), new Spectrum(1.5f, 1.5f, 1.5f));
		lightList.add(light);		
	}
}
