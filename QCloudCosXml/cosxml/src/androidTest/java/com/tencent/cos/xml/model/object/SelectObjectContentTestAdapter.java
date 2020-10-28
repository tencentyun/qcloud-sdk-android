package com.tencent.cos.xml.model.object;

import android.os.Environment;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.listener.SelectObjectContentListener;
import com.tencent.cos.xml.model.RequestTestAdapter;
import com.tencent.cos.xml.model.tag.eventstreaming.CompressionType;
import com.tencent.cos.xml.model.tag.eventstreaming.InputSerialization;
import com.tencent.cos.xml.model.tag.eventstreaming.JSONInput;
import com.tencent.cos.xml.model.tag.eventstreaming.JSONOutput;
import com.tencent.cos.xml.model.tag.eventstreaming.JSONType;
import com.tencent.cos.xml.model.tag.eventstreaming.OutputSerialization;
import com.tencent.cos.xml.model.tag.eventstreaming.SelectObjectContentEvent;

import java.io.File;

// Generate by auto
public class SelectObjectContentTestAdapter extends RequestTestAdapter<SelectObjectContentRequest, SelectObjectContentResult> {
    @Override
    protected SelectObjectContentRequest newRequestInstance() {
        SelectObjectContentRequest selectObjectContentRequest = new SelectObjectContentRequest(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SELECT_PATH,
                "Select * from COSObject", true,
                new InputSerialization(CompressionType.NONE,
                new JSONInput(JSONType.DOCUMENT)),
                new OutputSerialization(new JSONOutput())
        );

        String localPath = new File(Environment.getExternalStorageDirectory(), "select.json").getAbsolutePath();
        selectObjectContentRequest.setSelectResponseFilePath(localPath);
        selectObjectContentRequest.setSelectObjectContentProgressListener(new SelectObjectContentListener() {
            @Override
            public void onProcess(SelectObjectContentEvent event) {
                System.out.println("SelectObjectContentEvent type " + event.getClass().getSimpleName());
            }
        });

        return selectObjectContentRequest;
    }

    @Override
    protected SelectObjectContentResult exeSync(SelectObjectContentRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.selectObjectContent(request);
    }

    @Override
    protected void exeAsync(SelectObjectContentRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.selectObjectContentAsync(request, resultListener);
    }
}