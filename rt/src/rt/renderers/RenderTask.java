package rt.renderers;

import rt.Integrator;
import rt.Ray;
import rt.Sampler;
import rt.Scene;
import rt.Spectrum;

/**
 * A render task represents a rectangular image region that is rendered by a
 * thread in one chunk.
 */
public class RenderTask implements Runnable {
	public int left, right, bottom, top;
	public Integrator integrator;
	public Scene scene;
	public Sampler sampler;

	public RenderTask(Scene scene, int left, int right, int bottom, int top) {
		this.scene = scene;
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;

		// The render task has its own sampler and integrator. 
		// This way threads don't
		// compete for access to a shared sampler/integrator 
		// and thread contention is reduced.
		integrator = scene.getIntegratorFactory().make(scene);
		sampler = scene.getSamplerFactory().make();
	}

	@Override
	public void run() {
		// Go over every pixel (x, y) of the window this render task is 
		// responsible for.
		for (int j = bottom; j < top; j++) {
			for (int i = left; i < right; i++) {
				// Make random samples for use within this pixel.
				float samples[][] = integrator.makePixelSamples(sampler,
						scene.getSPP());
				// Go over all samples 
				for (int k = 0; k < samples.length; k++) {
					// Make ray
					Ray r = scene.getCamera().makeWorldSpaceRay(i, j,
							samples[k]);

					// Evaluate ray
					Spectrum s = integrator.integrate(r);

					// Write to film
					scene.getFilm().addSample(i + samples[k][0],
							j + samples[k][1], s);
				}
			}
		}
	}
}
