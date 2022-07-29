package com.hcsk.financialt.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hcsk.financialt.masterdb.dao.sqlMapper;
import com.hcsk.financialt.pojo.*;
import com.hcsk.financialt.publicfuncs.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hcsk.financialt.publicfuncs.exchangerate.sybizhongs;
import static com.hcsk.financialt.publicfuncs.exchangerate.lastexchangerates;
import static com.hcsk.financialt.publicfuncs.exchangerate.whkuaizhaolist;

@RestController
public class apicontroller {
    @Autowired
    exchangerate exchangeratea;
    @Autowired
    sqlMapper sqlmapper;
    @Autowired
    webdo webdoa;
    @Autowired
    pubfuncs pcs;
    @Autowired
    Accountaction accountaction;
    @Autowired
    hostdeal hd;
    //远程调用url获取汇率信息
    @PostMapping(value="/geturlr")
    public String geturlr(HttpServletRequest request){
        String resultvalue="";
        if (exchangerate.lastexchangeratesisready) {
            String gurl = request.getParameter("gu");
            String gmethod = request.getParameter("gm");
            //pubfuncs.printerrormsg("获取的输入参数："+gurl+","+gmethod);
            gurl = endecode1.decodestr(gurl);
            gmethod = gmethod.equals("1") ? "GET" : "POST";
            //pubfuncs.printerrormsg("输入参数解密后："+gurl);
            if (gurl.indexOf("/ctp/ctpservlet/EbdpAjaxServlet") > -1) {//账户外汇，此时用专用的调用接口的去获取
                resultvalue = webdoa.gethtmlfromurlzhwh(gurl, gmethod);
            } else {
                resultvalue = webdoa.gethtmlfromurl(gurl, gmethod);
            }
            //pubfuncs.printerrormsg("获取的返回值："+resultvalue);
        /*try{
            resultvalue=new String(resultvalue.getBytes(),"UTF-8");
        }catch (Exception ee){}*/
            resultvalue = endecode1.encodestr(resultvalue);

            //pubfuncs.printerrormsg("获取的返回值加密后："+resultvalue);
            //pubfuncs.printerrormsg("远程获取网页数据，长度：" + resultvalue.length());
        }
        return resultvalue;
    }

    //获取定时获取后缓存的未解析的包含汇率信息的字符串
    @PostMapping(value="/getnophlstr")
    public String getnophlstr(HttpServletRequest request){
        String resultvalue="";
        String gtype = request.getParameter("gt");
        if ((gtype.equals("1"))&&(exchangeratea.waihuinopstr.length()>0)) {//外汇
            resultvalue = exchangeratea.waihuinopstr;
            /*//先去除所有样式，减少不必要信息，减少传输和加密时间
            Document document = Jsoup.parse(resultvalue);
            //Elements elements =document.getElementsByTag("table");
            Elements elements =document.getElementsByClass("tableDataTable");
            Elements trs=elements.get(0).getElementsByTag("tr");
            String tablestr="<table class=\"tableDataTable\">";
            for (int i = 0; i < trs.size(); i++) {
                tablestr=tablestr+"<tr>";
                Elements tds = trs.get(i).getElementsByTag("td");
                for (int j = 0; j < tds.size(); j++) {
                    tablestr=tablestr+"<td>"+tds.get(j).text()+"</td>";
                }
                tablestr=tablestr+"</tr>";
            }
            tablestr=tablestr+"</table>";
            resultvalue="<html><head><title>1</title></head><body>"+tablestr+"</body></html>";*/
            /*resultvalue=new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")//24小时制
                    .create().toJson(exchangeratea.pracontent("icbc",resultvalue));*/
        } else if ((gtype.equals("2"))&&(exchangeratea.zhwhnopstr.length()>0)){//账户外汇
            resultvalue = exchangeratea.zhwhnopstr;
            /*resultvalue=new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")//24小时制
                    .create().toJson(exchangeratea.prazhwhcontent("icbc",resultvalue));*/
        } else if ((gtype.equals("3"))&&(exchangeratea.zhgjsnopstr.length()>0)){//账户贵金属
            resultvalue = exchangeratea.zhgjsnopstr;
        }
        //pubfuncs.printerrormsg("返回的未加密值："+resultvalue);
        /*try{
            resultvalue=new String(resultvalue.getBytes(),"UTF-8");
        }catch (Exception ee){}*/
        resultvalue = endecode1.encodestr(resultvalue);

        //pubfuncs.printerrormsg("获取的返回值加密后："+resultvalue);
        //pubfuncs.printerrormsg("远程获取网页数据，长度：" + resultvalue.length());
        return resultvalue;
    }

