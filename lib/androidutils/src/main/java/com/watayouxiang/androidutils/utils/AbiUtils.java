package com.watayouxiang.androidutils.utils;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/06
 *     desc   :
 * </pre>
 */
public class AbiUtils {
    public static String getCpuAbi() {
        return android.os.Build.CPU_ABI;
    }
}
