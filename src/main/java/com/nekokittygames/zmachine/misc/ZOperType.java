package com.nekokittygames.zmachine.misc;

/**
 * Created by nekosune on 29/11/15.
 */
public enum ZOperType {

    LARGE_CONSTANT(0b00),
    SMALL_CONSTANT(0b01),
    VARIABLE(0b10),
    OMMITED(0b11);

    private byte binary;



    ZOperType(int i) {
        this.binary= (byte) i;
    }

    public static ZOperType getType(byte byt)
    {
        for(ZOperType val:ZOperType.values())
        {
            if(val.binary==byt)
                return val;
        }
        return OMMITED;
    }
}
