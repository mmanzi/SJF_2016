package rt.materials;

import javax.vecmath.Vector3f;

import rt.*;

import java.io.*;

/**
 * Procedural Textures
 */
public class Procedural implements Material {

	Spectrum t1;
	Spectrum t2;
	Spectrum temp;
	float scale = 2;
	/**
	 * Note that the parameter value {@param kd} is the diffuse reflectance,
	 * which should be in the range [0,1], a value of 1 meaning all light
	 * is reflected (diffusely), and none is absorbed. The diffuse BRDF
	 * corresponding to {@param kd} is actually {@param kd}/pi.
	 * 
	 * @param kd the diffuse reflectance
	 */
	public Procedural(Spectrum t1, Spectrum t2)
	{
		this.t1 = new Spectrum(t1);
		this.t2 = new Spectrum(t2);
	}
	
	/**
	 *
	 **/
	public Procedural()
	{
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
		Spectrum kdr;
		if(Math.floor(hitRecord.position.x * 10) % scale == 0) {
			 kdr = new Spectrum(t2);
		} else if(Math.floor(hitRecord.position.z * 10) % scale == 0) {
			kdr = new Spectrum(t2);
		} else {
			 kdr = new Spectrum(t1);
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
