package com.nekokittygames.zmachine;

import com.nekokittygames.zmachine.blorb.BlorbFile;
import com.nekokittygames.zmachine.blorb.iff.chunks.RIdxChunk;
import com.nekokittygames.zmachine.blorb.iff.chunks.ZCodChunk;
import com.nekokittygames.zmachine.memory.Memory;
import com.nekokittygames.zmachine.memory.ZCallStack;
import com.nekokittygames.zmachine.memory.ZFrame;
import com.nekokittygames.zmachine.strings.ZStringManager;

import java.io.*;

/**
 * Created by Katrina on 16/02/2015.
 */
public class ZMachine {
    private Memory memory;
    private ZCallStack callStack;
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
}
