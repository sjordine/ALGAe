/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.philogeny.tree;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergio
 */
public class IntermediateNode extends TreeNode {

    public IntermediateNode(TreeNode node1, TreeNode node2){
        child1 = node1;
        child2 = node2;

        if (child1!=null){
            child1.parent=this;
        }

         if (child2!=null){
            child2.parent=this;
        }
    }

    @Override
    public List<Integer> getChildren() {
         List<Integer> returnValue = new ArrayList<Integer>();

        if (child1!=null){
            returnValue.addAll(child1.getChildren());
        }

         if (child2!=null){
            returnValue.addAll(child2.getChildren());
        }

        return returnValue;
    }

   

}
