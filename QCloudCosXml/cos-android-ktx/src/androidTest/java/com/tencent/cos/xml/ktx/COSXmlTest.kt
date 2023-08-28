package com.tencent.cos.xml.ktx

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.model.tag.ACLAccount
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.util.*

/**
 * Created by wjielai on 2020/7/7.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */
@RunWith(AndroidJUnit4::class)
class COSXmlTest {
    @Test
    fun simpleTest() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val sourceFile = File(appContext.externalCacheDir, "example")
        createFile(appContext, sourceFile, 1024 * 1024 * 2)

        val cos = cosService(context = appContext) {
            credentialProvider {
                ShortTimeCredentialProvider(
                        BuildConfig.COS_SECRET_ID,
                        BuildConfig.COS_SECRET_KEY,
                        600)
            }

            configuration {
                setDebuggable(true)
                setRegion("ap-guangzhou")
                isHttps(true)
            }
        }

        runBlocking {
            val cosBucket = cosBucket {
                service = cos
                name = "abc-1253653367"
                accessControl {
                    privateReadWrite()
                }
            }

            try {
                cosBucket.create()
            } catch (e: CosXmlServiceException) {
                Assert.assertEquals(e.statusCode, 409);
            }
            cosBucket.head()

            cosBucket.setAccessControl {
                privateReadWrite()
                read = ACLAccount().apply {
                    addAccount("100000000001", "100000000001")
                }
            }

            val `object` = cosObject {
                bucket = cosBucket
                key = "exampleObject"
            }

            `object`.upload(
                    localFile = sourceFile,
                    progressListener = { complete, target ->
                        Log.d("cosxmlktx", "upload onProgress: $complete / $target")
                    },
                    transferStateListener = { state ->
                        Log.d("cosxmlktx", "upload state is : $state")
                    }
            )
            `object`.head()

            `object`.download(
                    context = appContext,
                    destDirectory = appContext.externalCacheDir!!,
                    progressListener = { complete, target ->
                        Log.d("cosxmlktx", "download onProgress: $complete / $target")
                    },
                    transferStateListener = { state ->
                        Log.d("cosxmlktx", "download state is : $state")
                    }
            )

            `object`.delete()
        }

    }

    @Throws(IOException::class)
    fun createFile(context: Context, tempFile: File, fileLength: Long) {
        if (tempFile.exists()) {
            tempFile.delete()
        }

        val accessFile = RandomAccessFile(tempFile.path, "rws")
        accessFile.setLength(fileLength)
        accessFile.write(Random().nextInt(200))
        accessFile.seek(fileLength / 2)
        accessFile.write(Random().nextInt(200))
        accessFile.close()
    }
}