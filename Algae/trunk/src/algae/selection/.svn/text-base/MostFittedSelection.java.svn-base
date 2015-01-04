/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.selection;

import algae.chromossome.Chromossome;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author sergio
 */
public class MostFittedSelection implements Selection {

    public List<Chromossome> select(List<Chromossome> population, int selectionSize) {

        ArrayList<Chromossome> newPopulation = new ArrayList<Chromossome>();
        for (int j = 0; j < selectionSize; j++) {
            Chromossome newChromossome = (Chromossome) population.get(j).clone();
            //Keep recent origin
            if (!population.get(j).getOrigin().equals("CLONE")) {
                newChromossome.setOrigin(population.get(j).getOrigin());
            }
            newPopulation.add(newChromossome);
        }

        return newPopulation;
    }
}
