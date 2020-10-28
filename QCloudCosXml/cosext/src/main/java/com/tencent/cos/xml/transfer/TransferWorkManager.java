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


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.worker.MonitorWorker;

@SuppressLint("RestrictedApi")
public class TransferWorkManager {

    private TransferScheduler transferScheduler;

    private CosXmlService cosXmlService;

    private WorkTransferConfig transferConfig;

    private CosXmlServiceConfig serviceConfig;

    private ParcelableCredentialProvider credentialProvider;


    public TransferWorkManager(Context context, CosXmlServiceConfig serviceConfig, WorkTransferConfig transferConfig,
                               ParcelableCredentialProvider credentialProvider) {

        this.serviceConfig = serviceConfig;
        this.transferConfig = transferConfig;
        this.credentialProvider = credentialProvider;
        transferScheduler = TransferScheduler.getInstance(context);
        cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);
        // MonitorWorker.startMonitor(context);
        ServiceAliveTracker.start(context);
    }

    public COSXMLUploadTask upload(String bucket, String cosKey, String filePath) {

        return this.upload(new UploadRequest.Builder(bucket, cosKey)
                .setRegion(serviceConfig.getRegion())
                .setFilePath(filePath).build());
    }


    /**
     * 添加一个传输任务，或者续传任务
     *
     * SDK 内部记录 COS 地址和本地文件地址的传输任务信息，下次上传时会自动续传
     *
     * @return uploadRequest 对应的 id
     */
    public COSXMLUploadTask upload(UploadRequest uploadRequest) {

        COSXMLUploadTask cosxmlUploadTask = new COSXMLUploadTask(cosXmlService, uploadRequest.getRegion(),
                uploadRequest.getBucket(), uploadRequest.getCosKey(), uploadRequest.getFilePath(), null);

        cosxmlUploadTask.multiUploadSizeDivision = transferConfig.divisionForUpload; // 分片上传的界限
        cosxmlUploadTask.sliceSize = transferConfig.sliceSizeForUpload; // 分片上传的分片大小
        // cosxmlUploadTask.setOnSignatureListener(onSignatureListener);
        transferScheduler.schedule(uploadRequest, cosxmlUploadTask, this);

        return cosxmlUploadTask;
    }


    byte[] toBytes() {

        byte[] serviceEgg;

        Parcel parcel = Parcel.obtain();

        parcel.writeParcelable(serviceConfig, 0);
        parcel.writeParcelable(transferConfig, 0);
        parcel.writeParcelable(credentialProvider, 0);

        serviceEgg = parcel.marshall();
        parcel.recycle();
        return serviceEgg;
    }
}
