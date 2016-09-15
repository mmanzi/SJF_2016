package rt.integrators;

import javax.swing.JOptionPane;
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
public class WhittedShadowIntegrator implements Integrator {

	LightList lightList;
	Intersectable root;
	Scene scene;
	
	private static final int MAX_BOUNCES = 5;
	
	public WhittedShadowIntegrator(Scene scene)
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
			
			if(hitRecord.material.hasSpecularReflection() && r.bounces < WhittedShadowIntegrator.MAX_BOUNCES){
				
				Vector3f wIn = hitRecord.w;
				Vector3f n = hitRecord.normal;
				Vector3f wOut = new Vector3f();
				
				if(hitRecord.material.hasTheThing()) {
					n.scale((float)Math.random());
					n.scale((float)Math.random());
				}
				
				wIn.negate();
				
				Vector3f wInTemp = new Vector3f(wIn);
				n.scale(2*wInTemp.dot(n));
				wOut.sub(wIn,n);
				
				Ray r2 = new Ray(hitRecord.position, wOut);
				r2.bounces = r.bounces+1;
				outgoing = new Spectrum(hitRecord.material.evaluateSpecularReflection(hitRecord).brdf);
				//outgoing = hitRecord.material.evaluateSpecularReflection(hitRecord).brdf;
				outgoing.mult(integrate(r2));
				
			}else if(hitRecord.material.hasSpecularReflection() && r.bounces == WhittedShadowIntegrator.MAX_BOUNCES){
				
				
			}else{
				
				//if hitPosition.material.isSpecularReflective() && r.bounce<threshold
				//			create ray2 = new Ray(hitRecord.position, (mirror_direction at hitPiont.position) 
				//			ray2.bounce = r.bounce+1;
				//			outgoing = hitPosition.material.evaluateSpecularReflective(hitRecord) * integrate(ray2)
				
				for(int i = 0; i < lightList.size(); i++) {
					HitRecord lightHit = lightList.get(i).sample(null);
					
					Vector3f direction = new Vector3f(hitRecord.position);
					direction.sub(lightHit.position);
					Ray rshadowRay = new Ray(hitRecord.position, direction);
					rshadowRay.direction.normalize();
					rshadowRay.direction.negate();
					//rshadowRay.origin.negate();
					HitRecord ShadowhitRecord = root.intersect(rshadowRay);
					if(ShadowhitRecord != null) {
						if(ShadowhitRecord.t <= hitRecord.position.distance(lightHit.position) && !ShadowhitRecord.intersectable.equals(hitRecord.intersectable))
							continue;
					}
					
					Point3f lp = lightHit.position;
					
					
					Vector3f wOut = new Vector3f(
							hitRecord.position.x - lp.x, 
							hitRecord.position.y - lp.y,
							hitRecord.position.z - lp.z);
					wOut.normalize();
					Spectrum light_color = lightHit.material.evaluateEmission(lightHit, wOut);	
					wOut.negate();
					light_color.mult(hitRecord.material.evaluateBRDF(hitRecord, wOut, hitRecord.w));
					
					Vector3f p_min_v = new Vector3f(lp.x - hitRecord.position.x, lp.y - hitRecord.position.y, lp.z - hitRecord.position.z);
					float distanceToLightSquared = p_min_v.lengthSquared();
					
					light_color.mult(1.f/distanceToLightSquared);
					
					outgoing.add(light_color);
				}
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
