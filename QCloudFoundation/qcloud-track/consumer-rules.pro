-keep class net.jpountz.lz4.** { *; }

# beacon
-keep class com.tencent**qimei.** { *;}
-keep class com.tencent.qmsp.oaid2.** {*;}
-keep class com.tencent.beacon.** { *;}

# R8
-dontwarn com.tencent.beacon.event.open.BeaconConfig$Builder
-dontwarn com.tencent.beacon.event.open.BeaconConfig
-dontwarn com.tencent.beacon.event.open.BeaconEvent$Builder
-dontwarn com.tencent.beacon.event.open.BeaconEvent
-dontwarn com.tencent.beacon.event.open.BeaconReport
-dontwarn com.tencent.beacon.event.open.EventResult
-dontwarn com.tencent.beacon.event.open.EventType

-dontwarn com.tencent.qimei.sdk.IQimeiSDK
-dontwarn com.tencent.qimei.sdk.QimeiSDK
-dontwarn com.tencent.qimei.strategy.terminal.ITerminalStrategy
-dontwarn com.tencent.qimei.codez.FalconSdk
-dontwarn com.tencent.qimei.codez.IFalconSdk
-dontwarn com.tencent.qimei.codez.shell.UserInfoType
-dontwarn com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension
-dontwarn com.tencent.smtt.sdk.WebSettings
-dontwarn com.tencent.smtt.sdk.WebView
-dontwarn com.tencent.smtt.sdk.WebViewClient

-dontwarn com.tencentcloudapi.cls.android.producer.AsyncProducerClient
-dontwarn com.tencentcloudapi.cls.android.producer.AsyncProducerConfig
-dontwarn com.tencentcloudapi.cls.android.producer.Callback
-dontwarn com.tencentcloudapi.cls.android.producer.common.LogItem
-dontwarn com.tencentcloudapi.cls.android.producer.errors.ProducerException