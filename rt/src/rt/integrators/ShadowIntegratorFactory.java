package rt.integrators;

import rt.Integrator;
import rt.IntegratorFactory;
import rt.Scene;

/**
 * Makes a {@link ShadowIntegrator}.
 */
public class ShadowIntegratorFactory implements IntegratorFactory {

	public Integrator make(Scene scene) {
		return new ShadowIntegrator(scene);
	}

	public void prepareScene(Scene scene) {
		// TODO Auto-generated method stub
	}

}
