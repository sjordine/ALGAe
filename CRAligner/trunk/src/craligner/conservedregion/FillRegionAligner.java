/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package craligner.conservedregion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jbio.alignment.GlobalAlignment;
import jbio.alignment.GlobalAlignmentGapAffinity;
import jbio.alignment.Matrix;
import jbio.alignment.SemiGlobalAlignmentEndGap;
import jbio.alignment.SemiGlobalAlignmentStartingGap;
import jbio.alignment.Sequence;

/**
 * This class aligns two sequences based on its best local aligned regions
 * then it completes the rest of the sequence using global alignment and
 * variants of semi-global alignment on the edges(ignoring just gaps on begining
 * for the sequence start and just gaps on the end for sequence termination
 *
 * @author sergio
 */
public class FillRegionAligner {

    public static char[][] align(Sequence sequence1, Sequence sequence2, Matrix localMatrix,Matrix globalMatrix) {

        char[][] returnValue = null;

        //Prepare result alignment
        List<List<Character>> result = new ArrayList<List<Character>>();
        result.add(new ArrayList<Character>());
        result.add(new ArrayList<Character>());

        //Get sub-alignment conserved regions (sorted by its position)
        List<SubAlignment> subAlignments = RecursiveLocalAligner.getSubAlignments(sequence1, sequence2, localMatrix);

        if (subAlignments != null && !subAlignments.isEmpty()) {

            //Sort sub-alignments
            Collections.sort(subAlignments, new SubAlignmentComparator());

            SubAlignment startAnchor = null;
            SubAlignment endAnchor = null;

            //Sequence start
            if (subAlignments.size() >= 1) {
                startAnchor = subAlignments.get(0);
                endAnchor = startAnchor;
                int endPos1 = startAnchor.getStartSequence1();
                int endPos2 = startAnchor.getStartSequence2();

                if (endPos1 == 0 || endPos2 == 0) {
                    if (endPos1 == 0 && endPos2 == 0) {
                        //Sub-alignment starts at the beginning of both
                        //sequences. Append the subalignment to final alignment
                        //So, nothing is done here
                    } else {
                        if (endPos1 == 0) {
                            //Sequence 1 is aligned at the begining, add gaps
                            //to fill sequence 2 start
                            String subSequence = sequence2.getData().substring(0, endPos2);
                            for (int i = 0; i < subSequence.length(); i++) {
                                result.get(0).add('-');
                                result.get(1).add(subSequence.charAt(i));
                            }
                        } else {
                            //Sequence 2 is aligned at the begining, add gaps
                            //to fill sequence 1 start
                            String subSequence = sequence1.getData().substring(0, endPos1);
                            for (int i = 0; i < subSequence.length(); i++) {
                                result.get(0).add(subSequence.charAt(i));
                                result.get(1).add('-');
                            }
                        }
                    }
                } else {
                    //Align starting sequences
                    String subSeq1 = sequence1.getData().substring(0, endPos1);
                    String subSeq2 = sequence2.getData().substring(0, endPos2);

                    Sequence subSequence1 = new Sequence("seq1", subSeq1);
                    Sequence subSequence2 = new Sequence("seq2", subSeq2);

                    SemiGlobalAlignmentStartingGap aligner = new SemiGlobalAlignmentStartingGap(subSequence1, subSequence2, globalMatrix);
                    //GlobalAlignment aligner = new GlobalAlignment(subSequence1, subSequence2, globalMatrix);
                    //GlobalAlignmentGapAffinity aligner = new GlobalAlignmentGapAffinity(subSequence1, subSequence2, globalMatrix);

                    char[][] alignment = aligner.align();
                    for (int i = 0; i < alignment[0].length; i++) {
                        result.get(0).add(alignment[0][i]);
                    }
                    for (int i = 0; i < alignment[1].length; i++) {
                        result.get(1).add(alignment[1][i]);
                    }
                }
                //Append first sub-alignment
                for (int i = 0; i < startAnchor.getAlignedSequence1().length(); i++) {
                    result.get(0).add(startAnchor.getAlignedSequence1().charAt(i));
                    result.get(1).add(startAnchor.getAlignedSequence2().charAt(i));
                }

            }

            //Intermediate blocks (between two alignments)
            if (subAlignments.size() >= 2) {
                for (int i = 1; i < subAlignments.size(); i++) {

                    //Pre-condition: The start anchor was already set into the final alignment
                    endAnchor = subAlignments.get(i);
                    //Align between both anchors

                    int startPos1 = startAnchor.getEndSequence1() + 1;
                    int endPos1 = endAnchor.getStartSequence1();

                    if (startPos1 > endPos1) {
                        //Immediate sequences
                        startPos1 = endAnchor.getStartSequence1();
                        endPos1 = startPos1;
                    }

                    int startPos2 = startAnchor.getEndSequence2() + 1;
                    int endPos2 = endAnchor.getStartSequence2();

                    if (startPos2 > endPos2) {
                        //Immediate sequences
                        startPos2 = endAnchor.getStartSequence2();
                        endPos1 = startPos2;
                    }


                    if (startPos1 == endPos1 || startPos2 == endPos2) {
                        //At least one of the sequences has no residues
                        //between both sub-alignments
                        if (startPos1 == endPos1 && startPos2 == endPos2) {
                            //Both sub-alignments are consecutive without
                            //any letter between them.Nothing must be done
                            //besides add the final sub-alignment.
                            
                        } else {
                            if (startPos1 == endPos1) {
                                //There are residues just on sequence 2
                                //fill sequence 1 with gaps to add them
                                String subSequence = sequence2.getData().substring(startPos2, endPos2);
                                for (int j = 0; j < subSequence.length(); j++) {
                                    result.get(0).add('-');
                                    result.get(1).add(subSequence.charAt(j));
                                }
                            } else {
                                //There are residues on sequence 1
                                //fill sequence 2 with gaps to add them
                                String subSequence = sequence1.getData().substring(startPos1, endPos1);
                                for (int j = 0; j < subSequence.length(); j++) {
                                    result.get(0).add(subSequence.charAt(j));
                                    result.get(1).add('-');
                                }
                            }
                        }
                    } else {
                        //There are residues between both sequences
                        //align them using global alignment
                        String subSeq1 = sequence1.getData().substring(startPos1, endPos1);
                        String subSeq2 = sequence2.getData().substring(startPos2, endPos2);

                        Sequence subSequence1 = new Sequence("seq1", subSeq1);
                        Sequence subSequence2 = new Sequence("seq2", subSeq2);

                        //GlobalAlignment aligner = new GlobalAlignment(subSequence1, subSequence2, globalMatrix);
                        GlobalAlignmentGapAffinity aligner = new GlobalAlignmentGapAffinity(subSequence1, subSequence2, globalMatrix);

                        char[][] alignment = aligner.align();
                        for (int j = 0; j < alignment[0].length; j++) {
                            result.get(0).add(alignment[0][j]);
                        }
                        for (int j = 0; j < alignment[1].length; j++) {
                            result.get(1).add(alignment[1][j]);
                        }
                    }

                    //Add end subAlignment
                    for (int j = 0; j < endAnchor.getAlignedSequence1().length(); j++) {
                        result.get(0).add(endAnchor.getAlignedSequence1().charAt(j));
                        result.get(1).add(endAnchor.getAlignedSequence2().charAt(j));
                    }

                    //set this as the new starting sub-alignment
                    startAnchor = endAnchor;
                }
            }

            //Last block
            if (endAnchor != null) {
                //Align last block disconsidering gaps at the end
                int startPos1 = endAnchor.getEndSequence1() + 1;
                int startPos2 = endAnchor.getEndSequence2() + 1;
                int seq1Size = sequence1.getData().length();
                int seq2Size = sequence2.getData().length();
                if (startPos1 != (seq1Size - 1) || startPos2 != (seq2Size - 1)) {
                    //There are residues on at least one of the sequences
                    if (startPos1 != (seq1Size - 1) && startPos2 != (seq2Size - 1)) {
                        //There are residues on both sequences
                        //align using semi-global aligment (disconsidering gaps
                        //just at the end)
                        String subSeq1 = sequence1.getData().substring(startPos1, seq1Size);
                        String subSeq2 = sequence2.getData().substring(startPos2, seq2Size);

                        Sequence subSequence1 = new Sequence("seq1", subSeq1);
                        Sequence subSequence2 = new Sequence("seq2", subSeq2);

                        SemiGlobalAlignmentEndGap aligner = new SemiGlobalAlignmentEndGap(subSequence1, subSequence2, globalMatrix);
                        //GlobalAlignment aligner = new GlobalAlignment(subSequence1, subSequence2, globalMatrix);
                        //GlobalAlignmentGapAffinity aligner = new GlobalAlignmentGapAffinity(subSequence1, subSequence2, globalMatrix);

                        char[][] alignment = aligner.align();

                        for (int i = 0; i < alignment[0].length; i++) {
                            result.get(0).add(alignment[0][i]);
                        }
                        for (int i = 0; i < alignment[1].length; i++) {
                            result.get(1).add(alignment[1][i]);
                        }

                    } else {
                        if (startPos1 != (seq1Size - 1)) {
                            //There are residues just on sequence 1
                            //fill sequence 2 with gaps
                            for (int i=startPos1;i<seq1Size;i++){
                                result.get(0).add(sequence1.getData().charAt(i));
                                result.get(1).add('-');
                            }
                        } else {
                            //There are residues just on sequence 2
                            //fill sequence 1 with gaps
                            for (int i=startPos2;i<seq2Size;i++){
                                result.get(0).add('-');
                                result.get(1).add(sequence2.getData().charAt(i));                                
                            }
                        }
                    }
                }


            }

            returnValue = new char[2][result.get(0).size()];

        for (int i=0;i<result.get(0).size();i++){
            returnValue[0][i]=result.get(0).get(i);
            returnValue[1][i]=result.get(1).get(i);
        }

         validateALignment(result, sequence1, sequence2);

        } else {
            //Align Global
            GlobalAlignment aligner = new GlobalAlignment(sequence1, sequence2, globalMatrix);
            //GlobalAlignmentGapAffinity aligner = new GlobalAlignmentGapAffinity(sequence1, sequence2, globalMatrix);
            returnValue = aligner.align();
        }
     
        
        

        return returnValue;
    }

