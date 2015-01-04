/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package craligner.conservedregion;

import craligner.environment.Environment;
import java.util.ArrayList;
import java.util.List;
import jbio.alignment.LocalAlignment;
import jbio.alignment.Matrix;
import jbio.alignment.Sequence;
import jbio.alignment.compressedalphabet.CompressedAlphabet;
import jbio.alignment.compressedalphabet.Dayhoff6;
import jbio.score.PairWiseScore;
import jbio.util.alignment.MatrixUtil;

/**
 *
 * @author sergio
 */
public class RecursiveLocalAligner {

    private static final int CUT_SIZE = 3;
    private static final int MAX_GAP_SIZE = 3;

    public static List<SubAlignment> getSubAlignments(Sequence sequence1, Sequence sequence2, Matrix scoreMatrix) {
        List<SubAlignment> returnValue = new ArrayList<SubAlignment>();

        //System.out.println("Aligning sequences"+sequence1.getDescription()+"-"+sequence2.getDescription());


        List<SubAlignment> macroAlignments = alignSequences(sequence1, sequence2, scoreMatrix);


        List<SubAlignment> tempList = new ArrayList<SubAlignment>();

        splitBySequence1(macroAlignments, tempList);
        splitBySequence2(tempList, returnValue);


        return returnValue;
    }

    /**
     * Align two sequences looking for its common local sequences
     *
     * @param sequence1 First sequence
     * @param sequence2 Second sequence
     * @param scoreMatrix Socring matrix
     * @return List of sub-alignments(local alignment) with adequate size;
     */
    public static List<SubAlignment> alignSequences(Sequence sequence1, Sequence sequence2, Matrix scoreMatrix) {
        List<SubAlignment> returnValue = new ArrayList<SubAlignment>();

        CompressedAlphabet dayhoff6 = new Dayhoff6();

        Sequence sequence1Converted = dayhoff6.convertSequence(sequence1);
        Sequence sequence2Converted = dayhoff6.convertSequence(sequence2);

        LocalAlignment aligner = new LocalAlignment(sequence1Converted, sequence2Converted, scoreMatrix);
        //LocalAlignment aligner = new LocalAlignment(sequence1, sequence2, scoreMatrix);
        double[][] alignmentMatrix = aligner.alignmentMatrix();

        //System.out.println("Sequences aligned");

        alignSubSequence(returnValue, sequence1, sequence2, scoreMatrix, alignmentMatrix, 0, sequence1.getData().length() + 1, 0, sequence2.getData().length() + 1);

        return returnValue;
    }

