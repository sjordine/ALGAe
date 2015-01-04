/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package craligner.tree;

import craligner.msa.AlignmentInfo;
import java.util.Comparator;

/**
 *
 * @author sergio
 */
public class AlignmentInfoComparator implements Comparator<AlignmentInfo> {

    public int compare(AlignmentInfo o1, AlignmentInfo o2) {
        int returnValue = -1;

        if (o1 != null && o2 != null) {
            returnValue = ((Double) o1.getScore()).compareTo(o2.getScore());
        } else {
            if (o2 == null && o1 != null) {
                returnValue = 1;
            } else {
                return 0;
            }
        }



        return -1*returnValue;
    }
}