    private static void validateALignment(List<List<Character>> result, Sequence sequence1, Sequence sequence2) {
        int currPos = 0;
        for (int i=0;i<result.get(0).size();i++){
            char currentChar = result.get(0).get(i);
            if (currentChar!='-'){

                currPos++;

                /*
                if (currentChar != sequence1.getData().charAt(currPos)){

                        throw new RuntimeException("Alignment Error -> Error on Seq1 pos = "+currPos+": expected "+sequence1.getData().charAt(currPos)+" found "+currentChar);
                } else {
                    currPos++;
                }
                 * 
                 */
            }
        }

         if (currPos<sequence1.getData().length()-1){
            throw new RuntimeException("Alignment Error -> Error on Seq1: Missing characters from "+currPos+"to"+sequence1.getData().length());
        }

        currPos = 0;
        for (int i=0;i<result.get(1).size();i++){
            char currentChar = result.get(1).get(i);
            if (currentChar!='-'){
                currPos++;
                /*
                if (currentChar != sequence2.getData().charAt(currPos)){
                        throw new RuntimeException("Error on Seq2: pos = "+currPos+": expected "+sequence2.getData().charAt(currPos)+" found "+currentChar);
                } else {
                    currPos++;
                }
                 * */
                 
            }
        }

        if (currPos<sequence2.getData().length()-1){
            throw new RuntimeException("Error on Seq2: Missing characters from "+currPos+"to"+sequence2.getData().length());
        }
    }
}
