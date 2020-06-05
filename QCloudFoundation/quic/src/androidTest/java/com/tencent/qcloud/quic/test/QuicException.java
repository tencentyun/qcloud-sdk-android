package com.tencent.qcloud.quic.test;

public class QuicException extends Exception {
    public QuicException(){
        super();
    }

    public QuicException(String message){
        super(message);
    }

    public QuicException(Throwable cause){
        super(cause);
    }

    public QuicException(String message, Throwable cause){
        super(message, cause);
    }
}
