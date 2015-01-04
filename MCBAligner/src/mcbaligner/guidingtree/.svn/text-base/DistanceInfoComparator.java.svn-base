/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.guidingtree;

import java.util.Comparator;

/**
 *
 * @author sergio
 */
public class DistanceInfoComparator implements Comparator<DistanceInfo>{

    public int compare(DistanceInfo o1, DistanceInfo o2) {
        int returnValue = -1;

        if (o1 != null && o2 != null) {
            returnValue = ((Integer) o1.getScore()).compareTo(o2.getScore());
        } else {
            if (o2 == null && o1 != null) {
                returnValue = 1;
            } else {
                return 0;
            }
        }


        //The distances must be descendent ordered.
        return -1*returnValue;
    }

}
