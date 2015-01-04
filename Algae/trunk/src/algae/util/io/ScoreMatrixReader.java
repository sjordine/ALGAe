/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.util.io;

import algae.alignment.Matrix;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 *
 * @author sergio
 */
public class ScoreMatrixReader {

    private static final char COMMENT_LINE = '#';
    private static final String SEPARATOR = " ";

    private enum READ_STATE {

        BEFORE_START,
        COMMENT_SECTION_READING,
        HEADER_LINE_READING,
        SCORE_READING,
    }

    public static Matrix read(String name, String filepath) throws FileNotFoundException, IOException {

        Matrix returnValue = null;


        FileInputStream fstream = new FileInputStream(filepath);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader stream = new BufferedReader(new InputStreamReader(in));

        String line = null;
        StringBuffer buffer = null;
        String description = null;

        int maxLines = 0;
        int lineCount = 0;

        READ_STATE state = READ_STATE.BEFORE_START;

        while ((line = stream.readLine()) != null) {
            //Process line
            if (line.length() > 0) {
                switch (state) {
                    case BEFORE_START:
                        if (line.charAt(0) == COMMENT_LINE) {
                            state = READ_STATE.COMMENT_SECTION_READING;
                        } else {
                            state = READ_STATE.HEADER_LINE_READING;
                        }
                        break;
                    case COMMENT_SECTION_READING:
                        if (line.charAt(0) != COMMENT_LINE) {
                            state = READ_STATE.HEADER_LINE_READING;
                            //Analyze matrix size
                            StringTokenizer tokenizer = new StringTokenizer(line, SEPARATOR);
                            int elements = tokenizer.countTokens();
                            maxLines = elements;
                            returnValue = new Matrix();
                            returnValue.setName(name);
                            returnValue.setScores(new int[elements][elements]);
                            state = READ_STATE.SCORE_READING;
                        }
                        break;
                    case HEADER_LINE_READING:
                        break;
                    case SCORE_READING:
                        if (lineCount < maxLines) {
                            SetScore(returnValue, lineCount, line);
                            lineCount++;
                        } else {
                            //Error?
                        }
                        break;

                }
            }
        }

        return returnValue;
    }


     private static void SetScore(Matrix scoreMatrix, int lineCount, String line)
        {
            if (scoreMatrix != null && scoreMatrix.getScores() != null)
            {
                int[][] scores = scoreMatrix.getScores();

                if (lineCount < scores.length)
                {
                    StringTokenizer elements = new StringTokenizer(line,SEPARATOR);
                    //Remove the row related character
                    elements.nextToken();
                    int i= 0;
                    while (elements.hasMoreElements()){
                        scores[lineCount][i] = Integer.parseInt(elements.nextToken());
                        i++;
                    }                 

                }
            }
        }
}
