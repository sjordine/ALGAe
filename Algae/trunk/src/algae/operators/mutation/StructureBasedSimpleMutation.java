/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.operators.mutation;

import algae.chromossome.BasicListChromossome;
import algae.chromossome.Chromossome;
import algae.environment.Environment;
import algae.operators.mutation.selectionpoint.StructureRoulette;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author sergio
 */
public class StructureBasedSimpleMutation implements MutationOperator {

    public Chromossome execute(Chromossome c1) {
        Chromossome returnValue = null;

        BasicListChromossome parent = (BasicListChromossome) c1.clone();
        ArrayList<ArrayList<Character>> parentData = parent.getDataAsList();

        Environment environment = Environment.getInstance();


        Random r = new Random();

        //Select mutation candidate sequence and its structure roulette
        int sequenceIndex = r.nextInt(parentData.size());
        ArrayList<Character> mutatedSequence = parentData.get(sequenceIndex);
        StructureRoulette roulete = environment.getStructureRoulette(sequenceIndex);
        //Select position within Roulette
        if (roulete != null) {
            double roulettePosition = r.nextDouble();
            //search position using roulette
            int sequencePosition = roulete.getSelectedIndex(roulettePosition);
            //search actual position within alignment based on sequence position
            int positionToChange = findPosition(mutatedSequence,sequencePosition);
            //insert gap
            insertGap(parentData, sequenceIndex, positionToChange);            
            
            returnValue = new BasicListChromossome(parentData);
            returnValue.setOrigin("SP_STMT");
            returnValue.setRootOrigin("SP_STMT");
        } else {
            throw new RuntimeException("Invalid roulette - index " + sequenceIndex);
        }

        return returnValue;
    }

    private int findPosition(ArrayList<Character> mutatedSequence, int sequencePosition) {
        int position = -1;
        int cursor = 0;


        int sequenceSize = mutatedSequence.size();

        int i = 0;
        boolean found = false;

        while (i < sequenceSize && !found){
           char currentPosition = mutatedSequence.get(i);
           //Found an original sequence residue
           if (currentPosition != '-'){
               if (cursor == sequencePosition){
                   position = i;
                   found = true;
               }
               cursor++;
           }
           i++;
        }

        return position;
    }

    private void insertGap(ArrayList<ArrayList<Character>> parentData, int sequenceIndex, int positionToChange) {
        
        int sequenceCount = parentData.size();      

        for (int i=0; i < sequenceCount; i++){

            ArrayList<Character> mutatedSequence = parentData.get(i);

            if (i == sequenceIndex){
                //Add gap on the proper position
                mutatedSequence.add(positionToChange, '-');
            } else {
                //Add a gap to last position in order to keep alignment correct
                mutatedSequence.add('-');
            }
        }

    }


   
    
}
