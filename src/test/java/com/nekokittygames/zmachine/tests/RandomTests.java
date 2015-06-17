package com.nekokittygames.zmachine.tests;

import com.nekokittygames.zmachine.misc.ZRandom;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Katrina on 17/06/2015.
 */
public class RandomTests {




    @Test
    public void testModeChange()
    {
        ZRandom random=new ZRandom();
        assertFalse("Random should not start in predictable mode",random.isPredictable());

        random.setPredictable(10);
        assertTrue("Random should now be in predictable mode",random.isPredictable());

        random.setRandom();
        assertFalse("Random should not be in predictable mode",random.isPredictable());
    }



    @Test
    public void testPredictableResults()
    {
        ZRandom rand=new ZRandom();
        rand.setPredictable(1337);
        assertEquals(2, rand.getInt(10));
        assertEquals(5, rand.getInt(10));
        assertEquals(10, rand.getInt(10));
        assertEquals(23, rand.getInt(100));
        assertEquals(1014, rand.getInt(1337));
        assertEquals(89, rand.getInt(90));

        rand=new ZRandom();
        rand.setPredictable(1338);
        assertNotEquals(2,rand.getInt(10));
        assertNotEquals(5,rand.getInt(10));
        assertNotEquals(10,rand.getInt(10));
        assertNotEquals(23,rand.getInt(100));
        assertNotEquals(1014,rand.getInt(1337));
        assertNotEquals(89,rand.getInt(90));

        rand=new ZRandom();
        rand.setPredictable(10);
        assertEquals(1, rand.getInt(5));
        assertEquals(2, rand.getInt(5));
        assertEquals(3, rand.getInt(5));
        assertEquals(4, rand.getInt(5));
        assertEquals(5, rand.getInt(5));
        assertEquals(1, rand.getInt(5));
    }
}
