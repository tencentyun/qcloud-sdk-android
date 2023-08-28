package com.tencent.cos.cosxml.ktx.example

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tencent.cos.xml.CosXmlService
import com.tencent.cos.xml.ktx.cosBucket
import com.tencent.cos.xml.ktx.cosObject
import com.tencent.cos.xml.ktx.cosService
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider
import kotlinx.coroutines.launch

/**
 * <p>
 * </p>
 * Created by wjielai on 2020/7/8.
 * Copyright 2010-2020 Tencent Cloud. All Rights Reserved.
 */

class MainViewModel(application: Application) : AndroidViewModel(application){

    private val cos: CosXmlService

    init {
        cos = cosService(context = application.applicationContext) {

            configuration {
                setDebuggable(true)
                setRegion(BuildConfig.PERSIST_BUCKET_REGION)
                isHttps(true)
            }

            credentialProvider {
//                lifecycleCredentialProvider {
//                    // fetch credential from backend
//                    // ...
//                    return@lifecycleCredentialProvider SessionQCloudCredentials(
//                            "temp_secret_id",
//                            "temp_secret_key",
//                            "session_token",
//                            1565168075
//                    )
//                }
                return@credentialProvider ShortTimeCredentialProvider(BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY,60000)
            }
        }
    }

    fun downloadObject(appContext: Context, tv: TextView) {

        viewModelScope.launch {
            val `object` = cosObject {
                bucket = cosBucket {
                    service = cos
                    name = BuildConfig.PERSIST_BUCKET
                }
                key = "do_not_remove/big_object"
            }

            try {
                val result = `object`.download(
                        context = appContext,
                        destDirectory = appContext.externalCacheDir!!,
                        progressListener = { complete, target ->
                            Log.d("cosxmlktx", "download onProgress: " +
                                    "$complete / $target")
                        },
                        transferStateListener = { state ->
                            Log.d("cosxmlktx", "download state is : $state")
                        }
                )

                tv.text = "download success"

            } catch (e : Exception ) {
                e.printStackTrace()
                tv.text = "download error ${e.message}"
            }

        }
    }
}