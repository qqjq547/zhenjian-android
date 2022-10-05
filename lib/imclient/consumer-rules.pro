##### https://github.com/google/gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

##### https://github.com/greenrobot/EventBus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

##### imclient
-keep class com.watayouxiang.imclient.model.** { <fields>; }

##### https://github.com/Blankj/AndroidUtilCode
-dontwarn com.blankj.utilcode.**

-keepclassmembers class * {
    @com.blankj.utilcode.util.BusUtils$Bus <methods>;
}

-keep public class * extends com.blankj.utilcode.util.ApiUtils$BaseApi
-keep,allowobfuscation @interface com.blankj.utilcode.util.ApiUtils$Api
-keep @com.blankj.utilcode.util.ApiUtils$Api class *

-keepattributes *Annotation*
