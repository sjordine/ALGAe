/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.util.tree;

import algae.environment.Environment;
import algae.philogeny.tree.LeafNode;
import algae.philogeny.tree.TreeNode;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author sergio
 */
public class TreeUtil {


    public static int getTreeSize(TreeNode root){
        int returnValue = 0;

        if (root!=null){
            returnValue = 1;
            returnValue+=TreeUtil.getTreeSize(root.getChild1());
            returnValue+=TreeUtil.getTreeSize(root.getChild2());
        } 

        return returnValue;
    }

    public static List<Integer> getSequencesFromNode(int nodeNumber){
        List<Integer> returnValue = new ArrayList<Integer>();

        Environment environment = Environment.getInstance();
        TreeNode root = environment.getTree();

        //Get node by its index
        TreeNode node = getNode(root,nodeNumber);

        mountList(returnValue,node);


        return returnValue;
    }

    public static List<Integer> getComplementarySequencesFromNode(int nodeNumber){

        List<Integer> returnValue = new ArrayList<Integer>();
        List<Integer> baseList = new ArrayList<Integer>();

        Environment environment = Environment.getInstance();
        TreeNode root = environment.getTree();

        //Get node by its index
        TreeNode node = getNode(root,nodeNumber);

        mountList(returnValue,root);
        mountList(baseList,root);

        returnValue.removeAll(baseList);

        return returnValue;
    }

    private static TreeNode getNode(TreeNode node, int targetNodeIndex) {
        TreeNode returnValue = null;
        if (node.getNodeIndex() == targetNodeIndex){
            returnValue = node;
        } else {
            returnValue = node;
            int leftIndex=-1;
            int rightIndex=-1;
            if (node.getChild1()!=null){
                leftIndex = node.getChild1().getNodeIndex();
            }
            if (node.getChild2()!=null){
                rightIndex = node.getChild2().getNodeIndex();
            }
            //There is a left childNode and target node index
            //is between its index and the right child index (or there is no right child)
            if (leftIndex!=-1 && targetNodeIndex >= leftIndex &&
                    (rightIndex==-1 || targetNodeIndex < rightIndex)){
                returnValue = getNode(node.getChild1(),targetNodeIndex);
            }
            //There is a right child node and target node index is
            //bigger or equals to its index
            if (rightIndex!=-1 && targetNodeIndex >=rightIndex){
                returnValue = getNode(node.getChild2(),targetNodeIndex);
            }
        }

        return returnValue;
    }

    private static void mountList(List<Integer> sequenceList, TreeNode node) {
        if (node instanceof LeafNode){
            LeafNode seqNode = (LeafNode) node;
            sequenceList.add(seqNode.getSequenceIndex());
        } else {
            if (node.getChild1()!=null){
                mountList(sequenceList, node.getChild1());
            }
            if (node.getChild2()!=null){
                mountList(sequenceList, node.getChild2());
            }
        }
    }


}
