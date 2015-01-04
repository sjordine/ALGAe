/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.chromossome;

/**
 *
 * @author sergio
 */
public interface Chromossome extends Cloneable {

    public Double getScore();
    public void setScore(double value);

    public char[][] getData();

    public String getOrigin();
    public void setOrigin(String origin);

    public String getRootOrigin();
    public void setRootOrigin(String origin);

    public void printData();

    public Object clone();


}
