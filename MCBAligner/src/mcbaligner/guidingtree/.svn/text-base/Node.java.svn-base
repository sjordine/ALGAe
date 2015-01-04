/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.guidingtree;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergio
 */
public abstract class Node {


    protected Node parentNode = null;

    protected int sequence1Index = -1;
    protected int sequence2Index = -1;


    protected List<DistanceInfo> childDistances;

    public Node(int sequence1, int sequence2){
        this.sequence1Index = sequence1;
        this.sequence2Index = sequence2;
        this.parentNode = this;

        childDistances = new ArrayList<DistanceInfo>();
    }

    public int getSequence1Index() {
        return sequence1Index;
    }

    public int getSequence2Index() {
         return sequence2Index;
    }

    public Node getParent(){
        Node returnValue = null;
        if (parentNode!=this){
         returnValue = parentNode.getParent();
        } else {
         returnValue = this;
        }
        return returnValue;
    }

    public void addChildDistance(DistanceInfo distance){
        getChildDistances().add(distance);
    }

    


    public void setParent(Node parentNode){
        this.parentNode = parentNode;
    }

    public abstract void print();

    /**
     * @return the childDistances
     */
    public List<DistanceInfo> getChildDistances() {
        return childDistances;
    }


}
