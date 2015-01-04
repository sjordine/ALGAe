/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jbio.alignment.msa;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergio
 */
public class AnchorAlignmentUtil {

    public static List<List<Character>> align(char[][] pivotAlignment, List<char[]> alignment1, List<char[]> alignment2, int seq1Index, int seq2Index) {


        List<List<Character>> returnValue = new ArrayList<List<Character>>();

        for (int i = 0; i < alignment1.size(); i++) {
            if (alignment1.get(i) != null || alignment2.get(i) != null) {
                returnValue.add(new ArrayList<Character>());
            } else {
                returnValue.add(null);
            }
        }

        int seq1Cursor = 0;
        int seq2Cursor = 0;
        int pivotCursor = 0;
        int pivotSize = pivotAlignment[0].length;
        int subAlignment1Size = alignment1.get(seq1Index).length;
        int subAlignment2Size = alignment2.get(seq2Index).length;

        while (pivotCursor < pivotSize && seq1Cursor < subAlignment1Size && seq2Cursor < subAlignment2Size) {

            //If both sub-alignments are consistent with pivot
            //add its characters to final alignment
            //Otherwise there are the following possible conditions:
            //1-There are gaps associated with the sequence used on pivot
            //at one of the sub-alignments. Therefore, the final alignment must be
            //filled with the sub-alignment sequence up to find the aligned character
            //on the pivot, when the pivot character is found the proccess returns to
            //its general behaviour.
            //2- The pivot sequence is different from its sub-alignment counter-part
            //it indicates that there is an incompatibility and the process is invalid.
            if (pivotAlignment[0][pivotCursor] == alignment1.get(seq1Index)[seq1Cursor]
                    && pivotAlignment[1][pivotCursor] == alignment2.get(seq2Index)[seq2Cursor]) {
                //Copy both sub-alignments letters to final alignment
                for (int i = 0; i < alignment1.size(); i++) {
                    char[] sequence1 = alignment1.get(i);
                    char[] sequence2 = alignment2.get(i);
                    if (sequence1 != null) {
                        returnValue.get(i).add(sequence1[seq1Cursor]);
                    }
                    if (sequence2 != null && sequence1 == null) {
                        returnValue.get(i).add(sequence2[seq2Cursor]);
                    }
                }
                pivotCursor++;
                seq1Cursor++;
                seq2Cursor++;
            } else {
                //Discrepancies between pivot and sub-alignment1
                if (pivotAlignment[0][pivotCursor] != alignment1.get(seq1Index)[seq1Cursor]) {
                    if (pivotAlignment[0][pivotCursor] == '-') {
                        //Gap on pivot, include sub-alignment 2 related residues
                        if (alignment2.get(seq2Index)[seq2Cursor] == '-') {
                            //Gaps on sequence 2 - add gaps on sub-alignment 1 
                            while (seq2Cursor < subAlignment2Size && alignment2.get(seq2Index)[seq2Cursor] == '-') {
                                for (int i = 0; i < alignment1.size(); i++) {
                                    char[] sequence1 = alignment1.get(i);
                                    char[] sequence2 = alignment2.get(i);
                                    if (sequence1 != null) {
                                        returnValue.get(i).add('-');
                                    }
                                    if (sequence2 != null && sequence1 == null) {
                                        returnValue.get(i).add(sequence2[seq2Cursor]);
                                    }
                                }
                                seq2Cursor++;
                            }
                        } else {
                            //Sub-alignment 2 is on its proper position
                            for (int i = 0; i < alignment1.size(); i++) {
                                char[] sequence1 = alignment1.get(i);
                                char[] sequence2 = alignment2.get(i);
                                if (sequence1 != null) {
                                    returnValue.get(i).add('-');
                                }
                                if (sequence2 != null && sequence1 == null) {
                                    returnValue.get(i).add(sequence2[seq2Cursor]);
                                }
                            }
                            pivotCursor++;
                            seq2Cursor++;
                        }
                    } else {
                        if (alignment1.get(seq1Index)[seq1Cursor] == '-') {
                            //Gap on sub-alignment 1, fill sequence 1 residues up to proper element
                            while (seq1Cursor < subAlignment1Size && alignment1.get(seq1Index)[seq1Cursor] == '-') {
                                for (int i = 0; i < alignment1.size(); i++) {
                                    char[] sequence1 = alignment1.get(i);
                                    char[] sequence2 = alignment2.get(i);
                                    if (sequence1 != null) {
                                        returnValue.get(i).add(sequence1[seq1Cursor]);
                                    }
                                    if (sequence2 != null && sequence1 == null) {
                                        returnValue.get(i).add('-');
                                    }
                                }
                                seq1Cursor++;
                            }
                        } else {
                            throw new RuntimeException("Incompatible pivot sequence and sub-alignment 1 expected " + pivotAlignment[0][pivotCursor] + " found " + alignment1.get(seq1Index)[seq1Cursor]);
                        }
                    }
                } else {
                    //Discrepancies between pivot and sub-alignment2
                    if (pivotAlignment[1][pivotCursor] != alignment2.get(seq2Index)[seq2Cursor]) {
                        if (pivotAlignment[1][pivotCursor] == '-') {
                            //Gap on pivot, include sub-alignment 1 related residues
                            if (alignment1.get(seq1Index)[seq1Cursor] == '-') {
                                System.out.println("Gaps on sub-alignment 1?");
                                //Gaps on sequence 1 - add gaps on sub-alignment 2
                                while (alignment1.get(seq1Index)[seq1Cursor] == '-' && seq1Cursor < subAlignment1Size) {
                                    for (int i = 0; i < alignment1.size(); i++) {
                                        char[] sequence1 = alignment1.get(i);
                                        char[] sequence2 = alignment2.get(i);
                                        if (sequence1 != null) {
                                            returnValue.get(i).add(sequence1[seq1Cursor]);
                                        }
                                        if (sequence2 != null && sequence1 == null) {
                                            returnValue.get(i).add('-');
                                        }
                                    }
                                    seq1Cursor++;
                                }
                            } else {
                                ;
                                //Sub-alignment 1 is on its proper position
                                for (int i = 0; i < alignment1.size(); i++) {
                                    char[] sequence1 = alignment1.get(i);
                                    char[] sequence2 = alignment2.get(i);
                                    if (sequence1 != null) {
                                        returnValue.get(i).add(sequence1[seq1Cursor]);
                                    }
                                    if (sequence2 != null && sequence1 == null) {
                                        returnValue.get(i).add('-');
                                    }
                                }
                                pivotCursor++;
                                seq1Cursor++;
                            }
                        } else {
                            if (alignment2.get(seq2Index)[seq2Cursor] == '-') {
                                //Gap on sub-alignment 2, fill sequence 2 residues up to proper element
                                while (seq2Cursor < subAlignment2Size && alignment2.get(seq2Index)[seq2Cursor] == '-') {
                                    for (int i = 0; i < alignment1.size(); i++) {
                                        char[] sequence1 = alignment1.get(i);
                                        char[] sequence2 = alignment2.get(i);
                                        if (sequence1 != null) {
                                            returnValue.get(i).add('-');
                                        }
                                        if (sequence2 != null && sequence1 == null) {
                                            returnValue.get(i).add(sequence2[seq2Cursor]);
                                        }
                                    }
                                    seq2Cursor++;
                                }
                            } else {
                                throw new RuntimeException("Incompatible pivot sequence and sub-alignment 1");
                            }
                        }
                    }
                }
            }
        }


        // Complete remaining residues
        //Align remaining residues from alignment 1 with gaps on alignment 2
        if (seq1Cursor < subAlignment1Size) {
            for (int i = seq1Cursor; i < subAlignment1Size; i++) {

                for (int j = 0; j < alignment1.size(); j++) {
                    char[] sequence1 = alignment1.get(j);
                    char[] sequence2 = alignment2.get(j);
                    if (sequence1 != null) {
                        returnValue.get(j).add(sequence1[i]);
                    }
                    if (sequence2 != null && sequence1 == null) {
                        returnValue.get(j).add('-');
                    }
                }
                pivotCursor++;
                seq1Cursor++;
            }
        }
        //Align remaining residues from alignment 2 with gaps on alignment 1
        if (seq2Cursor < subAlignment2Size ) {
            for (int i = seq2Cursor; i < subAlignment2Size; i++) {

                for (int j = 0; j < alignment1.size(); j++) {
                    char[] sequence1 = alignment1.get(j);
                    char[] sequence2 = alignment2.get(j);
                    if (sequence1 != null) {
                        returnValue.get(j).add('-');
                    }
                    if (sequence2 != null && sequence1 == null) {
                        returnValue.get(j).add(sequence2[i]);
                    }
                }
                pivotCursor++;
                seq1Cursor++;

            }
        }

        /*
        //if (pivotCursor < pivotSize - 1) {
        if (seq1Cursor == subAlignment1Size) {
        //Gaps at the end of sequence1 to
        //complete sequence 2
        for (int i = seq2Cursor; i < subAlignment2Size; i++) {

        for (int j = 0; j < alignment1.size(); j++) {
        char[] sequence1 = alignment1.get(j);
        char[] sequence2 = alignment2.get(j);
        if (sequence1 != null) {
        returnValue.get(j).add('-');
        }
        if (sequence2 != null && sequence1 == null) {
        returnValue.get(j).add(sequence2[i]);
        }
        }
        pivotCursor++;
        seq1Cursor++;

        }
        } else {
        if (seq2Cursor == subAlignment2Size) {
        //  Gaps  at the end of sequence2
        // to complete sequence 1
        for (int i = seq1Cursor; i < subAlignment1Size; i++) {

        for (int j = 0; j < alignment1.size(); j++) {
        char[] sequence1 = alignment1.get(j);
        char[] sequence2 = alignment2.get(j);
        if (sequence1 != null) {
        returnValue.get(j).add(sequence1[i]);
        }
        if (sequence2 != null && sequence1 == null) {
        returnValue.get(j).add('-');
        }
        }
        pivotCursor++;
        seq1Cursor++;

        }
        } else {
        throw new RuntimeException("Incompatible pivot sequence and sub-alignments, there are remaining characters on both sequences");
        }
        }
        //}
         *
         */

        return returnValue;
    }

