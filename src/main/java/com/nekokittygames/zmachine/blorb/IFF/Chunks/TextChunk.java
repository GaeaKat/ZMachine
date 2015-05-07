package com.nekokittygames.zmachine.blorb.IFF.Chunks;

import com.google.common.io.CountingInputStream;
import com.nekokittygames.zmachine.blorb.IFF.Chunk;

import java.io.IOException;

/**
 * Created by Katrina on 13/02/2015.
 */
public class TextChunk extends Chunk {
    public byte[] text;

    @Override
    public void Parse(CountingInputStream inStream) throws IOException {
        super.Parse(inStream);
        text=new byte[getSize()];
        stream.readFully(text);

    }
}
