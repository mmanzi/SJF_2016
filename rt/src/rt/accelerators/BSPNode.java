package rt.accelerators;

import java.util.List;

import rt.Intersectable;
import rt.util.StaticVecmath;
import rt.util.StaticVecmath.Axis;

public class BSPNode {

	List<Intersectable> intersectables;
	final AxisAlignedBox boundingBox;
	BSPNode left, right;
	Axis splitAxis;
	/**
	 * Distance from origin to split axis plane
	 */
	float splitAxisDistance;
	
	public BSPNode(AxisAlignedBox boundingBox, Axis splitAxis) {
		this.boundingBox = boundingBox;
		setSplit(splitAxis, StaticVecmath.getDimension(boundingBox.getCenter(), splitAxis));
	}
	
	/**
	 * Initialized without split axis, needs to be set afterwards!
	 * @param boundingBox
	 */
	public BSPNode(AxisAlignedBox boundingBox) {
		this.boundingBox = boundingBox;
	}
	
	public void setSplit(Axis newAxis, float splitAxisDist) {
		this.splitAxis = newAxis;
		this.splitAxisDistance = splitAxisDist;
	}

	public String toString() {
		if (isLeaf())
			return intersectables.toString();
		else return "" + splitAxis + ", dist: " + splitAxisDistance;
	}
	
	public boolean isLeaf() {
		return intersectables != null;
	}
}