    /**
     * Align two subalignments based on an anchor alignment.
     *
     * Both sub-alignments must have the same size (with null on indexes of non-aligned sequences)
     * and be compatible (both sequence indexes are the same)
     * The first sequence on the anchor alignment must be present on alignment 1 and the second one
     * on the second sub-alignment
     *
     * @param childAlignment1 first sub-alignment
     * @param childAlignment2 second sub-alignment
     * @param anchorAlignment anchor alignment containing a sequence present on
     *        child alignment 1 on its first index and a sequence present on child
     *        alignment 2 on its second one.
     * @param seq1Index Index of the first anchor sequence into sub-alignment 1
     * @param seq2Index Index of the second anchor sequence into sub-alignment 2
     *
     * @return An alignment that merges both sub-alignments based on the given anchor alignment
     */
    public static List<List<Character>> mergeAlignmentsByAnchor(List<List<Character>> childAlignment1,
            List<List<Character>> childAlignment2, List<List<Character>> anchorAlignment,
            int seq1Index, int seq2Index) {

        //Use aligned sequences in order to align both sub-alignments
        int alignmentSize = anchorAlignment.get(0).size();
        int cursor1 = 0;
        int cursor2 = 0;
        int anchorCursor = 0;

        //Create an empty alignment;
        List<List<Character>> alignment = new ArrayList<List<Character>>();
        int numberOfSequences = childAlignment1.size();

        for (int i = 0; i < numberOfSequences; i++) {
            if (childAlignment1.get(i) != null || childAlignment2.get(i) != null) {
                alignment.add(new ArrayList<Character>());
            } else {
                alignment.add(null);
            }
        }

        List<Character> referenceSequence1 = childAlignment1.get(seq1Index);
        List<Character> referenceSequence2 = childAlignment2.get(seq2Index);

        List<Character> anchorSequence1 = anchorAlignment.get(0);
        List<Character> anchorSequence2 = anchorAlignment.get(1);

        int sequence1Size = referenceSequence1.size();
        int sequence2Size = referenceSequence2.size();


        while (cursor1 < sequence1Size && cursor2 < sequence2Size && anchorCursor < alignmentSize) {
            char charAtSeq1 = referenceSequence1.get(cursor1);
            char charAtSeq2 = referenceSequence2.get(cursor2);

            char charAtAnchor1 = anchorSequence1.get(anchorCursor);
            char charAtAnchor2 = anchorSequence2.get(anchorCursor);

            if (charAtSeq1 == charAtAnchor1 && charAtSeq2 == charAtAnchor2) {
                //Both characters are the same, add current position to both sequences
                copyPosition(alignment, childAlignment1, cursor1, childAlignment2, cursor2);
                //Forward all cursors;
                anchorCursor++;
                cursor1++;
                cursor2++;
                // System.err.println("Keep"+cursor1+","+cursor2+","+anchorCursor);
            } else {
                int index1 = -1;
                int index2 = -1;
                if (charAtSeq1 != charAtAnchor1 && charAtSeq2 == charAtAnchor2) {
                    if (charAtSeq1 != '-' && charAtAnchor1 != '-') {

                        throw new RuntimeException("ERRO na seq 1!!!" + charAtSeq1 + "," + charAtAnchor1 + "," + cursor1 + "," + anchorCursor);
                    } else {
                        if (charAtSeq1 == '-') {
                            //System.err.println("Old gap 1"+cursor1+","+cursor2+","+anchorCursor);
                            //Gap preserved from sequence 1
                            index1 = cursor1;
                            index2 = -1;
                            cursor1++;

                        } else {
                            //System.err.println("New gap 1"+cursor1+","+cursor2+","+anchorCursor);
                            //Gap created on alignment 1 from anchor
                            //keep cursor on the sub-alignment 1
                            index1 = -1;
                            index2 = cursor2;
                            anchorCursor++;
                            cursor2++;

                        }
                    }
                }
                if (charAtSeq1 == charAtAnchor1 && charAtSeq2 != charAtAnchor2) {
                    if (charAtSeq2 != '-' && charAtAnchor2 != '-') {
                        throw new RuntimeException("ERRO na seq 2!!!");
                    } else {
                        if (charAtSeq2 == '-') {
                            // System.err.println("Old gap 2 "+cursor1+","+cursor2+","+anchorCursor);
                            //Gap preserved from sequence 2
                            index2 = cursor2;
                            index1 = -1;
                            cursor2++;

                        } else {
                            //Gap created on alignment 2 from anchor
                            // keep cursor on the sub-alignment 2
                            // System.err.println("New gap 2 "+cursor1+","+cursor2+","+anchorCursor);
                            index1 = cursor1;
                            index2 = -1;
                            anchorCursor++;
                            cursor1++;
                        }
                    }
                }
                if (charAtSeq1 != charAtAnchor1 && charAtSeq2 != charAtAnchor2) {
                    if ((charAtSeq1 != '-' && charAtAnchor1 != '-')
                            || (charAtSeq2 != '-' && charAtAnchor2 != '-')) {

                        System.err.println(charAtSeq1 + "," + charAtAnchor1);
                        System.err.println(charAtSeq2 + "," + charAtAnchor2);

                        System.err.println(anchorCursor + "," + cursor1);

                        System.err.println(referenceSequence1);
                        System.err.println(anchorAlignment.get(0).toString());


                        throw new RuntimeException("ERRO nas sequencias!!!");
                    } else {
                        if (charAtSeq1 == '-' && charAtSeq2 == '-') {
                            //Both sequences have a preserved gap
                            //advance both cursors, keeping anchor cursor
                            index1 = cursor1;
                            index2 = cursor2;
                            cursor1++;
                            cursor2++;
                        } else {
                            if (charAtAnchor1 == '-' && charAtSeq2 == '-') {

                                //New gap on anchor for sub-alignment 1
                                //and preserved gap on sub-alignment 2
                                //create gap for sub-alignment 1 on final alignment
                                //keep cursor on sub-alignment 1
                                //advance cursor on sub-alignment 2
                                //keep anchor alignment
                                index1 = -1;
                                index2 = cursor2;
                                cursor2++;

                            } else {
                                if (charAtAnchor2 == '-' && charAtSeq1 == '-') {

                                    //New gap on anchor for sub-alignment2
                                    //and preserved gap on sub-alignment 1

                                    //create gap for sub-alignment 2 on final alignment
                                    //keep cursor on sub-alignment 2
                                    //advance cursor on sub-alignment 1
                                    //keep anchor alignment
                                    index2 = -1;
                                    index1 = cursor1;
                                    cursor1++;

                                } else {
                                    System.err.println(anchorAlignment.get(0));
                                    System.err.println(anchorAlignment.get(1));
                                    throw new RuntimeException("OOOOOPS!!!! E agora??" + charAtSeq1 + "," + charAtAnchor1 + ":" + charAtSeq2 + "," + charAtAnchor2 + ":" + anchorCursor);
                                }
                            }
                        }


                    }
                }


                copyPosition(alignment, childAlignment1, index1, childAlignment2, index2);



            }

        }

        if (cursor1 < sequence1Size || cursor2 < sequence2Size) {
            //Previous alignment remain (gaps at the end)
            if (cursor1 < sequence1Size) {
                //Fill alignment with last characters of the first sub-alignment adding
                //gaps to the end of the second
                for (int i = cursor1; i < sequence1Size; i++) {
                    copyPosition(alignment, childAlignment1, i, childAlignment2, -1);
                }
            } else {
                //Fill alignment with last characters of the second sub-alignment adding
                //gaps to the end of the first
                for (int i = cursor2; i < sequence2Size; i++) {
                    copyPosition(alignment, childAlignment1, -1, childAlignment2, i);
                }
            }
        }

        return alignment;

    }

    private static void copyPosition(List<List<Character>> alignment,
            List<List<Character>> childAlignment1,
            int cursor1,
            List<List<Character>> childAlignment2,
            int cursor2) {
        for (int i = 0; i < alignment.size(); i++) {
            List<Character> seq1 = childAlignment1.get(i);
            List<Character> seq2 = childAlignment2.get(i);

            if (seq1 != null && seq2 != null) {
                System.err.println("ERRO!!!!!!!!!!!!!!!!!!");
            }

            if (seq1 != null) {
                if (cursor1 != -1) {
                    alignment.get(i).add(seq1.get(cursor1));
                } else {
                    alignment.get(i).add('-');
                }
            }

            if (seq2 != null) {
                if (cursor2 != -1) {
                    alignment.get(i).add(seq2.get(cursor2));
                } else {
                    alignment.get(i).add('-');
                }
            }

        }

    }
}
