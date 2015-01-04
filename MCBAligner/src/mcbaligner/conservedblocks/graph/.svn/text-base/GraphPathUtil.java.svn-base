/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.conservedblocks.graph;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergio
 */
public class GraphPathUtil {

    public static void longestPath(List<GraphNode> nodes){
        List<GraphNode> topOrdered = topOrder(nodes);

        int[] longestPath = new int[nodes.size()];

        for (int i=(nodes.size()-1); i >= 0; i--){
            for (GraphNode end: nodes.get(i).getEdges()){
                if (longestPath[end.getNodeIndex()] <= longestPath[nodes.get(i).getNodeIndex()]+1){
                    longestPath[end.getNodeIndex()] = longestPath[nodes.get(i).getNodeIndex()]+1;
                }
            }
        }

        for (int i=0; i< longestPath.length; i++){
            System.out.println(longestPath[i]);
        }


    }

    public static List<GraphNode> topOrder(List<GraphNode> nodes){
        List<GraphNode> returnValue = new ArrayList<GraphNode>();

        GraphNode startNode = null;
        //Find start Node
        for (int i=0; i < nodes.size(); i++){
            GraphNode currNode = nodes.get(i);
            if (currNode!=null && currNode.isStartNode()){
                startNode = currNode;
            }
        }

        if (startNode!=null){
            visit(startNode,returnValue);
        } else {
          System.err.println("Ops! Cade o nó inicial????");
        }

        for (int i=(returnValue.size()-1); i>=0;i--){
            returnValue.get(i).setNodeIndex((returnValue.size()-1)-i);
        }

        return returnValue;
    }

    private static void visit(GraphNode node, List<GraphNode> returnValue) {
        if (!node.isVisited()){
            node.setVisited(true);
            List<GraphNode> connectedNodes = node.getEdges();
            for (GraphNode n : connectedNodes){
                visit(n,returnValue);
            }
            returnValue.add(node);
        }
    }

}
