package com.nekokittygames.zmachine;

import com.nekokittygames.zmachine.blorb.BlorbFile;
import com.nekokittygames.zmachine.blorb.iff.chunks.RIdxChunk;
import com.nekokittygames.zmachine.blorb.iff.chunks.ZCodChunk;
import com.nekokittygames.zmachine.memory.Memory;
import com.nekokittygames.zmachine.strings.ZStringManager;

import java.io.IOException;

/**
 * Created by Katrina on 16/02/2015.
 */
public class ZMachine {
    private Memory memory;


    public Memory getMemory()
    {
        return memory;
    }
    public void InitFromFile(String fileName) throws IOException {
        BlorbFile file=new BlorbFile(fileName);
        file.parseFile();
        RIdxChunk.Resource res=file.getResourceChunk().getResource("Exec",0);
        ZCodChunk chunk=(ZCodChunk)file.getResourceAt(res.Start);
        memory=Memory.loadMemoryFromStream(chunk.image);
        ZStringManager.InitializeManager(memory);
        InitZMachine(false);
    }

    public void InitZMachine(boolean restart) {
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
        memory.setInterpreterNumber((byte) 2);
        memory.setInterpreterVersion((byte) 1);
        memory.setScreenHeightLines((byte) 19);
        memory.setScreenWidthChars((byte) 49);
    }
}
