package com.nekokittygames.zmachine;

import com.google.common.primitives.UnsignedBytes;
import com.nekokittygames.zmachine.blorb.BlorbFile;
import com.nekokittygames.zmachine.blorb.iff.chunks.RIdxChunk;
import com.nekokittygames.zmachine.blorb.iff.chunks.ZCodChunk;
import com.nekokittygames.zmachine.memory.*;
import com.nekokittygames.zmachine.misc.ZOP2;
import com.nekokittygames.zmachine.misc.ZOpForm;
import com.nekokittygames.zmachine.misc.ZOpType;
import com.nekokittygames.zmachine.misc.ZOperType;
import com.nekokittygames.zmachine.strings.ZStringManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.primitives.UnsignedBytes.compare;

/**
 * Created by Katrina on 16/02/2015.
 */
public class ZMachine {
    private Memory memory;
    private ZCallStack callStack;
    private ZObjectTree tree;
    private String fileName;
    public Memory getMemory()
    {
        return memory;
    }
    public ZCallStack getCallStack() { return callStack;}
    public void InitFromFile(String fileName) throws IOException {
        BlorbFile file=new BlorbFile(fileName);
        file.parseFile();
        RIdxChunk.Resource res=file.getResourceChunk().getResource("Exec",0);
        ZCodChunk chunk=(ZCodChunk)file.getResourceAt(res.Start);
        memory=Memory.loadMemoryFromStream(chunk.image);
        ZStringManager.InitializeManager(memory);
        InitZMachine(false);


    }


    public void InitZMachine(boolean restart) {
        memory.setStatusLineAvailable(true);
        memory.setScreenSplitting(false);
        memory.setVariablePitch(false);
        memory.setColoursAvailable(true);
        memory.setBoldfaceAvailable(true);
        memory.setItalicAvailable(true);
        memory.setFixedSpaceAvailable(true);
        memory.setSoundEffectsAvailable(false);
        memory.setTimedKeyboardAvailable(false);

        memory.setUsePictures(true);
        memory.setUseMouse(false);
        memory.setUseSound(false);
        memory.setUseMenu(false);
        memory.setInterpreterNumber((byte) 2);
        memory.setInterpreterVersion((byte) 1);
        memory.setScreenHeightLines((byte) 19);
        memory.setScreenWidthChars((byte) 49);
        callStack=new ZCallStack(new ZFrame());
        if(memory.getVersion()!=6) {
            callStack.peek().getFrame().setPC(memory.getInitialPC());

        }
        else
        {
            System.out.println("mem: "+memory.getInitialPacked());
            callStack.peek().getFrame().setPC(memory.getPackedAddress(memory.getInitialPacked())+1);
            callStack.peek().getFrame().setNumValues(memory.getByte(memory.getPackedAddress(memory.getInitialPacked())));
        }
        tree=new ZObjectTree(memory);
    }

    public ZObjectTree getTree() {
        return tree;
    }

