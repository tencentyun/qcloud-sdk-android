package com.tencent.qcloud.network.sonar;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.qcloud.network.sonar.dns.DnsResult;
import com.tencent.qcloud.network.sonar.ping.PingResult;
import com.tencent.qcloud.network.sonar.traceroute.TracerouteResult;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(Integer.MAX_VALUE));
    static {
        threadPoolExecutor.allowCoreThreadTimeOut(true);
    }

    @Test
    public void sonar() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        String url = "harmony-cos-1258344699.cos.ap-guangzhou.myqcloud.com";
//        String url = "taobao.com";
        List<SonarType> types = new ArrayList<>();
        types.add(SonarType.DNS);
        types.add(SonarType.PING);
        types.add(SonarType.TRACEROUTE);
        final TestLocker testLocker = new TestLocker();
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                NetworkSonar.sonar(appContext, new SonarRequest(url), types, new NetworkSonarCallback() {
                    @Override
                    public void onSuccess(SonarResult result) {
                        Log.d("sonar", "onSuccess: " + result.getType().toString());
                        switch (result.getType()) {
                            case DNS:
                                DnsResult dnsResult = (DnsResult) result.getResult();
                                Log.d("sonar", dnsResult.toString());
                                break;
                            case PING:
                                PingResult pingResult = (PingResult) result.getResult();
                                Log.d("sonar", pingResult.toString());
                                break;
                            case TRACEROUTE:
                                TracerouteResult tracerouteResult = (TracerouteResult) result.getResult();
                                Log.d("sonar", tracerouteResult.toString());
                                break;
                        }
                    }

                    @Override
                    public void onFail(SonarResult result) {
                        Log.d("sonar", "onFail: " + result.getType().toString());
                        Log.e("sonar", result.getException().getMessage(), result.getException());
                    }

                    @Override
                    public void onFinish(List<SonarResult> results) {
                        Log.d("sonar", "Finish");
                        testLocker.release();
                    }
                });
            }
        });
        testLocker.lock();
    }
}