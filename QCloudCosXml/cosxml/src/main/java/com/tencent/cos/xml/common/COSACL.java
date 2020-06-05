package com.tencent.cos.xml.common;

/**
 * Created by bradyxiao on 2017/11/30.
 *
 * COS 访问权限
 * <ul>
 *     <li>private : 私有读写</li>
 *     <li>public-read : 私有写公有读</li>
 *     <li>public-read-write : 共有读写</li>
 * </ul>
 *
 */

public enum  COSACL {
    PRIVATE("private"),
    PUBLIC_READ("public-read"),
    PUBLIC_READ_WRITE("public-read-write"),
    DEFAULT("default");
    private String acl;
    COSACL(String acl){
        this.acl = acl;
    }
    public String getAcl(){
        return acl;
    }
    public static COSACL fromString(String acl){
        for(COSACL cosacl : COSACL.values()){
            if(cosacl.acl.equalsIgnoreCase(acl)){
                return cosacl;
            }
        }
        return null;
    }
}
