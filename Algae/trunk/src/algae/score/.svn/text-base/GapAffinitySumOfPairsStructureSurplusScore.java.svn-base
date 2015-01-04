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
public abstract class GapAffinitySumOfPairsStructureSurplusScore extends GapAffinitySumOfPairStructureBase {

    public GapAffinitySumOfPairsStructureSurplusScore(Matrix matrix) {
       super(matrix);
    }

    public double adjustScore(double currentScore,double mu, double pi, double bias){
       return currentScore = currentScore * (1 + pi* (1+ (bias * mu)));
    }

}
