/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.conservedblocks;

import java.util.ArrayList;
import java.util.List;
import jbio.alignment.Sequence;

/**
 * NOT BEING USED!!!!
 *
 * @author sergio
 */
public class BasicConservedBlockFinder {

    private static final int WINDOW_SIZE = 3;
    private static final double ACCEPTANCE_TRIGGER = 1;


    public static List<ConservedBlock> findBlocks(Sequence sequence1, Sequence sequence2){
        List<ConservedBlock> returnValue = new ArrayList<ConservedBlock>();

        if (sequence1 != null && sequence2 != null &&
            sequence2.getDataArray() != null && sequence2.getDataArray() != null)
        {
            char[] seq1Data = sequence1.getDataArray();
            char[] seq2Data = sequence2.getDataArray();

            int matchScore = 0;
            int matchMaxScore = 0;
            double score = 0;

            for (int i=0; i < seq1Data.length - WINDOW_SIZE; i++){
                for (int j=0; j < seq2Data.length - WINDOW_SIZE; j++){
                    matchScore = 0;
                    matchMaxScore = WINDOW_SIZE;
                    //Analize a sequence window
                    for (int k=0; k<WINDOW_SIZE; k++){
                        matchScore += checkMatch(seq1Data[i+k],seq2Data[j+k]);
                    }
                    score = (double)matchScore/(double)matchMaxScore;
                    if (score >= ACCEPTANCE_TRIGGER){
                        //Add conserved block to return list
                        ConservedBlock block = new ConservedBlock();
                        block.setSequence1Start(i);
                        block.setSequence1End(i + WINDOW_SIZE);
                        block.setSequence2Start(j);
                        block.setSequence2End(j + WINDOW_SIZE);
                        block.setScore(score);

                        System.out.println(block.getSequence1Start()+","+block.getSequence1End());
                        System.out.println(block.getSequence2Start()+","+block.getSequence2End());
                        System.out.println();

                        returnValue.add(block);
                    }
                }
            }
        }

        return returnValue;
    }

    private static int checkMatch(char characterSeq1, char characterSeq2) {
        return (characterSeq1 == characterSeq2) ? 1: 0;
    }

}
