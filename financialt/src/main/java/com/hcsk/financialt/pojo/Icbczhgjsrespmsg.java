package com.hcsk.financialt.pojo;

import java.util.List;

public class Icbczhgjsrespmsg {
    public String acGoldAllGoldTyle;//贵金属代号，":"@130060000043@130060000044@130060000041@130060000123@130060000045@130060000046@130060000042@130060000125@",
    public List<Icbczhgjsrespmarket> market;//账户贵金属价格列表，[
    public String sysdate;//发布日期，2020-12-03 11:07:19",
    public String TranErrorCode;//":"0",
    public int refreshInterval;//":"60000",
    public String TranErrorDisplayMsg;//":""

    public String getAcGoldAllGoldTyle() {
        return acGoldAllGoldTyle;
    }

    public void setAcGoldAllGoldTyle(String acGoldAllGoldTyle) {
        this.acGoldAllGoldTyle = acGoldAllGoldTyle;
    }

    public List<Icbczhgjsrespmarket> getMarket() {
        return market;
    }

    public void setMarket(List<Icbczhgjsrespmarket> market) {
        this.market = market;
    }

    public String getSysdate() {
        return sysdate;
    }

    public void setSysdate(String sysdate) {
        this.sysdate = sysdate;
    }

    public String getTranErrorCode() {
        return TranErrorCode;
    }

    public void setTranErrorCode(String tranErrorCode) {
        TranErrorCode = tranErrorCode;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public String getTranErrorDisplayMsg() {
        return TranErrorDisplayMsg;
    }

    public void setTranErrorDisplayMsg(String tranErrorDisplayMsg) {
        TranErrorDisplayMsg = tranErrorDisplayMsg;
    }
}
