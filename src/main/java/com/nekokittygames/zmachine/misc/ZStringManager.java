package com.nekokittygames.zmachine.misc;

import com.nekokittygames.zmachine.memory.Memory;

/**
 * Created by nekosune on 19/11/15.
 */
public class ZStringManager {


    private static Memory mem;
    private static ZStringManager instance;
    public static ZStringManager getInstance()
    {
        return instance;
    }


}
