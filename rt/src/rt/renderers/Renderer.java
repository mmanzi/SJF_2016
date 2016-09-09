package rt.renderers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import rt.Scene;
import rt.util.Timer;

public abstract class Renderer {

	protected final Scene scene;

	protected BufferedImage image;
	protected JFrame jf;
	protected JLabel label;
	
	public Renderer(Scene scene) {
		jf = new JFrame("Raytracer");
		label = new JLabel();
		jf.setSize(scene.getFilm().getWidth(),scene.getFilm().getHeight());
		jf.setLocationRelativeTo(null);
	    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.scene = scene;
	}
		
	/**
	 * Renders the scene used to construct this renderer. Will also keep track of time used.
	 */
	public void render() throws InterruptedException, ExecutionException {
		System.out.printf("Rendering scene %s %s, to file %s: \n", 
				scene.getClass().getName(), rendererMessage(), scene.outputFilename);
		Timer timer = new Timer();
		timer.reset();

		// The call to the renderer implementation. Here is where real work gets done.
		renderInternally();
		
		//output image to screen for convenience
		updateFrame();
		
		System.out.println();
		timer.printElapsedTimeToConsole("Image computed in");
	}
	
	/**
	 * Updates the shown frame
	 */
	protected void updateFrame(){
		image = scene.getTonemapper().process(scene.getFilm());
		jf.remove(label);
		label =  new JLabel(new ImageIcon(image));    
		jf.add(label);
		jf.repaint();
	    jf.setVisible(true);
	}
	
	/**
	 * This method has to be overwritten by every renderer implementation
	 */
	protected abstract void renderInternally() throws InterruptedException, ExecutionException;
	
	/**
	 * Some additional message from the renderer implementation for the status
	 * message before rendering starts.
	 */
	protected abstract String rendererMessage();
	
	/** 
	 * Tone map output image and write to file. Should be called after <code>render</code>
	 */
	public void writeImageToFile() {
		BufferedImage image = scene.getTonemapper().process(scene.getFilm());
		try
		{
			ImageIO.write(image, "png", new File(scene.getOutputFilename()+".png"));
		} catch (IOException e) {}
	}

}
