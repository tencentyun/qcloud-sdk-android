package com.tencent.cos.xml.common;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2018/3/14.
 */
@RunWith(AndroidJUnit4.class)
public class COSACLTest {

    @Test
    public void test() throws Exception{
        assertEquals(COSACL.PRIVATE, COSACL.fromString("private"));
        assertEquals(COSACL.PUBLIC_READ, COSACL.fromString("public-read"));
        assertEquals(COSACL.PUBLIC_READ_WRITE, COSACL.fromString("public-read-write"));
    }

}