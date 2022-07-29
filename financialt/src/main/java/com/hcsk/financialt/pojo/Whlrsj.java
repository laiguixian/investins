package com.hcsk.financialt.pojo;

import java.util.List;

//外汇利润数据
public class Whlrsj {
    public long bzid;//币种id
    public String bzname;//币种名称
    public List<Lirunlb> lirunlb;//利润列表
    public float xmrhmckcjyje;//先买后卖买入开仓交易金额
    public float xmrhmckccjje;//先买后卖买入开仓成交金额

    public float xmrhmcpcjyje;//先买后卖卖出平仓交易金额
    public float xmrhmcpccjje;//先买后卖卖出平仓成交金额

    public float xmchmrkcjyje;//先卖后买卖出开仓交易金额
    public float xmchmrkccjje;//先卖后买卖出开仓成交金额

    public float xmchmrpcjyje;//先卖后买买入平仓交易金额
    public float xmchmrpccjje;//先卖后买买入平仓成交金额

    public long getBzid() {
        return bzid;
    }

    public void setBzid(long bzid) {
        this.bzid = bzid;
    }

    public String getBzname() {
        return bzname;
    }

    public void setBzname(String bzname) {
        this.bzname = bzname;
    }

    public List<Lirunlb> getLirunlb() {
        return lirunlb;
    }

    public void setLirunlb(List<Lirunlb> lirunlb) {
        this.lirunlb = lirunlb;
    }

    public float getXmrhmckcjyje() {
        return xmrhmckcjyje;
    }

    public void setXmrhmckcjyje(float xmrhmckcjyje) {
        this.xmrhmckcjyje = xmrhmckcjyje;
    }

    public float getXmrhmckccjje() {
        return xmrhmckccjje;
    }

    public void setXmrhmckccjje(float xmrhmckccjje) {
        this.xmrhmckccjje = xmrhmckccjje;
    }

    public float getXmrhmcpcjyje() {
        return xmrhmcpcjyje;
    }

    public void setXmrhmcpcjyje(float xmrhmcpcjyje) {
        this.xmrhmcpcjyje = xmrhmcpcjyje;
    }

    public float getXmrhmcpccjje() {
        return xmrhmcpccjje;
    }

    public void setXmrhmcpccjje(float xmrhmcpccjje) {
        this.xmrhmcpccjje = xmrhmcpccjje;
    }

    public float getXmchmrkcjyje() {
        return xmchmrkcjyje;
    }

    public void setXmchmrkcjyje(float xmchmrkcjyje) {
        this.xmchmrkcjyje = xmchmrkcjyje;
    }

    public float getXmchmrkccjje() {
        return xmchmrkccjje;
    }

    public void setXmchmrkccjje(float xmchmrkccjje) {
        this.xmchmrkccjje = xmchmrkccjje;
    }

    public float getXmchmrpcjyje() {
        return xmchmrpcjyje;
    }

    public void setXmchmrpcjyje(float xmchmrpcjyje) {
        this.xmchmrpcjyje = xmchmrpcjyje;
    }

    public float getXmchmrpccjje() {
        return xmchmrpccjje;
    }

    public void setXmchmrpccjje(float xmchmrpccjje) {
        this.xmchmrpccjje = xmchmrpccjje;
    }
}
