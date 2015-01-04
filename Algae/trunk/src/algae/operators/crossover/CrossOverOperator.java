/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.operators.crossover;

import algae.chromossome.Chromossome;
import java.util.List;

/**
 *
 * @author sergio
 */
public interface CrossOverOperator {

    public List<Chromossome> execute(Chromossome c1,Chromossome c2);

}
