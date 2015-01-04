/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mcbaligner.guidingtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import jbio.alignment.Sequence;
import mcbaligner.conservedblocks.graph.pairbasedgraph.PairBasedGraphNode;


/**
 *
 * @author sergio
 */
public class TreeUtil {

    public static Node mountTree(Object[][] graphs, List<Sequence> sequences) {

        Node root = null;
        HashMap<Integer, Node> nodeHash = new HashMap<Integer, Node>();

        ArrayList<DistanceInfo> distances = new ArrayList<DistanceInfo>();

        for (int i = 0; i < sequences.size(); i++) {
            for (int j = 0; j < sequences.size(); j++) {

                List<PairBasedGraphNode> nodes = (List<PairBasedGraphNode>) graphs[i][j];
                DistanceInfo distance = new DistanceInfo(i, j, nodes.size());

                distances.add(distance);

            }
        }

        Collections.sort(distances, new DistanceInfoComparator());


        for (int i = 0; i < distances.size(); i++) {
            DistanceInfo currentAlignment = distances.get(i);

            Node node1 = null;
            Node node2 = null;

            int sequence1Index = currentAlignment.getSequence1();
            int sequence2Index = currentAlignment.getSequence2();

            if (nodeHash.containsKey(sequence1Index)) {
                node1 = nodeHash.get(sequence1Index);
            } else {
                node1 = new LeafNode(sequence1Index);
                nodeHash.put(sequence1Index, node1);
            }

            if (nodeHash.containsKey(sequence2Index)) {
                node2 = nodeHash.get(sequence2Index);
            } else {
                node2 = new LeafNode(sequence2Index);
                nodeHash.put(sequence2Index, node2);
            }

            //Add parent node if both are children
            //are not connected yet
            if (node1.getParent() != node2.getParent()) {

                Node parentNode = new BranchNode(sequence1Index, sequence2Index, node1.getParent(), node2.getParent());

                node1.getParent().setParent(parentNode);
                node2.getParent().setParent(parentNode);

                parentNode.addChildDistance(currentAlignment);

            } else {
                //Just keep track of all distances in proper order
                node1.getParent().addChildDistance(currentAlignment);
            }

        }

        root = nodeHash.get(0).getParent();


        root.print();

        System.out.println();

        return root;
    }
}
