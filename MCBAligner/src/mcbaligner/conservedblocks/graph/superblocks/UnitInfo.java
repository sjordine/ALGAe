/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.conservedblocks.graph.superblocks;

/**
 * This class represents the sequence unit within a super-block
 *
 *
 * @author sergio
 */
public class UnitInfo {
    private int sequenceIndex;
    private int startPosition;
    private int endPosition;

    /**
     * @return the sequenceIndex
     */
    public int getSequenceIndex() {
        return sequenceIndex;
    }

    /**
     * @param sequenceIndex the sequenceIndex to set
     */
    public void setSequenceIndex(int sequenceIndex) {
        this.sequenceIndex = sequenceIndex;
    }

    /**
     * @return the startPosition
     */
    public int getStartPosition() {
        return startPosition;
    }

    /**
     * @param startPosition the startPosition to set
     */
    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    /**
     * @return the endPosition
     */
    public int getEndPosition() {
        return endPosition;
    }

    /**
     * @param endPosition the endPosition to set
     */
    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }
}
