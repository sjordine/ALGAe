/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.initialpopulation;

import craligner.tree.Node;
import craligner.tree.TreeUtil;
import algae.alignment.Sequence;
import algae.chromossome.BasicListChromossome;
import algae.chromossome.Chromossome;
import algae.environment.Environment;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergio
 */
public class PopulationCR2 implements InitialPopulation {


   private int populationSize = 0;

    public PopulationCR2(Integer size) {
        populationSize = size;
    }

    public ArrayList<Chromossome> generate() {

        System.out.println("Initial Population ");

        ArrayList<Chromossome> returnValue = new ArrayList<Chromossome>();

        InitialPopulation subset = new BasicStarAlignmentPopulation(populationSize);
        returnValue.addAll(subset.generate());

        subset = new RandomStarAlignmentPopulation(populationSize);
        returnValue.addAll(subset.generate());


//        System.out.println("Generating local aligned sequence");
//        Node createDistanceTree = TreeUtil.createDistanceTree();
//        List<char[]> alignment = createDistanceTree.getAlignment();
//        List<List<Character>> convertedAlignment = new ArrayList<List<Character>>();
//        for (int i=0;i<alignment.size();i++){
//            List<Character> sequence = new ArrayList<Character>();
//
//            for (int j=0;j<alignment.get(0).length;j++){
//                sequence.add(alignment.get(i)[j]);
//            }
//
//            convertedAlignment.add(sequence);
//        }
//
//        Chromossome chromossome = new BasicListChromossome(convertedAlignment);
//
//        returnValue.add(chromossome);

        return returnValue;
    }


}
