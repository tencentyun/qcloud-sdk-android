package com.tencent.cos.xml.common;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2018/3/14.
 */
@RunWith(AndroidJUnit4.class)
public class RangeTest {

    @Test
    public void test() throws Exception{
        Range range = new Range(1);
        assertEquals("bytes=1-", range.getRange());
        assertEquals(1, range.getStart());
        assertEquals(-1, range.getEnd());
        Range range1 = new Range(1,100);
        assertEquals("bytes=1-100", range1.getRange());
    }
}