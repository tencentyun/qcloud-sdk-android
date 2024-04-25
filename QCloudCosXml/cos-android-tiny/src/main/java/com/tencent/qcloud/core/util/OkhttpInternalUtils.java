package com.tencent.qcloud.core.util;

import java.io.Closeable;

/**
 * 来自okhttp3.internal的方法
 * 因为okhttp3.internal不保证兼容性，因此从okhttp3:okhttp:3.12.6复制至此
 * <p>
 * Created by jordanqin on 2023/8/24 11:46.
 * Copyright 2010-2023 Tencent Cloud. All Rights Reserved.
 */
public class OkhttpInternalUtils {
    /**
     * Closes {@code closeable}, ignoring any checked exceptions. Does nothing if {@code closeable} is
     * null.
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }
}
