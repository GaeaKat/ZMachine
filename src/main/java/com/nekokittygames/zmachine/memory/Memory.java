package com.nekokittygames.zmachine.memory;

import com.nekokittygames.zmachine.misc.ZNumber;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * Represents the memory of the Z-Machine
 * Created by Katrina on 15/02/2015.
 */
public class Memory {

    protected final byte[] memory;


    /**
     * Constructs a new memory of the correct size according to the version
     *
     * @param version Version of the machine
     */
    public Memory(byte version) {
        switch (version) {
            case 1:
            case 2:
            case 3:
                memory = new byte[128000];
                break;
            case 4:
            case 5:
                memory = new byte[256000];
                break;
            case 6:
            case 7:
            case 8:
                memory = new byte[512000];
                break;
            default:
                memory = new byte[512000];

        }
        memory[0] = version;
    }

    /**
     * Loads memory from a byte array
     *
     * @param stream byte array to load
     * @return Constructed memory
     */
    public static Memory loadMemoryFromStream(byte[] stream) {
        byte version = stream[0];
        Memory mem = new Memory(version);
        System.arraycopy(stream, 1, mem.memory, 1, stream.length - 1);

        return mem;
    }

    public static boolean isBitSet(byte byt, int num) {
        BitSet set = BitSet.valueOf(new byte[]{byt});
        return set.get(num);
    }

    public static byte setBit(byte byt, int num, boolean bit) {
        BitSet set = BitSet.valueOf(new byte[]{byt});
        set.set(num, bit);
        if(set.isEmpty())
            return 0;
        return set.toByteArray()[0];
    }

    /**
     * Returns File version
     *
     * @return file version
     */
    public byte getVersion() {
        return memory[0];
    }

    public int getPackedAddress(int address) {
        return getPackedAddress(address, false);
    }

    /**
     * Returns the correct packed address for mem version
     *
     * @param address packed address
     * @param string  Is this a z-string or not?
     * @return Unpacked address
     */
    public int getPackedAddress(int address, boolean string) {
        switch (getVersion()) {
            case 1:
            case 2:
            case 3:
                return 2 * address;

            case 4:
            case 5:
                return 4 * address;
            case 6:
            case 7:
                if (string)
                    return 4 * address + 8 * getStringsOffset();
                else
                    return 4 * address + 8 * getRoutineOffset();
            case 8:
                return 8 * address;
            default:
                return address;
        }
    }

    public byte getByte(int pos)
    {
        byte[] num = ArrayUtils.subarray(memory, pos, pos + 1);
        ByteBuffer wrapped = ByteBuffer.wrap(num);

        return  wrapped.get();

    }
    public short getWordb(int pos) {
        byte[] num = ArrayUtils.subarray(memory, pos, pos + 2);
        ByteBuffer wrapped = ByteBuffer.wrap(num);

        return wrapped.getShort();
    }

    public int getWordu(int pos)
    {
        byte[] byt=new byte[2];
        System.arraycopy(memory,pos,byt,0,2);
        ZNumber num=new ZNumber(byt);
        return num.toUnsignedShort();
    }

    public void setWordb(int pos, short value) {
        ByteBuffer buf = ByteBuffer.allocate(2);
        buf.putShort(value);
        byte[] num = buf.array();
        memory[pos] = num[0];
        memory[pos + 1] = num[1];
    }

    public boolean getStatusType() {
        return isBitSet(memory[1], 1);
    }


    public boolean getSplit() {
        return isBitSet(memory[1], 2);
    }

    public boolean isCensorMode() {
        return isBitSet(memory[1], 3);
    }

    public void setCensorMode(boolean censor) {
        memory[1] = setBit(memory[1], 3, censor);
    }

    public boolean isStatusLineAvailable() {
        return !isBitSet(memory[1], 4);
    }

    public void setStatusLineAvailable(boolean isAvailable) {
        memory[1] = setBit(memory[1], 4, !isAvailable);
    }

