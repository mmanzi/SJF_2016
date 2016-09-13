package rt.samplers;

import rt.Sampler;

public class GridSampler implements Sampler{

	@Override
	public float[][] makeSamples(int n, int d) {
		
		float[][] samples = new float[n][d];
		
		int columns = (int)Math.sqrt(n);  //Standard is a 1:1 grid
		float offsetX = 1.f/columns;
		int rows = columns;
		
		for(int i = 1;i <= n-columns*columns; i++){  //Works out if the number of rows can be increased 
			if(columns*(columns+i) <= n){
				rows++;
			}
		}
		float offsetY = 1.f/rows; //x offset
		
		int counter = 0; //Counts the position in the sample array
		
		for(int i = 0; i < columns; i++){  //columns
			for(int e = 0;e < columns;e++){  //rows
				samples[counter][0] = offsetX*i + offsetX/2;  //x position is the grid position plus centre-offset
				samples[counter][1] = offsetY*e + offsetY/2;
				counter++;
				
			} 
		}
		
		return samples;

	}

}
