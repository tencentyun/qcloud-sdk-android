package com.tencent.cos.xml.common;

/**
 * Created by bradyxiao on 2017/5/24.
 * 权限信息
 * <ul>
 *     <li>READ : 读权限</li>
 *     <li>WRITE : 写权限</li>
 *     <li>FULL_CONTROL : 读写权限</li>
 * </ul>
 */
public enum Permission {
    /** 读权限 */
    READ("READ"),
    /** 写权限 */
    WRITE("WRITE"),
    /** 读写权限 */
    FULL_CONTROL("FULL_CONTROL");
    private String permission;
    Permission(String permission){
        this.permission = permission;
    }
    public String getPermission(){
        return permission;
    }
    public static Permission fromValue(String permission){
        for(Permission permission1 : Permission.values()){
            if(permission1.permission.equalsIgnoreCase(permission)){
                return permission1;
            }
        }
        return null;
    }
}
