package com.tencent.cos.xml.model.object;

import android.text.TextUtils;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.core.TestUtils;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.listener.SelectObjectContentListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.tag.eventstreaming.CSVInput;
import com.tencent.cos.xml.model.tag.eventstreaming.CSVOutput;
import com.tencent.cos.xml.model.tag.eventstreaming.CompressionType;
import com.tencent.cos.xml.model.tag.eventstreaming.FileHeaderInfo;
import com.tencent.cos.xml.model.tag.eventstreaming.InputSerialization;
import com.tencent.cos.xml.model.tag.eventstreaming.OutputSerialization;
import com.tencent.cos.xml.model.tag.eventstreaming.SelectObjectContentEvent;

import org.junit.Assert;

import java.io.File;
import java.io.IOException;

public class SelectObjectContentCsvTestAdapter extends NormalRequestTestAdapter<SelectObjectContentRequest, SelectObjectContentResult> {
    @Override
    protected SelectObjectContentRequest newRequestInstance() {
        CSVInput csvInput = new CSVInput("\n",
                ",", null, null, false, null, null);
        CSVOutput csvOutput = new CSVOutput(null,"\n",",", null, null);

        runCsvInput(csvInput);
        runCsvOutput(csvOutput);

        InputSerialization inputSerialization = new InputSerialization(CompressionType.NONE, csvInput);
        inputSerialization.withCompressionType(CompressionType.NONE.toString()).withCompressionType(CompressionType.NONE)
                .withCsv(csvInput).setCsv(csvInput);
        inputSerialization = new InputSerialization(CompressionType.NONE.toString(), csvInput);
        Assert.assertEquals(inputSerialization.getCsv(), csvInput.clone());

        OutputSerialization outputSerialization = new OutputSerialization(csvOutput);
        outputSerialization.withCsv(csvOutput).setCsv(csvOutput);
        Assert.assertEquals(outputSerialization.getCsv(), csvOutput.clone());

        Assert.assertFalse(TextUtils.isEmpty(csvInput.toString()));
        Assert.assertFalse(TextUtils.isEmpty(csvOutput.toString()));
        Assert.assertFalse(TextUtils.isEmpty(inputSerialization.toString()));
        Assert.assertFalse(TextUtils.isEmpty(outputSerialization.toString()));

        SelectObjectContentRequest selectObjectContentRequest = new SelectObjectContentRequest(
                TestConst.PERSIST_BUCKET, TestConst.PERSIST_BUCKET_SELECT_CSV_PATH,
                "select * from cosobject s limit 10", true,
                inputSerialization, outputSerialization
        );

        String localPath = new File(TestUtils.localParentPath(), "select.csv").getAbsolutePath();
        try {
            TestUtils.createFile(localPath, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void runCsvInput(CSVInput csvInput){
        csvInput.setAllowQuotedRecordDelimiter(false);
        csvInput.setComments((Character) null);
        csvInput.setComments((String) null);
        csvInput.setFieldDelimiter(",");
        csvInput.setFieldDelimiter(',');
        csvInput.setFileHeaderInfo(FileHeaderInfo.NONE);
        csvInput.setFileHeaderInfo(FileHeaderInfo.NONE.toString());
        csvInput.setQuoteCharacter((Character) null);
        csvInput.setQuoteCharacter((String) null);
        csvInput.setQuoteEscapeCharacter((Character) null);
        csvInput.setQuoteEscapeCharacter((String) null);
        csvInput.setRecordDelimiter("\n");
        csvInput.setRecordDelimiter('\n');

        csvInput.withAllowQuotedRecordDelimiter(false)
                .withComments((Character) null)
                .withComments((String) null)
                .withFieldDelimiter(",")
                .withFieldDelimiter(',')
                .withFileHeaderInfo(FileHeaderInfo.NONE)
                .withFileHeaderInfo(FileHeaderInfo.NONE.toString())
                .withQuoteCharacter((Character) null)
                .withQuoteCharacter((String) null)
                .withQuoteEscapeCharacter((Character) null)
                .withQuoteEscapeCharacter((String) null)
                .withRecordDelimiter("\n")
                .withRecordDelimiter('\n');

        Assert.assertFalse((boolean) csvInput.getAllowQuotedRecordDelimiter());
        Assert.assertNull(csvInput.getCommentsAsString());
        Assert.assertNull(csvInput.getComments());
        Assert.assertEquals(",", csvInput.getFieldDelimiterAsString());
        Assert.assertEquals(',', (char) csvInput.getFieldDelimiter());
        Assert.assertSame(FileHeaderInfo.fromValue(csvInput.getFileHeaderInfo()), FileHeaderInfo.NONE);
        Assert.assertNull(csvInput.getQuoteCharacterAsString());
        Assert.assertNull(csvInput.getQuoteCharacter());
        Assert.assertNull(csvInput.getQuoteEscapeCharacterAsString());
        Assert.assertNull(csvInput.getQuoteEscapeCharacter());
        Assert.assertEquals("\n", csvInput.getRecordDelimiterAsString());
        Assert.assertEquals('\n', (char) csvInput.getRecordDelimiter());
    }

    private void runCsvOutput(CSVOutput csvOutput){
        csvOutput.setFieldDelimiter(",");
        csvOutput.setFieldDelimiter(',');
        csvOutput.setQuoteCharacter((Character) null);
        csvOutput.setQuoteCharacter((String) null);
        csvOutput.setQuoteEscapeCharacter((Character) null);
        csvOutput.setQuoteEscapeCharacter((String) null);
        csvOutput.setRecordDelimiter("\n");
        csvOutput.setRecordDelimiter('\n');

        csvOutput.withFieldDelimiter(",")
                .withFieldDelimiter(',')
                .withQuoteCharacter((Character) null)
                .withQuoteCharacter((String) null)
                .withQuoteEscapeCharacter((Character) null)
                .withQuoteEscapeCharacter((String) null)
                .withRecordDelimiter("\n")
                .withRecordDelimiter('\n');

        Assert.assertEquals(",", csvOutput.getFieldDelimiterAsString());
        Assert.assertEquals(',', (char) csvOutput.getFieldDelimiter());
        Assert.assertNull(csvOutput.getQuoteCharacterAsString());
        Assert.assertNull(csvOutput.getQuoteCharacter());
        Assert.assertNull(csvOutput.getQuoteEscapeCharacterAsString());
        Assert.assertNull(csvOutput.getQuoteEscapeCharacter());
        Assert.assertEquals("\n", csvOutput.getRecordDelimiterAsString());
        Assert.assertEquals('\n', (char) csvOutput.getRecordDelimiter());
    }
}