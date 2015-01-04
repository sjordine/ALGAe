/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jbio.alignment.msa;

import java.util.ArrayList;
import java.util.List;
import jbio.alignment.Matrix;
import jbio.util.alignment.MatrixUtil;

/**
 *
 * @author sergio
 */
public class ProfileGlobalAlignment {

    public static List<List<Character>> align(Matrix scoreMatrix,
            List<List<Character>> s1,
            int startIndex1, int endIndex1,
            List<List<Character>> s2,
            int startIndex2, int endIndex2) {


        //Create return alignment structure
        List<List<Character>> returnValue = new ArrayList<List<Character>>();
        double maxScore = 0;

        for (int i = 0; i < s1.size(); i++) {
            if (s1.get(i) != null || s2.get(i) != null) {
                returnValue.add(new ArrayList<Character>());
            } else {
                returnValue.add(null);
            }
        }


        //Prepare subalignments
        //Remove null elements and keep 
        //a translation array to generate final alignment.
        //keep just the sequence part that must be aligned
        List<Integer> subAlignment1Indexes = new ArrayList<Integer>();
        List<List<Character>> subAlignment1 = new ArrayList<List<Character>>();
        List<Integer> subAlignment2Indexes = new ArrayList<Integer>();
        List<List<Character>> subAlignment2 = new ArrayList<List<Character>>();

        createSubAlignmentSummary(s1, subAlignment1, subAlignment1Indexes, startIndex1, endIndex1);
        createSubAlignmentSummary(s2, subAlignment2, subAlignment2Indexes, startIndex2, endIndex2);

        //Align sequences
        ScorePositionInfo[][] matrix = null;
        matrix = alignmentMatrix(subAlignment1, subAlignment2, scoreMatrix);

        //Mount alignment
        int sizeSub1 = subAlignment1.get(0).size();
        int sizeSub2 = subAlignment2.get(0).size();

        int i = sizeSub1;
        int j = sizeSub2;

        maxScore = matrix[i][j].getScore();

        while (i > 0 || j > 0) {
            switch (matrix[i][j].getDirection()) {
                case NE:
                    addPosition(returnValue, subAlignment1, i-1, subAlignment2, j-1, subAlignment1Indexes, subAlignment2Indexes);
                    i--;
                    j--;
                    break;
                case E:
                    addPosition(returnValue, subAlignment1, i-1, null, 0, subAlignment1Indexes, subAlignment2Indexes);
                    i--;
                    break;
                case N:
                    addPosition(returnValue, null, 0, subAlignment2, j-1, subAlignment1Indexes, subAlignment2Indexes);
                    j--;
                    break;
            }
        }

        /*
        for (i=0; i < returnValue.size();i++){
            List<Character> seq = returnValue.get(i);
            if (seq!=null){
                System.err.print(i+"-");
                for (j=0; j < seq.size(); j++){
                    char currChar = seq.get(j);
                    System.err.print(currChar);
                }
                System.err.println();
            }
        }
         */

        return returnValue;

    }

    private static void createSubAlignmentSummary(List<List<Character>> s, List<List<Character>> subAlignment, List<Integer> subAlignmentIndexes,
            int startIndex, int endIndex) {
        for (int i = 0; i < s.size(); i++) {
            List<Character> currSequence = s.get(i);
            if (currSequence != null) {
                //If this sequence exists on the base sub-alignment
                //keep it on the summary alignment
                subAlignment.add(createSubAlignmentExcerpt(currSequence, startIndex, endIndex));
                //Map the original index to the index into this summary
                subAlignmentIndexes.add(i);
            }
        }
    }

    private static List<Character> createSubAlignmentExcerpt(List<Character> currSequence, int startIndex, int endIndex) {
        List<Character> returnValue = null;

        if (currSequence != null && startIndex >= 0 && endIndex < currSequence.size()) {
            returnValue = new ArrayList<Character>();
            for (int i = startIndex; i <= endIndex; i++) {
                returnValue.add(currSequence.get(i));
            }
        } else {
            throw new RuntimeException("Invalid Sequence or indexes");
        }

        


        return returnValue;
    }

