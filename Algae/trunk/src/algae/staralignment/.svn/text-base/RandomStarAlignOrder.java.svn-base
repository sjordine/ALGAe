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
public class RandomStarAlignOrder implements AlignOrder {

    private List<Integer> alreadySelectedList;
    private List<Integer> selectionList;

    Random random;

    public RandomStarAlignOrder(int sequenceCount){
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
            int anchorIndex =  Math.abs(random.nextInt())%selectionList.size();
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
            //Select anchor from already selected
            int anchorIndex = Math.abs(random.nextInt())%alreadySelectedList.size();
            int anchor = alreadySelectedList.get(anchorIndex);
            
            //Select new sequence from candidate list
            int newSeqIndex =  Math.abs(random.nextInt())%selectionList.size();
            int newSeq = selectionList.get(newSeqIndex);
            selectionList.remove(newSeqIndex);
            alreadySelectedList.add(newSeq);

            pair.setAnchor(anchor);
            pair.setNewSequence(newSeq);            
        }

        if (random.nextBoolean()){
            pair.setType(DistanceType.GLOBAL);
        } else {
            pair.setType(DistanceType.SEMIGLOBAL);
        }

        return pair;
    }

    public boolean hasSequences() {
        return !selectionList.isEmpty();
    }

}
