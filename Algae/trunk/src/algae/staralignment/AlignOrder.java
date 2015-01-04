/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.staralignment;

/**
 *
 * @author sergio
 */
public interface AlignOrder {

    public AlignPair getNext();

    public boolean hasSequences();

}
