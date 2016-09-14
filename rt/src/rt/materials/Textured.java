package rt.materials;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.vecmath.Vector3f;

import rt.HitRecord;
import rt.Material;
import rt.Spectrum;

public class Textured implements Material {

	private BufferedImage texture;
	private BufferedImage bumpMap;

	public Textured(String textureFileName, String bumpMapFileName) {
		try {
			texture = ImageIO.read(new File(textureFileName));
			if (bumpMapFileName != null)
				bumpMap = ImageIO.read(new File(bumpMapFileName));
		} catch (IOException e) {
			System.err.println("Could not load texture: ");
			e.printStackTrace();
		}
	}

	@Override
	public Spectrum evaluateBRDF(HitRecord hitRecord, Vector3f wOut,
			Vector3f wIn) {
		// TODO: Give back the appropriate color for the given hit record.
		// TODO: For bump map, also adapt normal of hitRecord.
		return null;
	}

	@Override
	public Spectrum evaluateEmission(HitRecord hitRecord, Vector3f wOut) {
		return null;
	}

	@Override
	public boolean hasSpecularReflection() {
		return false;
	}

	@Override
	public ShadingSample evaluateSpecularReflection(HitRecord hitRecord) {
		return null;
	}

	@Override
	public boolean hasSpecularRefraction() {
		return false;
	}

	@Override
	public ShadingSample evaluateSpecularRefraction(HitRecord hitRecord) {
		return null;
	}
	
	@Override
	public float getRefractiveIndex() {
		return 0.f;                           //Returns 0 for no refraction
	}
	

	@Override
	public ShadingSample getShadingSample(HitRecord hitRecord, float[] sample) {
		// TODO: Implement this for path tracing.
		return null;
	}

	@Override
	public ShadingSample getEmissionSample(HitRecord hitRecord, float[] sample) {
		return null;
	}

	@Override
	public boolean castsShadows() {
		return true;
	}

}
