package com.tencent.cos.xml.qqlive;

import com.tencent.cos.xml.core.TestUtils;
import com.tencent.qcloud.core.common.QCloudProgressListener;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * <p>
 * Created by rickenwang on 2022/1/20.
 * Copyright 2010-2021 Tencent Cloud. All Rights Reserved.
 */
public class FileVerifyTest {


    @Test
    public void copyFile() {

        String sourcePath = TestUtils.localPath("1642166999131.m4a");
        String dstPath = sourcePath + ".bak";

        try {
            InputStream inputStream = new FileInputStream(sourcePath);
            BufferedSource source = Okio.buffer(Okio.source(inputStream));
            BufferedSink bufferedSink = Okio.buffer(Okio.sink(new File(dstPath)));
            bufferedSink.writeAll(source);
            bufferedSink.flush();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

}
