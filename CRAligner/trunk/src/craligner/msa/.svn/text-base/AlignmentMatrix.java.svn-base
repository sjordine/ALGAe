/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package craligner.msa;

import craligner.conservedregion.FillRegionAligner;
import craligner.environment.Environment;
import java.util.List;
import jbio.alignment.Sequence;
import jbio.score.PairWiseScoreGapAffinity;

/**
 *
 * @author sergio
 */
public class AlignmentMatrix {

    private AlignmentInfo[][] matrix;

    public void generateMatrix(List<Sequence> sequences) {

        Environment environment = Environment.getInstance();

        matrix = new AlignmentInfo[sequences.size()][sequences.size()];

        if (sequences != null) {
            for (int i = 0; i < sequences.size(); i++) {
                for (int j = i; j < sequences.size(); j++) {
                    char[][] result = FillRegionAligner.align(sequences.get(i), sequences.get(j), environment.getMatrixLocal(), environment.getMatrixGlobal());


                    StringBuilder seq1 = new StringBuilder();
                    StringBuilder seq2 = new StringBuilder();
                    char[][] mirroredResult = new char[2][result[0].length];

                    for (int k = 0; k < result[0].length; k++) {
                        seq1.append(result[0][k]);
                        seq2.append(result[1][k]);
                        mirroredResult[0][k]=result[1][k];
                        mirroredResult[1][k]=result[0][k];
                    }

                    double score = PairWiseScoreGapAffinity.getScore(seq1.toString(), seq2.toString(), environment.getMatrixGlobal());

                    AlignmentInfo alignmentInfo = new AlignmentInfo();
                    alignmentInfo.setSequence1(i);
                    alignmentInfo.setSequence2(j);
                    alignmentInfo.setAlignment(result);
                    alignmentInfo.setScore(score);
                    matrix[i][j] = alignmentInfo;

                     if (i!=j){
                        AlignmentInfo mirroredAlignmentInfo = new AlignmentInfo();
                        mirroredAlignmentInfo.setSequence1(j);
                        mirroredAlignmentInfo.setSequence2(i);
                        mirroredAlignmentInfo.setAlignment(mirroredResult);
                        mirroredAlignmentInfo.setScore(score);


                        matrix[j][i] = mirroredAlignmentInfo;
                    }                   

                }
            }
        }

    }

    /**
     * @return the matrix
     */
    public AlignmentInfo[][] getMatrix() {
        return matrix;
    }

    /**
     * @param matrix the matrix to set
     */
    public void setMatrix(AlignmentInfo[][] matrix) {
        this.matrix = matrix;
    }
}
