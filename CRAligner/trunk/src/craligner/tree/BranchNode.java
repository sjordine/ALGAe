/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package craligner.tree;

import craligner.environment.Environment;
import craligner.msa.AlignmentInfo;
import java.util.ArrayList;
import java.util.List;
import jbio.alignment.Sequence;
import jbio.alignment.msa.AnchorAlignmentUtil;
import jbio.alignment.msa.SimmilarAnchorAlignmentUtil;

/**
 *
 * @author sergio
 */
public class BranchNode extends Node {

    Node child1 = null;
    Node child2 = null;

    public BranchNode(int sequence1, int sequence2, Node child1, Node child2) {
        super(sequence1, sequence2);
        this.child1 = child1;
        this.child2 = child2;
    }

    public List<char[]> getAlignment() {



        List<List<Character>> tempAlignment = null;
        List<char[]> returnValue = null;

        Environment environment = Environment.getInstance();
        List<Sequence> sequences = environment.getSequences();

        if (child1 != null && child2 != null) {
            List<char[]> alignment1 = child1.getAlignment();
            List<char[]> alignment2 = child2.getAlignment();

            AlignmentInfo info = environment.getDistanceInfo(sequence1Index, sequence2Index);

            if (info != null) {
                char[][] anchorAlignment = info.getAlignment();
                //tempAlignment = AnchorAlignmentUtil.align(anchorAlignment, alignment1, alignment2,info.getSequence1(),info.getSequence2());


                tempAlignment = AnchorAlignmentUtil.align(anchorAlignment, alignment1, alignment2,info.getSequence1(),info.getSequence2());


                validateAlignment(tempAlignment);
            }

            if (tempAlignment != null) {
                returnValue = new ArrayList<char[]>();
                for (int i = 0; i < tempAlignment.size(); i++) {
                    List<Character> sequence = tempAlignment.get(i);
                    if (sequence != null) {
                        int alignmentSize = tempAlignment.get(i).size();

                        char[] alignedSequence = new char[alignmentSize];
                        for (int j = 0; j < alignmentSize; j++) {
                            alignedSequence[j] = sequence.get(j);
                        }
                        returnValue.add(alignedSequence);
                    } else {
                        returnValue.add(null);
                    }
                }
            }

        }



        return returnValue;
    }

    @Override
    public void print() {

        System.out.print("(");
        child1.print();
        System.out.print(" , ");
        child2.print();
        System.out.print(")");
    }

    private void validateAlignment(List<List<Character>> tempAlignment) {

       System.out.println("Validating alignment");

       Environment environment = Environment.getInstance();
       List<Sequence> sequences = environment.getSequences();
       for (int i=0;i<tempAlignment.size();i++){
           Sequence currSequence = sequences.get(i);
           List<Character> currAlignedSeq = tempAlignment.get(i);
           if (currAlignedSeq!=null){
               int cursor = 0;
               for (int j=0;j<currAlignedSeq.size();j++){
                   char currChar = currAlignedSeq.get(j);
                   if (currChar!='-'){
                       if (currChar!=currSequence.getData().charAt(cursor)){
                           throw new RuntimeException("Error :"+ currSequence.getDescription() +" expected "+currSequence.getData().charAt(cursor)+" found "+currChar+" on position "+j);
                       }
                       cursor++;
                   }
               }
               if (cursor!=currSequence.getData().length()){
                   System.out.println("Missing characters!!!");
                   throw new RuntimeException("Error! Invalid alignment!");
               }
           }
       }
    }
}
