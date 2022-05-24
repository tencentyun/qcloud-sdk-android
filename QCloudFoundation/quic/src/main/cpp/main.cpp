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

#include "COSQuic.h"
#include "cos_common.h"

struct QUIC_HANDLE_STRUCT *quic_handle_struct = NULL;
bool debug = false;
bool is_init = false;

TnetConfig tnetConfig;

map<int, COSQuic *> cos_quic_map;
pthread_mutex_t g_mut;

static JNINativeMethod g_methods[] = {
        {"init", "()V", (void *)init},
        {"setTnetConfigRaceType", "(I)V", (void *)set_tnet_config_race_type},
        {"setTnetConfigIsCustomProtocol", "(Z)V", (void *)set_tnet_config_is_custom_protocol},
        {"setTnetConfigTotalTimeoutSec", "(I)V", (void *)set_tnet_config_total_timeout_sec},
        {"setDebugLog", "(Z)V", (void *)set_debug_log},
        {"connect", "(ILjava/lang/String;Ljava/lang/String;II)V", (void *)connect},
        {"addHeader", "(ILjava/lang/String;Ljava/lang/String;)V", (void *)add_header},
        {"sendRequest", "(I[BIZ)V", (void *)send_request},
        {"cancelRequest", "(I)V", (void *)cancel_request},
        {"getState", "(I)Ljava/lang/String;", (void *)get_state},
        {"destory", "()V", (void *)destory},
        {"clear", "(I)V", (void *)clear},
};

jint regiseter_methods(JNIEnv *env){
    jclass clazz = env->FindClass("com/tencent/qcloud/quic/QuicNative");
    if(clazz == NULL){
        LOGE(true, "unable to find class: com/tencent/qcloud/quic/QuicNative");
        return 0;
    }
    if((env->RegisterNatives(clazz, g_methods, sizeof(g_methods) / sizeof(g_methods[0]))) < 0){
        LOGE(true, "register native methods failed for class: com/tencent/qcloud/quic/QuicNative");
        return 0;
    }
    return 1;
}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved)
{
    if(quic_handle_struct == NULL){
        quic_handle_struct = new QUIC_HANDLE_STRUCT();
    }
    quic_handle_struct->m_vm = vm;
    JNIEnv* env;
    if(vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK){
        LOGE(true, "JNI_OnLoad failed caused by JNI_VERSION_1_6");
        return 0;
    }
    //find class and register method
    if(!regiseter_methods(env)){
        LOGE(true, "JNI_OnLoad failed caused by register methods");
        return 0;
    }
    pthread_mutex_init(&g_mut, NULL);
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved){
     if(quic_handle_struct != NULL){
            delete quic_handle_struct;
     }
    pthread_mutex_destroy(&g_mut);
}

void init(JNIEnv *env, jobject thiz){
    //初始化 quic_handle_struct
    if(is_init) return;
    jclass local_clazz = env->FindClass("com/tencent/qcloud/quic/QuicNative");
    jmethodID local_connect = env->GetMethodID(local_clazz, "onConnect", "(I)V");
    if(!local_connect) return;
    quic_handle_struct->connect = local_connect;

    jmethodID local_data_recv = env->GetMethodID(local_clazz, "onDataReceive", "([BI)V");
    if(!local_data_recv) return;
    quic_handle_struct->dataReceive = local_data_recv;

    jmethodID local_complete= env->GetMethodID(local_clazz, "onCompleted", "(I)V");
    if(!local_complete) return;
    quic_handle_struct->completed = local_complete;

    jmethodID local_close = env->GetMethodID(local_clazz,  "onClose", "(ILjava/lang/String;)V");
    if(!local_complete) return;
    quic_handle_struct->close = local_close;

    tnetConfig.upload_optimize_ = true;
    tnetConfig.congestion_type_ = kBBR;

    is_init = true;
}

void set_debug_log(JNIEnv *env, jobject thiz, jboolean is_debug){
    debug = (is_debug == JNI_TRUE? true: false);
}


void set_tnet_config_race_type(JNIEnv *env, jobject thiz, jint race_type) {

    if (race_type == 0) {
        tnetConfig.race_type = kOnlyQUIC;
    } else if (race_type == 1) {
        tnetConfig.race_type = kQUICHTTP;
    } else if (race_type == 2) {
        tnetConfig.race_type = kOnlyHTTP;
    }
}

