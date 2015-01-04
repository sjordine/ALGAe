/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.environment;

import SecondaryStructure.InvalidHorizFileException;
import SecondaryStructure.ResidueStructure;
import algae.alignment.Matrix;
import algae.alignment.Sequence;
import algae.operators.mutation.selectionpoint.StructureRoulette;
import algae.philogeny.matrix.DistanceMatrix;
import algae.philogeny.matrix.DistanceType;
import algae.philogeny.matrix.PairwiseDistance;
import algae.philogeny.tree.BasicTree;
import algae.philogeny.tree.TreeNode;
import algae.score.Score;
import algae.util.io.FASTAReader;
import algae.util.io.ScoreMatrixReader;
import algae.util.tree.TreeUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jbio.io.HorizReader;

/**
 *
 * @author sergio
 */
public class Environment {

    private static Environment _instance = null;
    private List<Sequence> sequences = null;
    private List<List<ResidueStructure>> structures = null;
    private List<StructureRoulette> structureRoulettes = null;
    private double[][] distanceFactor = null;
    private Matrix matrixLow = null;
    private Matrix matrixHigh = null;
    private Score scoreMethod = null;
    private DistanceMatrix distances = null;
    private TreeNode philogeneticTree = null;
    private int treeSize = 0;

    private int populationSize = 0;
    
    private double pi = 0.0;
    private String mean = null;
    private String structureRouletteAlgorithm = null;
    private Class structureRouletteAlgorithmClass = null;

    private int gapOpeningScore = -8;
    private int gapGapScore = -6;
    private int gapExtensionScore = -2;


    private Environment() {
    }

    public static Environment getInstance() {
        if (_instance == null) {
            _instance = new Environment();
        }

        return _instance;
    }

