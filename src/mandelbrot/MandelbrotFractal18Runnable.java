package mandelbrot;

import org.apache.commons.math3.complex.Complex;
import java.awt.image.BufferedImage;


public class MandelbrotFractal18Runnable implements Runnable {
	private BufferedImage image;
	private int[] colors;
	
	private int startRow, endRow;
	private int startCol, endCol;
	
	private int maxIterations;
	private boolean isQuiet;
	private int numberOfThreads;
	private float firstRe, secondRe, firstIm, secondIm;
	
	MandelbrotFractal18Runnable(BufferedImage image, int[] colors, int startRow, int endRow, int startCol, int endCol, boolean isQuiet, int numberOfThreads,
			float firstRe, float secondRe, float firstIm, float secondIm) {
		this.image = image;
		this.colors = colors;
		
		this.startRow = startRow;
		this.endRow = endRow;
		this.startCol = startCol;
		this.endCol = endCol;
		
		this.maxIterations = colors.length;
		this.isQuiet = isQuiet;
		this.numberOfThreads = numberOfThreads;
		
		this.firstRe = firstRe;
		this.secondRe = secondRe;
		this.firstIm = firstIm;
		this.secondIm = secondIm;
	}
	
	private int computeSteps(Complex number) {
		Complex currentZ = new Complex(0.0, 0.0);
		Complex two = new Complex(2.0, 0.0);
		
		int steps = 0;
		
		float maxRadius = 4.0f;
		while (currentZ.abs() <= maxRadius && /* isBetween(currentZ.getReal(), firstRe, secondRe) && isBetween(currentZ.getImanginary(), firstIm, secondIm) && */ steps < maxIterations) {
			currentZ = currentZ.exp().add(currentZ).pow(two).add(number);
			
			++steps;
		}
		
		return steps;
	}
	
	private boolean isBetween(double middle, double first, double right) {
		return ( first <= middle && middle <= right );
	}
	
	public void run() {
		final long startTimeThread = System.currentTimeMillis();
		
		if (! this.isQuiet) {
			System.out.println("Thread-<" + this.numberOfThreads + "> started.");
		}
		
		final double lengthRe = secondRe - firstRe;
		final double lengthIm = secondIm - firstIm;
		
		int height = this.image.getHeight();
		int width = this.image.getWidth();
		
		float divisionRe = (float) lengthRe / width;
		float divisionIm = (float) lengthIm / height;
		
		// Re -> width -> col
		// Im -> height -> row
		
		for (int col = this.startCol; col < this.endCol; ++col) {
			float realPart = firstRe + col * divisionRe;
			
			for (int row = this.startRow; row < this.endRow; ++row) {
				float imaginaryPart = firstIm + row * divisionIm;
				
				int steps = computeSteps(new Complex(realPart, imaginaryPart));
				
				if (steps < this.maxIterations) {
					this.image.setRGB(col, row, this.colors[steps]);
				} else {
					this.image.setRGB(col, row, this.colors[0]);
				}		
			}
		}
		
		if (! this.isQuiet) {
			System.out.println("Thread-<" + this.numberOfThreads + "> stopped.");
			
			final long duration = System.currentTimeMillis() - startTimeThread;
            System.out.println("Thread-<" + numberOfThreads +"> execution time was (millis): " + duration);
		}
	}
}