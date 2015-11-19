package com.nekokittygames.zmachine.blorb.iff.chunks;

import com.google.common.io.CountingInputStream;
import com.nekokittygames.zmachine.blorb.iff.Chunk;

import java.io.IOException;

/**
 * Created by Katrina on 13/02/2015.
 */
public class BinaChunk extends Chunk {
    private byte[] data;

    @Override
    public void Parse(CountingInputStream inStream) throws IOException {
        super.Parse(inStream);
        data = new byte[getSize()];
        stream.readFully(data);
    }
}
