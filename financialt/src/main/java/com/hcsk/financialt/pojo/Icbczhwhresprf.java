package com.hcsk.financialt.pojo;
//工行账户外汇返回的信息里的rf列表
public class Icbczhwhresprf {
    public String updown_d;//排序
    public float buyRate;//银行买入价
    public float highPrice;//最高价
    public String proID;//币种id
    public String quoteTime;//发布时间
    public String quoteDate;//发布日期
    public String currType;
    public float middPrice;//中间价
    public String openprice_dr;//当日涨跌幅
    public float openprice_dv;//当日涨跌值
    public float sellRate;//银行卖出价
    public float lowPrice;//最低价
    public String updown_y;
    public String FIELD1;
    public String FIELD2;
    public String proName;//币种
    public String openprice_yr;//年度涨跌幅
    public String sswr;
    public String isCollected;

    public String getUpdown_d() {
        return updown_d;
    }

    public void setUpdown_d(String updown_d) {
        this.updown_d = updown_d;
    }

    public float getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(float buyRate) {
        this.buyRate = buyRate;
    }

    public float getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(float highPrice) {
        this.highPrice = highPrice;
    }

    public String getProID() {
        return proID;
    }

    public void setProID(String proID) {
        this.proID = proID;
    }

    public String getQuoteTime() {
        return quoteTime;
    }

    public void setQuoteTime(String quoteTime) {
        this.quoteTime = quoteTime;
    }

    public String getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(String quoteDate) {
        this.quoteDate = quoteDate;
    }

    public String getCurrType() {
        return currType;
    }

    public void setCurrType(String currType) {
        this.currType = currType;
    }

    public float getMiddPrice() {
        return middPrice;
    }

    public void setMiddPrice(float middPrice) {
        this.middPrice = middPrice;
    }

    public String getOpenprice_dr() {
        return openprice_dr;
    }

    public void setOpenprice_dr(String openprice_dr) {
        this.openprice_dr = openprice_dr;
    }

    public float getOpenprice_dv() {
        return openprice_dv;
    }

    public void setOpenprice_dv(float openprice_dv) {
        this.openprice_dv = openprice_dv;
    }

    public float getSellRate() {
        return sellRate;
    }

    public void setSellRate(float sellRate) {
        this.sellRate = sellRate;
    }

    public float getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(float lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getUpdown_y() {
        return updown_y;
    }

    public void setUpdown_y(String updown_y) {
        this.updown_y = updown_y;
    }

    public String getFIELD1() {
        return FIELD1;
    }

    public void setFIELD1(String FIELD1) {
        this.FIELD1 = FIELD1;
    }

    public String getFIELD2() {
        return FIELD2;
    }

    public void setFIELD2(String FIELD2) {
        this.FIELD2 = FIELD2;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getOpenprice_yr() {
        return openprice_yr;
    }

    public void setOpenprice_yr(String openprice_yr) {
        this.openprice_yr = openprice_yr;
    }

    public String getSswr() {
        return sswr;
    }

    public void setSswr(String sswr) {
        this.sswr = sswr;
    }

    public String getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(String isCollected) {
        this.isCollected = isCollected;
    }
}
