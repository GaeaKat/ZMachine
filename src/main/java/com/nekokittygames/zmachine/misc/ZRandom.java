package com.nekokittygames.zmachine.misc;

import java.util.Random;

/**
 * Created by Katrina on 17/06/2015.
 */
public class ZRandom {

    private Random rand;

    private boolean predictable;
    private long seed;


    public ZRandom()
    {
        rand=new Random();
    }



    public boolean isPredictable()
    {
        return predictable;
    }

    public void setPredictable(long seed)
    {
        predictable=true;
        rand=new Random(seed);
    }

    public void setRandom()
    {
        predictable=false;
        rand=new Random();
    }

    public int getInt(int range)
    {
        return rand.nextInt(range)+1;
    }
}
