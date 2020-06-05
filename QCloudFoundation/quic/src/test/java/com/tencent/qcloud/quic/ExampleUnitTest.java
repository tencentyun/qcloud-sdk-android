package com.tencent.qcloud.quic;

import junit.framework.Assert;
import org.junit.Test;

import java.net.URI;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will testExecute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSigneddAndUnsigned(){
        //byte -128 ~ 127
        int a = 127;
        Assert.assertEquals(a, (byte)(a & 0xFF));
        Assert.assertEquals(a, (byte)a);
        a = -128;
        Assert.assertEquals(a, (byte)(a & 0xFF));
        Assert.assertEquals(a, (byte)a);

        a = -100;
        Assert.assertEquals(a, (byte)(a & 0xFF));
        Assert.assertEquals(a, (byte)a);
        a = 100;
        Assert.assertEquals(a, (byte)(a & 0xFF));
        Assert.assertEquals(a, (byte)a);

        byte b = -56;
        Assert.assertEquals(200, (b & 0xFF));
    }
}