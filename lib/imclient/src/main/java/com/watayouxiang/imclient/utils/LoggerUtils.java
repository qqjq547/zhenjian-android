package com.watayouxiang.imclient.utils;

import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.watayouxiang.imclient.prefernces.IMPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LoggerUtils {
    private static final String LOG_TAG = "TioIMClient";
    private static final int LOG_MAX_LENGTH = 2 * 1024;
    private static final String SEPARATOR = System.getProperty("line.separator");

    private static boolean SHOW_THREAD_INFO = false;
    private static boolean DEBUG = true;
    private static Boolean JSON_FORMAT = null;

    public enum Priority {
        VERBOSE(2),
        DEBUG(3),
        INFO(4),
        WARN(5),
        ERROR(6),
        ASSERT(7);
        int value;

        Priority(int value) {
            this.value = value;
        }
    }

    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }

    public static void setShowThreadInfo(boolean showThreadInfo) {
        SHOW_THREAD_INFO = showThreadInfo;
    }

    public static void setJsonFormat(boolean jsonFormat) {
        IMPreferences.saveJsonFormatLog(jsonFormat);
        AppUtils.relaunchApp(true);
    }

    public static boolean isJsonFormat() {
        if (JSON_FORMAT == null) {
            JSON_FORMAT = IMPreferences.getJsonFormatLog();
        }
        return JSON_FORMAT;
    }

    public static void e(String msg) {
        log(Priority.ERROR, msg);
    }

    public static void e(String tag, String msg) {
        log(Priority.ERROR, tag, msg);
    }

    public static void i(String msg) {
        log(Priority.INFO, msg);
    }

    public static void i(String tag, String msg) {
        log(Priority.INFO, tag, msg);
    }

    public static void d(String msg) {
        log(Priority.DEBUG, msg);
    }

    public static void d(String tag, String msg) {
        log(Priority.DEBUG, tag, msg);
    }

    public static void log(Priority priority, String msg) {
        log(priority, LOG_TAG, msg);
    }

    public static void log(Priority priority, String tag, String msg) {
        if (!DEBUG) return;
        if (priority == null) return;
        if (tag == null) return;
        if (msg == null) return;

        if (SHOW_THREAD_INFO) {
            msg = String.format(Locale.getDefault(), "%s || %s", Thread.currentThread().toString(), msg);
        }

        int msgLen = msg.length();
        // 一次性显示
        if (msgLen <= LOG_MAX_LENGTH) {
            Log.println(priority.value, tag, msg);
            return;
        }
        // 分段显示
        for (int i = 0; i < msgLen; i += LOG_MAX_LENGTH) {
            String substring;
            if (i + LOG_MAX_LENGTH < msgLen) {
                substring = msg.substring(i, i + LOG_MAX_LENGTH);
            } else {
                substring = msg.substring(i);
            }
            if (i == 0) {
                Log.println(priority.value, tag, substring);
            } else {
                Log.println(priority.value, tag, "\t\t" + substring);
            }
        }
    }

    public static void d_json(String json, int indent) {
        if (!DEBUG) return;

        String indentStr = "";
        if (indent > 0 && indent <= 10) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < indent; i++) {
                builder.append("\t");
            }
            indentStr = builder.toString();
        }

        String _json = null;

        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                // 返回格式化的json字符串，其中的数字4是缩进字符数
                _json = jsonObject.toString(4);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                _json = jsonArray.toString(4);
            }
        } catch (JSONException ignored) {
        }

        // json 打印
        if (_json != null && SEPARATOR != null) {
            String[] lines = _json.split(SEPARATOR);

            for (int i = 0, length = lines.length; i < length; i++) {
                String line = lines[i];

                if (i == 0) {
                    // 第一行
                    Log.println(Priority.DEBUG.value, LOG_TAG, indentStr + "--------------------------------------------------");
                    Log.println(Priority.DEBUG.value, LOG_TAG, indentStr + "| " + line);
                } else if (i == length - 1) {
                    // 最后一行
                    Log.println(Priority.DEBUG.value, LOG_TAG, indentStr + "| " + line);
                    Log.println(Priority.DEBUG.value, LOG_TAG, indentStr + "--------------------------------------------------");
                } else {
                    // 中间行
                    Log.println(Priority.DEBUG.value, LOG_TAG, indentStr + "| " + line);
                }
            }
        }
    }
}
