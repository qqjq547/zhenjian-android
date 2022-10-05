#qq
-keep class com.tencent.**{*;}
#微博
-keep class com.sina.weibo.sdk.** { *; }
#微信
-keep class com.tencent.mm.opensdk.** { *; }
-keep class packageName.wxapi.** { *; }
#Gson
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
#SocialLib
-keep class com.watayouxiang.social.**{*;}