package com.hcsk.financialt.pojo;

import java.util.Date;
import java.util.List;

public class Bizhong {
    public long id;//主键
    public String bzname;//币种名称
    public String bzname2;//币种名称2
    public long orgid;//机构(比如银行)id
    public int bindex;//币种排序
    public int bzstatus;//币种状态：1-可看可投资，2-可看不可投资，3-不可看不可投资
    public String xmrhmclvrunlvalavalue;//先买入后卖出利润率警报值
    public String xmrhmclvrunalavalue;//先买入后卖出利润警报值
    public String xmchmrlvrunlvalavalue;//先卖出后买入利润率警报值
    public String xmchmrlvrunalavalue;//先卖出后买入利润警报值
    public String xmrhmcyhmrjalavalue;//先买入后卖出银行买入价警报值
    public String xmrhmcyhmcjalavalue;//先买入后卖出银行卖出价警报值
    public String xmchmryhmrjalavalue;//先卖出后买入银行买入价警报值
    public String xmchmryhmcjalavalue;//先卖出后买入银行卖出价警报值
    public float xhmrj;//现汇买入价
    public float xcmrj;//现钞买入价
    public float xhmcj;//现汇卖出价
    public float xcmcj;//现钞卖出价
    public float zdf3day;//3交易日涨跌幅
    public float zdf2day;//2交易日涨跌幅
    public float zdf1day;//1交易日涨跌幅
    public Date fbsj;
    public int sjpyl;//相比0时区时间偏移量
    public String sqmc;//时区名称
    public String bzzdgl;//记录该币种长期的涨跌规律
    public int[] zghlcs=new int[24];//最高汇率次数分布数组
    public int[] zdhlcs=new int[24];//最低汇率次数分布数组
    public List<Exchangerate> daydata;//按天的数据
    public List<Exchangerate> seconddata;//按秒的数据
    public List<Exchangerate> fhhldata;//返回的汇率数据
    public float zgxhmrj;//最高现汇买入价
    public float pjzgxhmrj;//平均现汇买入价
    public float tjzgxhmrj;//推荐现汇买入价
    public float zdxhmcj;//最低现汇卖出价
    public float pjzdxhmcj;//平均现汇卖出价
    public float tjzdxhmcj;//推荐现汇卖出价

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBzname() {
        return bzname;
    }

    public void setBzname(String bzname) {
        this.bzname = bzname;
    }

    public String getBzname2() {
        return bzname2;
    }

    public void setBzname2(String bzname2) {
        this.bzname2 = bzname2;
    }

    public int getBindex() {
        return bindex;
    }

