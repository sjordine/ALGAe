/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.initialpopulation;

import algae.alignment.Sequence;
import algae.chromossome.BasicListChromossome;
import algae.chromossome.Chromossome;
import algae.environment.Environment;
import craligner.tree.Node;
import craligner.tree.TreeUtil;
import java.util.*;
import msa.aligner.blocks.*;
import msa.definitions.AlignerInterface;
import msa.definitions.ImproverInterface;
import msa.methods.refinement.BadColumnsRebuilder;
import msa.methods.refinement.BadColumnsRebuilder04;
import msa.methods.refinement.BadColumnsRebuilder06;
import msa.methods.sequenceWeight.BasicSequenceWeight;
import msa.util.AlignmentUtil;

/**
 *
 * @author sergio
 */
public class PopulationCR3 implements InitialPopulation {

    private static String MSG_ALIGNER_ERROR = "Occur an error on alignment process.";
    private int populationSize = 0;

    public PopulationCR3(Integer size) {
        populationSize = size;
    }

    public ArrayList<Chromossome> generate() {

        System.out.println("Initial Population ");

        ArrayList<Chromossome> returnValue = new ArrayList<Chromossome>();


        // Primeiro algoritmo
        InitialPopulation subset = new BasicStarAlignmentPopulation(populationSize);
        returnValue.addAll(subset.generate());


        // Segundo algoritmo
        subset = new RandomStarAlignmentPopulation(populationSize);
        returnValue.addAll(subset.generate());


        // Terceiro algoritmo
        System.out.println("Generating local aligned sequence");
        Node createDistanceTree = TreeUtil.createDistanceTree();
        List<char[]> alignment = createDistanceTree.getAlignment();
        List<List<Character>> convertedAlignment = new ArrayList<List<Character>>();
        for (int i = 0; i < alignment.size(); i++) {
            List<Character> sequence = new ArrayList<Character>();
            for (int j = 0; j < alignment.get(0).length; j++) {
                sequence.add(alignment.get(i)[j]);
            }
            convertedAlignment.add(sequence);
        }
        Chromossome chromossome = new BasicListChromossome(convertedAlignment);
        returnValue.add(chromossome);

        // Gerando entrada para algoritmos desenvolvidos pelo André
        Environment environment = Environment.getInstance();
        List<Sequence> sequencesAlgae = environment.getSequences();
        String[][] sequences = new String[sequencesAlgae.size()][2];
        int curSeq = 0;
        Iterator<Sequence> itSequencesAlgae = sequencesAlgae.iterator();
        while (itSequencesAlgae.hasNext()) {
            Sequence sequence = itSequencesAlgae.next();
            sequences[curSeq][0] = sequence.getDescription();
            sequences[curSeq][1] = sequence.getData();
            curSeq++;
        }


        // Adicionando chromossomos gerados por alinhadores de bloco implementados pelo André
        String[][] originalMsa = null;
        String[][] msa = null;
        String[][] adjustedMsa = null;
        try {
            double[] weight = BasicSequenceWeight.getInstance().weightComputation(sequences, null, null);

            AlignerInterface alignerBA1 = new BlockAligner1();
            AlignerInterface alignerBA2 = new BlockAligner2();
            AlignerInterface alignerBA3 = new BlockAligner3();
            AlignerInterface alignerBA4 = new BlockAligner4();
            AlignerInterface alignerBA5 = new BlockAligner5();

            ImproverInterface improver04 = new BadColumnsRebuilder04();
            ImproverInterface improver05 = new BadColumnsRebuilder();
            ImproverInterface improver06 = new BadColumnsRebuilder06();

            /*
             * Alinhador BA1 e refinos
             */
            originalMsa = alignerBA1.align(sequences);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(originalMsa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver04.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver05.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver06.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            /*
             * Alinhador BA2 e refinos
             */
            originalMsa = alignerBA2.align(sequences);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(originalMsa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver04.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver05.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver06.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            /*
             * Alinhador BA3 e refinos
             */
            originalMsa = alignerBA3.align(sequences);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(originalMsa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver04.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver05.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver06.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            /*
             * Alinhador BA4 e refinos
             */
            originalMsa = alignerBA4.align(sequences);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(originalMsa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver04.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver05.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver06.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            /*
             * Alinhador BA5 e refinos
             */
            originalMsa = alignerBA5.align(sequences);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(originalMsa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver04.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver05.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

            msa = improver06.refine(null, originalMsa, sequences, weight);
            adjustedMsa = AlignmentUtil.adjustSequenceNames(msa, sequences);
            returnValue.add(convertAligment(adjustedMsa));

        } catch (Exception e) {
            AlignmentUtil.printErrorMessage(MSG_ALIGNER_ERROR);
            e.printStackTrace();
            System.exit(1);
        }

        return returnValue;
    }

    private Chromossome convertAligment(String[][] alignment) {
        List<List<Character>> convertedAlignment = new ArrayList<List<Character>>();
        Map<String, List<Character>> map = new HashMap<String, List<Character>>();
        for (int i = 0; i < alignment.length; i++) {
            List<Character> sequence = new ArrayList<Character>();
            for (int j = 0; j < alignment[i][1].length(); j++) {
                sequence.add(alignment[i][1].charAt(j));
            }
            map.put(alignment[i][0], sequence);
        }

        Environment env = Environment.getInstance();
        List<Sequence> sequences = env.getSequences();
        for (int i = 0; i < sequences.size(); i++) {
            String seqName = sequences.get(i).getDescription();
            convertedAlignment.add(map.get(seqName));
        }

        return new BasicListChromossome(convertedAlignment);
    }
}
