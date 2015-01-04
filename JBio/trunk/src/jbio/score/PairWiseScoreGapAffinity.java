/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jbio.score;

import jbio.alignment.Matrix;
import jbio.util.alignment.MatrixUtil;

/**
 *
 * @author sergio
 */
public class PairWiseScoreGapAffinity {

        private static final int FACTOR = 2;
    
        public static double getScore(String alignedSequence1,String alignedSequence2,Matrix scoreMatrix){
        double returnValue = 0;

        for (int i=0;i<alignedSequence1.length();i++){

            if ((i > 0 && alignedSequence1.charAt(i) == '-' && alignedSequence2.charAt(i)!='-' && alignedSequence1.charAt(i - 1) == '-')
                                    || (i > 0 && alignedSequence2.charAt(i) == '-' && alignedSequence1.charAt(i)!='-' &&
                                    alignedSequence2.charAt(i - 1) == '-')) {
                                returnValue += (MatrixUtil.getScore(scoreMatrix, alignedSequence1.charAt(i), alignedSequence2.charAt(i))/FACTOR);
                            } else {
                                returnValue += MatrixUtil.getScore(scoreMatrix, alignedSequence1.charAt(i), alignedSequence2.charAt(i));
                            }
        }

        return returnValue;
    }


}
