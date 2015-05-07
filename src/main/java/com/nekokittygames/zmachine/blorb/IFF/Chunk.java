package com.nekokittygames.zmachine.blorb.IFF;

import com.google.common.io.CountingInputStream;
import com.nekokittygames.zmachine.blorb.IFF.Chunks.*;

import javax.xml.crypto.Data;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Single chunk in an IFF file, may contain more chunks
 *
 *
 * Created by Katrina Swales on 09/02/2015.
 */
public abstract class Chunk {
    protected String identifier;
    protected int size;
    protected DataInputStream stream;
    private static HashMap<String,Class<? extends Chunk>> chunkClasses=new HashMap<String, Class<? extends Chunk>>();

    /**
     * gets the chunk identifier
     * @return identifying 4 letter string for chunk
     */
    public String getIdentifier() {
        return identifier;
    }


    /**
     * Sets 4 letter chunk ident
     * @param identifier ident to set
     * @throws IllegalArgumentException thrown if ident wished is not 4 characters
     */
    public void setIdentifier(String identifier){
        if(identifier.length()!=4)
            throw new IllegalArgumentException("Identifier must have 4 letters");
        this.identifier = identifier;
    }

    /**
     * Gets the size of the chunk
     * @return size of chunk in bytes
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the size of the chunk
     * @param size size of chunk in bytes
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Parses the chunk into the object
     * @param inStream Input stream holding chunk
     * @throws IOException If invalid file
     */
    public void Parse(CountingInputStream inStream) throws IOException {
        this.stream=new DataInputStream(inStream);
    }

    /**
     * Adds a chunk class to the parser
     * @param chunk
     * @param chunkClass
     */
    public static void AddChunkClass(String chunk,Class<? extends Chunk> chunkClass)
    {
        if(!chunkClasses.containsKey(chunk))
            chunkClasses.put(chunk,chunkClass);
    }

    /**
     * Adds the known chunk classs
     */
    static
    {
        AddChunkClass("FORM", FormChunk.class);
        AddChunkClass("RIdx", RIdxChunk.class);
        AddChunkClass("PNG ", PNGChunk.class);
        AddChunkClass("ZCOD", ZCodChunk.class);
        AddChunkClass("IFhd", IFhdChunk.class);
        AddChunkClass("IFmd", IFmdChunk.class);
        AddChunkClass("Fspc", FspcChunk.class);
        AddChunkClass("JPEG",JPEGChunk.class);
        AddChunkClass("Rect",RectChunk.class);
        AddChunkClass("OGGV",OGGVChunk.class);
        AddChunkClass("MOD ",MODChunk.class);
        AddChunkClass("SONG",SongChunk.class);
        AddChunkClass("TEXT",TextChunk.class);
        AddChunkClass("Plte",PlteChunk.class);

    }

    /**
     * gets a chunk from the stream
     * @param inStream Stream to read from
     * @return Fully formed chunk
     * @throws IOException If there is a problem reading from the file
     */
    public static Chunk getChunk(CountingInputStream inStream) throws IOException {
        DataInputStream stream=new DataInputStream(inStream);
        byte[] chars=new byte[4];
        try {
            stream.read(chars);
        }
        catch (EOFException e)
        {
            return null;
        }
        String ident=Utils.getIdentString(chars);
        if(chunkClasses.containsKey(ident))
        {
            try
            {
                Chunk chunk=chunkClasses.get(ident).newInstance();
                chunk.setIdentifier(ident);
                chunk.setSize(stream.readInt());
                byte[] bytes=new byte[chunk.getSize()];
                stream.read(bytes);
                chunk.Parse(new CountingInputStream(new ByteArrayInputStream(bytes)));
                if(inStream.getCount() % 2!=0)
                    stream.readByte();
                return chunk;
            }
            catch(Exception e) {
                return null;
            }


        }
        return null;

    }

}
