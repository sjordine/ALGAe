/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.guidingtree;


import jbio.alignment.Sequence;
import mcbaligner.environment.Environment;

/**
 *
 * @author sergio
 */
public class LeafNode extends Node {



    public LeafNode(int sequence1){
        super(sequence1,-1);
    }


    @Override
    public void print() {
        Environment environment = Environment.getInstance();
        Sequence sequence = environment.getSequences().get(sequence1Index);
        System.out.print(sequence.getDescription());
    }

}
