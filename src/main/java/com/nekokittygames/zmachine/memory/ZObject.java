package com.nekokittygames.zmachine.memory;

import java.util.BitSet;
import java.util.Map;

/**
 * Created by katsw on 01/12/2015.
 */
public class ZObject {

    private int parent;
    private int sibling;
    private int child;
    private BitSet attributes;
    private String name;
    private ZProperty[] properties;

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

    public BitSet getAttributes() {
        return attributes;
    }

    public void setAttributes(BitSet attributes) {
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZProperty[] getProperties() {
        return properties;
    }

    public void setProperties(ZProperty[] properties) {
        this.properties = properties;
    }
    public void setProperty(int i,ZProperty property)
    {
        this.properties[i]=property;
    }

    public ZProperty getProperty(int i)
    {
        return this.properties[i];
    }
}
