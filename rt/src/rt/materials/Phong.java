package rt.materials;

import javax.vecmath.Vector3f;

import rt.*;

/**
 * Blinn-Phong Shading
 */
public class Phong implements Material {

	Spectrum kd;
	Spectrum ks;
	Spectrum ka;
	int s;
	/**
	 * Note that the parameter value {@param kd} is the diffuse reflectance,
	 * which should be in the range [0,1], a value of 1 meaning all light
	 * is reflected (diffusely), and none is absorbed. The diffuse BRDF
	 * corresponding to {@param kd} is actually {@param kd}/pi.
	 * 
	 * @param kd the diffuse reflectance
	 */
	public Phong(Spectrum kd, Spectrum ks, Spectrum ka, int s)
	{
		this.kd = new Spectrum(kd);
		this.ks = new Spectrum(ks);
		this.ka = new Spectrum(ka);
		this.s = s;
		// Normalize
		this.kd.mult(1/(float)Math.PI);
		this.ks.mult(1/(float)Math.PI);
		this.ka.mult(1/(float)Math.PI);
	}
	
	/**
	 *
	 **/
	public Phong()
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
		Spectrum kdr = new Spectrum(kd);
		kdr.mult(ndo);
		
		Vector3f h = wOut;
		h.add(wIn);
		//h.scale(1.f/(wOut.length() + wIn.length()));
		h.normalize();
		
		float hdo = (float)Math.pow(h.dot(hitRecord.normal), s);
		Spectrum c = new Spectrum(ks);
		c.mult(hdo);
		c.add(ka);
		kdr.add(c);
		
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
	public boolean hasTheThing()
	{
		return false;
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
