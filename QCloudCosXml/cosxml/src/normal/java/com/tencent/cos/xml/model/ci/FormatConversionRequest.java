package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.object.ObjectRequest;
import com.tencent.cos.xml.model.tag.pic.PicOperationRule;
import com.tencent.cos.xml.model.tag.pic.PicOperations;
import com.tencent.qcloud.core.http.HttpConstants;
import com.tencent.qcloud.core.http.RequestBodySerializer;

import java.util.Collections;

/**
 * 格式转换请求
 */
public class FormatConversionRequest extends ObjectRequest {
    /**
     * 存储结果的目标存储桶，格式为 BucketName-APPID，如果不指定的话默认保存到当前存储桶
     */
    public String saveBucket;

    /**
     * 处理结果的文件路径名称，例如以/开头，则存入指定文件夹中，否则存入与原图文件相同的目录位置
     */
    public String fileId;

    /**
     * 转换格式
     */
    private final String format;

    public FormatConversionRequest(String bucket, String cosPath, String format) {
        super(bucket, cosPath);
        this.format = format;
        setQueryEncodedString("image_process");
    }

    public PicOperations getPicOperations() {
        PicOperationRule rule = new PicOperationRule("imageView2/format/"+format);
        rule.setBucket(saveBucket);
        rule.setFileId(fileId);
        return new PicOperations(false, Collections.singletonList(rule));
    }

    @Override
    public void checkParameters() throws CosXmlClientException {
        super.checkParameters();
        // 在检查参数时添加Pic-Operations header
        PicOperations picOperations = getPicOperations();
        if (picOperations != null) {
            addHeader("Pic-Operations", picOperations.toJsonStr());
        }
    }

    @Override
    public String getMethod() {
        return HttpConstants.RequestMethod.POST;
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return RequestBodySerializer.string(HttpConstants.ContentType.TEXT_PLAIN, "");
    }

    @Override
    public boolean headersHasUnsafeNonAscii(){
        return true;
    }
}
