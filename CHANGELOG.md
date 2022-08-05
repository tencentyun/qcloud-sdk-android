## v5.9.3
1. 完善分片上传完成接口响应异常处理.
2. 创建桶接口开启MAZ逻辑完善。

## v5.9.2
1. 增加语音识别功能.

## v5.9.1
1. foundation 优化QCloudTask异常抛出逻辑.
2. sdk AndroidManifest中去掉权限声明。
3. 增加关闭灯塔上报的方法setCloseBeacon。
4. 增加region未设置的异常提醒。
5. 修复put tag问题。
6. 修复下载时弱网重试进度丢失问题。
7. 新高级下载range累计bugfix。

## v5.9.0
1. 修复新高级上传完成接口分片排序问题。

## v5.8.9
1. 解决新高级上传cancel ConcurrentModificationException。
2. 启动日志开关。

## v5.8.7
1. 万象审核相关功能。
2. 分离出cos-android-base。

## v5.8.5
1. 升级灯塔版本。
2. 解决土耳其等地区上传签名大小写转换编码问题。
3. 完善xml自动序列化兼容性。

## v5.7.7 v5.7.8
1. 修复 TransferService 加密分片上传和下载。
2. 修复分片上传时未解析万象参数。
3. 将 TransferService 分片上传返回的 CompleteMultipartUploadResult 对象统一转化为 PutObjectResult。
4. 优化签名。

## v5.7.6
1. QUIC sdk 增加网络配置接口。
2. 优化签名。

## v5.7.5
1. 支持 AppendObject、GetMediaInfo、PutBucketReferer
2. 优化 TransferService 接口。
 
## v5.7.4
1. 优化获取上传时文件长度方式；
2. 支持取消传输时的线程动态控制；

## v5.7.2 v5.7.3
1. 增加 SelfSiger 签名方式，用户可以通过直接设置计算好的签名信息来对请求签名。

## v5.7.0、v5.7.1
1. 支持客户端加密。

## v5.6.14
1. 修复重试时没有关闭 okhttp 流，导致报错在 okhttp 3.14 以上版本会报错 cannot make a new request because the previous response is still open: please call response.close()。
2. 更新事件上报 sdk。

## v5.6.13
1. 修复上传时超时计时器线程同步问题

## v5.6.11、5.6.12
1. 修复 complete 计算签名问题

## v5.6.10
1. 在开始和完成上传时，报错 NoSuchUpload 后会校验是否已经上传成功；

## v5.6.9
1. 增加预连接功能；
2. beacon版本兼容；
3. 修复部分网络错误没有 onFailed 回调。

## v5.6.8
1. 修复兼容 okhttp 高版本不兼容问题。

## v5.6.6 v5.6.7
1. 优化上报事件

## V5.6.5
1. 优化上报事件。

## V5.6.4
1. 支持设置上传任务优先级；
2. 支持设置上传任务等待超时；
3. 优化上报事件。

## V5.6.3
1. 优化上传续传文件校验逻辑；
2. 支持返回请求链接的 address。

## V5.6.2
1. 增加分片上传时检查 uploadId 的有效性，如果 uploadId 不存在，或者 uploadId 和需要上传的文件不一致，会重新申请一个新的 uploadId 并重新上传文件；
2. 修复上传时状态监听器 IN_PROGRESS 状态的回调时机；
3. 上传时支持指定智能分层类型；
4. 修复简单上传时可能暂停失败的问题。

## V5.6.1
1. 修复混淆问题；

## V5.6.0
1. beacon增加失败情况下request_id；
2. beacon config更新，关闭部分开关；

## V5.5.9
1. 修复post object请求失败错误；
2. 修复post object传参inputStream偶现失败错误；
3. 修复put object传参URL请求失败错误；
4. 修复listBucketInventory响应xml解析错误；
5. 增加updateObjectMetaData，废弃updateObjectMeta；
6. 修复UploadPartRequest传参inputStream异常；

## V5.5.8
1. 增加灯塔功能；

## V5.5.7
1. 修复 putObject 图片处理结果解析错误；
2. 修复 TransferManager upload 图片处理结果未返回（注意只有简单上传才会返回图片处理结果）；
3. TransferConfig 支持设置 forceSimpleUpload 选项；

## V5.5.6
1. 修复请求的 header、query 的 key、value 都需要 encode；
2. 支持签名放在 url 中；

## V5.5.5

1. 请求 header 中 host 头部默认从 url 中读取，允许自定义。
2. 修复 doesBucketExist() 接口回调问题。

## V5.5.4

