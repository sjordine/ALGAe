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
public class PairWiseScore {

    public static double getScore(String alignedSequence1,String alignedSequence2,Matrix scoreMatrix){
        double returnValue = 0;

        for (int i=0;i<alignedSequence1.length();i++){
            returnValue+=MatrixUtil.getScore(scoreMatrix, alignedSequence1.charAt(i), alignedSequence2.charAt(i));
        }

        return returnValue;
    }

}
