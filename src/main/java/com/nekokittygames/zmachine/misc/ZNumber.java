package com.nekokittygames.zmachine.misc;

import java.nio.ByteBuffer;

/**
 * Helper function to help work on numbers
 * Created by katsw on 19/11/2015.
 */
public class ZNumber {

    private byte[] bytes=new byte[2];

    /**
     * Constructs a ZNumber from bytes
     * @param msb Most Siginificant Bit
     * @param lsb Least signigicant bit
     */
    public ZNumber(byte msb,byte lsb)
    {
        this(new byte[]{msb,lsb});
    }

    /**
     * Constructs a ZNumber from bytes
     * @param bytes byte array to gen
     */
    public ZNumber(byte[] bytes)
    {
        this.bytes=bytes;
    }


    /**
     * Constructs a ZNumber from a normal java int
     * @param num number to generate
     */
    public ZNumber(int num)
    {
        bytes[1] = (byte)(num & 0xff);
        bytes[0] = (byte)((num >> 8) & 0xff);
    }

    /**
     * ZNumber to byte array
     * @return byte array representing the ZNumber
     */
    public byte[] toBytes()
    {
        return bytes;
    }


    /**
     * ZNumber to Unsigned Short
     * @return short representing the unsigned value
     */
    public int toUnsignedShort()
    {
        ByteBuffer bb=ByteBuffer.wrap(bytes);
        return bb.getShort()&0xFFFF;
    }

    /**
     * ZNumber to signed short
     * @return short representing the signed value
     */
    public short toSignedShort()
    {
        ByteBuffer bb=ByteBuffer.wrap(bytes);
        return bb.getShort();
    }
}
