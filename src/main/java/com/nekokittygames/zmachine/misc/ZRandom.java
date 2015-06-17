package com.nekokittygames.zmachine.misc;

import java.util.Random;

/**
 * Created by Katrina on 17/06/2015.
 */
public class ZRandom {

    private Random rand;

    private boolean predictable;
    private long seed;
    private int place;
    public int[] sequence=new int[1000];
    public ZRandom()
    {
        rand=new Random();
        place=0;
        sequence=new int[1000];
    }



    public boolean isPredictable()
    {
        return predictable;
    }

    public void setPredictable(long seed)
    {
        predictable=true;
        rand=new Random(seed);
        this.seed=seed;
        place=0;
        sequence=new int[1000];
        for(int i=0;i<sequence.length;i++)
        {
            sequence[i]=(i%(int)seed)+1;
        }

    }

    public void setRandom()
    {
        predictable=false;
        rand=new Random();
        place=0;
        sequence=new int[1000];
    }

    // 1 2 3 4 5 6 7 8 9 10
    public int getInt(int range)
    {

        if(isPredictable())
        {
            if(seed<1000)
            {

                return sequence[((place++) % (int)seed)%range];

            }
        }
        return rand.nextInt(range)+1;
    }
}
