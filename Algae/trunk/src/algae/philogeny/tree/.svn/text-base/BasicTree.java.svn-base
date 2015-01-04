/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.philogeny.tree;

import algae.philogeny.matrix.DistanceInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author sergio
 */
public class BasicTree {

    public static TreeNode mountTree(DistanceInfo[][] distanceMatrix) {
        TreeNode rootNode = null;

        //Matrix linearization
        int length = distanceMatrix.length;

        List<PairwiseDistanceInfo> distanceArray = new ArrayList();

        int count = 0;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (i < j) {
                    PairwiseDistanceInfo info = new PairwiseDistanceInfo();
                    info.setSequence1(i);
                    info.setSequence2(j);
                    info.setScore(distanceMatrix[i][j].getMaxScore());
                    distanceArray.add(info);
                }
            }
        }

        //Sort distances
        Collections.sort(distanceArray,
                new Comparator<PairwiseDistanceInfo>() {

                    public int compare(PairwiseDistanceInfo p1, PairwiseDistanceInfo p2) {
                        if (p1 == null && p2 == null) {
                            return 0;
                        } else {
                            if (p1 == null && p2 != null) {
                                return -1;
                            }

                            if (p1 != null && p2 == null) {
                                return 1;
                            }

                            Double score1 = p1.getScore();
                            Double score2 = p2.getScore();
                            return -1*score1.compareTo(score2);

                        }

                    }
                });
         
         System.out.println();

         //Mount tree
         HashMap<Integer,TreeNode> processedNodes =new HashMap<Integer,TreeNode>();
         int currentPosition = 0;
         PairwiseDistanceInfo distanceInfo = null;
         TreeNode child1 = null;
         TreeNode child2 = null;
         while (rootNode==null || rootNode.getChildrenNumber()<length){
             
             distanceInfo = distanceArray.get(currentPosition);
             //Retrieve nodes
             child1 = getNode(processedNodes,distanceInfo.getSequence1());
             child2 = getNode(processedNodes,distanceInfo.getSequence2());
             //If this nodes are not yet connected, create a node to connect
             //both subtrees
             if (child1.getParent()!=child2.getParent()){
                 rootNode = new IntermediateNode(child1.getParent(), child2.getParent());
             }
             currentPosition++;
         }

         //Set node indexes
         setNodeIndexes(rootNode,0);

        return rootNode;
    }

    private static TreeNode getNode(HashMap<Integer, TreeNode> processedNodes, int seqIndex) {
            TreeNode returnValue = null;

             if (processedNodes.containsKey(seqIndex)){
                 returnValue = processedNodes.get(seqIndex);
             } else {
                returnValue = new LeafNode(seqIndex);
                processedNodes.put(seqIndex, returnValue);
             }

            return returnValue;
    }

    private static int setNodeIndexes(TreeNode currentNode, int i) {
        int maxNode = i;

        currentNode.setNodeIndex(i);

        if (currentNode.getChild1()!=null){
            maxNode = setNodeIndexes(currentNode.getChild1(),maxNode+1);
        }

        if (currentNode.getChild2()!=null){
            maxNode = setNodeIndexes(currentNode.getChild2(),maxNode+1);
        }


        return maxNode;
    }
}
