package com.hcsk.financialt.pojo;

public class Lirundata {
    public int tzbs;//投资笔数
    public float lrse;//利润数额
    public float pjtze;//平均投资额
    public float years;//跨度时间，单位：年
    public float syl;//收益率
    public float nhsyl;//年化收益率

    public int getTzbs() {
        return tzbs;
    }

    public void setTzbs(int tzbs) {
        this.tzbs = tzbs;
    }

    public float getLrse() {
        return lrse;
    }

    public void setLrse(float lrse) {
        this.lrse = lrse;
    }

    public float getPjtze() {
        return pjtze;
    }

    public void setPjtze(float pjtze) {
        this.pjtze = pjtze;
    }

    public float getSyl() {
        return syl;
    }

    public void setSyl(float syl) {
        this.syl = syl;
    }

    public float getYears() {
        return years;
    }

    public void setYears(float years) {
        this.years = years;
    }

    public float getNhsyl() {
        return nhsyl;
    }

    public void setNhsyl(float nhsyl) {
        this.nhsyl = nhsyl;
    }
}
