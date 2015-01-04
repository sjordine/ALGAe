/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.aligner;

import java.util.ArrayList;
import java.util.List;
import jbio.alignment.Matrix;
import jbio.alignment.Sequence;
import jbio.alignment.msa.ProfileGlobalAlignment;
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
public class TreeBasedProfileAligner {

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

            System.err.println("Sequences "+seq1Index+","+seq2Index);

            alignment = BlockBasedAlignment(childAlignment1, childAlignment2, blockGraphs, seq1Index, seq2Index);

        } else {
            LeafNode sequenceNode = (LeafNode) node;
            for (int i = 0; i < blockGraphs.length; i++) {
                alignment.add(null);
            }
            Environment environment = Environment.getInstance();
            List<Sequence> sequences = environment.getSequences();
            int sequenceIndex = sequenceNode.getSequence1Index();

            
            //System.err.println("Sequence "+sequenceIndex);

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

        List<List<Character>> returnValue = new ArrayList<List<Character>>();
        int numberOfSequences = childAlignment1.size();

        int subAlignment1Size = 0;
        int subAlignment2Size = 0;

        //Initialization
         for (int i = 0; i < numberOfSequences; i++) {
            if (childAlignment1.get(i) != null || childAlignment2.get(i) != null) {
                if (childAlignment1.get(i)!=null){
                    subAlignment1Size = childAlignment1.get(i).size();
                }
                if (childAlignment2.get(i)!=null){
                    subAlignment2Size = childAlignment2.get(i).size();
                }

                returnValue.add(new ArrayList<Character>());
            } else {
                returnValue.add(null);
            }
        }


     
        Environment environment = Environment.getInstance();
        Matrix scoreMatrix = environment.getMatrixGlobal();


        PairBasedGraphNode currentNode = new PairBasedGraphNode();
        currentNode.setStartNode(true);


        int lastPosition1 = -1;
        int lastPosition2 = -1;

        List<Integer> sequence1Indexes = new ArrayList<Integer>();
        List<Integer> sequence2Indexes = new ArrayList<Integer>();

        generateSequenceIndexes(sequence1Indexes,childAlignment1, seq1Index);
        generateSequenceIndexes(sequence2Indexes,childAlignment2, seq2Index);


        //Align sequences based on pair based path
        //Navigate through path
        for (int i = 0; i < bestPath.size(); i++) {
            PairBasedGraphNode nextNode = bestPath.get(i);

            int startPos1 = currentNode.isStartNode() ? 0 : currentNode.getSequence1End();
            int endPos1 = nextNode.getSequence1Start();
            int startPos2 = currentNode.isStartNode() ? 0 : currentNode.getSequence2End();
            int endPos2 = nextNode.getSequence2Start();


           if (startPos1 <= endPos1 && startPos2 <= endPos2) {
               //Align subsequences
               int actualStartPos1 = currentNode.isStartNode() ? 0: sequence1Indexes.get(startPos1)+1;
               int actualEndPos1 = sequence1Indexes.get(endPos1)-1;
               int actualStartPos2 = currentNode.isStartNode() ? 0: sequence2Indexes.get(startPos2)+1;
               int actualEndPos2 = sequence2Indexes.get(endPos2)-1;


                List<List<Character>> alignment = ProfileGlobalAlignment.align(scoreMatrix, 
                                                                               childAlignment1, actualStartPos1, actualEndPos1,
                                                                               childAlignment2, actualStartPos2, actualEndPos2);

                //Copy sub-alignment to final alignment
                for (int j=0; j< alignment.size(); j++){
                    List<Character> currSequence = alignment.get(j);
                    if (currSequence!=null){
                        for (int k=0; k <currSequence.size(); k++){
                            char currChar = currSequence.get(k);
                            returnValue.get(j).add(currChar);
                        }
                    }
                }

                /*
                 for (int j=0; j< alignment.size(); j++){
                    List<Character> currSequence = alignment.get(j);
                    if (currSequence!=null){                          
                            returnValue.get(j).add('@');
                       
                    }
                }
                 * *
                 */

                /*
                if (returnValue.get(10)!=null){
                    List<Character> temp = returnValue.get(10);
                    for (int k=0; k < temp.size(); k++){
                        if (temp.get(k)!='-'){
                            System.err.print(temp.get(k));
                        }
                    }
                    System.err.println();
                }*/

           }

           startPos1 = nextNode.getSequence1Start();
           endPos1 = nextNode.getSequence1End();
           startPos2 = nextNode.getSequence2Start();
           endPos2 = nextNode.getSequence2End();


           int actualNodeStartPos1 = (nextNode.getSequence1Start() > lastPosition1)?sequence1Indexes.get(startPos1):sequence1Indexes.get(lastPosition1)+1;
           int actualNodeEndPos1 = sequence1Indexes.get(endPos1);
           int actualNodeStartPos2 = (nextNode.getSequence2Start() > lastPosition2)?sequence2Indexes.get(startPos2):sequence2Indexes.get(lastPosition2)+1;
           int actualNodeEndPos2 = sequence2Indexes.get(endPos2);
          
           //Add node to sequence
           addNodeToAlignment(returnValue,
                   childAlignment1,seq1Index, actualNodeStartPos1,actualNodeEndPos1,
                   childAlignment2,seq2Index, actualNodeStartPos2,actualNodeEndPos2);

           lastPosition1 = nextNode.getSequence1End();
           lastPosition2 = nextNode.getSequence2End();

           /*
            if (returnValue.get(10)!=null){
                    List<Character> temp = returnValue.get(10);
                    for (int k=0; k < temp.size(); k++){
                        if (temp.get(k)!='-'){
                            System.err.print(temp.get(k));
                        }
                    }
                    System.err.println();
                }*/

           currentNode = nextNode;

        }

         //align from last node to end of sequence
        int startPos1 = currentNode.isStartNode() ? 0 : currentNode.getSequence1End();
        int startPos2 = currentNode.isStartNode() ? 0 : currentNode.getSequence2End();
       
        int actualStartPos1 = currentNode.isStartNode() ? 0: sequence1Indexes.get(startPos1)+1;
        int actualEndPos1 = subAlignment1Size - 1 ;
        int actualStartPos2 = currentNode.isStartNode() ? 0: sequence2Indexes.get(startPos2)+1;
        int actualEndPos2 = subAlignment2Size -1;
        
        List<List<Character>> alignment = ProfileGlobalAlignment.align(scoreMatrix,
                                                                       childAlignment1, actualStartPos1, actualEndPos1,
                                                                       childAlignment2, actualStartPos2, actualEndPos2);

                //Copy sub-alignment to final alignment
                for (int j=0; j< alignment.size(); j++){
                    List<Character> currSequence = alignment.get(j);
                    if (currSequence!=null){
                        for (int k=0; k <currSequence.size(); k++){
                            char currChar = currSequence.get(k);
                            returnValue.get(j).add(currChar);
                        }
                    }
                }


        return returnValue;

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

                for (int k=0; k < currentSequence.size();k++){
                    if (currentSequence.get(k)!='-'){
                        System.err.print(currentSequence.get(k));
                    }
                }
                System.err.println();
                //System.err.println(currentSequence);
                System.err.println(sequenceData);

                throw new RuntimeException("Sequencia invalida "+i+"encontrado:"+currentChar+" esperado:"+sequenceData[j]+" em "+j);
            }
            cursor++;
            if (cursor >= currentSequence.size()) {
                break;
            }
        }


    }

    private static void generateSequenceIndexes(List<Integer> sequenceIndexes, List<List<Character>> alignment, int seqIndex) {


        Environment environment = Environment.getInstance();
        List<Sequence> sequences = environment.getSequences();
        Sequence sequenceInfo = sequences.get(seqIndex);
        char[] sequence = sequenceInfo.getDataArray();


        List<Character> gapSequence = alignment.get(seqIndex);

        //System.err.println(gapSequence);
        //System.err.println(sequence);

        int baseSize = sequence.length;
        int alignmentSize = gapSequence.size();

        int baseCursor=0;
        int cursor = 0;

        while (cursor < alignmentSize){
            char baseChar = '\0';
            char currChar = gapSequence.get(cursor);

           if (baseCursor < baseSize){
               baseChar = sequence[baseCursor];
           }

           if (currChar=='-'){
               cursor++;
           } else {
                if (baseChar == currChar){
                    sequenceIndexes.add(cursor);
                    cursor++;
                    baseCursor++;
                } else {
                    throw new RuntimeException("Invalid sequence - expected:"+baseChar+" found "+currChar+" position "+cursor+","+baseCursor);
                }
           }

        }

        if (baseCursor < baseSize){
            throw new RuntimeException("Alignment is not complete!");
        }



    }

    private static void addNodeToAlignment(List<List<Character>> returnValue, 
            List<List<Character>> childAlignment1, int seq1Index, int actualNodeStartPos1, int actualNodeEndPos1,
            List<List<Character>> childAlignment2, int seq2Index,int actualNodeStartPos2, int actualNodeEndPos2) {
        int cursor1 = actualNodeStartPos1;
        int cursor2 = actualNodeStartPos2;

        while (cursor1<=actualNodeEndPos1 && cursor2 <=actualNodeEndPos2){

            //System.err.println("Adding "+cursor1+","+cursor2);

            char currChar1 = childAlignment1.get(seq1Index).get(cursor1);
            char currChar2 = childAlignment2.get(seq2Index).get(cursor2);

            //System.err.println("Adding "+currChar1+","+currChar2);

            if (currChar1!='-' && currChar2!='-'){
                /* Invalid comparison for new method, removing
                if (currChar1==currChar2){
                    //Copy both sub-alignments
                    addPosition(returnValue, childAlignment1, cursor1, childAlignment2, cursor2);
                    cursor1++;
                    cursor2++;
                } else {
                    throw new RuntimeException("Invalid Node->"+currChar1+","+currChar2);
                }
                 * 
                 */
                addPosition(returnValue, childAlignment1, cursor1, childAlignment2, cursor2);
                cursor1++;
                cursor2++;
            } else {
                if (currChar1=='-'){
                    if (currChar2=='-'){
                        //Copy both sub-alignment 1 and sub-alignment 2
                        addPosition(returnValue, childAlignment1, cursor1, childAlignment2, cursor2);
                        cursor1++;
                        cursor2++;
                    } else {
                        //Copy sub-alignment 1 and a gap on sub-alignment 2
                        addPosition(returnValue, childAlignment1, cursor1, childAlignment2, -1);
                        cursor1++;
                    }
                } else
                if (currChar2=='-'){
                    //Copy sub-alignment 1 and a gap on sub-alignment 1
                    addPosition(returnValue, childAlignment1, -1, childAlignment2, cursor2);
                    cursor2++;
                }
            }
        }

        /*
         if (returnValue.get(10)!=null){
                    List<Character> temp = returnValue.get(10);
                    for (int k=0; k < temp.size(); k++){
                        if (temp.get(k)!='-'){
                            System.err.print(temp.get(k));
                        }
                    }
                    System.err.println();
                }
         * 
         */

    }

    private static void addPosition(List<List<Character>> returnValue, 
            List<List<Character>> childAlignment1, int pos1,
            List<List<Character>> childAlignment2, int pos2) {

        for (int i=0; i< childAlignment1.size(); i++){

            List<Character> sequence1 = childAlignment1!=null? childAlignment1.get(i):null;
            List<Character> sequence2 = childAlignment2!=null? childAlignment2.get(i):null;

            if (sequence1 !=null && sequence2 !=null){
                throw new RuntimeException("Sequence present on both alignments "+i);
            } else {
                if (sequence1!=null){
                    char currChar = (pos1 >= 0)? sequence1.get(pos1):'-';

                    //if (i==10) System.err.println("(1)Adding "+currChar+"to 10");

                    returnValue.get(i).add(currChar);
                }
                if (sequence2!=null){
                   char currChar = (pos2 >= 0)? sequence2.get(pos2):'-';

                   //if (i==10) System.err.println("(2)Adding "+currChar+"to 10");

                   returnValue.get(i).add(currChar);
                }
            }

           

        }

    }

}
