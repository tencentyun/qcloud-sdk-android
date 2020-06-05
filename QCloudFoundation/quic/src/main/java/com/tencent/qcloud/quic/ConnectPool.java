package com.tencent.qcloud.quic;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import static com.tencent.qcloud.quic.QuicNative.CLIENT_FAILED;
import static com.tencent.qcloud.quic.QuicNative.COMPLETED;
import static com.tencent.qcloud.quic.QuicNative.CONNECTED;
import static com.tencent.qcloud.quic.QuicNative.INIT;
import static com.tencent.qcloud.quic.QuicNative.SERVER_FAILED;

public class ConnectPool {
    private final int MAX_REQUEST_SIZE = 5;
    private Deque<QuicNative> quicNatives = new ArrayDeque<>();
    private Object sync = new Object();

    protected void init(boolean isDebugLog){
        QuicNative.init();
        QuicNative.setDebugLog(isDebugLog);
    }

    public static QuicNative createNewQuicNative(String host, String ip, int port, int tcpPort) {

        QuicNative quicNative = new QuicNative();
        quicNative.host = host;
        quicNative.ip = ip;
        quicNative.port = port;
        quicNative.tcpPort = tcpPort;
        quicNative.currentState = INIT;
        return quicNative;
    }

    /**
     * obtain a quic connect's current state = INIT or CONNECTED
     * @param host
     * @param ip
     * @param port
     * @param tcpPort
     * @return
     */
    @Deprecated
    protected QuicNative getQuicNative(String host, String ip, int port, int tcpPort){
        QuicNative tmp = null;
//
//        tmp = new QuicNative();
//        tmp.host = host;
//        tmp.ip = ip;
//        tmp.port = port;
//        tmp.tcpPort = tcpPort;

        QLog.d("get an quic connect for(%s, %s, %d, %d)", host, ip, port, tcpPort);
        while (true) {
            synchronized (sync) {
                if(quicNatives.size() > MAX_REQUEST_SIZE) {
                    //复用一个已完成请求-响应且闲时较长的链接
                    long idleTime = Long.MAX_VALUE;
                    for(QuicNative quicNative : quicNatives) {
                        if (quicNative.host.equals(host)
                                && quicNative.ip.equals(ip)
                                && quicNative.port == port
                                && quicNative.tcpPort == tcpPort
                                && quicNative.isCompleted) {
                            QLog.d("has an old quic connect[%d] can reuse", quicNative.handleId);
                            quicNative.currentState = CONNECTED;
                            if(quicNative.idleStartTime <= idleTime){
                                tmp = quicNative;
                            }
                        }
                    }
                } else {
                    QLog.d("add new quic connect");
                    QuicNative quicNative = new QuicNative();
                    quicNative.host = host;
                    quicNative.ip = ip;
                    quicNative.port = port;
                    quicNative.tcpPort = tcpPort;
                    quicNatives.add(quicNative);
                    quicNative.currentState = INIT;
                    tmp = quicNative;
                    break;
                }


                if(tmp == null){
                    tmp = getLongestIdleConnect();
                    if(tmp != null){
                        QLog.d("get a longest idle quic connect[%d] for request", tmp.handleId);
                        tmp.host = host;
                        tmp.ip = ip;
                        tmp.port = port;
                        tmp.currentState = INIT;
                        tmp.tcpPort = tcpPort;
                    }
                }
                if(tmp != null){
                    tmp.isCompleted = false;
                    tmp.idleStartTime = Long.MAX_VALUE;
                }
            }

            if(tmp == null){
                //继续等待
                QLog.d("wait an idle quic connect");
                // dumpQuicNatives();
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

    void dumpQuicNatives() {

        QLog.d("handle message  dump quicNatives");
        QLog.d("handle message  quickNative size " + quicNatives.size());
        for(Iterator<QuicNative> iterator = quicNatives.iterator(); iterator.hasNext();) {
            QuicNative quicNative = iterator.next();
            QLog.d("handle message  quickNative handleId: " + quicNative.handleId);
            QLog.d("handle message  quickNative isCompleted: " + quicNative.isCompleted);

        }
    }

    protected void updateQuicNativeState(QuicNative quicNative, int newState){
        synchronized (sync){
            switch (newState){
                case CONNECTED:
                    quicNative.currentState = CONNECTED;
                    break;
                case COMPLETED:
                    quicNative.isCompleted = true;
                    quicNative.currentState = COMPLETED;
                    quicNative.idleStartTime = System.nanoTime();
                    QLog.d("quicnative[%d] is Commpleted", quicNative.handleId);
                    break;
                case SERVER_FAILED:
                case CLIENT_FAILED:
                    for(Iterator<QuicNative> iterator = quicNatives.iterator(); iterator.hasNext();){
                        if(iterator.next() == quicNative){
                            iterator.remove();
                            QLog.d("quicnative[%d] is failed and remove it from queue", quicNative.handleId);
                            break;
                        }
                    }
                    break;
                    default:
                        throw new IllegalStateException("invalid state: " + newState);
            }
        }
    }

//    protected void setComplete(QuicNative quicNative){
//        synchronized (sync){
//            quicNative.isCompleted = true;
//            quicNative.idleStartTime = System.nanoTime();
//            QLog.d("quicnative[%d] is Commpleted and remove it from queue", quicNative.handleId);
//        }
//    }

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
