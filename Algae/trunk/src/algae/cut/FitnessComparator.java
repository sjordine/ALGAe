/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.cut;

import algae.chromossome.Chromossome;
import java.util.Comparator;

/**
 *
 * @author sergio
 */
public class FitnessComparator implements Comparator<Chromossome> {

    public int compare(Chromossome c1, Chromossome c2) {
        Double score1=null;
        Double score2=null;

        if (c1!=null){
            score1=c1.getScore();
        }

        if (c2!=null){
            score2=c2.getScore();
        }

        //Descending order - multiply by -1
        return Double.compare(score1, score2) * -1;
    }

}
