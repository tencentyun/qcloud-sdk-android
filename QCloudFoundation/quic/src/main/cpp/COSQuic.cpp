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

#include <inttypes.h>
#include "COSQuic.h"

COSQuic::COSQuic(JNIEnv *env, jobject jcaller, jint handle_id, const TnetConfig& tnetConfig){

    this->m_caller = reinterpret_cast<jobject>(env->NewGlobalRef(jcaller));
    this->m_handle_id = handle_id;
//    TnetConfig tnetConfig;
//    tnetConfig.upload_optimize_ = true;
//    tnetConfig.congestion_type_ = kBBR;
//    m_tnetQuic = new TnetQuicRequest(this, tnetConfig);
    LOGI(debug, "[%d] tnetConfig", tnetConfig.race_type);
    LOGI(debug, "[%d] tnetConfig", tnetConfig.is_custom_);
    m_tnetQuic = new TnetQuicRequest(this, tnetConfig);
}

COSQuic::~COSQuic() {
    if(this->m_caller != NULL){
        JNIEnvPtr envPtr(quic_handle_struct->m_vm);
        envPtr->DeleteGlobalRef(this->m_caller);
    }
    if(m_tnetQuic != NULL) delete m_tnetQuic;

    //LOGI(debug, "[%d] release", this->m_handle_id);
}


jboolean COSQuic::Connect(JNIEnv *env, const jobject jcaller, const jstring host, const jstring ip,
                         const jint port, const jint tcp_port) {
    const char* c_host = env->GetStringUTFChars(host, NULL);
    const char* c_ip = env->GetStringUTFChars(ip, NULL);
    this->m_tnetQuic->Connect(c_host, c_ip, port, tcp_port);
    // LOGD(debug, "[%d]connect:%s(%s: %d) %s", this->m_handle_id, c_host, c_ip, port, "starting");
    env->ReleaseStringUTFChars(host, c_host);
    env->ReleaseStringUTFChars(ip, c_ip);
    return JNI_TRUE;
}

void COSQuic::AddHeaders(JNIEnv *env, const jobject jcaller, const jstring key,
                            const jstring value) {
    const char* c_key = env->GetStringUTFChars(key, NULL);
    const char* c_value = env->GetStringUTFChars(value, NULL);
    this->m_tnetQuic->AddHeaders(c_key, c_value);
    // LOGD(debug, "[%d]add header(%s:%s)", this->m_handle_id, c_key, c_value);
    env->ReleaseStringUTFChars(key, c_key);
    env->ReleaseStringUTFChars(value, c_value);
}

jboolean COSQuic::SendRequest(JNIEnv *env, const jobject jcaller, const jbyteArray data, jint len, jboolean fin) {
    int size = len;
    jbyte *bytes = env->GetByteArrayElements(data, 0);
    if(bytes == NULL) return JNI_FALSE;
    char *c_data = new char[size];
    memset(c_data,0,size);
    memcpy(c_data, bytes, size);
    //c_data[size] = 0;
    env->ReleaseByteArrayElements(data, bytes, 0);
    bool is_over = fin == JNI_TRUE? true: false;
    this->m_tnetQuic->SendRequest(c_data, size, is_over);
    delete[] c_data;
    // LOGD(debug, "[%d]send data(%d) %s (body is finish %s)", this->m_handle_id, size, "starting", is_over? "yes" :"no");
    return JNI_TRUE;
}

void COSQuic::CancelRequest(JNIEnv *env, const jobject jcaller) {
    this->m_tnetQuic->CancelRequest();
    //LOGD(debug, "[%d] cancel request", this->m_handle_id);
}

