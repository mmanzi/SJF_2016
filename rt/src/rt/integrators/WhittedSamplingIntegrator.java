package rt.integrators;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Integrator;
import rt.Intersectable;
import rt.LightList;
import rt.Ray;
import rt.Sampler;
import rt.Scene;
import rt.Spectrum;

/**
 * Integrator for Whitted style ray tracing. This is a basic version that needs to be extended!
 */
public class WhittedSamplingIntegrator implements Integrator {

	LightList lightList;
	Intersectable root;
	Scene scene;
	
	private static final int MAX_BOUNCES = 4;
	
	public WhittedSamplingIntegrator(Scene scene)
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
	
		
		float probability = 1.f;
		boolean first = false;
		boolean notSchlick = true;
		if (hitRecord != null) {		
			
			
			
			if(hitRecord.material.hasSpecularReflection() || hitRecord.material.hasSpecularRefraction()){				
				if(hitRecord.material.hasSpecularReflection() && hitRecord.material.hasSpecularRefraction()){
					notSchlick = false;
					if(Math.random() > 0.5){ //chance not to sample reflection
						first = true;
					}			
					probability = 0.5f;
				}
				
				Vector3f normalV = new Vector3f(hitRecord.normal);
				Vector3f inputV = new Vector3f(hitRecord.w);
				
				float n1 = 1.f;
				float n2 = 1.3f;
				
		
				if(hitRecord.material.hasSpecularReflection() && r.bounces < WhittedSamplingIntegrator.MAX_BOUNCES && (!first || notSchlick)){
					
					//Handles reflection
					Vector3f wIn = new Vector3f(hitRecord.w);
					Vector3f n = new Vector3f(hitRecord.normal);
					Vector3f wOut = new Vector3f();
					
			//		normalV = new Vector3f(n);
			//		inputV = new Vector3f(wIn);
					
					
					wIn.negate();
					
					Vector3f wInTemp = new Vector3f(wIn);
					n.scale(2*wInTemp.dot(n));
					wOut.sub(wIn,n);
					
					Ray r2 = new Ray(hitRecord.position, wOut);
					r2.bounces = r.bounces+1;
					outgoing = new Spectrum(hitRecord.material.evaluateSpecularReflection(hitRecord).brdf);
					outgoing.mult(integrate(r2));
					outgoing.mult(1.f/probability);
					
				}else if(hitRecord.material.hasSpecularRefraction() && r.bounces < WhittedSamplingIntegrator.MAX_BOUNCES && (first || notSchlick)){
					
					//Handles refraction
					Vector3f wIn = new Vector3f(hitRecord.w);  //The direction of the source ray-
					Vector3f n = new Vector3f(hitRecord.normal);	//The normal direction
					Vector3f wOut = new Vector3f(); //Output ray direction
					
					
					//Calculate if the normal and input are in the same side of material
					wIn.normalize();
					n.normalize();
					
					double alpha1 = Math.acos(wIn.dot(n));
	
					if(alpha1 < Math.toRadians(90)){  //Entering material
						n1 = r.popStack();
						n2 = hitRecord.material.getRefractiveIndex();
						r.pushStack(n2);

					}else{

						n1 = r.popStack();
						n2 = 1.f;
						r.pushStack(n2);
						n.negate();
			//			normalV.negate();
						alpha1 = Math.acos(wIn.dot(n));
					}	
					
					float aspect = n1/n2;
		
					wIn.negate();		
					double alpha2 = Math.asin(aspect*Math.sin(alpha1));
					
					wIn.scale(aspect);
					n.scale((float) (aspect*Math.cos(alpha1)-Math.cos(alpha2)));
					wOut.add(n, wIn);
					wOut.normalize();
					
					Ray r2 = new Ray(hitRecord.position, wOut);
					r2.copyStack(r.getStack());
					r2.bounces = r.bounces+1;
					outgoing = new Spectrum(hitRecord.material.evaluateSpecularRefraction(hitRecord).brdf);
					outgoing.mult(integrate(r2));
					outgoing.mult(1.f/probability);
					
					
				}
				if(!notSchlick){
					normalV = new Vector3f(hitRecord.normal);
					inputV = new Vector3f(hitRecord.w);
					
					double alpha1 = Math.acos(normalV.dot(inputV));
					
					if(alpha1 < Math.toRadians(90)){  //Entering material
						normalV.negate();
					}
					
					float tmp = (float) (Math.pow((n1-n2)/(n1+n2),2));	
					float result = normalV.dot(inputV);
					float ratio = (float) (tmp + (1.f - tmp)* (Math.pow((1.f-(result)), 5))); //1 = all reflected, 0 = all refracted
						
					outgoing.mult(ratio);
					
				}
			}else if(r.bounces < WhittedSamplingIntegrator.MAX_BOUNCES){
				
				//if hitPosition.material.isSpecularReflective() && r.bounce<threshold
				//			create ray2 = new Ray(hitRecord.position, (mirror_direction at hitPiont.position) 
				//			ray2.bounce = r.bounce+1;
				//			outgoing = hitPosition.material.evaluateSpecularReflective(hitRecord) * integrate(ray2)
				
				for(int i = 0; i < lightList.size(); i++) {
					HitRecord lightHit = lightList.get(i).sample(null);
					Point3f lp = lightHit.position;
	
					
					Vector3f wOut = new Vector3f(
							hitRecord.position.x - lp.x, 
							hitRecord.position.y - lp.y,
							hitRecord.position.z - lp.z);
					wOut.normalize();
					Spectrum light_color = lightHit.material.evaluateEmission(lightHit, wOut);	
					wOut.negate(); //WHY not negate?????
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
