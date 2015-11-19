package com.nekokittygames.zmachine.blorb.iff.chunks;

import com.google.common.io.CountingInputStream;
import com.nekokittygames.zmachine.blorb.iff.Chunk;
import com.nekokittygames.zmachine.blorb.iff.Utils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * FORM type chunk
 * Created by Katrina on 09/02/2015.
 */
public class FormChunk extends Chunk {
    private final Map<Long, Chunk> chunks = new HashMap<Long, Chunk>();
    private String type;

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    @Override
    public void Parse(CountingInputStream inStream) throws IOException {
        super.Parse(inStream);
        byte[] chars = new byte[4];
        stream.read(chars);
        this.setType(Utils.getIdentString(chars));
        long byteNumber = inStream.getCount();
        Chunk chunk = Chunk.getChunk(inStream);
        while (chunk != null) {
            chunks.put(byteNumber, chunk);
            byteNumber = inStream.getCount();
            chunk = Chunk.getChunk(inStream);
        }
    }

    public Chunk getChunk(long index) {
        return chunks.get(index);
    }

    /**
     * returnss string representing object
     *
     * @return string
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("identifier", identifier).append("size", size).append("type", type).append(chunks).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(identifier).append(type).append(size).hashCode();
    }

}
