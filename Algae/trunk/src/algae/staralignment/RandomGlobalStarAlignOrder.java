/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.staralignment;

import algae.philogeny.matrix.DistanceType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author sergio
 */
public class RandomGlobalStarAlignOrder implements AlignOrder {

    private List<Integer> alreadySelectedList;
    private List<Integer> selectionList;

    private int anchorIndex = -1;

    Random random;

    public RandomGlobalStarAlignOrder(int sequenceCount){
        selectionList = new ArrayList<Integer>();
        alreadySelectedList = new ArrayList<Integer>();
        for (int i=0;i<sequenceCount;i++){
            selectionList.add(i);
        }

         random = new Random();

    }

    public AlignPair getNext() {

        AlignPair pair = new AlignPair();


        if (alreadySelectedList.isEmpty()){
            //First Selection
            anchorIndex =  Math.abs(random.nextInt())%selectionList.size();
            int anchor = selectionList.get(anchorIndex);
            selectionList.remove(anchorIndex);
            alreadySelectedList.add(anchor);

            int newSeqIndex =  Math.abs(random.nextInt())%selectionList.size();
            int newSeq = selectionList.get(newSeqIndex);
            selectionList.remove(newSeqIndex);
            alreadySelectedList.add(newSeq);

            pair.setAnchor(anchor);
            pair.setNewSequence(newSeq);

        } else {
            //Get already selected anchor
            int anchor = alreadySelectedList.get(0);

            //Select new sequence from candidate list
            int newSeqIndex =  Math.abs(random.nextInt())%selectionList.size();
            int newSeq = selectionList.get(newSeqIndex);
            selectionList.remove(newSeqIndex);
            alreadySelectedList.add(newSeq);

            pair.setAnchor(anchor);
            pair.setNewSequence(newSeq);
        }

        pair.setType(DistanceType.GLOBAL);


        return pair;
    }

    public boolean hasSequences() {
        return !selectionList.isEmpty();
    }

}

