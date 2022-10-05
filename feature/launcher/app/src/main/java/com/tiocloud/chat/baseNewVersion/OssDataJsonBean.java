package com.tiocloud.chat.baseNewVersion;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OssDataJsonBean {


    @SerializedName("API_URLS")
    private List<String> aPI_URLS;
    @SerializedName("RES_URLS")
    private List<String> rES_URLS;
     public List<String> getAPI_URLS() {
        return aPI_URLS;
    }

    public void setAPI_URLS(List<String> aPI_URLS) {
        this.aPI_URLS = aPI_URLS;
    }

    public List<String> getRES_URLS() {
        return rES_URLS;
    }

    public void setRES_URLS(List<String> rES_URLS) {
        this.rES_URLS = rES_URLS;
    }

  
}