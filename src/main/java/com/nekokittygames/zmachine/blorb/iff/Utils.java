package com.nekokittygames.zmachine.blorb.iff;

/**
 * Utils used by the IFF parser
 * Created by Katrina on 09/02/2015.
 */
public class Utils {

    /**
     * Gets the identifier string from a 4 byte array
     *
     * @param ident Array of 4 bytes
     * @return identifier for chunk
     */
    public static String getIdentString(byte[] ident) {
        char[] identC = new char[4];
        for (int i = 0; i < 4; i++)
            identC[i] = (char) ident[i];
        return new String(identC);
    }
}
