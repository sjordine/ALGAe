/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.conservedblocks.graph.superblocks;

import java.util.ArrayList;
import java.util.List;

/**
 * A super-block represents a perfect alignment between multiple sequences on a certain sequence frame
 *
 * @author sergio
 */
public class SuperBlockNode {

    private List<UnitInfo> blockUnits;
    private List<SuperBlockNode> edges;

    public SuperBlockNode(int sequenceCount){
        blockUnits = new ArrayList<UnitInfo>();
        edges = new ArrayList<SuperBlockNode>();

        //Clean list of all possible sequences
        for (int i=0; i < sequenceCount; i++){
            blockUnits.add(null);
        }
    }

    public void addUnit(int sequence, int startPos, int endPos){
        UnitInfo unit= new UnitInfo();
        unit.setSequenceIndex(sequence);
        unit.setStartPosition(startPos);
        unit.setEndPosition(endPos);

        getBlockUnits().set(sequence,unit);
    }

    /**
     * @return the blockUnits
     */
    public List<UnitInfo> getBlockUnits() {
        return blockUnits;
    }

    boolean isCompatible(SuperBlockNode comparingBlock) {

        boolean returnValue = false;

        boolean isEqual = false;
        boolean isBigger = false;
        boolean isLesser = false;

        int blockSize = blockUnits.size();
        List<UnitInfo> comparedBlockUnits = comparingBlock.getBlockUnits();
        int comparingBlockSize = comparedBlockUnits.size();

        if (comparingBlockSize == blockSize){

            for (int i=0; i < blockUnits.size(); i++){
                UnitInfo currUnit = blockUnits.get(i);
                UnitInfo comparedUnit = comparedBlockUnits.get(i);

                if (currUnit!=null && comparedUnit!=null){
                    int currStart = currUnit.getStartPosition();
                    int comparedStart = comparedUnit.getStartPosition();

                    isEqual = isEqual || (currStart==comparedStart);
                    isBigger = isBigger || (currStart > comparedStart);
                    isLesser = isLesser || (currStart < comparedStart);
                }

            }

            //Just one of the three kinds of comparison must be true,
            //otherwise we have a conflict that makes the blocks incompatible.
            returnValue = (isEqual ^ isBigger) ^ isLesser;

        }



        return returnValue;
        
    }

    


}
