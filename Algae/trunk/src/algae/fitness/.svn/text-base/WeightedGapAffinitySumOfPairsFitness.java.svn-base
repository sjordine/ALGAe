/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.fitness;

import algae.alignment.Matrix;
import algae.chromossome.Chromossome;
import algae.environment.Environment;
import algae.score.WeightedGapAffinitySumOfPairsMatrixBasedScore;

/**
 *
 * @author sergio
 */
public class WeightedGapAffinitySumOfPairsFitness extends FitnessFunction {

    WeightedGapAffinitySumOfPairsMatrixBasedScore score = null;


    public WeightedGapAffinitySumOfPairsFitness(){

        Environment environment = Environment.getInstance();
        Matrix matrix = environment.getMatrix();
        score = new WeightedGapAffinitySumOfPairsMatrixBasedScore(matrix);
    }

    @Override
    public double fitnessFunction(Chromossome individual) {
        try{
            return score.calculateScore(individual);
        }catch (RuntimeException ex){
            System.out.println("ERRO");
            individual.printData();
            System.out.println("Origin->"+individual.getOrigin());
            throw ex;
        }
    }
}


