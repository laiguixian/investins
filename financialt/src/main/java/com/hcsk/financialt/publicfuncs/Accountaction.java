package com.hcsk.financialt.publicfuncs;

import com.google.gson.Gson;
import com.hcsk.financialt.masterdb.dao.sqlMapper;
import com.hcsk.financialt.pojo.Whjyb;
import com.hcsk.financialt.pojo.Whkuaizhao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//账户操作
@Component
public class Accountaction {
    @Autowired
    sqlMapper sqlmapper;
    //账户清算
    public void accountcount(){
        BigDecimal xjzhyue=new BigDecimal(0);//现金账户余额，用于累加

        BigDecimal bzjzhyue=new BigDecimal(0);//账户外汇保证金账户余额，用于累加
        BigDecimal tempbzjzhyue=new BigDecimal(0);//账户外汇保证金账户余额，用于累加

        BigDecimal zhgjsbzjzhyue=new BigDecimal(0);//账户贵金属保证金账户余额，用于累加
        BigDecimal tempzhgjsbzjzhyue=new BigDecimal(0);//账户贵金属保证金账户余额，用于累加

        BigDecimal zhwhbzjxmchmrmczje=new BigDecimal(0);//账户外汇先卖后买卖出总金额，临时计算用
        BigDecimal zhwhbzjxmchmrmrzje=new BigDecimal(0);//账户外汇先卖后买买入总金额，临时计算用

        BigDecimal zhgjsbzjxmchmrmczje=new BigDecimal(0);//账户贵金属先卖后买卖出总金额，临时计算用
        BigDecimal zhgjsbzjxmchmrmrzje=new BigDecimal(0);//账户贵金属先卖后买买入总金额，临时计算用

        float xmrhmccb=0;//先买入后卖出成本

        if (exchangerate.lastexchangeratesisready) {
            //新生成的外汇快照
            List<Whkuaizhao> whkuaizhaos=new ArrayList<Whkuaizhao>();
            //获取所有交易记录
            List<Whjyb> whjylb = sqlmapper.getalljyjl(1);
            //更新所有交易记录的现金及保证金余额
            for(int i=0;i<whjylb.size();i++){
                Whjyb whjyb=whjylb.get(i);
                //计算外汇快照
                //检查看快照里面是否有对应币种
                int bzindex=-1;
                for(int j=0;j<whkuaizhaos.size();j++){
                    if(whkuaizhaos.get(j).getBzid()==whjyb.getJybzid()){
                        bzindex=j;
                    }
                }
                //如果没有对应币种则直接进行新增
                if(bzindex<0){
                    Whkuaizhao whkuaizhao=new Whkuaizhao();
                    whkuaizhao.setBzid(whjyb.getJybzid());
                    whkuaizhao.setBzname(whjyb.getJybzname());
                    whkuaizhao.setXhmrj(0);
                    whkuaizhao.setXmchmrjine(0);
                    whkuaizhao.setXmchmrrmbcb(0);
                    whkuaizhao.setXmrhmcjine(0);
                    whkuaizhao.setXmrhmcrmbcb(0);
                    whkuaizhaos.add(whkuaizhao);
                    bzindex=whkuaizhaos.size()-1;
                }
                Whkuaizhao tempwhkuaizhao=whkuaizhaos.get(bzindex);

                //按交易类型进行快照计算
                if (whjyb.getJyleixing() == 1) {//交易类型等于买入开仓
                    //核增对应外币的快照先买后卖金额
                    tempwhkuaizhao.setXmrhmcjine(new BigDecimal(tempwhkuaizhao.getXmrhmcjine()+"").add(new BigDecimal(whjyb.getJyjine()+"")).floatValue());
                    tempwhkuaizhao.setXmrhmcrmbcb(new BigDecimal(tempwhkuaizhao.getXmrhmcrmbcb()+"").add(new BigDecimal(whjyb.getCjjine()+"")).floatValue());
                } else if (whjyb.getJyleixing() == 2) {//交易类型等于卖出平仓
                    float tempxmrhmcjine=new BigDecimal(tempwhkuaizhao.getXmrhmcjine()+"").subtract(new BigDecimal(whjyb.getJyjine()+"")).floatValue();

                    if(tempxmrhmcjine==0){
                        tempwhkuaizhao.setXmrhmcrmbcb(0);
                    }else {
                        //tempwhkuaizhao.setXmrhmcrmbcb(new BigDecimal(tempwhkuaizhao.getXmrhmcrmbcb()+"").subtract(new BigDecimal(whjyb.getCjjine()+"")).floatValue());
                        tempwhkuaizhao.setXmrhmcrmbcb(new BigDecimal(tempwhkuaizhao.getXmrhmcrmbcb()+"").subtract(new BigDecimal((tempwhkuaizhao.getXmrhmcrmbcb()*whjyb.getJyjine()/tempwhkuaizhao.getXmrhmcjine())+"")).floatValue());
                    }
                    //核减对应外币的快照先买后卖金额
                    tempwhkuaizhao.setXmrhmcjine(tempxmrhmcjine);
                } else if (whjyb.getJyleixing() == 3) {//交易类型等于卖出开仓
                    //核增对应外币的快照先卖后买金额
                    tempwhkuaizhao.setXmchmrjine(new BigDecimal(tempwhkuaizhao.getXmchmrjine()+"").add(new BigDecimal(whjyb.getJyjine()+"")).floatValue());
                    tempwhkuaizhao.setXmchmrrmbcb(new BigDecimal(tempwhkuaizhao.getXmchmrrmbcb()+"").add(new BigDecimal(whjyb.getCjjine()+"")).floatValue());
                } else if (whjyb.getJyleixing() == 4) {//交易类型等于买入平仓
                    float tempxmchmrjine=new BigDecimal(tempwhkuaizhao.getXmchmrjine()+"").subtract(new BigDecimal(whjyb.getJyjine()+"")).floatValue();

                    if(tempxmchmrjine==0){
                        tempwhkuaizhao.setXmchmrrmbcb(0);
                    }else {
                        //pubfuncs.printerrormsg("保证金更新："+tempwhkuaizhao.getXmchmrrmbcb()+","+angprice+","+whjyb.getJyjine()+","+(angprice*whjyb.getJyjine()));
                        //tempwhkuaizhao.setXmchmrrmbcb(new BigDecimal(tempwhkuaizhao.getXmchmrrmbcb()+"").subtract(new BigDecimal(whjyb.getCjjine()+"")).floatValue());
                        //tempwhkuaizhao.setXmchmrrmbcb(new BigDecimal(tempwhkuaizhao.getXmchmrrmbcb()+"").subtract(new BigDecimal((angprice*whjyb.getJyjine())+"")).floatValue());
                        //pubfuncs.printerrormsg("更新后的保证金："+tempwhkuaizhao.getXmchmrrmbcb());
                        //取出均价
                        float angprice=tempwhkuaizhao.getXmchmrrmbcb()/tempwhkuaizhao.getXmchmrjine();
                        tempwhkuaizhao.setXmchmrrmbcb(new BigDecimal(tempwhkuaizhao.getXmchmrrmbcb()+"").subtract(new BigDecimal((angprice*whjyb.getJyjine())+"")).floatValue());
                    }
                    //核减对应外币的快照先买后卖金额
                    tempwhkuaizhao.setXmchmrjine(tempxmchmrjine);
                }
                whkuaizhaos.set(bzindex,tempwhkuaizhao);

                //计算现金余额
                if((whjyb.getJyleixing()==1) || (whjyb.getJyleixing()==30) || (whjyb.getJyleixing()==31) || (whjyb.getJyleixing()>=50 && whjyb.getJyleixing()<=69)
                        || (whjyb.getJyleixing()>=110 && whjyb.getJyleixing()<=129)){//减现金余额
                    xjzhyue=xjzhyue.subtract(new BigDecimal(whjyb.getCjjine()+""));
                }else if((whjyb.getJyleixing()==2) || (whjyb.getJyleixing()==40) || (whjyb.getJyleixing()==41) || (whjyb.getJyleixing()>=70 && whjyb.getJyleixing()<=89)
                        || (whjyb.getJyleixing()>=90 && whjyb.getJyleixing()<=109)){//加现金余额
                    xjzhyue=xjzhyue.add(new BigDecimal(whjyb.getCjjine()+""));
                }
                //计算保证金余额
                if(whjyb.getJyleixing()==4){
                    //减暂存的用于计算的保证金余额，更新保证金余额（因为此时已经结算先卖后买，所以可以更新）
                    if(whjyb.getJybzid()>=107 && whjyb.getJybzid()<=152) {//账户外汇
                        /*tempbzjzhyue = tempbzjzhyue.subtract(new BigDecimal(whjyb.getCjjine() + ""));
                        if(tempwhkuaizhao.getXmchmrjine()==0) {//快照为0且币种不为人民币
                            bzjzhyue = tempbzjzhyue;
                        }*/
                        //tempbzjzhyue = tempbzjzhyue.add(new BigDecimal((whjyb.getCjjine()-whjyb.getJyjine()*angprice) + ""));
                        zhwhbzjxmchmrmrzje=zhwhbzjxmchmrmrzje.add(new BigDecimal(whjyb.getCjjine() + ""));
                        if(tempwhkuaizhao.getXmchmrjine()==0) {
                            bzjzhyue = bzjzhyue.add(zhwhbzjxmchmrmczje.subtract(zhwhbzjxmchmrmrzje));
                            zhwhbzjxmchmrmczje = new BigDecimal(0);//账户外汇先卖后买卖出总金额，临时计算用
                            zhwhbzjxmchmrmrzje = new BigDecimal(0);//账户外汇先卖后买买入总金额，临时计算用
                        }
                    }else if(whjyb.getJybzid()>=162 && whjyb.getJybzid()<=197) {//账户贵金属
                        /*tempzhgjsbzjzhyue = tempzhgjsbzjzhyue.subtract(new BigDecimal(whjyb.getCjjine() + ""));
                        if(tempwhkuaizhao.getXmchmrjine()==0) {//快照为0且币种不为人民币
                            zhgjsbzjzhyue = tempzhgjsbzjzhyue;
                        }*/
                        //tempzhgjsbzjzhyue = tempzhgjsbzjzhyue.add(new BigDecimal((whjyb.getCjjine()-whjyb.getJyjine()*angprice) + ""));
                        zhgjsbzjxmchmrmrzje=zhgjsbzjxmchmrmrzje.add(new BigDecimal(whjyb.getCjjine() + ""));
                        if(tempwhkuaizhao.getXmchmrjine()==0) {
                            zhgjsbzjzhyue = zhgjsbzjzhyue.add(zhgjsbzjxmchmrmczje.subtract(zhgjsbzjxmchmrmrzje));
                            zhgjsbzjxmchmrmczje = new BigDecimal(0);//账户外汇先卖后买卖出总金额，临时计算用
                            zhgjsbzjxmchmrmrzje = new BigDecimal(0);//账户外汇先卖后买买入总金额，临时计算用
                        }
                    }
                }else if(whjyb.getJyleixing()==40){
                    //账户外汇减暂存的用于计算的保证金余额，更新保证金余额
                    //tempbzjzhyue = tempbzjzhyue.subtract(new BigDecimal(whjyb.getCjjine() + ""));
                    /*if(tempwhkuaizhao.getXmchmrjine()==0 && (tempwhkuaizhao.getBzid()!=157)) {//快照为0且币种不为人民币
                        bzjzhyue = tempbzjzhyue;
                    }*/
                    bzjzhyue = bzjzhyue.subtract(new BigDecimal(whjyb.getCjjine() + ""));
                }else if(whjyb.getJyleixing()==41){
                    //账户贵金属减暂存的用于计算的保证金余额，更新保证金余额
                    ///tempzhgjsbzjzhyue = tempzhgjsbzjzhyue.subtract(new BigDecimal(whjyb.getCjjine() + ""));
                    /*if(tempwhkuaizhao.getXmchmrjine()==0 && (tempwhkuaizhao.getBzid()!=157)) {//快照为0且币种不为人民币
                        zhgjsbzjzhyue = tempzhgjsbzjzhyue;
                    }*/
                    zhgjsbzjzhyue = zhgjsbzjzhyue.subtract(new BigDecimal(whjyb.getCjjine() + ""));
                }else if(whjyb.getJyleixing()==3){
                    //加暂存的用于计算的保证金余额，还不能更新保证金余额（因为此时还没有结算先卖后买，所以不能更新）
                    /*if(whjyb.getJybzid()>=107 && whjyb.getJybzid()<=152) {//账户外汇
                        tempbzjzhyue = tempbzjzhyue.add(new BigDecimal(whjyb.getCjjine() + ""));
                    }else if(whjyb.getJybzid()>=162 && whjyb.getJybzid()<=197) {//账户贵金属
                        tempzhgjsbzjzhyue = tempzhgjsbzjzhyue.add(new BigDecimal(whjyb.getCjjine() + ""));
                    }*/

                    if(whjyb.getJybzid()>=107 && whjyb.getJybzid()<=152) {//账户外汇
                        zhwhbzjxmchmrmczje=zhwhbzjxmchmrmczje.add(new BigDecimal(whjyb.getCjjine() + ""));
                    }else if(whjyb.getJybzid()>=162 && whjyb.getJybzid()<=197) {//账户贵金属
                        zhgjsbzjxmchmrmczje=zhgjsbzjxmchmrmczje.add(new BigDecimal(whjyb.getCjjine() + ""));
                    }
                }else if(whjyb.getJyleixing()==30){//账户外汇加保证金余额
                    //tempbzjzhyue = tempbzjzhyue.add(new BigDecimal(whjyb.getCjjine() + ""));
                    //bzjzhyue = tempbzjzhyue;
                    bzjzhyue = bzjzhyue.add(new BigDecimal(whjyb.getCjjine() + ""));
                }else if(whjyb.getJyleixing()==31){//账户贵金属加保证金余额
                    zhgjsbzjzhyue = zhgjsbzjzhyue.add(new BigDecimal(whjyb.getCjjine() + ""));
                    //zhgjsbzjzhyue = tempzhgjsbzjzhyue;
                }
                //pubfuncs.printerrormsg("交易结果："+whjyb.getJyleixing()+","+whjyb.getJiaoyisj()+","+whjyb.getJybzname()+","+whjyb.getCjjine()+","+xjzhyue.floatValue()+","+bzjzhyue.floatValue()+","+tempbzjzhyue.floatValue());
                //sqlmapper.updatejyjlrmbyue(xjzhyue.floatValue(),bzjzhyue.floatValue(),zhgjsbzjzhyue.floatValue(),whjyb.getId());
                xmrhmccb=0;
                for(int k=0;k<whkuaizhaos.size();k++){
                    xmrhmccb=xmrhmccb+whkuaizhaos.get(k).getXmrhmcrmbcb();
                }
                sqlmapper.updatejyjlrmbyue(xjzhyue.floatValue(),bzjzhyue.floatValue(),zhgjsbzjzhyue.floatValue(),xmrhmccb,whjyb.getId());
            }
            //更新用户余额
            sqlmapper.updateuserrmbyue(xjzhyue.floatValue(),1);
            //更新用户账户外汇保证金余额
            //sqlmapper.updateuserbzjyue(bzjzhyue.floatValue(),1);
            sqlmapper.updateuserbzjyue(bzjzhyue.floatValue(),1);
            //更新用户账户贵金属保证金余额
            //sqlmapper.updateuserzhgjsbzjyue(zhgjsbzjzhyue.floatValue(),1);
            sqlmapper.updateuserzhgjsbzjyue(zhgjsbzjzhyue.floatValue(),1);
            //删除所有外汇快照
            sqlmapper.deletewhkz();
            //将计算得到的快照写入数据库
            for(int j=0;j<whkuaizhaos.size();j++){
                Whkuaizhao onewhkuaizhao=whkuaizhaos.get(j);
                sqlmapper.insertwhkz(1,onewhkuaizhao.getBzid(),onewhkuaizhao.getXmrhmcjine(),onewhkuaizhao.getXmrhmcrmbcb(),
                        onewhkuaizhao.getXmchmrjine(),onewhkuaizhao.getXmchmrrmbcb());
            }

            //加载余额大于0的外汇快照
            pubfuncs.printerrormsg("加载余额大于0的外汇快照");
            while(exchangerate.whkuaizhaoisinuse){}
            exchangerate.whkuaizhaoisinuse=true;
            exchangerate.whkuaizhaolist=sqlmapper.getwhkuaizhaos();
            exchangerate.whkuaizhaoisinuse=false;
        }
    }
}
