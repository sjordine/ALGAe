/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mcbaligner.conservedblocks.graph.superblocks;

import java.util.ArrayList;
import java.util.List;
import jbio.alignment.Sequence;
import mcbaligner.conservedblocks.graph.pairbasedgraph.PairBasedGraphNode;
import mcbaligner.environment.Environment;
import mcbaligner.guidingtree.BranchNode;
import mcbaligner.guidingtree.DistanceInfo;
import mcbaligner.guidingtree.Node;

/**
 *
 * @author sergio
 */
public class SuperBlockUtil {

    public static void mountSuperNodes(Node rootNode, Object[][] graphs) {

        Environment environment = Environment.getInstance();
        List<Sequence> sequences = environment.getSequences();

        //Initialize sequence control
        List<List<SuperBlockNode>> sequenceBlocks = new ArrayList<List<SuperBlockNode>>();
        for (int i = 0; i < sequences.size(); i++) {
            sequenceBlocks.add(new ArrayList<SuperBlockNode>());
        }

        //Add nodes based on the guiding tree
        mountBlocks(rootNode, sequenceBlocks, graphs);

        for (int i = 0; i < sequences.size(); i++) {
            System.out.print("Sequence "+i +"->");
            List<SuperBlockNode> list = sequenceBlocks.get(i);
            for (int j=0; j<list.size();j++){
                SuperBlockNode node = list.get(j);
                List<UnitInfo> units = node.getBlockUnits();
                System.out.print(units.get(i).getStartPosition()+",");
            }
            System.out.println();
        }


    }

    private static void mountBlocks(Node node, List<List<SuperBlockNode>> sequenceBlocks, Object[][] graphs) {
        if ((node instanceof BranchNode)) {
            BranchNode alignment = (BranchNode) node;
            mountBlocks(alignment.getChild1(), sequenceBlocks, graphs);
            mountBlocks(alignment.getChild2(), sequenceBlocks, graphs);

            //add blocks associated with the current node
            mountCurrentBlocks(alignment, sequenceBlocks, graphs);
        }
    }

    private static void mountCurrentBlocks(BranchNode alignment, List<List<SuperBlockNode>> sequenceBlocks, Object[][] graphs) {
        List<DistanceInfo> distances = alignment.getChildDistances();

        for (int i = 0; i < distances.size(); i++) {
            DistanceInfo distanceInfo = distances.get(i);

            int sequence1 = distanceInfo.getSequence1();
            int sequence2 = distanceInfo.getSequence2();

            List<PairBasedGraphNode> nodes = (List<PairBasedGraphNode>) graphs[sequence1][sequence2];

            //Add nodes into superblocks if compatible
            for (int j = 0; j < nodes.size(); j++) {            
                PairBasedGraphNode node = nodes.get(j);
                if (!node.isStartNode() && !node.isEndNode()){
                    addNode(node, sequenceBlocks, sequence1, sequence2);
                }
            }
        }
    }

