package rt.integrators;

import rt.Integrator;
import rt.IntegratorFactory;
import rt.Scene;

/**
 * Makes a {@link ShadowIntegrator}.
 */
public class WhittedShadowIntegratorFactory implements IntegratorFactory {

	public Integrator make(Scene scene) {
		return new WhittedShadowIntegrator(scene);
	}

	public void prepareScene(Scene scene) {
		// TODO Auto-generated method stub
	}

}
