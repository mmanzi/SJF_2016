package rt.materials;

import javax.vecmath.Vector3f;

import rt.*;

/**
 * A basic diffuse material.
 */
public class Mirrored implements Material {

	Spectrum kd;
	
	
	
	/**
	 * Note that the parameter value {@param kd} is the mirrored reflectance,
	 * which should be in the range [0,1], a value of 1 meaning all light
	 * is reflected , and none is absorbed. 
	 * 
	 * @param kd the mirrored reflectance
	 */
	public Mirrored(Spectrum kd)
	{
		this.kd = kd;
	}
	
	/**
	 * Default mirrored material with reflectance (1,1,1).
	 */
	public Mirrored()
	{
		this(new Spectrum(1.f, 1.f, 1.f));
	}

	/**
	 * Returns the recursively calculated value.
	 * 
	 *  @param wOut outgoing direction, by convention towards camera
	 *  @param wIn incident direction, by convention towards light
	 *  @param hitRecord hit record to be used
	 */
	public Spectrum evaluateBRDF(HitRecord hitRecord, Vector3f wOut, Vector3f wIn) {
		return null;
	}

	public boolean hasSpecularReflection()
	{
		return true;
	}
	
	public ShadingSample evaluateSpecularReflection(HitRecord hitRecord)
	{
		ShadingSample sample = new ShadingSample();
		sample.brdf = new Spectrum(1.f,1.f,1.f);
		return sample;
	}
	public boolean hasSpecularRefraction()
	{
		return false;
	}

	public ShadingSample evaluateSpecularRefraction(HitRecord hitRecord)
	{
		ShadingSample sample = new ShadingSample();
		sample.brdf = new Spectrum(1.f,1.f,1.f);
		return sample;
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
