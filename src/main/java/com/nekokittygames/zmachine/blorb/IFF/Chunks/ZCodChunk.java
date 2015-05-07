package com.nekokittygames.zmachine.blorb.IFF.Chunks;

import com.google.common.io.CountingInputStream;
import com.nekokittygames.zmachine.blorb.IFF.Chunk;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Katrina on 09/02/2015.
 */
public class ZCodChunk extends Chunk {

    public byte[] image;


    @Override
    public void Parse(CountingInputStream inStream) throws IOException {
        super.Parse(inStream);
        image=new byte[getSize()];
        stream.readFully(image);

    }

}
