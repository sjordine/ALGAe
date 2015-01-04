/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mcbaligner.aligner;

import java.util.ArrayList;
import java.util.List;
import jbio.alignment.GlobalAlignment;
import jbio.alignment.GlobalAlignmentGapAffinity;
import jbio.alignment.Sequence;
import jbio.alignment.msa.AnchorAlignmentUtil;
import mcbaligner.conservedblocks.graph.pairbasedgraph.PairBasedGraphNode;
import mcbaligner.conservedblocks.graph.pairbasedgraph.PairBasedPathUtil;
import mcbaligner.environment.Environment;
import mcbaligner.guidingtree.BranchNode;
import mcbaligner.guidingtree.DistanceInfo;
import mcbaligner.guidingtree.LeafNode;
import mcbaligner.guidingtree.Node;

/**
 *
 * @author sergio
 */
public class TreeBasedAligner {

    public static List<List<Character>> align(Node node, Object[][] blockGraphs) throws Exception {


        List<List<Character>> alignment = new ArrayList<List<Character>>();

        if (node instanceof BranchNode) {
            BranchNode currentNode = (BranchNode) node;
            List<List<Character>> childAlignment1 = align(currentNode.getChild1(), blockGraphs);
            List<List<Character>> childAlignment2 = align(currentNode.getChild2(), blockGraphs);

            List<DistanceInfo> distances = currentNode.getChildDistances();
            DistanceInfo mainDistance = distances.get(0);
            int seq1Index = mainDistance.getSequence1();
            int seq2Index = mainDistance.getSequence2();


            alignment = BlockBasedAlignment(childAlignment1, childAlignment2, blockGraphs, seq1Index, seq2Index);

        } else {
            LeafNode sequenceNode = (LeafNode) node;
            for (int i = 0; i < blockGraphs.length; i++) {
                alignment.add(null);
            }
            Environment environment = Environment.getInstance();
            List<Sequence> sequences = environment.getSequences();
            int sequenceIndex = sequenceNode.getSequence1Index();

            Sequence currentSequence = sequences.get(sequenceIndex);
            char[] sequenceCharacters = currentSequence.getDataArray();
            List<Character> sequence = new ArrayList<Character>();
            for (int i = 0; i < sequenceCharacters.length; i++) {
                sequence.add(sequenceCharacters[i]);
            }


            alignment.set(sequenceIndex, sequence);

        }

        validateAlignment(alignment);

        return alignment;

    }

