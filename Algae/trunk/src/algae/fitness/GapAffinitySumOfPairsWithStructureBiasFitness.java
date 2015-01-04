/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.fitness;


import algae.alignment.Matrix;
import algae.chromossome.Chromossome;
import algae.environment.Environment;
import algae.score.GASPStructureArithmetic;
import algae.score.GASPStructureGeometric;
import algae.score.GASPStructureHarmonic;
import algae.score.GapAffinitySumOfPairsStructureScore;

/**
 *
 * @author sergio
 */
public class GapAffinitySumOfPairsWithStructureBiasFitness  extends FitnessFunction {

    GapAffinitySumOfPairsStructureScore score = null;


     public GapAffinitySumOfPairsWithStructureBiasFitness(){

        Environment environment = Environment.getInstance();
        Matrix matrix = environment.getMatrix();

        String type = environment.getMean();

        if ("arithmetic".equals(type)){
            score = new GASPStructureArithmetic(matrix);
        }

         if ("geometric".equals(type)){
            score = new GASPStructureGeometric(matrix);
        }

         if ("harmonic".equals(type)){
            score = new GASPStructureHarmonic(matrix);
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
