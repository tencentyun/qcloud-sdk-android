package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * Created by bradyxiao on 2017/11/24.
 */

public class AccessControlPolicy {
    public Owner owner;
    public AccessControlList accessControlList;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder("{AccessControlPolicy:\n");
        if(owner != null)stringBuilder.append(owner.toString()).append("\n");
        if(accessControlList != null)stringBuilder.append(accessControlList.toString()).append("\n");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static class Owner{
        public String id;
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

    public static class AccessControlList{
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

    public static class Grant{
        public Grantee grantee;
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

    public static class Grantee{
        public String id;
        public String displayName;
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
