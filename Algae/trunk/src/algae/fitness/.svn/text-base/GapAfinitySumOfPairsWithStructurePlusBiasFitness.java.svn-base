/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.fitness;

import algae.alignment.Matrix;
import algae.chromossome.Chromossome;
import algae.environment.Environment;
import algae.score.GapAffinitySumOfPairsStructureSurplusScore;
import algae.score.GapStructureSurplusArithmetic;
import algae.score.GapStructureSurplusGeometric;
import algae.score.GapStructureSurplusHarmonic;

/**
 *
 * @author sergio
 */
public class GapAfinitySumOfPairsWithStructurePlusBiasFitness extends FitnessFunction {

    GapAffinitySumOfPairsStructureSurplusScore score = null;


     public GapAfinitySumOfPairsWithStructurePlusBiasFitness(){

        Environment environment = Environment.getInstance();
        Matrix matrix = environment.getMatrix();

        String type = environment.getMean();

        if ("arithmetic".equals(type)){
            score = new GapStructureSurplusArithmetic(matrix);
        }

         if ("geometric".equals(type)){
            score = new GapStructureSurplusGeometric(matrix);
        }

         if ("harmonic".equals(type)){
            score = new GapStructureSurplusHarmonic(matrix);
        }



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