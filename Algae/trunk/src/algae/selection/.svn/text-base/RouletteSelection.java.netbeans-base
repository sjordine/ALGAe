/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.selection;

import algae.chromossome.Chromossome;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author sergio
 */
public class RouletteSelection implements Selection {


    public List<Chromossome> select(List<Chromossome> population,int selectionSize){
        List<Chromossome> returnValue = new ArrayList<Chromossome>();
        List<Double> accProbability = null;

        accProbability = calculateAccumulatedProbability(population);

        Random random = new Random();
        
        for (int i=0;i<selectionSize;i++){
            double selectedProbability = random.nextDouble();
            //Get element
            int selectedIndex = getSelectedChromossomeIndex(selectedProbability,accProbability,population);
            returnValue.add(population.get(selectedIndex));
        }

        return returnValue;
    }

    private List<Double> calculateAccumulatedProbability(List<Chromossome> population) {
        List<Double> returnValue = new ArrayList<Double>();
        int populationSize = population.size();
        double totalValue = 0.0f;
        double accProbability = 0.0f;
        double worstScore = Double.MAX_VALUE;


        for (Chromossome c: population){
            double currScore = c.getScore();

            if (currScore < worstScore){
                worstScore = currScore;
            }
        }

        for (Chromossome c: population){
            totalValue += calculateDistance(worstScore,c.getScore());
        }


        for (int i=0;i<populationSize;i++){
            double currScore = population.get(i).getScore();
            accProbability  += (calculateDistance(worstScore, currScore)/totalValue);
            returnValue.add(accProbability);
        }



        return returnValue;
    }

    private int getSelectedChromossomeIndex(double selectedProbability, List<Double> accProbability, List<Chromossome> population) {
       int startIndex = 0;
       int lastIndex = population.size()-1;

       while (startIndex<lastIndex){
           int middleIndex = (startIndex + lastIndex) /2;
           if (accProbability.get(middleIndex) == selectedProbability){
               return middleIndex;
           }
           if (accProbability.get(middleIndex) < selectedProbability){
                startIndex = middleIndex+1;
           } else {
               lastIndex = middleIndex-1;
           }
       }

       return startIndex;

    }

    private Double calculateDistance(double worstCase, double currValue){
        return currValue - worstCase;
    }


}
