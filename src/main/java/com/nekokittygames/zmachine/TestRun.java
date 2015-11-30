package com.nekokittygames.zmachine;

import com.google.common.io.Resources;
import com.google.common.primitives.UnsignedBytes;
import com.nekokittygames.zmachine.memory.ZCallStack;
import com.nekokittygames.zmachine.memory.ZFrame;

import javax.sound.midi.SysexMessage;
import java.io.*;

/**
 * Created by Katrina on 09/02/2015.
 */
public class TestRun {

    public static void main(String[] arg) throws IOException, IllegalAccessException, InstantiationException {
        /*BlorbFile file=new BlorbFile("C:\\Users\\Katrina\\Downloads\\The Pelican.zblorb");
        file.parseFile();
        RIdxChunk.Resource res=file.getResourceChunk().getResource("Exec",0);
        ZCodChunk chunk=(ZCodChunk)file.getResourceAt(res.Start);
        Memory mem=Memory.loadMemoryFromStream(chunk.image);

        System.out.println(mem.getVersion());
        System.out.println(mem.getStatusType());
        System.out.println(mem.getSplit());
        System.out.println(mem.isCensorMode());
        System.out.println(mem.isStatusLineAvailable());
        System.out.println(mem.isScreenSplittingAvailable());
        System.out.println(mem.isColoursAvailable());
        System.out.println(mem.getBoldfaceAvailable());
        System.out.println(mem.getItalicAvailable());
        System.out.println(mem.getFixedSpaceAvailable());
        System.out.println(mem.getSoundEffectsAvailable());
        System.out.println(mem.getTimedKeyboardAvailable());
        System.out.println(mem.getHighAddress());
        System.out.println(mem.getInitialPC());
        System.out.println(mem.getDictionary());
        System.out.println(mem.getObjects());
        System.out.println(mem.getGlobalVariables());
        System.out.println(mem.getStaticMemoryLoc());
        System.out.println(mem.getSerialCode());
        System.out.println(mem.getAbbrieviationsTable());
        System.out.println(mem.getFileSize()*4);
        System.out.println((int)mem.getChecksum());
        System.out.println(mem.getRoutineOffset());
        System.out.println(mem.getStringsOffset());
        System.out.println((int)mem.getTerminatingCharactersTable());
        System.out.println(mem.getRevisionNumber());
        System.out.println((int)mem.getAlphabetTable());
        System.out.println((int)mem.getHeaderExtension());
        System.out.println((int)mem.getHeaderExtentionLength());
        System.out.println((int)mem.getUnicodeTranslationTable());

        System.out.println(mem.getTranscriptingOn());
        System.out.println(mem.getForcedFixedPitch());
        System.out.println(mem.getPictureAvailable());
        System.out.println(mem.getUseUndo());
        System.out.println(mem.getUseMouse());
        System.out.println(mem.getUseColours());
        System.out.println(mem.getUseSound());
        System.out.println(mem.getUseMenu());*/

//        ZFrame frame=new ZFrame();
//        ZCallStack stack=new ZCallStack(frame);
//        ZFrame one=new ZFrame();
//        one.setPC(5);
//        stack.push(one,  123);
//        ZFrame two=new ZFrame();
//        two.setPC(10);
//        two.getRoutineStack().push( 30);
//        stack.push(two,  124);
//        ZFrame three=new ZFrame();
//        three.setPC(20);
//        stack.push(three,  125);
//        try
//        {
//            FileOutputStream fos=new FileOutputStream("/tmp/zmachine.ser");
//            ObjectOutputStream oos=new ObjectOutputStream(fos);
//            oos.writeObject(stack);
//            oos.close();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//        ZCallStack stack=null;
//        try
//        {
//            FileInputStream fis=new FileInputStream("/tmp/zmachine.ser");
//            ObjectInputStream ois=new ObjectInputStream(fis);
//            stack= (ZCallStack) ois.readObject();
//            ois.close();
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        System.out.println(stack.getSize());

        ZMachine machine=new ZMachine();
        machine.InitFromFile(Resources.getResource("Galatea.zblorb").getFile());
        System.out.println(UnsignedBytes.toString(machine.getMemory().getByte((int) (machine.getCallStack().peek().getFrame().getPC()))));
        System.out.println(UnsignedBytes.toString(machine.getMemory().getByte((int) (machine.getCallStack().peek().getFrame().getPC()+1))));
        machine.cycle();
        machine.cycle();
        machine.cycle();
        machine.cycle();
        machine.cycle();




    }
}
