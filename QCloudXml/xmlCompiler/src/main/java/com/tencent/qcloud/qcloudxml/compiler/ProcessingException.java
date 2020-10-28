package com.tencent.qcloud.qcloudxml.compiler;

import javax.lang.model.element.Element;

/**
 * 处理注解引发的异常
 */
public class ProcessingException extends Exception {
    private static final String TAG = "ProcessingException";

    private Element element;

    public ProcessingException(Element element, String message) {
        super(message);
        this.element = element;
    }
}
