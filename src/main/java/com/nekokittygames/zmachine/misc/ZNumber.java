package com.nekokittygames.zmachine.misc;

import java.nio.ByteBuffer;

/**
 * Created by katsw on 19/11/2015.
 */
public class ZNumber {

    private byte[] bytes=new byte[2];


    public ZNumber(byte msb,byte lsb)
    {
        this(new byte[]{msb,lsb});
    }

    public ZNumber(byte[] bytes)
    {
        this.bytes=bytes;
    }

    public ZNumber(int num)
    {
        bytes[0] = (byte)(num & 0xff);
        bytes[1] = (byte)((num >> 8) & 0xff);
    }

    public byte[] toBytes()
    {
        return bytes;
    }

    public int toUnsignedShort()
    {
        ByteBuffer bb=ByteBuffer.wrap(bytes);
        return bb.getShort()&0xFFFF;
    }

    public short toSignedShort()
    {
        ByteBuffer bb=ByteBuffer.wrap(bytes);
        return bb.getShort();
    }

    public ZNumber add(ZNumber other)
    {
        return null;
    }
}
