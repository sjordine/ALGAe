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
public class GapStructureSurplusHarmonic extends GapAffinitySumOfPairsStructureSurplusScore {

     public GapStructureSurplusHarmonic(Matrix matrix){
        super(matrix);
    }


    @Override
    public double Mean(double value1, double value2) {
        return 2 / ((1/value1) + (1/value2));
    }


}
