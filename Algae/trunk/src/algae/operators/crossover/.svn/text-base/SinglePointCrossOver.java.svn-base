/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package algae.operators.crossover;

import algae.chromossome.BasicListChromossome;
import algae.chromossome.Chromossome;
import algae.util.alignment.SequenceUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author sergio
 */
public class SinglePointCrossOver implements CrossOverOperator{

    public List<Chromossome> execute(Chromossome c1, Chromossome c2) {

        List<Chromossome> returnValue = new ArrayList<Chromossome>();

        BasicListChromossome parent1 = (BasicListChromossome)c1.clone();
        BasicListChromossome parent2 = (BasicListChromossome)c2.clone();

        ArrayList<ArrayList<Character>> parent1Data =  parent1.getDataAsList();
        ArrayList<ArrayList<Character>> parent2Data =  parent2.getDataAsList();


        ArrayList<ArrayList<Character>> child1Data =  new ArrayList<ArrayList<Character>>();
        ArrayList<ArrayList<Character>> child2Data =  new ArrayList<ArrayList<Character>>();

        int[] indexes = new int[parent1Data.size()];
        int minIndex = Integer.MAX_VALUE;
        int maxIndex = Integer.MIN_VALUE;

        Random r = new Random();
        int cutPoint = 1 + r.nextInt(parent1Data.get(0).size() - 1);

         for (int i=0;i<parent1Data.size();i++){
            //Count letters on parent1
            int letterCount = countLetters(parent1Data.get(i),cutPoint-1);


            //Get position on parent 2
            int position = getPositionInSequence(parent2Data.get(i),letterCount);
            
            indexes[i] = position;

            if (position<minIndex){
                minIndex=position;
            }


            if ((position+1)>maxIndex){
                maxIndex = position +1;
            }
        }

        for (int i=0;i<parent1Data.size();i++){
            ArrayList<Character> child1Element = merge(parent1Data.get(i), parent2Data.get(i), cutPoint,indexes[i],indexes[i] - minIndex);
            child1Data.add(child1Element);
            ArrayList<Character> child2Element = merge(parent2Data.get(i), parent1Data.get(i), indexes[i],cutPoint,maxIndex-indexes[i]);
            child2Data.add(child2Element);
        }


        
        SequenceUtil.removeEmptyColumns(child1Data);
        SequenceUtil.removeEmptyColumns(child2Data);
        

        Chromossome child = new BasicListChromossome(child1Data);
        child.setRootOrigin("SP_CROV");
        child.setOrigin("SP_CROV");
        returnValue.add(child);
        child = new BasicListChromossome(child2Data);
        child.setRootOrigin("SP_CROV");
        child.setOrigin("SP_CROV");
        returnValue.add(child);
        
        return returnValue;
        
    }

    private int countLetters(List<Character> baseSequence, int index){

        int letterCount = 0;

        for (int i=index;i>=0;i--){
            if (baseSequence.get(i)!='-'){
                letterCount++;
            }
        }

        return letterCount;
    }

    private int getPositionInSequence(List<Character> comparedSequence, int letterIndex){

        int index=0;
        int i=0;


        for (i=0;i<comparedSequence.size();i++){            
            if (comparedSequence.get(i)!='-'){
                index++;
            }
            if (index>letterIndex) break;
        }

       
        return i;
    }  
 

    private ArrayList<Character> merge(List<Character> parent1, List<Character> parent2, int endPointParent1, int startPointParent2, int gapCount) {
      
        ArrayList<Character>  returnValue = new ArrayList<Character>();

        int parent2Size = parent2.size();


         List<Character> p1 = parent1.subList(0, Math.min(endPointParent1,parent1.size()));
         returnValue.addAll(p1);
        
        
        for (int i=0;i<gapCount;i++){
            returnValue.add('-');
        }
         

        
        List<Character> p2 = parent2.subList(startPointParent2, parent2Size);
        returnValue.addAll(p2);
        

        return returnValue;
        
    }


    

}
