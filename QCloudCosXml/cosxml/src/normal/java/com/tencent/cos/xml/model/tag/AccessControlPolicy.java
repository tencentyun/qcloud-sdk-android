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

package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * acl 信息
 */
public class AccessControlPolicy {
    /**
     * 存储桶持有者信息
     */
    public Owner owner;

    /**
     * 被授权者信息与权限信息
     */
    public AccessControlList accessControlList;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{AccessControlPolicy:\n");
        if(owner != null)stringBuilder.append(owner.toString()).append("\n");
        if(accessControlList != null)stringBuilder.append(accessControlList.toString()).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /**
     * 存储桶持有者信息
     */
    public static class Owner{
        /**
         * 存储桶持有者的完整 ID，格式为 qcs::cam::uin/[OwnerUin]:uin/[OwnerUin]，如 qcs::cam::uin/100000000001:uin/100000000001
         */
        public String id;
        /**
         * 存储桶持有者的名字
         */
        public String displayName;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Owner:\n");
            stringBuilder.append("Id:").append(id).append("\n");
            stringBuilder.append("DisplayName:").append(displayName).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    /**
     * 被授权者信息与权限信息
     */
    public static class AccessControlList{
        /**
         * 授权信息
         */
        public List<Grant> grants;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{AccessControlList:\n");
            if(grants != null){
                for(Grant grant : grants){
                    if(grant != null)stringBuilder.append(grant.toString()).append("\n");
                }
            }
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    /**
     * 授权信息
     */
    public static class Grant{
        /**
         * 被授权者信息，xsi:type 为 Group 或 CanonicalUser，当为 Group 时子节点包括且仅包括 URI，当指定为 CanonicalUser 时子节点包括且仅包括 ID 和 DisplayName
         */
        public Grantee grantee;
        /**
         * 授予的权限信息，枚举值请参见 <a href="https://cloud.tencent.com/document/product/436/30752#.E6.93.8D.E4.BD.9C-permission">ACL 概述</a> 文档中存储桶的操作部分，如 WRITE，FULL_CONTROL 等
         */
        public String permission;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Grant:\n");
            if(grantee != null)stringBuilder.append(grantee.toString()).append("\n");
            stringBuilder.append("Permission:").append(permission).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    /**
     * 被授权者信息，xsi:type 为 Group 或 CanonicalUser，当为 Group 时子节点包括且仅包括 URI，当指定为 CanonicalUser 时子节点包括且仅包括 ID 和 DisplayName
     */
    public static class Grantee{
        /**
         * 被授权者的完整 ID，格式为 qcs::cam::uin/[OwnerUin]:uin/[OwnerUin]，如 qcs::cam::uin/100000000001:uin/100000000001
         */
        public String id;
        /**
         * 被授权者的名字
         */
        public String displayName;
        /**
         * 预设用户组，请参见 <a href="https://cloud.tencent.com/document/product/436/30752#.E8.BA.AB.E4.BB.BD-grantee">ACL 概述</a> 文档中预设用户组部分，
         * 如 http://cam.qcloud.com/groups/global/AllUsers 或 http://cam.qcloud.com/groups/global/AuthenticatedUsers
         */
        public String uri;

        @Override
        public String toString(){
            StringBuilder stringBuilder = new StringBuilder("{Grantee:\n");
            stringBuilder.append("URI:").append(uri).append("\n");
            stringBuilder.append("Id:").append(id).append("\n");
            stringBuilder.append("DisplayName:").append(displayName).append("\n");
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }
}
