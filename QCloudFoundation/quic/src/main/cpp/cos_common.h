//
// Created by bradyxiao on 2019/3/12.
//

#ifndef QCLOUDFOUNDATION_COS_COMMON_H
#define QCLOUDFOUNDATION_COS_COMMON_H

#include <android/log.h>
#include <jni.h>
#include <stddef.h>
#include <map>
#include <pthread.h>
#include<iostream>
using namespace std;

#define TAG "COSQUIC"
#define LOGI(debug, ...) {if(debug)__android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__);}
#define LOGD(debug, ...) {if(debug)__android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__);}
#define LOGE(debug, ...) {if(debug)__android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__);}

#ifdef  __cplusplus
extern "C"{
#endif

void init(JNIEnv *env, jobject thiz);

void set_debug_log(JNIEnv *env, jobject thiz, jboolean is_debug);

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
    JNIEnvPtr(JavaVM *m_vm):m_env(nullptr), m_need_detach(false){
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
