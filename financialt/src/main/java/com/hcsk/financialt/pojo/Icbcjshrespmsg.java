package com.hcsk.financialt.pojo;

import java.util.List;

//工行结售汇请求回应返回的信息
public class Icbcjshrespmsg {
    public String TranErrorCode;//错误显示代码": "",
    public List<Icbcjshresprf> rf;//汇率信息": [
    public String TranErrorDisplayMsg;//错误显示信息": ""

    public String getTranErrorCode() {
        return TranErrorCode;
    }

    public void setTranErrorCode(String tranErrorCode) {
        TranErrorCode = tranErrorCode;
    }

    public List<Icbcjshresprf> getRf() {
        return rf;
    }

    public void setRf(List<Icbcjshresprf> rf) {
        this.rf = rf;
    }

    public String getTranErrorDisplayMsg() {
        return TranErrorDisplayMsg;
    }

    public void setTranErrorDisplayMsg(String tranErrorDisplayMsg) {
        TranErrorDisplayMsg = tranErrorDisplayMsg;
    }
}
