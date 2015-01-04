/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.initialpopulation;

import algae.chromossome.Chromossome;
import java.util.ArrayList;

/**
 *
 * @author sergio
 */
public class SuperPopulation implements InitialPopulation{

   private int populationSize = 0;

    public SuperPopulation(int size) {
        populationSize = size;
    }

    public ArrayList<Chromossome> generate() {
        ArrayList<Chromossome> returnValue = new ArrayList<Chromossome>();

        InitialPopulation subset = new BasicStarAlignmentPopulation(populationSize);
        returnValue.addAll(subset.generate());

        subset = new RandomStarAlignmentPopulation(populationSize);
        returnValue.addAll(subset.generate());


        return returnValue;
    }

    

}
