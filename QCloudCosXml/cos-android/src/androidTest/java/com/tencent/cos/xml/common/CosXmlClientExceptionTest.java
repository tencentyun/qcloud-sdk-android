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

package com.tencent.cos.xml.common;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.tencent.cos.xml.common.ClientErrorCode;
import com.tencent.cos.xml.exception.CosXmlClientException;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2018/3/14.
 */
@RunWith(AndroidJUnit4.class)
public class CosXmlClientExceptionTest {

    @Test
    public void test() throws Exception{
        CosXmlClientException cosXmlClientException = new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "exception cause by client");
        assertEquals("exception cause by client", cosXmlClientException.getMessage());
        assertEquals(cosXmlClientException.errorCode, ClientErrorCode.INVALID_ARGUMENT.getCode());

        CosXmlClientException cosXmlClientException2 = new CosXmlClientException(ClientErrorCode.INVALID_ARGUMENT.getCode(), "exception cause by client", new NullPointerException());
        assertEquals("exception cause by client", cosXmlClientException2.getMessage());
        assertEquals(true, cosXmlClientException2.getCause() instanceof NullPointerException);
        assertEquals(cosXmlClientException.errorCode, ClientErrorCode.INVALID_ARGUMENT.getCode());
    }
}