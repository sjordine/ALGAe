/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jbio.alignment;

import java.util.ArrayList;
import java.util.List;
import jbio.util.alignment.MatrixUtil;

/**
 *
 * @author sergio
 */
public class GlobalAlignmentGapAffinity extends PairWiseAlignment {

    private Sequence sequence1;
    private Sequence sequence2;
    private Matrix scoreMatrix;
    private static final int GAP_FACTOR = 2;

    public GlobalAlignmentGapAffinity(Sequence sequence1, Sequence sequence2, Matrix scoreMatrix) {
        this.sequence1 = sequence1;
        this.sequence2 = sequence2;
        this.scoreMatrix = scoreMatrix;
        maxScore = 0;
    }

    @Override
    public double[][] alignmentMatrix() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public char[][] align() {
        char[][] returnValue = null;

        //Matrix for matches and mismatches
        double[][] matrixNW = null;
        //Matrix for gaps on sequence 1
        double[][] matrixW = null;
        //Matrix for gaps on sequence 2
        double[][] matrixN = null;


        //Create and initialize matrices
        char[] seq1 = sequence1.getDataArray();
        char[] seq2 = sequence2.getDataArray();
        matrixNW = new double[seq1.length + 1][seq2.length + 1];
        matrixN = new double[seq1.length + 1][seq2.length + 1];
        matrixW = new double[seq1.length + 1][seq2.length + 1];

        matrixNW[0][0] = 0;
        matrixN[0][0] = 0;
        matrixW[0][0] = 0;

        for (int i = 1; i < (seq1.length + 1); i++) {
            matrixNW[i][0] = Double.MIN_VALUE;
            matrixW[i][0] = Double.MIN_VALUE;
            matrixN[i][0] = ScoreGaps(seq1[i - 1], i);
        }

        for (int i = 1; i < (seq2.length + 1); i++) {
            matrixNW[0][i] = Double.MIN_VALUE;
            matrixW[0][i] = ScoreGaps(seq2[i - 1], i);
            matrixN[0][i] = Double.MIN_VALUE;
        }

        //Populate matrices
        for (int i = 1; i < (seq1.length + 1); i++) {
            for (int j = 1; j < (seq2.length + 1); j++) {
                matrixNW[i][j] = MatrixUtil.getScore(scoreMatrix, seq1[i - 1], seq2[j - 1])
                        + Math.max(matrixNW[i - 1][j - 1],
                        Math.max(matrixW[i - 1][j - 1], matrixN[i - 1][j - 1]));
                matrixW[i][j] = Math.max(matrixNW[i][j - 1] + ScoreGaps(seq2[j - 1], 1),
                        Math.max(matrixW[i][j - 1] + ExtendGap(seq2[j - 1], 1),
                        matrixN[i][j - 1] + ScoreGaps(seq2[j - 1], 1)));
                matrixN[i][j] = Math.max(matrixNW[i - 1][j] + ScoreGaps(seq1[i - 1], 1),
                        Math.max(matrixN[i - 1][j] + ExtendGap(seq1[i - 1], 1),
                        matrixW[i - 1][j] + ScoreGaps(seq1[i - 1], 1)));
            }
        }

        //Align

        List[] alignedSequences = new List[2];
        alignedSequences[0] = new ArrayList<Character>();
        alignedSequences[1] = new ArrayList<Character>();

        int i = seq1.length;
        int j = seq2.length;

        maxScore = Math.max(matrixNW[i][j], Math.max(matrixW[i][j], matrixN[i][j]));


        while (i > 0 && j > 0) {
            if (matrixNW[i][j] > matrixW[i][j] && matrixNW[i][j] > matrixN[i][j]) {
                //Best alignment on this position is a match or mismatch
                alignedSequences[0].add(0, seq1[i - 1]);
                alignedSequences[1].add(0, seq2[j - 1]);
                i--;
                j--;
            } else {
                if (matrixW[i][j] < matrixN[i][j]) {
                    //Best alignment on this position is add a gap on sequence 2
                    alignedSequences[0].add(0, seq1[i - 1]);
                    alignedSequences[1].add(0, '-');
                    i--;
                } else {
                    //Best alignment on this position is add a gap on sequence 1
                    alignedSequences[0].add(0, '-');
                    alignedSequences[1].add(0, seq2[j - 1]);
                    j--;
                }
            }
        }

        if (j == 0 && i > 0) {
            //Align beginning of seq1 with gaps
            while (i > 0) {
                alignedSequences[0].add(0, seq1[i - 1]);
                alignedSequences[1].add(0, '-');
                i--;
            }
        }

        if (i == 0 && j > 0) {
            //Align beginning of seq2 with gaps
            while (j > 0) {
                alignedSequences[0].add(0, '-');
                alignedSequences[1].add(0, seq2[j - 1]);
                j--;
            }
        }

        returnValue = new char[2][alignedSequences[0].size()];
        for (i = 0; i < returnValue[0].length; i++) {
            returnValue[0][i] = (Character) alignedSequences[0].get(i);
            returnValue[1][i] = (Character) (alignedSequences[1].get(i));
        }



        return returnValue;
    }

    private double ScoreGaps(char letter, int i) {
        return MatrixUtil.getScore(scoreMatrix, letter, '-') + (MatrixUtil.getScore(scoreMatrix, letter, '-') / GAP_FACTOR) * i;
    }

    private double ExtendGap(char letter, int i) {
        return (MatrixUtil.getScore(scoreMatrix, letter, '-') / GAP_FACTOR) * i;
    }
}
