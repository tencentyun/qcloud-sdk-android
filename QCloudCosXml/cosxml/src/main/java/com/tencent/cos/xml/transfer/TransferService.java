/*
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.tencent.cos.xml.transfer;

import com.tencent.cos.xml.CosXmlSimpleService;
import com.tencent.cos.xml.crypto.COSDirectImpl;
import com.tencent.cos.xml.crypto.CryptoModuleAE;
import com.tencent.cos.xml.crypto.CryptoModuleBase;
import com.tencent.cos.xml.crypto.EncryptionMaterialsProvider;
import com.tencent.cos.xml.crypto.QCLOUDKMS;
import com.tencent.cos.xml.crypto.TencentCloudKMSClient;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;


/**
 * 高级传输接口
 */
public class TransferService {

    private CryptoModuleBase cryptoModule;

    protected CosXmlSimpleService cosXmlService;
    protected TransferConfig transferConfig;

    private QCLOUDKMS kms;

    /**
     * TransferManager 构造器，
     *
     * @param cosXmlService CosXmlSimpleService 对象，用于真正发起传输请求
     * @param transferConfig 传输配置类
     */
    public TransferService(CosXmlSimpleService cosXmlService, TransferConfig transferConfig){
        if(cosXmlService == null){
            throw new IllegalArgumentException("CosXmlService is null");
        }
        if(transferConfig == null){
            throw new IllegalArgumentException("TransferConfig is null");
        }
        this.cosXmlService = cosXmlService;
        this.transferConfig = transferConfig;
    }

    public TransferService(CosXmlSimpleService cosXmlService, TransferConfig transferConfig,
                           EncryptionMaterialsProvider encryptionMaterialsProvider){
        this(cosXmlService, transferConfig);
        this.kms = newTencentCloudKMSClient(cosXmlService.getCredentialProvider());
        cryptoModule = new CryptoModuleAE(kms, cosXmlService, cosXmlService.getCredentialProvider(), encryptionMaterialsProvider);
    }

    private TencentCloudKMSClient newTencentCloudKMSClient(QCloudCredentialProvider credentialProvider) {
        String region = cosXmlService.getConfig().getRegion();
        final TencentCloudKMSClient kmsClient = new TencentCloudKMSClient(region, credentialProvider);
        return kmsClient;
    }

    /**
     * 高级上传接口，
     *
     * @param putObjectRequest 上传请求 Request 封装类
     * @return COSUploadTask 上传任务
     */
    public COSUploadTask upload(PutObjectRequest putObjectRequest) {
        
        COSUploadTask cosUploadTask = new COSUploadTask(cosXmlService, putObjectRequest);
        cosUploadTask.setApiDirect(new COSDirectImpl(cosXmlService, cryptoModule));
        cosUploadTask.start();
        return cosUploadTask;
    }

    /**
     * 高级下载接口
     *
     * @param getObjectRequest 下载请求的 Request 封装类
     * @return COSDownloadTask 下载任务
     */
    public COSDownloadTask download(GetObjectRequest getObjectRequest) {
        COSDownloadTask cosDownloadTask = new COSDownloadTask(cosXmlService, getObjectRequest);
        cosDownloadTask.setApiDirect(new COSDirectImpl(cosXmlService, cryptoModule));
        cosDownloadTask.start();
        return cosDownloadTask;
    }
}
