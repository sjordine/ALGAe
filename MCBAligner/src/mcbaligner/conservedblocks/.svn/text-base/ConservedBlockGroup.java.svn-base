/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.conservedblocks;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergio
 */
public class ConservedBlockGroup {

    private List<List<ConservedBlockUnit>> conservedBlocks = null;

    public ConservedBlockGroup(int sequenceCount){
        conservedBlocks = new ArrayList<List<ConservedBlockUnit>>();
        for (int i=0; i<sequenceCount; i++){
            conservedBlocks.add(null);
        }        
    }

    public void add(int sequence, int startPosition, int endPosition)
    {
        List<ConservedBlockUnit> currentList = getConservedBlocks().get(sequence);

        if (currentList==null){

            currentList = new  ArrayList<ConservedBlockUnit>();
            getConservedBlocks().set(sequence, currentList);
            
        } 

        ConservedBlockUnit newBlock = new ConservedBlockUnit(sequence, startPosition, endPosition);
        currentList.add(newBlock);
    }

    public int numberOfSequences(){
        int count =0;

        for (int i=0;i<getConservedBlocks().size();i++){
            if (getConservedBlocks().get(i)!=null){
                count++;
            }
        }

        return count;
    }

    public void print(){
        for (int i=0;i<getConservedBlocks().size();i++){
            if (getConservedBlocks().get(i)!=null){
                System.out.println("seq "+i+" com "+getConservedBlocks().get(i).size());
            }               
        }
    }

    /**
     * @return the conservedBlocks
     */
    public List<List<ConservedBlockUnit>> getConservedBlocks() {
        return conservedBlocks;
    }

}
