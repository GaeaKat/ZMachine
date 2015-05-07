package com.nekokittygames.zmachine.blorb;

import com.google.common.io.CountingInputStream;
import com.nekokittygames.zmachine.blorb.IFF.Chunk;
import com.nekokittygames.zmachine.blorb.IFF.Chunks.FormChunk;
import com.nekokittygames.zmachine.blorb.IFF.Chunks.RIdxChunk;

import java.io.*;

/**
 * A generic zblorb file
 * Created by Katrina on 08/02/2015.
 */
public class BlorbFile {
    private File file;
    private CountingInputStream stream;
    private FormChunk formChunk;

    /**
     * String based constructor
     * @param fileName filename to load
     */
    public BlorbFile(String fileName)
    {
        this.file=new File(fileName);
    }

    /**
     * File based constructor
     * @param file file to load
     */
    public BlorbFile(File file)
    {
        this.file=file;
    }


    /**
     * parses the file into it's chunks
     * @throws IOException If there is an error reading the file
     * @throws InstantiationException If there is an unknown chunk found
     * @throws IllegalAccessException If something weird is going on
     */
    public void parseFile() throws IOException, InstantiationException, IllegalAccessException {
        stream=new CountingInputStream(new FileInputStream(file));
        formChunk= (FormChunk) Chunk.getChunk(stream);

    }

    /**
     * gets resource at index
     * @param index index to get resource at
     * @return chunk at said index
     */
    public Chunk getResourceAt(int index)
    {
        return formChunk.getChunk(index-8);
    }


    /**
     * Shortcut to get the resource chunk
     * @return resource chunk
     */
    public RIdxChunk getResourceChunk()
    {
        return (RIdxChunk) getResourceAt(12);
    }
}
