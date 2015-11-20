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



}
