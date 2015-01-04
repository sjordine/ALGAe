/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.conservedblocks.graph.pairbasedgraph;

import java.util.List;

/**
 *
 * @author sergio
 */
public class PairBasedGraphBuilder {

     public static void mountEdges(List<PairBasedGraphNode> nodes) {

        for (int i = 0; i < (nodes.size() - 1); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                PairBasedGraphNode node1 = nodes.get(i);
                PairBasedGraphNode node2 = nodes.get(j);

                if (!node1.isStartNode() && !node1.isEndNode() && !node2.isStartNode() && !node2.isEndNode()) {

                    if (node1.checkCompatibility(node2)) {
                        node1.addEdge(node2);
                    } else {
                        if (node2.checkCompatibility(node1)) {
                            node2.addEdge(node1);
                        }
                    }
                }
            }
        }

    }

}
