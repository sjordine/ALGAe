/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mcbaligner.conservedblocks.graph.pairbasedgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import jbio.alignment.Matrix;
import jbio.alignment.Sequence;
import jbio.util.alignment.MatrixUtil;
import mcbaligner.conservedblocks.ConservedBlockGroup;
import mcbaligner.conservedblocks.ConservedBlockUnit;
import mcbaligner.environment.Environment;


/**
 *
 * @author sergio
 */
public class TriageUtil {

    public static Object[][] mountGraphs(HashMap<String, ConservedBlockGroup> allBlocks, int sequenceCount){
        Object[][] result = null;

        result = triageBlocks(allBlocks, sequenceCount);

        for (int i=0; i<(sequenceCount-1); i++){
            for (int j=i+1; j < sequenceCount; j++){
                List<PairBasedGraphNode> nodes = (List<PairBasedGraphNode>)result[i][j];
                //Add start and end node
                PairBasedGraphNode startNode = new PairBasedGraphNode();
                startNode.setStartNode(true);
                for (int k=0; k < nodes.size(); k++){
                    startNode.addEdge(nodes.get(k));
                }
                PairBasedGraphNode endNode = new PairBasedGraphNode();
                endNode.setEndNode(true);
                for (int k=0; k < nodes.size(); k++){
                    nodes.get(k).addEdge(endNode);
                }
                startNode.addEdge(endNode);
                nodes.add(startNode);
                nodes.add(endNode);

                PairBasedGraphBuilder.mountEdges(nodes);

            }
        }

        return result;
    }


    public static Object[][] triageBlocks(HashMap<String, ConservedBlockGroup> allBlocks, int sequenceCount) {
        Object[][] result = new Object[sequenceCount][sequenceCount];

        for (int i = 0; i < sequenceCount; i++) {
            for (int j = 0; j < sequenceCount; j++) {
                result[i][j] = new ArrayList<PairBasedGraphNode>();
            }
        }

        Set<Entry<String, ConservedBlockGroup>> groupSet = allBlocks.entrySet();

        for (Entry<String, ConservedBlockGroup> groupBlockEntry : groupSet) {

            ConservedBlockGroup currentGroup = groupBlockEntry.getValue();
            List<List<ConservedBlockUnit>> triageList = currentGroup.getConservedBlocks();

            int score = 0;

            for (int i = 0; i < triageList.size(); i++) {

                //Score is bigger according to the number of sequences where the sub-sequence appears
                if (triageList.get(i) != null) {
                    score++;
                }

            }

            for (int i = 0; i < (triageList.size() - 1); i++) {
                for (int j = i + 1; j < triageList.size(); j++) {
                    List<ConservedBlockUnit> sequence1 = triageList.get(i);
                    List<ConservedBlockUnit> sequence2 = triageList.get(j);

                    createNodes((List<PairBasedGraphNode>) result[i][j], sequence1, sequence2, score, i, j);

                }
            }

        }
       
        return result;
    }

    private static void createNodes(List<PairBasedGraphNode> list, List<ConservedBlockUnit> sequence1, List<ConservedBlockUnit> sequence2, int score,
                                   int sequence1Index, int sequence2Index) {

        if (sequence1 != null && sequence2 != null && !sequence1.isEmpty() && !sequence2.isEmpty()) {
            for (int i = 0; i < sequence1.size(); i++) {
                for (int j = 0; j < sequence2.size(); j++) {
                    ConservedBlockUnit unit1 = sequence1.get(i);
                    ConservedBlockUnit unit2 = sequence2.get(j);

                    PairBasedGraphNode node = new PairBasedGraphNode();
                    node.setSequence1Start(unit1.getStartPosition());
                    node.setSequence1End(unit1.getEndPosition());
                    node.setSequence2Start(unit2.getStartPosition());
                    node.setSequence2End(unit2.getEndPosition());

                    //Consider a ratio associated with the score based on
                    //the similarity between both sequences (as a compressed alphabet can be used)
                    double factor = calculateFactor(sequence1Index, unit1, sequence2Index, unit2);


                    node.setScore(score*factor);

                    list.add(node);

                }
            }
        }




    }

    private static double calculateFactor(int sequence1Index, ConservedBlockUnit unit1, int sequence2Index, ConservedBlockUnit unit2) {

        double ratio = 0;

        Environment environment = Environment.getInstance();

        Matrix scoreMatrix = environment.getMatrixGlobal();
        List<Sequence> sequences = environment.getSequences();

        Sequence sequence1 = sequences.get(sequence1Index);
        Sequence sequence2 = sequences.get(sequence2Index);

        char[] seq1 = sequence1.getDataArray();
        char[] seq2 = sequence2.getDataArray();

        int start1 = unit1.getStartPosition();
        int end1 = unit1.getEndPosition();
        int start2 = unit2.getStartPosition();
        int end2 = unit2.getEndPosition();

        double max = 0;
        double score = 0;

        if ((end1-start1) == (end2-start2)){
            int size = end1-start1;

            for (int i=0; i < size; i++){
                char currChar1 = seq1[start1+i];
                char currChar2 = seq2[start2+i];

                max += MatrixUtil.getScore(scoreMatrix, currChar1, currChar1);
                score += MatrixUtil.getScore(scoreMatrix, currChar1, currChar2);

            }

            ratio = score/max;


        } else {
            throw new RuntimeException("Sequencias de tamanho distintas!!!!");
        }


        return ratio;
    }
}
