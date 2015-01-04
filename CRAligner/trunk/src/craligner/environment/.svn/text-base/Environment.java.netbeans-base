/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package craligner.environment;

import craligner.msa.AlignmentInfo;
import craligner.msa.AlignmentMatrix;
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
    private Matrix matrixLocal = null;
    private Matrix matrixGlobal = null;
    private AlignmentMatrix distanceMatrix = null;


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
     * @return the matrixLocal
     */
    public Matrix getMatrixLocal() {
        return matrixLocal;
    }

    /**
     * @return the matrixGlobal
     */
    public Matrix getMatrixGlobal() {
        return matrixGlobal;
    }

    public void loadSequences(String sequenceFile) {
        try {
            sequences = FASTAReader.read(sequenceFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Environment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


     public void loadMatrixLocal(String matrixName, String matrixFile) {
        try {
            matrixLocal = ScoreMatrixReader.read(matrixName, matrixFile);
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

    public void loadDistanceMatrix(List<Sequence> sequences){
        distanceMatrix = new AlignmentMatrix();
        distanceMatrix.generateMatrix(sequences);
    }

    public AlignmentInfo getDistanceInfo(int seq1,int seq2){
        AlignmentInfo returnValue = null;

        if (distanceMatrix!=null && distanceMatrix.getMatrix()!=null){
            AlignmentInfo[][] matrix = distanceMatrix.getMatrix();
            if (seq1 < matrix.length && seq2  < matrix[0].length){
                returnValue = matrix[seq1][seq2];
            }
            
        }

        return returnValue;
    }


}
