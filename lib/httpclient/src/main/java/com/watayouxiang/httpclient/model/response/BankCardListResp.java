package com.watayouxiang.httpclient.model.response;

import java.io.Serializable;
import java.util.List;

public class BankCardListResp  implements Serializable {


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
        private String createtime;
        private String backcolor;
        private Integer id;
        private String bankname;
        private String banklogo;
        private String updatetime;
        private String bankcode;
        private String bankwatermark;

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getBackcolor() {
            return backcolor;
        }

        public void setBackcolor(String backcolor) {
            this.backcolor = backcolor;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getBankname() {
            return bankname;
        }

        public void setBankname(String bankname) {
            this.bankname = bankname;
        }

        public String getBanklogo() {
            return banklogo;
        }

        public void setBanklogo(String banklogo) {
            this.banklogo = banklogo;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getBankcode() {
            return bankcode;
        }

        public void setBankcode(String bankcode) {
            this.bankcode = bankcode;
        }

        public String getBankwatermark() {
            return bankwatermark;
        }

        public void setBankwatermark(String bankwatermark) {
            this.bankwatermark = bankwatermark;
        }
    }
}