package com.sunxu;

/**
 * @author 孙许
 * @date 2018/01/14
 * @description
 */
public class Attachment {
    private String name;
    private byte[] contents;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }
}
