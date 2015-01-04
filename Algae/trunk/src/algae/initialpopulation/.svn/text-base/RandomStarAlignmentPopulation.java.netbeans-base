/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.initialpopulation;

import algae.alignment.Sequence;
import algae.chromossome.BasicListChromossome;
import algae.chromossome.Chromossome;
import algae.environment.Environment;
import algae.philogeny.matrix.DistanceMatrix;
import algae.staralignment.BasicStarAligner;
import algae.staralignment.RandomGlobalStarAlignOrder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergio
 */
public class RandomStarAlignmentPopulation implements InitialPopulation{

    private int populationSize = 0;

    public RandomStarAlignmentPopulation(int size) {
        populationSize = size;
    }


    public ArrayList<Chromossome> generate() {
         ArrayList<Chromossome> returnList = new ArrayList<Chromossome>();

        Environment environment = Environment.getInstance();
        List<Sequence> sequences = environment.getSequences();
        int sequenceCount = sequences.size();
        DistanceMatrix distances = environment.getDistanceMatrix();

        for (int i=0;i<sequenceCount*2;i++){
            RandomGlobalStarAlignOrder order = new RandomGlobalStarAlignOrder(sequenceCount);
            BasicStarAligner starAligner = new BasicStarAligner(order, sequenceCount);
            List<List<Character>> alignment = starAligner.align(distances);

            Chromossome newIndividual =  new  BasicListChromossome(alignment);

            returnList.add(newIndividual);

        }

        return returnList;
    }

}
