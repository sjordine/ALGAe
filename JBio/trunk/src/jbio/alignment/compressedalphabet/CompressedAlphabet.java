/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jbio.alignment.compressedalphabet;

import java.util.ArrayList;
import java.util.List;
import jbio.alignment.Sequence;

/**
 *
 * @author sergio
 */
public abstract class CompressedAlphabet {

    public List<Sequence> convertSequences(List<Sequence> baseSequences){

        List<Sequence> returnValue = null;

        if (baseSequences!=null){

            returnValue = new ArrayList<Sequence>();

            for (int i=0;i < baseSequences.size(); i++)
            {
                Sequence currentSequence = baseSequences.get(i);
                Sequence convertedSequence = convertSequence(currentSequence);

                returnValue.add(convertedSequence);
            }
        }

        return returnValue;

    }

    public Sequence convertSequence(Sequence baseSequence) {
       
        Sequence returnValue = null;

        if (baseSequence!=null){

            StringBuilder buffer = new StringBuilder();
            char[] data = baseSequence.getDataArray();

            for (int i=0; i < data.length; i++){
                buffer.append(convertChar(data[i]));
            }


            returnValue = new Sequence(baseSequence.getDescription(), buffer.toString());
        }


        return returnValue;
    }

    protected abstract char convertChar(char baseChar);

}
