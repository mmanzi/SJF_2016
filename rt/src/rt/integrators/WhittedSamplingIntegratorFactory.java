package rt.integrators;

import rt.Integrator;
import rt.IntegratorFactory;
import rt.Scene;

/**
 * Makes a {@link PointLightIntegrator}.
 */
public class WhittedSamplingIntegratorFactory implements IntegratorFactory {

	public Integrator make(Scene scene) {
		return new WhittedSamplingIntegrator(scene);
	}

	public void prepareScene(Scene scene) {
		// TODO Auto-generated method stub
	}

}
