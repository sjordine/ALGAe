/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcbaligner.conservedblocks;

import java.util.HashMap;
import java.util.List;
import jbio.alignment.Sequence;
import jbio.alignment.compressedalphabet.CompressedAlphabet;

/**
 *
 * @author sergio
 */
public class MSCompressedAlphabetConservedBlockFinder {

    public static HashMap<String, ConservedBlockGroup> findBlocks(List<Sequence> sequences, CompressedAlphabet alphabet) {

        HashMap<String, ConservedBlockGroup> groups = null;

        List<Sequence> convertedSequences = alphabet.convertSequences(sequences);

        groups = MSConservedBlockFinder.findBlocks(convertedSequences);

        return groups;
    }

}