    public void setBindex(int bindex) {
        this.bindex = bindex;
    }

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }

    public int getBzstatus() {
        return bzstatus;
    }

    public void setBzstatus(int bzstatus) {
        this.bzstatus = bzstatus;
    }

    public String getXmrhmclvrunlvalavalue() {
        return xmrhmclvrunlvalavalue;
    }

    public void setXmrhmclvrunlvalavalue(String xmrhmclvrunlvalavalue) {
        this.xmrhmclvrunlvalavalue = xmrhmclvrunlvalavalue;
    }

    public String getXmrhmclvrunalavalue() {
        return xmrhmclvrunalavalue;
    }

    public void setXmrhmclvrunalavalue(String xmrhmclvrunalavalue) {
        this.xmrhmclvrunalavalue = xmrhmclvrunalavalue;
    }

    public String getXmchmrlvrunlvalavalue() {
        return xmchmrlvrunlvalavalue;
    }

    public void setXmchmrlvrunlvalavalue(String xmchmrlvrunlvalavalue) {
        this.xmchmrlvrunlvalavalue = xmchmrlvrunlvalavalue;
    }

    public String getXmchmrlvrunalavalue() {
        return xmchmrlvrunalavalue;
    }

    public void setXmchmrlvrunalavalue(String xmchmrlvrunalavalue) {
        this.xmchmrlvrunalavalue = xmchmrlvrunalavalue;
    }

    public String getXmrhmcyhmrjalavalue() {
        return xmrhmcyhmrjalavalue;
    }

    public void setXmrhmcyhmrjalavalue(String xmrhmcyhmrjalavalue) {
        this.xmrhmcyhmrjalavalue = xmrhmcyhmrjalavalue;
    }

    public String getXmrhmcyhmcjalavalue() {
        return xmrhmcyhmcjalavalue;
    }

    public void setXmrhmcyhmcjalavalue(String xmrhmcyhmcjalavalue) {
        this.xmrhmcyhmcjalavalue = xmrhmcyhmcjalavalue;
    }

    public String getXmchmryhmrjalavalue() {
        return xmchmryhmrjalavalue;
    }

    public void setXmchmryhmrjalavalue(String xmchmryhmrjalavalue) {
        this.xmchmryhmrjalavalue = xmchmryhmrjalavalue;
    }

    public String getXmchmryhmcjalavalue() {
        return xmchmryhmcjalavalue;
    }

    public void setXmchmryhmcjalavalue(String xmchmryhmcjalavalue) {
        this.xmchmryhmcjalavalue = xmchmryhmcjalavalue;
    }

    public float getXhmrj() {
        return xhmrj;
    }

    public void setXhmrj(float xhmrj) {
        this.xhmrj = xhmrj;
    }

    public float getXcmrj() {
        return xcmrj;
    }

    public void setXcmrj(float xcmrj) {
        this.xcmrj = xcmrj;
    }

    public float getXhmcj() {
        return xhmcj;
    }

    public void setXhmcj(float xhmcj) {
        this.xhmcj = xhmcj;
    }

    public float getXcmcj() {
        return xcmcj;
    }

    public void setXcmcj(float xcmcj) {
        this.xcmcj = xcmcj;
    }

    public Date getFbsj() {
        return fbsj;
    }

    public void setFbsj(Date fbsj) {
        this.fbsj = fbsj;
    }

    public float getZdf3day() {
        return zdf3day;
    }

    public void setZdf3day(float zdf3day) {
        this.zdf3day = zdf3day;
    }

    public float getZdf2day() {
        return zdf2day;
    }

    public void setZdf2day(float zdf2day) {
        this.zdf2day = zdf2day;
    }

    public float getZdf1day() {
        return zdf1day;
    }

    public void setZdf1day(float zdf1day) {
        this.zdf1day = zdf1day;
    }

    public int getSjpyl() {
        return sjpyl;
    }

    public void setSjpyl(int sjpyl) {
        this.sjpyl = sjpyl;
    }

    public String getSqmc() {
        return sqmc;
    }

    public void setSqmc(String sqmc) {
        this.sqmc = sqmc;
    }

    public String getBzzdgl() {
        return bzzdgl;
    }

    public void setBzzdgl(String bzzdgl) {
        this.bzzdgl = bzzdgl;
    }

    public int[] getZghlcs() {
        return zghlcs;
    }

    public void setZghlcs(int[] zghlcs) {
        this.zghlcs = zghlcs;
    }

    public int[] getZdhlcs() {
        return zdhlcs;
    }

    public void setZdhlcs(int[] zdhlcs) {
        this.zdhlcs = zdhlcs;
    }

    public List<Exchangerate> getDaydata() {
        return daydata;
    }

    public void setDaydata(List<Exchangerate> daydata) {
        this.daydata = daydata;
    }

    public List<Exchangerate> getSeconddata() {
        return seconddata;
    }

    public void setSeconddata(List<Exchangerate> seconddata) {
        this.seconddata = seconddata;
    }

    public List<Exchangerate> getFhhldata() {
        return fhhldata;
    }

    public void setFhhldata(List<Exchangerate> fhhldata) {
        this.fhhldata = fhhldata;
    }

    public float getZgxhmrj() {
        return zgxhmrj;
    }

    public void setZgxhmrj(float zgxhmrj) {
        this.zgxhmrj = zgxhmrj;
    }

    public float getPjzgxhmrj() {
        return pjzgxhmrj;
    }

    public void setPjzgxhmrj(float pjzgxhmrj) {
        this.pjzgxhmrj = pjzgxhmrj;
    }

    public float getTjzgxhmrj() {
        return tjzgxhmrj;
    }

    public void setTjzgxhmrj(float tjzgxhmrj) {
        this.tjzgxhmrj = tjzgxhmrj;
    }

    public float getZdxhmcj() {
        return zdxhmcj;
    }

    public void setZdxhmcj(float zdxhmcj) {
        this.zdxhmcj = zdxhmcj;
    }

    public float getPjzdxhmcj() {
        return pjzdxhmcj;
    }

    public void setPjzdxhmcj(float pjzdxhmcj) {
        this.pjzdxhmcj = pjzdxhmcj;
    }

    public float getTjzdxhmcj() {
        return tjzdxhmcj;
    }

    public void setTjzdxhmcj(float tjzdxhmcj) {
        this.tjzdxhmcj = tjzdxhmcj;
    }
}
