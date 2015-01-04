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
public abstract class GapAffinitySumOfPairStructureBase implements Score{

   private final Matrix matrix;
    private static final int FACTOR = 2;

    public GapAffinitySumOfPairStructureBase(Matrix matrix) {
        this.matrix = matrix;
    }

    public abstract double Mean(double value1, double value2);

    public Double calculateScore(Chromossome c) {
        Double score = 0.0;
        Double positionScore = 0.0;

        double biasValue = 0.0;
        double mu = 0.0; // confidance rate

        Environment environment = Environment.getInstance();

        double pi = getPi();


        if (c != null && matrix != null) {
            char[][] alignment = c.getData();
            if (alignment != null) {
                int sequenceCount = alignment.length;



                //For each sequence
                for (int base1 = 0; base1 < (sequenceCount - 1); base1++) {


                    //For each other sequence (that was not checked against i yet ...
                    for (int base2 = base1 + 1; base2 < sequenceCount; base2++) {
                        int cursorSeq1 = -1;
                        int cursorSeq2 = -1;
                        //Check every position
                        for (int k = 0; k < alignment[0].length; k++) {
                            Double currentScore = 0.0;
                            //Calculate gap affinity pairwise residue score
                            if ((k > 0 && alignment[base1][k] == '-' && alignment[base2][k] != '-' && alignment[base1][k - 1] == '-')
                                    || (k > 0 && alignment[base2][k] == '-' && alignment[base1][k] != '-' && alignment[base2][k - 1] == '-')) {
                                currentScore += (MatrixUtil.getScore(matrix, alignment[base1][k], alignment[base2][k],true));
                            } else {
                                currentScore += MatrixUtil.getScore(matrix, alignment[base1][k], alignment[base2][k]);
                            }
                            //Add secondary structure score bias
                            if (alignment[base1][k] != '-') {
                                cursorSeq1++;
                            }
                            if (alignment[base2][k] != '-') {
                                cursorSeq2++;
                            }
                            if (alignment[base1][k] != '-' && alignment[base2][k] != '-') {
                                ResidueStructure residue1Structure = null;
                                ResidueStructure residue2Structure = null;
                                //get structure associated with each residue

                                 residue1Structure = environment.getResidueStructure(base1, cursorSeq1);
                                 residue2Structure = environment.getResidueStructure(base2, cursorSeq2);


                                if (residue1Structure != null && residue2Structure != null) {
                                    if (residue1Structure.getStructure() != 'C' && residue2Structure.getStructure() != 'C') {
                                        //Bias according to right structure alignment (H-H or E-E)
                                        if (residue1Structure.getStructure() == residue2Structure.getStructure()) {
                                            biasValue = 1.0;
                                        } else {
                                            biasValue = -1.0;
                                        }
                                        //Confidance rate
                                        double conf1 = (1.0 + residue1Structure.getConfidence()) / 10.0;
                                        double conf2 = (1.0 + residue2Structure.getConfidence()) / 10.0;
                                        mu = Mean(conf1, conf2);

                                        //Adjusted score
                                        currentScore = adjustScore(currentScore, mu, pi, biasValue);
                                        //currentScore = currentScore * (1 + biasValue * mu * pi);


                                    } else {
                                        //Coil - bias = 0 - nothing to do
                                    }

                                } else {
                                    throw new RuntimeException("Error getting residues (" + base1 + "," + k + ") or (" + base2 + "," + k + ")");
                                }

                            } else {
                                //bias = 0, nothing else to do
                            }

                            score += currentScore;
                        }
                    }
                }

            }
        }

        return score;
    }

    /**
     * @return the pi
     */
    public double getPi() {
        Environment environment = Environment.getInstance();
        return environment.getPi();
    }


    public abstract double adjustScore(double currentScore,double mu, double pi, double bias);

}
