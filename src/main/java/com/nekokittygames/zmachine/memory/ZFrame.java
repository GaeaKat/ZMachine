package com.nekokittygames.zmachine.memory;

import java.io.Serializable;
import java.util.Stack;

/**
 * Created by katsw on 20/11/2015.
 */
public class ZFrame implements Cloneable, Serializable{

    static final long serialVersionUID = 42L;
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

    public long getPC() {
        return PC;
    }

    public void setPC(long PC) {
        this.PC = PC;
    }

    public Stack<Short> getRoutineStack() {
        return routineStack;
    }

    public void setRoutineStack(Stack<Short> routineStack) {
        this.routineStack = routineStack;
    }

    public short[] getVariables() {
        return variables;
    }

    public void setVariables(short[] variables) {
        this.variables = variables;
    }

    public ProcedureCall getCall() {
        return call;
    }

    public void setCall(ProcedureCall call) {
        this.call = call;
    }

    public int getNumValues() {
        return numValues;
    }

    public void setNumValues(int numValues) {
        this.numValues = numValues;
    }
}
