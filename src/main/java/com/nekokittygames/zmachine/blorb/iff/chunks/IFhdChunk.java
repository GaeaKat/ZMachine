package com.nekokittygames.zmachine.blorb.iff.chunks;

import com.google.common.io.CountingInputStream;
import com.nekokittygames.zmachine.blorb.iff.Chunk;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;

/**
 * Created by Katrina on 09/02/2015.
 */
public class IFhdChunk extends Chunk {
    protected int releaseNumber;
    protected byte[] serialNumber;
    protected int checksum;
    protected byte[] PC;


    @Override
    public void Parse(CountingInputStream inStream) throws IOException {
        super.Parse(inStream);


        releaseNumber = stream.readInt();
        serialNumber = new byte[6];
        stream.read(serialNumber);
        checksum = stream.readInt();
        PC = new byte[3];
        stream.read(PC);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("releaseNumber", releaseNumber).append("serialNumber", serialNumber).append("checksum", checksum).append("PC", PC).toString();
    }
}
