package rt;

import java.util.Stack;

import javax.vecmath.*;

/**
 * A ray represented by an origin and a direction.
 */
public class Ray {

	public Point3f origin;
	public Vector3f direction;
	public int bounces;
	private Stack<Float> refractive_index;
	
	public Ray(Point3f origin, Vector3f direction)
	{
		this.origin = new Point3f(origin); 
		this.direction = new Vector3f(direction);
		this.bounces = 0;
		refractive_index = new Stack<Float>();
		
	}
	
	public Point3f pointAt(float t) 
	{
		Point3f p = new Point3f(direction);
		p.scaleAdd(t, origin); //t*p + origin //p=d, origin=e
		return p;
	}
	
	public Stack<Float> getStack(){
		return refractive_index;
	}
	
	public void copyStack(Stack<Float> stack){
		refractive_index = (Stack<Float>)stack.clone();
	}
	
	public void pushStack(float eta){
		refractive_index.push(new Float(eta));
	}
	
	public float peekStack(){
		return refractive_index.peek().floatValue();
	}
	
	public float popStack(){
		return refractive_index.pop().floatValue();
	}
}
