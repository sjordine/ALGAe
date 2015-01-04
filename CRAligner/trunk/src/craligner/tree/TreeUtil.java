/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package craligner.tree;

import craligner.environment.Environment;
import craligner.msa.AlignmentInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import jbio.alignment.Sequence;

/**
 *
 * @author sergio
 */
public class TreeUtil {

    public static Node createDistanceTree(){
        Node root = null;
        HashMap<Integer,Node> nodeHash = new HashMap<Integer, Node>();
        List<AlignmentInfo> alignments = new ArrayList<AlignmentInfo>();

        Environment environment = Environment.getInstance();
        List<Sequence> sequences = environment.getSequences();
        int sequenceCount = sequences.size();

        for (int i=0;i<sequenceCount;i++){
            for (int j=i+1;j<sequenceCount;j++){
                alignments.add(environment.getDistanceInfo(i, j));
            }
        }

        Collections.sort(alignments, new AlignmentInfoComparator());

        for (int i=0;i<alignments.size();i++){
            AlignmentInfo currentAlignment = alignments.get(i);
            
            Node node1 = null;
            Node node2 = null;

            int sequence1Index = currentAlignment.getSequence1();
            int sequence2Index = currentAlignment.getSequence2();

            if (nodeHash.containsKey(sequence1Index)){
                node1 = nodeHash.get(sequence1Index);
            } else {
                node1 = new LeafNode(sequence1Index);
                nodeHash.put(sequence1Index,node1);
            }

            if (nodeHash.containsKey(sequence2Index)){
                node2 = nodeHash.get(sequence2Index);
            } else {
                node2 = new LeafNode(sequence2Index);
                nodeHash.put(sequence2Index,node2);
            }
            
            //Add parent node if both are children 
            //are not connected yet
            if (node1.getParent()!=node2.getParent()){

                Node parentNode = new BranchNode(sequence1Index, sequence2Index, node1.getParent(), node2.getParent());

                node1.getParent().setParent(parentNode);
                node2.getParent().setParent(parentNode);


            }

        }

        root = nodeHash.get(0).getParent();

        return root;
    }

}
