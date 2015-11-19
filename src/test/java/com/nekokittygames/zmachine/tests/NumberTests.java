package com.nekokittygames.zmachine.tests;

import com.nekokittygames.zmachine.misc.ZNumber;

import org.junit.Test;

import java.io.Console;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
/**
 * Created by katsw on 19/11/2015.
 */
public class NumberTests {


    @Test
    public void TestSigned()
    {
        ZNumber num=new ZNumber((byte)0b11111111,(byte)0b11111111);
        assertEquals("Must be -1",num.toSignedShort(),-1);

        ZNumber num2=new ZNumber((byte)0b11111111,(byte)0b11100110);
        assertEquals("Must be -26",num2.toSignedShort(),-26);

        ZNumber num3=new ZNumber((byte)0b10000000,(byte)0b00000000);
        assertEquals("Must be -32768",num3.toSignedShort(),-32768);

        ZNumber num4=new ZNumber((byte)0b01111111,(byte)0b11111111);
        assertEquals("Must be 32767",num4.toSignedShort(),32767);
    }

    @Test
    public void fromNumber()
    {
        ZNumber num1=new ZNumber(4);
        assertEquals("Must be empty",num1.toBytes()[0],0b00000000);
        assertEquals("Must be 0b00000100",num1.toBytes()[1],0b00000100);
    }
}
