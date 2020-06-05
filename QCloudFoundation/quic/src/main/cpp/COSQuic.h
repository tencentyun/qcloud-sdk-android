//
// Created by bradyxiao on 2019/3/12.
//

#ifndef QCLOUDFOUNDATION_COSQUIC_H
#define QCLOUDFOUNDATION_COSQUIC_H

#include <jni.h>
#include "tnet_quic_request.h"
#include "cos_common.h"
#include <string.h>
class COSQuic : public TnetRequestDelegate{

private:
    TnetQuicRequest* m_tnetQuic;
    jobject m_caller;
    int32_t m_handle_id;

public:

    COSQuic(JNIEnv *env, jobject jcaller, jint handle_id);
    ~COSQuic() override;
    jboolean Connect(JNIEnv *env,
                 const jobject jcaller,
                 const jstring host,
                 const jstring ip,
                 const jint port,
                 const jint tcp_port);
    // Add the headers you want in header_map with key-value format.
    void AddHeaders(JNIEnv *env,
                    const jobject jcaller,
                    const jstring key,
                    const jstring value);

    // Sends an HTTP request and does not wait for response before returning.
    jboolean SendRequest(JNIEnv *env,
                     const jobject jcaller,
                     const jbyteArray data,
                     const jint len,
                     jboolean finish);

    void CancelRequest(JNIEnv *env,
                       const jobject jcaller);

    jstring GetState(JNIEnv *env,
                      const jobject jcaller);

    // 连接成功
    void OnConnect(int error_code) override;

    // 收到数据并且输出
    void OnDataRecv(const char* buf,
                    const int buf_len) override;

    // 连接关闭回调
    void OnConnectionClose(int error_code, const char* error_detail) override;

    // 单次请求已完成的回调
    void OnRequestFinish(int stream_error) override;

};

#endif //QCLOUDFOUNDATION_COSQUIC_H
