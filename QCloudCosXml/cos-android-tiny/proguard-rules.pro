# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/rickenwang/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.tencent.cos.xml.*
-keep class com.tencent.cos.xml.** {*;}

-keep class com.tencent.qcloud.core.auth.SessionQCloudCredentials
-keepclassmembers class com.tencent.qcloud.core.auth.SessionQCloudCredentials {
 public <init>(java.lang.String, java.lang.String, java.lang.String, long);
 public <init>(java.lang.String, java.lang.String, java.lang.String, long, long);
}


-keep class com.tencent.qcloud.core.auth.QCloudLifecycleCredentials

-keep class com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider
-keepclassmembers class com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider {
 protected com.tencent.qcloud.core.auth.QCloudLifecycleCredentials fetchNewCredentials();

}

-keep class com.tencent.qcloud.core.auth.QCloudCredentialProvider
-keep class com.tencent.qcloud.core.auth.QCloudCredentialProvider {*;}

-keep class com.tencent.qcloud.core.auth.ShortTimeCredentialProvider
-keepclassmembers class com.tencent.qcloud.core.auth.ShortTimeCredentialProvider {
 public <init>(java.lang.String, java.lang.String, long);
}

-keeppackagenames com.tencent.qcloud.core.auth
-keeppackagenames com.tencent.qcloud.core.common
-keeppackagenames com.tencent.qcloud.core.http
-keeppackagenames com.tencent.qcloud.core.task
-keeppackagenames com.tencent.qcloud.core.util
-keeppackagenames com.tencent.qcloud.qcloudxml.core
-keeppackagenames com.tencent.bolts