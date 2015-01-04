/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jbio.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jbio.alignment.Sequence;


/**
 *
 * @author sergio
 */
public class MSFWriter {

    private static final int LINE_SIZE = 50;
    private static final int BLOCK_SIZE = 10;

    public static void create(String filePath, List<char[]> alignment, List<Sequence> sequences) throws IOException{

        int numberOfSequences = alignment.size();
        int alignmentSize = alignment.get(0).length;

        char[][] convertedAlignment = new char[numberOfSequences][alignmentSize];
        for (int i=0;i<numberOfSequences;i++){
            char[] sequence = alignment.get(i);
            System.arraycopy(sequence, 0, convertedAlignment[i], 0, alignmentSize);
        }

        create(filePath,convertedAlignment,sequences);

    }

    public static void create(String filePath, char[][] alignment, List<Sequence> sequences) throws IOException {
        FileWriter writer = null;
        BufferedWriter stream = null;
        //Get Sequence names


        //Get Alignmnet
        int alignmentLength = alignment[0].length;
        int sequenceNumber = alignment.length;

        //Number of alignment lines
        int alignmentLines = alignmentLength / LINE_SIZE;
        alignmentLines += (alignmentLength % LINE_SIZE != 0) ? 1 : 0;

        //Max Sequence name length
        int maxNameLength = 0;
        for (Sequence s : sequences) {
            int length = s.getDescription().length();
            if (length > maxNameLength) {
                maxNameLength = length;
            }
        }

        //ProcessFile
        try{

            writer = new FileWriter(filePath);
            stream = new BufferedWriter(writer);

            //Header

            stream.write("Pile Up");
            stream.newLine();
            stream.newLine();

            stream.write(" MSF: " + alignmentLength);
            stream.write(" Type: " + "P");
            stream.write(" Check: " + "0");
            stream.write(" " + "..");
            stream.newLine();
            stream.newLine();

            //Sequences list
            for (Sequence s : sequences) {
                stream.write(" Name: " + filledName(s.getDescription(), maxNameLength) + " ");
                stream.write(" Len: " + alignmentLength + " ");
                stream.write("\t");
                stream.write(" Check: " + "0");
                stream.write(" Weight: " + "0");
                stream.newLine();
            }

            stream.newLine();
            stream.write("//");
            stream.newLine();
            stream.newLine();

            //Alignment
            int index = 0;
            for (int i = 0; i < alignmentLines; i++) {
                for (int j = 0; j < sequenceNumber; j++) {
                    stream.write(filledName(sequences.get(j).getDescription(), maxNameLength) + " ");
                    stream.write(" ");
                    for (int k = 0; k < LINE_SIZE && index + k < alignmentLength; k++) {
                        stream.write(alignment[j][index+k]);
                        if (k % BLOCK_SIZE == 0) {
                            stream.write(" ");
                        }
                    }
                    stream.newLine();
                }
                stream.newLine();
                index += LINE_SIZE;
            }

        }
        finally {
            try {
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(MSFWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static String filledName(String sequenceName, int maxNameLength) {
        StringBuilder buffer = new StringBuilder();

        buffer.append(sequenceName);

        int sequenceNameLenght = sequenceName.length();

        for (int i = 0; i < maxNameLength - sequenceNameLenght; i++) {
            buffer.append(" ");
        }


        return buffer.toString();
    }
}
