package com.nekokittygames.zmachine.memory;

import java.util.Stack;

/**
 * Created by katsw on 20/11/2015.
 */
public class ZFrame implements Cloneable{

    public enum ProcedureCall
    {
        NA,
        Function,
        Procedure,
        Interupt
    }
    private long PC;
    private Stack<Short> routineStack=new Stack<>();
    private short[] variables=new short[15];
    private ProcedureCall call=ProcedureCall.NA;
    private int numValues;
}
