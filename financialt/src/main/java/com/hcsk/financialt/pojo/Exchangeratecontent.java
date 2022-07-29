package com.hcsk.financialt.pojo;

import java.util.Date;

public class Exchangeratecontent {
    public long id;//主键',
    public String content;//获取的报文内容',
    public long orgid;//机构(比如银行)主键
    public Date gettime;//获取时间',
    public int doctype;//1-外汇单网页获取，2-账户外汇接口获取，3-外汇网页获取，4-账户外汇网页获取

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    public Date getGettime() {
        return gettime;
    }

    public void setGettime(Date gettime) {
        this.gettime = gettime;
    }

    public int getDoctype() {
        return doctype;
    }

    public void setDoctype(int doctype) {
        this.doctype = doctype;
    }
}
