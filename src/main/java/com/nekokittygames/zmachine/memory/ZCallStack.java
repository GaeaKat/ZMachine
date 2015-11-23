package com.nekokittygames.zmachine.memory;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Created by katsw on 20/11/2015.
 */
public class ZCallStack {

    private final ZCallStackElement first;
    private ZCallStackElement last;

    private int size;
    public ZCallStack(ZFrame frame,short framePointer)
    {
        first=new ZCallStackElement(framePointer,frame,null);
        last=first;
        size=1;
    }

    public ZCallStack(ZFrame frame)
    {
        this(frame, (short) -1);
    }

    public class ZCallStackElement
    {
        private final short framePointer;
        private final ZFrame frame;
        private final ZCallStackElement prev;

        public ZCallStackElement(short framePointer,ZFrame frame,ZCallStackElement prev)
        {
            this.framePointer=framePointer;
            this.frame=frame;
            this.prev=prev;
        }
        public ZCallStackElement(ZFrame frame,ZCallStackElement prev)
        {
            this((short)-1,frame,prev);
        }

        public short getFramePointer() {
            return framePointer;
        }

        public ZFrame getFrame() {
            return frame;
        }
        public ZCallStackElement getPrev() { return prev; }
    }

    public ZCallStackElement pop()
    {

        if(first==last)
            throw new EmptyStackException();
        ZCallStackElement obj=last;
        last=obj.getPrev();
        size--;
        return obj;
    }

    public ZCallStackElement peek()
    {
        return last;
    }

    public boolean push(ZFrame frame,short framePointer)
    {
        if(framePointer!=-1) {
            ZCallStackElement element = last;
            while (element.prev != null) {
                if (element.getFramePointer()==framePointer)
                    return false;
                element=element.prev;
            }
        }
        last=new ZCallStackElement(framePointer,frame,last);
        size++;
        return true;
    }

    public boolean push(ZFrame frame)
    {
        return push(frame,(short)-1);
    }

    public int getSize()
    {
        return size;
    }
}
