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
public class GapStructureSurplusGeometric extends GapAffinitySumOfPairsStructureSurplusScore {

     public GapStructureSurplusGeometric(Matrix matrix){
        super(matrix);
    }

    @Override
    public double Mean(double value1, double value2) {
        return Math.sqrt(value1*value2);
    }

}
