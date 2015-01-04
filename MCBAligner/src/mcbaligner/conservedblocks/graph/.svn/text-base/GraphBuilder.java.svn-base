/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mcbaligner.conservedblocks.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import jbio.alignment.Sequence;
import mcbaligner.conservedblocks.ConservedBlockGroup;
import mcbaligner.conservedblocks.ConservedBlockUnit;
import mcbaligner.conservedblocks.MSConservedBlockFinder;

/**
 *
 * @author sergio
 */
public class GraphBuilder {


    public static List<GraphNode> mountVertexes(List<Sequence> sequences) {
        List<GraphNode> returnValue = new ArrayList<GraphNode>();
        
        HashMap<String, ConservedBlockGroup> groups = MSConservedBlockFinder.findBlocks(sequences);

        Set<Entry<String, ConservedBlockGroup>> groupSet = groups.entrySet();

        for (Entry<String, ConservedBlockGroup> groupBlockEntry : groupSet) {

            ConservedBlockGroup groupBlock = groupBlockEntry.getValue();

            // System.out.println("Creating Nodes for "+groupBlockEntry.getKey()+" with "+ groupBlock.numberOfSequences());

            if (groupBlock.numberOfSequences() > 1) {
                List<GraphNode> blockNodes = mountBlockNodes(groupBlock, sequences.size());
                if (blockNodes != null && blockNodes.size() > 0) {
                    returnValue.addAll(blockNodes);
                }
            }

        }

        //Create Start Node
        GraphNode startNode = new GraphNode(sequences.size());
        startNode.setStartNode(true);
        for (int i=0; i < returnValue.size(); i++){
            startNode.addEdge(returnValue.get(i));
        }

        //Create Finish Node
        GraphNode endNode = new GraphNode(sequences.size());
        endNode.setEndNode(true);
        for (int i=0; i < returnValue.size(); i++){
            returnValue.get(i).addEdge(endNode);
        }

        returnValue.add(startNode);
        returnValue.add(endNode);

        return returnValue;
    }

    public static void mountEdges(List<GraphNode> nodes) {

        for (int i = 0; i < (nodes.size() - 1); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                GraphNode node1 = nodes.get(i);
                GraphNode node2 = nodes.get(j);

                if (!node1.isStartNode() && !node1.isEndNode() && !node2.isStartNode() && !node2.isEndNode()) {

                    if (node1.checkCompatibility(node2)==NodeRelation.COMPATIBLE) {
                        node1.addEdge(node2);
                    } else {
                        if (node2.checkCompatibility(node1)==NodeRelation.COMPATIBLE) {
                            node2.addEdge(node1);
                        }
                    }
                }
            }
        }


        int count = 0;
        for (int i = 0; i < nodes.size(); i++) {
            count += nodes.get(i).edgeCount();
        }

        System.out.println("Edges = " + count);


    }

    private static List<GraphNode> mountBlockNodes(ConservedBlockGroup groupBlock, int sequenceCount) {
        List<GraphNode> returnValue = new ArrayList<GraphNode>();

        List<List<ConservedBlockUnit>> units = groupBlock.getConservedBlocks();

        List<List<Integer>> indexes = getIndexes(0, null, units, sequenceCount);



        //Create Graph Nodes
        for (int i = 0; i < indexes.size(); i++) {


            GraphNode node = new GraphNode(sequenceCount);
            List<Integer> currentNodeInfo = indexes.get(i);
            for (int j = 0; j < currentNodeInfo.size(); j++) {
                int index = currentNodeInfo.get(j);
                if (index != -1) {
                    node.addUnit(j, units.get(j).get(index));
                }
            }

            returnValue.add(node);
        }


        return returnValue;
    }

    private static List<List<Integer>> getIndexes(int index, List<List<Integer>> previousNodes, List<List<ConservedBlockUnit>> units, int sequenceCount) {
        List<List<Integer>> expandedList = new ArrayList<List<Integer>>();

        if (index < sequenceCount) {

            List<ConservedBlockUnit> currentUnits = units.get(index);


            if (previousNodes == null) {
                //Mount first list
                if (currentUnits == null || currentUnits.size() < 1) {
                    return getIndexes(index + 1, null, units, sequenceCount);
                } else {
                    for (int j = 0; j < currentUnits.size(); j++) {
                        ArrayList<Integer> nodeIndexes = new ArrayList<Integer>();
                        for (int i = 0; i < index; i++) {
                            nodeIndexes.add(-1);
                        }
                        nodeIndexes.add(j);
                        expandedList.add(nodeIndexes);
                    }
                    return getIndexes(index + 1, expandedList, units, sequenceCount);
                }

            } else {



                if (currentUnits == null || currentUnits.size() < 1) {

                    for (int i = 0; i < previousNodes.size(); i++) {
                        ArrayList<Integer> nodeIndexes = (ArrayList<Integer>) previousNodes.get(i);
                        List<Integer> nodeIndexesClone = (List<Integer>) nodeIndexes.clone();
                        nodeIndexesClone.add(-1);
                        expandedList.add(nodeIndexesClone);
                    }
                } else {
                    for (int i = 0; i < previousNodes.size(); i++) {
                        for (int j = 0; j < currentUnits.size(); j++) {
                            ArrayList<Integer> nodeIndexes = (ArrayList<Integer>) previousNodes.get(i);
                            List<Integer> nodeIndexesClone = (List<Integer>) nodeIndexes.clone();
                            nodeIndexesClone.add(j);
                            expandedList.add(nodeIndexesClone);
                        }
                    }
                }


                return getIndexes(index + 1, expandedList, units, sequenceCount);
            }
        } else {
            return previousNodes;
        }
    }
}
