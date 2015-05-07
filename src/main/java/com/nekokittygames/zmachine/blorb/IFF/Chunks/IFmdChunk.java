package com.nekokittygames.zmachine.blorb.IFF.Chunks;

import com.google.common.io.CountingInputStream;
import com.nekokittygames.zmachine.blorb.IFF.Chunk;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.EOFException;
import java.io.IOException;

/**
 * Created by Katrina on 09/02/2015.
 */
public class IFmdChunk extends Chunk{

    protected String xml;

    @Override
    public void Parse(CountingInputStream inStream) throws IOException {
        super.Parse(inStream);

        byte[] output=new byte[getSize()];
        stream.read(output);
        char [] outputC=new char[getSize()];
        for(int i=0;i<output.length;i++)
            outputC[i]=(char)output[i];
        xml=new String(outputC);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("xml",xml).toString();
    }
}
