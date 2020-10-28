package com.tencent.qcloud.qcloudxml;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.qcloud.qcloudxml.core.QCloudXml;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String xml = "\n" +
                "<TestBeana>\n" +
                "  <Aab>aaa</Aab>\n" +
                "  <Bbb>3</Bbb>\n" +
                "  <ITestBean2>\n" +
                "    <Ibbb>1</Ibbb>\n" +
                "  </ITestBean2>\n" +
                "  <ITestBean1>\n" +
                "    <Iaaa>iaaaaaa</Iaaa>\n" +
                "  </ITestBean1>\n" +
                "  <ITestBean2List>\n" +
                "    <ITestBean2>\n" +
                "      <Ibbb>1</Ibbb>\n" +
                "    </ITestBean2>\n" +
                "    <ITestBean2>\n" +
                "      <Ibbb>2</Ibbb>\n" +
                "    </ITestBean2>\n" +
                "    <ITestBean2>\n" +
                "      <Ibbb>3</Ibbb>\n" +
                "    </ITestBean2>\n" +
                "  </ITestBean2List>\n" +
                "</TestBeana>";

//        @SuppressLint("ResourceType") InputStream inputStream = this.getResources().openRawResource(R.xml.test);

        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

        TestBean testBean = null;
        try {
            testBean = QCloudXml.fromXml(inputStream, TestBean.class);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Toast.makeText(this, testBean.aaa, Toast.LENGTH_LONG).show();

        TestBean testBeanTo = new TestBean();
        testBeanTo.aaa = "aaa";
        testBeanTo.bbb = 3;
        testBeanTo.iTestBean1 = new TestBean.ITestBean1();
        testBeanTo.iTestBean1.iaaa = "iaaaaaa";
        testBeanTo.iTestBean2List = new ArrayList<>();
        TestBean.ITestBean2 iTestBean21 = new TestBean.ITestBean2();
        iTestBean21.ibbb = 1;
        TestBean.ITestBean2 iTestBean22 = new TestBean.ITestBean2();
        iTestBean22.ibbb = 2;
        TestBean.ITestBean2 iTestBean23 = new TestBean.ITestBean2();
        iTestBean23.ibbb = 3;
        testBeanTo.iTestBean2List.add(iTestBean21);
        testBeanTo.iTestBean2List.add(iTestBean22);
        testBeanTo.iTestBean2List.add(iTestBean23);

        String xmlTo = "";
        try {
            xmlTo = QCloudXml.toXml(testBeanTo);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("qjd", xmlTo);
    }
}