    //新增汇率信息，被动新增，主要是通过特制浏览器自动新增
    @PostMapping(value="/uphl")
    public Resmsg addwhjyjl(String hlstr,String gettype,String gettime){
        Resmsg resmsg=new Resmsg();
        if (exchangerate.lastexchangeratesisready) {
            //pubfuncs.printerrormsg("传入的初始值：" + hlstr.length()+",,,"+hlstr);
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                Date ingettime=format.parse(gettime);
                boolean indataisafterlast=false;//新传进来的数据是比刚刚进来的数据后发布的
                int gettypeint=Integer.parseInt(gettype);
                if(gettypeint==1 || gettypeint==3){
                    indataisafterlast=ingettime.after(exchangerate.jshgxddsj);
                }else if(gettypeint==2 || gettypeint==4){
                    indataisafterlast=ingettime.after(exchangerate.zhwhgxddsj);
                }
                if(indataisafterlast) {
                    String decodestr = endecode1.decodestr(hlstr);
                    //pubfuncs.printerrormsg("解密后的初始值：" + decodestr.length()+",,,"+decodestr);
                    exchangeratea.banktsexchangerate("icbc", decodestr, gettypeint);
                    //pubfuncs.printerrormsg("传入的解密后汇率信息：" + decodestr);
                    pubfuncs.printerrormsg("传入更新的汇率信息");
                }else{
                    pubfuncs.printerrormsg("传入的汇率信息已过期，废弃");
                }
                resmsg.setResultcode("101");
                resmsg.setResultmsg("提交成功");
            } catch (Exception ee) {
                pubfuncs.printerrormsg("异常：" + ee.getMessage());
            }
        }
        return resmsg;
    }
    //获取各币种信息及全量汇率信息
    @PostMapping(value="/gethuilv")
    //@RequestMapping(value = "/gethuilv", method = {RequestMethod.GET,RequestMethod.POST})
    //public List<Exchangerate> gethuilv(HttpServletRequest request){
    public String gethuilv(HttpServletRequest request){
        pubfuncs.printerrormsg("请求最新汇率数据开始");
        String resultvalue ="";
        if (exchangerate.lastexchangeratesisready) {
            String getdf = request.getParameter("getdf");
            String getdt = request.getParameter("getdt");
            String bzid = request.getParameter("bzid");
            String gettype = request.getParameter("gettype");
            //pubfuncs.printerrormsg("传入的：gettype="+gettype+",bzid="+bzid);
            List<Bizhong> newbzs=new ArrayList<Bizhong>();
            newbzs = exchangeratea.gethlbybzandsj(bzid, getdf,getdt,gettype);
            //pubfuncs.printerrormsg("返回记录数："+newexchangerates.size());
            //pubfuncs.printerrormsg("返回的记录："+new Gson().toJson(newbzs));
            //return newexchangerates;
            resultvalue = new GsonBuilder()
                    //.setDateFormat("yyyy-MM-dd hh:mm:ss")//12小时制
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")//24小时制
                    .create().toJson(newbzs);
        }
        pubfuncs.printerrormsg("请求最新汇率数据结束");
        return resultvalue;
    }

    //获取各币种最新汇率信息
    @PostMapping(value="/getlasthuilv")
    public String getlasthuilv(HttpServletRequest request){
        String resultvalue ="";
        if (exchangerate.lastexchangeratesisready) {
            resultvalue = new GsonBuilder()
                    //.setDateFormat("yyyy-MM-dd hh:mm:ss")//12小时制
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")//24小时制
                    .create().toJson(lastexchangerates);
        }
        return resultvalue;
    }

    //新增外汇交易记录
    @PostMapping(value="/addwhjyjl")
    public Resmsg addwhjyjl(Whjyb whjyb){
        //pubfuncs.printerrormsg("传入的值："+new Gson().toJson(whjyb));
        Resmsg resmsg=new Resmsg();
        if (exchangerate.lastexchangeratesisready) {
            if (whjyb.getJyleixing() >= 1 && whjyb.getJyleixing() <= 4) {//交易类型为需要更新快照的类型
                Whkuaizhao whkuaizhao = sqlmapper.getwhkzbyuseridandbz(1, whjyb.getJybzid());//查询交易币种的快照
                if (whkuaizhao == null) {//如果找不到交易币种的快照则直接新增该交易币种的快照
                    sqlmapper.insertwhkz(1, whjyb.getJybzid(), 0, 0, 0, 0);
                    whkuaizhao = sqlmapper.getwhkzbyuseridandbz(1, whjyb.getJybzid());//查询交易币种的快照
                }
                float yhrmbyue = sqlmapper.selectrmbyuebyuserid(1);//查询用户人民币余额
                float yhbzjyue = 0;//用户保证金余额
                if(whjyb.getJybzid()>=107 && whjyb.getJybzid()<=152) {//账户外汇
                    yhbzjyue = sqlmapper.selectbzjyuebyuserid(1);//查询用户账户外汇保证金余额
                }else if(whjyb.getJybzid()>=162 && whjyb.getJybzid()<=197) {//账户贵金属
                    yhbzjyue = sqlmapper.selectzhgjsbzjyuebyuserid(1);//查询用户账户贵金属保证金余额
                }
                if (whjyb.getJyleixing() == 1) {//交易类型等于买入开仓
                    if (yhrmbyue < whjyb.getCjjine()) {//如果人民币余额小于成交金额，此时金额不够，不能成交
                        resmsg.setResultcode("501");
                        resmsg.setResultmsg("人民币余额不足，无法成交");
                        return resmsg;
                    }
                } else if (whjyb.getJyleixing() == 2) {//交易类型等于卖出平仓
                    if (whkuaizhao.getXmrhmcjine() < whjyb.getJyjine()) {//如果人民币余额小于成交金额，此时金额不够，不能成交
                        resmsg.setResultcode("501");
                        resmsg.setResultmsg("交易币种（对应外币）余额不足，无法成交");
                        return resmsg;
                    }
                } else if (whjyb.getJyleixing() == 3) {//交易类型等于卖出开仓
                    if (yhbzjyue < whjyb.getCjjine()) {//如果人民币余额小于成交金额，此时金额不够，不能成交
                        resmsg.setResultcode("501");
                        resmsg.setResultmsg("保证金余额不足，无法成交");
                        return resmsg;
                    }
                } else if (whjyb.getJyleixing() == 4) {//交易类型等于买入平仓
                    if (whkuaizhao.getXmchmrjine() < whjyb.getJyjine()) {//如果人民币余额小于成交金额，此时金额不够，不能成交
                        resmsg.setResultcode("501");
                        resmsg.setResultmsg("交易币种（对应外币）余额不足，无法成交");
                        return resmsg;
                    }
                }
            }
            if (sqlmapper.insertwhjyb(whjyb) <= 0) {
                resmsg.setResultcode("601");
                resmsg.setResultmsg("交易记录无法写入："+new Gson().toJson(whjyb));
                return resmsg;
            }
            //更新用户交易记录及余额快照的现金余额和保证金余额
            accountaction.accountcount();
            if (whjyb.getJyleixing() >= 1 && whjyb.getJyleixing() <= 4) {//交易类型为需要更新快照的类型
                //将外汇快照存入内存
                while (exchangerate.whkuaizhaoisinuse) {
                }
                exchangerate.whkuaizhaoisinuse = true;
                whkuaizhaolist = sqlmapper.getwhkuaizhaos();
                exchangerate.whkuaizhaoisinuse = false;
            }
            resmsg.setResultcode("101");
            resmsg.setResultmsg("提交成功");
        }
        return resmsg;
    }

    //获取利润分析数据
    @PostMapping(value="/viewlrdata")
    public String viewlrdata(String kssj,String jssj) {
        String returnvalue="";
        if (exchangerate.lastexchangeratesisready) {
            //pubfuncs.printerrormsg("传入的初始值：" + hlstr.length()+",,,"+hlstr);
            try {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat onlydayformat = new SimpleDateFormat("yyyy-MM-dd");
                Date ftime = format.parse(kssj);
                Date ttime = format.parse(jssj);
                List<Whjyb> whjylb=sqlmapper.getjyjlbytime(1,ftime,ttime);
                if(ftime.before(whjylb.get(0).getJiaoyisj()))
                    ftime=whjylb.get(0).getJiaoyisj();
                if(ttime.after(whjylb.get(whjylb.size()-1).getJiaoyisj())){
                    ttime=whjylb.get(whjylb.size()-1).getJiaoyisj();
                }
                //用于计算的投资金额累加
                float totaltzjine=0;
                float totallirun=0;
                int totalcs=0;
                int tzsc=0;
                float daytzjine=0;
                float daypjtzjine=0;
                int daycs=1;
                float xmrhmcyue=0;//先买入后卖出人民币
                String lastdatestr="";
                String nowdatestr="";
                Date lastjysj=null;
                Lirundata lirundata=new Lirundata();
                //lastdatestr=onlydayformat.format(whjylb.get(0).getJiaoyisj());
                for(int i=0;i<whjylb.size();i++){
                    //if(whjylb.get(i).getJyleixing()==1 || whjylb.get(i).getJyleixing()==2 || whjylb.get(i).getJyleixing()==3 || whjylb.get(i).getJyleixing()==4) {
                        //累积投资笔数
                        if (whjylb.get(i).getJyleixing() == 1 || whjylb.get(i).getJyleixing() == 3) {
                            //买入开仓或卖出开仓一次相当于投资一次
                            tzsc = tzsc + 1;
                        }
                        nowdatestr = onlydayformat.format(whjylb.get(i).getJiaoyisj());
                        //累积利润
                        totallirun = totallirun + whjylb.get(i).getJslirun();
                        if(whjylb.get(i).getJyleixing()<1 || whjylb.get(i).getJyleixing()>4) {
                            if (nowdatestr.equals(lastdatestr)) {
                                daycs = daycs + 1;
                                //pubfuncs.printerrormsg("相同计算：" + whjylb.get(i).getXjzhyue() + "," + whjylb.get(i).getWhbzjyue() + "," + whjylb.get(i).getZhgjsbzjyue());
                                daytzjine = daytzjine + whjylb.get(i).getXmrhmccb()+whjylb.get(i).getXjzhyue() + whjylb.get(i).getWhbzjyue() + whjylb.get(i).getZhgjsbzjyue();
                            }
                            //pubfuncs.printerrormsg("比较数字："+nowdatestr+","+lastdatestr+","+i+","+whjylb.size());
                            if ((!nowdatestr.equals(lastdatestr)) || i == whjylb.size() - 1) {

                                if (daycs > 1) {
                                    daytzjine = daytzjine + whjylb.get(i).getXmrhmccb()+whjylb.get(i).getXjzhyue() + whjylb.get(i).getWhbzjyue() + whjylb.get(i).getZhgjsbzjyue();
                                    daypjtzjine = daytzjine / daycs;
                                } else {
                                    //pubfuncs.printerrormsg("不同计算：" + whjylb.get(i).getXjzhyue() + "," + whjylb.get(i).getWhbzjyue() + "," + whjylb.get(i).getZhgjsbzjyue());
                                    daypjtzjine = whjylb.get(i).getXmrhmccb()+whjylb.get(i).getXjzhyue() + whjylb.get(i).getWhbzjyue() + whjylb.get(i).getZhgjsbzjyue();
                                }
                                //pubfuncs.printerrormsg("计算一次：" + daypjtzjine+","+daytzjine + "," + daycs);
                                totalcs = totalcs + 1;
                                totaltzjine = totaltzjine + daypjtzjine;
                                if (lastjysj != null) {
                                    int passday = (int) ((whjylb.get(i).getJiaoyisj().getTime() - lastjysj.getTime()) / (1000 * 3600 * 24));
                                    if (passday > 0) {
                                        totalcs = totalcs + passday;
                                        totaltzjine = totaltzjine + daypjtzjine * passday;
                                    }
                                }
                                daytzjine = 0;
                                daycs = 1;
                            }
                            lastdatestr = nowdatestr;
                        }
                    //}
                }
                //pubfuncs.printerrormsg("分析数据：" + totaltzjine+","+totalcs);
                lirundata.setLrse(totallirun);
                lirundata.setPjtze(totaltzjine/totalcs);
                lirundata.setTzbs(tzsc);
                lirundata.setSyl(totallirun*100/lirundata.getPjtze());
                //pubfuncs.printerrormsg("时间：" + format.format(ftime)+","+format.format(ttime)+","+ftime.getTime()+","+ttime.getTime()+","+((ttime.getTime()-ftime.getTime()) / (1000*3600*24*365)));
                lirundata.setYears((ttime.getTime()-ftime.getTime()) / 1000f/3600f/24f/365f);
                lirundata.setNhsyl(lirundata.getSyl()/((ttime.getTime()-ftime.getTime()) / 1000f/3600f/24f/365f));
                /*returnvalue = new GsonBuilder()
                        //.setDateFormat("yyyy-MM-dd hh:mm:ss")//12小时制
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")//24小时制
                        .create().toJson(lirundata);*/
                returnvalue="{\"lrse\":"+lirundata.getLrse()+",\"pjtze\":"+lirundata.getPjtze()+",\"tzbs\":"+
                        lirundata.getTzbs()+",\"syl\":"+lirundata.getSyl()+",\"years\":"+lirundata.getYears()+",\"nhsyl\":"+lirundata.getNhsyl()+"}";
            } catch (Exception ee) {
                pubfuncs.printerrormsg("获取利润分析数据出错：" + ee.getMessage());
                ee.printStackTrace();
            }
        }
        return returnvalue;
    }
    //账户清算
    @PostMapping(value="/accountcount")
    public Resmsg accountcount(){
        Resmsg resmsg=new Resmsg();
        if (exchangerate.lastexchangeratesisready) {
            try{
                accountaction.accountcount();
                resmsg.setResultcode("101");
                resmsg.setResultmsg("清算成功");
            }catch (Exception ee){
                resmsg.setResultcode("901");
                resmsg.setResultmsg("账户清算失败："+ee.getMessage());
                System.out.println("账户清算失败："+ee.getMessage());
            }
        }
        return resmsg;
    }

    //删除外汇交易记录
    @PostMapping(value="/deletewhjyjl")
    public Resmsg deletewhjyjl(long whjybid){
        Resmsg resmsg=new Resmsg();
        if (exchangerate.lastexchangeratesisready) {
            Whjyb whjyb = sqlmapper.getwhjyjlbyid(whjybid);
            if (whjyb != null) {
                //删除交易记录
                if (sqlmapper.deletewhjybbyid(whjybid) <= 0) {
                    resmsg.setResultcode("601");
                    resmsg.setResultmsg("删除失败：" + whjybid);
                    return resmsg;
                }

                //更新用户交易记录及余额快照的现金余额和保证金余额
                accountaction.accountcount();

                //将外汇快照存入内存
                while (exchangerate.whkuaizhaoisinuse) {
                }
                exchangerate.whkuaizhaoisinuse = true;
                whkuaizhaolist = sqlmapper.getwhkuaizhaos();
                exchangerate.whkuaizhaoisinuse = false;
                resmsg.setResultcode("101");
                resmsg.setResultmsg("删除成功");
            } else {
                resmsg.setResultcode("301");
                resmsg.setResultmsg("找不到外汇记录");
            }
        }
        return resmsg;
    }
    //获取外汇交易记录
    @PostMapping(value="/getwhjyjl")
    public List<Whjyb> getwhjyjl(String mrbzidstr){
        if (exchangerate.lastexchangeratesisready) {
            return sqlmapper.getwhjyjl(1, mrbzidstr);
        }else{
            return new ArrayList<Whjyb>();
        }
    }

    //获取外汇交易记录利润数据
    @PostMapping(value="/getwhjylrsj")
    //@RequestMapping(value = "/getwhjylrsj", method = {RequestMethod.GET,RequestMethod.POST})
    public String getwhjylrsj(String mrbzidstr){
        List<Whlrsj> whlrsjs=new ArrayList<Whlrsj>();
        List<Long> bzids=new ArrayList<Long>();
        if (exchangerate.lastexchangeratesisready) {
            List<Whjyb> whjylb=sqlmapper.getallwhjyjl(1);
            if(whjylb.size()>0) {
                Whlrsj whlrsj0 = new Whlrsj();
                whlrsj0.setBzid(0);
                whlrsj0.setBzname("所有");
                whlrsj0.setLirunlb(new ArrayList<Lirunlb>());
                whlrsjs.add(whlrsj0);
                bzids.add(0L);
                for (int i = 0; i < whjylb.size(); i++) {
                    if(i>0 && whjylb.get(i).getJyleixing()==whjylb.get(i-1).getJyleixing() &&
                            whjylb.get(i).getJyjine()==whjylb.get(i-1).getJyjine() &&
                            whjylb.get(i).getCjjine()==whjylb.get(i-1).getCjjine() &&
                            whjylb.get(i).getJybzid()==whjylb.get(i-1).getJybzid() &&
                            whjylb.get(i).getJiaoyisj()==whjylb.get(i-1).getJiaoyisj()){
                        System.out.println("疑似重复提交的数据："+new Gson().toJson(whjylb.get(i))+",,,"+new Gson().toJson(whjylb.get(i-1)));
                    }
                    //System.out.println("交易数据："+new Gson().toJson(whjylb.get(i)));
                    if (bzids.indexOf(whjylb.get(i).getJybzid()) < 0) {//如果币种没有在列表则新增
                        bzids.add(whjylb.get(i).getJybzid());
                        Whlrsj whlrsj = new Whlrsj();
                        whlrsj.setBzid(whjylb.get(i).getJybzid());
                        whlrsj.setBzname(whjylb.get(i).getJybzname());
                        whlrsj.setLirunlb(new ArrayList<Lirunlb>());
                        whlrsjs.add(whlrsj);
                    }
                    //获取币种
                    int bzindex = bzids.indexOf(whjylb.get(i).getJybzid());
                    if (whjylb.get(i).getJyleixing() == 1) {//买入开仓
                        whlrsjs.get(bzindex).setXmrhmckcjyje(whlrsjs.get(bzindex).getXmrhmckcjyje()+whjylb.get(i).getJyjine());
                        whlrsjs.get(bzindex).setXmrhmckccjje(whlrsjs.get(bzindex).getXmrhmckccjje()+whjylb.get(i).getCjjine());
                    }else if (whjylb.get(i).getJyleixing() == 2) {//卖出平仓
                        whlrsjs.get(bzindex).setXmrhmcpcjyje(whlrsjs.get(bzindex).getXmrhmcpcjyje()+whjylb.get(i).getJyjine());
                        whlrsjs.get(bzindex).setXmrhmcpccjje(whlrsjs.get(bzindex).getXmrhmcpccjje()+whjylb.get(i).getCjjine());
                    }else if (whjylb.get(i).getJyleixing() == 3) {//卖出开仓
                        whlrsjs.get(bzindex).setXmchmrkcjyje(whlrsjs.get(bzindex).getXmchmrkcjyje()+whjylb.get(i).getJyjine());
                        whlrsjs.get(bzindex).setXmchmrkccjje(whlrsjs.get(bzindex).getXmchmrkccjje()+whjylb.get(i).getCjjine());
                    }else if (whjylb.get(i).getJyleixing() == 4) {//买入平仓
                        whlrsjs.get(bzindex).setXmchmrpcjyje(whlrsjs.get(bzindex).getXmchmrpcjyje()+whjylb.get(i).getJyjine());
                        whlrsjs.get(bzindex).setXmchmrpccjje(whlrsjs.get(bzindex).getXmchmrpccjje()+whjylb.get(i).getCjjine());
                    }
                    //System.out.println(i+","+bzids.get(bzindex)+","+whlrsjs.get(bzindex).getBzname()+","+whjylb.get(i).getJiaoyisj()+",,,"+whjylb.get(i).getJyleixing()+","+whlrsjs.get(bzindex).getXmrhmckcjyje()+","+whlrsjs.get(bzindex).getXmrhmckcjyje()+","+whlrsjs.get(bzindex).getXmrhmcpcjyje()+",,,"+whlrsjs.get(bzindex).getXmchmrkcjyje()+","+whlrsjs.get(bzindex).getXmchmrkcjyje()+","+whlrsjs.get(bzindex).getXmchmrpcjyje());
                    //计算币种自己的利润
                    if(whjylb.get(i).getJyleixing() == 2 && whlrsjs.get(bzindex).getXmrhmckcjyje()>0 && whlrsjs.get(bzindex).getXmrhmckcjyje()==whlrsjs.get(bzindex).getXmrhmcpcjyje()){
                        //当先买后卖的买入跟卖出相等，即可以计算利润时，计算利润
                        float lastlirun=0;//币种上次利润
                        if(whlrsjs.get(bzindex).getLirunlb().size()>0){
                            lastlirun=whlrsjs.get(bzindex).getLirunlb().get(whlrsjs.get(bzindex).getLirunlb().size()-1).getLirun();
                        }

                        //计算利润
                        float lirun=whlrsjs.get(bzindex).getXmrhmcpccjje()-whlrsjs.get(bzindex).getXmrhmckccjje();
                        sqlmapper.updatejslirunbyid(lirun,whjylb.get(i).getId());
                        Lirunlb lirunlb=new Lirunlb();
                        lirunlb.setDate(whjylb.get(i).getJiaoyisj());
                        lirunlb.setLirun(lastlirun+lirun);
                        whlrsjs.get(bzindex).getLirunlb().add(lirunlb);
                        Lirunlb lirunlball=new Lirunlb();
                        lirunlball.setDate(whjylb.get(i).getJiaoyisj());
                        //所有币种上次利润
                        float allsclirun=0;
                        if(whlrsjs.get(0).getLirunlb().size()>0){
                            allsclirun=whlrsjs.get(0).getLirunlb().get(whlrsjs.get(0).getLirunlb().size()-1).getLirun();
                        }
                        lirunlball.setLirun(allsclirun+lirun);
                        whlrsjs.get(0).getLirunlb().add(lirunlball);
                        whlrsjs.get(bzindex).setXmrhmckcjyje(0);
                        whlrsjs.get(bzindex).setXmrhmcpcjyje(0);
                        whlrsjs.get(bzindex).setXmrhmcpccjje(0);
                        whlrsjs.get(bzindex).setXmrhmckccjje(0);
                    }else if(whjylb.get(i).getJyleixing() == 4 && whlrsjs.get(bzindex).getXmchmrkcjyje()>0 && whlrsjs.get(bzindex).getXmchmrkcjyje()==whlrsjs.get(bzindex).getXmchmrpcjyje()){
                        //当先卖后买的买入跟卖出相等，即可以计算利润时，计算利润
                        float lastlirun=0;//币种上次利润
                        if(whlrsjs.get(bzindex).getLirunlb().size()>0){
                            lastlirun=whlrsjs.get(bzindex).getLirunlb().get(whlrsjs.get(bzindex).getLirunlb().size()-1).getLirun();
                        }

                        //计算利润
                        float lirun=whlrsjs.get(bzindex).getXmchmrkccjje()-whlrsjs.get(bzindex).getXmchmrpccjje();
                        sqlmapper.updatejslirunbyid(lirun,whjylb.get(i).getId());
                        Lirunlb lirunlb=new Lirunlb();
                        lirunlb.setDate(whjylb.get(i).getJiaoyisj());
                        lirunlb.setLirun(lastlirun+lirun);
                        whlrsjs.get(bzindex).getLirunlb().add(lirunlb);
                        Lirunlb lirunlball=new Lirunlb();
                        lirunlball.setDate(whjylb.get(i).getJiaoyisj());
                        //所有币种上次利润
                        float allsclirun=0;
                        if(whlrsjs.get(0).getLirunlb().size()>0){
                            allsclirun=whlrsjs.get(0).getLirunlb().get(whlrsjs.get(0).getLirunlb().size()-1).getLirun();
                        }
                        lirunlball.setLirun(allsclirun+lirun);
                        whlrsjs.get(0).getLirunlb().add(lirunlball);
                        whlrsjs.get(bzindex).setXmchmrkcjyje(0);
                        whlrsjs.get(bzindex).setXmchmrpcjyje(0);
                        whlrsjs.get(bzindex).setXmchmrkccjje(0);
                        whlrsjs.get(bzindex).setXmchmrpccjje(0);

                    }
                }
                /*//计算总的利润
                if(whlrsjs.size()>1) {
                    List<Lirunlb> lirunlbs=new ArrayList<Lirunlb>();
                    //第一个币种直接载入
                    List<Lirunlb> templirunlbs=whlrsjs.get(1).getLirunlb();
                    if(templirunlbs.size()>0) {
                        for(int j=0;j<templirunlbs.size();j++) {
                            lirunlbs.add(templirunlbs.get(j));
                        }
                    }
                    if(whlrsjs.size()>2) {
                        for (int i = 2; i < whlrsjs.size(); i++) {
                            templirunlbs = whlrsjs.get(i).getLirunlb();
                            if (templirunlbs.size() > 0) {
                                for (int j = 0; j < templirunlbs.size(); j++) {
                                    Date datadate=templirunlbs.get(j).getDate();
                                    int datadateafterindex=-1;//总利润刚好大于数据的时间的位置
                                    for(int k=0;k<lirunlbs.size();k++){
                                        if(datadate.after(lirunlbs.get(k).getDate())){
                                            datadateafterindex=k;
                                            break;
                                        }
                                    }
                                    int datadatedengyuindex=-1;//总利润刚好等于数据的时间的位置
                                    for(int k=0;k<lirunlbs.size();k++){
                                        if(datadate.equals(lirunlbs.get(k).getDate())){
                                            datadatedengyuindex=k;
                                            break;
                                        }
                                    }
                                    int datadatebeforeindex=-1;//总利润刚好小于数据的时间的位置
                                    for(int k=lirunlbs.size()-1;k>-1;k--){
                                        if(datadate.before(lirunlbs.get(k).getDate())){
                                            datadatebeforeindex=k;
                                            break;
                                        }
                                    }
                                    if(datadatedengyuindex>-1){//刚好找到
                                        lirunlbs.get(datadatedengyuindex).setLirun(lirunlbs.get(datadatedengyuindex).getLirun()+templirunlbs.get(j).getLirun());
                                    }else{

                                    }
                                    //lirunlbs.add(templirunlbs.get(i));
                                }
                                if(j == templirunlbs.size()-1){
                                    if(templirunlbs.size()<lirunlbs.size())
                                }
                            }
                        }
                    }
                }*/
            }
        }
        return new GsonBuilder()
                //.setDateFormat("yyyy-MM-dd hh:mm:ss")//12小时制
                .setDateFormat("yyyy-MM-dd HH:mm:ss")//24小时制
                .create().toJson(whlrsjs);
    }

    //获取外汇交易单子，返回的是可供layui识别的数据,gettype:all-全部交易单子，time-从起始事件到结束事件的单子，whx-还未核销的单子
    @PostMapping(value="/getwhjyjllayui")
    //public Respdatalist getwhjyjllayui(){
    public String getwhjyjllayui(){
        if (exchangerate.lastexchangeratesisready) {
            List<Whjyb> whjybs = sqlmapper.getwhjyjl(1, "");
            Respdatalist respdatalist = new Respdatalist();
            respdatalist.setResultcode("101");
            respdatalist.setResultmsg("获取成功");
            respdatalist.setTotalcount(whjybs.size());
            respdatalist.setRows(whjybs);
            //return respdatalist;
            return new GsonBuilder()
                    //.setDateFormat("yyyy-MM-dd hh:mm:ss")//12小时制
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")//24小时制
                    .create().toJson(respdatalist);
        }else{
            return "";
        }
    }
    //获取余额大于0的外汇快照记录，gettype：all-所有快照，yedy0-余额大于0的快照
    @PostMapping(value="/getwhkuaizhaos")
    List<Whkuaizhao> getwhkuaizhaos(String gettype){
        if (exchangerate.lastexchangeratesisready) {
            List<Whkuaizhao> tempwhkuaizhaos = whkuaizhaolist;
            List<Exchangerate> templastexchangerates = lastexchangerates;
            for (int i = 0; i < tempwhkuaizhaos.size(); i++) {
                if (tempwhkuaizhaos.get(i).getBzid() == 157) {//为人民币
                    tempwhkuaizhaos.get(i).setXhmrj(100);
                } else {
                    for (int j = 0; j < templastexchangerates.size(); j++) {
                        if (tempwhkuaizhaos.get(i).getBzid() == templastexchangerates.get(j).getBzid()) {
                            tempwhkuaizhaos.get(i).setXhmrj(templastexchangerates.get(j).getXhmrj());
                            break;
                        }
                    }
                }
            }
            return tempwhkuaizhaos;
        }else{
            return new ArrayList<Whkuaizhao>();
        }
    }
    //币种设置
    @PostMapping(value="/bzset")
    Resmsg setbzseting(long bzid, String xmrhmclvrunlvalavalue, String xmrhmclvrunalavalue, String xmrhmcyhmrjalavalue,
                       String xmrhmcyhmcjalavalue, String xmchmrlvrunlvalavalue, String xmchmrlvrunalavalue,
                       String xmchmryhmrjalavalue,String xmchmryhmcjalavalue){
        Resmsg resmsg=new Resmsg();
        if (exchangerate.lastexchangeratesisready) {
            if (sqlmapper.setbzseting(bzid, xmrhmclvrunlvalavalue, xmrhmclvrunalavalue, xmrhmcyhmrjalavalue, xmrhmcyhmcjalavalue
                    , xmchmrlvrunlvalavalue, xmchmrlvrunalavalue, xmchmryhmrjalavalue, xmchmryhmcjalavalue) > 0) {
                //加载设置到币种
                Bizhong tempbizhong =exchangerate.sybizhongs.get(exchangerate.sybizhongid.indexOf(bzid));
                tempbizhong.setXmrhmclvrunalavalue(xmrhmclvrunalavalue);
                tempbizhong.setXmrhmclvrunlvalavalue(xmrhmclvrunlvalavalue);
                tempbizhong.setXmchmrlvrunalavalue(xmchmrlvrunalavalue);
                tempbizhong.setXmchmrlvrunlvalavalue(xmchmrlvrunlvalavalue);
                tempbizhong.setXmrhmcyhmrjalavalue(xmrhmcyhmrjalavalue);
                tempbizhong.setXmrhmcyhmcjalavalue(xmrhmcyhmcjalavalue);
                tempbizhong.setXmchmryhmrjalavalue(xmchmryhmrjalavalue);
                tempbizhong.setXmchmryhmcjalavalue(xmchmryhmcjalavalue);
                resmsg.setResultcode("101");
                resmsg.setResultmsg("提交成功");
            }else{
                resmsg.setResultcode("101");
                resmsg.setResultmsg("没有需要改变的");
            }
        }
        return resmsg;
    }

    /**
     * 获取站点单个负载
     */
    @GetMapping(value = "/gethost")
    public String gethost(HttpServletRequest request, HttpServletResponse response) {
        String gethost="";
        System.out.println(new Date()+"进入方法了，传入值："+request.getParameter("can"));
        gethost=hd.gethosts(request.getParameter("webname"),1);//默认从redis服务器里面取
        if(gethost.length()>0 && !gethost.equals("no")) {
            gethost=gethost.substring(0,gethost.indexOf(":"));
            return gethost;
        }else{//redis里面取不到就从nginx配置文件里面取解析出来
            String hostsstr=pubfuncs.getoneipfromnginx("itdknet").trim();
            if(hostsstr.length()>0) {
                String[] hosts =hostsstr.split(";");
                for(int i=0;i<hosts.length;i++) {
                    String onehost=hosts[i].trim();
                    onehost=onehost.substring(onehost.indexOf(" ")+1,onehost.indexOf(":")).trim();
                    if (!onehost.equals("207.148.17.218") && !onehost.equals("127.0.0.1")) {//排除云服务器的地址和本地地址
                        gethost=onehost.trim();
                        break;
                    }
                }
            }
            //pubfuncs.printerrormsg("获取的内容："+gethost);
        }
        return gethost;
        //return "127001";
    }
    /**
     * 获取每个币种的当前汇率及是否达到警报值
     */
    @GetMapping(value = "/gethlandala")
    public String gethlandala(HttpServletRequest request, HttpServletResponse response) {
        //pubfuncs.printerrormsg("进入获取每个币种的当前汇率及是否达到警报值方法");
        String alllirunmsg="";
        String allamsg="";
        String allhllist="";
        for(int k=0;k< sybizhongs.size();k++) {
            String alamsg="";
            String lirunmsg="";
            long bzid=sybizhongs.get(k).getId();
            for (int j = 0; j < lastexchangerates.size(); j++) {
                if (bzid == lastexchangerates.get(j).getBzid()) {
                    String tempbzala="";
                    float xhmrj=lastexchangerates.get(j).getXhmrj();
                    float xhmcj=lastexchangerates.get(j).getXhmcj();
                    tempbzala=pubfuncs.prabzala(sybizhongs.get(k).getXmrhmcyhmrjalavalue(), xhmrj, "先买入后卖出现汇买入价");
                    alamsg = alamsg+((alamsg.length()>0 && tempbzala.length()>0)?"，":"")+tempbzala;
                    tempbzala=pubfuncs.prabzala(sybizhongs.get(k).getXmrhmcyhmcjalavalue(), xhmcj, "先买入后卖出现汇卖出价");
                    alamsg = alamsg+((alamsg.length()>0 && tempbzala.length()>0)?"，":"")+tempbzala;
                    tempbzala=pubfuncs.prabzala(sybizhongs.get(k).getXmchmryhmrjalavalue(), xhmrj, "先卖出后买入现汇买入价");
                    alamsg = alamsg+((alamsg.length()>0 && tempbzala.length()>0)?"，":"")+tempbzala;
                    tempbzala=pubfuncs.prabzala(sybizhongs.get(k).getXmchmryhmcjalavalue(), xhmcj, "先卖出后买入现汇卖出价");
                    alamsg = alamsg+((alamsg.length()>0 && tempbzala.length()>0)?"，":"")+tempbzala;
                    for (int i = 0; i < whkuaizhaolist.size(); i++) {//外汇快照
                        if(bzid==whkuaizhaolist.get(i).getBzid()) {
                            float xmrhmcrmbcb=whkuaizhaolist.get(i).getXmrhmcrmbcb();
                            float xmrhmcjine=whkuaizhaolist.get(i).getXmrhmcjine();
                            float xmchmrrmbcb=whkuaizhaolist.get(i).getXmchmrrmbcb();
                            float xmchmrjine=whkuaizhaolist.get(i).getXmchmrjine();
                            if (whkuaizhaolist.get(i).getXmrhmcjine() > 0) {//先买入后卖出金额大于0
                                //先买后卖相比买入时的利润
                                float xmrhmcmoneyget = 0;

                                //先买后卖持仓均价
                                float xmrhmcholdwhpri = 0;
                                if(bzid>=107 && bzid<=152) {//账户外汇
                                    xmrhmcholdwhpri = (xmrhmcrmbcb * 100 / xmrhmcjine);
                                    xmrhmcmoneyget = (xmrhmcjine * xhmrj) / 100 - xmrhmcrmbcb;
                                }else if(bzid>=162 && bzid<=197) {//账户贵金属
                                    xmrhmcholdwhpri = (xmrhmcrmbcb / xmrhmcjine);
                                    xmrhmcmoneyget = (xmrhmcjine * xhmrj) - xmrhmcrmbcb;
                                }
                                //先买后卖相比买入时的涨跌幅
                                float xmrhmcxbmrzdf = (xmrhmcmoneyget / xmrhmcrmbcb) * 100;
                                //判断有否触发币种设置的警报条件
                                //console.log(onebzdata.xmrhmclvrunalavalue+","+onebzdata.xmrhmclvrunlvalavalue);
                                tempbzala=pubfuncs.prabzala(sybizhongs.get(k).getXmrhmclvrunalavalue(), xmrhmcmoneyget, "先买入后卖出利润");
                                alamsg = alamsg+((alamsg.length()>0 && tempbzala.length()>0)?"，":"")+tempbzala;
                                tempbzala=pubfuncs.prabzala(sybizhongs.get(k).getXmrhmclvrunlvalavalue(), xmrhmcxbmrzdf, "先买入后卖出利润率");
                                alamsg = alamsg+((alamsg.length()>0 && tempbzala.length()>0)?"，":"")+tempbzala;
                                lirunmsg=lirunmsg+sybizhongs.get(k).getBzname()+"：先买后卖：均：" + new DecimalFormat("0.00").format(xmrhmcholdwhpri) + "，总：" + new DecimalFormat("0.00").format(xmrhmcjine) +"，" + new DecimalFormat("0.00").format(xmrhmcrmbcb) +"，利："+new DecimalFormat("0.00").format(xmrhmcmoneyget)+"，"+new DecimalFormat("0.00").format(xmrhmcxbmrzdf)+"，买："+new DecimalFormat("0.00").format(xhmrj)+"，卖："+new DecimalFormat("0.00").format(xhmcj);
                            }
                            if (whkuaizhaolist.get(i).getXmchmrjine() > 0) {//先卖出后买入金额大于0
                                //先卖后买相比买入时的利润
                                float xmchmrmoneyget = 0;

                                //先卖后买持仓均价
                                float xmchmrholdwhpri = 0;
                                if(bzid>=107 && bzid<=152) {//账户外汇
                                    xmchmrholdwhpri = (xmchmrrmbcb * 100 / xmchmrjine);
                                    xmchmrmoneyget = xmchmrrmbcb - ((xmchmrjine * xhmcj) / 100);
                                }else if(bzid>=162 && bzid<=197) {//账户贵金属
                                    xmchmrholdwhpri = (xmchmrrmbcb / xmchmrjine);
                                    xmchmrmoneyget = xmchmrrmbcb - (xmchmrjine * xhmcj);
                                }

                                //先卖后买相比买入时的涨跌幅
                                float xmchmrxbmrzdf = (xmchmrmoneyget / xmchmrrmbcb) * 100;
                                //判断有否触发币种设置的警报条件
                                //console.log(onebzdata.xmchmrlvrunalavalue+","+onebzdata.xmchmrlvrunlvalavalue);
                                tempbzala=pubfuncs.prabzala(sybizhongs.get(k).getXmchmrlvrunalavalue(), xmchmrmoneyget, "先卖出后买入利润");
                                alamsg = alamsg+((alamsg.length()>0 && tempbzala.length()>0)?"，":"")+tempbzala;
                                tempbzala=pubfuncs.prabzala(sybizhongs.get(k).getXmchmrlvrunlvalavalue(), xmchmrxbmrzdf, "先卖出后买入利润率");
                                alamsg = alamsg+((alamsg.length()>0 && tempbzala.length()>0)?"，":"")+tempbzala;
                                lirunmsg=lirunmsg+(lirunmsg.length()>0?"，":"")+"先卖后买：均：" + new DecimalFormat("0.00").format(xmchmrholdwhpri) + "，总：" + new DecimalFormat("0.00").format(xmchmrjine) + "，" + new DecimalFormat("0.00").format(xmchmrrmbcb) +"，利："+new DecimalFormat("0.00").format(xmchmrmoneyget)+"，"+new DecimalFormat("0.00").format(xmchmrxbmrzdf)+"，买："+new DecimalFormat("0.00").format(xhmrj)+"，卖："+new DecimalFormat("0.00").format(xhmcj);
                            }

                        }
                    }
                    allhllist=allhllist+sybizhongs.get(k).getBzname()+"："+new DecimalFormat("0.00").format(xhmrj)+","+new DecimalFormat("0.00").format(xhmcj)+"，涨跌：1："+new DecimalFormat("0.00").format(sybizhongs.get(k).getZdf1day())+"，2："+new DecimalFormat("0.00").format(sybizhongs.get(k).getZdf2day())+"，3："+new DecimalFormat("0.00").format(sybizhongs.get(k).getZdf3day())+"，时间："+new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss").format(sybizhongs.get(k).getFbsj())+"\n";
                    break;
                }
            }
            if(alamsg.length()>0){
                allamsg=allamsg+(allamsg.length()>0?"；":"")+sybizhongs.get(k).getBzname()+"："+alamsg+"，涨跌：1："+new DecimalFormat("0.00").format(sybizhongs.get(k).getZdf1day())+"，2："+new DecimalFormat("0.00").format(sybizhongs.get(k).getZdf2day())+"，3："+new DecimalFormat("0.00").format(sybizhongs.get(k).getZdf3day())+"\n";
            }
            if(lirunmsg.length()>0) {
                alllirunmsg=alllirunmsg+(alllirunmsg.length()>0?"；":"")+sybizhongs.get(k).getBzname()+"：" +lirunmsg+"，涨跌：1："+new DecimalFormat("0.00").format(sybizhongs.get(k).getZdf1day())+"，2："+new DecimalFormat("0.00").format(sybizhongs.get(k).getZdf2day())+"，3："+new DecimalFormat("0.00").format(sybizhongs.get(k).getZdf3day())+"\n";
            }
        }
        if(allamsg.length()>0){
            allamsg=allamsg+"，触发临界值警报。";
        }

        return alllirunmsg+allamsg+allhllist;
    }
}
