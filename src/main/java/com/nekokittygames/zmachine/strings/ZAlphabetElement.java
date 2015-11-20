package com.nekokittygames.zmachine.strings;

/**
 * Created by katsw on 20/11/2015.
 */
public class ZAlphabetElement {
    private short zcharCode;
    private ZAlphabet.Alphabet alphabet;

    public ZAlphabetElement(ZAlphabet.Alphabet alphabet,short zcharCode)
    {
        this.alphabet=alphabet;
        this.zcharCode=zcharCode;
    }

    public ZAlphabet.Alphabet getAlphabet() {
        return alphabet;
    }

    public short getZcharCode() {
        return zcharCode;
    }
}
