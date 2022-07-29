package com.hcsk.financialt.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Exchangerate {
    public long id;//主键
    public long bzid;//币种id',
    public long orgid;//机构名称',
    public float xhmrj;//现汇买入价',
    public float xcmrj;//现钞买入价',
    public float xhmcj;//现汇卖出价',
    public float xcmcj;//现汇卖出价',
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date fbsj;//发布时间',
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public int fbsjdate;//发布时间到日的转整型
    public Date hqsj;//获取时间',
    public long bwid;//获取的报文id',

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getFbsjdate() {
        return fbsjdate;
    }

    public void setFbsjdate(int fbsjdate) {
        this.fbsjdate = fbsjdate;
    }

    public Date getHqsj() {
        return hqsj;
    }

    public void setHqsj(Date hqsj) {
        this.hqsj = hqsj;
    }

    public long getBwid() {
        return bwid;
    }

    public void setBwid(long bwid) {
        this.bwid = bwid;
    }

    public long getBzid() {
        return bzid;
    }

    public void setBzid(long bzid) {
        this.bzid = bzid;
    }

    public long getOrgid() {
        return orgid;
    }

    public void setOrgid(long orgid) {
        this.orgid = orgid;
    }
}
