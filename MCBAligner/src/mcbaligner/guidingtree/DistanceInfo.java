/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.guidingtree;

/**
 *
 * @author sergio
 */
public class DistanceInfo {

    private int sequence1;
    private int sequence2;
    private int score;

    public DistanceInfo(int sequence1,int sequence2, int score){
        this.sequence1 = sequence1;
        this.sequence2 = sequence2;
        this.score = score;
    }

    /**
     * @return the sequence1
     */
    public int getSequence1() {
        return sequence1;
    }

    /**
     * @return the sequence2
     */
    public int getSequence2() {
        return sequence2;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

}
