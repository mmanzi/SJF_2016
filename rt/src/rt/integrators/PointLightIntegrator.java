package rt.integrators;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Integrator;
import rt.Intersectable;
import rt.LightGeometry;
import rt.LightList;
import rt.Ray;
import rt.Sampler;
import rt.Scene;
import rt.Spectrum;

/**
 * Integrator for Whitted style ray tracing. This is a basic version that needs to be extended!
 */
public class PointLightIntegrator implements Integrator {

	LightList lightList;
	Intersectable root;
	Scene scene;
	
	public PointLightIntegrator(Scene scene)
	{
		this.lightList = scene.getLightList();
		this.root = scene.getIntersectable();
		this.scene = scene;
	}

	/**
	 * Basic integrator that simply iterates over the light sources and accumulates
	 * their contributions. No shadow testing, reflection, refraction, or 
	 * area light sources, etc. supported.
	 */
	public Spectrum integrate(Ray r) {
		Spectrum outgoing = new Spectrum(0.f, 0.f, 0.f);	
		
		HitRecord hitRecord = root.intersect(r);
		
		if (hitRecord != null) {
			for(int i = 0; i < lightList.size(); i++) {
				HitRecord lightHit = lightList.get(i).sample(null);
				Point3f lp = lightHit.position;
				Vector3f wOut = new Vector3f(
						hitRecord.position.x - lp.x, 
						hitRecord.position.y - lp.y,
						hitRecord.position.z - lp.z);
				wOut.normalize();
				Spectrum light_color = lightHit.material.evaluateEmission(lightHit, wOut);	
				wOut.negate();
				float ndo = Math.max(0.f, hitRecord.normal.dot(wOut));
				light_color.mult(ndo);
				light_color.mult(hitRecord.material.evaluateBRDF(hitRecord, hitRecord.w, wOut));
				outgoing.add(light_color);
			}
			return outgoing;
		} else {
			return new Spectrum(0.f, 0.f, 0.f);
		}
	}

	public float[][] makePixelSamples(Sampler sampler, int n) {
		return sampler.makeSamples(n, 2);
	}

}
