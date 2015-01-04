/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.engine;

import algae.alignment.Sequence;
import algae.chromossome.BasicListChromossome;
import algae.chromossome.Chromossome;
import algae.cut.FitnessComparator;
import algae.engine.configuration.Configuration;
import algae.engine.configuration.Property;
import algae.environment.Environment;
import algae.fitness.BasicSumOfPairsFitness;
import algae.fitness.FitnessFunction;
import algae.initialpopulation.InitialPopulation;
import algae.initialpopulation.PopulationCR4;
import algae.logger.GALogger;
import algae.operators.crossover.CrossOverOperator;
import algae.operators.mutation.MutationOperator;
import algae.selection.MostFittedSelection;
import algae.selection.RouletteSelection;
import algae.selection.Selection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergio
 */
public class ConfigurableGeneticAlgorithim {

    private Random random = null;
    protected int POPULATION_SIZE = 100;
    protected int SELECTION_RATE = 2;
    protected double CROSS_OVER_PROBABILITY = 0.2;
    protected double MUTATION_PROBABILITY = 0.2;
    protected int generationNumber = 0;
    protected InitialPopulation initialPopulation = null;
    protected FitnessFunction fitness = null;
    protected Selection selectionAlgorithim = null;
    protected Selection popSelectionAlgorithm = null;
    protected int selectionSize = 0;
    private Comparator fitnessComparator = null;
    private List<CrossOverOperator> crossOverOperators = null;
    private List<MutationOperator> mutationOperators = null;

    public ConfigurableGeneticAlgorithim(int generationNumber, InitialPopulation initialPopulation) {

        System.out.println("Running scenario");

        Environment environment = Environment.getInstance();

        this.generationNumber = generationNumber;
        //initialPopulation = new BasicStarAlignmentPopulation(POPULATION_SIZE);
        //initialPopulation = new SuperPopulation(POPULATION_SIZE);
        System.out.println("Creating Initial Population (Basic Star Alignment)");
//        initialPopulation = new BasicStarAlignmentPopulation(environment.getPopulationSize());
        this.initialPopulation = initialPopulation;
        fitness = new BasicSumOfPairsFitness();
        //fitness = new GapAffinitySumOfPairsFitness();
        selectionAlgorithim = new RouletteSelection();
        popSelectionAlgorithm = new MostFittedSelection();
        selectionSize = POPULATION_SIZE / SELECTION_RATE;
        fitnessComparator = new FitnessComparator();

        random = new Random();

        crossOverOperators = new ArrayList<CrossOverOperator>();
        mutationOperators = new ArrayList<MutationOperator>();


    }

