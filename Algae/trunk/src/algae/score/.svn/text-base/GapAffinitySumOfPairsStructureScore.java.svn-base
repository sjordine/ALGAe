/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.score;

import SecondaryStructure.ResidueStructure;
import algae.alignment.Matrix;
import algae.chromossome.Chromossome;
import algae.environment.Environment;
import algae.util.alignment.MatrixUtil;

/**
 *
 * @author sergio
 */
public abstract class GapAffinitySumOfPairsStructureScore extends GapAffinitySumOfPairStructureBase {

    public GapAffinitySumOfPairsStructureScore(Matrix matrix) {
       super(matrix);
    }

    public double adjustScore(double currentScore,double mu, double pi, double bias){
       return currentScore = currentScore * (1 + bias * mu * pi);
    }

}
