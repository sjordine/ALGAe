/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algae.philogeny.matrix;

import algae.alignment.GlobalAlignment;
import algae.alignment.Matrix;
import algae.alignment.PairWiseAlignment;
import algae.alignment.SemiGlobalAlignment;
import algae.alignment.Sequence;
import java.util.List;

/**
 *
 * @author sergio
 */
public class DistanceMatrix {

    private PairwiseDistance[][] globalDistance;
    private PairwiseDistance[][] semiGlobalDistance;
    private DistanceInfo[][] bestDistance;
    private List<Sequence> sequences;

    public DistanceMatrix(List<Sequence> sequences) {
        this.sequences = sequences;
    }

    public void calculateDistances(Matrix scoreMatrix) {

        if (sequences != null && sequences.size() > 0) {
            int sequenceCount = sequences.size();
            globalDistance = new PairwiseDistance[sequenceCount][sequenceCount];
            semiGlobalDistance = new PairwiseDistance[sequenceCount][sequenceCount];
            bestDistance = new DistanceInfo[sequenceCount][sequenceCount];


            PairwiseDistance distance = null;


            for (int i = 0; i < sequenceCount; i++) {
                for (int j = 0; j < sequenceCount; j++) {
                    if (i < j) {
                        double globalScore = 0;
                        double semiGlobalScore = 0;

                        PairWiseAlignment aligner = new GlobalAlignment(sequences.get(i), sequences.get(j), scoreMatrix);
                        char[][] alignment = aligner.align();
                        distance = new PairwiseDistance();
                        distance.setAlignment(alignment);
                        globalScore = aligner.getMaxScore();
                        distance.setScore(globalScore);
                        globalDistance[i][j] = distance;

                        char[][] mirroredAlignment = new char[alignment.length][alignment[0].length];
                        mirroredAlignment[0] = alignment[1];
                        mirroredAlignment[1] = alignment[0];
                        distance = new PairwiseDistance();
                        distance.setAlignment(mirroredAlignment);
                        distance.setScore(globalScore);
                        globalDistance[j][i] = distance;

                        aligner = new SemiGlobalAlignment(sequences.get(i), sequences.get(j), scoreMatrix);
                        alignment = aligner.align();
                        distance = new PairwiseDistance();
                        distance.setAlignment(alignment);
                        semiGlobalScore = aligner.getMaxScore();
                        distance.setScore(semiGlobalScore);
                        semiGlobalDistance[i][j] = distance;

                        mirroredAlignment = new char[alignment.length][alignment[0].length];
                        mirroredAlignment[0] = alignment[1];
                        mirroredAlignment[1] = alignment[0];
                        distance = new PairwiseDistance();
                        distance.setAlignment(mirroredAlignment);
                        distance.setScore(semiGlobalScore);
                        semiGlobalDistance[j][i] = distance;

                        DistanceInfo info = null;

                        if (globalScore > semiGlobalScore) {
                            info = new DistanceInfo();
                            info.setMaxScore(globalScore);
                            info.setType(DistanceType.GLOBAL);

                        } else {
                            info = new DistanceInfo();
                            info.setMaxScore(semiGlobalScore);
                            info.setType(DistanceType.SEMIGLOBAL);
                        }

                        bestDistance[i][j] = info;
                        bestDistance[j][i] = info;

                    } else {

                        if (i == j) {

                            double globalScore = 0;

                            PairWiseAlignment aligner = new GlobalAlignment(sequences.get(i), sequences.get(j), scoreMatrix);
                            char[][] alignment = aligner.align();
                            distance = new PairwiseDistance();
                            distance.setAlignment(alignment);
                            globalScore = aligner.getMaxScore();
                            distance.setScore(globalScore);
                            globalDistance[i][j] = distance;
                            semiGlobalDistance[i][j] = distance;

                            DistanceInfo info = null;

                            info = new DistanceInfo();
                            info.setMaxScore(globalScore);
                            info.setType(DistanceType.GLOBAL);

                            bestDistance[i][j] = info;
                        }
                    }
                }
            }

            for (int i = 0; i < sequenceCount; i++) {
                for (int j = 0; j < sequenceCount; j++) {

                    bestDistance[i][j].setSimilarityRate(bestDistance[i][j].getMaxScore()/
                            Math.min(bestDistance[i][i].getMaxScore(), bestDistance[j][j].getMaxScore()));

                }
            }
        }
    }

    public void test() {
        for (int i = 0; i < getBestDistances().length; i++) {
            for (int j = 0; j < getBestDistances().length; j++) {

                System.out.print(getBestDistances()[i][j].getSimilarityRate() + "(" + ((getBestDistances()[i][j].getType() == DistanceType.GLOBAL) ? "G" : "S") + ") ");

            }
            System.out.println();
        }

        System.out.println();



    }

    /**
     * @return the bestDistance
     */
    public DistanceInfo[][] getBestDistances() {
        return bestDistance;
    }

    public PairwiseDistance getAlignment(int seq1, int seq2, DistanceType type) {
        PairwiseDistance returnValue = null;

        switch (type) {
            case GLOBAL:
                returnValue = globalDistance[seq1][seq2];
                break;
            case SEMIGLOBAL:
                returnValue = semiGlobalDistance[seq1][seq2];
                break;
        }

        return returnValue;
    }
}
