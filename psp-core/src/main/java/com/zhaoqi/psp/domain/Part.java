package com.zhaoqi.psp.domain;

import java.io.Serializable;

/**
 * Created by qi on 17-11-14.
 */
public class Part implements Serializable{

    private byte[] content;
    private String name;
    private static final Part NULL = new Part();

    public Part() {
        this(null, null);
    }

    public Part(String name, byte[] content) {
        this.name = name;
        this.content = content;
    }

    public byte[] getContent() {
        return this.content;
    }

    public String getName() {
        return this.name;
    }
}
