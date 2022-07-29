package com.hcsk.financialt.pojo;

import java.util.Date;
import java.util.List;

//工行账户外汇请求回应返回的信息
public class Icbczhwhrespmsg {
    public int refreshInterval;//刷新秒数
    public String TranErrorCode;//错误信息
    public String sysdate;//系统时间
    public String TranErrorDisplayMsg;//错误显示信息
    public List<Icbczhwhresprf> rf;//汇率信息

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public String getTranErrorCode() {
        return TranErrorCode;
    }

    public void setTranErrorCode(String tranErrorCode) {
        TranErrorCode = tranErrorCode;
    }

    public String getSysdate() {
        return sysdate;
    }

    public void setSysdate(String sysdate) {
        this.sysdate = sysdate;
    }

    public String getTranErrorDisplayMsg() {
        return TranErrorDisplayMsg;
    }

    public void setTranErrorDisplayMsg(String tranErrorDisplayMsg) {
        TranErrorDisplayMsg = tranErrorDisplayMsg;
    }

    public List<Icbczhwhresprf> getRf() {
        return rf;
    }

    public void setRf(List<Icbczhwhresprf> rf) {
        this.rf = rf;
    }
}