    public void setScreenSplitting(boolean isAvailable) {
        memory[1] = setBit(memory[1], 5, isAvailable);
    }

    public boolean isScreenSplittingAvailable() {
        return isBitSet(memory[1], 5);
    }

    public void setVariablePitch(boolean isDefault) {
        memory[1] = setBit(memory[1], 6, isDefault);
    }

    public boolean isVariablePitchDefault() {
        return isBitSet(memory[1], 6);
    }

    public boolean isColoursAvailable() {
        return isBitSet(memory[2], 0);
    }

    public void setColoursAvailable(boolean available) {
        memory[2] = setBit(memory[2], 0, available);
    }

    public void setPicturesAvailable(boolean available) {
        memory[2] = setBit(memory[2], 1, available);
    }

    public boolean getPictureAvailable() {
        return isBitSet(memory[2], 1);
    }

    public boolean getBoldfaceAvailable() {
        return isBitSet(memory[2], 2);
    }

    public void setBoldfaceAvailable(boolean available) {
        memory[2] = setBit(memory[2], 2, available);
    }

    public boolean getItalicAvailable() {
        return isBitSet(memory[2], 3);
    }

    public void setItalicAvailable(boolean available) {
        memory[2] = setBit(memory[2], 3, available);
    }

    public boolean getFixedSpaceAvailable() {
        return isBitSet(memory[2], 4);
    }

    public void setFixedSpaceAvailable(boolean available) {
        memory[2] = setBit(memory[2], 4, available);
    }

    public boolean getSoundEffectsAvailable() {
        return isBitSet(memory[2], 5);
    }

    public void setSoundEffectsAvailable(boolean available) {
        memory[2] = setBit(memory[2], 5, available);
    }

    public boolean getTimedKeyboardAvailable() {
        return isBitSet(memory[2], 7);
    }

    public void setTimedKeyboardAvailable(boolean available) {
        memory[2] = setBit(memory[2], 7, available);
    }

    public short getHighAddress() {
        return getWordb(4);
    }

    public short getInitialPC() {
        return (short) getWordu(6);
    }

    public int getInitialPacked() {
        return getWordu(0x6);
    }

    public short getDictionary() {
        return getWordb(8);
    }

    public short getObjects() {
        return getWordb(0xA);
    }

    public short getGlobalVariables() {
        return getWordb(0xC);
    }

    public short getStaticMemoryLoc() {
        return getWordb(0xE);
    }

    public boolean getTranscriptingOn() {
        return isBitSet(memory[0x10], 0);
    }

    public void setTranscriptingOn(boolean transcriptingOn) {
        memory[0x10] = setBit(memory[0x10], 0, transcriptingOn);
    }

    public void setForceFixedPitch(boolean forced) {
        memory[0x10] = setBit(memory[0x10], 1, forced);
    }


    public boolean getForcedFixedPitch() {
        return isBitSet(memory[0x10], 1);
    }

    public boolean getScreenRedraw() {
        return isBitSet(memory[0x10], 2);
    }

    public void setScreenRedraw(boolean redraw) {
        memory[0x10] = setBit(memory[0x10], 2, redraw);
    }

    public boolean getUsePictures() {
        return isBitSet(memory[0x10], 3);
    }

    public void setUsePictures(boolean use) {
        memory[0x10] = setBit(memory[0x10], 3, use);
    }

    public boolean getUseUndo() {
        return isBitSet(memory[0x10], 4);
    }

    public void setUseUndo(boolean use) {
        memory[0x10] = setBit(memory[0x10], 4, use);
    }

    public boolean getUseMouse() {
        return isBitSet(memory[0x10], 5);
    }

    public void setUseMouse(boolean use) {
        memory[0x10] = setBit(memory[0x10], 5, use);
    }

    public boolean getUseColours() {
        return isBitSet(memory[0x10], 6);
    }

    public void setUseColours(boolean use) {
        memory[0x10] = setBit(memory[0x10], 6, use);
    }

    public boolean getUseSound() {
        return isBitSet(memory[0x10], 7);
    }

