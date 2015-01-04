/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.util.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import algae.alignment.Sequence;

/**
 *
 * @author sergio
 */
public class FASTAReader {

    private static final char DESCRIPTION_LINE_TAG = '>';

    /**
     * Read a multi-FASTA file and return its sequences
     * 
     * @param filepath file on FASTA format
     * @return list of sequences defined on this file
     * 
     * @throws FileNotFoundException Input file was not found
     * @throws IOException Error reading file
     */
    public static List<Sequence> read(String filepath) throws FileNotFoundException, IOException {

        List<Sequence> returnValue = new ArrayList<Sequence>();


        FileInputStream fstream = new FileInputStream(filepath);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader stream = new BufferedReader(new InputStreamReader(in));

        String line = null;
        StringBuffer buffer = null;
        String description = null;

        while ((line = stream.readLine()) != null) {
            //Process line
            if (line.length() > 0) {
                if (line.length() > 0 && line.charAt(0) == DESCRIPTION_LINE_TAG) {
                    //Add previous sequence to list(if any)
                    if (buffer != null) {
                        Sequence newSeq = new Sequence(description, buffer.toString());
                        returnValue.add(newSeq);
                    }
                    //Start new sequence handling
                    description = line.substring(1, line.length());
                    buffer = null;
                } else {
                    //Add line to current buffer
                    if (buffer == null) {
                        buffer = new StringBuffer();
                    }

                    buffer.append(line);
                }
            }
        }

        if (buffer != null) {
            //Add last sequence to list
            Sequence newSeq = new Sequence(description, buffer.toString());
            returnValue.add(newSeq);
        }


        return returnValue;
    }
}
