/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package craligner.tree;

import java.util.List;

/**
 *
 * @author sergio
 */
public abstract class Node {

    protected Node parentNode = null;

    protected int sequence1Index = -1;
    protected int sequence2Index = -1;

    public Node(int sequence1, int sequence2){
        this.sequence1Index = sequence1;
        this.sequence2Index = sequence2;
        this.parentNode = this;
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

    public void setParent(Node parentNode){
        this.parentNode = parentNode;
    }

    public abstract List<char[]> getAlignment();

    public abstract void print();

}
