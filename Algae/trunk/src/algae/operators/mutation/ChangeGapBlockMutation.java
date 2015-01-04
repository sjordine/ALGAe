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
public class ChangeGapBlockMutation implements MutationOperator{

    private class GapPosition{
        private int sequence;
        private int startPosition;
        private int endPosition;

        public GapPosition(int sequence, int startPosition, int endPosition){
            this.sequence = sequence;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
        }
    }

    public Chromossome execute(Chromossome c1) {
        Chromossome returnValue = null;

        BasicListChromossome parent = (BasicListChromossome)c1.clone();
        ArrayList<ArrayList<Character>> parentData =  parent.getDataAsList();

        //Get all gap sequences on the alignment
        List<GapPosition> gapList = getListGaps(parentData);

        //Select one of the gap sequences
        Random r = new Random();
        int gap = r.nextInt(gapList.size());

        //Move gap one position to left or right
        shiftGap(parentData, gapList.get(gap));

        SequenceUtil.removeEmptyColumns(parentData);

        returnValue = new BasicListChromossome(parentData);
        returnValue.setOrigin("CGB_MT");
        returnValue.setRootOrigin("CGB_MT");

        return returnValue;

    }

    private List<GapPosition> getListGaps(ArrayList<ArrayList<Character>> alignment){
        List<GapPosition> gaps = new ArrayList<GapPosition>();

        for(int j = 0; j < alignment.size(); j++){
            ArrayList<Character> selectedSequence = alignment.get(j);
            for(int i = 0; i < selectedSequence.size() ; i++)
                if(selectedSequence.get(i) == '-'){
                    int positionFirst = i;
                    while(i < selectedSequence.size() && selectedSequence.get(i) == '-'){
                        i++;
                    }
                    gaps.add(new GapPosition(j, positionFirst, i-1));
                }
        }
        return gaps;
    }

    private void shiftGap(ArrayList<ArrayList<Character>> alignment, GapPosition gapBlock) {
        ArrayList<Character> sequence  = alignment.get(gapBlock.sequence);
        
        if(gapBlock.startPosition == 0 && gapBlock.endPosition == sequence.size()-1)
            return;

        boolean left = true;

        if(gapBlock.startPosition == 0 ){
            left = false;
        }
        else if(gapBlock.endPosition != sequence.size()-1){
            left = (Math.random() < 0.5);
        }

        String newSequence = "";

        //Shift block on position to the defined direction
        if(left){
            char characterToLeft = sequence.get(gapBlock.startPosition-1);
            sequence.set(gapBlock.startPosition-1, '-');
            sequence.set(gapBlock.endPosition,characterToLeft);
        }
        else{
            char characterToRight = sequence.get(gapBlock.endPosition+1);
            sequence.set(gapBlock.endPosition+1, '-');
            sequence.set(gapBlock.startPosition,characterToRight);
        }
    }

}