    public static ScorePositionInfo[][] alignmentMatrix(List<List<Character>> sub1, List<List<Character>> sub2, Matrix scoreMatrix) {
        ScorePositionInfo[][] matrix = null;

        int sizeSub1 = sub1.get(0).size();
        int sizeSub2 = sub2.get(0).size();

        if (sub1 != null && sub1 != null && scoreMatrix != null) {

            matrix = new ScorePositionInfo[sizeSub1 + 1][sizeSub2 + 1];

            //Initialize Matrix
            matrix[0][0] = new ScorePositionInfo();
            matrix[0][0].setScore(0);

            for (int i = 1; i < matrix.length; i++) {
                matrix[i][0] = new ScorePositionInfo();
                matrix[i][0].setDirection(ScorePositionInfo.DIRECTION.E);
                matrix[i][0].setScore(matrix[i-1][0].getScore()+getScore(scoreMatrix, sub1, i-1, null, 0));
            }

            for (int i = 1; i < matrix[0].length; i++) {
                matrix[0][i] = new ScorePositionInfo();
                matrix[0][i].setDirection(ScorePositionInfo.DIRECTION.N);
                matrix[0][i].setScore(matrix[0][i-1].getScore()+getScore(scoreMatrix, null, 0, sub2, i-1));
            }

            //Populate matrix
            for (int i = 1; i <= sizeSub1; i++) {
                for (int j = 1; j <= sizeSub2; j++) {
                    double ss = matrix[i - 1][j - 1].getScore() + getScore(scoreMatrix, sub1, i - 1, sub2, j - 1);
                    double _s = matrix[i][j - 1].getScore() + getScore(scoreMatrix, null, 0, sub2, j - 1);
                    double s_ = matrix[i - 1][j].getScore() + getScore(scoreMatrix, sub1, i - 1, null, 0);

                    double score = 0;
                    ScorePositionInfo.DIRECTION direction = ScorePositionInfo.DIRECTION.NE;

                    if (ss > s_) {
                        if (ss > _s) {
                            score = ss;
                            direction = ScorePositionInfo.DIRECTION.NE;
                        } else {
                            score = _s;
                            direction = ScorePositionInfo.DIRECTION.N;
                        }
                    } else {
                        if (s_ > _s) {
                            score = s_;
                            direction = ScorePositionInfo.DIRECTION.E;
                        } else {
                            score = _s;
                            direction = ScorePositionInfo.DIRECTION.N;
                        }
                    }

                    matrix[i][j] = new ScorePositionInfo();
                    matrix[i][j].setScore(score);
                    matrix[i][j].setDirection(direction);
                }
            }

        }

        return matrix;
    }

    private static double getScore(Matrix matrix, List<List<Character>> alignment1, int pos1, List<List<Character>> alignment2, int pos2) {
        double returnValue = 0;

        if (alignment1 != null && alignment2 != null) {

            for (int i = 0; i < alignment1.size(); i++) {

                char currChar1 = alignment1.get(i).get(pos1);

                for (int j = 0; j < alignment2.size(); j++) {
                    char currChar2 = alignment2.get(j).get(pos2);

                    returnValue += MatrixUtil.getScore(matrix, currChar1, currChar2);
                }
            }

        } else {
            if (alignment1 != null && alignment2 == null) {

                for (int i = 0; i < alignment1.size(); i++) {

                    char currChar1 = alignment1.get(i).get(pos1);

                    returnValue += MatrixUtil.getScore(matrix, currChar1, '-');

                }

            } else {
                if (alignment1 == null && alignment2 != null) {

                    for (int i = 0; i < alignment2.size(); i++) {

                        char currChar2 = alignment2.get(i).get(pos2);

                        returnValue += MatrixUtil.getScore(matrix, '-', currChar2);

                    }

                } else {
                    throw new RuntimeException("Invalid score!!!");
                }
            }
        }


        return returnValue;
    }

    private static void addPosition(List<List<Character>> returnValue, List<List<Character>> subAlignment1,
            int pos1, List<List<Character>> subAlignment2,
            int pos2, List<Integer> subAlignment1Indexes,
            List<Integer> subAlignment2Indexes) {
        if (subAlignment1 != null && subAlignment2 != null) {
            copyChar(returnValue, subAlignment1, pos1, subAlignment1Indexes);
            copyChar(returnValue, subAlignment2, pos2, subAlignment2Indexes);
        } else {
            if (subAlignment1 == null) {
                if (subAlignment2 != null) {
                    createGap(returnValue, subAlignment1Indexes);
                    copyChar(returnValue, subAlignment2, pos2, subAlignment2Indexes);
                } else {
                    throw new RuntimeException("Error! Two empty alignments");
                }
            } else {
                copyChar(returnValue, subAlignment1, pos1, subAlignment1Indexes);
                createGap(returnValue, subAlignment2Indexes);
            }
        }
    }

    private static void copyChar(List<List<Character>> alignment, List<List<Character>> subAlignment, int pos, List<Integer> subAlignmentIndexes) {
        for (int i = 0; i < subAlignment.size(); i++) {
            int index = subAlignmentIndexes.get(i);
            char currChar = subAlignment.get(i).get(pos);
            alignment.get(index).add(0, currChar);
        }
    }

    private static void createGap(List<List<Character>> alignment, List<Integer> subAlignmentIndexes) {
        for (int i = 0; i < subAlignmentIndexes.size(); i++) {
            int index = subAlignmentIndexes.get(i);
            alignment.get(index).add(0, '-');
        }
    }
}
