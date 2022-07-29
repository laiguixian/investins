package com.hcsk.financialt.pojo;

public class Yonghub {
    public long id;//主键
    public float rmbyue;//人民币余额
    public float whbzjyue;//外汇保证金余额
    public float zhgjsbzjyue;//账户贵金属保证金余额

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getRmbyue() {
        return rmbyue;
    }

    public void setRmbyue(float rmbyue) {
        this.rmbyue = rmbyue;
    }

    public float getWhbzjyue() {
        return whbzjyue;
    }

    public void setWhbzjyue(float whbzjyue) {
        this.whbzjyue = whbzjyue;
    }

    public float getZhgjsbzjyue() {
        return zhgjsbzjyue;
    }

    public void setZhgjsbzjyue(float zhgjsbzjyue) {
        this.zhgjsbzjyue = zhgjsbzjyue;
    }
}
