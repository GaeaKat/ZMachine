package com.nekokittygames.zmachine.tests;

import com.nekokittygames.zmachine.blorb.BlorbFile;
import com.nekokittygames.zmachine.blorb.IFF.Chunks.RIdxChunk;
import com.nekokittygames.zmachine.blorb.IFF.Chunks.ZCodChunk;
import com.nekokittygames.zmachine.memory.Memory;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Katrina on 07/05/2015.
 */
public class HeaderTests {

    public static Memory mem;

    @BeforeClass
    public static void before()
    {
        //try {
        //    BlorbFile file = new BlorbFile("C:\\Users\\Katrina\\Downloads\\The Pelican.zblorb");
        //    file.parseFile();
        //    RIdxChunk.Resource res = file.getResourceChunk().getResource("Exec", 0);
        //    ZCodChunk chunk = (ZCodChunk) file.getResourceAt(res.Start);
        //    mem = Memory.loadMemoryFromStream(chunk.image);
        //}
        //catch(Exception e)
        //{
///
 //       }
    }
    @Test
    public void MemoryIsBitSet()
    {
        assertFalse("Wrong Bit should be false",mem.isBitSet((byte)  0x1,1));
        assertEquals("Right Bit should not be false", true, mem.isBitSet((byte) 0x1, 0));

        assertEquals("Right Bit 4should not be false",true,mem.isBitSet((byte)  0x8,3));
        assertEquals("Right Bit  !4 should not be false",false,mem.isBitSet((byte)  0x8,2));

    }


    @Test
    public void MemorySetBit()
    {
        assertFalse("Should make a difference",0x00==Memory.setBit((byte)0x00,0,true));
        assertEquals("Should make a correct difference", 0x1, Memory.setBit((byte) 0x00, 0, true));
        assertEquals("Should make a correct difference with another bit", 0x4, Memory.setBit((byte) 0x00, 2, true));
        assertEquals("Correctly handles retaining existing bit", 0x5, Memory.setBit((byte) 0x01, 2, true));
    }

}
