/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package craligner.msa;

import jbio.alignment.Sequence;

/**
 *
 * @author sergio
 */
public class AlignmentInfo {

    private int sequence1;
    private int sequence2;
    private char[][] alignment;
    private double score;

    /**
     * @return the sequence1
     */
    public int getSequence1() {
        return sequence1;
    }

    /**
     * @param sequence1 the sequence1 to set
     */
    public void setSequence1(int sequence1) {
        this.sequence1 = sequence1;
    }

    /**
     * @return the sequence2
     */
    public int getSequence2() {
        return sequence2;
    }

    /**
     * @param sequence2 the sequence2 to set
     */
    public void setSequence2(int sequence2) {
        this.sequence2 = sequence2;
    }

    /**
     * @return the alignment
     */
    public char[][] getAlignment() {
        return alignment;
    }

    /**
     * @param alignment the alignment to set
     */
    public void setAlignment(char[][] alignment) {
        this.alignment = alignment;
    }

    /**
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(double score) {
        this.score = score;
    }

}
