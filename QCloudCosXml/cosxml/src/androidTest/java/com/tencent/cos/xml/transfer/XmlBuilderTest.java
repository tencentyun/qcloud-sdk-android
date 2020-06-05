package com.tencent.cos.xml.transfer;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.model.tag.RestoreConfigure;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2018/4/4.
 */
@RunWith(AndroidJUnit4.class)
public class XmlBuilderTest {

    @Test
    public void test() throws Exception{
        RestoreConfigure restoreConfigure = new RestoreConfigure();
        restoreConfigure.days = 1;
        restoreConfigure.casJobParameters = new RestoreConfigure.CASJobParameters();
        restoreConfigure.casJobParameters.tier = RestoreConfigure.Tier.Standard.getTier();
        String restore = XmlBuilder.buildRestore(restoreConfigure);
        Log.d("XIAO", restore);
    }
}