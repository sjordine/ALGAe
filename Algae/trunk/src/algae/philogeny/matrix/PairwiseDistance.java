/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.philogeny.matrix;

/**
 *
 * @author sergio
 */
public class PairwiseDistance {
    private char[][] alignment;
    private double score;

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

        this.alignment = alignment.clone();

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
