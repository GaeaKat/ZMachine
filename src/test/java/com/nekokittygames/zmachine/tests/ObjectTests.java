package com.nekokittygames.zmachine.tests;

import com.google.common.io.Resources;
import com.nekokittygames.zmachine.ZMachine;
import com.nekokittygames.zmachine.memory.ZObject;
import com.nekokittygames.zmachine.memory.ZObjectTree;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by APPLE on 08/12/2015.
 */
public class ObjectTests {
    private static ZMachine zMachine;
    private static ZObject testObj;
    @BeforeClass
    public static void before()
    {
        try {
            zMachine=new ZMachine();
            zMachine.InitFromFile(Resources.getResource("Galatea.zblorb").getFile());
            ZObjectTree tree=new ZObjectTree(zMachine.getMemory());
            testObj=tree.getObject(72);
        }
        catch(Exception e)
        {
            assertFalse("Could not open Test File with error"+e.getMessage(),true);
        }
    }


    @Test
    public void Name()
    {

        assertEquals("Name must be 'her journey'","her journey",testObj.getName());
    }


    @Test
    public void Parent()
    {
        assertEquals("Parent must be 66",66,testObj.getParent());
    }

    @Test
    public void Sibling()
    {
        assertEquals("Sibling must be 77",77,testObj.getSibling());
    }

    @Test
    public void Child()
    {
        assertEquals("Child must be 74",74,testObj.getChild());
    }

    @Test
    public void Properties()
    {
        byte[] arr52=new byte[]{0x19,0x47};
        byte[] ar1=new byte[]{0x51, (byte) 0x98,0x51, (byte) 0x8f,0x51,0x50,0x64,0x55,0x6b, (byte) 0x81,0x6b, (byte) 0x9c,0x5e, (byte) 0xfd};

        assertArrayEquals("Property 52 is two byte simple",arr52,testObj.getProperty(52));
        assertArrayEquals("Property 1 is long complicated",ar1,testObj.getProperty(1));
    }

    @Test
    public void Attributes()
    {
        assertEquals("Attribute 16 must be set",true,testObj.getAttributes().get(16));
        assertEquals("Attribute 17 must not be set",false,testObj.getAttributes().get(17));
        assertEquals("Attribute 23 must be set",true,testObj.getAttributes().get(23));
    }
}
