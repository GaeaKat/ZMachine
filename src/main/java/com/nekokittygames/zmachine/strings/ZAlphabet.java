package com.nekokittygames.zmachine.strings;

import com.nekokittygames.zmachine.memory.Memory;

/**
 * Created by nekosune on 19/11/15.
 */
public class ZAlphabet {


    private static final String A0CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String A1CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String A2CHARS = " \n0123456789.,!?_#'\"/\\-:()";
    private static final String A2V1CHARS = " 0123456789.,!?_#'\"/\\<-:()";
    /** Defines the possible alphabets here. */
    enum Alphabet {  A0, A1, A2 }

    static final int ALPHABET_START  = 6;
    static final int ALPHABET_END    = 31;
    static final int ALPHABET_SIZE = 26;

    static final char SHIFT_2 = 0x02; // Shift 1
    static final char SHIFT_3 = 0x03; // Shift 2
    static final char SHIFT_4 = 0x04; // Shift lock 1
    static final char SHIFT_5 = 0x05; // Shift lock 2

    /** This character code, used from A2, denotes that a 10 bit value follows. */
    static final char A2_ESCAPE = 0x06; // escape character

    private static Memory mem;

    /**
     * Initializes the Alphabet
     * @param mem memory to use
     */
    public static void initAlphabet(Memory mem)
    {
        ZAlphabet.mem=mem;
    }


    /**
     * gets Alphabet 0 char
     * @param zChar character to get
     * @return chaarcter
     */
    public static char getA0Char(final byte zChar)
    {
        if(zChar==0)
            return ' ';
        if(mem.getAlphabetTable()>0)
            return (char) mem.getWordb(mem.getAlphabetTable()+(zChar-ALPHABET_START));
        if(mem.getVersion()==1 && zChar==1)
            return '\n';
        return A0CHARS.charAt(zChar-ALPHABET_START);
    }

    /**
     * gets Alphabet 1 char
     * @param zChar character to get
     * @return chaarcter
     */
    public static char getA1Char(final byte zChar)
    {
        if(zChar==0)
            return ' ';
        if(mem.getAlphabetTable()>0)
            return (char) mem.getWordb(ALPHABET_SIZE+mem.getAlphabetTable()+(zChar-ALPHABET_START));
        if(mem.getVersion()==1 && zChar==1)
            return '\n';
        return A1CHARS.charAt(zChar-ALPHABET_START);
    }

    /**
     * gets Alphabet 2 char
     * @param zChar character to get
     * @return chaarcter
     */
    public static char getA2Char(final byte zChar)
    {
        if(zChar==0)
            return ' ';
        if(mem.getAlphabetTable()>0)
            return (char) mem.getWordb((2*ALPHABET_SIZE)+mem.getAlphabetTable()+(zChar-ALPHABET_START));
        if(mem.getVersion()==1 && zChar==1)
            return '\n';
        if(mem.getVersion()==1)
        {
            return A2V1CHARS.charAt(zChar-ALPHABET_START);
        }
        return A2CHARS.charAt(zChar-ALPHABET_START);
    }


    /***
     * gets index of character from A0
     * @param zsciiChar character
     * @return index
     */
    public static int getA0CharCode(final char zsciiChar)
    {
        if(mem.getAlphabetTable()>0)
        {
            for (int i = ALPHABET_START; i < ALPHABET_START + ALPHABET_SIZE; i++) {
                if (getA0Char((byte) i) == zsciiChar) return i;
            }
            return -1;
        }
        return getCharCodeFor(A0CHARS,zsciiChar);
    }

    /***
     * gets index of character from A1
     * @param zsciiChar character
     * @return index
     */
    public static int getA1CharCode(final char zsciiChar)
    {
        if(mem.getAlphabetTable()>0)
        {
            for (int i = ALPHABET_START; i < ALPHABET_START + ALPHABET_SIZE; i++) {
                if (getA1Char((byte) i) == zsciiChar) return i;
            }
            return -1;
        }
        return getCharCodeFor(A1CHARS,zsciiChar);
    }

    /***
     * gets index of character from A2
     * @param zsciiChar character
     * @return index
     */
    public static int getA2CharCode(final char zsciiChar)
    {
        if(mem.getAlphabetTable()>0)
        {
            for (int i = ALPHABET_START; i < ALPHABET_START + ALPHABET_SIZE; i++) {
                if (getA2Char((byte) i) == zsciiChar) return i;
            }
            return -1;
        }
        if(mem.getVersion()==1)
            return getCharCodeFor(A2V1CHARS,zsciiChar);
        return getCharCodeFor(A2CHARS,zsciiChar);
    }

    private static int getCharCodeFor(String chars, char zsciiChar) {
        int index=chars.indexOf(zsciiChar);
        if(index>=0)
            index+=ALPHABET_START;
        return index;
    }

    /**
     * Is Shift1
     * @param zChar     character to check
     * @return <c>true</c> if character is shift1 <c>false</c> if not
     */
    public boolean isShift1(final char zChar)
    {
        if(mem.getVersion()==2)
            return zChar==SHIFT_2 || zChar==SHIFT_4;
        return zChar==SHIFT_4;
    }

    /**
     * Is Shift2
     * @param zChar     character to check
     * @return <c>true</c> if character is shift2 <c>false</c> if not
     */
    public boolean isShift2(final char zChar)
    {
        if(mem.getVersion()==2)
            return zChar==SHIFT_3 || zChar==SHIFT_5;
        return zChar==SHIFT_5;
    }


    /**
     * Is Shift
     * @param zChar     character to check
     * @return <c>true</c> if character is shift <c>false</c> if not
     */
    public boolean isShift(final char zChar)
    {
        return isShift1(zChar) || isShift2(zChar);
    }

    /**
     * Is Shift lock?
     * @param zChar     character to check
     * @return <c>true</c> if character is a shift lock <c>false</c> if not
     */
    public boolean isShiftLock(final char zChar)
    {
        if(mem.getVersion()==2)
            return zChar==SHIFT_4||zChar==SHIFT_5;
        return false;
    }

    /**
     * Is an abbreviation?
     * @param zChar     character to check
     * @return <c>true</c> if character is an abbreviation <c>false</c> if not
     */
    public boolean isAbbreviation(final char zChar)
    {
        if(mem.getVersion()==2)
            return zChar==1;
        if(mem.getVersion()==1)
            return false;
        return 1<=zChar && zChar<=3;
    }


}
