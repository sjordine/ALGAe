/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.staralignment;

import algae.philogeny.matrix.DistanceMatrix;
import algae.philogeny.matrix.PairwiseDistance;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergio
 */
public class BasicStarAligner {

    private int sequenceCount = 0;
    private AlignOrder sequenceAligner = null;


    public BasicStarAligner(AlignOrder alignerOrder,int sequenceCount){
        sequenceAligner = alignerOrder;
        this.sequenceCount = sequenceCount;
    }

    public List<List<Character>> align(DistanceMatrix matrix) {

        boolean fistTime = true;
        List<List<Character>> alignment = new ArrayList<List<Character>>();
        List<Character> newSequence = null;

        for (int i = 0; i < sequenceCount; i++) {
            alignment.add(null);
        }



        while (sequenceAligner.hasSequences()) {
            //Callback for a pair anchor X new sequence
            int anchorIndex = 0;
            int newSeqIndex = 0;
            AlignPair pair = sequenceAligner.getNext();
            anchorIndex = pair.getAnchor();
            newSeqIndex = pair.getNewSequence();
            PairwiseDistance alignmentInfo = matrix.getAlignment(anchorIndex, newSeqIndex, pair.getType());

            if (fistTime) {
                //Align first two sequences based on its pairwise alignment
                alignment.set(pair.getAnchor(), new ArrayList<Character>());
                alignment.set(pair.getNewSequence(), new ArrayList<Character>());
                copySequence(alignment.get(anchorIndex), alignmentInfo.getAlignment()[0]);
                copySequence(alignment.get(newSeqIndex), alignmentInfo.getAlignment()[1]);
                fistTime = false;
            } else {
                //Align new sequence using anchor
                int i = 0;
                int j = 0;
                alignment.set(newSeqIndex, new ArrayList<Character>());
                newSequence = alignment.get(newSeqIndex);

                char[] anchorSeq = alignmentInfo.getAlignment()[0];
                char[] includeSeq = alignmentInfo.getAlignment()[1];
                List<Character> anchorRef = alignment.get(anchorIndex);
                while (i < anchorSeq.length && j < anchorRef.size()) {
                    if (anchorSeq[i] == anchorRef.get(j)) {
                        newSequence.add(includeSeq[i]);
                        i++;
                        j++;
                    } else {
                        if (anchorRef.get(j) == '-') {
                            //Insert gap on including sequence
                            //current position on this sequence was not yet included
                            newSequence.add('-');
                            j++;
                        } else {
                            //Gap on including sequence, shift all sequences
                            shiftSequences(newSeqIndex, alignment, j);
                            newSequence.add(includeSeq[i]);
                            j++;
                            i++;
                        }
                    }                   
                }

                if (i < anchorSeq.length) {
                    for (int pos = i; pos < anchorSeq.length; pos++) {
                        shiftSequences(newSeqIndex, alignment, j);
                        newSequence.add(includeSeq[pos]);
                        j++;
                    }
                }

                if (j < anchorRef.size()) {
                    for (int pos = j; pos < anchorRef.size(); pos++) {
                        newSequence.add('-');
                    }
                }
            }

        }

        return alignment;
    }

    private void copySequence(List<Character> list, char[] sequence) {
        for (int i = 0; i < sequence.length; i++) {
            list.add(sequence[i]);
        }
    }

    private void shiftSequences(int currentIndex, List<List<Character>> alignment, int index) {
        for (int i = 0; i < alignment.size(); i++) {
            if (i != currentIndex) {
                List<Character> currentSeq = alignment.get(i);
                if (currentSeq != null) {
                    currentSeq.add(index, '-');
                }
            }
        }
    }
}
