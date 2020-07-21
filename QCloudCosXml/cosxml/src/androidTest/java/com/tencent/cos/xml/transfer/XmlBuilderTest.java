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

package com.tencent.cos.xml.transfer;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.model.tag.RestoreConfigure;

import org.junit.Test;
import org.junit.runner.RunWith;

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