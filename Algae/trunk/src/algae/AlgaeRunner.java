/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae;

import algae.chromossome.Chromossome;
import algae.engine.ConfigurableGeneticAlgorithim;
import algae.engine.ResultDebugger;
import algae.engine.configuration.Configuration;
import algae.engine.configuration.Property;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import algae.environment.Environment;
import algae.initialpopulation.InitialPopulation;
import algae.logger.GALogger;
import algae.util.io.MSFWriter;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergio
 */
public class AlgaeRunner implements ResultDebugger {

    private static final String RESULT_PATH = System.getenv("HOME")+"/Development/results/";
    private static String structureDBPath = System.getenv("HOME")+"/Mestrado/structure/";
    private static final int NUMBER_OF_RUNS = 20;
    private static final int GENERATION_NUMBER = 650;
    private static final int POPULATION_SIZE = 650;

    private String resultPath = null;

    private int numberOfRuns;
    private int generationNumber;
    
    private int currentRun = 0;
    private String testSetId = null;
    private String runFolder = null;
    private String alignmentId = "BB11002_SP_20_B62";

    public static void main(String[] args) {
        AlgaeRunner runner = new AlgaeRunner();
        runner.run(args);
    }

    private void run(String[] args) {

        String inputFile = "RV11/BB11002.tfa";
        String matrixFolder = null;

        String matrix = "BLOSUM80";

        int debugResult = -1;
        int populationSize = POPULATION_SIZE;



        if (args.length < 4) {
            System.err.println("Usage: GAAligner inputFile alignmetId matrixFolder configurationFile [resultDir]");
        }



        inputFile = args[0];
        alignmentId = args[1];

        String initialPopulationClass = args[4];
        
        if (args.length == 6){
            resultPath = args[5];
        } else {
            resultPath = RESULT_PATH;
        }
        System.out.println("Saving results to dir "+ resultPath);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        testSetId = String.format("%s_%02d%02d%02d%02d%02d%02d", alignmentId, (year % 100), month + 1, day, hour, minutes, seconds);

        try {

            //Set matrix folder
            matrixFolder = args[2];

            //Load configuration
            Configuration configuration = Configuration.getInstance();
            configuration.configure(args[3]);

            //Prepare environment
            Environment environment = Environment.getInstance();
            craligner.environment.Environment crEnvironment = craligner.environment.Environment.getInstance();

            //Get general purpose information

            numberOfRuns = NUMBER_OF_RUNS;
            generationNumber = GENERATION_NUMBER;

            List<Property> properties = configuration.getProperties();
            for (Property property : properties) {
                //Structure Database Path
                if ("structureDBPath".equals(property.getPropertyName())) {
                    structureDBPath = property.getPropertyValue();
                    System.out.println("Structure DB path set to " + structureDBPath);
                }
                
                //Number of runs
                if ("runs".equals(property.getPropertyName())) {
                    numberOfRuns = Integer.parseInt(property.getPropertyValue());
                    System.out.println("Runs set to " + numberOfRuns);
                }
                //Number of generations
                if ("generations".equals(property.getPropertyName())) {
                    generationNumber = Integer.parseInt(property.getPropertyValue());
                    System.out.println("Generations set to " + generationNumber);
                }
                //Population size
                if ("population".equals(property.getPropertyName())) {
                    populationSize = Integer.parseInt(property.getPropertyValue());
                    System.out.println("Population size set to " + populationSize);
                }
                //Structure Roulette Algorithm
                if ("structureRoulette".equals(property.getPropertyName())) {
                    String rouletteAlgorithm = property.getPropertyValue();
                    System.out.println("Using structureRoulette " + rouletteAlgorithm);
                    environment.setStructureRouletteAlgorithm(rouletteAlgorithm);
                }
                //Matrix
                if ("matrix".equals(property.getPropertyName())) {
                    matrix = property.getPropertyValue();
                }
                //Debugging result
                if ("MSFatGeneration".equals(property.getPropertyName())) {
                    debugResult = Integer.parseInt(property.getPropertyValue());
                    System.out.println("MSF for debug generated at each " + debugResult + "generations");
                }

                //Gap score
                if ("gapOpening".equals(property.getPropertyName())){
                    environment.setGapOpeningScore(Integer.parseInt(property.getPropertyValue()));
                }
                if ("gapExtension".equals(property.getPropertyName())){
                    environment.setGapExtensionScore(Integer.parseInt(property.getPropertyValue()));
                }
                if ("gapGapScore".equals(property.getPropertyName())){
                    environment.setGapGapScore(Integer.parseInt(property.getPropertyValue()));
                }

            }

            //Create folder
            File testFolder = new File(resultPath + "/" + testSetId);
            testFolder.mkdirs();

            System.out.println("Population Size:" + populationSize);
            environment.setPopulationSize(populationSize);

            System.out.println("Loading Sequences and Structures");
            environment.loadSequences(inputFile);
            crEnvironment.loadSequences( inputFile);

            environment.loadStructures(structureDBPath);

            System.out.println("Loading Matrix:" + matrix);
            environment.loadMatrix(null, matrixFolder+"/"+matrix+".txt");
            crEnvironment.loadMatrixLocal(null, matrixFolder+"/BLOSUM80.txt");
            crEnvironment.loadMatrixGlobal(null, matrixFolder+"/"+matrix+".txt");

            System.out.println("Calculating pairwise distances");
            environment.calculateDistances();
            System.out.println("Calculating conserved region distances");
            List<jbio.alignment.Sequence> crSequences = crEnvironment.getSequences();
            crEnvironment.loadDistanceMatrix(crSequences);

            System.out.println("Calculating philogenetic tree");
            environment.mountTree();

            //Run test set
            GALogger logger = GALogger.getInstance();

            logger.startTestSet(resultPath + "/" + testSetId, alignmentId);

            for (int i = 1; i <= numberOfRuns; i++) {
                System.out.println("Starting test run " + i);

                currentRun = i;
                runFolder = testFolder + "/" + alignmentId + "_Run" + i;

                logger.startTestRun(i);

                Constructor initialPopulationConstructor = ((Class)Class.forName(initialPopulationClass)).getConstructor(Integer.class);                
                InitialPopulation initialPopulation = (InitialPopulation)initialPopulationConstructor.newInstance(populationSize);
                ConfigurableGeneticAlgorithim ga = new ConfigurableGeneticAlgorithim(generationNumber, initialPopulation);
                ga.configure();
                Chromossome c = null;
                if (debugResult > 0) {
                    //Create folder
                    File debugFolder = new File(runFolder);
                    debugFolder.mkdirs();
                    c = ga.run(this, debugResult);
                } else {
                    c = ga.run();
                }

                System.out.println("Writing output file " + i);
                MSFWriter.create(testFolder + "/" + alignmentId + "_Run" + i + ".msf", c);

                logger.endTestRun();
            }

            logger.endTestSet();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AlgaeRunner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AlgaeRunner.class.getName()).log(Level.SEVERE, null, ex);
        }




    }

    public void debug(int generation, Chromossome result) {
        try {
            MSFWriter.create(runFolder + "/" + alignmentId + "_Run" + currentRun + "_Gen" + generation + ".msf", result);
        } catch (IOException ex) {
            Logger.getLogger(AlgaeRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
