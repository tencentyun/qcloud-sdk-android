package com.tencent.cos.xml.common;

/**
 * COS存储类型
 * <ul>
 *     <li>Standard : 标准存储</li>
 *     <li>Standard_IA : 冷存储</li>
 *     <li>Nearline : 近线存储</li>
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
