package com.hcsk.financialt.pojo;

public class Resmsg {
    public String resultcode="301";//成功为101，失败为301
    public String resultmsg="请求失败";

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
}
