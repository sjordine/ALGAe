/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.philogeny.tree;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergio
 */
public class LeafNode extends TreeNode{

    private int sequenceIndex;

    public LeafNode(int sequenceIndex){
        this.sequenceIndex = sequenceIndex;
        this.child1 = null;
        this.child2 = null;
    }

    public List<Integer> getChildren(){
        List<Integer> returnValue = new ArrayList<Integer>();

        returnValue.add(getSequenceIndex());

        return returnValue;
    }

    @Override
    public int getChildrenNumber(){
        return 1;
    }

    /**
     * @return the sequenceIndex
     */
    public int getSequenceIndex() {
        return sequenceIndex;
    }

    

}
