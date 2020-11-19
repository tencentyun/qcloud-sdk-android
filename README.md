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