    /**
     * Recursivielly find the non-conflicting local alignments on two sequences
     *
     * @param subAlignmentList output list
     * @param sequence1 First Sequence
     * @param sequence2 Second Sequence
     * @param scoreMatrix scoring matrix
     * @param alignmentMatrix  local matrix from the dynamic programming algorithm
     * @param seq1Start Start index of the aligned region on sequence 1
     * @param seq1End   End index of the aligned region on sequence 1
     * @param seq2Start Start index of the aligned region on sequence 2
     * @param seq2End   End index of the aligned region on sequence 2
     */
    private static void alignSubSequence(List<SubAlignment> subAlignmentList,
            Sequence sequence1, Sequence sequence2, Matrix scoreMatrix,
            double[][] alignmentMatrix,
            int seq1Start, int seq1End,
            int seq2Start, int seq2End) {

        //System.out.println("Aligning subsequences ("+seq1Start+","+seq1End+")"+" ("+seq2Start+","+seq2End+")");

        double maxScore = 0;
        int x = 0;
        int y = 0;

        int PosSeq1End = 0;
        int PosSeq2End = 0;

        int PosSeq1Start = 0;
        int PosSeq2Start = 0;

        CompressedAlphabet dayhoff6 = new Dayhoff6();

        Sequence sequence1Converted = dayhoff6.convertSequence(sequence1);
        Sequence sequence2Converted = dayhoff6.convertSequence(sequence2);
        
        char[] convSeq1 = sequence1Converted.getDataArray();
        char[] convSeq2 = sequence2Converted.getDataArray();

        char[] seq1 = sequence1.getDataArray();
        char[] seq2 = sequence2.getDataArray();

        StringBuilder alignedSeq1 = new StringBuilder();
        StringBuilder alignedSeq2 = new StringBuilder();


        if (seq2End > seq2Start && seq1End > seq1Start) {
            //Find the best local alignment score within the given region
            for (int i = seq1Start; i < seq1End; i++) {
                for (int j = seq2Start; j < seq2End; j++) {
                    if (alignmentMatrix[i][j] >= maxScore) {
                        maxScore = alignmentMatrix[i][j];
                        x = i;
                        y = j;
                    }
                }
            }

            //Keep the the last position of the local alignment
            PosSeq1End = x;
            PosSeq2End = y;

            //Navigate through the sequence to find first alignment pair
            int posSeq1 = x;
            int posSeq2 = y;

            int size1 = 0;
            int size2 = 0;

            while (alignmentMatrix[posSeq1][posSeq2] > 0
                    && posSeq1 > seq1Start && posSeq2 > seq2Start) {                
               
                //System.out.println(x+","+y+"("+seq1[x-1]+","+seq2[y-1]+")");

                if (alignmentMatrix[posSeq1][posSeq2] == alignmentMatrix[posSeq1 - 1][posSeq2 - 1]
                        + Score(convSeq1[posSeq1 - 1], convSeq2[posSeq2 - 1], scoreMatrix)) {
                    //Match or mismatch
                    alignedSeq1.insert(0, seq1[posSeq1 - 1]);
                    alignedSeq2.insert(0, seq2[posSeq2 - 1]);

                    x = posSeq1;
                    y = posSeq2;

                    posSeq1--;
                    posSeq2--;

                    size1++;
                    size2++;
                } else {
                    if (alignmentMatrix[posSeq1][posSeq2] == alignmentMatrix[posSeq1 - 1][posSeq2]
                            + Score('-', convSeq2[posSeq2 - 1], scoreMatrix)) {
                        //Gap on sequence 2
                        alignedSeq1.insert(0, '-');
                        alignedSeq2.insert(0, seq2[posSeq2 - 1]);

                        y = posSeq2;
                        
                        posSeq2--;

                        size2++;
                    } else {
                        if (alignmentMatrix[posSeq1][posSeq2] == alignmentMatrix[posSeq1][posSeq2 - 1]
                                + Score('-', convSeq1[posSeq1 - 1], scoreMatrix)) {
                            //Gap on sequence 1
                            alignedSeq1.insert(0, seq1[posSeq1 - 1]);
                            alignedSeq2.insert(0, '-');

                              x= posSeq1;

                            posSeq1--;

                            size1++;
                        }
                    }
                }
            }

            //Keep starting position on each sequence (-1 to remove the 0,0 index of the matrix)
            PosSeq1Start = x;
            PosSeq2Start = y;

            //Keep sub-alignment if valid and recursivelly calls next round
            if (size1 > CUT_SIZE && size2 > CUT_SIZE) {
                //Add new sub-alignment
                SubAlignment subAlignment = new SubAlignment();

                //Adding -1 in order to remove the row and colum 0
                //of the dynamic programming (keeping the proper index
                //on the sequence string
                subAlignment.setStartSequence1(PosSeq1Start-1);
                subAlignment.setEndSequence1(PosSeq1End-1);

                subAlignment.setStartSequence2(PosSeq2Start-1);
                subAlignment.setEndSequence2(PosSeq2End-1);

                subAlignment.setAlignedSequence1(alignedSeq1.toString());
                subAlignment.setAlignedSequence2(alignedSeq2.toString());

                subAlignment.setScore(maxScore);

                subAlignmentList.add(subAlignment);

                //Recursive calls
                alignSubSequence(subAlignmentList, sequence1, sequence2, scoreMatrix, alignmentMatrix, seq1Start, PosSeq1Start - 1, seq2Start, PosSeq2Start - 1);
                alignSubSequence(subAlignmentList, sequence1, sequence2, scoreMatrix, alignmentMatrix, PosSeq1End + 1, seq1End, PosSeq2End + 1, seq2End);
            }


        }

    }