    private static void addNode(PairBasedGraphNode node, List<List<SuperBlockNode>> sequenceBlocks, int seq1, int seq2) {
        //Create super-block trial
        SuperBlockNode wannaBeBlock = new SuperBlockNode(sequenceBlocks.size());
        wannaBeBlock.addUnit(seq1, node.getSequence1Start(), node.getSequence1End());
        wannaBeBlock.addUnit(seq2, node.getSequence2Start(), node.getSequence2End());

        //Check if pair is compatible with the already existent blocks
        List<SuperBlockNode> superBlocks1 = sequenceBlocks.get(seq1);
        List<SuperBlockNode> superBlocks2 = sequenceBlocks.get(seq2);


        if (isCompatible(wannaBeBlock, superBlocks1) && isCompatible(wannaBeBlock, superBlocks2)) {
            //Find position within super-blocks list
            int index1 = 0;
            int index2 = 0;
            SuperBlockNode currentBlock1 = null;
            SuperBlockNode currentBlock2 = null;
            boolean found = false;
            for (int i = 0; i < superBlocks1.size() && !found; i++) {
                SuperBlockNode currentBlock = superBlocks1.get(i);
                List<UnitInfo> units = currentBlock.getBlockUnits();
                if (units.get(seq1) != null) {
                    UnitInfo targetUnit = units.get(seq1);
                   
                        if (targetUnit.getStartPosition() == node.getSequence1Start()) {
                            currentBlock1 = currentBlock;
                            index1=i;
                            found=true;
                        } else {
                            if (targetUnit.getStartPosition() > node.getSequence1Start()){
                            found = true;
                            index1=i;
                            }
                        }                    

                } else {
                    //TODO: Error!!!
                    System.err.println("Ooopps - seq1!!!!");
                }
            }

            if (!found){
                index1= superBlocks1.size();
            }

            found = false;
            for (int i = 0; i < superBlocks2.size() && !found; i++) {
                SuperBlockNode currentBlock = superBlocks2.get(i);
                List<UnitInfo> units = currentBlock.getBlockUnits();
                if (units.get(seq2) != null) {
                    UnitInfo targetUnit = units.get(seq2);
                    
                        if (targetUnit.getStartPosition() == node.getSequence2Start()) {
                            currentBlock2 = currentBlock;
                            index2=i;
                            found=true;
                        } else {
                            if (targetUnit.getStartPosition() > node.getSequence2Start()) {
                                found = true;
                                index2=i;
                            }
                        }



                } else {
                    //TODO: Error!!!
                    System.err.println("Ooopps - seq2!!!!");                    
                }
            }

             if (!found){
                index2= superBlocks2.size();
            }

            //Check if it is a merge or a new super-block
            if (currentBlock1!=null && currentBlock2!= null){
                //Merge both sequences
                SuperBlockNode newNode = mergeNodes(currentBlock1, currentBlock2);
                superBlocks1.set(index1, newNode);
                superBlocks2.set(index2, newNode);
            }
            if (currentBlock1!=null && currentBlock2==null){
                //Merge on super block 1
                currentBlock1.addUnit(seq2, node.getSequence2Start(), node.getSequence2End());
                //add to sequence 2
                addSuperBlock(superBlocks2,currentBlock1,index2,seq2);
            }
            if (currentBlock1 == null && currentBlock2 != null){
                //Merge on super block 2
                currentBlock2.addUnit(seq1, node.getSequence1Start(), node.getSequence1End());
                //add to sequence 1
                 addSuperBlock(superBlocks1,currentBlock2,index1,seq1);
            }
            if (currentBlock1 == null && currentBlock2 == null){
                //Add new superblock on both sequences
                addSuperBlock(superBlocks1,wannaBeBlock,index1,seq1);
                addSuperBlock(superBlocks2,wannaBeBlock,index2,seq2);
            }

        }
    }

    private static boolean isCompatible(SuperBlockNode wannaBeBlock, List<SuperBlockNode> superBlocks) {
        boolean returnValue = true;

        for (int i = 0; i < superBlocks.size(); i++) {
            SuperBlockNode currentNode = superBlocks.get(i);
            returnValue = returnValue && currentNode.isCompatible(wannaBeBlock);
        }


        return returnValue;
    }

    private static void addSuperBlock(List<SuperBlockNode> superBlocks, SuperBlockNode block, int index,int seqIndex) {
        if (index >= superBlocks.size()){
            superBlocks.add(block);
        } else {
            superBlocks.add(index, block);
        }
    }


    private static SuperBlockNode mergeNodes(SuperBlockNode superBlock1, SuperBlockNode superBlock2){

        int sequenceCount = superBlock1.getBlockUnits().size();
        SuperBlockNode returnValue = new SuperBlockNode(sequenceCount);

        List<UnitInfo> units1 = superBlock1.getBlockUnits();
        List<UnitInfo> units2 = superBlock2.getBlockUnits();

        for (int i=0; i< units1.size(); i++){
            //Assuming both sequences are compatible and equals on common ground.
            UnitInfo unit1 = units1.get(i);
            UnitInfo unit2 = units2.get(i);
            if (unit1!=null){
                returnValue.addUnit(unit1.getSequenceIndex(), unit1.getStartPosition(), unit1.getEndPosition());
            } else {
                if (unit2!=null){
                    returnValue.addUnit(unit2.getSequenceIndex(), unit2.getStartPosition(), unit2.getEndPosition());
                }
            }
        }


        return returnValue;
    }
}