void set_tnet_config_is_custom_protocol(JNIEnv *env, jobject thiz, jboolean is_custom_protocol) {
    tnetConfig.is_custom_ = is_custom_protocol;
}

void set_tnet_config_total_timeout_sec(JNIEnv *env, jobject thiz, jint total_timeout_sec) {
    tnetConfig.total_timeout_sec_ = total_timeout_sec;
}

void connect(JNIEnv *env, jobject thiz, jint handle_id, jstring host, jstring ip, jint port, jint tcp_port){
    //根据 handle_Id 找到对应的 COSQUIC
    int key_id = handle_id;
    pthread_mutex_lock(&g_mut);
    if(cos_quic_map.count(key_id) == 0){
        COSQuic *cos_quic = new COSQuic(env, thiz, handle_id, tnetConfig);
        cos_quic_map.insert(pair<int, COSQuic *>(key_id, cos_quic));
    }
    COSQuic *tmp = cos_quic_map[key_id];
    pthread_mutex_unlock(&g_mut);
    tmp->Connect(env, thiz, host, ip, port, tcp_port);
}

void add_header(JNIEnv *env, jobject thiz, jint handle_id, jstring key, jstring value){
    int key_id = handle_id;
    pthread_mutex_lock(&g_mut);
    if(cos_quic_map.count(key_id) == 0){
        COSQuic *cos_quic = new COSQuic(env, thiz, handle_id, tnetConfig);
        cos_quic_map.insert(pair<int, COSQuic *>(key_id, cos_quic));
    }
    COSQuic *tmp = cos_quic_map[key_id];
    pthread_mutex_unlock(&g_mut);
    tmp->AddHeaders(env, thiz, key, value);
}

void send_request(JNIEnv *env, jobject thiz, jint handle_id, jbyteArray data, jint len, jboolean finish){
    int key_id = handle_id;
    pthread_mutex_lock(&g_mut);
    if(cos_quic_map.count(key_id) == 0){
        COSQuic *cos_quic = new COSQuic(env, thiz, handle_id, tnetConfig);
        cos_quic_map.insert(pair<int, COSQuic *>(key_id, cos_quic));
    }
    COSQuic *tmp = cos_quic_map[key_id];
    pthread_mutex_unlock(&g_mut);
    jboolean is_send = tmp->SendRequest(env, thiz, data, len, finish);
}

void cancel_request(JNIEnv *env, jobject thiz, jint handle_id){
    int key_id = handle_id;
    pthread_mutex_lock(&g_mut);
    if(cos_quic_map.count(key_id) == 0){
        return;
    }
    COSQuic *tmp = cos_quic_map[key_id];
    pthread_mutex_unlock(&g_mut);
    tmp->CancelRequest(env, thiz);
}

jstring get_state(JNIEnv *env, jobject thiz, jint handle_id){
    int key_id = handle_id;
    pthread_mutex_lock(&g_mut);
    if(cos_quic_map.count(key_id) == 0){
        COSQuic *cos_quic = new COSQuic(env, thiz, handle_id, tnetConfig);
        cos_quic_map.insert(pair<int, COSQuic *>(key_id, cos_quic));
    }
    COSQuic *tmp = cos_quic_map[key_id];
    pthread_mutex_unlock(&g_mut);
    return tmp->GetState(env, thiz);
}

void destory(JNIEnv *env, jobject thiz){
    is_init = false;
    pthread_mutex_lock(&g_mut);
    if(cos_quic_map.size() > 0){
        map<int, COSQuic *> ::iterator iterator;
        for(iterator = cos_quic_map.begin(); iterator != cos_quic_map.end();){
            if(iterator->second != NULL) delete iterator->second;
            cos_quic_map.erase(iterator ++);
        }
    }
    pthread_mutex_unlock(&g_mut);
//    LOGI(debug, "remain %lu", cos_quic_map.size());
}

void clear(JNIEnv *env, jobject thiz, jint handle_id){
    int key_id = handle_id;
    pthread_mutex_lock(&g_mut);
    if(cos_quic_map.count(key_id) > 0){
        COSQuic *tmp = cos_quic_map[key_id];
        delete tmp;
        cos_quic_map.erase(key_id);
        LOGI(debug, "clear %d", key_id);
    }
    pthread_mutex_unlock(&g_mut);
}
