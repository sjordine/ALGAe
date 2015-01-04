/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jbio.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import jbio.alignment.MultipleSequenceAlignment;

/**
 *
 * @author sergio
 */
public class MSFReader {

    private static final int BEFORE_ALIGNMENT = 0;
    private static final int IN_ALIGNMENT = 1;

    public static MultipleSequenceAlignment read(String filepath) throws FileNotFoundException, IOException {
        MultipleSequenceAlignment returnValue = new MultipleSequenceAlignment();
        HashMap<String, List<Character>> sequences = new HashMap<String, List<Character>>();


        FileInputStream fstream = new FileInputStream(filepath);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader stream = new BufferedReader(new InputStreamReader(in));

        int currentState = BEFORE_ALIGNMENT;

        //Ignoring everithing up to the alignment by now...
        String line = null;

        while ((line = stream.readLine()) != null) {
            if (currentState == IN_ALIGNMENT) {
                StringTokenizer tokenizer = new StringTokenizer(line, " ");
                int count = 0;
                String token = null;
                String sequenceName = null;
                StringBuilder sequenceStr = new StringBuilder();

                if (tokenizer.countTokens() > 0) {
                    while (tokenizer.hasMoreTokens()) {
                        token = tokenizer.nextToken();
                        if (count == 0) {
                            sequenceName = token;
                        } else {
                            sequenceStr.append(token);
                        }

                        count++;
                    }


                    List<Character> currentSequence = null;

                    if (sequences.containsKey(sequenceName)) {
                        currentSequence = sequences.get(sequenceName);
                    } else {
                        currentSequence = new ArrayList<Character>();
                        sequences.put(sequenceName, currentSequence);
                    }

                    addToSequence(currentSequence, sequenceStr);

                }

            } else {
                if (line.contains("//")) {
                    currentState = IN_ALIGNMENT;
                }
            }



        }

        List<String> sequenceList = new ArrayList<String>();
        List<List<Character>> alignment = new ArrayList<List<Character>>();


        Set<String> sequenceNames = sequences.keySet();

        for (String sequence : sequenceNames) {
            sequenceList.add(sequence);
        }

        for (int i = 0; i < sequenceList.size(); i++) {
            List<Character> alignedSequence = sequences.get(sequenceList.get(i));
            alignment.add(alignedSequence);
        }

        returnValue.setSequences(sequenceList);
        returnValue.setAlignment(alignment);

        return returnValue;
    }

    private static void addToSequence(List<Character> currentSequence, StringBuilder sequenceStr) {

        for (int i = 0; i < sequenceStr.length(); i++) {
            char currentChar = sequenceStr.charAt(i);

            if (isSpace(currentChar)) {
                currentChar = '-';
            }

            currentSequence.add(currentChar);
        }
    }

    private static boolean isSpace(char currentChar) {
        return (currentChar == '-' || currentChar == '.' || currentChar == '~');
    }
}
