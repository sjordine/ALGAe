/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mcbaligner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import jbio.alignment.Sequence;
import jbio.alignment.compressedalphabet.Dayhoff6;
import jbio.io.MSFWriter;
import mcbaligner.aligner.TreeBasedAligner;
import mcbaligner.aligner.TreeBasedProfileAligner;

import mcbaligner.conservedblocks.ConservedBlockGroup;
import mcbaligner.conservedblocks.MSCompressedAlphabetConservedBlockFinder;
import mcbaligner.conservedblocks.MSConservedBlockFinder;
import mcbaligner.conservedblocks.graph.pairbasedgraph.PairBasedGraphNode;
import mcbaligner.conservedblocks.graph.pairbasedgraph.PairBasedPathUtil;
import mcbaligner.conservedblocks.graph.pairbasedgraph.TriageUtil;
import mcbaligner.conservedblocks.graph.superblocks.SuperBlockUtil;

import mcbaligner.environment.Environment;
import mcbaligner.guidingtree.Node;
import mcbaligner.guidingtree.TreeUtil;

/**
 *
 * @author sergio
 */
public class MCBAlign {

    private static final String inputPath = "/home/sergio/Development/BAliBASE3.0/bb3_release/";
    private static final String resultPath = "/home/sergio/Development/results/";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

       String inputFile = "RV20/BB20032.tfa";
       String alignmentId = "BB20032_TEST";


        /**
        String inputFile = "RV20/BB20014.tfa";
        String alignmentId = "BB20014_TEST";

         */

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
        environment.loadSequences(inputPath + "/" + inputFile);

        List<Sequence> sequences = environment.getSequences();

        System.out.println("Sequences = " + sequences.size());

        System.out.println("Loading Matrix");
         if (args.length<3){
            environment.loadMatrixGlobal(null, "/home/sergio/Development/matrizes/BLOSUM62.txt");
        } else {
            System.out.println("Loading matrix "+args[2]);
            environment.loadMatrixGlobal(null, args[2]);
        }


        List<List<Character>> alignment = align();

        List<char[]> alignmentOut = new ArrayList<char[]>();

        for (int i=0; i< alignment.size(); i++){
            List<Character> currentSeq = alignment.get(i);
            char[] convertedAlignment = new char[currentSeq.size()];
            for (int j=0; j<currentSeq.size(); j++){
                convertedAlignment[j] = currentSeq.get(j);
            }
            alignmentOut.add(convertedAlignment);            
        }



         try {
            MSFWriter.create(testFolder+"/"+alignmentId+".msf", alignmentOut, sequences);
        } catch (IOException ex) {
            System.err.println("Error writing output file");
        }

    }

    public static List<List<Character>>  align() throws Exception {

        Environment environment = Environment.getInstance();

        List<Sequence> sequences = environment.getSequences();

        //HashMap<String, ConservedBlockGroup> blocks = MSConservedBlockFinder.findBlocks(sequences);

        HashMap<String, ConservedBlockGroup> blocks = MSCompressedAlphabetConservedBlockFinder.findBlocks(sequences, new Dayhoff6());

        Object[][] graphs = TriageUtil.mountGraphs(blocks, sequences.size());

        Node rootNode = TreeUtil.mountTree(graphs, sequences);

        List<List<Character>> alignment = TreeBasedAligner.align(rootNode, graphs);
        //List<List<Character>> alignment = TreeBasedProfileAligner.align(rootNode,graphs);

        return alignment;

    }
}
