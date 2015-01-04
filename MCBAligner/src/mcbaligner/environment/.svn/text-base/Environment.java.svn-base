/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.environment;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jbio.alignment.Matrix;

import jbio.alignment.Sequence;
import jbio.io.FASTAReader;
import jbio.io.ScoreMatrixReader;

/**
 *
 * @author sergio
 */
public class Environment {

    private static Environment _instance = null;

    private List<Sequence> sequences = null;
    private Matrix matrixGlobal;

     private Environment() {
    }

    public static Environment getInstance() {
        if (_instance == null) {
            _instance = new Environment();
        }

        return _instance;
    }

     /**
     * @return the sequences
     */
    public List<Sequence> getSequences() {
        return sequences;
    }


    /**
     * Load sequences from a FASTA file
     *
     * @param sequenceFile FASTA file path
     */
     public void loadSequences(String sequenceFile) {
        try {
            sequences = FASTAReader.read(sequenceFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


      public void loadMatrixGlobal(String matrixName, String matrixFile) {
         try {
            matrixGlobal = ScoreMatrixReader.read(matrixName, matrixFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the matrixGlobal
     */
    public Matrix getMatrixGlobal() {
        return matrixGlobal;
    }

}
