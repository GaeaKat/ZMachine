package com.nekokittygames.zmachine.strings;

import com.nekokittygames.zmachine.memory.Memory;

/**
 * Created by nekosune on 19/11/15.
 */
public class ZStringManager {



    private static Memory mem;
    private static int currentAlphabet=0;

    public static void InitializeManager(Memory mem)
    {
        ZStringManager.mem=mem;
        ZSCII.Init(mem);
        ZAlphabet.initAlphabet(mem);
    }



    public static String Decode(int address)
    {
        return Decode(address,0);
    }

    public static String Decode(int address,int length)
    {
        ZDecode decode=new ZDecode(new ZTranslate());
        return decode.decode2Zscii(mem,address,length);
    }



}