1. 请求 header 中 host 头部不再默认从 url 中读取，而是设置为 cos 源站域名，允许自定义。
2. TransferManager 下载时，head 请求携带和 get 请求相同的 header 和 query 参数。
3. TransferManager 第一次下载文件时，如果本地路径下已存在文件，会先删除。
4. TransferManager 下载文件时，下载时 head 请求失败不会导致下载失败，会删除之前的下载文件，然后重新下载整个文件。
5. 修复生成预签名下载链接时，由于签名 header 导致下载链接失效。
6. 修复 quic 签名校验失败的问题和 Content-Lenght 缺失问题。
7. 支持通过 cdn 域名下载文件。

## V5.5.3
1. 重构 host 逻辑，支持用户自定义 host 格式
2. 支持通过 uri 上传
3. 默认签名所有的 header 和 path

## V5.5.2
1. fix TransferManager 上传时动态修改分片大小的 bug.
2. 增加是否使能 DNS 缓存的接口
3. 支持创建多 AZ 存储桶


## V5.5.1

1. 支持图片处理       
2. 支持全球加速上传   
3. 支持用户自定义 Content-Type 和 Host 头部
4. COSXMLUploadTask 增加 pauseSafely() 接口，之前的 pause() 接口在进度 100% 时暂停并恢复时可能会报错。

## V5.5.0

1. 增加 DNS 缓存
2. 优化重试逻辑


## V5.4.32

1. 优化上传逻辑

## V5.4.31

1. 优化上传监控逻辑
2. 修复部分情况下发送错误时，ServiceException 为空

## V5.4.30

1. 统一 cosxml 和 mtaUtils、logUtils 的版本
2. 修复部分请求的 xml 解析错误

## V5.4.29

1. 修复 foundation 内部线程池处理

## V5.4.28

1. 修复暂停上传下载任务后，恢复任务失败问题
2. 优化 TransferManager 线程池处理

## V5.4.25 ~ V5.4.27

1. 修复偶现 NullPointerException 问题
2. 修复签名时间校验多线程问题


## V5.4.24

1. 修复了下载没有回调问题

## V5.4.23

1. 修复 putobject 上传大文件计算MD5 报错
2. 支持[100k, 1M)内分片续传


## V5.4.21 ~ V5.4.22

1. fix bugs

## V5.4.20

1. 增加了Object操作传入key为空的校验；
2. logServer 增加判空处理；
3. 其他一些优化；

## V5.4.19
1. TransferManager 提供了上传字节数组 和 字节流方法
2. SDK 内部针对签名部分进行了再次优化
3. Okhttp 需要使用3.9及以上版本
4. 修复其他一些bug

## V5.4.18
1. 修正了SDK签名算法, 默认签所有参数和头部字段，增加了安全性，如果是采用 jar 包集成的方式，qcloud-foundation jar 也需要同步更新；
2. 废弃了设置签名时间的接口，签名过期时间默认和密钥过期时间一样；
3. 修正了 PostObject 在主线程获取密钥的 bug；
4. 重构了设置域名的接口，增加了多种设置方式；
5. 增加了接口获取请求性能参数；
6. 修复了上传请求不会触发本地超时机制的bug；
7. 修复了其他一些bug；

## V5.4.17

1. CosClientException中增加了errorcode,用于区分CosClientException类型
2. 修复了使用TransferManager上传、下载、复制遇到不能抛出异常错误的bug问题
3. 为了满足Android 版本兼容性，更改了日志显示控件，使用ListView替代RecycleView
4. 更改了mta依赖问题，使用gradle方式替代jar包形式

## V5.4.14 ~ V5.4.16

1. 修复了遇到的bug
2. 增加了查询日志显示功能
3. CosXmlService 支持 QCloudSigner 方式鉴权。

## V5.4.13

1. 添加COSXMLUploadTask 代替 UploadServer
2. 添加COSDownloadTask 代替 Downloader
3. 添加COSXMLCopyTask 代替 CopyServer
4. 引入腾讯 mta检测

## V5.4.13

1. 修复 bug。

## V5.4.12

1. gradle 集成由 aar 包变为 jar 包；
2. 给 CosSimpleService 添加 addVerifiedHost() 接口，不对特定的 host 校验 HTTPS 证书。

## 5.4.11

1. 修复 QCloudTask executeNow() 方法在特殊情况下的偶发 NullPointerExcetion；
2. CosXmlSimpleService 缓存信息路径由 getExternalCacheDir() 变更为 getFilesDir()。

## V5.4.8 ~ 5.4.10

1. 修复特殊情况计算签名的bug。
2. 支持动态加速，包括普通上传和 UploadService 上传；
3. 支持 SSEC 和 SEE-KMS 加密。

## V5.4.7

1. 增加 bolts-tasks 库。

## V5.4.6

1. 修复 UploadService 上传 bug。

## V5.4.5

1. 修复 bug。


## V1.3

缩小了包的体积大小；


## V1.2

1. 所有 Request API, 均只提供了带参数的构造方法；
2. 支持 CAM方式 获取临时密钥，具体请查看 `com.tencent.qcloud.core.network.auth.LocalSessionCredentialProvider`
