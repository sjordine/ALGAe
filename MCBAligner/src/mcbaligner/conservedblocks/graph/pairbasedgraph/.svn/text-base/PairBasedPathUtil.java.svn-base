/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mcbaligner.conservedblocks.graph.pairbasedgraph;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergio
 */
public class PairBasedPathUtil {

    public static List<PairBasedGraphNode>  longestPath(List<PairBasedGraphNode> nodes) throws Exception {

        List<PairBasedGraphNode> returnValue = null;
        List<PairBasedGraphNode> topOrdered = topOrder(nodes);

        PairBasedGraphNode[] array = new PairBasedGraphNode[nodes.size()];

        double[] longestPath = new double[nodes.size()];
        int[] fromNode = new int[nodes.size()];


        for (int i = 0; i < nodes.size(); i++) {
            PairBasedGraphNode node = nodes.get(i);
            array[node.getNodeIndex()] = node;
        }

        for (int i = 0; i < array.length; i++) {
            for (PairBasedGraphNode end : array[i].getEdges()) {
                if (longestPath[end.getNodeIndex()]<=longestPath[array[i].getNodeIndex()] + array[i].getScore()){
                    longestPath[end.getNodeIndex()] = longestPath[array[i].getNodeIndex()] + array[i].getScore();
                    fromNode[end.getNodeIndex()] = i;
                }
            }
        }

        int i = nodes.size() - 1;

        returnValue = new ArrayList<PairBasedGraphNode>();


        while (i > 0) {
            PairBasedGraphNode node = array[fromNode[i]];
            if (!node.isStartNode() && !node.isEndNode()){
                returnValue.add(0,node);
            }
            i = fromNode[i];
        }


        return returnValue;

    }

    public static List<PairBasedGraphNode> topOrder(List<PairBasedGraphNode> nodes) {
        List<PairBasedGraphNode> returnValue = new ArrayList<PairBasedGraphNode>();

        PairBasedGraphNode startNode = null;
        //Find start Node
        for (int i = 0; i < nodes.size(); i++) {
            PairBasedGraphNode currNode = nodes.get(i);
            if (currNode != null && currNode.isStartNode()) {
                startNode = currNode;
            }
        }

        if (startNode != null) {
            visit(startNode, returnValue);
        } else {
            System.out.println("Ops! Cade o nó inicial????");
        }

        for (int i = (returnValue.size() - 1); i >= 0; i--) {
            returnValue.get(i).setNodeIndex((returnValue.size() - 1) - i);
        }

        return returnValue;
    }

    private static void visit(PairBasedGraphNode node, List<PairBasedGraphNode> returnValue) {
        if (!node.isVisited()) {
            node.setVisited(true);
            List<PairBasedGraphNode> connectedNodes = node.getEdges();
            for (PairBasedGraphNode n : connectedNodes) {
                visit(n, returnValue);
            }
            returnValue.add(node);
        }
    }
}
