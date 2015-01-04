/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package craligner.conservedregion;

import java.util.Comparator;

/**
 *
 * @author sergio
 */
public class SubAlignmentComparator implements Comparator<SubAlignment>{

    public int compare(SubAlignment subAlignment1, SubAlignment subAlignment2) {
        int result = -1 ;

        if (subAlignment1 == null || subAlignment2==null){
            //TODO throws a runtime exception
        } else {
            int startPos1 = subAlignment1.getStartSequence1();
            int startPos2 = subAlignment2.getStartSequence1();
            
            result = (startPos1 == startPos2)? 0 : (startPos1 > startPos2)? 1: -1;
        }

        return result;

    }

}
