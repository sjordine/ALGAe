/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.operators.crossover;

import algae.alignment.GlobalAlignment;
import algae.alignment.Matrix;
import algae.alignment.PairWiseAlignment;
import algae.alignment.Sequence;
import algae.chromossome.BasicListChromossome;
import algae.chromossome.Chromossome;
import algae.environment.Environment;
import algae.util.alignment.MatrixUtil;
import algae.util.alignment.SequenceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sergio
 */
public class BestPartialAlignmentCrossOver implements CrossOverOperator {

    public List<Chromossome> execute(Chromossome c1, Chromossome c2) {

         List<Chromossome> returnValue = new ArrayList<Chromossome>();

        int numberOfSequences =  c1.getData() .length;
        double[][] sumOfDistances = new double[2][numberOfSequences];
        Map<Integer,Integer> usedSequences = new HashMap<Integer,Integer>();
       

       //Calculate sum of distances for all sequences
       calculateSequenceDistances(sumOfDistances,0, c1.getData());
       calculateSequenceDistances(sumOfDistances,1, c2.getData());

       //Select best sequences
       usedSequences = selectBestSequences(numberOfSequences,sumOfDistances);
       
       //Create new alignment based on the sequences defined above
       //Get sub-alignments from each parent
       int firstParentSize = 0;
       int secondParentSize = 0;

       for (int i=0;i<numberOfSequences;i++){
           if (usedSequences.get(i)==0){
               firstParentSize++;
           } else {
               secondParentSize++;
           }
       }

       //Split relevant sequences from parent alignments
       char[][] firstParentSequences = new char[firstParentSize][c1.getData()[0].length];
       char[][] secondParentSequences = new char[secondParentSize][c2.getData()[0].length];

       int firstParentIndex = 0;
       int secondParentIndex = 0;

       for (int i=0;i<numberOfSequences;i++){
           if (usedSequences.get(i)==0){
               firstParentSequences[firstParentIndex] = c1.getData()[i];
               firstParentIndex++;
           } else {
               secondParentSequences[secondParentIndex] = c2.getData()[i];
               secondParentIndex++;
           }
       }

 
       ArrayList<ArrayList<Character>> childData = createChild(usedSequences,numberOfSequences,firstParentSequences,secondParentSequences);            

       SequenceUtil.removeEmptyColumns(childData);

       Chromossome child  = new BasicListChromossome(childData);
       child.setOrigin("BPA_COV");
       child.setRootOrigin("BPA_COV");
       returnValue.add(child);

        return returnValue;
    }

    /**
     * Calculate the sum of distances for all sequences on a given alignment
     * 
     * This algorithim is O(n²m) where n stands for the number of sequences on the given alignment and m its size
     *
     * @param sumOfDistances (out) matrix where the sum of distances might be kept
     * @param alignmentIndex alignment index (0 or 1 to indicate the first or second parent)
     * @param alignment The given sequence alignment
     */
    private void calculateSequenceDistances(double[][] sumOfDistances, int alignmentIndex, char[][] alignment) {

        Environment environment = Environment.getInstance();
        Matrix matrix = environment.getMatrix();

         int numberOfSequences =  alignment.length;
         int alignmentSize = alignment[0].length;

          for (int i=0;i<numberOfSequences;i++){
            for (int j=0;j<numberOfSequences;j++)
            {
                if (i!=j){
                    for (int k=0;k<alignmentSize;k++){
                        sumOfDistances[alignmentIndex][i] += MatrixUtil.getScore(matrix,alignment[i][k],alignment[j][k]);
                    }
                }
            }
        }
    }

    private Map<Integer, Integer> selectBestSequences(int numberOfSequences, double[][] sumOfDistances) {

        Map<Integer,Integer> returnValue = new HashMap<Integer,Integer>();

        int currentAlignment = 0;

       for (int i=0;i<numberOfSequences;i++){
           Double bestScore = null;
           Integer selectedIndex = null;
           for (int j=0;j<numberOfSequences;j++){
               if (returnValue.get(j)==null){
                    if (bestScore!=null){
                       if (sumOfDistances[currentAlignment][j] > bestScore){
                           bestScore = sumOfDistances[currentAlignment][j];
                           selectedIndex = j;
                       }
                    } else {
                        bestScore = sumOfDistances[currentAlignment][j];
                        selectedIndex = j;
                    }
               }
           }
           //Map this sequences
           if (selectedIndex != null){
               returnValue.put(selectedIndex, currentAlignment);
           } else {
               //TODO Error! Throw exception
               System.out.println("Error executing BestPartialAlignmentCrossOver");
           }

           //Switch to use the other parent next time
           currentAlignment = (currentAlignment+1)%2;
       }
      
        return returnValue;
    }

    private ArrayList<ArrayList<Character>> createChild(Map<Integer,Integer> usedSequences, int numberOfSequences,char[][] firstParentSequences,char[][] secondParentSequences) {

       ArrayList<ArrayList<Character>> returnValue = new ArrayList<ArrayList<Character>>();

       //Create consensus sequences and align them
       Environment environment = Environment.getInstance();
       Matrix matrix = environment.getMatrix();
       Sequence consensus1 = SequenceUtil.getConsensusSequence(matrix, firstParentSequences);
       Sequence consensus2 = SequenceUtil.getConsensusSequence(matrix, secondParentSequences);



       PairWiseAlignment aligner = new GlobalAlignment(consensus1, consensus2, matrix);
       char[][] consensusAlignment = aligner.align();
       aligner = null;


        //Initialize data

       for (int i=0;i<numberOfSequences;i++){
           returnValue.add(new ArrayList<Character>());
       }

       //Generate child alignment based on consensus alignment
       int[] consensusIndex = new int[2];
       int[] subAlignmentIndex = new int[2];

       for (int i=0;i<2;i++){
            consensusIndex[i]=0;
            subAlignmentIndex[i]=0;
       }

       char[][] consensusData = new char[2][];
       consensusData[0] = consensus1.getDataArray();
       consensusData[1] = consensus2.getDataArray();

       char[][][] subAlignmentSequences = new char[2][][];
       subAlignmentSequences[0] = firstParentSequences;
       subAlignmentSequences[1] = secondParentSequences;

       for (int i=0;i<consensusAlignment[0].length;i++){
           for (int j=0;j<2;j++){
            subAlignmentIndex[j]=0;
           }
           for (int j=0;j<numberOfSequences;j++){
                int currentAlignment = usedSequences.get(j);
                if (consensusIndex[currentAlignment]>=consensusData[currentAlignment].length){
                   returnValue.get(j).add('-');
                   subAlignmentIndex[currentAlignment]++;
                } else {
                    if (consensusAlignment[currentAlignment][i]== consensusData[currentAlignment][consensusIndex[currentAlignment]]){
                        returnValue.get(j).add(subAlignmentSequences[currentAlignment][subAlignmentIndex[currentAlignment]][consensusIndex[currentAlignment]]);
                        subAlignmentIndex[currentAlignment]++;
                    } else {
                        returnValue.get(j).add('-');
                        subAlignmentIndex[currentAlignment]++;
                    }
               }
           }
           for (int j=0;j<2;j++){
               if (consensusIndex[j] < consensusData[j].length &&
                    consensusAlignment[j][i]== consensusData[j][consensusIndex[j]]){
                    consensusIndex[j]++;
               }
           }
       }

       return returnValue;
    }

}
