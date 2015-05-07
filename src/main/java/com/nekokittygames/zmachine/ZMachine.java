package com.nekokittygames.zmachine;

import com.nekokittygames.zmachine.memory.Memory;

/**
 * Created by Katrina on 16/02/2015.
 */
public class ZMachine {
    Memory memory;

    public void InitZMachine(boolean restart)
    {
        memory.setStatusLineAvailable(true);
        memory.setScreenSplitting(false);
        memory.setVariablePitch(false);
        memory.setColoursAvailable(true);
        memory.setBoldfaceAvailable(true);
        memory.setItalicAvailable(true);
        memory.setFixedSpaceAvailable(true);
        memory.setSoundEffectsAvailable(false);
        memory.setTimedKeyboardAvailable(false);

        memory.setUsePictures(false);
        memory.setUseMouse(false);
        memory.setUseSound(false);
        memory.setUseMenu(false);
        memory.setInterpreterNumber((byte)2);
        memory.setInterpreterVersion((byte)1);
        memory.setScreenHeightLines((byte)19);
        memory.setScreenWidthChars((byte)49);
    }
}
