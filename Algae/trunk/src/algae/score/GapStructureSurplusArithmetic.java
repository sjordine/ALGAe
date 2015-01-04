/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.score;

import algae.alignment.Matrix;

/**
 *
 * @author sergio
 */
public class GapStructureSurplusArithmetic extends GapAffinitySumOfPairsStructureSurplusScore {

     public GapStructureSurplusArithmetic(Matrix matrix){
        super(matrix);
    }

    @Override
    public double Mean(double value1, double value2) {
        return value1 + value2;
    }

}
