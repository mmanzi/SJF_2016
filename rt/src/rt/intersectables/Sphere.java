package rt.intersectables;

import javax.vecmath.*;

import rt.*;
import rt.accelerators.AxisAlignedBox;
import rt.materials.*;

public class Sphere implements Intersectable {

	private Point3f center;
	private float radius;
	private float surfaceArea;
	private AxisAlignedBox boundingBox;
	public Material material;
	
	public Sphere(Point3f center, float radius)
	{
		material = new Diffuse(new Spectrum(1.f, 1.f, 1.f));
		
		this.center = center;
		this.radius = radius;
		
		// TODO: Bounding box and surface area.
		boundingBox = null;
		surfaceArea = 0;
	}
	
	public Sphere()
	{
		this(new Point3f(0.f, 0.f, 0.f), 1.f);
	}
	
	public float surfaceArea()
	{
		return surfaceArea;
	}
	
	public HitRecord intersect(Ray r) {
		// TODO: Find intersections.
		return new HitRecord();
	}
	
	public AxisAlignedBox getBoundingBox()
	{
		return boundingBox;
	}
}
