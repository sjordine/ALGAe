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
public class SimpleMutation implements MutationOperator{

   

    public Chromossome execute(Chromossome c1) {

        Chromossome returnValue = null;

        BasicListChromossome parent = (BasicListChromossome)c1.clone();
        ArrayList<ArrayList<Character>> parentData =  parent.getDataAsList();

        Random r = new Random();
        int swapCount = 2 + r.nextInt(parentData.size() / 2);
        while (swapCount > 0) {
            swapSequence(parentData);
            swapCount--;
        }

        SequenceUtil.removeEmptyColumns(parentData);

        returnValue = new BasicListChromossome(parentData);
        returnValue.setOrigin("SP_MT");
        returnValue.setRootOrigin("SP_MT");

        return returnValue;
        
    }

    private void swapSequence(ArrayList<ArrayList<Character>> parentData) {
        
         Random r = new Random();
        //Try 5 times to make a mutation, if not possible after this attemps ,
         //this mutation is not done.
        for (int i = 0; i < 5; i++) {
            int iSequence = r.nextInt(parentData.size());
            List<Character> sequence = parentData.get(iSequence);
            int sequenceSize = sequence.size();
            int position = r.nextInt(sequenceSize);
            while (position < sequenceSize &&
                    sequence.get(position) != '-') {
                position++;
            }

            //swap if a gap was found
            if (position < sequenceSize) {
                //procura o final do bloco de gaps
                while (position < sequenceSize &&
                        sequence.get(position) == '-') {
                    position++;
                }

                //Non-terminal block
                if (position < sequenceSize) {
                ArrayList<Character> newSequence = new ArrayList<Character>();
                newSequence.addAll(sequence.subList(0, position-1));
                newSequence.add(sequence.get(position));
                newSequence.add('-');
                newSequence.addAll(sequence.subList(position+1, sequenceSize));
                parentData.set(iSequence, newSequence);
                }

            }
        }
    }

}
