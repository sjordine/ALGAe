/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jbio.alignment.msa;

/**
 *
 * @author sergio
 */
public class ScorePositionInfo {

   
    public enum DIRECTION{
        N,
        E,
        NE
    }


    private DIRECTION direction;
    private double score;

     /**
     * @return the direction
     */
    public DIRECTION getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(DIRECTION direction) {
        this.direction = direction;
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
