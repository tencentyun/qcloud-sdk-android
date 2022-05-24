package com.tencent.cos.xml.model.object;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.listener.SelectObjectContentListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.tag.eventstreaming.CompressionType;
import com.tencent.cos.xml.model.tag.eventstreaming.InputSerialization;
import com.tencent.cos.xml.model.tag.eventstreaming.JSONInput;
import com.tencent.cos.xml.model.tag.eventstreaming.JSONOutput;
import com.tencent.cos.xml.model.tag.eventstreaming.JSONType;
import com.tencent.cos.xml.model.tag.eventstreaming.OutputSerialization;
import com.tencent.cos.xml.model.tag.eventstreaming.SelectObjectContentEvent;

import org.junit.Assert;

import java.io.File;

// Generate by auto
public class SelectObjectContentJsonTestAdapter extends NormalRequestTestAdapter<SelectObjectContentRequest, SelectObjectContentResult> {
    @Override
    protected SelectObjectContentRequest newRequestInstance() {
        JSONInput jsonInput = new JSONInput(JSONType.DOCUMENT);
        jsonInput.withType(JSONType.DOCUMENT).withType(JSONType.DOCUMENT.toString()).setType(JSONType.DOCUMENT);
        jsonInput = new JSONInput(JSONType.DOCUMENT.toString());
        Assert.assertEquals(jsonInput.getType(), JSONType.DOCUMENT.toString());

        JSONOutput jsonOutput = new JSONOutput();
        jsonOutput.withRecordDelimiter("\n").withRecordDelimiter('\n').setRecordDelimiter("\n");
        jsonOutput.setRecordDelimiter('\n');
        jsonOutput = new JSONOutput("\n");
        Assert.assertEquals(jsonOutput.getRecordDelimiter().toString(), "\n");

        InputSerialization inputSerialization = new InputSerialization(CompressionType.NONE, jsonInput);
        inputSerialization.withJson(jsonInput).setJson(jsonInput);
        inputSerialization = new InputSerialization(CompressionType.NONE.toString(), jsonInput);

        OutputSerialization outputSerialization = new OutputSerialization(jsonOutput);
        outputSerialization.withJson(jsonOutput).setJson(jsonOutput);

        SelectObjectContentRequest selectObjectContentRequest = new SelectObjectContentRequest(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SELECT_JSON_PATH,
                "select * from cosobject s limit 100", true,
                inputSerialization, outputSerialization
        );

        String localPath = new File(TestUtils.localParentPath(), "select.json").getAbsolutePath();
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