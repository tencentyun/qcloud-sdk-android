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