    private static List<List<Character>> BlockBasedAlignment(List<List<Character>> childAlignment1,
            List<List<Character>> childAlignment2, Object[][] blockGraphs,
            int seq1Index, int seq2Index) throws Exception {


        List<PairBasedGraphNode> nodes = (List<PairBasedGraphNode>) blockGraphs[seq1Index][seq2Index];
        List<PairBasedGraphNode> bestPath = PairBasedPathUtil.longestPath(nodes);

        List<List<Character>> anchorAlignment = new ArrayList<List<Character>>();
        anchorAlignment.add(new ArrayList<Character>());
        anchorAlignment.add(new ArrayList<Character>());




        //Align sequences based on pair based path
        PairBasedGraphNode currentNode = new PairBasedGraphNode();
        currentNode.setStartNode(true);


        Environment environment = Environment.getInstance();
        List<Sequence> sequences = environment.getSequences();
        Sequence sequence1 = sequences.get(seq1Index);
        Sequence sequence2 = sequences.get(seq2Index);

        int lastPosition1 = -1;
        int lastPosition2 = -1;

        for (int i = 0; i < bestPath.size(); i++) {

            
            PairBasedGraphNode nextNode = bestPath.get(i);

            int startPos1 = currentNode.isStartNode() ? 0 : currentNode.getSequence1End() + 1;
            int endPos1 = nextNode.getSequence1Start();
            int startPos2 = currentNode.isStartNode() ? 0 : currentNode.getSequence2End() + 1;
            int endPos2 = nextNode.getSequence2Start();


            if (startPos1 <= endPos1 && startPos2 <= endPos2) {

                String subSequence1Str = sequence1.getData().substring(startPos1, endPos1);
                String subSequence2Str = sequence2.getData().substring(startPos2, endPos2);


                Sequence subSequence1 = new Sequence("seq1", subSequence1Str);
                Sequence subSequence2 = new Sequence("seq2", subSequence2Str);

                //GlobalAlignment aligner = new GlobalAlignment(subSequence1, subSequence2, environment.getMatrixGlobal());
                GlobalAlignmentGapAffinity aligner = new GlobalAlignmentGapAffinity(subSequence1, subSequence2, environment.getMatrixGlobal());
                char[][] align = aligner.align();


                for (int j = 0; j < align[0].length; j++) {
                    anchorAlignment.get(0).add(align[0][j]);
                }
                for (int j = 0; j < align[1].length; j++) {
                    anchorAlignment.get(1).add(align[1][j]);
                }


            }


            //Add node to sequence
            if (nextNode.getSequence1Start() > lastPosition1) {
                for (int j = nextNode.getSequence1Start(); j <= nextNode.getSequence1End(); j++) {
                    anchorAlignment.get(0).add(sequence1.getDataArray()[j]);
                }
            } else {
                for (int j = lastPosition1 + 1; j <= nextNode.getSequence1End(); j++) {
                    anchorAlignment.get(0).add(sequence1.getDataArray()[j]);
                }
            }
            lastPosition1 = nextNode.getSequence1End();

            if (nextNode.getSequence2Start() > lastPosition2) {
                for (int j = nextNode.getSequence2Start(); j <= nextNode.getSequence2End(); j++) {
                    anchorAlignment.get(1).add(sequence2.getDataArray()[j]);
                }
            } else {
                for (int j = lastPosition2 + 1; j <= nextNode.getSequence2End(); j++) {
                    anchorAlignment.get(1).add(sequence2.getDataArray()[j]);
                }

            }

            lastPosition2 = nextNode.getSequence2End();


            currentNode = nextNode;
        }

        //align from last node to end of sequence
        //int startPos1 = currentNode.getSequence1End() + 1;
        //int endPos1 = sequence1.getData().length();
        //int startPos2 = currentNode.getSequence2End() + 1;
        //int endPos2 = sequence2.getData().length();

        int startPos1 = currentNode.isStartNode() ? 0 : currentNode.getSequence1End() + 1;
        int endPos1 = sequence1.getData().length();
        int startPos2 = currentNode.isStartNode() ? 0 : currentNode.getSequence2End() + 1;
        int endPos2 = sequence2.getData().length();

        String subSequence1Str = sequence1.getData().substring(startPos1, endPos1);
        String subSequence2Str = sequence2.getData().substring(startPos2, endPos2);


        Sequence subSequence1 = new Sequence("seq1", subSequence1Str);
        Sequence subSequence2 = new Sequence("seq2", subSequence2Str);

        GlobalAlignment aligner = new GlobalAlignment(subSequence1, subSequence2, environment.getMatrixGlobal());

        char[][] align = aligner.align();


        for (int j = 0; j < align[0].length; j++) {
            anchorAlignment.get(0).add(align[0][j]);
        }
        for (int j = 0; j < align[1].length; j++) {
            anchorAlignment.get(1).add(align[1][j]);
        }


        return AnchorAlignmentUtil.mergeAlignmentsByAnchor(childAlignment1,childAlignment2, anchorAlignment, seq1Index, seq2Index);

    }

    private static void validateAlignment(List<List<Character>> alignment) {
        for (int i=0; i < alignment.size(); i++){
            List<Character> currentSequence = alignment.get(i);
            if (currentSequence!=null){
                validateSequence(currentSequence,i);
            }
        }
    }

    private static void validateSequence(List<Character> currentSequence, int i) {
        Environment environment = Environment.getInstance();
        List<Sequence> sequences = environment.getSequences();
        Sequence sequence = sequences.get(i);

        char[] sequenceData = sequence.getDataArray();

        int cursor = 0;
        char currentChar;
        for (int j=0; j<sequenceData.length ;j++){

            currentChar = currentSequence.get(cursor);
            while (currentChar=='-' ){
                cursor++;
                if (cursor < currentSequence.size()){
                currentChar = currentSequence.get(cursor);
                } else {
                    break;
                }
            }

            if (cursor < currentSequence.size() &&  currentChar!=sequenceData[j]){

                System.out.println(currentSequence);
                System.out.println(sequenceData);

                throw new RuntimeException("Sequencia invalida "+i+"encontrado:"+currentChar+" esperado:"+sequenceData[j]+" em "+j);
            }
            cursor++;
            if (cursor >= currentSequence.size()) {
                break;
            }
        }


    }


}
