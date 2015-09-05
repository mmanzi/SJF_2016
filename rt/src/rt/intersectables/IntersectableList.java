package rt.intersectables;

import java.util.LinkedList;
import java.util.Iterator;

import javax.vecmath.Point3f;

import rt.*;
import rt.accelerators.AxisAlignedBox;

public class IntersectableList extends Aggregate {

	public LinkedList<Intersectable> list;
	private AxisAlignedBox bbox;
	private float surfaceArea;
	
	public IntersectableList()
	{
		list = new LinkedList<Intersectable>();
		bbox = new AxisAlignedBox(new Point3f(0,0,0), new Point3f(0,0,0));
		surfaceArea = 0.f;
	}
	
	public void add(Intersectable i)
	{
		AxisAlignedBox b = i.getBoundingBox();
		this.bbox = bbox.unionWith(b);
		surfaceArea += i.surfaceArea();
		list.add(i);
	}
	
	public Iterator<Intersectable> iterator() {
		return list.iterator();
	}

	public AxisAlignedBox getBoundingBox() {
		return bbox;
	}
	
	public float surfaceArea()
	{
		return surfaceArea;
	}
	
	public Material getMaterial()
	{
		return null;
	}
}
