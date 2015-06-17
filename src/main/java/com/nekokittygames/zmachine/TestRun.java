package com.nekokittygames.zmachine;

import com.nekokittygames.zmachine.blorb.BlorbFile;
import com.nekokittygames.zmachine.blorb.IFF.Chunk;
import com.nekokittygames.zmachine.blorb.IFF.Chunks.JPEGChunk;
import com.nekokittygames.zmachine.blorb.IFF.Chunks.RIdxChunk;
import com.nekokittygames.zmachine.blorb.IFF.Chunks.ZCodChunk;
import com.nekokittygames.zmachine.memory.Memory;
import com.nekokittygames.zmachine.misc.ZRandom;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by Katrina on 09/02/2015.
 */
public class TestRun {

    public static void main (String[] arg) throws IOException, IllegalAccessException, InstantiationException {
        /*BlorbFile file=new BlorbFile("C:\\Users\\Katrina\\Downloads\\The Pelican.zblorb");
        file.parseFile();
        RIdxChunk.Resource res=file.getResourceChunk().getResource("Exec",0);
        ZCodChunk chunk=(ZCodChunk)file.getResourceAt(res.Start);
        Memory mem=Memory.loadMemoryFromStream(chunk.image);

        System.out.println(mem.getVersion());
        System.out.println(mem.getStatusType());
        System.out.println(mem.getSplit());
        System.out.println(mem.isCensorMode());
        System.out.println(mem.isStatusLineAvailable());
        System.out.println(mem.isScreenSplittingAvailable());
        System.out.println(mem.isColoursAvailable());
        System.out.println(mem.getBoldfaceAvailable());
        System.out.println(mem.getItalicAvailable());
        System.out.println(mem.getFixedSpaceAvailable());
        System.out.println(mem.getSoundEffectsAvailable());
        System.out.println(mem.getTimedKeyboardAvailable());
        System.out.println(mem.getHighAddress());
        System.out.println(mem.getInitialPC());
        System.out.println(mem.getDictionary());
        System.out.println(mem.getObjects());
        System.out.println(mem.getGlobalVariables());
        System.out.println(mem.getStaticMemory());
        System.out.println(mem.getSerialCode());
        System.out.println(mem.getAbbrieviationsTable());
        System.out.println(mem.getFileSize()*4);
        System.out.println((int)mem.getChecksum());
        System.out.println(mem.getRoutineOffset());
        System.out.println(mem.getStringsOffset());
        System.out.println((int)mem.getTerminatingCharactersTable());
        System.out.println(mem.getRevisionNumber());
        System.out.println((int)mem.getAlphabetTable());
        System.out.println((int)mem.getHeaderExtension());
        System.out.println((int)mem.getHeaderExtentionLength());
        System.out.println((int)mem.getUnicodeTranslationTable());

        System.out.println(mem.getTranscriptingOn());
        System.out.println(mem.getForcedFixedPitch());
        System.out.println(mem.getPictureAvailable());
        System.out.println(mem.getUseUndo());
        System.out.println(mem.getUseMouse());
        System.out.println(mem.getUseColours());
        System.out.println(mem.getUseSound());
        System.out.println(mem.getUseMenu());*/

    }
}
