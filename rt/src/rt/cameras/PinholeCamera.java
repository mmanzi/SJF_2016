package rt.cameras;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import rt.Camera;
import rt.Ray;

/**
 * A simple camera model of a movable camera.
 */
public class PinholeCamera implements Camera {

	private Matrix4f m;
	private int width;
	private int height;
	
	private float t;
	private float r;

	private Point3f e;
	
	public PinholeCamera(Point3f eye, Point3f lookAt, Vector3f up, 
			float fov, float aspect, int width, int height) {
		
		//Create and normalize the three vectors signifying the camera-coordinate system
		Vector3f w = new Vector3f(eye.x-lookAt.x, eye.y - lookAt.y, eye.z - lookAt.z);
		w.normalize();
		
		Vector3f u = new Vector3f();
		u.cross(up, w);   
		u.normalize();
		
		Vector3f v = new Vector3f();
		v.cross(w, u);     //v is at a right angle to u and w and therefore the cross product
		
		e = eye;
		
		this.width = width;
		this.height = height;
		
		//Create the matrix 
		m = new Matrix4f();
		
		m.m00 = u.x;
		m.m10 = u.y;
		m.m20 = u.z;
		m.m30 = 0;  //0 for vector
		
		m.m01 = v.x;
		m.m11 = v.y;
		m.m21 = v.z;
		m.m31 = 0;
		
		m.m02 = w.x;
		m.m12 = w.y;
		m.m22 = w.z;
		m.m32 = 0;
		
		m.m03 = e.x;
		m.m13 = e.y;
		m.m23 = e.z;
		m.m33 = 1;  //1 for point
		
		t = (float) Math.tan((double)Math.toRadians(fov/2.f));
		r = aspect*t;
		
		m.invert();
		
		// TODO: Use parameters passed to create camera matrix.  -- Done
	}
	
	@Override
	public Ray makeWorldSpaceRay(int i, int j, float[] sample) {
		// TODO: Create a ray that originates in eye and goes through pixel (i, j) with offset given from sample[]. 
		// HINT: Make sure the direction of the ray is normalized.
		
		float u = (float) (-t + (2*t)*((i+sample[0])/width));
		float v = (float) (-r + (2*r)*((j+sample[1])/height));
		
		/*Vector3f p = new Vector3f();
		
		Vector4f u1 = new Vector4f();
		m.getColumn(0, u1);
		Vector4f v1 = new Vector4f();
		m.getColumn(1, v1);
		Vector4f w1 = new Vector4f();
		m.getColumn(0, w1);		
		u1.scale(u);
		v1.scale(v);
		w1.scale(-1);
		Vector4f sum = new Vector4f();	
		sum.add(u1,v1);
		sum.add(sum, w1);
		Ray ray = new Ray(e, new Vector3f(sum.x, sum.y, sum.z));
		*/
		
		Vector4f s = new Vector4f(u,v,-1,1);
		m.transform(s);
		s.normalize();
		Ray ray = new Ray(e, new Vector3f(s.x, s.y, s.z));
		
		return ray;
	}

}
