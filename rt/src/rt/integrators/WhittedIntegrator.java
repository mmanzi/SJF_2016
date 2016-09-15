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
public class WhittedIntegrator implements Integrator {

	LightList lightList;
	Intersectable root;
	Scene scene;
	
	private static final int MAX_BOUNCES = 4;
	
	public WhittedIntegrator(Scene scene)
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
		
		if(Math.abs(r.direction.x)<0.0001f && Math.abs(r.direction.y)<0.0001f && r.bounces==0){
			int fl = 23453;
			fl=fl*2;
		}
		
		if (hitRecord != null) {
			
			if(hitRecord.material.hasSpecularReflection() || hitRecord.material.hasSpecularRefraction()){
				
				Spectrum outgoing1 = new Spectrum(0.f, 0.f, 0.f);
				Spectrum outgoing2 = new Spectrum(0.f, 0.f, 0.f);
				
				boolean out1 = false;
				boolean out2 = false;
				
				float n1 = 1.f;
				float n2 = 1.3f;
				
				Vector3f normalV = null;
				Vector3f inputV = null;
				
				if(hitRecord.material.hasSpecularReflection() && r.bounces < WhittedIntegrator.MAX_BOUNCES){
					
					out1 = true;
					
					//Handles reflection
					Vector3f wIn = new Vector3f(hitRecord.w);
					Vector3f n = new Vector3f(hitRecord.normal);
					Vector3f wOut = new Vector3f();
					
					wIn.negate();
					
					Vector3f wInTemp = new Vector3f(wIn);
					n.scale(2*wInTemp.dot(n));
					wOut.sub(wIn,n);
					
					Ray r2 = new Ray(hitRecord.position, wOut);
					r2.bounces = r.bounces+1;
					outgoing1 = new Spectrum(hitRecord.material.evaluateSpecularReflection(hitRecord).brdf);
					outgoing1.mult(integrate(r2));
					
				}
				if(hitRecord.material.hasSpecularRefraction() && r.bounces < WhittedIntegrator.MAX_BOUNCES){
					
					out2 = true;
					
					Vector3f wIn = new Vector3f(hitRecord.w);  //The direction of the source ray-
					Vector3f n = new Vector3f(hitRecord.normal);	//The normal direction
					Vector3f wOut = new Vector3f(); //Output ray direction
					
					normalV = new Vector3f(n);
					inputV = new Vector3f(wIn);
					
					//Calculate if the normal and input are in the same side of material
					wIn.normalize();
					n.normalize();
					
					double alpha1 = Math.acos(wIn.dot(n));
					
					
			//		n1 = 0; 
			//		n2 = 0;
					
					
					if(alpha1 < Math.toRadians(90)){  //Entering material
						n1 = r.popStack();
						n2 = hitRecord.material.getRefractiveIndex();
						r.pushStack(n2);

					}else{

						n1 = r.popStack();
						n2 = 1.f;
						r.pushStack(n2);
						n.negate();
						normalV.negate();
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
					outgoing2 = new Spectrum(hitRecord.material.evaluateSpecularRefraction(hitRecord).brdf);
					outgoing2.mult(integrate(r2));
					
					
				}
				
				
				if(out1 && out2){
					
					float tmp = (float) (Math.pow((n1-n2)/(n1+n2),2));
					float result = normalV.dot(inputV);
					float ratio = (float) (tmp + (1.f - tmp)* (Math.pow((1.f-(result)), 5))); //1 = all reflected, 0 = all refracted
			
				//	if(ratio>1)
				//		System.out.println("hsdjhjdhv");
					
					outgoing = new Spectrum(ratio*outgoing1.r + (1-ratio)*outgoing2.r, 
											ratio*outgoing1.g + (1-ratio)*outgoing2.g, 
											ratio*outgoing1.b + (1-ratio)*outgoing2.b);
				}else if(out1){
					outgoing = outgoing1;
				}else if(out2){
					outgoing = outgoing2;
				}
				
			}else if(r.bounces < WhittedIntegrator.MAX_BOUNCES){
				
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