    public void configure() {

        Configuration configuration = Configuration.getInstance();
        List<Property> properties = configuration.getProperties();

        for (Property property : properties) {

            String key = property.getPropertyName();
            String value = property.getPropertyValue();
            try {
                processConfigFileLine(key, value);
            } catch (InstantiationException ex) {
                Logger.getLogger(ConfigurableGeneticAlgorithim.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ConfigurableGeneticAlgorithim.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ConfigurableGeneticAlgorithim.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error " + ex.getMessage());
            }

        }
    }

    public Chromossome run() {
        return run(null, 0);
    }

    public Chromossome run(ResultDebugger debugger, int atEach) {
        Chromossome bestAlign = null;
        List<Chromossome> population = null;
        GALogger logger = GALogger.getInstance();


//        initialPopulation = new PopulationCR3(POPULATION_SIZE);

        //Create Initial Population (and its fitness)
        population = initialPopulation.generate();
        fitness.setFitness(population);

        validatePopulation(population);

        System.out.println("Initial population created!");

        //GA loop

        for (int i = 0; i < this.generationNumber; i++) {
            try {
                System.out.println("Executing generation " + i);
                logger.startGeneration(i);
                //Select Individuals for Breeding
                List<Chromossome> breedingCandidates = selectionAlgorithim.select(population, selectionSize);
                //Breeding - execute operators
                population.addAll(runOperators(breedingCandidates));

                //Calculate fitness
                fitness.setFitness(population);

                logger.preCutPopulation(population);

                //validatePopulation(population);

                //Cut population
                Collections.sort(population, fitnessComparator);

                population = popSelectionAlgorithm.select(population, this.POPULATION_SIZE);

                /*
                ArrayList<Chromossome> newPopulation = new ArrayList<Chromossome>();
                for (int j = 0; j < ; j++) {
                    Chromossome newChromossome = (Chromossome) population.get(j).clone();
                    //Keep recent origin
                    if (!population.get(j).getOrigin().equals("CLONE")) {
                        newChromossome.setOrigin(population.get(j).getOrigin());
                    }
                    newPopulation.add(newChromossome);
                }
                population = newPopulation;
                 */

                logger.postCutPopulation(population);

                //All survival population was cloned
                for (Chromossome c : population) {
                    c.setOrigin("CLONE");
                }

                //Select best Alignment
                bestAlign = population.get(0);

                //If debug is set, create an MSF file if needed
                if (debugger != null && i % atEach == 0) {
                    debugger.debug(i, bestAlign);
                }

                validatePopulation(population);

                logger.endGeneration();
            } catch (Exception ex) {
                Logger.getLogger(ConfigurableGeneticAlgorithim.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException();
            }
        }

        validatePopulation(population);

        return bestAlign;
    }

    private List<Chromossome> runOperators(List<Chromossome> breedingCandidates) {
        List<Chromossome> returnValue = new ArrayList<Chromossome>();
        List<Chromossome> crossOverCandidates = new ArrayList<Chromossome>();
        List<Chromossome> mutationCandidates = new ArrayList<Chromossome>();


        for (Chromossome c : breedingCandidates) {

            double crossOverOdd = random.nextDouble();

            if (crossOverOdd < CROSS_OVER_PROBABILITY) {
                crossOverCandidates.add(c);
            }

            double mutationOdd = random.nextDouble();

            if (mutationOdd < MUTATION_PROBABILITY) {
                mutationCandidates.add(c);
            }
        }

        //Run crossing-overs
        for (int i = 0; i < crossOverCandidates.size() - 1; i += 2) {
            returnValue.addAll(runCrossOver(crossOverCandidates.get(i), crossOverCandidates.get(i + 1)));
        }

        //Run mutations
        for (Chromossome c : mutationCandidates) {
            Chromossome child = runMutation(c);
            if (child != null) {
                returnValue.add(child);
            }
        }


        return returnValue;
    }

    private List<Chromossome> runCrossOver(Chromossome c1, Chromossome c2) {

        int operationIndex = Math.abs(random.nextInt()) % crossOverOperators.size();

        CrossOverOperator operator = crossOverOperators.get(operationIndex);

        return operator.execute(c1, c2);

    }

    private Chromossome runMutation(Chromossome c) {

        int operationIndex = Math.abs(random.nextInt()) % mutationOperators.size();

        MutationOperator operator = mutationOperators.get(operationIndex);

        return operator.execute(c);
    }

    private void validatePopulation(List<Chromossome> population) {

        Environment env = Environment.getInstance();
        List<Sequence> seqs = env.getSequences();

        for (Chromossome c : population) {
            validateChromossome(c, seqs);
        }
    }

    public static void validateChromossome(Chromossome c, List<Sequence> seqs) {

        if (seqs == null) {
            Environment env = Environment.getInstance();
            seqs = env.getSequences();
        }

        BasicListChromossome c1 = (BasicListChromossome) c.clone();
        ArrayList<ArrayList<Character>> data = c1.getDataAsList();
        for (int i = 0; i < data.size(); i++) {
            int j = 0;
            int cursor = 0;
            List<Character> currSeq = data.get(i);
            String baseSeq = seqs.get(i).getData();
            while (j < currSeq.size()) {
                if (currSeq.get(j) != '-') {
                    if (currSeq.get(j) != baseSeq.charAt(cursor)) {
                        System.out.println(baseSeq);
                        System.out.println(currSeq);
                        System.out.println("Indexes==>" + cursor + "," + j);
                        System.out.println("Origin==>" + c.getOrigin());
                        System.out.println();
                        c.printData();
                        System.out.println();
                        throw new RuntimeException("Invalid sequence!!!! Expected " + baseSeq.charAt(cursor) + " received " + currSeq.get(j) + " on sequence " + i);
                    }
                    cursor++;
                }
                j++;
            }
            if (cursor < (baseSeq.length() - 1)) {
                System.out.println(baseSeq);
                System.out.println(String.valueOf(c.getData()[i]));
                System.out.println(c.getRootOrigin());
                System.out.println(c.getOrigin());
                throw new RuntimeException("INvalid sequence - Missing characters!!!!");
            }
        }

    }

    private void processConfigFileLine(String key, String value) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (key != null) {
            if ("population".equals(key)) {
                int populationSize = Integer.parseInt(value);
                System.out.println("Set Population Size =" + populationSize);
                this.POPULATION_SIZE = populationSize;
            }
            if ("coProbability".equals(key)) {
                double crossingOver = Double.parseDouble(value);
                System.out.println("Set Crossing Over Probability =" + (crossingOver * 100));
                this.CROSS_OVER_PROBABILITY = crossingOver;
            }
            if ("mtProbability".equals(key)) {
                double mutation = Double.parseDouble(value);
                System.out.println("Set Mutation Probability =" + (mutation * 100));
                this.MUTATION_PROBABILITY = mutation;
            }
            if ("coOperator".equals(key)) {
                Class crossOverOperatorClass = Class.forName(value);
                System.out.println("Adding Cross Over Operator =" + value);
                CrossOverOperator crossOverOperator = (CrossOverOperator) crossOverOperatorClass.newInstance();
                crossOverOperators.add(crossOverOperator);
            }
            if ("mtOperator".equals(key)) {
                Class mutationOperatorClass = Class.forName(value);
                System.out.println("Adding Mutation Operator =" + value);
                MutationOperator mutationOperator = (MutationOperator) mutationOperatorClass.newInstance();
                mutationOperators.add(mutationOperator);
            }
            if ("fitnessClass".equals(key)) {
                Class fitnessClass = Class.forName(value);
                System.out.println("Adding Fitness Function =" + value);
                FitnessFunction fitnessFunction = (FitnessFunction) fitnessClass.newInstance();
                this.fitness = fitnessFunction;
            }
            if ("selectionClass".equals(key)) {
                Class selectionClass = Class.forName(value);
                System.out.println("Adding Selection Function =" + value);
                Selection selectionFunction = (Selection) selectionClass.newInstance();
                this.selectionAlgorithim = selectionFunction;
            }

            if ("selectionSize".equals(key)){
                int selSize = Integer.parseInt(value);
                System.out.println("Set Breeding Selection Size =" + selSize);
                this.selectionSize = selSize;
            }


            if ("populationSelectionClass".equals(key)){
                Class selectionClass = Class.forName(value);
                System.out.println("Adding Population Selection Function =" + value);
                Selection selectionFunction = (Selection) selectionClass.newInstance();
                this.popSelectionAlgorithm = selectionFunction;
            }


            if ("pi".equals(key)) {
                Environment environment = Environment.getInstance();
                double pi = Double.parseDouble(value);
                environment.setPi(pi);
            }

            if ("mean".equals(key)) {
                Environment environment = Environment.getInstance();
                System.out.println("Using mean " + value);
                environment.setMean(value);
            }

        }
    }
}
