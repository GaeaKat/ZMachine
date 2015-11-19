package com.nekokittygames.zmachine.misc;

import java.util.Random;

/**
 * Z-Machine Random Generator
 * Created by Katrina on 17/06/2015.
 */
public class ZRandom {

    public int[] sequence = new int[1000];
    private Random rand;
    private boolean predictable;
    private long seed;
    private int place;

    /**
     * Default Constructor
     */
    public ZRandom() {
        rand = new Random();
        place = 0;
        sequence = new int[1000];
    }


    /**
     * Returns mode the gen is in
     *
     * @return if the gen is in predictable mode
     */
    public boolean isPredictable() {
        return predictable;
    }

    /**
     * Sets the gen into predictable mode
     *
     * @param seed seed to use, if less than 1000 it uses sequence mode
     */
    public void setPredictable(long seed) {
        predictable = true;
        rand = new Random(seed);
        this.seed = seed;
        place = 0;
        sequence = new int[1000];
        for (int i = 0; i < sequence.length; i++) {
            sequence[i] = (i % (int) seed) + 1;
        }

    }

    /**
     * Sets the gen into random mode again
     */
    public void setRandom() {
        predictable = false;
        rand = new Random();
        place = 0;
        sequence = new int[1000];
    }

    /**
     * Gets next int in range
     *
     * @param range range of results
     * @return random or predictable number
     */
    public int getInt(int range) {

        if (isPredictable()) {
            if (seed < 1000) {

                return sequence[((place++) % (int) seed) % range];

            }
        }
        return rand.nextInt(range) + 1;
    }
}
