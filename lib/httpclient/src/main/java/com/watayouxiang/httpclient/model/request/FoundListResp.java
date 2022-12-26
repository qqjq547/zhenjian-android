package com.watayouxiang.httpclient.model.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FoundListResp extends LinkedList<FoundListResp.Found> implements Serializable {


    public static class Found implements Serializable {
        /**
         *       "createperson": 2,
         *       "createtime": "2021-10-22 22:28:34",
         *       "icon": "/user/base/avatar/2021101586/2139101449006938598809600.png",
         *       "iconbig":"",
         *       "id": 2,
         *       "itemname": "案说法",
         *       "linkedaddress": "案说法啊",
         *       "status": 1,
         *       "updateperson": 2,
         *       "updatetime": "2021-10-22 22:28:34"
         */
        public int createperson;
        public String createtime;
        public String icon;
        public String iconbig;
        public int id;
        public String itemname;
        public String linkedaddress;
        public int status;
        public int updateperson;
        public String updatetime;
        public String selecticon;
        public int type;
        public int pid;
        public List<Found> items=new ArrayList<>();
    }
}
