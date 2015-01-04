/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.fitness;

import algae.alignment.Matrix;
import algae.chromossome.Chromossome;
import algae.environment.Environment;
import algae.score.SumOfPairsMatrixBasedScore;

/**
 *
 * @author sergio
 */
public class BasicSumOfPairsFitness extends FitnessFunction {

    SumOfPairsMatrixBasedScore score = null;


    public BasicSumOfPairsFitness(){
        
        Environment environment = Environment.getInstance();
        Matrix matrix = environment.getMatrix();
        score = new SumOfPairsMatrixBasedScore(matrix);
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
