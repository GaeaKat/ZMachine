package com.nekokittygames.zmachine.strings;

import com.nekokittygames.zmachine.memory.Memory;

/**
 * All things to do with ZSCII
 * Created by nekosune on 19/11/15.
 */
public class ZSCII {

    public static Memory mem;

    /**
     * Initializes the ZSCII conversion
     * @param mem memory of the machine dealing with
     */
    public static void Init(Memory mem)
    {
        ZSCII.mem=mem;
    }

    private static final char[] STANDARD_TRANSLATION_TABLE = {
            '\u00e4', '\u00f6', '\u00fc', '\u00c4', '\u00d6', '\u00dc', '\u00df',
            '\u00bb', '\u00ab',
            '\u00eb', '\u00ef', '\u00ff', '\u00cb', '\u00cf',
            '\u00e1', '\u00e9', '\u00ed', '\u00f3', '\u00fa', '\u00fd',
            '\u00c1', '\u00c9', '\u00cd', '\u00d3', '\u00da', '\u00dd',
            '\u00e0', '\u00e8', '\u00ec', '\u00f2', '\u00f9',
            '\u00c0', '\u00c8', '\u00cc', '\u00d2', '\u00d9',
            '\u00e2', '\u00ea', '\u00ee', '\u00f4', '\u00fb',
            '\u00c2', '\u00ca', '\u00ce', '\u00d4', '\u00db',
            '\u00e5', '\u00c5', '\u00f8', '\u00d8',
            '\u00e3', '\u00f1', '\u00f5', '\u00c3', '\u00d1', '\u00d5',
            '\u00e6', '\u00c6', '\u00e7', '\u00c7',
            '\u00fe', '\u00fd', '\u00f0', '\u00d0',
            '\u00a3', '\u0153', '\u0152', '\u00a1', '\u00bf'
    };

    static final  char NULL          = 0;
    static final  char DELETE        = 8;
    static final  char NEWLINE_10    = 10;
    static final  char NEWLINE       = 13;
    static final  char ESCAPE        = 27;
    static final  char CURSOR_UP     = 129;
    static final  char CURSOR_DOWN   = 130;
    static final  char CURSOR_LEFT   = 131;
    static final  char CURSOR_RIGHT  = 132;
    static final  char ASCII_START   = 32;
    static final  char ASCII_END     = 126;

    /** The start of the accent range. */
    static final  char ACCENT_START = 155;

    /** End of the accent range. */
    static final  char ACCENT_END   = 251;

    static final  char MOUSE_DOUBLE_CLICK = 253;
    static final  char MOUSE_SINGLE_CLICK = 254;

    /**
     * Length of the unicode custom table
     * @return length as an int
     */
    public int getUnicodeTableLength()
    {
        int res=0;
        if(mem.getUnicodeTranslationTable()>0)
        {
            return mem.getWordb(mem.getUnicodeTranslationTable());
        }
        return res;
    }

    /**
     * Gets the specified unicode character
     * @param index which character to get
     * @return Character
     */
    public char getUnicode(final int index)
    {
        char result='?';
        if(mem.getUnicodeTranslationTable()>0)
        {
            result= (char) mem.getWordb(mem.getUnicodeTranslationTable()+(index*2)+1);
        }
        else
            result=STANDARD_TRANSLATION_TABLE[index];
        return result;
    }

    /**
     * gets index of the lowercase version of the unicocde
     * @param index index of upper case
     * @return lower case index
     */
    public int getIndexOfLowerCase(final int index)
    {
        final char c = (char) getUnicode(index);
        final char lower = Character.toLowerCase(c);
        final int length = getUnicodeTableLength();
        for (int i = 0; i < length; i++) {
            if (getUnicode(i) == lower) return i;
        }
        return index;
    }

    /**
     * Is the character a valid ZSCII character
     * @param zchar Chaarcter to check
     * @return true if valid false if not
     */
    public boolean isZsciiCharacter(final char zchar) {
        switch (zchar) {
            case NULL:
            case DELETE:
            case NEWLINE:
            case ESCAPE:
                return true;
            default:
                return isAscii(zchar) || isAccent(zchar)
                        || isUnicodeCharacter(zchar);
        }
    }

    /**
     * Is the character ascii?
     * @param zchar Character to check
     * @return true if ascii false if not
     */
    public static boolean isAscii(final char zchar) {
        return zchar >= ASCII_START && zchar <= ASCII_END;
    }


    /**
     * Is the character accent
     * @param zchar character to check
     * @return true if accent false if not
     */
    public static boolean isAccent(final char zchar) {
        return zchar >= ACCENT_START && zchar <= ACCENT_END;
    }

    /**
     * Is the character a cursor key
     * @param zsciiChar character to check
     * @return true if cursor false if not
     */
    public static boolean isCursorKey(final char zsciiChar) {
        return zsciiChar >= CURSOR_UP && zsciiChar <= CURSOR_RIGHT;
    }

    /**
     * Is the character a function key
     * @param zsciiChar character to check
     * @return true if function false if not
     */
    public static boolean isFunctionKey(final char zsciiChar) {
        return (zsciiChar >= 129 && zsciiChar <= 154)
                || (zsciiChar >= 252 && zsciiChar <= 254);
    }

    /**
     * Is character unicode
     * @param zchar character to check
     * @return true if valid false if not
     */
    private static boolean isUnicodeCharacter(final char zchar) {
        return zchar >= 256;
    }

    /**
     * Is characrer convertible to ZSCII
     * @param c character to check
     * @return true if valid false if not
     */
    public boolean isConvertableToZscii(final char c) {
        return isAscii(c) || isInTranslationTable(c) || c == '\n'
                || c == 0 || isUnicodeCharacter(c);
    }

    /**
     * gets unicode char from index
     * @param zchar index to get
     * @return character unicode
     */
    public char getUnicodeChar(final char zchar) {
        if (isAscii(zchar)) return zchar;
        if (isAccent(zchar)) {
            final int index = zchar - ACCENT_START;
            if (index < getUnicodeTableLength()) {
                return (char) getUnicode(index);
            }
        }
        if (zchar == NULL) return '\0';
        if (zchar == NEWLINE || zchar == NEWLINE_10) return '\n';
        if (isUnicodeCharacter(zchar)) return zchar;
        return '?';
    }

    /**
     * Converts a string to a ZSCII string
     * @param str string to convert
     * @return string as ZSCII
     */
    public String convertToZscii(final String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            result.append(getZsciiChar(str.charAt(i)));
        }
        return result.toString();
    }

    /**
     * gets a character as ZSCII
     * @param c character to convert
     * @return character in ZSCII
     */
    public char getZsciiChar(final char c) {
        if (isAscii(c)) {
            return c;
        } else if (isInTranslationTable(c)) {
            return (char) (getIndexInTranslationTable(c) + ACCENT_START);
        } else if (c == '\n') {
            return NEWLINE;
        }
        return 0;
    }

    private boolean isInTranslationTable(final char c) {
        for (int i = 0; i < getUnicodeTableLength(); i++) {
            if (getUnicode(i) == c) return true;
        }
        return false;
    }

    private int getIndexInTranslationTable(final char c) {
        for (int i = 0; i < getUnicodeTableLength(); i++) {
            if (getUnicode(i) == c) return i;
        }
        return -1;
    }
}
