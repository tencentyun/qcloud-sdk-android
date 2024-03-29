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
 *     <li>Standard_IA : 低频存储</li>
 *     <li>COLD : 冷存储</li>
 *     <li>ARCHIVE : 归档存储</li>
 *     <li>DEEP_ARCHIVE : 深度归档存储</li>
 *     <li>INTELLIGENT_TIERING : 智能分层存储</li>
 *     <li>MAZ_STANDARD : 标准存储（多 AZ）</li>
 *     <li>MAZ_STANDARD_IA : 低频存储（多 AZ）</li>
 *     <li>MAZ_COLD : 冷存储（多 AZ）</li>
 *     <li>MAZ_INTELLIGENT_TIERING : 智能分层存储（多 AZ）</li>
 * </ul>
 * 详细描述，请参考：<a href="https://cloud.tencent.com/document/product/436/33417">存储类型</a>
 */
public enum COSStorageClass {

    /**
     * 标准存储
     */
    STANDARD("STANDARD"),

    /**
     * 低频存储
     */
    STANDARD_IA("STANDARD_IA"),

    /**
     * 冷存储
     */
    COLD("COLD"),

    /**
     * 归档存储
     */
    ARCHIVE("ARCHIVE"),

    /**
     * 深度归档存储
     */
    DEEP_ARCHIVE("DEEP_ARCHIVE"),

    /**
     * 智能分层存储
     */
    INTELLIGENT_TIERING("INTELLIGENT_TIERING"),

    /**
     * 标准存储（多 AZ）
     */
    MAZ_STANDARD("MAZ_STANDARD"),

    /**
     * 低频存储（多 AZ）
     */
    MAZ_STANDARD_IA("MAZ_STANDARD_IA"),

    /**
     * 冷存储（多 AZ）
     */
    MAZ_COLD("MAZ_COLD"),

    /**
     * 智能分层存储（多 AZ）
     */
    MAZ_INTELLIGENT_TIERING("MAZ_INTELLIGENT_TIERING");

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
