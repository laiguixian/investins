package com.hcsk.financialt.pojo;

import java.util.Date;

public class Whkuaizhao {
    public long id;//主键',
    public long userid;//用户主键',
    public long bzid;//币种主键
    public String bzname;//币种名称
    public float xmrhmcjine;//先买入后卖出金额',
    public float xmrhmcrmbcb;//先买入后卖出成本人民币，就是持仓的时候花的相当于人民币的钱
    public float xmchmrjine;//先卖出后买入金额',
    public float xmchmrrmbcb;//先卖出后买入成本人民币，就是持仓的时候花的相当于人民币的钱
    public float xhmrj;//现汇买入价
    public Date modifytime;//最后修改时间

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBzid() {
        return bzid;
    }

    public void setBzid(long bzid) {
        this.bzid = bzid;
    }


    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getBzname() {
        return bzname;
    }

    public void setBzname(String bzname) {
        this.bzname = bzname;
    }


    public float getXhmrj() {
        return xhmrj;
    }

    public void setXhmrj(float xhmrj) {
        this.xhmrj = xhmrj;
    }

    public float getXmrhmcjine() {
        return xmrhmcjine;
    }

    public void setXmrhmcjine(float xmrhmcjine) {
        this.xmrhmcjine = xmrhmcjine;
    }

    public float getXmrhmcrmbcb() {
        return xmrhmcrmbcb;
    }

    public void setXmrhmcrmbcb(float xmrhmcrmbcb) {
        this.xmrhmcrmbcb = xmrhmcrmbcb;
    }

    public float getXmchmrjine() {
        return xmchmrjine;
    }

    public void setXmchmrjine(float xmchmrjine) {
        this.xmchmrjine = xmchmrjine;
    }

    public float getXmchmrrmbcb() {
        return xmchmrrmbcb;
    }

    public void setXmchmrrmbcb(float xmchmrrmbcb) {
        this.xmchmrrmbcb = xmchmrrmbcb;
    }
}
