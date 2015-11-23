package com.nekokittygames.zmachine.tests;

import com.nekokittygames.zmachine.memory.ZCallStack;
import com.nekokittygames.zmachine.memory.ZFrame;
import org.junit.Assert;
import org.junit.Test;

import java.util.EmptyStackException;

import static org.junit.Assert.*;

/**
 * Created by nekosune on 23/11/15.
 */
public class CallStackTests {


    @Test
    public void CreateCallStack()
    {
        ZFrame frame=new ZFrame();
        ZCallStack stack=new ZCallStack(frame);
        assertEquals("Must be the same frame",frame,stack.peek().getFrame());
        assertEquals("Must have -1 word",-1,stack.peek().getFramePointer());
        assertEquals("Must have length of 1",1,stack.getSize());
    }

    @Test(expected = EmptyStackException.class)
    public void PopEmptyStack()
    {

        ZFrame frame=new ZFrame();
        ZCallStack stack=new ZCallStack(frame);
        ZCallStack.ZCallStackElement element=stack.pop();

    }

    @Test
    public void PushOntoStack()
    {
        ZFrame frame=new ZFrame();
        ZCallStack stack=new ZCallStack(frame);
        ZFrame one=new ZFrame();
        stack.push(one);
        assertEquals("Size must be 2",2,stack.getSize());
        assertEquals("Top must be frame one",one,stack.peek().getFrame());
    }

    @Test
    public void PopOffStack()
    {

        ZFrame frame=new ZFrame();
        ZCallStack stack=new ZCallStack(frame);
        ZFrame one=new ZFrame();
        stack.push(one);
        ZCallStack.ZCallStackElement oneElm=stack.pop();
        assertEquals("Frame must be last pushed",one,oneElm.getFrame());
        assertEquals("Stack must only have 1",1,stack.getSize());
        assertNotEquals("Top Stack Element must not be popped",one,stack.peek().getFrame());
    }

    @Test
    public void PushDifferentPointers()
    {
        ZFrame frame=new ZFrame();
        ZCallStack stack=new ZCallStack(frame);
        ZFrame one=new ZFrame();
        assertTrue("Must work on empty",stack.push(one, (short) 123));
        assertEquals("Pointer must be 123",123,stack.peek().getFramePointer());
        ZFrame two=new ZFrame();
        assertTrue("Must work on different",stack.push(two, (short) 124));
        assertEquals("Pointer must be 124",124,stack.peek().getFramePointer());
    }


    public void PushSamePointer()
    {
        ZFrame frame=new ZFrame();
        ZCallStack stack=new ZCallStack(frame);
        ZFrame one=new ZFrame();
        assertTrue("Must work on empty",stack.push(one, (short) 123));
        ZFrame two=new ZFrame();
        assertFalse("Must fail on same pointer",stack.push(two, (short) 123));
    }

}
