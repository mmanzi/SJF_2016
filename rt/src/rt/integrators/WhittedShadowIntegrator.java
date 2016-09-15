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
	
	int GridX;// = 30;
	int GridY;// = 30;
	int GridZ;// = 30;
	Vector3f[][][] Grid;// = new Vector3f[GridX][GridY][GridZ];
	
	
	private static final int MAX_BOUNCES = 5;
	
	public WhittedShadowIntegrator(Scene scene)
	{
		this.lightList = scene.getLightList();
		this.root = scene.getIntersectable();
		this.scene = scene;
		
	/*	for(int i = 0; i < GridX; i++)
		for(int j = 0; j < GridY; j++)
		for(int k = 0; k < GridZ; k++)
		{
			Grid[i][j][k] = new Vector3f((float)Math.random(),(float)Math.random(),(float)Math.random());
		}*/
		
		if(scene.getGrid()!=null){
			this.GridX = scene.getGrid().GridX;
			this.GridY = scene.getGrid().GridY;
			this.GridZ = scene.getGrid().GridZ;
			this.Grid = scene.getGrid().Grid;
		}
		
	}
	private Vector3f returnRandomValue(Point3f pos, float scale){
		
		float px = (pos.x+10000) * scale;
		float py = (pos.y+10000) * scale;
		float pz = (pos.z+10000) * scale;
		
		int x = Math.floorMod((int)px, GridX);
		int y = Math.floorMod((int)py, GridY);
		int z = Math.floorMod((int)pz, GridZ);
		
		float x_value = px - (int)px; 	
		float y_value = py - (int)py; 
		float z_value = pz - (int)pz;

	//	x_value = px<0 ? 1.f-x_value : x_value;
	//	y_value = py<0 ? 1.f-y_value : y_value;
	//	z_value = pz<0 ? 1.f-z_value : z_value;
		
		Vector3f[] ecken = new Vector3f[8];
		ecken[0] = Grid[x][y][z];
		ecken[1] = Grid[x==GridX-1 ? 0 : x+1][y][z];
		ecken[2] = Grid[x][y==GridY-1 ? 0 : y+1][z];
		ecken[3] = Grid[x==GridX-1 ? 0 : x+1][y==GridY-1 ? 0 : y+1][z];
		ecken[4] = Grid[x][y][z+1==GridZ ? 0 : z+1];
		ecken[5] = Grid[x==GridX-1 ? 0 : x+1][y][z+1==GridZ ? 0 : z+1];
		ecken[6] = Grid[x][y==GridY-1 ? 0 : y+1][z+1==GridZ ? 0 : z+1];
		ecken[7] = Grid[x==GridX-1 ? 0 : x+1][y==GridY-1 ? 0 : y+1][z+1==GridZ ? 0 : z+1];
		
		Vector3f x1 = interpolate(ecken[0], ecken[1], x_value);
		Vector3f x2 = interpolate(ecken[2], ecken[3], x_value);
		Vector3f x3 = interpolate(ecken[4], ecken[5], x_value);
		Vector3f x4 = interpolate(ecken[6], ecken[7], x_value);
		Vector3f y1 = interpolate(x1, x2, y_value);
		Vector3f y2 = interpolate(x3, x4, y_value);
		Vector3f z1 = interpolate(y1, y2, z_value);
		
		return z1;
	}
	private Vector3f interpolate(Vector3f n1, Vector3f n2, float val)
	{
		Vector3f rtn = new Vector3f(n1);
		rtn.scale(1.f-val);
		Vector3f rtn2 = new Vector3f(n2);
		rtn2.scale(val);
		rtn.add(rtn2);
		return rtn;
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
			
			//if hit material has has the thing then 
			//put position into return random vector3f vlaue =-> use this random vector to perturb hitRecord.n (dont forget to normalize afterwards!!!!!)
			if(hitRecord.material.hasTheThing()) {
				
				Vector3f retn = new Vector3f(0,0,0);//returnRandomValue(hitRecord.normal, 5);
				
				int count = 5;
				int startValue = 5;
				
				for(int i = 0; i < count; i++) {
					Vector3f rt = returnRandomValue(hitRecord.position, startValue);
					retn.x += (float)Math.sin(rt.x*2.f*Math.PI)/ Math.pow(2.f,i);
					retn.y += (float)Math.sin(rt.y*2.f*Math.PI)/ Math.pow(2.f,i);
					retn.z += (float)Math.sin(rt.z*2.f*Math.PI)/ Math.pow(2.f,i);	
					startValue *= 2;
				}
				
				retn.x = retn.x*3.f - (int)(retn.x*3.f);
				retn.y = retn.y*3.f - (int)(retn.y*3.f);
				retn.z = retn.z*3.f - (int)(retn.z*3.f);
				
				
				retn.x -= 0.5f;
				retn.y -= 0.5f;
				retn.z -= 0.5f;
				
				retn.scale(0.05f);
				
				hitRecord.normal.add(retn);
				hitRecord.normal.normalize();
			}
			
			if(hitRecord.material.hasSpecularReflection() && r.bounces < WhittedShadowIntegrator.MAX_BOUNCES){
				
				Vector3f wIn = hitRecord.w;
				Vector3f n = hitRecord.normal;
				Vector3f wOut = new Vector3f();
				
				/*
				if(hitRecord.material.hasTheThing()) {
					n.scale(1 + (float)Math.random());
					n.scale(1 + (float)Math.random());
				}
				*/
			/*	
				if(hitRecord.material.hasTheThing()) {
					n.x+=((float)Math.random()-0.5f)/50.f;
					n.y+=((float)Math.random()-0.5f)/50.f;
					n.normalize();
				}
			*/	
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
