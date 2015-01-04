/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package craligner;

import craligner.environment.Environment;
import craligner.tree.Node;
import craligner.tree.TreeUtil;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jbio.alignment.Sequence;
import jbio.io.MSFWriter;

/**
 *
 * @author sergio
 */
public class Main {

    private static final String inputPath = System.getenv("HOME")+"/Development/BAliBASE3.0/bb3_release/";
    private static final String resultPath = System.getenv("HOME")+"/Development/results/";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String inputFile = "RV11/BB11002.tfa";
        String alignmentId = "BB11002_SP_20_B62";

        if (args.length >= 2){
               inputFile=args[0];
               alignmentId=args[1];
        }

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        String testSetId = String.format("%s_%02d%02d%02d%02d%02d%02d", alignmentId, (year % 100), month + 1, day, hour, minutes, seconds);

        //Create folder
        File testFolder = new File(resultPath + "/" + testSetId);
        testFolder.mkdirs();


        //Prepare environment
        Environment environment = Environment.getInstance();

        System.out.println("Loading Sequences");
        environment.loadSequences(inputPath+"/"+inputFile);
        System.out.println("Loading Matrix");
        if (args.length<3){
            environment.loadMatrixGlobal(null, System.getenv("HOME")+"/Development/matrizes/BLOSUM62.txt");
        } else {
            System.out.println("Loading matrix "+args[2]);
            environment.loadMatrixGlobal(null, args[2]);
        }

         if (args.length<4){
            environment.loadMatrixLocal(null, System.getenv("HOME")+"/Development/matrizes/BLOSUM80.txt");
        } else {
            System.out.println("Loading local matrix "+args[3]);
            environment.loadMatrixLocal(null, args[3]);
        }

     
        //Find all local alignments
        List<Sequence> sequences = environment.getSequences();

      

        environment.loadDistanceMatrix(sequences);
        Node createDistanceTree = TreeUtil.createDistanceTree();
        createDistanceTree.print();
        System.out.println();
        List<char[]> alignment = createDistanceTree.getAlignment();
        try {
            MSFWriter.create(testFolder+"/"+alignmentId+".msf", alignment, sequences);
        } catch (IOException ex) {
            System.err.println("Error writing output file");
        }

    }
}
