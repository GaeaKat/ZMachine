package com.nekokittygames.zmachine.memory;

/**
 * Created by katsw on 20/11/2015.
 */
public class ZCallStack {

    public class ZCallStackElement
    {
        private final short framePointer;
        private final ZFrame frame;
        public ZCallStackElement(short framePointer,ZFrame frame)
        {
            this.framePointer=framePointer;
            this.frame=frame;
        }
        public ZCallStackElement(ZFrame frame)
        {
            this((short)-1,frame);
        }

        public short getFramePointer() {
            return framePointer;
        }

        public ZFrame getFrame() {
            return frame;
        }
    }

    
}
