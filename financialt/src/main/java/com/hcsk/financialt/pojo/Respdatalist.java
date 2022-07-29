package com.hcsk.financialt.pojo;

import java.util.List;

public class Respdatalist {
    public String resultcode="301";//成功为101，失败为301
    public String resultmsg="请求失败";
    public long totalcount;//记录数
    public List<?> rows;//泛型记录集

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getResultmsg() {
        return resultmsg;
    }

    public void setResultmsg(String resultmsg) {
        this.resultmsg = resultmsg;
    }

    public long getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(long totalcount) {
        this.totalcount = totalcount;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
