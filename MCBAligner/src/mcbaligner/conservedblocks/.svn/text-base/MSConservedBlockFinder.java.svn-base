/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mcbaligner.conservedblocks;

import java.util.HashMap;
import java.util.List;
import jbio.alignment.Sequence;

/**
 * This class intends to keep all conserved blocks with multiple sequences
 * 
 * loop throughout all sequences searching this
 *
 *
 * @author sergio
 */
public class MSConservedBlockFinder {

    private static final int WINDOW_SIZE = 12;
    private static final double ACCEPTANCE_TRIGGER = 1;

    public static HashMap<String, ConservedBlockGroup> findBlocks(List<Sequence> sequences) {

        HashMap<String, ConservedBlockGroup> groups = new HashMap<String, ConservedBlockGroup>();


        //Start sequences loop
        for (int i = 0; i < sequences.size() - 1; i++) {

            //Swipe whole sequence
            char[] currSequence = sequences.get(i).getDataArray();

            for (int j = 0; j < currSequence.length - WINDOW_SIZE; j++) {
                //Get target block
                String targetBlock = getTargetBlock(currSequence, j, WINDOW_SIZE);

                if (!groups.containsKey(targetBlock)) {

                    ConservedBlockGroup group = new ConservedBlockGroup(sequences.size());
                    group.add(i, j, j + (WINDOW_SIZE - 1));
                    groups.put(targetBlock, group);

                    ///Look for the same sequence in the next sequences
                    for (int k = i + 1; k < sequences.size(); k++) {
                        char[] comparedSequence = sequences.get(k).getDataArray();
                        for (int l = 0; l < comparedSequence.length - WINDOW_SIZE; l++) {
                            String comparedBlock = getTargetBlock(comparedSequence, l, WINDOW_SIZE);
                            if (comparedBlock.equals(targetBlock)) {
                                group.add(k, l, l+ (WINDOW_SIZE-1));
                            }
                        }
                    }
                }
            }
        }

        return groups;
    }

    private static String getTargetBlock(char[] sequence, int startPos, int size) {
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < size; i++) {
            buffer.append(sequence[startPos + i]);
        }

        return buffer.toString();
    }
}
