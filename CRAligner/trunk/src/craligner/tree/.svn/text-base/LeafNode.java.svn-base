/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package craligner.tree;

import craligner.environment.Environment;
import java.util.ArrayList;
import java.util.List;
import jbio.alignment.Sequence;

/**
 *
 * @author sergio
 */
public class LeafNode extends Node {





    public LeafNode(int sequence1){
        super(sequence1,-1);
    }
   
    public List<char[]> getAlignment() {



        List<char[]> returnValue = null;

        Environment environment = Environment.getInstance();
        List<Sequence> sequences = environment.getSequences();

        if (sequences!=null && sequence1Index < sequences.size()){
            returnValue = new ArrayList<char[]>();
            for (int i=0;i<sequences.size();i++){
                if (i!=sequence1Index){
                    returnValue.add(null);
                } else {
                    returnValue.add(sequences.get(i).getDataArray().clone());
                }
            }
        }




        return returnValue;
    }

    @Override
    public void print() {
        Environment environment = Environment.getInstance();
        Sequence sequence = environment.getSequences().get(sequence1Index);
        System.out.print(sequence.getDescription());
    }


}
