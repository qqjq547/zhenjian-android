package com.tiocloud.chat.feature.session.common.model;

import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.tiocloud.chat.util.AESEncrypt;
import com.watayouxiang.imclient.utils.MD5Utils;

import org.json.JSONObject;

import java.io.Serializable;

public class TextContent implements Serializable {
    private String data;
    private String fingerprint;

    public TextContent(String data, String fingerprint) {
        this.data = data;
        this.fingerprint = fingerprint;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
    public static String toJson(String c){
        try {
        String fingerprint = MD5Utils.getMd5(c);
        String data= AESEncrypt.encrypt(c,fingerprint);
        return GsonUtils.toJson(new TextContent(data,fingerprint));
        }catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }
    public static String fromJson(String jsonString){
        try {
            TextContent textContent=GsonUtils.fromJson(jsonString,TextContent.class);
            return AESEncrypt.decrypt(textContent.getData(),textContent.getFingerprint());
        }catch (Exception e){
            e.printStackTrace();
            Log.e("hjq",GsonUtils.toJson(jsonString));
        }
        return jsonString;
    }
}
