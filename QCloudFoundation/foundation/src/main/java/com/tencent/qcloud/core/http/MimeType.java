/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.qcloud.core.http;

import java.util.HashMap;
import java.util.Map;

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
