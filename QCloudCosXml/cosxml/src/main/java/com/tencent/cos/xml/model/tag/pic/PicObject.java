package com.tencent.cos.xml.model.tag.pic;

public class PicObject {

    public String key;

    public String location;

    public String format;

    public int width;

    public int height;

    public int size;

    public int quality;

    public PicObject(String key, String location, String format, int width, int height, int size, int quality) {
        this.key = key;
        this.location = location;
        this.format = format;
        this.width = width;
        this.height = height;
        this.size = size;
        this.quality = quality;
    }

    public PicObject() {}
}
