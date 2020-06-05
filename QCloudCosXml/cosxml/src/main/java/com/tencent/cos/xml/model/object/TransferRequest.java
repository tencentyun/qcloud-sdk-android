package com.tencent.cos.xml.model.object;

interface TransferRequest {

    /**
     * 单链接限速，单位为bit/s
     *
     * 限速值设置范围为819200~838860800，即100KB/s~100MB/s，如果超出该范围会返回400错误。
     * @param limit
     */
    void setTrafficLimit(long limit);
}
