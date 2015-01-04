/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.operators.mutation.selectionpoint;

import SecondaryStructure.ResidueStructure;

/**
 *
 * @author sergio
 */
public class SimpleStructureRoulette extends StructureRoulette {

    @Override
    protected double score(ResidueStructure residue) {
        double value = 0.0;

        value = 10.0 - residue.getConfidence();

        if (residue.getStructure() == ResidueStructure.COIL){
            value *= 2.0;
        }

        return value;
    }
}
