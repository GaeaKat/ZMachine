package com.nekokittygames.zmachine.memory;

import com.nekokittygames.zmachine.strings.ZString;
import com.nekokittygames.zmachine.strings.ZStringManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by katsw on 01/12/2015.
 */
public class ZObjectTree {

    private Memory memory;
    public ZObjectTree(Memory memory)
    {
        this.memory=memory;
        if(memory.getVersion()<4) {
            defaultProperties = new short[31];
        }
        else {
            defaultProperties = new short[63];
        }


        int current=0;
        int memSize=memory.getVersion()<4?31:63;
        for(int i=0;i<memSize;i++)
        {
            defaultProperties[i]= (short) memory.getWordu(memory.getObjects()+current);
            current+=2;
           // defaultProperties[i]=memory.getWordb(memory.getObjects()+(i*2));
        }

    }

    public ZObject getObject(int i)
    {
        int current=(memory.getVersion() <4? 31*2:63*2)+(memory.getVersion() <4?9:14)*(i-1);
        ZObject tmp=new ZObject();
        int num=memory.getVersion() <4 ? 4:6;
        byte[] attributes=new byte[num];
        for(int byt=0;byt<num;byt++)
        {
            attributes[byt]=memory.getByte(memory.getObjects()+current);
            current++;

        }
        tmp.setAttributes(attributes);
        num=memory.getVersion()<4?3:6;
        //byte[] assocs=new byte[num];
        //for(int byt=0;byt<num;byt++)
        //{
        //    assocs[byt]=memory.getByte(current);
        //    current++;
        //}

        if(memory.getVersion()>=4)
        {
            tmp.setParent(memory.getWordb(memory.getObjects()+current));
            current+=2;
            tmp.setSibling(memory.getWordb(memory.getObjects()+current));
            current+=2;
            tmp.setChild(memory.getWordb(memory.getObjects()+current));
            current+=2;
        }
        int properties=memory.getWordu(memory.getObjects()+current);
        byte nameSize=memory.getByte(properties);
        current=properties+1;
        tmp.setName(ZStringManager.Decode(current));
        current+=nameSize;
        tmp.setProperties(new byte[memory.getVersion() < 4?31:63][]);
        
        return tmp;
    }
    public short[] defaultProperties;
}
