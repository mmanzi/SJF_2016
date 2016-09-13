package rt.intersectables;

import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Intersectable;
import rt.Ray;
import rt.Spectrum;
import rt.accelerators.AxisAlignedBox;
import rt.materials.Diffuse;

/**
 * Defines a triangle by referring back to a {@link Mesh}
 * and its vertex and index arrays. 
 */
public class MeshTriangle implements Intersectable {

	private Mesh mesh;
	private int index;
	
	private Vector3f normal;
	
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
		
		
		
		
		// 2. Access x,y,z coordinates for each vertex
		float a0 = vertices[v0*3];
		float a1 = vertices[v0*3+1];
		float a2 = vertices[v0*3+2];
		float b0 = vertices[v1*3];
		float b1 = vertices[v1*3+1];
		float b2 = vertices[v1*3+2];
		float c0 = vertices[v2*3];
		float c1 = vertices[v2*3+1];
		float c2 = vertices[v2*3+2];
		

		/*float a0 = vertices[v0*3];
		float a1 = vertices[v1*3];
		float a2 = vertices[v2*3];
		float b0 = vertices[v0*3+1];
		float b1 = vertices[v1*3+1];
		float b2 = vertices[v2*3+1];
		float c0 = vertices[v0*3+2];
		float c1 = vertices[v1*3+2];
		float c2 = vertices[v2*3+2];*/
		
		
		Vector3f s1 = new Vector3f(a0-b0,a1-b1,a2-b2);
		Vector3f s2 = new Vector3f(a0-c0,a1-c1,a2-c2);
		
		Vector3f s3 = new Vector3f(a0-r.origin.x,a1-r.origin.y,a2-r.origin.z);
		
		Matrix3f f0 = new Matrix3f();
		f0.setColumn(0, s1);
		f0.setColumn(1, s2);
		f0.m02 = r.direction.x;
		f0.m12 = r.direction.y;
		f0.m22 = r.direction.z;
		
		Matrix3f f1 = new Matrix3f(f0);
		f1.setColumn(0, s3);
		
		Matrix3f f2 = new Matrix3f(f0);
		f2.setColumn(1, s3);
		
		Matrix3f f3 = new Matrix3f(f0);
		f3.setColumn(2, s3);
		
	
		float beta = f1.determinant()/f0.determinant();
		float gamma = f2.determinant()/f0.determinant();
		float t = f3.determinant()/f0.determinant();
		
		//if(beta+gamma <= 1 && beta >= 0 && gamma >= 0){
		//	System.out.println(t);
		//}
	
		
		
		
		if(beta+gamma <= 1 && beta >= 0 && gamma >= 0 && t>=0){
			
		//	System.out.println("hello");
			Point3f position = r.pointAt(t);
			
			Vector3f wIn = new Vector3f(r.direction);
			wIn.negate();
			wIn.normalize();
			
			float normals[] = mesh.normals;	
			float na0 = normals[v0*3];
			float na1 = normals[v0*3+1];
			float na2 = normals[v0*3+2];
			float nb0 = normals[v1*3];
			float nb1 = normals[v1*3+1];
			float nb2 = normals[v1*3+2];
			float nc0 = normals[v2*3];
			float nc1 = normals[v2*3+1];
			float nc2 = normals[v2*3+2];
			
			Vector3f na = new Vector3f(na0,na1,na2);
			Vector3f nb = new Vector3f(nb0,nb1,nb2);
			Vector3f nc = new Vector3f(nc0,nc1,nc2);
			Vector3f normal = new Vector3f();
			na.scale(1-gamma-beta);
			nb.scale(beta);
			nc.scale(gamma);
			
			normal.add(na,nb);
			normal.add(nc);
	
			return new HitRecord(t,  position, normal, wIn, this, new Diffuse(new Spectrum(1.f, 1.f, 1.f)), 0.f, 0.f);
		}else{
			return null;
		}
		
		// TODO: Return a hitrecord if the ray intersects this triangle, null otherwise.
		
		
		
		
		//TODO: compute intersection with cramers rule on barycentric coordinates
		//given an intersection point, check is it valid? is t positive
		// given valid intersection compute stuff for hitRecord (t, normal, intersection-position, direction from intersection to eye normalized(eye - pos)
		
	}

	@Override
	public AxisAlignedBox getBoundingBox() {
		
		float vertices[] = mesh.vertices;	
		
		// Access the triangle vertices as follows (same for the normals):		
		// 1. Get three vertex indices for triangle
		int v0 = mesh.indices[index*3];
		int v1 = mesh.indices[index*3+1];
		int v2 = mesh.indices[index*3+2];

		// 2. Access x,y,z coordinates for each vertex
		/*float a0 = vertices[v0*3];
		float a1 = vertices[v0*3+1];
		float a2 = vertices[v0*3+2];
		float b0 = vertices[v1*3];
		float b1 = vertices[v1*3+1];
		float b2 = vertices[v1*3+2];
		float c0 = vertices[v2*3];
		float c1 = vertices[v2*3+1];
		float c2 = vertices[v2*3+2];*/
		
		float smallestX = Math.min(vertices[v0*3], Math.min(vertices[v1*3], vertices[v2*3]));
		float smallestY = Math.min(vertices[v0*3+1], Math.min(vertices[v1*3+1], vertices[v2*3+1]));
		float smallestZ = Math.min(vertices[v0*3+2], Math.min(vertices[v1*3+2], vertices[v2*3+2]));
		float largestX = Math.max(vertices[v0*3], Math.max(vertices[v1*3], vertices[v2*3]));
		float largestY = Math.max(vertices[v0*3+1], Math.max(vertices[v1*3+1], vertices[v2*3+1]));
		float largestZ = Math.max(vertices[v0*3+2], Math.max(vertices[v1*3+2], vertices[v2*3+2]));
		
		

		return new AxisAlignedBox(new Point3f(smallestX, smallestY, smallestZ), new Point3f(largestX, largestY, largestZ));
	}

	@Override
	public float surfaceArea() {
		// TODO Compute surface area of this triangle.
		return 0;
	}
	
}
