# qcloud-sdk-android

[![GitHub release](https://img.shields.io/github/release/tencentyun/qcloud-sdk-android.svg)](https://github.com/tencentyun/qcloud-sdk-android/releases/latest)
[![Travis CI](https://travis-ci.org/tencentyun/qcloud-sdk-android.svg?branch=master)](https://travis-ci.org/tencentyun/qcloud-sdk-android)

腾讯云 Android SDK 发布仓库

## 产品列表

当前仓库内提供的 SDK 有:

* QCloudCosXml - [腾讯云COS XML](https://cloud.tencent.com/document/product/436)

## 示例工程

我们提供了完整的示例工程，您可以下载体验，也可以参考代码的编写。如果想要查看，可以点击 [这里](https://github.com/tencentyun/qcloud-sdk-android-samples) 。

## 添加 SDK

Android 平台上，我们提供 gradle 远程依赖或者 jar 包两种主流的集成方式。

### 通过 gradle 远程依赖集成

如果您使用 Android Studio 作为开发工具或者使用 gradle 编译系统，**我们推荐您使用此方式集成依赖。**

#### 1. 使用 jcenter 作为仓库来源

在工程根目录下的 build.gradle 使用 jcenter 作为远程仓库：

```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        ...
    }
}

allprojects {
    repositories {
        jcenter()
    }
}
```

#### 2. 添加 SDK 库依赖

在您的应用级 build.gradle（通常是 app/build.gradle）添加您需要产品的 SDK 的依赖：

```
dependencies {
    //增加这行
    compile 'com.tencent.qcloud:cosxml:5.5.+'
}
```

然后，点击您 IDE 的 【gradle】 同步按钮，会自动将依赖包同步到本地。

### 各个产品 SDK 的 gradle 依赖

##### COS XML SDK

```
compile 'com.tencent.qcloud:cosxml:5.5.+'
```

### 手动集成

如果您无法采用远程依赖的方式，可以通过下载 [最新版本](https://github.com/tencentyun/qcloud-sdk-android/releases) 的压缩包，解压后，手动集成到您的工程中。

## 兼容 CDN 域名下载

1. 初始化支持 cdn 下载的 TransferManager

```
/**
* 通过 cdn 域名下载，需要 1. 开启 cdn 回源鉴权；2. 开启 cdn 鉴权。详情请参考：
* https://cloud.tencent.com/document/product/436/18669
*
*
* </p>
* 注意，这样创建的 TransferManager 只能用于 cdn 下载，不能用于上传，或者通过源站域名下载
*/
public static TransferManager newCdnTransferManager() {

   /**
    * 假设您的 bucket 为 examplebucket-1250000000，地域为 ap-beijing
    */
   CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
           .isHttps(true)
           .setRegion("ap-beijing")
           .setDebuggable(false)
           .setHostFormat("${bucket}.file.myqcloud.com") // cdn 默认域名 host 格式
           .addHeader("Host", "examplebucket-1250000000.file.myqcloud.com") // 修改 header 中的 host 字段
           .builder();

   /**
    * 通过 cdn 下载，并开启回源鉴权后，
    */
   CosXmlService cosXmlService = new CosXmlService(getContext(), cosXmlServiceConfig);

   TransferConfig transferConfig = new TransferConfig.Builder().build();
   return new TransferManager(cosXmlService, transferConfig);
}
```

2. 如果您开启了 cdn 鉴权，那么需要在 url 上添加 cdn 签名，cdn 鉴权请参考[这里](https://cloud.tencent.com/document/product/228/41622)。

```
String bucket = "examplebucket-1250000000";
String cosPath = "exampleobject";
String srcPath = new File(context.getCacheDir(), "exampleobject")
        .toString(); //本地文件的绝对路径
String cdnSign = "1595307148-ktug8jzwijjs5khj-0-953d8ac2a84af18e"; // cdn 鉴权参数，这里已 typeA 为例

GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, cosPath, srcPath);

Map<String, String> paras = new HashMap<>();
paras.put("sign", cdnSign);
getObjectRequest.setQueryParameters(paras);

COSXMLDownloadTask downloadTask = transferManager.download(context, getObjectRequest);

downloadTask.setCosXmlResultListener(new CosXmlResultListener() {
    @Override
    public void onSuccess(CosXmlRequest request, CosXmlResult result) {}

    @Override
    public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {}

});
```

## 开发文档

如果您想要查看每个 SDK 是如何调用的，请参考各个产品的开发文档：

* [COS XML SDK](https://cloud.tencent.com/document/product/436/12159)

## License

腾讯云 Android SDK 通过 `MIT ` License 发布。

```
Copyright (c) 2017 腾讯云

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
