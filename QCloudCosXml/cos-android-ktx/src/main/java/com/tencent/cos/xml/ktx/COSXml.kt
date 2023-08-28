package com.tencent.cos.xml.ktx

import android.content.Context
import android.net.Uri
import com.tencent.cos.xml.CosXmlService
import com.tencent.cos.xml.CosXmlServiceConfig
import com.tencent.cos.xml.common.COSACL
import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.listener.CosXmlProgressListener
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.model.CosXmlRequest
import com.tencent.cos.xml.model.CosXmlResult
import com.tencent.cos.xml.model.`object`.DeleteObjectRequest
import com.tencent.cos.xml.model.`object`.DeleteObjectResult
import com.tencent.cos.xml.model.`object`.HeadObjectRequest
import com.tencent.cos.xml.model.`object`.HeadObjectResult
import com.tencent.cos.xml.model.bucket.*
import com.tencent.cos.xml.model.tag.ACLAccount
import com.tencent.cos.xml.transfer.TransferConfig
import com.tencent.cos.xml.transfer.TransferManager
import com.tencent.cos.xml.transfer.TransferState
import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider
import com.tencent.qcloud.core.auth.QCloudCredentialProvider
import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.io.InputStream
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Created by wjielai on 2020/7/3.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

data class COSBucket(private val builder: COSBucketBuilder) {

    val name: String
    val service: CosXmlService
    val aclRule: COSACLRule?

    init {
        service = builder.service ?: throw IllegalArgumentException("cos service is null")
        name = builder.name ?: throw IllegalArgumentException("name is null")
        aclRule = builder.aclRule
    }

    suspend fun create(): PutBucketResult {
        return suspendBlock<PutBucketResult> { listener ->
            val request = PutBucketRequest(name)
            aclRule?.let {
                request.setXCOSACL(it.acl)
                request.setXCOSGrantRead(it.read)
                request.setXCOSGrantWrite(it.write)
                request.setXCOSReadWrite(it.readwrite)
            }
            service.putBucketAsync(request, listener)
        }
    }

    suspend fun head(): HeadBucketResult {
        return suspendBlock<HeadBucketResult> { listener ->
            val request = HeadBucketRequest(name)
            service.headBucketAsync(request, listener)
        }
    }

    suspend fun setAccessControl(init: COSACLRule.() -> Unit): PutBucketACLResult {
        val rule = COSACLRule().apply(init)
        return suspendBlock<PutBucketACLResult> { listener ->
            val request = PutBucketACLRequest(name)
            request.setXCOSACL(rule.acl)
            request.setXCOSGrantRead(rule.read)
            request.setXCOSGrantWrite(rule.write)
            request.setXCOSReadWrite(rule.readwrite)

            service.putBucketACLAsync(request, listener)
        }
    }
}

class COSBucketBuilder {
    var service:CosXmlService? = null

    var name: String? = null
    var aclRule:COSACLRule? = null

    fun accessControl(init: COSACLRule.() -> Unit) {
        val rule = COSACLRule()
        rule.init()
        aclRule = rule
    }
}

data class COSObject(private val builder: COSObjectBuilder) {

    val bucket: COSBucket
    val key:String
    val service:CosXmlService

    init {
        bucket = builder.bucket ?: throw java.lang.IllegalArgumentException("bucket is null")
        key = builder.key ?: throw java.lang.IllegalArgumentException("key is null")
        service = bucket.service
    }

    suspend fun head(): HeadObjectResult {
        return suspendBlock { listener ->
            val request = HeadObjectRequest(bucket.name, key)
            service.headObjectAsync(request, listener)
        }
    }

    suspend fun upload(localFile: File? = null,
                       bytes: ByteArray? = null,
                       uri: Uri? = null,
                       inputStream: InputStream? = null,
                       uploadId: String? = null,
                       progressListener: ((complete: Long, target: Long)->Unit)? = null,
                       transferStateListener:((state: TransferState)->Unit)? = null):
            CosXmlResult {

        return suspendBlock<CosXmlResult> { listener ->
            val task = localFile?.let {
                service.transferManager.upload(bucket.name, key, it.path, uploadId)
            } ?: bytes?.let {
                service.transferManager.upload(bucket.name, key, it)
            } ?: uri?.let {
                service.transferManager.upload(bucket.name, key, it, uploadId)
            } ?: inputStream?.let {
                service.transferManager.upload(bucket.name, key, it)
            } ?: throw java.lang.IllegalArgumentException("upload source is null")

            task.setCosXmlResultListener(listener)
            task.setCosXmlProgressListener(progressListener)
            task.setTransferStateListener(transferStateListener)
        }
    }

