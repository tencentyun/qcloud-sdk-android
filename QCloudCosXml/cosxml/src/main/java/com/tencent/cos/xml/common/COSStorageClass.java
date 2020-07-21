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

package com.tencent.cos.xml.common;

/**
 * COS存储类型
 * <ul>
 *     <li>Standard : 标准存储</li>
 *     <li>Standard_IA : 冷存储</li>
 *     <li>Nearline : 近线存储</li>
 * 详细描述，请参考：<a href="https://cloud.tencent.com/document/product/436/33417">存储类型</a>
 * </ul>
 */
public enum COSStorageClass {

    /** 标准存储 */
    STANDARD("Standard"),

    /** 冷存储 */
    STANDARD_IA("Standard_IA"),

    /** 归档直传 */
    ARCHIVE("ARCHIVE ");

//    /** 近线存储 */
//    NEARLINE("Nearline");


    private String cosStorageClass;

    COSStorageClass(String cosStorageClass) {
        this.cosStorageClass = cosStorageClass;
    }

    public String getStorageClass(){
        return cosStorageClass;
    }

    public static COSStorageClass fromString(String cosStorageClass){
        for(COSStorageClass storageClass : COSStorageClass.values()){
            if(storageClass.cosStorageClass.equalsIgnoreCase(cosStorageClass)){
                return storageClass;
            }
        }
        return null;
    }
}
