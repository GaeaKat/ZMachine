package com.nekokittygames.zmachine.memory;

import com.nekokittygames.zmachine.strings.ZString;
import com.nekokittygames.zmachine.strings.ZStringManager;

import java.nio.ByteBuffer;
import java.util.*;

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

    public void setObject(int pos,ZObject obj) {
        int current=(memory.getVersion() <4? 31*2:63*2)+(memory.getVersion() <4?9:14)*(pos-1);
        byte[] attributes=obj.getAttributes().toByteArray();
        byte[] attributeBytes=new byte[memory.getVersion() <4 ? 4:6];
        System.arraycopy(attributes,0,attributeBytes,0,attributes.length);
        for(int i=0;i<attributeBytes.length;i++)
        {
            memory.setByte(memory.getObjects()+current++,attributeBytes[i]);
        }

        if(memory.getVersion()>=4) {
            memory.setWordb(memory.getObjects()+current,(short)obj.getParent());
            current+=2;
            memory.setWordb(memory.getObjects()+current,(short)obj.getSibling());
            current+=2;
            memory.setWordb(memory.getObjects()+current,(short)obj.getChild());
            current+=2;
        }
        else
        {
            memory.setByte(memory.getObjects()+current++,(byte)obj.getParent());
            memory.setByte(memory.getObjects()+current++,(byte)obj.getSibling());
            memory.setByte(memory.getObjects()+current++,(byte)obj.getChild());
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
        tmp.setAttributes(BitSet.valueOf(attributes));
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
        else
        {
            tmp.setParent(memory.getByte(memory.getObjects()+current++));
            tmp.setSibling(memory.getByte(memory.getObjects()+current++));
            tmp.setChild(memory.getByte(memory.getObjects()+current++));
        }
        int properties=memory.getWordu(memory.getObjects()+current);
        byte nameSize=memory.getByte(properties);
        current=properties+1;
        tmp.setName(ZStringManager.Decode(current));
        current+=nameSize*2;
        tmp.setProperties(new ZProperty[memory.getVersion() < 4?31:63]);
        int cnt=0;
        byte byt=memory.getByte(current++);
        int startPos=current;
        while(byt!=0)
        {
            int amt=byt;
            int pos=(memory.getVersion() < 4?31:63)-cnt++;
            if(memory.getVersion()>3)
            {
                byte two=-1;
                byte tmpa= (byte) (byt&0xFF);
                byte tmpb= (byte) (tmpa >> 7);
                if(tmpb !=0 )
                {
                    two=memory.getByte(current++);
                    amt=two&0x3F;
                    if(amt==0)
                        amt=64;
                    pos=byt&0x3F;
                }
                else
                {
                    pos=byt&0x3F;
                    amt=((byt&0x40 )>> 6)==1?2:1;
                }
            }
            byte[] array=new byte[amt];
            for(int bytNum=0;bytNum<amt;bytNum++)
                array[bytNum]=memory.getByte(current++);
            tmp.getProperties()[pos]=new ZProperty(array,startPos);
            byt=memory.getByte(current++);
        }
        return tmp;
    }
    public short[] defaultProperties;

    public short getProperty(int i,int prop)
    {
        ZObject obj=getObject(i);
        ZProperty tmp=obj.getProperty(prop);
        if(tmp==null)
            return defaultProperties[prop];
        ByteBuffer buff=ByteBuffer.wrap(tmp.getBytes());
        return buff.getShort();
    }
    public short getPropertyAddress(int i,int prop)
    {
        ZObject obj=getObject(i);
        ZProperty tmp=obj.getProperty(prop);
        if(tmp==null)
            return 0;
        return (short) tmp.getAddress();
    }

    public short getNextProperty(int i,int prop)
    {
        ZObject obj=getObject(i);
        for(int j=prop;j<obj.getProperties().length;j++)
        {
            if(obj.getProperty(j)!=null)
                return (short) j;
        }
        return 0;
    }
}
