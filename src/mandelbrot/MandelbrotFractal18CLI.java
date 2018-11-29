package mandelbrot;

import org.apache.commons.cli.*;

public class MandelbrotFractal18CLI {
	private static Options createOptions() {
		Option size = new Option("s", "size", true, "size of the image");
		Option rect = new Option("r", "rect", true, "coordinates where we search in for points from Mandelbrot set"); 
		Option task = new Option("t", "tasks", true, "number of threads");
		Option output = new Option("o", "output", true, "output file name");
		Option quiet = new Option("q", "quiet", false, "without log messages");
	
		Options options = new Options();
		options.addOption(size);
		options.addOption(rect);
		options.addOption(task);
		options.addOption(output);
		options.addOption(quiet);
		
		return options;
	}
	
	private static CommandLine createCmd(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();
		
		try {
			return parser.parse(options, args);
		} catch (ParseException ex) {
			throw new IllegalArgumentException("Parsing error!");
		}
	}
	
	private static MandelbrotFractal18 createMandelbrot(CommandLine cmd) {
        int width = 640, height = 480;
        
        if (cmd.hasOption("s")) {
            String arg = cmd.getOptionValue("s");
            String[] res = arg.split("x");
            width = Integer.parseInt(res[0]);
            height = Integer.parseInt(res[1]);

            if(width < 0 || height < 0) {
                throw new IllegalArgumentException("width and height must be positive integers");
            }
        }
        
        float firstRe, secondRe, firstIm, secondIm;
       
        firstRe = -2.0f; secondRe = 2.0f;
        firstIm = -2.0f; secondIm = 2.0f;

        if (cmd.hasOption("r")) {
            String arg = cmd.getOptionValue("r");
            String[] res = arg.split(":");
            firstRe = Float.parseFloat(res[0]);
            secondRe = Float.parseFloat(res[1]);
            firstIm = Float.parseFloat(res[2]);
            secondIm = Float.parseFloat(res[3]);
        }

        int countOfThreads = 1;
        if (cmd.hasOption("t")) {
            countOfThreads = Integer.parseInt(cmd.getOptionValue("t"));

            if(countOfThreads < 1) {
                throw new IllegalArgumentException("Exception threads number > 0");
            }
        }
        
        String fileName = "zad18.png";
        if (cmd.hasOption("o")) {
            fileName = cmd.getOptionValue("o");
        }

        boolean isQuiet = false;
        if (cmd.hasOption("q")) {
            isQuiet = true;
        }

        return new MandelbrotFractal18(width, height, countOfThreads, fileName, isQuiet, firstRe, secondRe, firstIm, secondIm);
    }
	
	public static void generateMandelbrotFractal(String[] args) {
        Options options = createOptions();
        CommandLine cmd = createCmd(options, args);
        MandelbrotFractal18 mandelbrotFractal = createMandelbrot(cmd);

        mandelbrotFractal.generate();
    }

    public static void main(String[] args) {
        generateMandelbrotFractal(args);
    }
}