    public void setUseSound(boolean use) {
        memory[0x10] = setBit(memory[0x10], 7, use);
    }

    public boolean getUseMenu() {
        return isBitSet(memory[0x11], 0);
    }

    public void setUseMenu(boolean use) {
        memory[0x11] = setBit(memory[0x11], 0, use);
    }

    public String getSerialCode() {
        byte[] num = ArrayUtils.subarray(memory, 0x12, 0x18);
        char[] chr = new char[num.length];
        for (int i = 0; i < num.length; i++)
            chr[i] = (char) num[i];
        return new String(chr);
    }


    public short getAbbrieviationsTable() {
        return getWordb(0x18);
    }

    public char getFileSize() {
        return (char) getWordb(0x1A);
    }

    public char getChecksum() {
        return (char) getWordb(0x1C);
    }

    public byte getInterpreterNumber() {
        return memory[0x1E];
    }

    public void setInterpreterNumber(byte version) {
        memory[0x1E] = version;
    }

    public byte getInterpreterVersion() {
        return memory[0x1F];
    }

    public void setInterpreterVersion(byte version) {
        memory[0x1F] = version;
    }

    public byte getScreenHeightLines() {
        return memory[0x20];
    }

    public void setScreenHeightLines(byte height) {
        memory[0x20] = height;
    }

    public byte getScreenWidthChars() {
        return memory[0x21];
    }

    public void setScreenWidthChars(byte width) {
        memory[0x21] = width;
    }

    public byte getScreenWidthUnits() {
        return memory[0x22];
    }

    public void setScreenWidthUnits(byte width) {
        memory[0x22] = width;
    }

    public byte getScreenHeightUnits() {
        return memory[0x24];
    }

    public void setScreenHeightUnits(byte height) {
        memory[0x24] = height;
    }

    public byte getFontHeight() {
        return memory[0x26];
    }

    public void setFontHeight(byte height) {
        memory[0x26] = height;
    }

    public byte getFontWidth() {
        return memory[0x27];
    }

    public void setFontWidth(byte width) {
        memory[0x27] = width;
    }

    public char getRoutineOffset() {
        return (char) getWordb(0x28);
    }

    public char getStringsOffset() {
        return (char) getWordb(0x2A);
    }

    public byte getDefaultBackgroundColour() {
        return memory[0x2C];
    }

    public void setDefaultBackgroundColour(byte colour) {
        memory[0x2C] = colour;
    }

    public byte getDefaultForegroundColour() {
        return memory[0x2D];
    }

    public void setDefaultForegroundColour(byte colour) {
        memory[0x2D] = colour;
    }

    public char getTerminatingCharactersTable() {
        return (char) getWordb(0x2E);
    }

    public short getTotalPixelWidth() {
        return getWordb(0x30);
    }

    public void setTotalPixelWidth(short word) {
        setWordb(0x30, word);
    }

    public short getRevisionNumber() {
        return getWordb(0x32);
    }

    public void setRevisionNumber(short word) {
        setWordb(0x32, word);
    }

    public char getAlphabetTable() {
        return (char) getWordb(0x34);
    }

    public char getHeaderExtension() {
        return (char) getWordb(0x36);
    }

    public char getHeaderExtentionLength() {
        return (char) getWordb(getHeaderExtension());
    }

    public void setMouseXCord(short x) {
        setWordb(getHeaderExtension() + 2, x);
    }

    public void setMouseYCord(short y) {
        setWordb(getHeaderExtension() + 4, y);
    }

    public char getUnicodeTranslationTable() {
        return (char) getWordb(getHeaderExtension() + 6);
    }


    public byte[] getDynamicMem()
    {
        byte[] dyn=new byte[getStaticMemoryLoc()];
        System.arraycopy(memory,0,dyn,0,getStaticMemoryLoc());;
        return dyn;
    }
    public void setDynamicMem(byte[] bytes)
    {
        System.arraycopy(bytes,0,memory,0,bytes.length);
    }
}
