/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jbio.alignment;


/**
 *
 * @author sergio
 */
public class Sequence {

    private String description;
    private String data;
    private char[] dataArray = null;


    public Sequence()
    {
    }

    public Sequence (String description,String data){
        setDescription(description);
        setData(data);
    }


    /**
     * Get the Sequence description
     *
     * @return  sequence description
     */
    public String getDescription() {
        return description;
    }

    /**
     *  Set the Sequence description
     *
     * @param description  sequence description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the sequence data (sequence of characters)
     *
     * @return sequence data (sequence of characters)
     */
    public String getData() {
        return data;
    }

    public char[] getDataArray(){
        return dataArray;
    }

    /**
     *  Get the sequence data (sequence of characters)
     *
     * @param data sequence data (sequence of characters)
     */
    public void setData(String data) {
        this.data = data;
        //Populate data as a char array
        if (this.data!=null){
            int size = data.length();
            dataArray = new char[size];
            for (int i=0;i<size;i++){
                dataArray[i] = data.charAt(i);
            }
        }
    }


}

