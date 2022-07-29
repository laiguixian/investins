package com.hcsk.financialt.pojo;
//工行结售汇返回的信息里的rf列表
public class Icbcjshresprf {
    public float rmbmdrat;//656.17",
    public float standBuyRate;//656.17",
    public String rmbType;//": "",
    public float sellcPrice;//现钞卖出价，": "657.61",
    public float rmbebrat;//现汇买入价，": "654.86",
    public float sellePrice;//现汇卖出价，": "657.61",
    public float rmbcbrat;//现钞买入价，": "649.61",
    public String proId;//": "130060000016",
    public String currType;//": "014",
    public String currTypeName;//": "美元",
    public String rmbTypeName;//": "",
    public float standSellRate;//": "656.17"

    public float getRmbmdrat() {
        return rmbmdrat;
    }

    public void setRmbmdrat(float rmbmdrat) {
        this.rmbmdrat = rmbmdrat;
    }

    public float getStandBuyRate() {
        return standBuyRate;
    }

    public void setStandBuyRate(float standBuyRate) {
        this.standBuyRate = standBuyRate;
    }

    public String getRmbType() {
        return rmbType;
    }

    public void setRmbType(String rmbType) {
        this.rmbType = rmbType;
    }

    public float getSellcPrice() {
        return sellcPrice;
    }

    public void setSellcPrice(float sellcPrice) {
        this.sellcPrice = sellcPrice;
    }

    public float getRmbebrat() {
        return rmbebrat;
    }

    public void setRmbebrat(float rmbebrat) {
        this.rmbebrat = rmbebrat;
    }

    public float getSellePrice() {
        return sellePrice;
    }

    public void setSellePrice(float sellePrice) {
        this.sellePrice = sellePrice;
    }

    public float getRmbcbrat() {
        return rmbcbrat;
    }

    public void setRmbcbrat(float rmbcbrat) {
        this.rmbcbrat = rmbcbrat;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getCurrType() {
        return currType;
    }

    public void setCurrType(String currType) {
        this.currType = currType;
    }

    public String getCurrTypeName() {
        return currTypeName;
    }

    public void setCurrTypeName(String currTypeName) {
        this.currTypeName = currTypeName;
    }

    public String getRmbTypeName() {
        return rmbTypeName;
    }

    public void setRmbTypeName(String rmbTypeName) {
        this.rmbTypeName = rmbTypeName;
    }

    public float getStandSellRate() {
        return standSellRate;
    }

    public void setStandSellRate(float standSellRate) {
        this.standSellRate = standSellRate;
    }
}
