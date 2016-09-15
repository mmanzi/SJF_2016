package rt;

/**
 * Defines scene properties that need to be made accessible to the renderer. 
 * @param <ProceduralGrid>
 */
public abstract class Scene<ProceduralGrid> {

	public String outputFilename;
	protected int SPP;
	protected int width;
	protected int height;
	protected Camera camera;
	protected Film film;
	protected IntegratorFactory integratorFactory;
	protected SamplerFactory samplerFactory;
	protected Tonemapper tonemapper;
	protected Intersectable root;
	protected LightList lightList;
	protected ProceduralGrid proceduralGrid;
	
	public IntegratorFactory getIntegratorFactory() {
		return integratorFactory;
	}

	public SamplerFactory getSamplerFactory() {
		return samplerFactory;
	}
	
	public String getOutputFilename()
	{
		return outputFilename;
	}
	
	public Camera getCamera() {
		return camera;
	}

	public Film getFilm() {
		return film;
	}

	public Intersectable getIntersectable() {
		return root;
	}
	
	public LightList getLightList() {
		return lightList;
	}

	public int getSPP() {
		return SPP;
	}
	
	public Tonemapper getTonemapper()
	{
		return tonemapper;
	}
	
	public void prepare()
	{
	}
	
	public ProceduralGrid getGrid(){
		return proceduralGrid;
	}

}
