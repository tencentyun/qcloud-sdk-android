package com.tencent.cos.xml.model.tag;

import java.util.List;

/**
 * Created by bradyxiao on 2017/11/24.
 */

public class Delete {
    public boolean quiet;
    public List<DeleteObject> deleteObjects;

    public static class DeleteObject{
        public String key;
        public String versionId;
    }
}
