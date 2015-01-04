/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.util.alignment;

import algae.alignment.Aminoacids;
import algae.alignment.Matrix;
import algae.alignment.Sequence;
import java.util.ArrayList;

/**
 *
 * @author sergio
 */
public class SequenceUtil {

    /**
     * Return a string sequence as an array of chars
     *
     * @param data input sequence (string)
     * @return array of chars for the same sequence
     */
    public static char[] getAsCharArray(String data) {
        char[] returnValue = null;

        if (data != null) {
            returnValue = new char[data.length()];
            for (int i = 0; i < data.length(); i++) {
                returnValue[i] = data.charAt(i);
            }
        }

        return returnValue;
    }

    public static Sequence getConsensusSequence(Matrix matrix, char[][] alignment) {

        int numberOfSequences = alignment.length;
        int alignmentSize = alignment[0].length;

        Aminoacids[] aminoacids = Aminoacids.values();
        int alphabetSize = aminoacids.length;

        char[] consensusSequence = new char[alignmentSize];

        for (int i = 0; i < alignmentSize; i++) {
            int maxScore = Integer.MIN_VALUE;
            //Anything but gaps
            for (int j = 0; j < alphabetSize - 1; j++) {
                int score = 0;
                int gapCount = 0;
                for (int k = 0; k < numberOfSequences; k++) {
                    if (alignment[k][i] == '-') {
                        gapCount++;
                    }
                    if (k == 0) {
                        score = MatrixUtil.getScore(matrix, j, alignment[k][i]);
                    } else {
                        score += MatrixUtil.getScore(matrix, j, alignment[k][i]);
                    }
                }
                if (gapCount == numberOfSequences) {
                    consensusSequence[i] = '-';
                } else {
                    if (score > maxScore) {
                        maxScore = score;
                        consensusSequence[i] = aminoacids[j].toString().charAt(0);
                    }
                }
            }
        }



        Sequence returnValue = new Sequence("consensus", String.copyValueOf(consensusSequence));

        return returnValue;
    }

    public static void removeEmptyColumns(ArrayList<ArrayList<Character>> alignment) {

        int i = 0;
        boolean allGaps = false;

        while (i < alignment.get(0).size()) {
            allGaps = true;
            for (int j = 0; j < alignment.size(); j++) {
                allGaps = allGaps && (alignment.get(j).get(i) == '-');
            }
            if (allGaps) {
                //Remove this column
                for (int j = 0; j < alignment.size(); j++) {
                    alignment.get(j).remove(i);
                }
            } else {
                //look next column
                i++;
            }
        }
    }

    public static void fillGapsAtEnd(ArrayList<ArrayList<Character>> childData) {
        int maxLength = 0;
        for (int i = 0; i < childData.size(); i++) {
            if (childData.get(i).size() > maxLength) {
                maxLength = childData.get(i).size();
            }
        }

        for (int i = 0; i < childData.size(); i++) {
            if (childData.get(i).size() < maxLength) {
               int numberOfGaps = maxLength-childData.get(i).size();
               for (int j = 0;j < numberOfGaps;j++){
                   childData.get(i).add('-');
                }
            }
        }
    }
}
