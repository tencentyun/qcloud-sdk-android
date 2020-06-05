package com.tencent.cos.xml.model.object;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.cos.xml.QServer;
import com.tencent.cos.xml.common.COSACL;
import com.tencent.cos.xml.common.COSStorageClass;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.utils.DateUtils;
import com.tencent.cos.xml.utils.DigestUtils;
import com.tencent.qcloud.core.http.HttpConstants;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by bradyxiao on 2018/6/11.
 */

@RunWith(AndroidJUnit4.class)
public class PostObjectRequestTest {


    @Test
    public void testPolicy() throws Exception{
        PostObjectRequest.Policy policy = new PostObjectRequest.Policy();

        String regex = "[0-9]{4}-[0-1][0-9]-[0-3][0-9]T[0-2][0-9]:[0-6][0-9]:[0-6][0-9].[0-9]{3}Z";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(DateUtils.getFormatTime("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", System.currentTimeMillis()));
        Log.d("XIAO", String.valueOf(matcher.find()));
        Log.d("XIAO", matcher.group(0));

        policy.setExpiration(System.currentTimeMillis());
        Log.d("XIAO", policy.content());

        policy.addContentConditions(0, 100);
        Log.d("XIAO", policy.content());

        policy.addConditions("acl","public-read", false);
        policy.addConditions("acl","public-read", true);
        Log.d("XIAO", policy.content());
    }

    @Test
    public void testFormParameters() throws CosXmlClientException {
        PostObjectRequest postObjectRequest = new PostObjectRequest("bucket", "1.txt", "/e/1.txt");
        postObjectRequest.setAcl(COSACL.PRIVATE.getAcl());
        postObjectRequest.setCacheControl("cache-control");
        postObjectRequest.setContentType("text/plain");
        postObjectRequest.setContentEncoding("utf-8");
        postObjectRequest.setCosStorageClass(COSStorageClass.STANDARD.getStorageClass());
        postObjectRequest.setExpires("100");
        postObjectRequest.setContentDisposition("form-data");
        postObjectRequest.setCustomerHeader("x-cos-meta-ssl", "sha1");
        postObjectRequest.setSuccessActionStatus(204);
        postObjectRequest.setSuccessActionRedirect("www.cloud.tencent.com");
        postObjectRequest.setPolicy(new PostObjectRequest.Policy());
        Map<String, String> map = postObjectRequest.testFormParameters();
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String, String> entry : map.entrySet()){
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        Log.d("XIAO", stringBuilder.toString());
    }

    @Test
    public void testPostObject() throws Exception{
        Context context = InstrumentationRegistry.getContext();
        QServer.init(context);
        String bucket = QServer.persistBucket;
        String cosPath = "postobject2.txt";
        String srcPath = QServer.createFile(context, 1024 * 1024);
        byte[] data = "this is post object test".getBytes("utf-8");
        PostObjectRequest postObjectRequest = new PostObjectRequest(bucket, cosPath, data);
        postObjectRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                Log.d("XIAO", "progress =" + complete / target);
            }
        });
        QServer.init(context);
        try {
            PostObjectResult postObjectResult = QServer.cosXml.postObject(postObjectRequest);
            Log.d("XIAO", postObjectResult.printResult());
        }catch (CosXmlClientException ex){
            throw  ex;
        }catch (CosXmlServiceException ex) {
            Log.d("XIAO", ex.getMessage());
        }
        QServer.deleteLocalFile(srcPath);
    }

}