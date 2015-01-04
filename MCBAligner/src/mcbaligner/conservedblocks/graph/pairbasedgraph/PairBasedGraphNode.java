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
public class PairBasedGraphNode {

    private int sequence1Start;
    private int sequence1End;
    private int sequence2Start;
    private int sequence2End;
    private double score;
    private boolean visited;

    private List<PairBasedGraphNode> edges;

    private boolean startNode;
    private boolean endNode;
    private int nodeIndex;


    public PairBasedGraphNode(){

        edges = new ArrayList<PairBasedGraphNode>();

    }


    /**
     * @return the sequence1Start
     */
    public int getSequence1Start() {
        return sequence1Start;
    }

    /**
     * @param sequence1Start the sequence1Start to set
     */
    public void setSequence1Start(int sequence1Start) {
        this.sequence1Start = sequence1Start;
    }

    /**
     * @return the sequence1End
     */
    public int getSequence1End() {
        return sequence1End;
    }

    /**
     * @param sequence1End the sequence1End to set
     */
    public void setSequence1End(int sequence1End) {
        this.sequence1End = sequence1End;
    }

    /**
     * @return the sequence2Start
     */
    public int getSequence2Start() {
        return sequence2Start;
    }

    /**
     * @param sequence2Start the sequence2Start to set
     */
    public void setSequence2Start(int sequence2Start) {
        this.sequence2Start = sequence2Start;
    }

    /**
     * @return the sequence2End
     */
    public int getSequence2End() {
        return sequence2End;
    }

    /**
     * @param sequence2End the sequence2End to set
     */
    public void setSequence2End(int sequence2End) {
        this.sequence2End = sequence2End;
    }

    /**
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(double score) {
        this.score = score;
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


    public void print(){
         if (startNode){
             System.out.print("@");
        }
        if (endNode){
            System.out.print("*");
        }
        System.out.print("("+getSequence1Start()+","+getSequence2Start()+"->"+getScore()+")");
    }

    /**
     * @return the edges
     */
    public List<PairBasedGraphNode> getEdges() {
        return edges;
    }

    /**
     * @param edges the edges to set
     */
    public void setEdges(List<PairBasedGraphNode> edges) {
        this.edges = edges;
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

    boolean checkCompatibility(PairBasedGraphNode node) {

        boolean returnValue = false;

      //  return ((this.sequence1Start < node.sequence1Start) && (this.sequence2Start < node.sequence2Start));
      if ((this.sequence1Start < node.sequence1Start) && (this.sequence2Start < node.sequence2Start)) {
            if ((this.sequence1End >= node.sequence1Start) || (this.sequence2End >= node.sequence2Start)){
                 if ((this.sequence1End <= node.sequence1Start) && (this.sequence2End <= node.sequence2Start)){
                     returnValue = ((node.sequence1End - this.sequence1End)==(node.sequence2End - this.sequence2End));
                 }
            } else {
                returnValue = true;
            }
      } 

        return returnValue;

    }

    void addEdge(PairBasedGraphNode endPoint) {
        edges.add(endPoint);
    }

    public int edgeCount(){
        return edges.size();
    }


}
