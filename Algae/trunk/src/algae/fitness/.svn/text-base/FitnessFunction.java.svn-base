/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.fitness;

import algae.chromossome.Chromossome;
import java.util.List;

/**
 *
 * @author sergio
 */
public abstract class FitnessFunction {

    public void setFitness(List<Chromossome> population){

        int popSize = population.size();

        for (int i=0;i<popSize;i++){
            Chromossome individual = population.get(i);
            if (individual!=null && individual.getScore()==null){
                individual.setScore(fitnessFunction(individual));
            }
        }
    }

    public abstract double fitnessFunction(Chromossome individual);
    
}
