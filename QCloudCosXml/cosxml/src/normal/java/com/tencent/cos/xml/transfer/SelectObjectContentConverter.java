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
import java.nio.ByteBuffer;
import java.util.List;

/**
 *
 * Copyright 2010-2017 Tencent Cloud. All Rights Reserved.
 */
public class SelectObjectContentConverter<T> extends ResponseBodyConverter<T> {

    private SelectObjectContentResult selectObjectContentResult;

    private SelectObjectContentListener contentListener;

    private MessageDecoder messageDecoder;

    private String localPath;

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
