package com.tencent.qcloud.core.http;

/**
 * 安全关闭的Converter
 * 设置为SelfCloseConverter 网络请求后不再自动关闭response，需要业务自己进行关闭
 */
public interface SelfCloseConverter {
}
