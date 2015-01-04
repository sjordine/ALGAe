/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.operators.crossover;

import algae.alignment.Matrix;
import algae.chromossome.BasicListChromossome;
import algae.chromossome.Chromossome;
import algae.environment.Environment;
import algae.philogeny.matrix.DistanceInfo;
import algae.philogeny.matrix.DistanceMatrix;
import algae.philogeny.matrix.DistanceType;
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
public class BestPartialAlignmentAnchorCO2 implements CrossOverOperator{


    public List<Chromossome> execute(Chromossome c1, Chromossome c2) {

        List<Chromossome> returnValue = new ArrayList<Chromossome>();

        int numberOfSequences = c1.getData().length;
        double[][] sumOfDistances = new double[2][numberOfSequences];
        Map<Integer, Integer> usedSequences = new HashMap<Integer, Integer>();


        //Calculate sum of distances for all sequences
        calculateSequenceDistances(sumOfDistances, 0, c1.getData());
        calculateSequenceDistances(sumOfDistances, 1, c2.getData());

        //Select best sequences
        usedSequences = selectBestSequences(numberOfSequences, sumOfDistances);

        //Create new alignment based on the sequences defined above
        //Get sub-alignments from each parent
        int firstParentSize = 0;
        int secondParentSize = 0;

        for (int i = 0; i < numberOfSequences; i++) {
            if (usedSequences.get(i) == 0) {
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



        for (int i = 0; i < numberOfSequences; i++) {
            if (usedSequences.get(i) == 0) {
                firstParentSequences[firstParentIndex] = c1.getData()[i];
                firstParentIndex++;
            } else {
                secondParentSequences[secondParentIndex] = c2.getData()[i];
                secondParentIndex++;
            }
        }

        //Set anchors
        int[] anchorIndex = new int[2];
        anchorIndex[0] = -1;
        anchorIndex[1] = -1;

        for (int i = 0; i < numberOfSequences; i++) {
            int currentSequence = usedSequences.get(i);
            if (anchorIndex[currentSequence] == -1) {
                anchorIndex[currentSequence] = i;
            } else {
                if (sumOfDistances[currentSequence][i] > sumOfDistances[currentSequence][anchorIndex[currentSequence]]) {
                    anchorIndex[currentSequence] = i;
                }
            }
        }

        //Get best sub-alignment between both anchors
        int pivot = getPivotAlignment(anchorIndex, c1.getData(), c2.getData());


        ArrayList<ArrayList<Character>> childData = null;

        childData = new ArrayList<ArrayList<Character>>();

        //Initialize data

        for (int i = 0; i < numberOfSequences; i++) {
            childData.add(new ArrayList<Character>());
        }

        
        char[][] pivotAlignment = (pivot == 0) ? c1.getData() : c2.getData();

        Environment environment = Environment.getInstance();
        DistanceMatrix distances = environment.getDistanceMatrix();
        DistanceInfo[][] distanceInfo = distances.getBestDistances();
        DistanceType type = distanceInfo[anchorIndex[0]][anchorIndex[1]].getType();


        int[] subAlignmentAnchorIndex = new int[2];

        firstParentIndex = 0;
        secondParentIndex = 0;

        //Get anchor index within the sub-alignments
        for (int i = 0; i < numberOfSequences; i++) {
            if (usedSequences.get(i) == 0) {
                if (i == anchorIndex[0]) {
                    subAlignmentAnchorIndex[0] = firstParentIndex;
                }
                firstParentIndex++;
            } else {
                if (i == anchorIndex[1]) {
                    subAlignmentAnchorIndex[1] = secondParentIndex;
                }
                secondParentIndex++;
            }
        }



        //Merge sub-alignment using anchors
        mountAlignment(childData, usedSequences, 0, pivotAlignment, 0, firstParentSequences, subAlignmentAnchorIndex[0],numberOfSequences);
        mountAlignment(childData, usedSequences, 1, pivotAlignment, 1, secondParentSequences, subAlignmentAnchorIndex[1],numberOfSequences);

        SequenceUtil.fillGapsAtEnd(childData);
        SequenceUtil.removeEmptyColumns(childData);

        Chromossome child = new BasicListChromossome(childData);

        child.setOrigin("BPAA2_COV");
        child.setRootOrigin("BPAA2_COV");

       

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

        int numberOfSequences = alignment.length;
        int alignmentSize = alignment[0].length;

        for (int i = 0; i < numberOfSequences; i++) {
            for (int j = 0; j < numberOfSequences; j++) {
                if (i != j) {
                    for (int k = 0; k < alignmentSize; k++) {
                        sumOfDistances[alignmentIndex][i] += MatrixUtil.getScore(matrix, alignment[i][k], alignment[j][k]);
                    }
                }
            }
        }
    }

    private Map<Integer, Integer> selectBestSequences(int numberOfSequences, double[][] sumOfDistances) {

        Map<Integer, Integer> returnValue = new HashMap<Integer, Integer>();

        int currentAlignment = 0;

        for (int i = 0; i < numberOfSequences; i++) {
            Double bestScore = null;
            Integer selectedIndex = null;
            for (int j = 0; j < numberOfSequences; j++) {
                if (returnValue.get(j) == null) {
                    if (bestScore != null) {
                        if (sumOfDistances[currentAlignment][j] > bestScore) {
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
            if (selectedIndex != null) {
                returnValue.put(selectedIndex, currentAlignment);
            } else {
                //TODO Error! Throw exception
                System.out.println("Error executing BestPartialAlignmentCrossOver");
            }

            //Switch to use the other parent next time
            currentAlignment = (currentAlignment + 1) % 2;
        }

        return returnValue;
    }

    private int getPivotAlignment(int[] anchorIndex, char[][] alignment1, char[][] alignment2) {

        int returnValue = 0;
        double sum1 = 0;
        double sum2 = 0;

        //Get score matrix
        Environment environment = Environment.getInstance();
        Matrix matrix = environment.getMatrix();

        //Get best score between both anchors (on both alignments)
        for (int i = 0; i < alignment1.length; i++) {
            sum1 += MatrixUtil.getScore(matrix, alignment1[anchorIndex[0]][i], alignment1[anchorIndex[1]][i]);
        }

        for (int i = 0; i < alignment2.length; i++) {
            sum2 += MatrixUtil.getScore(matrix, alignment2[anchorIndex[0]][i], alignment1[anchorIndex[1]][i]);
        }


        returnValue = (sum1 > sum2) ? 0 : 1;


        return returnValue;
    }

    private void mountAlignment(ArrayList<ArrayList<Character>> childData, Map<Integer, Integer> usedSequences, int subAlignmentId,
            char[][] pivotAlignment, int anchorIndex, char[][] subAlignment, int subAlignmentAnchor,int numberOfSequences) {
        int pivotCursor = 0;
        int subAlignmentCursor = 0;
        int subAlignmentIndex = 0;

        while (pivotCursor < pivotAlignment[0].length && subAlignmentCursor < subAlignment[0].length) {
            subAlignmentIndex = 0;

            if (pivotAlignment[anchorIndex][pivotCursor] == subAlignment[subAlignmentAnchor][subAlignmentCursor]) {
                for (int j = 0; j < numberOfSequences; j++) {
                    if (usedSequences.get(j) == subAlignmentId) {
                        childData.get(j).add(subAlignment[subAlignmentIndex][subAlignmentCursor]);
                        subAlignmentIndex++;
                    }
                }
                pivotCursor++;
                subAlignmentCursor++;
            } else {

                if (pivotAlignment[anchorIndex][pivotCursor] == '-') {

                    //gap on pivot
                    //add a gap, advance pivot keeping sub-alignment sequence index
                    for (int j = 0; j < numberOfSequences; j++) {
                        if (usedSequences.get(j) == subAlignmentId) {
                            childData.get(j).add('-');
                        }
                    }
                    pivotCursor++;
                } else {
                    //gap on sub-alignment sequence, but not on pivot
                    //add all letters associated with this position on sub-alignment
                    for (int j = 0; j < numberOfSequences; j++) {
                        if (usedSequences.get(j) == subAlignmentId) {
                            childData.get(j).add(subAlignment[subAlignmentIndex][subAlignmentCursor]);
                            subAlignmentIndex++;
                        }
                    }
                    subAlignmentCursor++;
                }
            }
        }

        while (pivotCursor < pivotAlignment[0].length) {
            for (int j = 0; j < numberOfSequences; j++) {
                if (usedSequences.get(j) == subAlignmentId) {
                    childData.get(j).add('-');
                }
            }
            pivotCursor++;
        }

        while (subAlignmentCursor < subAlignment[0].length) {
            subAlignmentIndex=0;
            for (int j = 0; j < numberOfSequences; j++) {
                if (usedSequences.get(j) == subAlignmentId) {
                   childData.get(j).add(subAlignment[subAlignmentIndex][subAlignmentCursor]);
                   subAlignmentIndex++;
                }
            }
            subAlignmentCursor++;
        }

    }

}
