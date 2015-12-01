package com.nekokittygames.zmachine.memory;

import java.util.Map;

/**
 * Created by katsw on 01/12/2015.
 */
public class ZObject {

    private int parent;
    private int sibling;
    private int child;
    private byte[] attributes;
    private String name;
    private byte[][] properties;

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getSibling() {
        return sibling;
    }

    public void setSibling(int sibling) {
        this.sibling = sibling;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    public byte[] getAttributes() {
        return attributes;
    }

    public void setAttributes(byte[] attributes) {
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[][] getProperties() {
        return properties;
    }

    public void setProperties(byte[][] properties) {
        this.properties = properties;
    }
    public void setProperty(int i,byte[] property)
    {
        this.properties[i]=property;
    }

    public byte[] getProperty(int i)
    {
        return this.properties[i];
    }
}
