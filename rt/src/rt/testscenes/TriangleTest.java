package rt.testscenes;

import java.io.IOException;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.*;
import rt.accelerators.BSPAccelerator;
import rt.cameras.*;
import rt.films.*;
import rt.integrators.*;
import rt.intersectables.*;
import rt.lightsources.PointLight;
import rt.materials.*;
import rt.samplers.*;
import rt.tonemappers.*;

public class TriangleTest extends Scene {

	public TriangleTest()
	{
		// Output file name
		outputFilename = new String("../output/testscenes/TriangleTest");
		
		// Image width and height in pixels
		width = 1024;
		height = 1024;
		
		// Number of samples per pixel
		SPP = 10;
		
		// Specify which camera, film, and tonemapper to use
		Point3f eye = new Point3f(0.f, 0.f, 3.f);
		Point3f lookAt = new Point3f(0.f, 0.f, 0.f);
		Vector3f up = new Vector3f(0.f, 1.f, 0.f);
		float fov = 90.f;
		float aspect = (float)width/(float)height;
		camera = new PinholeCamera(eye, lookAt, up, fov, aspect, width, height);
		
		//camera = new FixedCamera(width,height);
		
		film = new BoxFilterFilm(width, height);
		tonemapper = new ClampTonemapper();
		
		// Specify which integrator and sampler to use
		integratorFactory = new ShadowIntegratorFactory();
		samplerFactory = new GridSamplerFactory();
			
		// Make a triangle. Note: convention is that vertex order is counter
		// clockwise when triangle is seen from outside (outside is by convention
		// the direction the normal points into).
		/*float[] vertices = {0.f, 0.f, 0.f, 
							0.f, 1.f, 0.f, 
							-1.f, 0.f, 0.f};
		float[] normals = { 0.f, 0.f, 1.f, 
							0.f, 0.f, 1.f, 
							0.f, 0.f, 1.f};
		int[] indices = {0, 1, 2};
		
		Mesh mesh = new Mesh(vertices, normals, indices);
		*/
		Mesh mesh;
		try
		{
			mesh = ObjReader.read("../obj/dragon.obj", 0.35f);
			mesh.material = new Phong(new Spectrum(1.f,1.f,0.f), new Spectrum(10.f, 10.f,0.5f), new Spectrum(0.f,0.f,0.f), 50);
		} catch(IOException e) 
		{
			System.out.printf("Could not read .obj file\n");
			return;
		}
		
		Plane p1 = new Plane(new Vector3f(0.f, 1.f, 0.f), 0.45f);
		//p1.material = new Phong(new Spectrum(0.5f, 0.5f, 0.5f), new Spectrum(1.f, 1.f, 1.f), new Spectrum(0.f, 0.f, 0.f), 128);
		p1.material = new Procedural(new Spectrum(1f, 1f, 1f), new Spectrum(1f, 0f, 0f), 8, 200);
		
		//Rectangle rec = new Rectangle(new Point3f(-0.5f,-0.5f,-2.f), new Vector3f(-0.5f,0.f,-0.f), new Vector3f(0.f,-0.5f,-0.f));
		//rec.material=new Phong(new Spectrum(1.f,0.f,1.f), new Spectrum(0.f, 0.5f,0.f), new Spectrum(0.f,0.f,0.f), 128);
		//Sphere s1 = new Sphere(new Point3f(0,0,-3), 1);
		//s1.material = new Diffuse(new Spectrum(0,1,1));
		
		Sphere s1 = new Sphere(new Point3f(0, 1.f, -1.f), 0.42f);
		s1.material = new Diffuse(new Spectrum(1f, 1f, 0.f));
		

		
		IntersectableList intersectableList = new IntersectableList();
		//intersectableList.add(mesh);
		//intersectableList.add(rec);
		//intersectableList.add(s1);
		intersectableList.add(p1);
		
		PointLight light =  new PointLight(new Vector3f(0.f, 5.f, 2.f), new Spectrum(50.f, 50.f, 50.f));
		lightList = new LightList();
		lightList.add(light);
		
		BSPAccelerator accel = new BSPAccelerator(mesh);
		intersectableList.add(accel);
		
		root = intersectableList;
	}

}
