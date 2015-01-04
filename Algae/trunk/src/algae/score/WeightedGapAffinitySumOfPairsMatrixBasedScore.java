/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.score;

import algae.alignment.Matrix;
import algae.chromossome.Chromossome;
import algae.environment.Environment;
import algae.util.alignment.MatrixUtil;

/**
 *
 * @author sergio
 */
public class WeightedGapAffinitySumOfPairsMatrixBasedScore implements Score {

    private final Matrix matrix;
    private static final int FACTOR = 2;

    public WeightedGapAffinitySumOfPairsMatrixBasedScore(Matrix matrix) {
        this.matrix = matrix;
    }

    public Double calculateScore(Chromossome c) {

        Double score = 0.0;
        Environment environment = Environment.getInstance();



        if (c != null && matrix != null) {
            char[][] alignment = c.getData();
            if (alignment != null) {
                int sequenceCount = alignment.length;
                for (int i = 0; i < alignment[0].length; i++) {
                    for (int base1 = 0; base1 < (sequenceCount - 1); base1++) {
                        for (int base2 = base1 + 1; base2 < sequenceCount; base2++) {
                            if ((i > 0 && alignment[base1][i] == '-' && alignment[base2][i]!='-' && alignment[base1][i - 1] == '-')
                                    || (i > 0 && alignment[base2][i] == '-' && alignment[base1][i]!='-' && alignment[base2][i - 1] == '-')) {
                                score += (MatrixUtil.getScore(matrix, alignment[base1][i], alignment[base2][i])/FACTOR)*environment.getDistanceFactor(base1, base2);
                            } else {
                                score += MatrixUtil.getScore(matrix, alignment[base1][i], alignment[base2][i])*environment.getDistanceFactor(base1, base2);
                            }
                        }
                    }
                }
            }
        }

        return score;
    }
}
