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
import algae.util.alignment.SequenceUtil;
import algae.util.tree.TreeUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author sergio
 */
public class SequenceSimilarityCrossOver implements CrossOverOperator {

    public List<Chromossome> execute(Chromossome c1, Chromossome c2) {
        List<Chromossome> returnValue = new ArrayList<Chromossome>();


        int numberOfSequences =  c1.getData().length;


        Environment environment = Environment.getInstance();
        int treeSize = environment.getTreeSize();

        Map<Integer,Integer> usedSequences = null;

        Random r = new Random();

         List<Integer> similarSequence = null;
       List<Integer> complementarySequence = null;

        for (int i=0;i<5;i++){

             int cutPoint = r.nextInt(treeSize);

        similarSequence = TreeUtil.getSequencesFromNode(cutPoint);
        complementarySequence = TreeUtil.getComplementarySequencesFromNode(cutPoint);
        
        if (!similarSequence.isEmpty() && !complementarySequence.isEmpty())
            break;
        }
    
        if (similarSequence.isEmpty() || complementarySequence.isEmpty()){
            return returnValue;
        }

        usedSequences = defineUsedSequences(similarSequence,numberOfSequences);

        char[][] firstParentSequences = new char[similarSequence.size()][c1.getData()[0].length];
        char[][] secondParentSequences = new char[complementarySequence.size()][c2.getData()[0].length];

        System.out.println("Sizes = "+ firstParentSequences.length+","+secondParentSequences.length);

        int firstParentIndex = 0;
        int secondParentIndex = 0;

        for (int i=0;i<numberOfSequences;i++){
           if (similarSequence.contains(i)){
               firstParentSequences[firstParentIndex] = c1.getData()[i];
               firstParentIndex++;
           } else {
               secondParentSequences[secondParentIndex] = c2.getData()[i];
               secondParentIndex++;
               System.out.println("Index = "+secondParentIndex);
           }
       }

       ArrayList<ArrayList<Character>> childData = createChild(usedSequences,numberOfSequences,firstParentSequences,secondParentSequences);

       SequenceUtil.removeEmptyColumns(childData);

       Chromossome child  = new BasicListChromossome(childData);
       child.setOrigin("SS_COV");
       child.setRootOrigin("SS_COV");
       returnValue.add(child);

        firstParentSequences = new char[similarSequence.size()][c2.getData()[0].length];
        secondParentSequences = new char[complementarySequence.size()][c1.getData()[0].length];

        firstParentIndex = 0;
        secondParentIndex = 0;

        for (int i=0;i<numberOfSequences;i++){
           if (similarSequence.contains(i)){
               firstParentSequences[firstParentIndex] = c2.getData()[i];
               firstParentIndex++;
           } else {
               secondParentSequences[secondParentIndex] = c1.getData()[i];
               secondParentIndex++;
           }
       }
        
       childData = createChild(usedSequences,numberOfSequences,firstParentSequences,secondParentSequences);

       SequenceUtil.removeEmptyColumns(childData);

       child  = new BasicListChromossome(childData);
       child.setOrigin("SS_COV");
       child.setRootOrigin("SS_COV");
       returnValue.add(child);



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

    private Map<Integer, Integer> defineUsedSequences(List<Integer> similarSequences, int numberOfSequences) {
        Map<Integer,Integer> returnValue = new HashMap<Integer,Integer>();

        for (int i=0;i<numberOfSequences;i++){
            if (similarSequences.contains(i)){
                returnValue.put(i, 0);
            } else {
                returnValue.put(i, 1);
            }
        }

        return returnValue;
    }

}
