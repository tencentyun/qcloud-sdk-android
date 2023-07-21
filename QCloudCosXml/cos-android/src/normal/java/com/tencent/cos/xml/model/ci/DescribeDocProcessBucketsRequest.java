package com.tencent.cos.xml.model.ci;

import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.common.RequestMethod;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.qcloud.core.http.RequestBodySerializer;

/**
 * <p>
 * </p>
 * Created by wjielai on 2020/6/17.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

public class DescribeDocProcessBucketsRequest extends CosXmlRequest {
    public DescribeDocProcessBucketsRequest() {
    }

    public DescribeDocProcessBucketsRequest(String region) {
        this.region = region;
    }

    /**
     * 地域信息，以“,”分隔字符串，支持 All、ap-shanghai、ap-beijing
     * @param regions 地域信息，以“,”分隔字符串，支持 All、ap-shanghai、ap-beijing
     */
    public void setRegions(String regions) {
        queryParameters.put("regions", regions);
    }

    /**
     * 存储桶名称，以“,”分隔，支持多个存储桶，精确搜索
     * @param bucketNames 存储桶名称，以“,”分隔，支持多个存储桶，精确搜索
     */
    public void setBucketNames(String bucketNames) {
        queryParameters.put("bucketNames", bucketNames);
    }

    /**
     * 存储桶名称前缀，前缀搜索
     * @param bucketName 存储桶名称前缀，前缀搜索
     */
    public void setBucketName(String bucketName) {
        queryParameters.put("bucketName", bucketName);
    }

    /**
     * 第几页
     * @param pageNumber 第几页
     */
    public void setPageNumber(int pageNumber) {
        queryParameters.put("pageNumber", String.valueOf(pageNumber));
    }

    /**
     * 每页个数
     * @param pageSize 每页个数
     */
    public void setPageSize(int pageSize) {
        queryParameters.put("pageSize", String.valueOf(pageSize));
    }

    @Override
    public String getMethod() {
        return RequestMethod.GET;
    }

    @Override
    public String getPath(CosXmlServiceConfig cosXmlServiceConfig) {
        return "/docbucket";
    }

    @Override
    public RequestBodySerializer getRequestBody() throws CosXmlClientException {
        return null;
    }

    @Override
    public String getRequestHost(CosXmlServiceConfig config) {
        return config.getRequestHost(region, CosXmlServiceConfig.CI_REGION_HOST_FORMAT);
    }
}
