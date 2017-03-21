package utils.parser;

import org.apache.commons.cli.*;
import utils.exceptions.InternalAnalyzerException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Nicolas Müller
 *
 */
public class ArgParser {

    // flags
    final static String MAINCLASS_FLAG = "m";
    final static String CLASSPATH = "cp";
    final static String OTHER_CLASSES_FOR_STATIC_ANALYZER = "s";
    final static String JIMPLE_FLAG = "j";
    final static String OUTPUT_FOLDER_FLAG = "o";
    final static String ADDITIONAL_FILES_FLAG = "f";
    final static String PUBLIC_TYPING_FOR_JIMPLE = "x";
    final static String VERBOSE = "v";
    final static String HELP = "h";

	private static void printHelp(Options options) {
	    HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("jgs", options);
	}

	// TODO: some options are unused at the moment. Clean up.
    public static ArgumentContainer getSootOptions(String[] args) {

        // locals variables to hold parsing results
        String mainclass;
        boolean toJimple;
        String outputFolder;
        List<String> addDirsToClasspath = new ArrayList<>();
        List<String> addClasses = new ArrayList<>();
        List<String> additionalFiles = new ArrayList<>();
        boolean usePublicTyping;



        // ======== PREPARE OPTIONS =========
		Options options = new Options();

        Option mainopt = new Option(MAINCLASS_FLAG,
                                    "main-class",
                                    true,
                                    "the main-class of the application");
        mainopt.setRequired(true);
        options.addOption(mainopt);


		Option addDirsToClasspathOption = new Option(CLASSPATH, "classpath", true,
				"set the classpath");
		addDirsToClasspathOption.setRequired(false);
		addDirsToClasspathOption.setArgs(Option.UNLIMITED_VALUES);
		options.addOption(addDirsToClasspathOption);

		Option addOtherClassesForStatic = new Option(OTHER_CLASSES_FOR_STATIC_ANALYZER, "otherClasses", true,
		                "Add other classes for the static analyzer");
		addOtherClassesForStatic.setRequired(false);
		addOtherClassesForStatic.setArgs(Option.UNLIMITED_VALUES);
		// options.addOption(addOtherClassesForStatic);

        Option format = new Option(JIMPLE_FLAG, "jimple", false,
                "Optional: Output as Jimple instead of as compiled class");
        format.setRequired(false);
        options.addOption(format);

		Option output = new Option(OUTPUT_FOLDER_FLAG, "output", true,
                "Optional: set output folder");
		output.setRequired(false);
		options.addOption(output);

		Option filesToAdd = new Option(ADDITIONAL_FILES_FLAG, "files", true,
                "Optional: add additional files to instrumentation process");
		filesToAdd.setRequired(false);
		filesToAdd.setArgs(Option.UNLIMITED_VALUES);
		// options.addOption(filesToAdd);

        Option verbose = new Option(VERBOSE, "verbose", false, "verbose logging");
        verbose.setRequired(false);
        options.addOption(verbose);

		Option help = new Option(HELP, "help", false, "Print Help");
		help.setRequired(false);
		options.addOption(help);

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);

            // help flag
            if (cmd.hasOption("h")){
                printHelp(options);
                System.exit(0);
            }

			// m flag
            mainclass = cmd.getOptionValue(MAINCLASS_FLAG);

            // case p flag
            if (cmd.hasOption(CLASSPATH)) {
                for ( String s : cmd.getOptionValues(CLASSPATH)) {
                    File tmp = new File(s);
                    addDirsToClasspath.add(tmp.isAbsolute() ? s : new File(System.getProperty("user.dir"), s).getAbsolutePath());
                }
            }

            // case s flag
            if (cmd.hasOption(OTHER_CLASSES_FOR_STATIC_ANALYZER)) {
                for (String s: cmd.getOptionValues(OTHER_CLASSES_FOR_STATIC_ANALYZER)) {
                    File tmp = new File(s);
                    addClasses.add(tmp.isAbsolute() ? s : new File(System.getProperty("user.dir"), s).getAbsolutePath());
                }
            }

			// case j flag
            toJimple = cmd.hasOption(JIMPLE_FLAG);

            // case o flag
            if (cmd.hasOption(OUTPUT_FOLDER_FLAG)) {
                outputFolder = cmd.getOptionValue(OUTPUT_FOLDER_FLAG);
            } else {
                outputFolder = ArgumentContainer.VALUE_NOT_SET;
            }

			// case f flag
            if (cmd.hasOption(ADDITIONAL_FILES_FLAG)) {
                Collections.addAll(additionalFiles, cmd.getOptionValues(ADDITIONAL_FILES_FLAG));
            }

           usePublicTyping = cmd.hasOption(PUBLIC_TYPING_FOR_JIMPLE);

            //

            return new ArgumentContainer(mainclass,
                                         addDirsToClasspath,
                                         addClasses,
                                         toJimple,
                                         outputFolder,
                                         additionalFiles,
                                         usePublicTyping,
                                         cmd.hasOption(VERBOSE));

			// if illegal input
		} catch (ParseException e) {
			printHelp(options);
			System.exit(-1);
		}

	    throw new InternalAnalyzerException("This line must never be reached"); // compiler complains if i dont put this here?!
	}
}
