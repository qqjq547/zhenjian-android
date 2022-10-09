package com.watayouxiang.httpclient.model.response;

import java.io.Serializable;
import java.util.List;

public class CustServiceTeamListResp implements Serializable {


    private Boolean firstPage;
    private Boolean lastPage;
    private List<ListBean> list;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPage;
    private Integer totalRow;

    public Boolean getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(Boolean firstPage) {
        this.firstPage = firstPage;
    }

    public Boolean getLastPage() {
        return lastPage;
    }

    public void setLastPage(Boolean lastPage) {
        this.lastPage = lastPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(Integer totalRow) {
        this.totalRow = totalRow;
    }

    public static class ListBean implements Serializable  {
        private int rotateflag;
        private String createtime;
        private int rotatecurrid;
        private String name;
        private int id;
        private String updatetime;
        private int status;

        public int getRotateflag() {
            return rotateflag;
        }

        public void setRotateflag(int rotateflag) {
            this.rotateflag = rotateflag;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getRotatecurrid() {
            return rotatecurrid;
        }

        public void setRotatecurrid(int rotatecurrid) {
            this.rotatecurrid = rotatecurrid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}