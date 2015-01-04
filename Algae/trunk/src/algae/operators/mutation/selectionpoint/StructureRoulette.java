/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.operators.mutation.selectionpoint;

import SecondaryStructure.ResidueStructure;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergio
 */
public abstract class StructureRoulette {

    List<Double> accProbabilities = null;

    /**
     * Prepare a selection roulette based on the given structure
     *
     * @param structure protein structure
     */
    public void prepareRoulette(List<ResidueStructure> structure) {
        //Calculate accumulated probabilities
        double totalValue = calculateAccumulatedValue(structure);
        //Prepare roulette
        accProbabilities = calculateAccumulatedProbability(structure, totalValue);

        System.out.println("Roulette for structure "+structure.size() +" roulette "+accProbabilities.size());

    }

    /**
     * Get the selected index within the sequence based on a given probability
     *
     * @param selectedProbability Accumulated probability (range:0 to 1)
     * 
     * @return index of the selected residue within the structure
     */
    public int getSelectedIndex(double selectedProbability) {
        int startIndex = 0;

        if (accProbabilities != null) {


            int lastIndex = accProbabilities.size() - 1;

            while (startIndex < lastIndex) {
                int middleIndex = (startIndex + lastIndex) / 2;
                if (accProbabilities.get(middleIndex) == selectedProbability) {
                    return middleIndex;
                }
                if (accProbabilities.get(middleIndex) < selectedProbability) {
                    startIndex = middleIndex + 1;
                } else {
                    lastIndex = middleIndex - 1;
                }
            }
        }

        return startIndex;
    }

    private List<Double> calculateAccumulatedProbability(List<ResidueStructure> structure, double totalValue) {

        List<Double> returnValue = new ArrayList<Double>();
        double accProbability = 0.0f;

        for (ResidueStructure residue : structure) {
            double currentScore = score(residue);
            accProbability += (currentScore / totalValue);
            returnValue.add(accProbability);
        }

        return returnValue;


    }

    private double calculateAccumulatedValue(List<ResidueStructure> structure) {

        double returnValue = 0.0;

        for (ResidueStructure residue : structure) {
            returnValue += score(residue);
        }

        return returnValue;
    }

    protected abstract double score(ResidueStructure residue);

    public int size() {
        return accProbabilities.size();
    }
}
