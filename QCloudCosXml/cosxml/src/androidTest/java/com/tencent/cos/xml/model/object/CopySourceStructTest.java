package com.tencent.cos.xml.model.object;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.CosXmlServiceConfig;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2018/4/4.
 */
@RunWith(AndroidJUnit4.class)
public class CopySourceStructTest {

    @Test
    public void test() throws Exception{
        CopyObjectRequest.CopySourceStruct copySourceStruct = new CopyObjectRequest.CopySourceStruct(
                "appid", "bucket", "region", "cosPath", "versionId");
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion("appid2", "region2")
                .builder();

        Assert.assertEquals("bucket-appid.cos.region.myqcloud.com/cosPath?versionId=versionId",
                copySourceStruct.getSource(cosXmlServiceConfig));
    }

}