    private static void splitBySequence1(List<SubAlignment> macroAlignments, List<SubAlignment> resultList) {


        for (SubAlignment s : macroAlignments) {

            int startIndex = -1;
            int endIndex = -1;
            String alignedSequence = s.getAlignedSequence1();
            int seqSize = alignedSequence.length();
            int currSeqSize = 0;
            int gapCount = 0;

            for (int i = 0; i < seqSize; i++) {
                if (alignedSequence.charAt(i) == '-') {
                    
                    gapCount++;

                    if (gapCount > MAX_GAP_SIZE && startIndex >= 0) {
                        //Add last subalignment, if big enough
                        addAlignment(resultList, s, startIndex, endIndex, currSeqSize);

                        //Reset alignment position
                        startIndex = -1;
                        endIndex = -1;
                        currSeqSize = 0;
                    }
                } else {
                    //No sub sequence started
                    if (startIndex == -1) {
                        startIndex = i;
                        endIndex = i;
                        currSeqSize = 1;
                        gapCount = 0;
                    } else {
                        //Expand sequence if already on it                        
                        endIndex = i;
                        currSeqSize++;
                    }
                }
            }

            addAlignment(resultList, s, startIndex, endIndex, currSeqSize);

        }
    }

    private static void splitBySequence2(List<SubAlignment> macroAlignments, List<SubAlignment> resultList) {
        for (SubAlignment s : macroAlignments) {
            int startIndex = -1;
            int endIndex = -1;
            String alignedSequence = s.getAlignedSequence2();
            int seqSize = alignedSequence.length();
            int currSeqSize = 0;
            int gapCount = 0;

            for (int i = 0; i < seqSize; i++) {
                if (alignedSequence.charAt(i) == '-') {
                    gapCount++;

                    if (gapCount > MAX_GAP_SIZE && startIndex >= 0) {
                        //Add last subalignment, if big enough
                        addAlignment(resultList, s, startIndex, endIndex, currSeqSize);

                        //Reset alignment position
                        startIndex = -1;
                        endIndex = -1;
                        currSeqSize = 0;
                    }
                } else {
                    //No sub sequence started
                    if (startIndex == -1) {
                        startIndex = i;
                        endIndex = i;
                        currSeqSize = 1;
                        gapCount = 0;
                    } else {
                        //Expand sequence if already on it
                        endIndex = i;
                        currSeqSize++;
                    }
                }
            }

            addAlignment(resultList, s, startIndex, endIndex, currSeqSize);

        }
    }

    private static double Score(char c1, char c2, Matrix scoreMatrix) {
        return MatrixUtil.getScore(scoreMatrix, c1, c2);
    }

    private static void addAlignment(List<SubAlignment> subAlignmentList, SubAlignment s, int startIndex, int endIndex, int seqSize) {


        if (seqSize > CUT_SIZE && startIndex > -1 && endIndex > -1) {
            SubAlignment alignment = new SubAlignment();
            int refPos1 = s.getStartSequence1();
            int refPos2 = s.getStartSequence2();

            String subSeq1 = s.getAlignedSequence1().substring(startIndex, endIndex + 1);
            String subSeq2 = s.getAlignedSequence2().substring(startIndex, endIndex + 1);

            int totalNumberOfLetters = 0;
            int numberOfLettersOnSelectedRegion = 0;
            for (int i=0;i<=endIndex;i++){
                if (s.getAlignedSequence1().charAt(i)!='-'){
                    totalNumberOfLetters++;
                    if (i>=startIndex){
                        numberOfLettersOnSelectedRegion++;
                    }
                }
            }


            int startPos = refPos1+(totalNumberOfLetters-numberOfLettersOnSelectedRegion);
            int endPos= refPos1+(totalNumberOfLetters-1);

            alignment.setStartSequence1(startPos);
            alignment.setEndSequence1(endPos);


            totalNumberOfLetters = 0;
            numberOfLettersOnSelectedRegion = 0;
            for (int i=0;i<endIndex;i++){
                if (s.getAlignedSequence2().charAt(i)!='-'){
                    totalNumberOfLetters++;
                    if (i>=startIndex){
                        numberOfLettersOnSelectedRegion++;
                    }
                }
            }
            startPos = refPos2+(totalNumberOfLetters-numberOfLettersOnSelectedRegion);
            endPos= refPos2+(totalNumberOfLetters);

            alignment.setStartSequence2(startPos);
            alignment.setEndSequence2(endPos);


           
            alignment.setAlignedSequence1(subSeq1);
            alignment.setAlignedSequence2(subSeq2);

            Environment environment = Environment.getInstance();

            alignment.setScore(PairWiseScore.getScore(subSeq1, subSeq2,environment.getMatrixLocal()));


            if (alignment.getScore()>0){
                subAlignmentList.add(alignment);
            }
        }
    }
}
