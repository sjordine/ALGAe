/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.conservedblocks.graph;

import java.util.ArrayList;
import java.util.List;
import mcbaligner.conservedblocks.ConservedBlockUnit;

/**
 *
 * @author sergio
 */
public class GraphNode {
    private List<ConservedBlockUnit> units;
    private List<GraphNode> edges;
    private boolean visited;
    private boolean startNode;
    private boolean endNode;
    private int nodeIndex;

    public GraphNode(int sequenceNo){
        units = new ArrayList<ConservedBlockUnit>();
        edges = new ArrayList<GraphNode>();
        visited = false;
         for (int i=0; i<sequenceNo;i++){
            units.add(null);
        }
    }

    public void addUnit(int sequenceId, ConservedBlockUnit unit){
        units.set(sequenceId,unit);
    }

    public void addEdge(GraphNode node){
        edges.add(node);
    }

    public NodeRelation checkCompatibility(GraphNode node){

        NodeRelation compatible = NodeRelation.INDEPENDENT;

        //A node is compatible with another if there is
        //a valid connection between them. It means that
        //exists at least one common sequence between both nodes
        //and the common sequences are ordered

        for (int i=0; i < units.size(); i++){
            ConservedBlockUnit thisUnit = units.get(i);
            ConservedBlockUnit otherUnit = node.units.get(i);

            if (thisUnit != null && otherUnit != null){
                if (thisUnit.getStartPosition() < otherUnit.getStartPosition() ){
                    //If both units are compatible(or if no relation was previosuly found) it means that the nodes remains compatible
                    if (compatible == NodeRelation.COMPATIBLE || compatible == NodeRelation.INDEPENDENT){
                        compatible = NodeRelation.COMPATIBLE;
                    }
                    
                } else {
                    //The relation indicates the nodes are incompatible
                    compatible = NodeRelation.INCOMPATIBLE;
                }
            }
        }

        return compatible;

    }


    public void print(){
        if (startNode){
             System.out.print("@");
        }
        if (endNode){
            System.out.print("*");
        }
        System.out.print(""+nodeIndex+"{");
        for (int i=0; i< units.size();i++){
            ConservedBlockUnit unit = units.get(i);            
            if (unit!=null){
                System.out.print("("+unit.getStartPosition()+"-"+unit.getEndPosition()+"),");
            } else {
                System.out.print("(),");
            }
           
        }
        System.out.println("}");
    }
    
    public int edgeCount(){
        return edges.size();
    }

    /**
     * @return the visited
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * @param visited the visited to set
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * @return the startNode
     */
    public boolean isStartNode() {
        return startNode;
    }

    /**
     * @param startNode the startNode to set
     */
    public void setStartNode(boolean startNode) {
        this.startNode = startNode;
    }

    /**
     * @return the endNode
     */
    public boolean isEndNode() {
        return endNode;
    }

    /**
     * @param endNode the endNode to set
     */
    public void setEndNode(boolean endNode) {
        this.endNode = endNode;
    }

    List<GraphNode> getEdges() {
        return edges;
    }

    /**
     * @return the nodeIndex
     */
    public int getNodeIndex() {
        return nodeIndex;
    }

    /**
     * @param nodeIndex the nodeIndex to set
     */
    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }



}
