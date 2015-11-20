package com.nekokittygames.zmachine.strings;

import com.nekokittygames.zmachine.memory.Memory;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by katsw on 20/11/2015.
 */
public final class ZDecode {

    private ZTranslate translator;
    private ZDecode abbreviationsDecoder;

    public ZDecode(ZTranslate translator)
    {
        this.translator=translator;
    }


    public String decode2Zscii(final Memory mem,final int address,final int length)
    {
        final StringBuilder builder=new StringBuilder();

        translator.reset();
        final char[] zBytes=extractZbytes(mem,address,length);
        char zChar;
        int i=0,newpos;

        while(i<zBytes.length)
        {
            boolean decoded=false;
            zChar=zBytes[i];
            newpos=handleAbbreviation(builder,mem,zBytes,i);
            decoded=(newpos>i);
            i=newpos;
            if(!decoded)
            {
                newpos=handleEscapeA2(builder,zBytes,i);
                decoded=newpos>i;
            }
            if(!decoded)
            {
                decodeZChar(builder,zChar);
                i++;
            }
        }

        return builder.toString();
    }

    private int handleAbbreviation(final StringBuilder builder,final  Memory mem,final  char[] data, int pos) {
        int position=pos;

        final char zChar=data[position];
        if(translator.isAbbreviation(zChar))
        {
            if(position<(data.length-1))
            {
                position++;

                if(mem.getAbbrieviationsTable()>0)
                {
                    final int x=data[position];
                    final int entryNum=32*(zChar-1)+x;
                    final int entryAddress=mem.getWordb(mem.getAbbrieviationsTable()+entryNum*2)*2;
                    createAbbreviationDecoder();
                    appendAbbreviationAtAddress(mem,entryAddress,builder);
                }
            }
            position++;
        }
        return position;
    }

    private void appendAbbreviationAtAddress(Memory mem, int entryAddress, StringBuilder builder) {
        if(abbreviationsDecoder!=null)
        {
            final String abbrev=abbreviationsDecoder.decode2Zscii(mem,entryAddress,0);
            builder.append(abbrev);
        }
    }


    private void createAbbreviationDecoder() {
        if(abbreviationsDecoder==null) {
            try {
                abbreviationsDecoder = new ZDecode((ZTranslate) translator.clone());

            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }


    }

    private int handleEscapeA2(final StringBuilder builder,final char[] data,final int pos)
    {
        int position=pos;
        if(translator.willEscapeAO(data[position]))
        {
            if(position<data.length-2)
            {
                joinToZsciiChar(builder,data[position+1],data[position+2]);
                position+=2;
            }
            position++;
            translator.resetTolast();
        }
        return position;
    }

    public char decodeZChar(final char zChar)
    {
        if(ZSCII.isAscii(zChar) || ZSCII.isAccent(zChar))
        {
            return zChar;
        }
        else
        {
            return translator.translate(zChar);
        }
    }

    private void decodeZChar(final StringBuilder builder,final char zChar)
    {
        final char c=decodeZChar(zChar);
        if(c!=0)
            builder.append(c);
    }

    public ZTranslate getTranslator()
    {
        return translator;
    }


    public static boolean isEndWord(final char zWord)
    {
        return (zWord&0x8000)>0;
    }

    public static char[] extractZbytes(final Memory mem,final int address,final int length)
    {
        char zWord=0;
        int currentAddr=address;
        final List<char[]> byteList=new ArrayList<char[]>();

        do {
            zWord= (char) mem.getWordb(currentAddr);
            byteList.add(extractZEncodedBytes(zWord));
            currentAddr+=2;

            if(length>0 && (currentAddr-address)>length)
            {
                break;
            }

        } while(!isEndWord(zWord));

        final char[] result=new char[byteList.size()*3];
        int i=0;
        for(char[] triplet : byteList)
        {
            for(char b:triplet)
            {
                result[i++]=b;
            }
        }
        return result;
    }

    public int getNumZEncodedBytes(Memory mem,int address)
    {
        char zWord=0;
        int currentAddress=address;
        do {
            zWord= (char) mem.getWordb(currentAddress);
            currentAddress+=2;

        }while(!isEndWord(zWord));
        return currentAddress-address;
    }

    private static char[] extractZEncodedBytes(final char zWord)
    {
        final char[] result=new char[3];
        result[2]=(char) (zWord & 0x1f);
        result[1] = (char) ((zWord >> 5) & 0x1f);
        result[0] = (char) ((zWord >> 10) & 0x1f);
        return result;
    }

    private static void joinToZsciiChar(final StringBuilder builder,final char top,final char bottom)
    {
        builder.append((char) (top << 5 | bottom));
    }

}
