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
		boundingBox = rt.accelerators.AxisAlignedBox.INFINITE_BOUNDING_BOX;
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
		Point3f e = r.origin;
		Point3f ce = center;
		
		Vector3f d = r.direction;	
		d.scale(2);
		Vector3f e_min_c = new Vector3f(e.x-ce.x, e.y-ce.y,e.z-ce.z);
		
		Vector3f d2 = r.direction;
		float a = d2.dot(d2);
		
		float b = d.dot(e_min_c);
		float c = e_min_c.dot(e_min_c) - radius*radius;
		
		float root = b*b-4*a*c;
		if(root<0)
			return null;
		
		float t1 = (-b + (float)Math.sqrt(root)) / (2*a);
		float t2 = (-b - (float)Math.sqrt(root)) / (2*a);
		if(t1 <= t2) {
			Point3f position = r.pointAt(t1);
			
			Vector3f wIn = new Vector3f(r.direction);
			wIn.negate();
			wIn.normalize();
			
			Vector3f normal = new Vector3f(position.x - ce.x,position.y - ce.y,position.z - ce.z);
			normal.normalize();
			
			return new HitRecord(t1, position, normal, wIn, this, material, 0.f, 0.f);		
		} else {
			Point3f position = r.pointAt(t2);
			
			Vector3f wIn = new Vector3f(r.direction);
			wIn.negate();
			wIn.normalize();
			
			Vector3f normal = new Vector3f(position.x - ce.x,position.y - ce.y,position.z - ce.z);
			normal.normalize();
			
			return new HitRecord(t2, position, normal, wIn, this, material, 0.f, 0.f);	
		}
	}
	
	public AxisAlignedBox getBoundingBox()
	{
		return boundingBox;
	}
}
