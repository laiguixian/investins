package com.hcsk.financialt.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Whjyb {
    public long id;//主键',
    public long userid;//用户主键',
    public long jybzid;//交易币种id
    public String jybzname;//交易币种名称',
    public float jyjine;//交易金额
    public float cjjine;//成交金额
    public float cjjiage;//成交价格
    public float jslirun;//结算利润
    public float xmrhmccb;//先买入后卖出成本
    public int jyleixing;//交易类型：交易类型：1-买入开仓，2-卖出平仓，3-卖出开仓，4-买入平仓
    public String jyleixingname;//交易类型名称
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date jiaoyisj;//交易时间',
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date addtime;//记录生成时间
    //危险等级（用于标识买入的币种汇率下跌的趋势，包含下跌的持续时间和下跌幅度的考量）：
    //正常状态，衡量指标：最近6次外汇报价下跌次数小于等于3次，最后面一次比最前面一次下跌幅度小于0.001，3日内最终汇率至少有一次上涨本日最终汇率比3日前
    //下跌幅度小于0.001，无须关注
    //时刻关注，衡量指标：最近6次外汇报价下跌次数大于4次，最后面一次比最前面一次下跌幅度小于0.001，3日内最终汇率至少两次下跌，3日内最终汇率至少有一次上涨本日最终汇率比3日前
    //下跌幅度小于0.001，需要时刻关注
    //情况危急，衡量指标：最近6次外汇报价下跌次数大于5次，或最近6次外汇报价下跌次数大于4次且最近6次最后面一次比最前面一次下跌幅度大于0.001，
    // 或3日内最终汇率至少有两次下跌本日最终汇率比3日前下跌幅度大于0.002，必须立即采取行动
    public float xjzhyue;//现金账户余额
    public float whbzjyue;//外汇保证金账户余额
    public float zhgjsbzjyue;//账户贵金属保证金账户余额
    public String remark;//交易备注

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getJybzid() {
        return jybzid;
    }

    public void setJybzid(long jybzid) {
        this.jybzid = jybzid;
    }

    public String getJybzname() {
        return jybzname;
    }

    public void setJybzname(String jybzname) {
        this.jybzname = jybzname;
    }

    public float getJyjine() {
        return jyjine;
    }

    public void setJyjine(float jyjine) {
        this.jyjine = jyjine;
    }

    public float getCjjine() {
        return cjjine;
    }

    public void setCjjine(float cjjine) {
        this.cjjine = cjjine;
    }

    public int getJyleixing() {
        return jyleixing;
    }

    public void setJyleixing(int jyleixing) {
        this.jyleixing = jyleixing;
    }

    public String getJyleixingname() {
        return jyleixingname;
    }

    public void setJyleixingname(String jyleixingname) {
        this.jyleixingname = jyleixingname;
    }

    public Date getJiaoyisj() {
        return jiaoyisj;
    }

    public void setJiaoyisj(Date jiaoyisj) {
        this.jiaoyisj = jiaoyisj;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public float getCjjiage() {
        return cjjiage;
    }

    public void setCjjiage(float cjjiage) {
        this.cjjiage = cjjiage;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public float getXjzhyue() {
        return xjzhyue;
    }

    public void setXjzhyue(float xjzhyue) {
        this.xjzhyue = xjzhyue;
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

    public float getJslirun() {
        return jslirun;
    }

    public void setJslirun(float jslirun) {
        this.jslirun = jslirun;
    }

    public float getXmrhmccb() {
        return xmrhmccb;
    }

    public void setXmrhmccb(float xmrhmccb) {
        this.xmrhmccb = xmrhmccb;
    }
}
