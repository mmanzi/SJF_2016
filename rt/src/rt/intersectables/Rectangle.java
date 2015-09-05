package rt.intersectables;

import javax.vecmath.*;

import rt.*;
import rt.accelerators.AxisAlignedBox;
import rt.materials.*;
import rt.util.StaticVecmath;

public class Rectangle implements Intersectable {

	protected Vector3f bottomLeft;
	protected Vector3f top;
	protected Vector3f right;
	protected Vector3f normal;
	public Material material;
	
	public Rectangle(Vector3f bottomLeft, Vector3f right, Vector3f top)
	{
		material = new Diffuse();
		
		this.bottomLeft = new Vector3f(bottomLeft);
		this.right = new Vector3f(right);
		this.top = new Vector3f(top);
		normal = new Vector3f();
		normal.cross(right, top);
		normal.normalize();
	}
	
	public Rectangle(Rectangle r)
	{
		material = new Diffuse();
		
		this.bottomLeft = new Vector3f(r.bottomLeft);
		this.right = new Vector3f(r.right);
		this.top = new Vector3f(r.top);
		this.normal = new Vector3f(r.normal);
	}

	public HitRecord intersect(Ray r) {
		
		Matrix3f m = new Matrix3f(r.direction.x, -right.x, -top.x,
								  r.direction.y, -right.y, -top.y,
								  r.direction.z, -right.z, -top.z);
		
		try
		{
			m.invert();
		} catch(SingularMatrixException e)
		{
			return null;
		}
		
		Vector3f t = new Vector3f(-r.origin.x+bottomLeft.x, -r.origin.y+bottomLeft.y, -r.origin.z+bottomLeft.z);
		m.transform(t);
		
		if(t.y>=0.f && t.y<=1.f && t.z>=0.f && t.z<=1.f)
		{
			Point3f position = r.pointAt(t.x);
			Vector3f wIn = new Vector3f(r.direction);
			wIn.negate();
			wIn.normalize();
			
			if(t.x>0) 
				return new HitRecord(t.x, position, normal, wIn, this, material, t.y, t.z); 
			else 
				return null;
		} else
		{
			return null;
		}
	}
	
	public AxisAlignedBox getBoundingBox() {
		Point3f min = new Point3f(bottomLeft);
		Point3f max = new Point3f(bottomLeft);
		Point3f bottomPlusTop = new Point3f(bottomLeft);
		bottomPlusTop.add(top);
		Point3f bottomPlusRight = new Point3f(bottomLeft);
		bottomPlusTop.add(right);

		StaticVecmath.elementwiseMax(max, bottomPlusTop);
		StaticVecmath.elementwiseMax(max, bottomPlusRight);
		StaticVecmath.elementwiseMin(min, bottomPlusTop);
		StaticVecmath.elementwiseMin(min, bottomPlusRight);

		return new AxisAlignedBox(min, max);
	}

	public float surfaceArea() {
		return top.length()*right.length();
	}
	
}
