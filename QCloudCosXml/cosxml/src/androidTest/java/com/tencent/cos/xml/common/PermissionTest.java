package com.tencent.cos.xml.common;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by bradyxiao on 2018/3/14.
 */
@RunWith(AndroidJUnit4.class)
public class PermissionTest {

    @Test
    public void test() throws Exception{
        assertEquals(Permission.READ, Permission.fromValue("READ"));
        assertEquals(Permission.WRITE, Permission.fromValue("WRITE"));
        assertEquals(Permission.FULL_CONTROL, Permission.fromValue("FULL_CONTROL"));
    }
}