/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.fitness;

import algae.alignment.Matrix;
import algae.chromossome.Chromossome;
import algae.environment.Environment;
import algae.score.GapAffinitySumOfPairsMatrixBasedScore;


/**
 *
 * @author sergio
 */
public class GapAffinitySumOfPairsFitness  extends FitnessFunction {
 GapAffinitySumOfPairsMatrixBasedScore score = null;


    public GapAffinitySumOfPairsFitness(){

        Environment environment = Environment.getInstance();
        Matrix matrix = environment.getMatrix();
        score = new GapAffinitySumOfPairsMatrixBasedScore(matrix);
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
