package com.tiocloud.chat.baseNewVersion.utils2;

import android.os.Handler;
import android.widget.Toast;


import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;


public class ToastshowUtils {
    private static Toast toast = null;
    private static String oldMsg;
    private static long oneTime = 0;
    private static long twoTime = 0;
    /** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
    public static void showToastSafe(String text) {
        if (toast != null) {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        } else {
            toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);

        }
        toast.show();

//        if (toast == null) {
//            toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
//            toast.show();
//            oneTime = System.currentTimeMillis();
//        } else {
//            twoTime = System.currentTimeMillis();
//            if (text.equals(oldMsg)) {
//                if (twoTime - oneTime > 0) {
//                    // 这里是判断toast上一次显示的时间和这次的显示时间如果大于2000，
//                    //  则显示新的toast
//                    toast.cancel();
//                    toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
//                    toast.show();
//                    oneTime = twoTime;
//                }
//            } else {
//                toast.cancel();
//                toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
//                oldMsg = text;
//                toast.show();
//                oneTime = twoTime;
//            }
//        }
    }


}