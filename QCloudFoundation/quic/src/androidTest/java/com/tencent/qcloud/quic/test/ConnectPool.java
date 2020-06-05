package com.tencent.qcloud.quic.test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class ConnectPool {
    private final int MAX_REQUEST_SIZE = 3;
    private Deque<QuicNative> quicNatives = new ArrayDeque<>();
    private Object sync = new Object();

    protected void init(boolean isDebugLog){
        QuicNative.init();
        QuicNative.setDebugLog(isDebugLog);
    }

    protected QuicNative getQuicNative(String host, String ip, int port, int tcpPort){
        QuicNative tmp = null;
        while (true){
            synchronized (sync){
                if(quicNatives.size() > MAX_REQUEST_SIZE){
                    //复用一个已完成请求-响应且闲时较长的链接
                    QLog.d("search an idle quic connect for(%s, %s, %d, %d)", host, ip, port, tcpPort);
                    long idleTime = Long.MAX_VALUE;
                    for(QuicNative quicNative : quicNatives) {
                        if (quicNative.host.equals(host)
                                && quicNative.ip.equals(ip)
                                && quicNative.port == port
                                && quicNative.tcpPort == tcpPort
                                && quicNative.isCompleted) {
                            QLog.d("has an old quic connect[%d] can reuse", quicNative.handleId);
                            if(quicNative.idleStartTime <= idleTime){
                                tmp = quicNative;
                            }
                        }
                    }
                }else {
                    QLog.d("add new quic connect");
                    QuicNative quicNative = new QuicNative();
                    quicNative.host = host;
                    quicNative.ip = ip;
                    quicNative.port = port;
                    quicNative.tcpPort = tcpPort;
                    quicNatives.add(quicNative);
                    tmp = quicNative;
                    break;
                }
                if(tmp == null){
                    tmp = getLongestIdleConnect();
                    if(tmp != null){
                        QLog.d("get a longest idle quic connect[%d] for request", tmp.handleId);
                        tmp.currentState = QuicNative.INIT;
                        tmp.host = host;
                        tmp.ip = ip;
                        tmp.port = port;
                        tmp.tcpPort = tcpPort;
                    }
                }
            }

            if(tmp == null){
                //继续等待
                QLog.d("wait an idle quic connect");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }else {
                break;
            }
        }
        return tmp;
    }

    protected void setComplete(QuicNative quicNative, boolean isCompleted){
        synchronized (sync){
            QLog.d("quicnative[%d] is Commpleted %s", quicNative.handleId, String.valueOf(isCompleted));
            quicNative.isCompleted = isCompleted;
            if(quicNative.isCompleted){
                quicNative.idleStartTime = System.nanoTime();
            }else {
                quicNative.idleStartTime = Long.MAX_VALUE;
            }
        }
    }

    /**
     * get a quic connect with a long idle
     */
    protected QuicNative getLongestIdleConnect(){
        QuicNative longestIdleQuicNative = null;
        synchronized (sync){
            long tmp = Long.MAX_VALUE;
            for(Iterator<QuicNative> iterator = quicNatives.iterator(); iterator.hasNext();){
                QuicNative quicNative = iterator.next();
                if(quicNative.isCompleted && quicNative.idleStartTime <= tmp){
                    tmp = quicNative.idleStartTime;
                    longestIdleQuicNative = quicNative;
                }
            }
        }
        return longestIdleQuicNative;
    }


    protected void destroy(){
        synchronized (sync){
            quicNatives.clear();
            QuicNative.destory();
        }
    }


}
