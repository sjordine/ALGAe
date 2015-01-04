/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.operators.mutation;

import algae.chromossome.BasicListChromossome;
import algae.chromossome.Chromossome;
import algae.util.alignment.SequenceUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author sergio
 */
public class ShiftGapBlockMutation implements MutationOperator {

    private class GapPosition {

        private int sequence;
        private int startPosition;
        private int endPosition;

        public GapPosition(int sequence, int startPosition, int endPosition) {
            this.sequence = sequence;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
        }
    }

    public Chromossome execute(Chromossome c1) {
        Chromossome returnValue = null;

        BasicListChromossome parent = (BasicListChromossome) c1.clone();
        ArrayList<ArrayList<Character>> parentData = parent.getDataAsList();

        //Get all gap sequences on the alignment
        List<GapPosition> gapList = getListGaps(parentData);

        //Select one of the gap sequences
        Random r = new Random();

        if (!gapList.isEmpty()) {

            int gap = r.nextInt(gapList.size());

            //Select a position within this gap
            int gapSize = 0;
            int i = 0;
            GapPosition gapData = null;
            int sequenceIndex = -1;
            int splitPosition = -1;



            gapData = gapList.get(gap);
            sequenceIndex = gapData.sequence;
            gapSize = gapData.endPosition - gapData.startPosition;
            splitPosition = r.nextInt(gapSize);





            //Select a position after gap on the selected sequence
            int seqSize = parentData.get(sequenceIndex).size();
            int delta = seqSize - gapData.endPosition;
            int finalPosition = r.nextInt(delta);

            //Split gap
            List<Character> selectedSequence = parentData.get(sequenceIndex);
            List<Character> head = selectedSequence.subList(0, gapData.startPosition + splitPosition);
            List<Character> gapsToMove = selectedSequence.subList(gapData.startPosition + splitPosition, gapData.endPosition);
            List<Character> sequenceToMove = selectedSequence.subList(gapData.endPosition, gapData.endPosition + finalPosition);
            List<Character> tail = selectedSequence.subList(gapData.endPosition + finalPosition, seqSize);

            ArrayList<Character> newSeq = new ArrayList<Character>();
            newSeq.addAll(head);
            newSeq.addAll(sequenceToMove);
            newSeq.addAll(gapsToMove);
            newSeq.addAll(tail);

            parentData.set(sequenceIndex, newSeq);

            SequenceUtil.removeEmptyColumns(parentData);

            returnValue = new BasicListChromossome(parentData);
            returnValue.setOrigin("SGB_MT");
            returnValue.setRootOrigin("SGB_MT");
        }


        return returnValue;
    }

    private List<GapPosition> getListGaps(ArrayList<ArrayList<Character>> alignment) {
        List<GapPosition> gaps = new ArrayList<GapPosition>();

        for (int j = 0; j < alignment.size(); j++) {
            ArrayList<Character> selectedSequence = alignment.get(j);
            for (int i = 0; i < selectedSequence.size(); i++) {
                if (selectedSequence.get(i) == '-') {
                    int positionFirst = i;
                    while (i < selectedSequence.size() && selectedSequence.get(i) == '-') {
                        i++;
                    }
                    if (positionFirst < i - 1) {
                        gaps.add(new GapPosition(j, positionFirst, i - 1));
                    }
                }
            }
        }
        return gaps;
    }
}
