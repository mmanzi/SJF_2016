package rt.integrators;

import rt.HitRecord;
import rt.Integrator;
import rt.Ray;
import rt.Sampler;
import rt.Scene;
import rt.Spectrum;

/**
 * A very simple integrator to get started. Just takes the color of the
 * intersectable hit.
 */
public class TrivialIntegrator implements Integrator {

	Scene scene;

	public TrivialIntegrator(Scene scene) {
		this.scene = scene;
	}

	public Spectrum integrate(Ray r) {
		HitRecord hitRecord = scene.getIntersectable().intersect(r);

		if (hitRecord != null) {
			//return new Spectrum(1.f, 1.f, 1.f);
			// Only works with diffuse material
			Spectrum color = hitRecord.material.evaluateBRDF(hitRecord, null,
					null);
			return color;
		} else {
			return new Spectrum(0.f, 0.f, 0.f);
		}
	}

	@Override
	public float[][] makePixelSamples(Sampler sampler, int n) {
		return sampler.makeSamples(n, 2);
	}
}
