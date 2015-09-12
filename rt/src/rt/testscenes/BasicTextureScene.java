package rt.testscenes;

import java.io.IOException;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.*;
import rt.cameras.PinholeCamera;
import rt.films.BoxFilterFilm;
import rt.integrators.*;
import rt.intersectables.*;
import rt.lightsources.*;
import rt.materials.*;
import rt.samplers.*;
import rt.tonemappers.ClampTonemapper;

public class BasicTextureScene extends Scene {
	
	public BasicTextureScene()
	{
		// Output file name
		outputFilename = new String("../output/testscenes/BasicTextureScene");
		
		// Image width and height in pixels
		width = 640;
		height = 360;
		
		// Number of samples per pixel
		SPP = 1;
		
		// Specify which camera, film, and tonemapper to use
		Point3f eye = new Point3f(0.f, 0.f, 5.f);
		Point3f lookAt = new Point3f(0.f, -.5f, 0.f);
		Vector3f up = new Vector3f(0.f, 1.f, 0.f);
		float fov = 60.f;
		float aspect = 16.f/9.f;
		camera = new PinholeCamera(eye, lookAt, up, fov, aspect, width, height);
		film = new BoxFilterFilm(width, height);
		tonemapper = new ClampTonemapper();
		
		// Specify which integrator and sampler to use
		integratorFactory = new PointLightIntegratorFactory();
		samplerFactory = new OneSamplerFactory();		
		
		Material chessTexture = new Textured("../textures/chessboard.jpg", "../normalmaps/normal.gif");
		Material couch = new Textured("../textures/pink.jpg", "../normalmaps/couch.png");

		Sphere sphere = new Sphere(new Point3f(-1, 0, 0), 1.5f);
		sphere.material = chessTexture;
		Rectangle rect = new Rectangle(new Point3f(1,0,0), new Vector3f(1,0,0),  new Vector3f(0,1,0));
		
		rect.material = couch;
		
		// Ground and back plane
		Plane groundPlane = new Plane(new Vector3f(0.f, 1.f, 0.f), 1.5f);
		groundPlane.material = new Diffuse(new Spectrum(0.5f, 1f, 0.5f));
		Plane backPlane = new Plane(new Vector3f(0.f, 0.f, 1.f), 3.15f);
		
		// Collect objects in intersectable list
		IntersectableList intersectableList = new IntersectableList();
		intersectableList.add(sphere);	
		intersectableList.add(rect);
		intersectableList.add(groundPlane);
		intersectableList.add(backPlane);
		
		// Set the root node for the scene
		root = intersectableList;
		
		// Light sources
		Vector3f lightPos = new Vector3f(eye);
		lightPos.add(new Vector3f(-1.f, 1.f, 0.f));
		LightGeometry pointLight1 = new PointLight(lightPos, new Spectrum(14.f, 14.f, 14.f));
		lightList = new LightList();
		lightList.add(pointLight1);
	}
}
