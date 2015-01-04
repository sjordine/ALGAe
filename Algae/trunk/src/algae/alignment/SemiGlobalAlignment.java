/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.alignment;

import algae.util.alignment.MatrixUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergio
 */
public class SemiGlobalAlignment extends PairWiseAlignment {

    private Sequence sequence1;
    private Sequence sequence2;
    private Matrix scoreMatrix;

    public SemiGlobalAlignment(Sequence sequence1, Sequence sequence2, Matrix scoreMatrix){
        this.sequence1 = sequence1;
        this.sequence2 = sequence2;
        this.scoreMatrix = scoreMatrix;
    }


     public char[][] align(){
        char[][] alignment = null;
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
                    matrix[i][j] = Math.max (
                            Math.max(matrix[i-1][j]+MatrixUtil.getScore(scoreMatrix, seq1[i-1], '-'),
                                     matrix[i][j-1]+MatrixUtil.getScore(scoreMatrix, seq2[j-1], '-')),
                            matrix[i-1][j-1] + MatrixUtil.getScore(scoreMatrix, seq1[i-1], seq2[j-1]));
                }
            }


            //Mount alignment
            List[] alignedSequences = new List[2];
            alignedSequences[0] = new ArrayList<Character>();
            alignedSequences[1] = new ArrayList<Character>();

            int i = seq1.length;
            int j = seq2.length;

            int maxI = seq1.length;
            int maxJ = seq2.length;

            double max = Double.MIN_VALUE;


            for (int pos=seq1.length;pos>=0;pos--){
                if (matrix[pos][seq2.length] > max){
                    max = matrix[pos][seq2.length];
                    maxI = pos;
                    maxJ = seq2.length;
                }
            }

            for (int pos=seq2.length;pos>=0;pos--){
                if (matrix[seq1.length][pos] > max){
                    max = matrix[seq1.length][pos];
                    maxJ = pos;
                    maxI = seq1.length;
                }
            }


            maxScore = matrix[maxI][maxJ];


            //Last gaps
            while (i>maxI || j>maxJ){
                if (i>maxI){
                    alignedSequences[0].add(0,seq1[i-1]);
                    alignedSequences[1].add(0,'-');
                    i--;
                }
                if (j>maxJ){
                    alignedSequences[0].add(0,'-');
                    alignedSequences[1].add(0,seq2[j-1]);
                    j--;
                }
            }


            while (i > 0 && j> 0){
                //Align both sequences
                if (matrix[i][j]-matrix[i-1][j-1]==MatrixUtil.getScore(scoreMatrix, seq1[i-1], seq2[j-1])){
                    alignedSequences[0].add(0,seq1[i-1]);
                    alignedSequences[1].add(0,seq2[j-1]);
                    i--;
                    j--;
                }
                else {
                    //Align seq1 with gap
                    if (matrix[i][j]-matrix[i-1][j]==MatrixUtil.getScore(scoreMatrix,seq1[i-1],'-')){
                         alignedSequences[0].add(0,seq1[i-1]);
                        alignedSequences[1].add(0,'-');
                        i--;
                    } else {
                         //Align seq2 with gap
                         if (matrix[i][j]-matrix[i][j-1]==MatrixUtil.getScore(scoreMatrix,seq2[j-1],'-')){
                            alignedSequences[0].add(0,'-');
                            alignedSequences[1].add(0,seq2[j-1]);
                            j--;
                        }
                    }
                }
            }

            if (j==0 && i>0){
                //Align beginning of seq1 with gaps
                while (i>0){
                    alignedSequences[0].add(0,seq1[i-1]);
                    alignedSequences[1].add(0,'-');
                    i--;
                }
            }

            if (i==0 && j>0){
                //Align beginning of seq2 with gaps
                while (j>0){
                    alignedSequences[0].add(0,'-');
                    alignedSequences[1].add(0,seq2[j-1]);
                    j--;
                }
            }

            //Copy alignment

            alignment = new char[2][alignedSequences[0].size()];
            for (i=0;i<alignment[0].length;i++){
                alignment[0][i] =(Character)alignedSequences[0].get(i);
                alignment[1][i] =(Character)(alignedSequences[1].get(i));
            }



        }


        return alignment;
    }


}
