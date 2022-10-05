package com.watayouxiang.httpclient.model.response;

import java.io.Serializable;
import java.util.ArrayList;

public class EmojiAllResp extends ArrayList<EmojiAllResp.Data> {
    public static class Data implements Serializable {

        public String coverurl;
        public Integer uid;
        public Integer imgid;
        public String url;
        public String name;
        public String descinfo;
    }
}