    public void loadSequences(String sequenceFile) {
        try {
            sequences = FASTAReader.read(sequenceFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadStructures(String structureDBPath) {

        structures = new ArrayList<List<ResidueStructure>>();
        structureRoulettes = new ArrayList<StructureRoulette>();

        try {

            for (int i = 0; i < sequences.size(); i++) {

                Sequence currentSequence = sequences.get(i);

                StringBuilder completePath = new StringBuilder();

                //Build sequence structure file name
                completePath.append(structureDBPath);
                completePath.append("/");
                completePath.append(currentSequence.getDescription());
                completePath.append(".horiz");

                List<ResidueStructure> currentStructure = HorizReader.ReadFile(completePath.toString());
                structures.add(currentStructure);

                System.out.println("Read secondary structure of sequence "+currentSequence.getDescription());

                //Create structure roulette for this structure
                if (structureRouletteAlgorithmClass != null){
                    StructureRoulette roulette;
                    try {
                        roulette = (StructureRoulette) structureRouletteAlgorithmClass.newInstance();
                        roulette.prepareRoulette(currentStructure);
                        structureRoulettes.add(roulette);
                    } catch (InstantiationException ex) {
                        throw new RuntimeException("Invalid class" +structureRouletteAlgorithmClass.getName());
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException("Illegal access to class" +structureRouletteAlgorithmClass.getName());
                    }                    
                } else {
                   System.out.println("No roulette algorithm being used");
                }

                System.out.println("Created secondary structure roulette for sequence "+currentSequence.getDescription());
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidHorizFileException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ResidueStructure getResidueStructure(int sequenceIndex, int positionIndex){
        ResidueStructure returnValue = null;



        if (structures!=null && structures.size() > sequenceIndex){
            List<ResidueStructure> targetStructure = structures.get(sequenceIndex);

            
            

            if (targetStructure!=null && targetStructure.size() > positionIndex){
                returnValue = targetStructure.get(positionIndex);
            } else {
                throw new RuntimeException("Seq "+ sequenceIndex +" possui "+targetStructure.size()+" residuos, foi pedido "+positionIndex);
            }
        } else {
            throw new RuntimeException("Existem "+structures.size()+" seqs, foi pedido "+sequenceIndex);
        }

        return returnValue;
    }



    public List<Sequence> getSequences() {
        return sequences;
    }

    public void loadMatrix(String matrixName, String matrixFile) {
        loadMatrixLowSimmilarity(matrixName, matrixFile);
    }

    public void loadMatrixLowSimmilarity(String matrixName, String matrixFile) {
        try {
            matrixLow = ScoreMatrixReader.read(matrixName, matrixFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadMatrixHighSimmilarity(String matrixName, String matrixFile) {
        try {
            matrixHigh = ScoreMatrixReader.read(matrixName, matrixFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Matrix getLowSimmilarityMatrix() {
        return matrixLow;
    }

    public Matrix getHighSimmilarityMatrix() {
        Matrix returnValue = null;


        if (matrixHigh == null) {
            returnValue = matrixLow;
        } else {
            returnValue = matrixHigh;
        }

        return returnValue;
    }

    public void setScoreMethod(Score scoreMethod) {
        this.scoreMethod = scoreMethod;
    }

    public void calculateDistances() {

        Matrix matrix = getHighSimmilarityMatrix();

        if (matrix != null) {
            distances = new DistanceMatrix(sequences);
            distances.calculateDistances(matrix);

            //Calculate distance factor
            distanceFactor = new double[sequences.size()][sequences.size()];
            double maxDistance = Double.MIN_VALUE;
            double minDistance = Double.MAX_VALUE;
            for (int i=0; i < sequences.size(); i++){
                for (int j=0; j < sequences.size(); j++){
                     double distanceValue = Double.MIN_VALUE;
                     PairwiseDistance distance =  distances.getAlignment(i, j, DistanceType.GLOBAL);
                     if (distance!=null){
                         distanceValue = distance.getScore();
                     } else {
                         distanceValue = 0;
                     }                     
                     if (distanceValue > maxDistance){
                         maxDistance = distanceValue;
                     }
                     if (distanceValue < minDistance){
                         minDistance = distanceValue;
                     }
                }
            }

            double maxScore = maxDistance - minDistance;

            for (int i=0; i < (sequences.size()-1); i++){
                for (int j=i+1; j < sequences.size(); j++){
                    PairwiseDistance distance =  distances.getAlignment(i, j, DistanceType.GLOBAL);
                    double distanceValue = 0;
                    double averageScore = 0;
                    if (distance!=null){
                         distanceValue = distance.getScore();
                         averageScore = ((distanceValue - minDistance)+(maxDistance - distanceValue))/2;                  

                         distanceFactor[i][j] =averageScore/maxScore;
                         distanceFactor[j][i] = distanceFactor[i][j];
                    } else {
                     distanceFactor[i][j] =0;
                     distanceFactor[j][i] = 0;
                    }
                }
            }
        }
    }

    public double getDistanceFactor(int seq1Index, int seq2Index){
        return distanceFactor[seq1Index][seq2Index];
    }


    public DistanceMatrix getDistanceMatrix() {
        return distances;
    }

    public Matrix getMatrix() {
        return getLowSimmilarityMatrix();
    }

    public void mountTree() {
        philogeneticTree = BasicTree.mountTree(distances.getBestDistances());
        treeSize = TreeUtil.getTreeSize(philogeneticTree);
    }

    public TreeNode getTree() {
        return philogeneticTree;
    }

    /**
     * @return the treeSize
     */
    public int getTreeSize() {
        return treeSize;
    }

    public void setPi(double pi) {
        this.pi = pi;
    }

    public double getPi(){
        return pi;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean){
        this.mean = mean;
    }

    /**
     * @return the structureRoulettes
     */
    public List<StructureRoulette> getStructureRoulettes() {
        return structureRoulettes;
    }

    public StructureRoulette getStructureRoulette(int index){
        StructureRoulette returnValue = null;

        if (structureRoulettes!=null && structureRoulettes.size() > index){
            returnValue = structureRoulettes.get(index);
        }

        return returnValue;
    }


    public void setStructureRouletteAlgorithm(String structureRouletteAlgorithmName) {
        this.structureRouletteAlgorithm = structureRouletteAlgorithmName;
        try {
            this.structureRouletteAlgorithmClass = Class.forName(structureRouletteAlgorithmName);
        } catch (ClassNotFoundException ex) {
           throw new RuntimeException("Invalid structure algorithm class "+structureRouletteAlgorithmName);
        }
    }

    public String getStructureRouletteAlgorithm(String value) {
        return this.structureRouletteAlgorithm;
    }

    /**
     * @return the populationSize
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /**
     * @param populationSize the populationSize to set
     */
    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getGapGapScore() {
       return gapGapScore;
    }

    public void setGapGapScore(int score){
        gapGapScore = score;
    }

    public int getGapOpeningScore() {
       return gapOpeningScore;
    }

    public void setGapOpeningScore(int score){
        gapOpeningScore = score;
    }

    public int getGapExtensionScore() {
       return gapExtensionScore;
    }

    public void setGapExtensionScore(int score){
        gapExtensionScore = score;
    }
}