    suspend fun download(context: Context,
                         destDirectory: File,
                         fileName: String = key,
                         progressListener: ((complete: Long, target: Long)->Unit)? = null,
                         transferStateListener:((state: TransferState)->Unit)? = null):
            CosXmlResult {

        return suspendBlock<CosXmlResult> { listener ->
            val task = service.transferManager.download(context, bucket.name, key,
                    destDirectory.path, fileName)

            task.setCosXmlResultListener(listener)
            task.setCosXmlProgressListener(progressListener)
            task.setTransferStateListener(transferStateListener)
        }
    }

    suspend fun delete(): DeleteObjectResult {
        return suspendBlock { listener ->
            val request = DeleteObjectRequest(bucket.name, key)
            service.deleteObjectAsync(request, listener)
        }
    }
}

class COSObjectBuilder {
    var bucket:COSBucket? = null

    var key: String? = null
    var aclRule:COSACLRule? = null

    fun accessControl(init: COSACLRule.() -> Unit) {
        val rule = COSACLRule()
        rule.init()
        aclRule = rule
    }
}

class COSServiceBuilder(val context: Context) {
    var cp: QCloudCredentialProvider? = null
        private set
    var cosXmlConfig: CosXmlServiceConfig? = null
        private set

    fun configuration(init: CosXmlServiceConfig.Builder.() -> Unit) {
        val builder = CosXmlServiceConfig.Builder().apply(init)
        cosXmlConfig = builder.builder()
    }

    fun credentialProvider(callback: () -> QCloudCredentialProvider) {
        cp = callback()
    }

    fun lifecycleCredentialProvider(callback: () -> QCloudLifecycleCredentials):
            QCloudCredentialProvider {
        return object : BasicLifecycleCredentialProvider() {
            override fun fetchNewCredentials(): QCloudLifecycleCredentials {
                return callback()
            }
        }
    }
}

data class COSACLRule(var read: ACLAccount? = null,
                      var write: ACLAccount? = null,
                      var readwrite: ACLAccount? = null) {

    var acl: COSACL? = null
        private set

    fun privateReadWrite() {
        acl = COSACL.PRIVATE
    }

    fun publicRead() {
        acl = COSACL.PUBLIC_READ
    }

    fun publicReadWrite() {
        acl = COSACL.PUBLIC_READ_WRITE
    }
}

fun cosService(context: Context, init: COSServiceBuilder.() -> Unit): CosXmlService {
    val builder = COSServiceBuilder(context).apply(init)
    val config = builder.cosXmlConfig ?: throw java.lang.IllegalArgumentException("config is null")
    val provider = builder.cp
    return CosXmlService(builder.context, config, provider)
}

fun cosBucket(init: COSBucketBuilder.() -> Unit): COSBucket {
    return COSBucket(COSBucketBuilder().apply(init))
}

fun cosObject(init: COSObjectBuilder.() -> Unit): COSObject {
    return COSObject(COSObjectBuilder().apply(init))
}


val CosXmlService.transferManager: TransferManager
    get() = TransferManager(this, TransferConfig.Builder().build())


suspend inline fun <T> suspendBlock(crossinline block: (listener: CosXmlResultListener) -> Unit): T where T : CosXmlResult {
    return suspendCancellableCoroutine<T> { cont ->
        block(cosXmlListenerWrapper(cont))
    }
}

fun <T> cosXmlListenerWrapper(cont: Continuation<T>): CosXmlResultListener where T : CosXmlResult {
    return object : CosXmlResultListener {
        override fun onSuccess(p0: CosXmlRequest?, p1: CosXmlResult?) {
            cont.resume(p1 as T)
        }

        override fun onFail(p0: CosXmlRequest?, p1: CosXmlClientException?, p2: CosXmlServiceException?) {
            cont.resumeWithException(p1 ?: p2!!)
        }
    }
}