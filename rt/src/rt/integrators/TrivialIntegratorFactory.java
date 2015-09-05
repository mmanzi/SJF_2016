package rt.integrators;

import rt.Integrator;
import rt.IntegratorFactory;
import rt.Scene;

public class TrivialIntegratorFactory implements IntegratorFactory {

	public Integrator make(Scene scene)
	{
		return new TrivialIntegrator(scene);
	}
	
	public void prepareScene(Scene scene)
	{		
	}

}
