package rt.cameras;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.Camera;
import rt.Ray;

/**
 * A simple camera model of a movable camera.
 */
public class PinholeCamera implements Camera {

	public PinholeCamera(Point3f eye, Point3f lookAt, Vector3f up, 
			float fov, float aspect, int width, int height) {
		// TODO: Use parameters passed to create camera matrix.
	}
	
	@Override
	public Ray makeWorldSpaceRay(int i, int j, float[] sample) {
		// TODO: Create a ray that originates in eye and goes through pixel (i, j) with offset given from sample[]. 
		// HINT: Make sure the direction of the ray is normalized.
		return null;
	}

}