    public void save_machine(String file)
    {
        try
        {
            FileOutputStream fos=new FileOutputStream(file);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(callStack);
            byte[] array=memory.getDynamicMem();
            oos.writeInt(array.length);
            oos.write(array);
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load_machine(String file)
    {
        try {

            FileInputStream fis=new FileInputStream(file);
            ObjectInputStream ois=new ObjectInputStream(fis);
            callStack= (ZCallStack) ois.readObject();
            int len=ois.readInt();
            byte[] mem=new byte[len];
            ois.read(mem,0,len);
            memory.setDynamicMem(mem);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void cycle() {
        ZFrame currentFrame=callStack.peek().getFrame();
        long PC = currentFrame.getPC();
        long nextPC = 1;

        byte nextInst = memory.getByte((int) PC);
        byte opCode=0;
        ZOpType type=null;

        ZOpForm form=null;


        byte bitForm = (byte) ((nextInst & 0xFF) >> 6);

        switch (bitForm) {
            case 0b11:
                form = ZOpForm.VARIABLE;
                break;
            case 0b10:
                form = ZOpForm.SHORT;
                break;
            default:
                form = ZOpForm.LONG;
        }
        if (compare(nextInst, (byte) 0xBE)==0)
            form = ZOpForm.EXTENDED;
        ZOperType[] operTypes=new ZOperType[]{  ZOperType.OMMITED,ZOperType.OMMITED,ZOperType.OMMITED,ZOperType.OMMITED,
                                                ZOperType.OMMITED,ZOperType.OMMITED,ZOperType.OMMITED,ZOperType.OMMITED};
        long operands[] = new long[8];
        switch(form)
        {
            case LONG:
                type=ZOpType.OP2;
                opCode= (byte) (nextInst&0x1F);
                break;
            case SHORT:
                type= (nextInst&0x18) >> 3 == 3 ? ZOpType.OP0 : ZOpType.OP1;
                opCode= (byte) (nextInst&0xF);
                break;
            case VARIABLE:
                byte byt= (byte) ((nextInst&0x20) >> 5);
                type=byt == 1 ? ZOpType.VAR:ZOpType.OP2;
                opCode=(byte) (nextInst&0x1F);
                break;
            case EXTENDED:
                type=ZOpType.VAR;
                opCode=memory.getByte((int) (PC+1));
                nextPC++;
                break;
        }

        switch(form)
        {
            case SHORT:
                if(type==ZOpType.OP1)
                {
                    operTypes[0]=ZOperType.getType((byte) ((nextInst&0x30) >> 4));
                }
                break;
            case LONG:
                operTypes[0]=(nextInst&0x40) >> 6 ==0? ZOperType.SMALL_CONSTANT:ZOperType.VARIABLE;
                operTypes[1]=(nextInst&0x20) >> 5 ==0? ZOperType.SMALL_CONSTANT:ZOperType.VARIABLE;
                break;
            case VARIABLE:
            case EXTENDED:
                byte types=memory.getByte((int) (PC+nextPC));
                nextPC++;
                int count=0;
                operTypes[0]=ZOperType.getType ((byte) ((types&0xFF) >> 6));
                operTypes[1]=ZOperType.getType ((byte) ((types&0xFF) >> 4));
                operTypes[2]=ZOperType.getType ((byte) ((types&0xFF) >> 2));
                operTypes[3]=ZOperType.getType ((byte) ((types&0xFF) ));

                if(nextInst==236 || nextInst==250)
                {
                    byte types2=memory.getByte((int) (PC+nextPC));
                    nextPC++;
                    operTypes[4]=ZOperType.getType ((byte) ((types2&0xFF) >> 6));
                    operTypes[5]=ZOperType.getType ((byte) ((types2&0xFF) >> 4));
                    operTypes[6]=ZOperType.getType ((byte) ((types2&0xFF) >> 2));
                    operTypes[7]=ZOperType.getType ((byte) ((types2&0xFF) ));
                }
                break;
        }
        for(int i=0;i<8;i++)
        {
            switch (operTypes[i])
            {
                case OMMITED:
                    break;
                case SMALL_CONSTANT:
                case VARIABLE:
                    operands[i]=memory.getByte((int) (PC+nextPC));
                    nextPC+=1;
                    break;
                case LARGE_CONSTANT:
                    operands[i]=memory.getWordu((int) (PC+nextPC));
                    nextPC+=2;
                    break;
            }
        }
        switch(type)
        {
            case OP2:
            {
                System.out.print(ZOP2.values()[opCode-1]+" ");
                System.out.print(operTypes[0] + " - "+operands[0] + ", ");
                System.out.print(operTypes[1] + " - "+operands[1]);
                switch(opCode) {
                    case 0x1:

                        byte offset = memory.getByte((int) (PC + nextPC));
                        nextPC++;
                        boolean branchPositive = ((offset & 0xFF) >> 7) == 1;
                        boolean shortOffset = ((offset & 0x40) >> 6) == 1;
                        long offsetAmount = 0;
                        if (shortOffset) {
                            offsetAmount = offset & 0x3F;
                        } else {
                            byte second = memory.getByte((int) (PC + nextPC));
                            nextPC++;
                            short tmp = (short) (((offset & 0x3F) << 8) | second);
                            offsetAmount = fromTwoComplement(tmp, 14);

                        }
                        System.out.println((branchPositive ? " [TRUE] " : "[FALSE] ") + " - " + offsetAmount);

                        if (getValue(operTypes, operands, 0) == getValue(operTypes, operands, 1)) {
                            if (branchPositive) {
                                nextPC = nextPC + offsetAmount - 2;
                            }
                        } else {
                            if (!branchPositive)
                                nextPC = nextPC + offsetAmount - 2;
                        }
                        break;
                    case 0x2:
                        offset = memory.getByte((int) (PC + nextPC));
                        nextPC++;
                        branchPositive = ((offset & 0xFF) >> 7) == 1;
                        shortOffset = ((offset & 0x40) >> 6) == 1;
                        offsetAmount = 0;
                        if (shortOffset) {
                            offsetAmount = offset & 0x3F;
                        } else {
                            byte second = memory.getByte((int) (PC + nextPC));
                            nextPC++;
                            short tmp = (short) (((offset & 0x3F) << 8) | second);
                            offsetAmount = fromTwoComplement(tmp, 14);

                        }
                        System.out.println((branchPositive ? " [TRUE] " : "[FALSE] ") + " - " + offsetAmount);
                        if (getValue(operTypes, operands, 0) < getValue(operTypes, operands, 1)) {
                            if (branchPositive) {
                                nextPC = nextPC + offsetAmount - 2;
                            }
                        } else {
                            if (!branchPositive)
                                nextPC = nextPC + offsetAmount - 2;
                        }
                        break;
                    case 0x3:
                        offset = memory.getByte((int) (PC + nextPC));
                        nextPC++;
                        branchPositive = ((offset & 0xFF) >> 7) == 1;
                        shortOffset = ((offset & 0x40) >> 6) == 1;
                        offsetAmount = 0;
                        if (shortOffset) {
                            offsetAmount = offset & 0x3F;
                        } else {
                            byte second = memory.getByte((int) (PC + nextPC));
                            nextPC++;
                            short tmp = (short) (((offset & 0x3F) << 8) | second);
                            offsetAmount = fromTwoComplement(tmp, 14);

                        }
                        System.out.println((branchPositive ? " [TRUE] " : "[FALSE] ") + " - " + offsetAmount);
                        if (getValue(operTypes, operands, 0) > getValue(operTypes, operands, 1)) {
                            if (branchPositive) {
                                nextPC = nextPC + offsetAmount - 2;
                            }
                        } else {
                            if (!branchPositive)
                                nextPC = nextPC + offsetAmount - 2;
                        }
                        break;
                    case 0x4:
                        offset = memory.getByte((int) (PC + nextPC));
                        nextPC++;
                        branchPositive = ((offset & 0xFF) >> 7) == 1;
                        shortOffset = ((offset & 0x40) >> 6) == 1;
                        offsetAmount = 0;
                        if (shortOffset) {
                            offsetAmount = offset & 0x3F;
                        } else {
                            byte second = memory.getByte((int) (PC + nextPC));
                            nextPC++;
                            short tmp = (short) (((offset & 0x3F) << 8) | second);
                            offsetAmount = fromTwoComplement(tmp, 14);

                        }
                        System.out.println((branchPositive ? " [TRUE] " : "[FALSE] ") + " - " + offsetAmount);
                        setVariable((byte) operands[0], (short) (getVariable((byte) operands[0]) - 1));
                        if (getVariable((byte) operands[1]) < getValue(operTypes, operands, 1)) {
                            if (branchPositive) {
                                nextPC = nextPC + offsetAmount - 2;
                            }
                        } else {
                            if (!branchPositive) {
                                nextPC = nextPC + offsetAmount - 2;
                            }
                        }
                        break;
                    case 0x5:
                        offset = memory.getByte((int) (PC + nextPC));
                        nextPC++;
                        branchPositive = ((offset & 0xFF) >> 7) == 1;
                        shortOffset = ((offset & 0x40) >> 6) == 1;
                        offsetAmount = 0;
                        if (shortOffset) {
                            offsetAmount = offset & 0x3F;
                        } else {
                            byte second = memory.getByte((int) (PC + nextPC));
                            nextPC++;
                            short tmp = (short) (((offset & 0x3F) << 8) | second);
                            offsetAmount = fromTwoComplement(tmp, 14);

                        }
                        System.out.println((branchPositive ? " [TRUE] " : "[FALSE] ") + " - " + offsetAmount);
                        setVariable((byte) operands[0], (short) (getVariable((byte) operands[0]) + 1));
                        if (getVariable((byte) operands[1]) > getValue(operTypes, operands, 1)) {
                            if (branchPositive) {
                                nextPC = nextPC + offsetAmount - 2;
                            }
                        } else {
                            if (!branchPositive) {
                                nextPC = nextPC + offsetAmount - 2;
                            }
                        }
                        break;
                    case 0x6:
                        offset = memory.getByte((int) (PC + nextPC));
                        nextPC++;
                        branchPositive = ((offset & 0xFF) >> 7) == 1;
                        shortOffset = ((offset & 0x40) >> 6) == 1;
                        offsetAmount = 0;
                        if (shortOffset) {
                            offsetAmount = offset & 0x3F;
                        } else {
                            byte second = memory.getByte((int) (PC + nextPC));
                            nextPC++;
                            short tmp = (short) (((offset & 0x3F) << 8) | second);
                            offsetAmount = fromTwoComplement(tmp, 14);

                        }
                        System.out.println((branchPositive ? " [TRUE] " : "[FALSE] ") + " - " + offsetAmount);
                        if (tree.getObject((int) operands[1]).getParent() == operands[0]) {
                            if (branchPositive)
                                nextPC = nextPC + offsetAmount - 2;
                        } else if (!branchPositive)
                            nextPC = nextPC + offsetAmount - 2;

                        break;
                    case 0x7:
                        offset = memory.getByte((int) (PC + nextPC));
                        nextPC++;
                        branchPositive = ((offset & 0xFF) >> 7) == 1;
                        shortOffset = ((offset & 0x40) >> 6) == 1;
                        offsetAmount = 0;
                        if (shortOffset) {
                            offsetAmount = offset & 0x3F;
                        } else {
                            byte second = memory.getByte((int) (PC + nextPC));
                            nextPC++;
                            short tmp = (short) (((offset & 0x3F) << 8) | second);
                            offsetAmount = fromTwoComplement(tmp, 14);

                        }
                        System.out.println((branchPositive ? " [TRUE] " : "[FALSE] ") + " - " + offsetAmount);
                        if ((operands[0] & operands[1]) == operands[1]) {
                            if (branchPositive)
                                nextPC = nextPC + offsetAmount - 2;
                        } else if (!branchPositive)
                            nextPC = nextPC + offsetAmount - 2;


                        break;
                    case 0x8:
                        byte variable=memory.getByte((int)(PC+nextPC));
                        nextPC++;
                        setVariable(variable, (short) (operands[0] | operands[1]));
                        break;
                    case 0x9:
                        variable=memory.getByte((int)(PC+nextPC));
                        nextPC++;
                        setVariable(variable, (short) (operands[0] & operands[1]));
                        break;
                    case 0xA:
                        offset = memory.getByte((int) (PC + nextPC));
                        nextPC++;
                        branchPositive = ((offset & 0xFF) >> 7) == 1;
                        shortOffset = ((offset & 0x40) >> 6) == 1;
                        offsetAmount = 0;
                        if (shortOffset) {
                            offsetAmount = offset & 0x3F;
                        } else {
                            byte second = memory.getByte((int) (PC + nextPC));
                            nextPC++;
                            short tmp = (short) (((offset & 0x3F) << 8) | second);
                            offsetAmount = fromTwoComplement(tmp, 14);

                        }
                        System.out.println((branchPositive ? " [TRUE] " : "[FALSE] ") + " - " + offsetAmount);
                        ZObject obj=tree.getObject((int) operands[0]);
                        if(obj.getAttributes().get((int)operands[1]))
                        {
                            if(branchPositive)
                                nextPC = nextPC + offsetAmount - 2;
                        }
                        else
                            if(!branchPositive)
                                nextPC = nextPC + offsetAmount - 2;
                        break;
                    case 0xB:
                        obj=tree.getObject((int) operands[0]);
                        obj.getAttributes().set((int)operands[1]);
                        tree.setObject((int) operands[0],obj);
                        break;
                    case 0xC:
                        obj=tree.getObject((int) operands[0]);
                        obj.getAttributes().clear((int)operands[1]);
                        tree.setObject((int) operands[0],obj);
                        break;
                    case 0xD:
                        setVariable((byte)operands[0],(short)operands[1]);
                        break;
                    case 0xE:
                        ZObject obj1=tree.getObject((int) operands[0]);
                        ZObject obj2=tree.getObject((int) operands[1]);
                        obj1.setParent((int) operands[1]);
                        obj1.setSibling(obj2.getChild());
                        obj2.setChild((int) operands[0]);
                        tree.setObject((int) operands[0],obj1);
                        tree.setObject((int) operands[1],obj2);
                        break;
                    case 0xF:
                        variable=memory.getByte((int)(PC+nextPC));
                        nextPC++;
                        setVariable(variable, (short) memory.getWordu((int) (operands[0]+(2*operands[1]))));
                        break;
                    case 0x10:
                        variable=memory.getByte((int)(PC+nextPC));
                        nextPC++;
                        setVariable(variable, (short) memory.getByte((int)(operands[0]+(2*operands[1]))));
                        break;
                    case 0x11:
                        variable=memory.getByte((int)(PC+nextPC));
                        nextPC++;
                        setVariable(variable,tree.getProperty((int)operands[0],(int)operands[1]));
                        break;
                    case 0x12:
                        variable=memory.getByte((int)(PC+nextPC));
                        nextPC++;
                        setVariable(variable,tree.getPropertyAddress((int)operands[0],(int)operands[1]));
                        break;
                    case 0x13:
                        variable=memory.getByte((int)(PC+nextPC));
                        nextPC++;
                        setVariable(variable,tree.getNextProperty((int)operands[0],(int)operands[1]));
                        break;
                    case 0x14:
                        variable=memory.getByte((int)(PC+nextPC));
                        nextPC++;
                        setVariable(variable, (short) (operands[0]+operands[1]));
                        break;
                    case 0x15:
                        variable=memory.getByte((int)(PC+nextPC));
                        nextPC++;
                        setVariable(variable, (short) (operands[0]-operands[1]));
                        break;
                    case 0x16:
                        variable=memory.getByte((int)(PC+nextPC));
                        nextPC++;
                        setVariable(variable, (short) (operands[0]*operands[1]));
                        break;
                    case 0x17:
                        variable=memory.getByte((int)(PC+nextPC));
                        nextPC++;
                        setVariable(variable, (short) (operands[0]/operands[1]));
                        break;
                    case 0x18:
                        variable=memory.getByte((int)(PC+nextPC));
                        nextPC++;
                        setVariable(variable, (short) (operands[0]%operands[1]));
                        break;
                    case 0x19:
                        variable=memory.getByte((int)(PC+nextPC));
                        nextPC++;
                        ZFrame frame=constructCall( memory.getPackedAddress((int) operands[0]),variable,new Object[]{operands[1]});
                        callStack.push(frame);
                        break;
                    case 0x1A:
                        frame=constructCall( memory.getPackedAddress((int) operands[0]), (byte) 0,new Object[]{operands[1]});
                        callStack.push(frame);
                        break;

                }
            }
            case VAR:
            {
                switch(opCode)
                {
                    case 0x0:
                        System.out.print("CALL");
                        if(memory.getVersion()>=4)
                            System.out.print("_VS");
                        System.out.print(" "+operTypes[0] + " - "+operands[0]+" -> ");
                        byte retVal=memory.getByte((int) (PC+nextPC));
                        nextPC++;
                        if(UnsignedBytes.compare((byte)0,retVal)==0)
                        System.out.println();
                        else
                        System.out.println(UnsignedBytes.toString(retVal));
                        int cnt=1;
                        ZOperType typ=operTypes[cnt];
                        List<Short> args=new ArrayList<Short>();

                        while(typ!=ZOperType.OMMITED)
                        {
                            if(typ==ZOperType.VARIABLE)
                                args.add(getVariable((byte) operands[cnt]));
                            else
                                args.add((short) operands[cnt]);
                            cnt++;
                        }
                        ZFrame frame=constructCall( memory.getPackedAddress((int) operands[0]),retVal,args.toArray());
                        callStack.push(frame);
                        break;
                    case 0x19:
                        System.out.print("CALL_VN");
                        System.out.println(" "+operTypes[0] + " - "+operands[0]);
                        cnt=1;
                        typ=operTypes[cnt];
                        args=new ArrayList<Short>();

                        while(typ!=ZOperType.OMMITED)
                        {
                            if(typ==ZOperType.VARIABLE)
                                args.add(getVariable((byte) operands[cnt]));
                            else
                                args.add((short) operands[cnt]);
                            cnt++;
                            typ=operTypes[cnt];
                        }
                        ZFrame framec=constructCall( memory.getPackedAddress((int) operands[0]), (byte) 0,args.toArray());
                        callStack.push(framec);


                }
            }
            case OP0:
            {
                switch (opCode)
                {
                    case 0xA:
                        System.out.println("QUIT");
                }
            }
        }

        currentFrame.setPC(PC+nextPC);
    }

    private long getValue(ZOperType[] operTypes, long[] operands, int count) {
        if(operTypes[count]!=ZOperType.VARIABLE)
            return operands[count];
        else
            return getVariable((byte) operands[count]);
    }
    public static int fromTwoComplement(int value, int bitSize) {
        int shift = Integer.SIZE - bitSize;
        // shift sign into position
        int result  = value << shift;
        // Java right shift uses sign extension, but only works on integers or longs
        result = result >> shift;
        return result;
    }

    public ZFrame constructCall(int address, byte retVal, Object[] arguments)
    {
        ZFrame frame=new ZFrame();
        frame.setRetVal(retVal);
        int currentAdd=address;
        frame.setNumValues(memory.getByte(currentAdd));
        currentAdd++;
        if(memory.getVersion()<=4)
        {
            for(int i=0;i<frame.getNumValues();i++)
            {
                frame.getVariables()[i]= (short) memory.getWordu(currentAdd);
                currentAdd+=2;
            }
        }
        else
        {
            for(int i=0;i<frame.getNumValues();i++)
            {
                frame.getVariables()[i]= (short) 0;
            }
        }
        int cnt=0;
        if(arguments.length!=0) {
            for (Object arg : arguments) {
                frame.getVariables()[cnt++] = (Short)arg;
            }
        }

        frame.setPC(currentAdd);
        return frame;
    }
    public short getVariable(byte var)
    {
        if(var>0 && var<0x10)
        {
            return callStack.peek().getFrame().getVariables()[var-1];
        }
        if(var==0)
        {
            return callStack.peek().getFrame().getRoutineStack().pop();
        }
        return (short) memory.getWordu(memory.getGlobalVariables()+var-0x10);
    }

    public void setVariable(byte var,short value) {
        if (var > 0 && var < 0x10) {
            callStack.peek().getFrame().getVariables()[var - 1] = value;
        } else if (var == 0)
            callStack.peek().getFrame().getRoutineStack().push(value);
        else
            memory.setWordb(memory.getGlobalVariables() + var - 0x10, value);
    }
}
