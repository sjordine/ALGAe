/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jbio.io;

import SecondaryStructure.InvalidHorizFileException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import SecondaryStructure.ResidueStructure;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author sergio
 */
public class HorizReader {

    private enum FileState {

        NONE,
        COMMENT_LINE,
        BLANK_LINE,
        CONFIDENCE_LINE,
        PREDICTED_STRUCTURE_LINE,
        RESIDUE_LINE
    }
    private final static String COMMENT = "#";
    private final static String CONFIDENCE = "Conf:";
    private final static String PREDICTED_STRUCTURE = "Pred:";
    private final static String RESIDUE = "AA:";

    public static List<ResidueStructure> ReadFile(String filePath) throws FileNotFoundException, IOException, InvalidHorizFileException {


        List<ResidueStructure> returnValue = new ArrayList<ResidueStructure>();

        FileInputStream fstream = new FileInputStream(filePath);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader stream = new BufferedReader(new InputStreamReader(in));

        String line = null;
        StringBuffer buffer = null;
        String description = null;

        int lastPositionCompleted = -1;
        int lineNumber = 0;
        FileState state = FileState.NONE;
        FileState currentState = FileState.NONE;

        StringTokenizer lineParser = null;

        while ((line = stream.readLine()) != null) {


            lineNumber++;
            line = line.trim();
            state = checkLineType(line);

            //Ignore lines without data
            if (state!=FileState.BLANK_LINE && state!=FileState.COMMENT_LINE && state!=FileState.NONE){
                checkValidState(currentState, state);

                switch (state){
                    case CONFIDENCE_LINE:
                        lineParser = new StringTokenizer(line, ":");
                        if (lineParser.countTokens()==2){
                            lineParser.nextToken();
                            String confidenceList = lineParser.nextToken();
                            confidenceList = confidenceList.trim();
                            for (int i=0; i< confidenceList.length(); i++){
                                ResidueStructure structure = new ResidueStructure();
                                structure.setConfidence(confidenceList.charAt(i)-'0');
                                returnValue.add(structure);
                            }
                        } 
                        break;
                    case PREDICTED_STRUCTURE_LINE:
                        lineParser = new StringTokenizer(line, ":");
                        if (lineParser.countTokens()==2){
                            lineParser.nextToken();
                            String predictionList = lineParser.nextToken();
                            predictionList = predictionList.trim();
                            for (int i=0; i< predictionList.length(); i++){
                                ResidueStructure structure = returnValue.get(lastPositionCompleted + i +1);
                                structure.setStructure(predictionList.charAt(i));
                            }
                        } 
                        break;
                    case RESIDUE_LINE:
                        lineParser = new StringTokenizer(line, ":");
                        if (lineParser.countTokens()==2){
                            lineParser.nextToken();
                            String residueList = lineParser.nextToken();
                            residueList = residueList.trim();
                            for (int i=0; i< residueList.length(); i++){
                                ResidueStructure structure = returnValue.get(lastPositionCompleted + i +1);
                                structure.setResidue(residueList.charAt(i));
                            }
                            lastPositionCompleted += residueList.length();
                        }                        
                        break;
                }

                currentState = state;
            }

        }

        return returnValue;
    }

    private static  FileState checkLineType(String line) {

        FileState state = FileState.NONE;

        //Check relevant lines
        if (line.startsWith(COMMENT)) {
            state = FileState.COMMENT_LINE;
        }
        if (line.startsWith(CONFIDENCE)) {
            state = FileState.CONFIDENCE_LINE;
        }
        if (line.startsWith(PREDICTED_STRUCTURE)) {
            state = FileState.PREDICTED_STRUCTURE_LINE;
        }
        if (line.startsWith(RESIDUE)) {
            state = FileState.RESIDUE_LINE;
        }

        return state;
    }

    private static void checkValidState(FileState currentState, FileState newState) throws InvalidHorizFileException {
        boolean valid = false;

        switch (currentState) {
            case NONE:
                valid = (newState == FileState.CONFIDENCE_LINE);
                break;
            case CONFIDENCE_LINE:
                valid = (newState == FileState.PREDICTED_STRUCTURE_LINE);
                break;
            case PREDICTED_STRUCTURE_LINE:
                valid = (newState == FileState.RESIDUE_LINE);
                break;
            case RESIDUE_LINE:
                valid = (newState == FileState.CONFIDENCE_LINE);
                break;
        }


        if (!valid)
            throw new InvalidHorizFileException("Parse Error - Invalid Horiz file");
    }
}
