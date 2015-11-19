package com.nekokittygames.zmachine.blorb.iff.chunks;

import com.google.common.io.CountingInputStream;
import com.nekokittygames.zmachine.blorb.iff.Chunk;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by Katrina on 09/02/2015.
 */
public class JPEGChunk extends Chunk {

    public BufferedImage bufferedImage;
    protected byte[] image;

    @Override
    public void Parse(CountingInputStream inStream) throws IOException {
        super.Parse(inStream);
        image = new byte[getSize()];
        stream.readFully(image);
        bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
    }
}
