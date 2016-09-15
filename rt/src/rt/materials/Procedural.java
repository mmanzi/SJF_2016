package rt.materials;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.*;

import java.io.*;
import java.util.Random;

/**
 * Procedural Textures
 */
public class Procedural implements Material {

	Spectrum t1;
	Spectrum t2;
	Spectrum temp;
	float scale;
	int mal;
	
	int scale_grid = 5;
	static int GridX = 200;
	static int GridY = 200;
	float[][] perlNoiseGrid = new float[GridX][GridY]; 
	int loopcount = 1;
	int initialValue = 1;
	
	int method = 1;
	/**
	 * Note that the parameter value {@param kd} is the diffuse reflectance,
	 * which should be in the range [0,1], a value of 1 meaning all light
	 * is reflected (diffusely), and none is absorbed. The diffuse BRDF
	 * corresponding to {@param kd} is actually {@param kd}/pi.
	 * 
	 * @param kd the diffuse reflectance
	 */
	public Procedural(Spectrum t1, Spectrum t2, float scale, int mal)
	{
		this.t1 = new Spectrum(t1);
		this.t2 = new Spectrum(t2);
		this.scale = scale;
		this.mal = mal;
		this.method = 1;
	}
	public Procedural(Spectrum t1, Spectrum t2, int loopcount, int initialValue)
	{
		this.t1 = new Spectrum(t1);
		this.t2 = new Spectrum(t2);
		this.method = 2;
		
		this.loopcount = loopcount;
		this.initialValue = initialValue;
		
		Random rnd = new Random(/*123456*/); //Seed me!
		
		for(int i = 0; i < GridX; i++) {
			for(int j = 0; j < GridY; j++) {
				perlNoiseGrid[i][j] = (float)rnd.nextFloat();
			}
		}
	}
	
	/**
	 *
	 **/
	public Procedural()
	{
		
	}

	
	
	private float returnRandomValue(Point3f pos, float scale){
		
		float px = (pos.x+10000) * scale;
		float py = (pos.y+10000) * scale;
		
		int x = Math.floorMod((int)px, GridX);
		int y = Math.floorMod((int)py, GridY);

		int test = Math.floorMod(-1, 4);
	//	int x = px < 0 ? (GridX - 1) - Math.floorMod((int)(px * scale), (GridX - 1))-1 : Math.floorMod((int)(px * scale), (GridX - 1));
	//	int y = py < 0 ? (GridY - 1) - Math.floorMod((int)(py * scale), (GridY - 1))-1 : Math.floorMod((int)(py * scale), (GridY - 1));
		
		float x_value = px - (int)px; 	
		float y_value = py - (int)py; 

		x_value = px<0 ? 1.f-x_value : x_value;
		y_value = py<0 ? 1.f-y_value : y_value;
		
		float[] ecken = new float[4];
		ecken[0] = perlNoiseGrid[x][y];
		ecken[1] = perlNoiseGrid[x==GridX-1 ? 0 : x+1][y];
		ecken[2] = perlNoiseGrid[x][y==GridY-1 ? 0 : y+1];
		ecken[3] = perlNoiseGrid[x==GridX-1 ? 0 : x+1][y==GridY-1 ? 0 : y+1];

		
		float x1 = interpolate(ecken[0], ecken[1], x_value);
		float x2 = interpolate(ecken[2], ecken[3], x_value);	
		float y1 = interpolate(x1, x2, y_value);
		
		return (float)y1;
	}
	
	private float interpolate(float n1, float n2, float val)
	{
		return (1.f-val)*n1 + (val)*n2;
	}
	
	/**
	 * Returns diffuse BRDF value, that is, a constant.
	 * 
	 *  @param wOut outgoing direction, by convention towards camera
	 *  @param wIn incident direction, by convention towards light
	 *  @param hitRecord hit record to be used
	 */
	public Spectrum evaluateBRDF(HitRecord hitRecord, Vector3f wOut, Vector3f wIn) {
		float ndo = Math.max(0.f, hitRecord.normal.dot(wOut));
		Spectrum kdr = new Spectrum(t1);
		if(method == 1) {
			if(Math.floor(hitRecord.position.x * mal) % scale == 0) {
				 kdr = new Spectrum(t2);
			} else if(Math.floor(hitRecord.position.z * mal) % scale == 0) {
				kdr = new Spectrum(t2);
			} else {
				 kdr = new Spectrum(t1);
			}
		} else if(method == 2) {
			kdr = new Spectrum(t1);

			float value = 0;
			
			int count = loopcount;
			int startValue = initialValue;
			
			for(int i = 0; i < count; i++) {
				float rt = returnRandomValue(hitRecord.position, startValue);
				value += (float)Math.sin(rt*2.f*Math.PI)/ Math.pow(2.f,i);
				startValue *= 2;
			}
			
			//value = Math.max(0.f,value);
			value = Math.abs(value);
			/*
			float rand1 = returnRandomValue(hitRecord.position, 5.f);
			float rand2 = returnRandomValue(hitRecord.position, 10.f);
			float rand3 = returnRandomValue(hitRecord.position, 20.f);
			float rand4 = returnRandomValue(hitRecord.position, 40.f);
			
			float test = (float)(Math.sin(rand1)+Math.sin(rand2)/2.f+Math.sin(rand3)/4.f+Math.sin(rand4)/8.f);
			*/
			
			//kdr.mult(value);
			float test = interpolate(0, 1, value);
			kdr.mult(new Spectrum(test, test, test));
		}
		kdr.mult(ndo);
		return new Spectrum(kdr);
	}

	public boolean hasSpecularReflection()
	{
		return false;
	}
	
	public ShadingSample evaluateSpecularReflection(HitRecord hitRecord)
	{
		return null;
	}
	public boolean hasSpecularRefraction()
	{
		return false;
	}

	public ShadingSample evaluateSpecularRefraction(HitRecord hitRecord)
	{
		return null;
	}
	
	public float getRefractiveIndex() {
		return 0.f;                           //Returns 0 for no refraction
	}
	
	
	// To be implemented for path tracer!
	public ShadingSample getShadingSample(HitRecord hitRecord, float[] sample)
	{
		return null;	
	}
		
	public boolean castsShadows()
	{
		return true;
	}
	
	public Spectrum evaluateEmission(HitRecord hitRecord, Vector3f wOut) {
		return new Spectrum(0.f, 0.f, 0.f);
	}

	public ShadingSample getEmissionSample(HitRecord hitRecord, float[] sample) {
		return new ShadingSample();
	}
	
}
