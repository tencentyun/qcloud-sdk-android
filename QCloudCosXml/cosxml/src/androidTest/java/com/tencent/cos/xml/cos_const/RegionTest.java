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

package com.tencent.cos.xml.cos_const;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.common.Region;

import org.junit.Assert;
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
        Assert.assertEquals(Region.AP_Beijing_1, Region.fromValue("ap-beijing-1"));
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