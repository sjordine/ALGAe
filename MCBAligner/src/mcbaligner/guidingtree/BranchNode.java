/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.guidingtree;

/**
 *
 * @author sergio
 */
public class BranchNode extends Node{
    private Node child1 = null;
    private Node child2 = null;

    public BranchNode(int sequence1, int sequence2, Node child1, Node child2) {
        super(sequence1, sequence2);
        this.child1 = child1;
        this.child2 = child2;
        
    }

    @Override
    public void print() {

        System.out.print("(");
        getChild1().print();
        System.out.print(" , ");
        getChild2().print();
        System.out.print(")");
    }

    /**
     * @return the child1
     */
    public Node getChild1() {
        return child1;
    }

    /**
     * @return the child2
     */
    public Node getChild2() {
        return child2;
    }

}
