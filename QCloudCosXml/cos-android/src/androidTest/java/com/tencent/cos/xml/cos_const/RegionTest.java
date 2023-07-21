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

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.common.Region;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Assert.assertNull(Region.fromValue("test"));
    }

    @Test public void test2() {

        // 按指定模式在字符串查找
        String line = "android-ut-persist-bucket-1253653367.cos.ap-guangzhou.myqcloud.com";
        String pattern = ".*\\.cos\\.(.*)\\.myqcloud.com";

        // 创建 Pattern 对象
        Pattern params = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher matcher = params.matcher(line);


        try {
            if (matcher.find() ) {
                // params.put("region", matcher.group(1));
                System.out.println("Found value: " + matcher.group(0));
                System.out.println("Found value: " + matcher.group(1));
                System.out.println("Found value: " + matcher.group(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        if (m.find( )) {
//            System.out.println("Found value: " + m.group(1) );
//        } else {
//            System.out.println("NO MATCH");
//        }
    }
}