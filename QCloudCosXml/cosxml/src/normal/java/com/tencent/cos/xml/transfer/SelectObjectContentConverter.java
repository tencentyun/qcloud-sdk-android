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

import android.text.TextUtils;

import com.tencent.cos.xml.listener.SelectObjectContentListener;
import com.tencent.cos.xml.model.object.SelectObjectContentResult;
import com.tencent.cos.xml.model.tag.eventstreaming.Message;
import com.tencent.cos.xml.model.tag.eventstreaming.MessageDecoder;
import com.tencent.cos.xml.model.tag.eventstreaming.SelectObjectContentEvent;
import com.tencent.cos.xml.model.tag.eventstreaming.SelectObjectContentEventUnmarshaller;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.core.common.QCloudServiceException;
import com.tencent.qcloud.core.http.HttpResponse;
import com.tencent.qcloud.core.http.ResponseBodyConverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 检索对象响应结果内容转换器<br>
 * 将响应结果写入指定文件
 * @param <T>
 */

public class SelectObjectContentConverter<T> extends ResponseBodyConverter<T> {

    private SelectObjectContentResult selectObjectContentResult;

    private SelectObjectContentListener contentListener;

    private MessageDecoder messageDecoder;

    private String localPath;

    /**
     * 构造检索对象响应结果内容转换器
     * @param selectObjectContentResult 检索文件响应结果
     * @param localPath 结果存储文件路径
     */
    public SelectObjectContentConverter(SelectObjectContentResult selectObjectContentResult, String localPath) {

        this.selectObjectContentResult = selectObjectContentResult;
        messageDecoder = new MessageDecoder();
        this.localPath = localPath;
    }

    @Override
    public T convert(HttpResponse<T> response) throws QCloudClientException, QCloudServiceException {

        HttpResponse.checkResponseSuccessful(response);

        InputStream inputStream = response.byteStream();

        byte[] payload = new byte[256];

        FileOutputStream fileOutputStream = newFileOutputStream(localPath);

        try {
            int read = 0;
            while ((read = inputStream.read(payload)) > 0) {

                List<Message> messages = messageDecoder.feed(payload, 0, read);

                if (fileOutputStream != null) {
                    fileOutputStream.write(payload, 0, read);
                }

                for (Message message : messages) {
                    SelectObjectContentEvent event = SelectObjectContentEventUnmarshaller.unmarshalMessage(message);
                    if (contentListener != null) {
                        contentListener.onProcess(event);
                    }

//                    if (event instanceof SelectObjectContentEvent.RecordsEvent) {
//                        if (fileOutputStream != null) {
//                            ByteBuffer byteBuffer = ((SelectObjectContentEvent.RecordsEvent) event).getPayload();
//                            fileOutputStream.write(byteBuffer.array(), 0, byteBuffer.limit());
//                        }
//                    }
                }
            }
            closeFileOutputStream(fileOutputStream);

        } catch (IOException e) {
            e.printStackTrace();
            throw new QCloudClientException(e);
        }

        return (T) selectObjectContentResult;
    }

    /**
     * 设置检索进度监听器
     * @param contentListener 检索进度监听器
     */
    public void setContentListener(SelectObjectContentListener contentListener) {
        this.contentListener = contentListener;
    }

    private FileOutputStream newFileOutputStream(String path) throws QCloudClientException {

        if (TextUtils.isEmpty(path)) {
            return null;
        }

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            if (file.createNewFile()) {
                return new FileOutputStream(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new QCloudClientException(e);
        }
        return null;
    }

    private void closeFileOutputStream(FileOutputStream fileOutputStream) throws QCloudClientException {

        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new QCloudClientException(e);
            }
        }

    }

}
