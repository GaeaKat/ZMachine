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
        assertEquals(rand.getInt(10),2);
        assertEquals(rand.getInt(10), 5);
        assertEquals(rand.getInt(10), 10);
        assertEquals(rand.getInt(100),23);
        assertEquals(rand.getInt(1337), 1014);
        assertEquals(rand.getInt(90), 89);

        rand=new ZRandom();
        rand.setPredictable(1338);
        assertNotEquals(rand.getInt(10), 2);
        assertNotEquals(rand.getInt(10), 5);
        assertNotEquals(rand.getInt(10), 10);
        assertNotEquals(rand.getInt(100), 23);
        assertNotEquals(rand.getInt(1337), 1014);
        assertNotEquals(rand.getInt(90), 89);
    }
}
