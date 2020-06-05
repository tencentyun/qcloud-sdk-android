package com.tencent.cos.xml.model.tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bradyxiao on 2017/11/30.
 */

public class ACLAccount {
    List<String> idList;
    public ACLAccount(){
        idList = new ArrayList<>();
    }

    public void addAccount(String ownerUin, String subUin){
        if(ownerUin != null && subUin != null){
            idList.add(String.format("id=\"qcs::cam::uin/%s:uin/%s\"",ownerUin, subUin));
        }
    }

    public void addAccount(String owner){
        addAccount(owner, owner);
    }

    public String getAccount(){
        StringBuilder stringBuilder = new StringBuilder();
        for(String account : idList){
            stringBuilder.append(account).append(",");
        }
        String result = stringBuilder.toString();
        int last = result.lastIndexOf(",");
        if(last > 0) {
            return result.substring(0, last);
        }
        return null;
    }
}
