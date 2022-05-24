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

#ifndef QCLOUDFOUNDATION_COS_COMMON_H
#define QCLOUDFOUNDATION_COS_COMMON_H

#include <android/log.h>
#include <jni.h>
#include <stddef.h>
#include <map>
#include <pthread.h>
#include<iostream>
using namespace std;

#define TAG "QCloudQuic"
#define LOGI(debug, ...) {if(debug)__android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__);}
#define LOGD(debug, ...) {if(debug)__android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__);}
#define LOGE(debug, ...) {if(debug)__android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__);}

#ifdef  __cplusplus
extern "C"{
#endif

void init(JNIEnv *env, jobject thiz);

void set_debug_log(JNIEnv *env, jobject thiz, jboolean is_debug);

void set_tnet_config_race_type(JNIEnv *env, jobject thiz, jint race_type);

void set_tnet_config_is_custom_protocol(JNIEnv *env, jobject thiz, jboolean is_custom_protocol);

void set_tnet_config_total_timeout_sec(JNIEnv *env, jobject thiz, jint total_timeout_sec);

void connect(JNIEnv *env, jobject thiz, jint handle_id, jstring host, jstring ip, jint port, jint tcp_port);

void add_header(JNIEnv *env, jobject thiz, jint handle_id, jstring key, jstring value);

void send_request(JNIEnv *env, jobject thiz, jint handle_id, jbyteArray data, jint len, jboolean finish);

void cancel_request(JNIEnv *env, jobject thiz, jint handle_id);

jstring get_state(JNIEnv *env, jobject thiz, jint handle_id);

void destory(JNIEnv *env, jobject thiz);

void clear(JNIEnv *env,
           const jobject jcaller, jint handle_id);

jint regiseter_methods(JNIEnv *env);

class JNIEnvPtr{
private:
    JavaVM *m_vm;
    JNIEnv * m_env;
    bool m_need_detach;
private:
    JNIEnvPtr(const JNIEnvPtr&) = delete;
    JNIEnvPtr& operator=(const JNIEnvPtr&) = delete;
public:
    explicit JNIEnvPtr(JavaVM *m_vm):m_env(nullptr), m_need_detach(false){
        this->m_vm = m_vm;
        if(m_vm->GetEnv((void**) &m_env, JNI_VERSION_1_6) == JNI_EDETACHED){
            m_vm->AttachCurrentThread(&m_env, nullptr);
            m_need_detach = true;
        }
    }

    ~JNIEnvPtr(){
        if(m_need_detach){
            m_vm->DetachCurrentThread();
        }
    }

    JNIEnv* operator->(){
        return m_env;
    }
};

class QUIC_HANDLE_STRUCT{
public:
  jmethodID connect;
  jmethodID dataReceive;
  jmethodID completed;
  jmethodID close;
  JavaVM *m_vm;
};

extern QUIC_HANDLE_STRUCT *quic_handle_struct;
extern bool debug;

#ifdef __cplusplus
}
#endif

#endif //QCLOUDFOUNDATION_COS_COMMON_H
