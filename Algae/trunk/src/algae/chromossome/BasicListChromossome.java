/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.chromossome;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergio
 */
public class BasicListChromossome implements Chromossome {

    private Double score;
    private ArrayList<ArrayList<Character>> data;
    private String origin;
    private String rootOrigin;

    public BasicListChromossome(ArrayList<ArrayList<Character>> data) {

        if (data != null) {

            this.data = new ArrayList<ArrayList<Character>>() {
            };

            for (int i = 0; i < data.size(); i++) {
                List<Character> currentSequence = data.get(i);
                int sequenceSize = currentSequence.size();
                ArrayList<Character> newSequence = new ArrayList<Character>();
                for (int j = 0; j < sequenceSize; j++) {
                    newSequence.add(currentSequence.get(j));
                }
                this.data.add(newSequence);
            }
        }
    }

    public BasicListChromossome(List<List<Character>> data) {

        if (data != null) {

            this.data = new ArrayList<ArrayList<Character>>() {
            };

            for (int i = 0; i < data.size(); i++) {
                List<Character> currentSequence = data.get(i);
                int sequenceSize = currentSequence.size();
                ArrayList<Character> newSequence = new ArrayList<Character>();
                for (int j = 0; j < sequenceSize; j++) {
                    newSequence.add(currentSequence.get(j));
                }
                this.data.add(newSequence);
            }
        }

        this.origin="NEW";
        this.rootOrigin="NEW";
    }

    public Double getScore() {
        return score;
    }

    public void setScore(double value) {
        score = value;
    }

    public char[][] getData() {
        char[][] returnValue = null;
        if (data != null && data.get(0) != null) {
            returnValue = new char[data.size()][data.get(0).size()];
            for (int i = 0; i < data.size(); i++) {
                List<Character> sequence = data.get(i);
                int seqSize = sequence.size();
                for (int j = 0; j < seqSize; j++) {
                    returnValue[i][j] = sequence.get(j);
                }
            }
        }

        return returnValue;
    }



    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public Object clone() {
        BasicListChromossome clone = new BasicListChromossome(data);

        clone.setRootOrigin(this.rootOrigin);
        clone.setOrigin("CLONE");
        if (this.getScore()!=null){
            clone.setScore(this.score);
        }

        return clone;
    }

    public void printData() {

         if (data!=null){
            for (int i=0;i<data.size();i++){
                for (int j=0;j<data.get(i).size();j++){
                    System.out.print(data.get(i).get(j));
                }
                System.out.println();
            }
        }
         
    }

    public ArrayList<ArrayList<Character>> getDataAsList() {
        return data;
    }

    public String getRootOrigin() {
        return rootOrigin;
    }

    public void setRootOrigin(String origin) {
        rootOrigin = origin;
    }
}
