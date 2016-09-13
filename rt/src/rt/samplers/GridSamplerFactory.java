package rt.samplers;

import rt.Sampler;
import rt.SamplerFactory;

/**
 * Makes a {@link RandomSampler}.
 */
public class GridSamplerFactory implements SamplerFactory {

	public Sampler make() {
		return new GridSampler();
	}

}
