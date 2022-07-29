package com.hcsk.financialt.pojo;

public class Organ {
    public long id;//主键
    public String orgname;//机构(比如银行)名称

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
}
