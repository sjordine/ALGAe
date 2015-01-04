/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jbio.alignment.compressedalphabet;

/**
 *
 * @author sergio
 */
public class Dayhoff6 extends CompressedAlphabet {

    @Override
    protected char convertChar(char baseChar) {

        char returnValue = Character.MIN_VALUE;

        switch (baseChar){
            case 'A':
            case 'G':
            case 'P':
            case 'S':
            case 'T':
                returnValue = 'A';
                break;
            case 'C':
                returnValue = 'C';
                break;
            case 'B':
            case 'D':
            case 'E':
            case 'N':
            case 'Q':
            case 'Z':
                returnValue = 'D';
                break;
            case 'F':
            case 'W':
            case 'Y':
                returnValue = 'F';
                break;
            case 'H':
            case 'K':
            case 'R':
                returnValue = 'H';
                break;
            case 'I':
            case 'L':
            case 'M':
            case 'V':
                returnValue = 'I';
                break;
            case 'X':
                returnValue = 'X';
                break;
            default:
                throw new RuntimeException ("Invalid Amino-acid "+baseChar);
        }

        return returnValue;
    }

}