jstring COSQuic::GetState(JNIEnv *env, const jobject jcaller) {
    TnetStats tnetStats = this->m_tnetQuic->GetTnetStates();
    char *buf = new char[1024];
    sprintf(buf, "is_valid=%s,is_quic=%s,is_0rtt=%s,connect_ms=%" PRIu64 ",ttfb_ms=%" PRIu64 ",complete_ms=%" PRIu64 ",srtt_us=%" PRIu64 ",packets_sent=%" PRIu64 ",packets_retransmitted=%" PRIu64 ",bytes_sent=%" PRIu64 ",bytes_retransmitted=%" PRIu64 ",packets_lost=%" PRIu64 ",packets_received=%" PRIu64 ",bytes_received=%" PRIu64 ",stream_bytes_received=%" PRIu64 "",
            (tnetStats.is_valid ? "true" : "false"),
            (tnetStats.is_quic ? "true" : "false"),
            (tnetStats.is_0rtt ? "true" : "false"),
            tnetStats.connect_ms,
            tnetStats.ttfb_ms,
            tnetStats.complete_ms,
            tnetStats.srtt_us,
            tnetStats.packets_sent,
            tnetStats.packets_retransmitted,
            tnetStats.bytes_sent,
            tnetStats.bytes_retransmitted,
            tnetStats.packets_lost,
            tnetStats.packets_received,
            tnetStats.bytes_received,
            tnetStats.stream_bytes_received);
    //LOGD(debug, "[%d] state %s", this->m_handle_id, buf);
    jstring encoding = env->NewStringUTF("GB2312");
    jclass str_class = env->FindClass("java/lang/String");
    jmethodID str_initID = env->GetMethodID(str_class, "<init>", "([BLjava/lang/String;)V"); //
    int size = strlen(buf); //不包含NULL
    jbyteArray bytes = env->NewByteArray(size);
    env->SetByteArrayRegion(bytes, 0, size, (jbyte*)buf);
    jstring result = reinterpret_cast<jstring>(env->NewObject(str_class, str_initID, bytes, encoding)); // 转化为 String
    delete[] buf;
    return result;
}

void COSQuic::OnConnect(int error_code) {
    //LOGI(debug, "[%d] connect %s(%d)", this->m_handle_id, error_code == 0? "success" : "failed", error_code);
    JNIEnvPtr envPtr(quic_handle_struct->m_vm);
    envPtr->CallVoidMethod(this->m_caller, quic_handle_struct->connect, error_code);
}

void COSQuic::OnDataRecv(const char *buf, const int buf_len) {
    //LOGD(debug, "[%d] receive response : (len=%d)", this->m_handle_id, buf_len);
    JNIEnvPtr envPtr(quic_handle_struct->m_vm);
    jbyteArray bytes = envPtr->NewByteArray(buf_len);
    envPtr->SetByteArrayRegion(bytes, 0, buf_len, (jbyte*)buf);
    envPtr->CallVoidMethod(this->m_caller, quic_handle_struct->dataReceive, bytes, buf_len);
}

void COSQuic::OnRequestFinish(int stream_error) {
    //LOGI(debug, "[%d] request completed", this->m_handle_id);
    JNIEnvPtr envPtr(quic_handle_struct->m_vm);
    envPtr->CallVoidMethod(this->m_caller, quic_handle_struct->completed, stream_error);
}

void COSQuic::OnConnectionClose(int error_code, const char *error_detail) {
    //LOGE(debug, "[%d] connection close (%d, %s)", this->m_handle_id, error_code, error_detail);
    JNIEnvPtr envPtr(quic_handle_struct->m_vm);
    jstring encoding = envPtr->NewStringUTF("GB2312");
    jclass str_class = envPtr->FindClass("java/lang/String");
    jmethodID str_initID = envPtr->GetMethodID(str_class, "<init>", "([BLjava/lang/String;)V");
    int size = strlen(error_detail); //不包含NULL
    jbyteArray bytes = envPtr->NewByteArray(size);
    envPtr->SetByteArrayRegion(bytes, 0, size, (jbyte*)error_detail);
    envPtr->CallVoidMethod(this->m_caller, quic_handle_struct->close,
            error_code, envPtr->NewObject(str_class, str_initID, bytes, encoding));
}