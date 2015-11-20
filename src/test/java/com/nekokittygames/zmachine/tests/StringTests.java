package com.nekokittygames.zmachine.tests;

import com.google.common.io.Resources;
import com.nekokittygames.zmachine.ZMachine;
import com.nekokittygames.zmachine.strings.ZStringManager;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by katsw on 20/11/2015.
 */
public class StringTests {

    private static ZMachine zMachine;

    @BeforeClass
    public static void before()
    {
        try {
            zMachine=new ZMachine();
            zMachine.InitFromFile(Resources.getResource("Galatea.zblorb").getFile());
        }
        catch(Exception e)
        {
            assertFalse("Could not open Test File with error"+e.getMessage(),true);
        }
    }

    @Test
    public void testString()
    {
        assertEquals("String at 0x5B70 must be galatea","galatea",ZStringManager.Decode(0x5B70));
    }
}
