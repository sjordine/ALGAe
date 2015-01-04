/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.philogeny.tree;

import java.util.List;

/**
 *
 * @author sergio
 */
public abstract class TreeNode {
    
    protected TreeNode child1;
    protected TreeNode child2;
    protected TreeNode parent;
    private int nodeIndex;

    public abstract List<Integer> getChildren();

    /**
     * @return the child1
     */
    public TreeNode getChild1() {
        return child1;
    }

    /**
     * @param child1 the child1 to set
     */
    public void setChild1(TreeNode child1) {
        this.child1 = child1;        
    }

    /**
     * @return the child2
     */
    public TreeNode getChild2() {
        return child2;
    }

    /**
     * @param child2 the child2 to set
     */
    public void setChild2(TreeNode child2) {
        this.child2 = child2;
    }

    /**
     * @return the parent
     */
    public TreeNode getParent() {

        if (parent==null) {
            return this;
        } else {
            return parent.getParent();
        }
        
    }

    

    /**
     * @return the childrenNumber
     */
    public int getChildrenNumber() {
        int returnValue = 0;

        if (child1!=null ){
            returnValue+= child1.getChildrenNumber();
        }

        if (child2!=null ){
            returnValue+= child2.getChildrenNumber();
        }

        return returnValue;
    }

    /**
     * @return the nodeIndex
     */
    public int getNodeIndex() {
        return nodeIndex;
    }

    /**
     * @param nodeIndex the nodeIndex to set
     */
    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

        
    
}
