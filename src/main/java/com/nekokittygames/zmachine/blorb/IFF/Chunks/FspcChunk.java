package com.nekokittygames.zmachine.blorb.iff.chunks;

import com.google.common.io.CountingInputStream;
import com.nekokittygames.zmachine.blorb.iff.Chunk;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;

/**
 * Created by Katrina on 09/02/2015.
 */
public class FspcChunk extends Chunk {
    protected int number;


    @Override
    public void Parse(CountingInputStream inStream) throws IOException {
        super.Parse(inStream);
        number = stream.readInt();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("number", number).toString();
    }
}
