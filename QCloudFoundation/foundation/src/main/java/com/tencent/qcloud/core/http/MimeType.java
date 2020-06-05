package com.tencent.qcloud.core.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */

public final class MimeType {

    private final static Map<String, String> mimeTypes;

    static {
        mimeTypes = new HashMap<>();
        // any kind of binary data
        mimeTypes.put("bin", "application/octet-stream");

        mimeTypes.put("bmp", "image/bmp");
        mimeTypes.put("cgm", "image/cgm");
        mimeTypes.put("djv", "image/vnd.djvu");
        mimeTypes.put("djvu", "image/vnd.djvu");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("ico", "image/x-icon");
        mimeTypes.put("ief", "image/ief");
        mimeTypes.put("jp2", "image/jp2");
        mimeTypes.put("jpe", "image/jpeg");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("mac", "image/x-macpaint");
        mimeTypes.put("pbm", "image/x-portable-bitmap");
        mimeTypes.put("pct", "image/pict");
        mimeTypes.put("pgm", "image/x-portable-graymap");
        mimeTypes.put("pic", "image/pict");
        mimeTypes.put("pict", "image/pict");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("pnm", "image/x-portable-anymap");
        mimeTypes.put("pnt", "image/x-macpaint");
        mimeTypes.put("pntg", "image/x-macpaint");
        mimeTypes.put("ppm", "image/x-portable-pixmap");
        mimeTypes.put("qti", "image/x-quicktime");
        mimeTypes.put("qtif", "image/x-quicktime");
        mimeTypes.put("ras", "image/x-cmu-raster");
        mimeTypes.put("rgb", "image/x-rgb");
        mimeTypes.put("svg", "image/svg+xml");
        mimeTypes.put("tif", "image/tiff");
        mimeTypes.put("tiff", "image/tiff");
        mimeTypes.put("wbmp", "image/vnd.wap.wbmp");
        mimeTypes.put("xbm", "image/x-xbitmap");
        mimeTypes.put("xpm", "image/x-xpixmap");
        mimeTypes.put("xwd", "image/x-xwindowdump");


    }

    public static String getTypeByFileName(String fileName) {

        if (fileName==null) {
            return null;
        }
        String extension = "";

        int startIndex = fileName.lastIndexOf(".");
        if (startIndex!=-1) {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        }
        String type = mimeTypes.get(extension.toLowerCase());
        if (type == null) {
            type = mimeTypes.get("bin");
        }
        return type;
    }

}
