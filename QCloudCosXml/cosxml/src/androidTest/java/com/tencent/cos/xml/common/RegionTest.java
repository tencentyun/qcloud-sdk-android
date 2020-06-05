package com.tencent.cos.xml.common;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2018/3/14.
 */
@RunWith(AndroidJUnit4.class)
public class RegionTest {

    @Test
    public void test() throws Exception{
        assertEquals(Region.AP_Beijing_1, Region.fromValue("ap-beijing-1"));
        assertEquals(Region.AP_Beijing, Region.fromValue("ap-beijing"));
        assertEquals(Region.AP_Shanghai, Region.fromValue("ap-shanghai"));
        assertEquals(Region.AP_Guangzhou, Region.fromValue("ap-guangzhou"));
        assertEquals(Region.AP_Guangzhou_2, Region.fromValue("ap-guangzhou-2"));
        assertEquals(Region.AP_Chengdu, Region.fromValue("ap-chengdu"));
        assertEquals(Region.AP_Singapore, Region.fromValue("ap-singapore"));
        assertEquals(Region.AP_Hongkong, Region.fromValue("ap-hongkong"));
        assertEquals(Region.NA_Toronto, Region.fromValue("na-toronto"));
        assertEquals(Region.EU_Frankfurt, Region.fromValue("eu-frankfurt"));
        assertEquals(Region.CN_NORTH, Region.fromValue("cn-north"));
        assertEquals(Region.CN_SOUTH, Region.fromValue("cn-south"));
        assertEquals(Region.CN_EAST, Region.fromValue("cn-east"));
        assertEquals(Region.CN_SOUTHWEST, Region.fromValue("cn-southwest"));
    }
}