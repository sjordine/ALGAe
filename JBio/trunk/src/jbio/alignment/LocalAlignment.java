/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jbio.alignment;

import jbio.util.alignment.MatrixUtil;

/**
 *
 * @author sergio
 */
public class LocalAlignment extends PairWiseAlignment{

    private Sequence sequence1;
    private Sequence sequence2;
    private Matrix scoreMatrix;


    public LocalAlignment(Sequence sequence1, Sequence sequence2, Matrix scoreMatrix){
        this.sequence1 = sequence1;
        this.sequence2 = sequence2;
        this.scoreMatrix = scoreMatrix;
        maxScore=0;
    }

    @Override
    public double[][] alignmentMatrix() {
        double[][] matrix = null;

         if (sequence1!=null && sequence2!=null && scoreMatrix != null &&
                sequence1.getDataArray()!=null && sequence2.getDataArray()!=null){

            char[] seq1 = sequence1.getDataArray();
            char[] seq2 = sequence2.getDataArray();
            matrix = new double[seq1.length+1][seq2.length+1];

            //Initialize Matrix
            matrix[0][0] = 0;

            for (int i=1;i<matrix.length;i++){
                matrix[i][0] = 0;
            }

            for (int i=1;i<matrix[0].length;i++){
                matrix[0][i] = 0;
            }

            //Populate matrix
            for (int i=1; i <= seq1.length; i++){
                for (int j=1; j <= seq2.length; j++){
                    matrix[i][j] = Math.max(0,Math.max (
                            Math.max(matrix[i-1][j]+MatrixUtil.getScore(scoreMatrix, seq1[i-1], '-'),
                                     matrix[i][j-1]+MatrixUtil.getScore(scoreMatrix, seq2[j-1], '-')),
                            matrix[i-1][j-1] + MatrixUtil.getScore(scoreMatrix, seq1[i-1], seq2[j-1])));
                }
            }

         }

        return matrix;
    }

    @Override
    public char[][] align() {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
