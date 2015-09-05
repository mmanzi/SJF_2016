package rt.lightsources;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.*;
import rt.accelerators.AxisAlignedBox;
import rt.materials.PointLightMaterial;

/**
 * Implements a point light using a {@link rt.materials.PointLightMaterial}.
 */
public class PointLight implements LightGeometry {

	Vector3f position;
	PointLightMaterial pointLightMaterial;
	
	public PointLight(Vector3f position, Spectrum emission)
	{
		this.position = new Vector3f(position);
		pointLightMaterial = new PointLightMaterial(emission);
	}
	
	/**
	 * A ray never hit a point.
	 */
	public HitRecord intersect(Ray r) {
		return null;
	}

	public AxisAlignedBox getBoundingBox() {
		return null;
	}

	public float surfaceArea() {
		return 0;
	}

	/**
	 * Sample a point on the light geometry. On a point light,
	 * always return light position with probability one. 
	 * Set normal to null.
	 */
	public HitRecord sample(float[] s) {
		HitRecord hitRecord = new HitRecord();
		hitRecord.position = new Point3f(position);
		hitRecord.material = pointLightMaterial;
		hitRecord.normal = null;
		hitRecord.p = 1.f;
		return hitRecord;
	}

}
