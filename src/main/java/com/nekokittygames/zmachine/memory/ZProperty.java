package com.nekokittygames.zmachine.memory;

/**
 * Created by APPLE on 08/12/2015.
 */
public class ZProperty {

    private byte[] bytes;
    private int address;

    public ZProperty(byte[] bytes, int address) {
        this.bytes = bytes;
        this.address = address;
    }
    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
}
