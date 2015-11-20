package com.nekokittygames.zmachine.strings;

/**
 * Created by nekosune on 19/11/15.
 */
public class ZTranslate {
    private ZAlphabet.Alphabet currentAlphabet;
    private ZAlphabet.Alphabet lockAlphabet;
    private boolean shiftLock;


    public ZTranslate()
    {
        reset();
    }

    public final void reset()
    {
        currentAlphabet= ZAlphabet.Alphabet.A0;
        lockAlphabet=null;
        shiftLock=false;
    }

    public void resetTolast()
    {
        if(lockAlphabet==null)
            currentAlphabet= ZAlphabet.Alphabet.A0;
        else {
            currentAlphabet = lockAlphabet;
            shiftLock = true;
        }
    }

    public ZAlphabet.Alphabet getCurrentAlphabet()
    {
        return currentAlphabet;
    }

    public char translate(final char zChar)
    {
        if(shift(zChar))
            return '\0';

        char result='?';
        if(isInAlphabetRange(zChar))
        {
            switch (currentAlphabet)
            {
                case A0:
                    result=(char)ZAlphabet.getA0Char((byte)zChar);
                    break;
                case A1:
                    result=(char)ZAlphabet.getA1Char((byte)zChar);
                    break;
                case A2:
                    result=(char)ZAlphabet.getA2Char((byte)zChar);
                    break;
            }
        }
        if(!shiftLock)
            resetTolast();
        return result;
    }

    public boolean willEscapeAO(final char zChar)
    {
        return currentAlphabet== ZAlphabet.Alphabet.A2 && zChar==ZAlphabet.A2_ESCAPE;
    }


    public boolean isAbbreviation(final char zChar)
    {
        return ZAlphabet.isAbbreviation(zChar);
    }


    public ZAlphabetElement getElementFor(final char zChar)
    {
        if(zChar=='\n')
            return new ZAlphabetElement(ZAlphabet.Alphabet.A2,(short)7);

        ZAlphabet.Alphabet alphabet=null;

        int zCharCode=ZAlphabet.getA0CharCode(zChar);
        if(zCharCode>=0)
        {
            alphabet= ZAlphabet.Alphabet.A0;
        }
        else
        {
            zCharCode=ZAlphabet.getA1CharCode(zChar);
            if(zCharCode>=0)
            {
                alphabet= ZAlphabet.Alphabet.A1;
            }
            else
            {
                zCharCode=ZAlphabet.getA2CharCode(zChar);
                if(zCharCode>=0)
                {
                    alphabet= ZAlphabet.Alphabet.A2;
                }
            }
        }
        if(alphabet==null)
            zCharCode=zChar;

        return new ZAlphabetElement(alphabet,(short)zCharCode);
    }

    private static boolean isInAlphabetRange(final char zChar)
    {
        return 0 <= zChar && zChar<= ZAlphabet.ALPHABET_END;
    }

    private boolean shift(final char zChar)
    {
        if(ZAlphabet.isShift(zChar))
        {
            currentAlphabet=shiftFrom(currentAlphabet,zChar);
            if(ZAlphabet.isShiftLock(zChar))
            {
                lockAlphabet=currentAlphabet;
            }
            return true;
        }
        return false;
    }

    private ZAlphabet.Alphabet shiftFrom(final ZAlphabet.Alphabet alphabet,final char shiftChar)
    {
        ZAlphabet.Alphabet result=null;
        if(ZAlphabet.isShift1(shiftChar))
        {
            switch (alphabet)
            {
                case A0:
                    result= ZAlphabet.Alphabet.A1;
                    break;
                case A1:
                    result= ZAlphabet.Alphabet.A2;
                    break;
                case A2:
                    result= ZAlphabet.Alphabet.A0;
                    break;
            }
        }
        else if(ZAlphabet.isShift2(shiftChar))
        {
            switch (alphabet)
            {
                case A0:
                    result= ZAlphabet.Alphabet.A2;
                    break;
                case A1:
                    result= ZAlphabet.Alphabet.A0;
                    break;
                case A2:
                    result= ZAlphabet.Alphabet.A1;
                    break;
            }
        }
        else
        {
            result=alphabet;
        }
        shiftLock=ZAlphabet.isShiftLock(shiftChar);


        return result;
    }
}
