package mandelbrot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class MandelbrotFractal18 {
	private int width;
	private int height;
	
	private int countOfThreads;
	private String fileName;
	private boolean isQuiet;
	
	private float firstIm, secondIm, firstRe, secondRe; 
	
	MandelbrotFractal18(int width, int height, int countOfThreads, String fileName, boolean isQuiet, float firstRe, float secondRe, float firstIm, float secondIm) {
		this.width = width;
		this.height = height;
		this.countOfThreads = countOfThreads;
		
		this.firstRe = firstRe;
		this.secondRe = secondRe;
		this.firstIm = firstIm;
		this.secondIm = secondIm;
		
		this.fileName = fileName;
		this.isQuiet = isQuiet;
	}
	
	void generate() {
		final long overAllStartTime = System.currentTimeMillis();
		
		BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		
		final int maxSteps = 700; // maximum iteration count after each we stop
		
		int[] colors = new int[maxSteps];
		
		for (int index = 0; index < maxSteps; ++index) {
			colors[index] = Color.HSBtoRGB((float) index / 256.0f, 1.0f, (float) index / (index + 10.0f));
		}
		
		 /*
		
		// Dividing by rows
		
		int chunk = this.height / this.countOfThreads;
		int leftForTheLastChunk = this.height % chunk;
		
		Thread[] threads = new Thread[this.countOfThreads];
		
		int colStart = 0, colEnd = this.width;
		
		for (int index = 0; index < this.countOfThreads; ++index) {
			int rowStart = index * chunk;
			int rowEnd = (index + 1) * chunk;
			
			if (index == this.countOfThreads - 1) {
				rowEnd += leftForTheLastChunk;
			}
			
			MandelbrotFractal18Runnable slaveThread = new MandelbrotFractal18Runnable(image, colors, rowStart, rowEnd, colStart, colEnd, this.isQuiet, index + 1, firstRe, secondRe, firstIm, secondIm);
			threads[index] = new Thread(slaveThread);
			threads[index].start();
		}
		 */
		 
		
		// Dividing by columns
		
//		/*
			int chunk = this.width / this.countOfThreads;
			int leftForTheLastChunk = this.width % chunk;
			
			Thread[] threads = new Thread[this.countOfThreads];
			
			int rowStart = 0, rowEnd = this.height;
			
			for (int index = 0; index < this.countOfThreads; ++index) {
				int colStart = index * chunk;
				int colEnd = (index + 1) * chunk;
				
				if (index == this.countOfThreads - 1) {
					colEnd += leftForTheLastChunk;
				}
				
				MandelbrotFractal18Runnable slaveThread = new MandelbrotFractal18Runnable(image, colors, rowStart, rowEnd, colStart, colEnd, this.isQuiet, index + 1, firstRe, secondRe, firstIm, secondIm);
				threads[index] = new Thread(slaveThread);
				threads[index].start();
			}		
	//	*/
		
		
		for (int index = 0; index < this.countOfThreads; ++index) {
			try {
				threads[index].join();
			} catch (InterruptedException except) {
				
			}
		}
		
		try {
			ImageIO.write(image, "png", new File(this.fileName));
		} catch (IOException ex) {
			System.out.println("Exception: picture write error");
		}
		
		System.out.println("Threads used in current run: " + this.countOfThreads);
		
		final long duration = System.currentTimeMillis() - overAllStartTime;
		System.out.println("Total execution time for current run (millis): " + duration);
	}
} 