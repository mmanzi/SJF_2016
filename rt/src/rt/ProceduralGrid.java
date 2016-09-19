package rt;

import javax.vecmath.Vector3f;

public class ProceduralGrid {

	public int GridX = 30;
	public int GridY = 30;
	public int GridZ = 30;
	public Vector3f[][][] Grid = new Vector3f[GridX][GridY][GridZ];
	
	public ProceduralGrid(){
		for(int i = 0; i < GridX; i++)
		for(int j = 0; j < GridY; j++)
		for(int k = 0; k < GridZ; k++)
		{
			Grid[i][j][k] = new Vector3f((float)Math.random(),(float)Math.random(),(float)Math.random());
		}
	}
}
