package com.nekokittygames.zmachine.blorb;

import com.google.common.io.CountingInputStream;
import com.nekokittygames.zmachine.blorb.IFF.Chunk;
import com.nekokittygames.zmachine.blorb.IFF.Chunks.FormChunk;
import com.nekokittygames.zmachine.blorb.IFF.Chunks.RIdxChunk;

import java.io.*;

/**
 * Created by Katrina on 08/02/2015.
 */
public class BlorbFile {
    private File file;
    private CountingInputStream stream;
    private FormChunk formChunk;
    public BlorbFile(String fileName)
    {
        this.file=new File(fileName);
    }


    public void parseFile() throws IOException, InstantiationException, IllegalAccessException {
        stream=new CountingInputStream(new FileInputStream(file));
        formChunk= (FormChunk) Chunk.getChunk(stream);

    }
    public Chunk getResourceAt(int index)
    {
        return formChunk.getChunk(index-8);
    }

    public RIdxChunk getResourceChunk()
    {
        return (RIdxChunk) getResourceAt(12);
    }
}
