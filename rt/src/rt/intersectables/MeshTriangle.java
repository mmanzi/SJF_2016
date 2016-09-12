package rt.intersectables;

import javax.vecmath.Matrix3f;

import rt.HitRecord;
import rt.Intersectable;
import rt.Ray;
import rt.accelerators.AxisAlignedBox;

/**
 * Defines a triangle by referring back to a {@link Mesh}
 * and its vertex and index arrays. 
 */
public class MeshTriangle implements Intersectable {

	private Mesh mesh;
	private int index;
	
	/**
	 * Make a triangle.
	 * 
	 * @param mesh the mesh storing the vertex and index arrays
	 * @param index the index of the triangle in the mesh
	 */
	public MeshTriangle(Mesh mesh, int index)
	{
		this.mesh = mesh;
		this.index = index;		
	}
	
	public HitRecord intersect(Ray r)
	{
		float vertices[] = mesh.vertices;	
		
		// Access the triangle vertices as follows (same for the normals):		
		// 1. Get three vertex indices for triangle
		int v0 = mesh.indices[index*3];
		int v1 = mesh.indices[index*3+1];
		int v2 = mesh.indices[index*3+2];
		
		
		Matrix3f m = new Matrix3f();
		
		//// 2. Access x,y,z coordinates for each vertex
		
		m.m00 = vertices[v0*3];
		m.m01 = vertices[v1*3];
		m.m02 = vertices[v2*3];
		m.m10 = vertices[v0*3+1];
		m.m11 = vertices[v1*3+1];
		m.m12 = vertices[v2*3+1];
		m.m20 = vertices[v0*3+2];
		m.m21 = vertices[v1*3+2];
		m.m22 = vertices[v2*3+2];
		
		// TODO: Return a hitrecord if the ray intersects this triangle, null otherwise.
		
		
		
		
		//rodo: compute intersection with cramers rule on barycentric coordinates
		//given an intersection point, check is it valid? is t positive
		// given valid intersection compute stuff for hitRecord (t, normal, intersection-position, direction from intersection to eye normalized(eye - pos)
		return null;
	}

	@Override
	public AxisAlignedBox getBoundingBox() {
		// TODO Generate some useful bounding box
		return null;
	}

	@Override
	public float surfaceArea() {
		// TODO Compute surface area of this triangle.
		return 0;
	}
	
}
