package com.hcsk.financialt.publicfuncs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hcsk.financialt.configure.para;
import com.hcsk.financialt.masterdb.dao.sqlMapper;
import com.hcsk.financialt.pojo.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;



@Component
public class exchangerate {
    //累计的汇率信息（至少最近30天）
    //public static List<Exchangerate> manyexchangerates=new ArrayList<Exchangerate>();
    //上一次的汇率信息
    public static List<Exchangerate> lastexchangerates=new ArrayList<Exchangerate>();
    //上一次的汇率已经加载完毕
    public static boolean lastexchangeratesisready=false;
    //可看不可投资币种
    public static List<Bizhong> kkbktzbizhongs=new ArrayList<Bizhong>();
    public static String kkbktzbizhongidstr="";

    //可看可投资币种
    public static List<Bizhong> kkktzbizhongs=new ArrayList<Bizhong>();
    public static String kkktzbizhongidstr="";
    public static String kkktzbizhongnamestr="";
    //所有可看币种
    public static List<Bizhong> sykkbizhongs=new ArrayList<Bizhong>();
    public static String sykkbizhongidstr="";
    //所有币种
    public static List<Bizhong> sybizhongs=new ArrayList<Bizhong>();
    //所有币种的id累加字符串
    public static String sybizhongidstr="";
    //所有币种名称集合
    public static List<String> sybizhongname=new ArrayList<String>();
    //所有币种名称2集合
    public static List<String> sybizhongname2=new ArrayList<String>();
    //所有币种名称3集合
    public static List<String> sybizhongname3=new ArrayList<String>();
    //所有币种的主键集合
    public static List<Long> sybizhongid=new ArrayList<Long>();
    //外汇快照是否在使用中，防止冲突
    public static boolean whkuaizhaoisinuse=false;
    //外汇快照
    public static List<Whkuaizhao> whkuaizhaolist=new ArrayList<Whkuaizhao>();
    //清除5天前汇率到哪一天
    public static String cleartodate="";
    //清除11个交易日前的汇率秒数据执行到哪天
    public static String delete11dayagotodate="";
    //账户外汇更新到的时间
    public static Date zhwhgxddsj=new Date();
    //结售汇更新到的时间
    public static Date jshgxddsj=new Date();
    //外汇汇率信息未解析字符串
    public static String waihuinopstr="";
    //账户外汇汇率信息未解析字符串
    public static String zhwhnopstr="";
    //账户贵金属价格信息未解析字符串
    public static String zhgjsnopstr="";
    @Autowired
    public webdo webdoa;
    @Autowired
    sqlMapper sqlmapper;

    //拉取内容开始

    //获取即时银行外汇汇率报文,当date,time不为空的时候代表取某个时间的汇率，gettype:1-自己获取，2-通过设定的网址获取
    public String getwhcontent(String bankname,String date,String time,int gettype){
        String resultvalue="";
        if(gettype==1) {
            if (bankname.equals("icbc")) {
                /*if (date.length() > 0) {
                    resultvalue= webdoa.gethtmlfromurl("http://www.icbc.com.cn/ICBCDynamicSite/Optimize/Quotation/QuotationListIframe.aspx?variety=1&currency=&publishDate=" + date, "GET");
                } else {
                    resultvalue= webdoa.gethtmlfromurl("http://www.icbc.com.cn/ICBCDynamicSite/Optimize/Quotation/QuotationListIframe.aspx", "GET");
                }*/
                resultvalue= webdoa.gethtmlfromurl("https://mybank.icbc.com.cn/servlet/AsynGetDataServlet?oratype=1&Area_code=0200&ajaxsend=1&tranCode=A00525", "POST");
            }
        }else if(gettype==2) {
            /*if (bankname.equals("icbc")) {
                if (date.length() > 0) {
                    resultvalue= webdoa.gethtmlfromurldecode(para.urlgetinfurl+"/geturlr?gu="+endecode1.encodestr("http://www.icbc.com.cn/ICBCDynamicSite/Optimize/Quotation/QuotationListIframe.aspx?variety=1&currency=&publishDate=" + date)+"&gm=1", "POST");
                } else {
                    resultvalue= webdoa.gethtmlfromurldecode(para.urlgetinfurl+"/geturlr?gu="+endecode1.encodestr("http://www.icbc.com.cn/ICBCDynamicSite/Optimize/Quotation/QuotationListIframe.aspx")+"&gm=1", "POST");
                }
            }*/
            if (bankname.equals("icbc")) {
                resultvalue= webdoa.gethtmlfromurldecode(para.urlgetinfurl+"/getnophlstr?gt=1", "POST");
            }

        }
        pubfuncs.printerrormsg(gettype+"，，，结售汇返回值："+resultvalue);
        return resultvalue;
    }

    //获取即时银行账户外汇汇率报文,当date,time不为空的时候代表取某个时间的汇率，gettype:1-自己获取，2-通过设定的网址获取
    public String getzhwhcontent(String bankname,int gettype){
        String resultvalue="";
        if(gettype==1) {
            if (bankname.equals("icbc")) {
                resultvalue= webdoa.gethtmlfromurlzhwh("https://mybank.icbc.com.cn/ctp/ctpservlet/EbdpAjaxServlet?"
                                + "trademodeNew=1&cmdFlag=1&selfRefreshFlag=4&ajaxsend=1&isFirstTime=0&proIdsIn=130060000931%7C130060000932%7C130060000933%7C130060000934%7C130060000935%7C130060001311%7C130060085591%7C130060085592%7C130060085593%7C130060085594&tranCode=A00513"
                        , "POST");
            }
        }else if(gettype==2) {
            /*if (bankname.equals("icbc")) {
                resultvalue= webdoa.gethtmlfromurldecode(para.urlgetinfurl+"/geturlr?gu="+endecode1.encodestr("https://mybank.icbc.com.cn/ctp/ctpservlet/EbdpAjaxServlet?"
                                + "trademodeNew=1&cmdFlag=1&selfRefreshFlag=4&ajaxsend=1&isFirstTime=0&proIdsIn=130060000931%7C130060000932%7C130060000933%7C130060000934%7C130060000935%7C130060001311%7C130060085591%7C130060085592%7C130060085593%7C130060085594&tranCode=A00513")+"&gm=2"
                        , "POST");
            }*/
            if (bankname.equals("icbc")) {
                resultvalue= webdoa.gethtmlfromurldecode(para.urlgetinfurl+"/getnophlstr?gt=2", "POST");
            }

        }
        pubfuncs.printerrormsg(gettype+"，，，账户外汇返回值："+resultvalue);
        return resultvalue;
    }

    //获取即时银行账户贵金属价格报文，gettype:1-自己获取，2-通过设定的网址获取
    public String getzhgjscontent(String bankname,int gettype){
        String resultvalue="";
        if(gettype==1) {
            if (bankname.equals("icbc")) {
                resultvalue= webdoa.gethtmlfromurl("https://mybank.icbc.com.cn/servlet/AsynGetDataServlet?Area_code=0200&trademode=1&proIdsIn=130060000043|130060000044|130060000041|130060000123|130060000045|130060000046|130060000042|130060000125&isFirstTime=0&tranCode=A00500", "POST");
            }
        }else if(gettype==2) {
            if (bankname.equals("icbc")) {
                resultvalue= webdoa.gethtmlfromurldecode(para.urlgetinfurl+"/getnophlstr?gt=3", "POST");
            }

        }
        //pubfuncs.printerrormsg(gettype+"，，，账户贵金属返回值："+resultvalue);
        return resultvalue;
    }

    //拉取内容结束


    //解析内容开始

    //主动拉取结售汇网页解析网页版银行外汇汇率报文
    public List<Exchangerate> pracontent(String bankname, String content){
        List<Exchangerate> exchangerates=new ArrayList<Exchangerate>();
        try {
            /*//6.Jsoup解析html
            Document document = Jsoup.parse(html);
            //像js一样，通过标签获取title
            System.out.println(document.getElementsByTag("title").first());
            //像js一样，通过id 获取文章列表元素对象
            Element postList = document.getElementById("post_list");
            //像js一样，通过class 获取列表下的所有博客
            Elements postItems = postList.getElementsByClass("post_item");
            //循环处理每篇博客
            for (Element postItem : postItems) {
                //像jquery选择器一样，获取文章标题元素
                Elements titleEle = postItem.select(".post_item_body a[class='titlelnk']");
                System.out.println("文章标题:" + titleEle.text());;
                System.out.println("文章地址:" + titleEle.attr("href"));
                //像jquery选择器一样，获取文章作者元素
                Elements footEle = postItem.select(".post_item_foot a[class='lightblue']");
                System.out.println("文章作者:" + footEle.text());;
                System.out.println("作者主页:" + footEle.attr("href"));
                System.out.println("*********************************");yyyyMMddHHmmss
            }*/
            DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            DateFormat format2 = new SimpleDateFormat("yyyyMMdd");
            //6.Jsoup解析html
            Document document = Jsoup.parse(content);
            //Elements elements =document.getElementsByTag("table");
            Elements elements = document.getElementsByClass("tableDataTable");
            if (elements.size() == 1) {
                Elements trs = elements.get(0).getElementsByTag("tr");
                Elements tds = trs.get(0).getElementsByTag("td");
                //pubfuncs.printerrormsg("解析的数据："+tds.get(0).text()+","+tds.get(1).text()+","+tds.get(2).text()+","+tds.get(3).text()+","+tds.get(4).text()+","+tds.get(5).text());
                if (tds.size() == 6) {
                    if ((tds.get(0).text() + "," + tds.get(1).text() + "," + tds.get(2).text() + "," + tds.get(3).text() + "," + tds.get(4).text() + "," + tds.get(5).text()).equals("币种,现汇买入价,现钞买入价,现汇卖出价,现钞卖出价,发布时间")) {
                        if (trs.size() > 1) {
                            for (int i = 1; i < trs.size(); i++) {
                                tds = trs.get(i).getElementsByTag("td");
                                if (tds.size() == 6) {
                                    Exchangerate exchangerate = new Exchangerate();
                                    int bzidindex = sybizhongname.indexOf(tds.get(0).text());
                                    String bzname=tds.get(0).text();
                                    if (bzidindex < 0) {
                                        String bizhong3=bzname.indexOf("账户")>-1?bzname.substring(0,bzname.indexOf("账户")+3):bzname.substring(0,bzname.length()-1);
                                        pubfuncs.printerrormsg("比较："+bizhong3);
                                        bzidindex = sybizhongname3.indexOf(bizhong3);
                                    }
                                    if (bzidindex < 0) {
                                        pubfuncs.printerrormsg("喔噢，找不到的币种：" + tds.get(0).text());
                                    }else {
                                        exchangerate.setBzid(sybizhongid.get(bzidindex));
                                        exchangerate.setOrgid(2);
                                        //有获取到汇率数据
                                        boolean havedata = false;
                                        try {
                                            exchangerate.setXhmrj(Float.parseFloat(tds.get(1).text()));
                                            havedata = true;
                                        } catch (Exception ee) {
                                        }
                                        try {
                                            exchangerate.setXcmrj(Float.parseFloat(tds.get(2).text()));
                                            havedata = true;
                                        } catch (Exception ee) {
                                        }
                                        try {
                                            exchangerate.setXhmcj(Float.parseFloat(tds.get(3).text()));
                                            havedata = true;
                                        } catch (Exception ee) {
                                        }
                                        try {
                                            exchangerate.setXcmcj(Float.parseFloat(tds.get(4).text()));
                                            havedata = true;
                                        } catch (Exception ee) {
                                        }
                                        try {
                                            exchangerate.setFbsj(format.parse(tds.get(5).text()));
                                            exchangerate.setFbsjdate(Integer.parseInt(format2.format(exchangerate.getFbsj())));
                                        } catch (Exception ee) {
                                            pubfuncs.printerrormsg("哇，工商银行汇率网页格式变了，赶紧解决哦，原来日期格式是这样的：yyyy年MM月dd日 HH:mm:ss，现在变成了：" + tds.get(5).text());
                                        }
                                        exchangerates.add(exchangerate);
                                    }
                                } else {
                                    if (!trs.get(i).text().equals("没有找到满足条件的记录"))
                                        pubfuncs.printerrormsg("哇，工商银行汇率网页读取有异常，该行识别不出，赶紧解决哦：" + trs.get(i).text() + ",,," + content);
                                }
                            }
                        } else {
                            pubfuncs.printerrormsg("啊哈，" + bankname + "银行获取的汇率报文里面没有数据，请赶紧处理");
                        }
                    } else {
                        pubfuncs.printerrormsg("哇，工商银行汇率网页格式变了，赶紧解决哦，原来数据表表头是这样的：币种,现汇买入价,现钞买入价,现汇卖出价,现钞卖出价,发布时间，现在变成了：" + tds.get(0).text() + "," + tds.get(1).text() + "," + tds.get(2).text() + "," + tds.get(3).text() + "," + tds.get(4).text() + "," + tds.get(5).text());
                    }
                } else {
                    pubfuncs.printerrormsg("哇，工商银行汇率网页格式变了，赶紧解决哦，原来数据表表头个数有6个，现在个数变成了：" + tds.size());
                }
            } else {
                pubfuncs.printerrormsg("哇，工商银行汇率网页格式变了，赶紧解决哦，原来类型为tableDataTable的数据表只有1个，现在个数：" + elements.size());
            }
        }catch (Exception ee){
            pubfuncs.printerrormsg("解析外汇返回字串出错："+ee.getMessage()+"，"+ee.getStackTrace()+"，请赶紧处理");
        }
        return exchangerates;
    }

    //主动拉取结售汇接口解析银行账户外汇汇率报文
    /*
       {"TranErrorCode":"","rf":[{"rmbmdrat":"656.17","standBuyRate":"656.17","rmbType":"","sellcPrice":"657.61","rmbebrat":"654.86","sellePrice":"657.61","rmbcbrat":"649.61","proId":"130060000016","currType":"014","currTypeName":"美元","rmbTypeName":"","standSellRate":"656.17"},{"rmbmdrat":"791.87","standBuyRate":"791.87","rmbType":"","sellcPrice":"794.96","rmbebrat":"789.10","sellePrice":"794.96","rmbcbrat":"766.53","proId":"130060000016","currType":"038","currTypeName":"欧元","rmbTypeName":"","standSellRate":"791.87"},{"rmbmdrat":"879.96","standBuyRate":"879.96","rmbType":"","sellcPrice":"883.39","rmbebrat":"876.88","sellePrice":"883.39","rmbcbrat":"851.80","proId":"130060000016","currType":"012","currTypeName":"英镑","rmbTypeName":"","standSellRate":"879.96"},{"rmbmdrat":"6.2836","standBuyRate":"6.2836","rmbType":"","sellcPrice":"6.3081","rmbebrat":"6.2616","sellePrice":"6.3081","rmbcbrat":"6.0825","proId":"130060000016","currType":"027","currTypeName":"日元","rmbTypeName":"","standSellRate":"6.2836"},{"rmbmdrat":"506.79","standBuyRate":"506.79","rmbType":"","sellcPrice":"508.77","rmbebrat":"505.02","sellePrice":"508.77","rmbcbrat":"490.57","proId":"130060000016","currType":"028","currTypeName":"加拿大元","rmbTypeName":"","standSellRate":"506.79"},{"rmbmdrat":"483.60","standBuyRate":"483.60","rmbType":"","sellcPrice":"485.49","rmbebrat":"481.91","sellePrice":"485.49","rmbcbrat":"468.12","proId":"130060000016","currType":"029","currTypeName":"澳大利亚元","rmbTypeName":"","standSellRate":"483.60"},{"rmbmdrat":"84.64","standBuyRate":"84.64","rmbType":"","sellcPrice":"84.83","rmbebrat":"84.48","sellePrice":"84.83","rmbcbrat":"83.79","proId":"130060000016","currType":"013","currTypeName":"港币","rmbTypeName":"","standSellRate":"84.64"},{"rmbmdrat":"463.12","standBuyRate":"463.12","rmbType":"","sellcPrice":"464.93","rmbebrat":"461.50","sellePrice":"464.93","rmbcbrat":"448.30","proId":"130060000016","currType":"087","currTypeName":"新西兰元","rmbTypeName":"","standSellRate":"463.12"},{"rmbmdrat":"490.58","standBuyRate":"490.58","rmbType":"","sellcPrice":"492.49","rmbebrat":"488.86","sellePrice":"492.49","rmbcbrat":"474.88","proId":"130060000016","currType":"018","currTypeName":"新加坡元","rmbTypeName":"","standSellRate":"490.58"},{"rmbmdrat":"21.72","standBuyRate":"21.72","rmbType":"","sellcPrice":"21.80","rmbebrat":"21.64","sellePrice":"21.80","rmbcbrat":"20.97","proId":"130060000016","currType":"084","currTypeName":"泰国铢","rmbTypeName":"","standSellRate":"21.72"},{"rmbmdrat":"0.5940","standBuyRate":"0.5940","rmbType":"","sellcPrice":"0.5963","rmbebrat":"0.5919","sellePrice":"0.5963","rmbcbrat":"0.5750","proId":"130060000016","currType":"103","currTypeName":"韩元","rmbTypeName":"","standSellRate":"0.5940"},{"rmbmdrat":"8.64","standBuyRate":"8.64","rmbType":"","sellcPrice":"8.67","rmbebrat":"8.61","sellePrice":"8.67","rmbcbrat":"8.34","proId":"130060000016","currType":"070","currTypeName":"卢布","rmbTypeName":"","standSellRate":"8.64"},{"rmbmdrat":"729.73","standBuyRate":"729.73","rmbType":"","sellcPrice":"732.58","rmbebrat":"727.18","sellePrice":"732.58","rmbcbrat":"706.38","proId":"130060000016","currType":"015","currTypeName":"瑞士法郎","rmbTypeName":"","standSellRate":"729.73"},{"rmbmdrat":"77.34","standBuyRate":"77.34","rmbType":"","sellcPrice":"77.64","rmbebrat":"77.07","sellePrice":"77.64","rmbcbrat":"74.87","proId":"130060000016","currType":"021","currTypeName":"瑞典克朗","rmbTypeName":"","standSellRate":"77.34"},{"rmbmdrat":"82.17","standBuyRate":"82.17","rmbType":"","sellcPrice":"82.35","rmbebrat":"82.01","sellePrice":"82.35","rmbcbrat":"81.35","proId":"130060000016","currType":"081","currTypeName":"澳门元","rmbTypeName":"","standSellRate":"82.17"},{"rmbmdrat":"74.45","standBuyRate":"74.45","rmbType":"","sellcPrice":"74.74","rmbebrat":"74.19","sellePrice":"74.74","rmbcbrat":"72.07","proId":"130060000016","currType":"023","currTypeName":"挪威克朗","rmbTypeName":"","standSellRate":"74.45"},{"rmbmdrat":"106.39","standBuyRate":"106.39","rmbType":"","sellcPrice":"106.80","rmbebrat":"106.02","sellePrice":"106.80","rmbcbrat":"102.99","proId":"130060000016","currType":"022","currTypeName":"丹麦克朗","rmbTypeName":"","standSellRate":"106.39"},{"rmbmdrat":"42.90","standBuyRate":"42.90","rmbType":"","sellcPrice":"43.07","rmbebrat":"42.75","sellePrice":"43.07","rmbcbrat":"40.67","proId":"130060000016","currType":"088","currTypeName":"南非 兰特","rmbTypeName":"","standSellRate":"42.90"}],"TranErrorDisplayMsg":""}
    */
    public List<Exchangerate> prajshcontent(String bankname, String content){
        List<Exchangerate> exchangerates=new ArrayList<Exchangerate>();
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat format2 = new SimpleDateFormat("yyyyMMdd");
            Icbcjshrespmsg icbcjshrespmsg = null;
            //返回的是json，所以开始对json进行解析
            try {
                icbcjshrespmsg = new Gson().fromJson(content, Icbcjshrespmsg.class);
            } catch (Exception ee) {
                pubfuncs.printerrormsg("解析工银返回的结售汇信息出错：" + ee.getMessage());
            }
            if (icbcjshrespmsg != null && icbcjshrespmsg.getRf().size()>0) {
                List<Icbcjshresprf> rfs = icbcjshrespmsg.getRf();
                if (rfs.size() > 0) {
                    for (int i = 0; i < rfs.size(); i++) {
                        Exchangerate exchangerate = new Exchangerate();
                        int bzidindex = sybizhongname2.indexOf(rfs.get(i).getCurrTypeName());
                        String bzname=rfs.get(i).getCurrTypeName();
                        if (bzidindex < 0) {
                            String bizhong3=bzname.indexOf("账户")>-1?bzname.substring(0,bzname.indexOf("账户")+3):bzname.substring(0,bzname.length()-1);
                            pubfuncs.printerrormsg("比较："+bizhong3);
                            bzidindex = sybizhongname3.indexOf(bizhong3);
                        }
                        if (bzidindex < 0) {
                            pubfuncs.printerrormsg("喔噢，找不到的币种：" + rfs.get(i).getCurrTypeName());
                        }else {
                            exchangerate.setBzid(sybizhongid.get(bzidindex));
                            exchangerate.setOrgid(2);
                            exchangerate.setXhmrj(rfs.get(i).getRmbebrat());
                            exchangerate.setXcmrj(rfs.get(i).getRmbcbrat());
                            exchangerate.setXhmcj(rfs.get(i).getSellePrice());
                            exchangerate.setXcmcj(rfs.get(i).getSellcPrice());
                            exchangerate.setFbsj(new Date());
                            exchangerate.setFbsjdate(Integer.parseInt(format2.format(exchangerate.getFbsj())));
                            exchangerates.add(exchangerate);
                        }
                    }
                } else {
                    pubfuncs.printerrormsg("哇，工商银行汇率结售汇返回信息格式变了，赶紧解决哦，找不到rf");
                }
            } else {
                pubfuncs.printerrormsg("哇，工商银行结售汇返回信息格式变了，赶紧解决哦，找不到rf或返回null");
            }
        }catch (Exception ee){
            pubfuncs.printerrormsg("解析结售汇返回字串出错："+ee.getMessage()+"，"+ee.getStackTrace()+"，请赶紧处理");
        }
        return exchangerates;
    }

    //主动拉取账户外汇接口解析银行账户外汇汇率报文
    /*
       {"refreshInterval":"60000","rf":[{"updown_d":"2","buyRate":"792.40","riseSign":"2","highPrice":"795.10","proID":"130060000931","quoteTime":"15:23:08","quoteDate":"2020-07-01","currType":"038","middPrice":"793.20","openprice_dr":"-0.11%","openprice_dv":"-0.88","sellRate":"794.00","lowPrice":"792.51","updown_y":"1","FIELD1":"781550000000","FIELD2":"794.08","proName":"账户欧元","openprice_yr":"+1.49%","sswr":"2","isCollected":"0"},{"updown_d":"1","buyRate":"874.55","riseSign":"2","highPrice":"876.25","proID":"130060000932","quoteTime":"15:23:08","quoteDate":"2020-07-01","currType":"012","middPrice":"875.35","openprice_dr":"+0.16%","openprice_dv":"+1.43","sellRate":"876.15","lowPrice":"873.04","updown_y":"2","FIELD1":"922130000000","FIELD2":"873.92","proName":"账户英镑","openprice_yr":"-5.07%","sswr":"2","isCollected":"0"},{"updown_d":"1","buyRate":"487.35","riseSign":"2","highPrice":"488.75","proID":"130060000933","quoteTime":"15:23:08","quoteDate":"2020-07-01","currType":"029","middPrice":"488.15","openprice_dr":"+0.24%","openprice_dv":"+1.17","sellRate":"488.95","lowPrice":"486.56","updown_y":"2","FIELD1":"489440000000","FIELD2":"486.98","proName":"账户澳大利亚元","openprice_yr":"-0.26%","sswr":"2","isCollected":"0"},{"updown_d":"1","buyRate":"520.58","riseSign":"1","highPrice":"521.43","proID":"130060000934","quoteTime":"15:23:06","quoteDate":"2020-07-01","currType":"028","middPrice":"521.38","openprice_dr":"+0.45%","openprice_dv":"+2.32","sellRate":"522.18","lowPrice":"518.85","updown_y":"2","FIELD1":"536950000000","FIELD2":"519.06","proName":"账户加拿大元","openprice_yr":"-2.90%","sswr":"2","isCollected":"0"},{"updown_d":"2","buyRate":"745.06","riseSign":"2","highPrice":"746.91","proID":"130060000935","quoteTime":"15:23:04","quoteDate":"2020-07-01","currType":"015","middPrice":"745.86","openprice_dr":"-0.06%","openprice_dv":"-0.46","sellRate":"746.66","lowPrice":"745.19","updown_y":"1","FIELD1":"718890000000","FIELD2":"746.32","proName":"账户瑞士法郎","openprice_yr":"+3.75%","sswr":"2","isCollected":"0"},{"updown_d":"1","buyRate":"6.5571","riseSign":"1","highPrice":"6.5741","proID":"130060001311","quoteTime":"15:23:07","quoteDate":"2020-07-01","currType":"027","middPrice":"6.5651","openprice_dr":"+0.15%","openprice_dv":"+0.0100","sellRate":"6.5731","lowPrice":"6.5321","updown_y":"1","FIELD1":"6408000000","FIELD2":"6.5551","proName":"账户日元","openprice_yr":"+2.45%","sswr":"4","isCollected":"0"},{"updown_d":"1","buyRate":"455.52","riseSign":"2","highPrice":"456.71","proID":"130060085591","quoteTime":"15:23:07","quoteDate":"2020-07-01","currType":"087","middPrice":"456.32","openprice_dr":"+0.20%","openprice_dv":"+0.92","sellRate":"457.12","lowPrice":"455.08","updown_y":"2","FIELD1":"469530000000","FIELD2":"455.40","proName":"账户新西兰元","openprice_yr":"-2.81%","sswr":"2","isCollected":"0"},{"updown_d":"1","buyRate":"506.43","riseSign":"1","highPrice":"507.33","proID":"130060085592","quoteTime":"15:23:02","quoteDate":"2020-07-01","currType":"018","middPrice":"507.23","openprice_dr":"+0.09%","openprice_dv":"+0.48","sellRate":"508.03","lowPrice":"506.61","updown_y":"2","FIELD1":"517810000000","FIELD2":"506.75","proName":"账户新加坡元","openprice_yr":"-2.04%","sswr":"2","isCollected":"0"},{"updown_d":"1","buyRate":"73.338","riseSign":"1","highPrice":"73.709","proID":"130060085593","quoteTime":"15:23:07","quoteDate":"2020-07-01","currType":"023","middPrice":"73.638","openprice_dr":"+0.64%","openprice_dv":"+0.466","sellRate":"73.938","lowPrice":"73.145","updown_y":"2","FIELD1":"79197000000","FIELD2":"73.172","proName":"账户挪威克朗","openprice_yr":"-7.02%","sswr":"3","isCollected":"0"},{"updown_d":"1","buyRate":"75.622","riseSign":"2","highPrice":"76.020","proID":"130060085594","quoteTime":"15:23:08","quoteDate":"2020-07-01","currType":"021","middPrice":"75.922","openprice_dr":"+0.00%","openprice_dv":"+0.002","sellRate":"76.222","lowPrice":"75.630","updown_y":"1","FIELD1":"74407000000","FIELD2":"75.920","proName":"账户瑞典克朗","openprice_yr":"+2.04%","sswr":"3","isCollected":"0"}],"TranErrorCode":"","sysdate":"2020-07-01 15:23:12","TranErrorDisplayMsg":""}
    */
    public List<Exchangerate> prazhwhcontent(String bankname, String content){
        List<Exchangerate> exchangerates=new ArrayList<Exchangerate>();
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat format2 = new SimpleDateFormat("yyyyMMdd");
            Icbczhwhrespmsg icbczhwhrespmsg = null;
            //返回的是json，所以开始对json进行解析
            try {
                icbczhwhrespmsg = new Gson().fromJson(content, Icbczhwhrespmsg.class);
            } catch (Exception ee) {
                pubfuncs.printerrormsg("解析工银返回的账户外汇信息出错：" + ee.getMessage());
            }
            if (icbczhwhrespmsg != null && icbczhwhrespmsg.getRefreshInterval() == 60000) {
                List<Icbczhwhresprf> rfs = icbczhwhrespmsg.getRf();
                if (rfs.size() > 0) {
                    for (int i = 0; i < rfs.size(); i++) {
                        Exchangerate exchangerate = new Exchangerate();
                        int bzidindex = sybizhongname.indexOf(rfs.get(i).getProName());
                        String bzname=rfs.get(i).getProName();
                        if (bzidindex < 0) {
                            String bizhong3=bzname.indexOf("账户")>-1?bzname.substring(0,bzname.indexOf("账户")+3):bzname.substring(0,bzname.length()-1);
                            pubfuncs.printerrormsg("比较："+bizhong3);
                            bzidindex = sybizhongname3.indexOf(bizhong3);
                        }
                        if (bzidindex < 0) {
                            String bizhong3=rfs.get(i).getProName().indexOf("账户")>-1?rfs.get(i).getProName().substring(0,rfs.get(i).getProName().indexOf("账户")+3):rfs.get(i).getProName().substring(0,rfs.get(i).getProName().length()-1);
                            pubfuncs.printerrormsg("比较："+bizhong3);
                            bzidindex = sybizhongname3.indexOf(bizhong3);
                        }
                        if (bzidindex < 0) {
                            pubfuncs.printerrormsg("喔噢，找不到的币种：" + rfs.get(i).getProName());
                        }else {
                            exchangerate.setBzid(sybizhongid.get(bzidindex));
                            exchangerate.setOrgid(2);
                            exchangerate.setXhmrj(rfs.get(i).getBuyRate());
                            exchangerate.setXhmcj(rfs.get(i).getSellRate());
                            try {
                                exchangerate.setFbsj(format.parse(rfs.get(i).getQuoteDate() + " " + rfs.get(i).getQuoteTime()));
                                exchangerate.setFbsjdate(Integer.parseInt(format2.format(exchangerate.getFbsj())));
                            } catch (Exception ee) {
                                pubfuncs.printerrormsg("哇，工商银行汇率网页格式变了，赶紧解决哦，原来日期格式是这样的：yyyy-MM-dd HH:mm:ss，现在变成了：" + rfs.get(i).getQuoteDate() + " " + rfs.get(i).getQuoteTime());
                            }
                            exchangerates.add(exchangerate);
                        }
                    }
                } else {
                    pubfuncs.printerrormsg("哇，工商银行汇率账户外汇返回信息格式变了，赶紧解决哦，找不到rf");
                }
            } else {
                pubfuncs.printerrormsg("哇，工商银行账户外汇返回信息格式变了，赶紧解决哦，原来可能为空或刷新时间不为60000毫秒");
            }
        }catch (Exception ee){
            pubfuncs.printerrormsg("解析账户外汇返回字串出错："+ee.getMessage()+"，"+ee.getStackTrace()+"，请赶紧处理");
        }
        return exchangerates;
    }

    //浏览器推送解析网页版外汇数据
    public List<Exchangerate> prawebwhcontent(String bankname, String content){
            List<Exchangerate> exchangerates=new ArrayList<Exchangerate>();
        System.out.println("传入初始值："+content);
        /*//6.Jsoup解析html
        Document document = Jsoup.parse(html);
        //像js一样，通过标签获取title
        System.out.println(document.getElementsByTag("title").first());
        //像js一样，通过id 获取文章列表元素对象
        Element postList = document.getElementById("post_list");
        //像js一样，通过class 获取列表下的所有博客
        Elements postItems = postList.getElementsByClass("post_item");
        //循环处理每篇博客
        for (Element postItem : postItems) {
            //像jquery选择器一样，获取文章标题元素
            Elements titleEle = postItem.select(".post_item_body a[class='titlelnk']");
            System.out.println("文章标题:" + titleEle.text());;
            System.out.println("文章地址:" + titleEle.attr("href"));
            //像jquery选择器一样，获取文章作者元素
            Elements footEle = postItem.select(".post_item_foot a[class='lightblue']");
            System.out.println("文章作者:" + footEle.text());;
            System.out.println("作者主页:" + footEle.attr("href"));
            System.out.println("*********************************");yyyyMMddHHmmss
        }*/
        //content="<table width=\"100%\" align=\"center\" class=\"lst\" id=\"closeSellTable\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\t\t\t\t\t<tbody><tr id=\"header\" style=\"height: 50px;\">\t\t\t\t\t\t<th width=\"15%\" style=\"text-align: left; padding-left: 20px;\">币种</th>\t\t\t\t\t\t<th width=\"20%\">银行买入价（汇）</th>\t\t\t\t\t\t<th width=\"20%\">银行买入价（钞）</th>\t\t\t\t\t\t<th width=\"20%\">银行卖出价（汇）</th>\t\t\t\t\t\t<th width=\"20%\">银行卖出价（钞）</th>\t\t\t\t\t\t<th width=\"10.3%\" style=\"text-align: center;\">操作</th>\t\t\t\t\t</tr>\t\t\t<tr height=\"19\" class=\"contentStyle\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_014\" style=\"text-align: left; padding-left: 15px;\">美元</td><td align=\"center\" id=\"rmbmdrat_014\">698.68</td><td align=\"center\" id=\"rmbebrat_014\">693.08</td><td align=\"center\" id=\"sellcPrice_014\">701.62</td><td align=\"center\" id=\"sellePrice_014\">701.62</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('014','美元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_038\" style=\"text-align: left; padding-left: 15px;\">欧元</td><td align=\"center\" id=\"rmbmdrat_038\">794.71</td><td align=\"center\" id=\"rmbebrat_038\">771.98</td><td align=\"center\" id=\"sellcPrice_038\">800.61</td><td align=\"center\" id=\"sellePrice_038\">800.61</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('038','欧元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_012\" style=\"text-align: left; padding-left: 15px;\">英镑</td><td align=\"center\" id=\"rmbmdrat_012\">876.96</td><td align=\"center\" id=\"rmbebrat_012\">851.88</td><td align=\"center\" id=\"sellcPrice_012\">883.47</td><td align=\"center\" id=\"sellePrice_012\">883.47</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('012','英镑');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_027\" style=\"text-align: left; padding-left: 15px;\">日元</td><td align=\"center\" id=\"rmbmdrat_027\">6.5062</td><td align=\"center\" id=\"rmbebrat_027\">6.3202</td><td align=\"center\" id=\"sellcPrice_027\">6.5546</td><td align=\"center\" id=\"sellePrice_027\">6.5546</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('027','日元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_028\" style=\"text-align: left; padding-left: 15px;\">加拿大元</td><td align=\"center\" id=\"rmbmdrat_028\">513.86</td><td align=\"center\" id=\"rmbebrat_028\">499.16</td><td align=\"center\" id=\"sellcPrice_028\">517.67</td><td align=\"center\" id=\"sellePrice_028\">517.67</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('028','加拿大元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_029\" style=\"text-align: left; padding-left: 15px;\">澳大利亚元</td><td align=\"center\" id=\"rmbmdrat_029\">487.54</td><td align=\"center\" id=\"rmbebrat_029\">473.59</td><td align=\"center\" id=\"sellcPrice_029\">491.16</td><td align=\"center\" id=\"sellePrice_029\">491.16</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('029','澳大利亚元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_013\" style=\"text-align: left; padding-left: 15px;\">港币</td><td align=\"center\" id=\"rmbmdrat_013\">90.13</td><td align=\"center\" id=\"rmbebrat_013\">89.40</td><td align=\"center\" id=\"sellcPrice_013\">90.50</td><td align=\"center\" id=\"sellePrice_013\">90.50</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('013','港币');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_087\" style=\"text-align: left; padding-left: 15px;\">新西兰元</td><td align=\"center\" id=\"rmbmdrat_087\">456.60</td><td align=\"center\" id=\"rmbebrat_087\">443.54</td><td align=\"center\" id=\"sellcPrice_087\">459.99</td><td align=\"center\" id=\"sellePrice_087\">459.99</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('087','新西兰元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_018\" style=\"text-align: left; padding-left: 15px;\">新加坡元</td><td align=\"center\" id=\"rmbmdrat_018\">501.46</td><td align=\"center\" id=\"rmbebrat_018\">487.12</td><td align=\"center\" id=\"sellcPrice_018\">505.18</td><td align=\"center\" id=\"sellePrice_018\">505.18</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('018','新加坡元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_084\" style=\"text-align: left; padding-left: 15px;\">泰国铢</td><td align=\"center\" id=\"rmbmdrat_084\">22.03</td><td align=\"center\" id=\"rmbebrat_084\">21.34</td><td align=\"center\" id=\"sellcPrice_084\">22.20</td><td align=\"center\" id=\"sellePrice_084\">22.20</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('084','泰国铢');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_103\" style=\"text-align: left; padding-left: 15px;\">韩元</td><td align=\"center\" id=\"rmbmdrat_103\">0.5790</td><td align=\"center\" id=\"rmbebrat_103\">0.5624</td><td align=\"center\" id=\"sellcPrice_103\">0.5833</td><td align=\"center\" id=\"sellePrice_103\">0.5833</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('103','韩元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_070\" style=\"text-align: left; padding-left: 15px;\">卢布</td><td align=\"center\" id=\"rmbmdrat_070\">9.75</td><td align=\"center\" id=\"rmbebrat_070\">9.44</td><td align=\"center\" id=\"sellcPrice_070\">9.82</td><td align=\"center\" id=\"sellePrice_070\">9.82</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('070','卢布');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_015\" style=\"text-align: left; padding-left: 15px;\">瑞士法郎</td><td align=\"center\" id=\"rmbmdrat_015\">738.47</td><td align=\"center\" id=\"rmbebrat_015\">717.35</td><td align=\"center\" id=\"sellcPrice_015\">743.95</td><td align=\"center\" id=\"sellePrice_015\">743.95</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('015','瑞士法郎');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_021\" style=\"text-align: left; padding-left: 15px;\">瑞典克朗</td><td align=\"center\" id=\"rmbmdrat_021\">76.90</td><td align=\"center\" id=\"rmbebrat_021\">74.70</td><td align=\"center\" id=\"sellcPrice_021\">77.47</td><td align=\"center\" id=\"sellePrice_021\">77.47</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('021','瑞典克朗');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_081\" style=\"text-align: left; padding-left: 15px;\">澳门元</td><td align=\"center\" id=\"rmbmdrat_081\">87.49</td><td align=\"center\" id=\"rmbebrat_081\">86.79</td><td align=\"center\" id=\"sellcPrice_081\">87.86</td><td align=\"center\" id=\"sellePrice_081\">87.86</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('081','澳门元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_023\" style=\"text-align: left; padding-left: 15px;\">挪威克朗</td><td align=\"center\" id=\"rmbmdrat_023\">75.10</td><td align=\"center\" id=\"rmbebrat_023\">72.95</td><td align=\"center\" id=\"sellcPrice_023\">75.65</td><td align=\"center\" id=\"sellePrice_023\">75.65</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('023','挪威克朗');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_022\" style=\"text-align: left; padding-left: 15px;\">丹麦克朗</td><td align=\"center\" id=\"rmbmdrat_022\">106.74</td><td align=\"center\" id=\"rmbebrat_022\">103.68</td><td align=\"center\" id=\"sellcPrice_022\">107.53</td><td align=\"center\" id=\"sellePrice_022\">107.53</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('022','丹麦克朗');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_088\" style=\"text-align: left; padding-left: 15px;\">南非 兰特</td><td align=\"center\" id=\"rmbmdrat_088\">41.71</td><td align=\"center\" id=\"rmbebrat_088\">39.68</td><td align=\"center\" id=\"sellcPrice_088\">42.02</td><td align=\"center\" id=\"sellePrice_088\">42.02</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('088','南非 兰特');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: table-row;\"><td colspan=\"5\"><span style=\"font-size: 14px; margin-left: 15px; float: left;\">单位：人民币/100外币</span><span id=\"lastRefreshedTime\" style=\"font-size: 14px; margin-right: 15px; float: right;\">更新时间：2020-07-17 11:57:12</span></td></tr></tbody></table>";
        //"<table align=\"center\" class=\"lst\" id=\"foreignEXList\" style=\"width: 100%;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\t\t<tbody><tr class=\"frame_foreign1\" style=\"height: 50px;\">\t\t\t\t<th align=\"center\"><nobr>&nbsp;&nbsp;品种&nbsp;&nbsp;</nobr></th>\t\t\t\t<th align=\"center\"><nobr>涨跌</nobr></th>\t\t\t\t<th align=\"center\">银行<br>买入价</th>\t\t\t\t<th align=\"center\">银行<br>卖出价</th>\t\t\t\t<th align=\"center\">中间价</th>\t\t\t\t<th align=\"center\">当日<br>涨跌值</th>\t\t\t\t<th align=\"center\">当日<br>涨跌幅</th>\t\t\t\t<th align=\"center\">当年<br>涨跌幅</th>\t\t\t\t<th align=\"center\">操作</th>\t\t</tr>\t<tr><td align=\"center\" id=\"metalname_130060000931\">账户欧元</td><td align=\"center\" id=\"upordown_130060000931\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fdown.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060000931\">796.70</td><td align=\"center\" id=\"sellprice_130060000931\">798.30</td><td align=\"center\" id=\"middleprice_130060000931\">797.50</td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dv_130060000931\">-1.52</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dr_130060000931\">-0.19%</span></td><td align=\"center\"><span class=\"style_up\" id=\"openprice_yr_130060000931\">+2.04%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060000931','账户欧元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr><td align=\"center\" id=\"metalname_130060000932\">账户英镑</td><td align=\"center\" id=\"upordown_130060000932\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fdown.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060000932\">879.24</td><td align=\"center\" id=\"sellprice_130060000932\">880.84</td><td align=\"center\" id=\"middleprice_130060000932\">880.04</td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dv_130060000932\">-2.04</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dr_130060000932\">-0.23%</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_yr_130060000932\">-4.56%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060000932','账户英镑');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr><td align=\"center\" id=\"metalname_130060000933\">账户澳大利亚元</td><td align=\"center\" id=\"upordown_130060000933\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fdown.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060000933\">488.45</td><td align=\"center\" id=\"sellprice_130060000933\">490.05</td><td align=\"center\" id=\"middleprice_130060000933\">489.25</td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dv_130060000933\">-0.56</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dr_130060000933\">-0.11%</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_yr_130060000933\">-0.04%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060000933','账户澳大利亚元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr><td align=\"center\" id=\"metalname_130060000934\">账户加拿大元</td><td align=\"center\" id=\"upordown_130060000934\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fdown.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060000934\">514.86</td><td align=\"center\" id=\"sellprice_130060000934\">516.46</td><td align=\"center\" id=\"middleprice_130060000934\">515.66</td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dv_130060000934\">-0.63</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dr_130060000934\">-0.12%</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_yr_130060000934\">-3.96%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060000934','账户加拿大元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: none;\"><td align=\"center\" id=\"metalname_130060000935\">账户瑞士法郎</td><td align=\"center\" id=\"upordown_130060000935\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fdown.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060000935\">740.26</td><td align=\"center\" id=\"sellprice_130060000935\">741.86</td><td align=\"center\" id=\"middleprice_130060000935\">741.06</td><td align=\"center\"><span class=\"style_up\" id=\"openprice_dv_130060000935\">+0.08</span></td><td align=\"center\"><span class=\"style_up\" id=\"openprice_dr_130060000935\">+0.01%</span></td><td align=\"center\"><span class=\"style_up\" id=\"openprice_yr_130060000935\">+3.08%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060000935','账户瑞士法郎');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: none;\"><td align=\"center\" id=\"metalname_130060001311\">账户日元</td><td align=\"center\" id=\"upordown_130060001311\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fup.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060001311\">6.5211</td><td align=\"center\" id=\"sellprice_130060001311\">6.5371</td><td align=\"center\" id=\"middleprice_130060001311\">6.5291</td><td align=\"center\"><span class=\"style_up\" id=\"openprice_dv_130060001311\">+0.0004</span></td><td align=\"center\"><span class=\"style_up\" id=\"openprice_dr_130060001311\">+0.01%</span></td><td align=\"center\"><span class=\"style_up\" id=\"openprice_yr_130060001311\">+1.89%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060001311','账户日元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: none;\"><td align=\"center\" id=\"metalname_130060085591\">账户新西兰元</td><td align=\"center\" id=\"upordown_130060085591\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fdown.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060085591\">457.40</td><td align=\"center\" id=\"sellprice_130060085591\">459.00</td><td align=\"center\" id=\"middleprice_130060085591\">458.20</td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dv_130060085591\">-0.65</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dr_130060085591\">-0.14%</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_yr_130060085591\">-2.41%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060085591','账户新西兰元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: none;\"><td align=\"center\" id=\"metalname_130060085592\">账户新加坡元</td><td align=\"center\" id=\"upordown_130060085592\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fup.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060085592\">502.42</td><td align=\"center\" id=\"sellprice_130060085592\">504.02</td><td align=\"center\" id=\"middleprice_130060085592\">503.22</td><td align=\"center\"><span class=\"style_up\" id=\"openprice_dv_130060085592\">+0.19</span></td><td align=\"center\"><span class=\"style_up\" id=\"openprice_dr_130060085592\">+0.04%</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_yr_130060085592\">-2.82%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060085592','账户新加坡元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: none;\"><td align=\"center\" id=\"metalname_130060085593\">账户挪威克朗</td><td align=\"center\" id=\"upordown_130060085593\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fup.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060085593\">75.065</td><td align=\"center\" id=\"sellprice_130060085593\">75.665</td><td align=\"center\" id=\"middleprice_130060085593\">75.365</td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dv_130060085593\">-0.166</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dr_130060085593\">-0.22%</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_yr_130060085593\">-4.84%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060085593','账户挪威克朗');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: none;\"><td align=\"center\" id=\"metalname_130060085594\">账户瑞典克朗</td><td align=\"center\" id=\"upordown_130060085594\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fdown.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060085594\">76.865</td><td align=\"center\" id=\"sellprice_130060085594\">77.465</td><td align=\"center\" id=\"middleprice_130060085594\">77.165</td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dv_130060085594\">-0.181</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dr_130060085594\">-0.23%</span></td><td align=\"center\"><span class=\"style_up\" id=\"openprice_yr_130060085594\">+3.71%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060085594','账户瑞典克朗');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: table-row;\"><td colspan=\"9\"><span class=\"refreshTimeStyle\" id=\"lastRefreshedTime\">更新时间：2020-07-17 11:57:14</span></td></tr></tbody></table>"
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat format2 = new SimpleDateFormat("yyyyMMdd");
        //6.Jsoup解析html
        Document document = Jsoup.parse(content);
        //Elements elements =document.getElementsByTag("table");
        Element element =document.getElementById("closeSellTable");
        Elements trs=element.getElementsByTag("tr");
        Elements ths=trs.get(0).getElementsByTag("th");
        //pubfuncs.printerrormsg("解析的数据："+tds.get(0).text()+","+tds.get(1).text()+","+tds.get(2).text()+","+tds.get(3).text()+","+tds.get(4).text()+","+tds.get(5).text());
        if(ths.size()==6) {
            if ((ths.get(0).text() + "," + ths.get(1).text() + "," + ths.get(2).text() + "," + ths.get(3).text() + "," + ths.get(4).text() + "," + ths.get(5).text()).equals("币种,银行买入价（汇）,银行买入价（钞）,银行卖出价（汇）,银行卖出价（钞）,操作")) {
                if (trs.size() > 2) {
                    String fasjtd=trs.get(trs.size()-1).getElementsByTag("td").get(0).getElementById("lastRefreshedTime").text();
                    if(fasjtd.startsWith("更新时间：")) {
                        //获取发布时间
                        fasjtd=fasjtd.substring("更新时间：".length());
                        try {
                            Date fbsj = format.parse(fasjtd);
                            int fbsjint=Integer.parseInt(format2.format(fbsj));
                            for (int i = 1; i < trs.size() - 1; i++) {
                                Elements tds = trs.get(i).getElementsByTag("td");
                                if (tds.size() == 6) {
                                    Exchangerate exchangerate = new Exchangerate();
                                    int bzidindex = sybizhongname2.indexOf(tds.get(0).text());
                                    String bzname=tds.get(0).text();
                                    if (bzidindex < 0) {
                                        String bizhong3=bzname.indexOf("账户")>-1?bzname.substring(0,bzname.indexOf("账户")+3):bzname.substring(0,bzname.length()-1);
                                        pubfuncs.printerrormsg("比较："+bizhong3);
                                        bzidindex = sybizhongname3.indexOf(bizhong3);
                                    }
                                    if (bzidindex < 0) {
                                        pubfuncs.printerrormsg("喔噢，找不到的币种：" + tds.get(0).text());
                                    }else {
                                        exchangerate.setBzid(sybizhongid.get(bzidindex));
                                        exchangerate.setOrgid(2);
                                        //有获取到汇率数据
                                        boolean havedata = false;
                                        try {
                                            exchangerate.setXhmrj(Float.parseFloat(tds.get(1).text()));
                                            havedata = true;
                                        } catch (Exception ee) {
                                        }
                                        try {
                                            exchangerate.setXcmrj(Float.parseFloat(tds.get(2).text()));
                                            havedata = true;
                                        } catch (Exception ee) {
                                        }
                                        try {
                                            exchangerate.setXhmcj(Float.parseFloat(tds.get(3).text()));
                                            havedata = true;
                                        } catch (Exception ee) {
                                        }
                                        try {
                                            exchangerate.setXcmcj(Float.parseFloat(tds.get(4).text()));
                                            havedata = true;
                                        } catch (Exception ee) {
                                        }

                                        exchangerate.setFbsj(fbsj);
                                        exchangerate.setFbsjdate(fbsjint);

                                        exchangerates.add(exchangerate);
                                    }
                                } else {
                                    pubfuncs.printerrormsg("哇，工商银行汇率网页读取有异常，该行识别不出，赶紧解决哦：" + trs.get(i).text() + ",,," + content);
                                }
                            }
                        }catch (Exception e){
                            pubfuncs.printerrormsg(bankname+",3,转换发布时间出错："+e.getMessage());
                        }
                    }else {
                        pubfuncs.printerrormsg("哇，工商银行汇率网页格式变了，赶紧解决哦，原来发布时间是在表格最后一行的，现在找不到了，最后一行内容为：" + trs.get(trs.size()-1).html());
                    }
                }else{
                    pubfuncs.printerrormsg("啊哈，"+bankname+"银行获取的汇率报文里面没有数据，请赶紧处理");
                }
            }else {
                pubfuncs.printerrormsg("哇，工商银行汇率网页格式变了，赶紧解决哦，原来数据表表头是这样的：币种,银行买入价（汇）,银行买入价（钞）,银行卖出价（汇）,银行卖出价（钞）,操作，现在变成了：" + ths.get(0).text() + "," + ths.get(1).text() + "," + ths.get(2).text() + "," + ths.get(3).text() + "," + ths.get(4).text() + "," + ths.get(5).text());
            }
        }else{
            pubfuncs.printerrormsg("哇，工商银行汇率网页格式变了，赶紧解决哦，原来数据表表头个数有6个，现在个数变成了：" + ths.size());
        }
        //pubfuncs.printerrormsg("返回数据："+new Gson().toJson(exchangerates));
        return exchangerates;
    }

    //浏览器推送解析网页版账户外汇数据
    public List<Exchangerate> prawebzhwhcontent(String bankname, String content){
        List<Exchangerate> exchangerates=new ArrayList<Exchangerate>();
        //content="<table width=\"100%\" align=\"center\" class=\"lst\" id=\"closeSellTable\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\t\t\t\t\t<tbody><tr id=\"header\" style=\"height: 50px;\">\t\t\t\t\t\t<th width=\"15%\" style=\"text-align: left; padding-left: 20px;\">币种</th>\t\t\t\t\t\t<th width=\"20%\">银行买入价（汇）</th>\t\t\t\t\t\t<th width=\"20%\">银行买入价（钞）</th>\t\t\t\t\t\t<th width=\"20%\">银行卖出价（汇）</th>\t\t\t\t\t\t<th width=\"20%\">银行卖出价（钞）</th>\t\t\t\t\t\t<th width=\"10.3%\" style=\"text-align: center;\">操作</th>\t\t\t\t\t</tr>\t\t\t<tr height=\"19\" class=\"contentStyle\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_014\" style=\"text-align: left; padding-left: 15px;\">美元</td><td align=\"center\" id=\"rmbmdrat_014\">698.68</td><td align=\"center\" id=\"rmbebrat_014\">693.08</td><td align=\"center\" id=\"sellcPrice_014\">701.62</td><td align=\"center\" id=\"sellePrice_014\">701.62</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('014','美元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_038\" style=\"text-align: left; padding-left: 15px;\">欧元</td><td align=\"center\" id=\"rmbmdrat_038\">794.71</td><td align=\"center\" id=\"rmbebrat_038\">771.98</td><td align=\"center\" id=\"sellcPrice_038\">800.61</td><td align=\"center\" id=\"sellePrice_038\">800.61</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('038','欧元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_012\" style=\"text-align: left; padding-left: 15px;\">英镑</td><td align=\"center\" id=\"rmbmdrat_012\">876.96</td><td align=\"center\" id=\"rmbebrat_012\">851.88</td><td align=\"center\" id=\"sellcPrice_012\">883.47</td><td align=\"center\" id=\"sellePrice_012\">883.47</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('012','英镑');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_027\" style=\"text-align: left; padding-left: 15px;\">日元</td><td align=\"center\" id=\"rmbmdrat_027\">6.5062</td><td align=\"center\" id=\"rmbebrat_027\">6.3202</td><td align=\"center\" id=\"sellcPrice_027\">6.5546</td><td align=\"center\" id=\"sellePrice_027\">6.5546</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('027','日元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_028\" style=\"text-align: left; padding-left: 15px;\">加拿大元</td><td align=\"center\" id=\"rmbmdrat_028\">513.86</td><td align=\"center\" id=\"rmbebrat_028\">499.16</td><td align=\"center\" id=\"sellcPrice_028\">517.67</td><td align=\"center\" id=\"sellePrice_028\">517.67</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('028','加拿大元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_029\" style=\"text-align: left; padding-left: 15px;\">澳大利亚元</td><td align=\"center\" id=\"rmbmdrat_029\">487.54</td><td align=\"center\" id=\"rmbebrat_029\">473.59</td><td align=\"center\" id=\"sellcPrice_029\">491.16</td><td align=\"center\" id=\"sellePrice_029\">491.16</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('029','澳大利亚元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_013\" style=\"text-align: left; padding-left: 15px;\">港币</td><td align=\"center\" id=\"rmbmdrat_013\">90.13</td><td align=\"center\" id=\"rmbebrat_013\">89.40</td><td align=\"center\" id=\"sellcPrice_013\">90.50</td><td align=\"center\" id=\"sellePrice_013\">90.50</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('013','港币');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_087\" style=\"text-align: left; padding-left: 15px;\">新西兰元</td><td align=\"center\" id=\"rmbmdrat_087\">456.60</td><td align=\"center\" id=\"rmbebrat_087\">443.54</td><td align=\"center\" id=\"sellcPrice_087\">459.99</td><td align=\"center\" id=\"sellePrice_087\">459.99</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('087','新西兰元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_018\" style=\"text-align: left; padding-left: 15px;\">新加坡元</td><td align=\"center\" id=\"rmbmdrat_018\">501.46</td><td align=\"center\" id=\"rmbebrat_018\">487.12</td><td align=\"center\" id=\"sellcPrice_018\">505.18</td><td align=\"center\" id=\"sellePrice_018\">505.18</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('018','新加坡元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_084\" style=\"text-align: left; padding-left: 15px;\">泰国铢</td><td align=\"center\" id=\"rmbmdrat_084\">22.03</td><td align=\"center\" id=\"rmbebrat_084\">21.34</td><td align=\"center\" id=\"sellcPrice_084\">22.20</td><td align=\"center\" id=\"sellePrice_084\">22.20</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('084','泰国铢');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_103\" style=\"text-align: left; padding-left: 15px;\">韩元</td><td align=\"center\" id=\"rmbmdrat_103\">0.5790</td><td align=\"center\" id=\"rmbebrat_103\">0.5624</td><td align=\"center\" id=\"sellcPrice_103\">0.5833</td><td align=\"center\" id=\"sellePrice_103\">0.5833</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('103','韩元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_070\" style=\"text-align: left; padding-left: 15px;\">卢布</td><td align=\"center\" id=\"rmbmdrat_070\">9.75</td><td align=\"center\" id=\"rmbebrat_070\">9.44</td><td align=\"center\" id=\"sellcPrice_070\">9.82</td><td align=\"center\" id=\"sellePrice_070\">9.82</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('070','卢布');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_015\" style=\"text-align: left; padding-left: 15px;\">瑞士法郎</td><td align=\"center\" id=\"rmbmdrat_015\">738.47</td><td align=\"center\" id=\"rmbebrat_015\">717.35</td><td align=\"center\" id=\"sellcPrice_015\">743.95</td><td align=\"center\" id=\"sellePrice_015\">743.95</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('015','瑞士法郎');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_021\" style=\"text-align: left; padding-left: 15px;\">瑞典克朗</td><td align=\"center\" id=\"rmbmdrat_021\">76.90</td><td align=\"center\" id=\"rmbebrat_021\">74.70</td><td align=\"center\" id=\"sellcPrice_021\">77.47</td><td align=\"center\" id=\"sellePrice_021\">77.47</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('021','瑞典克朗');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_081\" style=\"text-align: left; padding-left: 15px;\">澳门元</td><td align=\"center\" id=\"rmbmdrat_081\">87.49</td><td align=\"center\" id=\"rmbebrat_081\">86.79</td><td align=\"center\" id=\"sellcPrice_081\">87.86</td><td align=\"center\" id=\"sellePrice_081\">87.86</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('081','澳门元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_023\" style=\"text-align: left; padding-left: 15px;\">挪威克朗</td><td align=\"center\" id=\"rmbmdrat_023\">75.10</td><td align=\"center\" id=\"rmbebrat_023\">72.95</td><td align=\"center\" id=\"sellcPrice_023\">75.65</td><td align=\"center\" id=\"sellePrice_023\">75.65</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('023','挪威克朗');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_022\" style=\"text-align: left; padding-left: 15px;\">丹麦克朗</td><td align=\"center\" id=\"rmbmdrat_022\">106.74</td><td align=\"center\" id=\"rmbebrat_022\">103.68</td><td align=\"center\" id=\"sellcPrice_022\">107.53</td><td align=\"center\" id=\"sellePrice_022\">107.53</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('022','丹麦克朗');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr height=\"19\" class=\"contentStyle\" style=\"display: none;\" onmouseover=\"this.className='overStyle'\" onmouseout=\"this.className='contentStyle'\"><td align=\"center\" id=\"currType_088\" style=\"text-align: left; padding-left: 15px;\">南非 兰特</td><td align=\"center\" id=\"rmbmdrat_088\">41.71</td><td align=\"center\" id=\"rmbebrat_088\">39.68</td><td align=\"center\" id=\"sellcPrice_088\">42.02</td><td align=\"center\" id=\"sellePrice_088\">42.02</td><td style=\"text-align: center;\"><a href=\"javascript:tradeButton('088','南非 兰特');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: table-row;\"><td colspan=\"5\"><span style=\"font-size: 14px; margin-left: 15px; float: left;\">单位：人民币/100外币</span><span id=\"lastRefreshedTime\" style=\"font-size: 14px; margin-right: 15px; float: right;\">更新时间：2020-07-17 11:57:12</span></td></tr></tbody></table>";
        //content="<table align=\"center\" class=\"lst\" id=\"foreignEXList\" style=\"width: 100%;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\t\t<tbody><tr class=\"frame_foreign1\" style=\"height: 50px;\">\t\t\t\t<th align=\"center\"><nobr>&nbsp;&nbsp;品种&nbsp;&nbsp;</nobr></th>\t\t\t\t<th align=\"center\"><nobr>涨跌</nobr></th>\t\t\t\t<th align=\"center\">银行<br>买入价</th>\t\t\t\t<th align=\"center\">银行<br>卖出价</th>\t\t\t\t<th align=\"center\">中间价</th>\t\t\t\t<th align=\"center\">当日<br>涨跌值</th>\t\t\t\t<th align=\"center\">当日<br>涨跌幅</th>\t\t\t\t<th align=\"center\">当年<br>涨跌幅</th>\t\t\t\t<th align=\"center\">操作</th>\t\t</tr>\t<tr><td align=\"center\" id=\"metalname_130060000931\">账户欧元</td><td align=\"center\" id=\"upordown_130060000931\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fdown.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060000931\">796.70</td><td align=\"center\" id=\"sellprice_130060000931\">798.30</td><td align=\"center\" id=\"middleprice_130060000931\">797.50</td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dv_130060000931\">-1.52</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dr_130060000931\">-0.19%</span></td><td align=\"center\"><span class=\"style_up\" id=\"openprice_yr_130060000931\">+2.04%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060000931','账户欧元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr><td align=\"center\" id=\"metalname_130060000932\">账户英镑</td><td align=\"center\" id=\"upordown_130060000932\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fdown.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060000932\">879.24</td><td align=\"center\" id=\"sellprice_130060000932\">880.84</td><td align=\"center\" id=\"middleprice_130060000932\">880.04</td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dv_130060000932\">-2.04</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dr_130060000932\">-0.23%</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_yr_130060000932\">-4.56%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060000932','账户英镑');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr><td align=\"center\" id=\"metalname_130060000933\">账户澳大利亚元</td><td align=\"center\" id=\"upordown_130060000933\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fdown.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060000933\">488.45</td><td align=\"center\" id=\"sellprice_130060000933\">490.05</td><td align=\"center\" id=\"middleprice_130060000933\">489.25</td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dv_130060000933\">-0.56</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dr_130060000933\">-0.11%</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_yr_130060000933\">-0.04%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060000933','账户澳大利亚元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr><td align=\"center\" id=\"metalname_130060000934\">账户加拿大元</td><td align=\"center\" id=\"upordown_130060000934\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fdown.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060000934\">514.86</td><td align=\"center\" id=\"sellprice_130060000934\">516.46</td><td align=\"center\" id=\"middleprice_130060000934\">515.66</td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dv_130060000934\">-0.63</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dr_130060000934\">-0.12%</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_yr_130060000934\">-3.96%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060000934','账户加拿大元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: none;\"><td align=\"center\" id=\"metalname_130060000935\">账户瑞士法郎</td><td align=\"center\" id=\"upordown_130060000935\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fdown.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060000935\">740.26</td><td align=\"center\" id=\"sellprice_130060000935\">741.86</td><td align=\"center\" id=\"middleprice_130060000935\">741.06</td><td align=\"center\"><span class=\"style_up\" id=\"openprice_dv_130060000935\">+0.08</span></td><td align=\"center\"><span class=\"style_up\" id=\"openprice_dr_130060000935\">+0.01%</span></td><td align=\"center\"><span class=\"style_up\" id=\"openprice_yr_130060000935\">+3.08%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060000935','账户瑞士法郎');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: none;\"><td align=\"center\" id=\"metalname_130060001311\">账户日元</td><td align=\"center\" id=\"upordown_130060001311\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fup.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060001311\">6.5211</td><td align=\"center\" id=\"sellprice_130060001311\">6.5371</td><td align=\"center\" id=\"middleprice_130060001311\">6.5291</td><td align=\"center\"><span class=\"style_up\" id=\"openprice_dv_130060001311\">+0.0004</span></td><td align=\"center\"><span class=\"style_up\" id=\"openprice_dr_130060001311\">+0.01%</span></td><td align=\"center\"><span class=\"style_up\" id=\"openprice_yr_130060001311\">+1.89%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060001311','账户日元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: none;\"><td align=\"center\" id=\"metalname_130060085591\">账户新西兰元</td><td align=\"center\" id=\"upordown_130060085591\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fdown.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060085591\">457.40</td><td align=\"center\" id=\"sellprice_130060085591\">459.00</td><td align=\"center\" id=\"middleprice_130060085591\">458.20</td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dv_130060085591\">-0.65</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dr_130060085591\">-0.14%</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_yr_130060085591\">-2.41%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060085591','账户新西兰元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: none;\"><td align=\"center\" id=\"metalname_130060085592\">账户新加坡元</td><td align=\"center\" id=\"upordown_130060085592\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fup.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060085592\">502.42</td><td align=\"center\" id=\"sellprice_130060085592\">504.02</td><td align=\"center\" id=\"middleprice_130060085592\">503.22</td><td align=\"center\"><span class=\"style_up\" id=\"openprice_dv_130060085592\">+0.19</span></td><td align=\"center\"><span class=\"style_up\" id=\"openprice_dr_130060085592\">+0.04%</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_yr_130060085592\">-2.82%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060085592','账户新加坡元');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: none;\"><td align=\"center\" id=\"metalname_130060085593\">账户挪威克朗</td><td align=\"center\" id=\"upordown_130060085593\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fup.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060085593\">75.065</td><td align=\"center\" id=\"sellprice_130060085593\">75.665</td><td align=\"center\" id=\"middleprice_130060085593\">75.365</td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dv_130060085593\">-0.166</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dr_130060085593\">-0.22%</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_yr_130060085593\">-4.84%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060085593','账户挪威克朗');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: none;\"><td align=\"center\" id=\"metalname_130060085594\">账户瑞典克朗</td><td align=\"center\" id=\"upordown_130060085594\"><img width=\"13\" height=\"15\" src=\"/icbc/newperbank/images/fdown.png\" border=\"0\"></td><td align=\"center\" id=\"buyprice_130060085594\">76.865</td><td align=\"center\" id=\"sellprice_130060085594\">77.465</td><td align=\"center\" id=\"middleprice_130060085594\">77.165</td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dv_130060085594\">-0.181</span></td><td align=\"center\"><span class=\"style_down\" id=\"openprice_dr_130060085594\">-0.23%</span></td><td align=\"center\"><span class=\"style_up\" id=\"openprice_yr_130060085594\">+3.71%</span></td><td align=\"center\"><a href=\"javascript:exchangeClikcFun('130060085594','账户瑞典克朗');\"><img onmouseover=\"this.src='/icbc/newperbank/perbank3/images/gold_trade1.png'\" onmouseout=\"this.src='/icbc/newperbank/perbank3/images/gold_trade.png'\" src=\"/icbc/newperbank/perbank3/images/gold_trade.png\"></a></td></tr><tr style=\"display: table-row;\"><td colspan=\"9\"><span class=\"refreshTimeStyle\" id=\"lastRefreshedTime\">更新时间：2020-07-17 11:57:14</span></td></tr></tbody></table>";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat format2 = new SimpleDateFormat("yyyyMMdd");
        //6.Jsoup解析html
        Document document = Jsoup.parse(content);
        //Elements elements =document.getElementsByTag("table");
        Element element =document.getElementById("foreignEXList");
        Elements trs=element.getElementsByTag("tr");
        Elements ths=trs.get(0).getElementsByTag("th");
        //pubfuncs.printerrormsg("解析的数据："+tds.get(0).text()+","+tds.get(1).text()+","+tds.get(2).text()+","+tds.get(3).text()+","+tds.get(4).text()+","+tds.get(5).text());
        if(ths.size()==9) {
            if ((ths.get(0).text() + "," + ths.get(1).text() + "," + ths.get(2).text() + "," + ths.get(3).text() + "," + ths.get(4).text() + "," + ths.get(5).text()+ "," + ths.get(6).text() + "," + ths.get(7).text() + "," + ths.get(8).text()).equals("品种,涨跌,银行 买入价,银行 卖出价,中间价,当日 涨跌值,当日 涨跌幅,当年 涨跌幅,操作")) {
                if (trs.size() > 2) {
                    String fasjtd=trs.get(trs.size()-1).getElementsByTag("td").get(0).getElementById("lastRefreshedTime").text();
                    if(fasjtd.startsWith("更新时间：")) {
                        //获取发布时间
                        fasjtd=fasjtd.substring("更新时间：".length());
                        try {
                            Date fbsj = format.parse(fasjtd);
                            int fbsjint=Integer.parseInt(format2.format(fbsj));
                            for (int i = 1; i < trs.size() - 1; i++) {
                                Elements tds = trs.get(i).getElementsByTag("td");
                                if (tds.size() == 9) {
                                    Exchangerate exchangerate = new Exchangerate();
                                    int bzidindex = sybizhongname2.indexOf(tds.get(0).text());
                                    String bzname=tds.get(0).text();
                                    if (bzidindex < 0) {
                                        String bizhong3=bzname.indexOf("账户")>-1?bzname.substring(0,bzname.indexOf("账户")+3):bzname.substring(0,bzname.length()-1);
                                        pubfuncs.printerrormsg("比较："+bizhong3);
                                        bzidindex = sybizhongname3.indexOf(bizhong3);
                                    }
                                    if (bzidindex < 0) {
                                        pubfuncs.printerrormsg("喔噢，找不到的币种：" + tds.get(0).text());
                                    }else {
                                        exchangerate.setBzid(sybizhongid.get(bzidindex));
                                        exchangerate.setOrgid(2);
                                        //有获取到汇率数据
                                        boolean havedata = false;
                                        try {
                                            exchangerate.setXhmrj(Float.parseFloat(tds.get(2).text()));
                                            havedata = true;
                                        } catch (Exception ee) {
                                        }
                                        try {
                                            exchangerate.setXcmrj(0);
                                            havedata = true;
                                        } catch (Exception ee) {
                                        }
                                        try {
                                            exchangerate.setXhmcj(Float.parseFloat(tds.get(3).text()));
                                            havedata = true;
                                        } catch (Exception ee) {
                                        }
                                        try {
                                            exchangerate.setXcmcj(0);
                                            havedata = true;
                                        } catch (Exception ee) {
                                        }

                                        exchangerate.setFbsj(fbsj);
                                        exchangerate.setFbsjdate(fbsjint);

                                        exchangerates.add(exchangerate);
                                    }
                                } else {
                                    pubfuncs.printerrormsg("哇，工商银行汇率网页读取有异常，该行识别不出，赶紧解决哦：" + trs.get(i).text() + ",,," + content);
                                }
                            }
                        }catch (Exception e){
                            pubfuncs.printerrormsg(bankname+",3,转换发布时间出错："+e.getMessage());
                        }
                    }else {
                        pubfuncs.printerrormsg("哇，工商银行汇率网页格式变了，赶紧解决哦，原来发布时间是在表格最后一行的，现在找不到了，最后一行内容为：" + trs.get(trs.size()-1).html());
                    }
                }else{
                    pubfuncs.printerrormsg("啊哈，"+bankname+"银行获取的汇率报文里面没有数据，请赶紧处理");
                }
            }else {
                pubfuncs.printerrormsg("哇，工商银行汇率网页格式变了，赶紧解决哦，原来数据表表头是这样的：品种,涨跌,银行 买入价,银行 卖出价,中间价,当日 涨跌值,当日 涨跌幅,当年 涨跌幅,操作，现在变成了：" + ths.get(0).text() + "," + ths.get(1).text() + "," + ths.get(2).text() + "," + ths.get(3).text() + "," + ths.get(4).text() + "," + ths.get(5).text()+ "," + ths.get(6).text() + "," + ths.get(7).text() + "," + ths.get(8).text());
            }
        }else{
            pubfuncs.printerrormsg("哇，工商银行汇率网页格式变了，赶紧解决哦，原来数据表表头个数有9个，现在个数变成了：" + ths.size());
        }
        //pubfuncs.printerrormsg("返回数据："+new Gson().toJson(exchangerates));
        return exchangerates;
    }




    //主动拉取结售汇接口解析银行账户外汇汇率报文
    /*
       {"TranErrorCode":"","rf":[{"rmbmdrat":"656.17","standBuyRate":"656.17","rmbType":"","sellcPrice":"657.61","rmbebrat":"654.86","sellePrice":"657.61","rmbcbrat":"649.61","proId":"130060000016","currType":"014","currTypeName":"美元","rmbTypeName":"","standSellRate":"656.17"},{"rmbmdrat":"791.87","standBuyRate":"791.87","rmbType":"","sellcPrice":"794.96","rmbebrat":"789.10","sellePrice":"794.96","rmbcbrat":"766.53","proId":"130060000016","currType":"038","currTypeName":"欧元","rmbTypeName":"","standSellRate":"791.87"},{"rmbmdrat":"879.96","standBuyRate":"879.96","rmbType":"","sellcPrice":"883.39","rmbebrat":"876.88","sellePrice":"883.39","rmbcbrat":"851.80","proId":"130060000016","currType":"012","currTypeName":"英镑","rmbTypeName":"","standSellRate":"879.96"},{"rmbmdrat":"6.2836","standBuyRate":"6.2836","rmbType":"","sellcPrice":"6.3081","rmbebrat":"6.2616","sellePrice":"6.3081","rmbcbrat":"6.0825","proId":"130060000016","currType":"027","currTypeName":"日元","rmbTypeName":"","standSellRate":"6.2836"},{"rmbmdrat":"506.79","standBuyRate":"506.79","rmbType":"","sellcPrice":"508.77","rmbebrat":"505.02","sellePrice":"508.77","rmbcbrat":"490.57","proId":"130060000016","currType":"028","currTypeName":"加拿大元","rmbTypeName":"","standSellRate":"506.79"},{"rmbmdrat":"483.60","standBuyRate":"483.60","rmbType":"","sellcPrice":"485.49","rmbebrat":"481.91","sellePrice":"485.49","rmbcbrat":"468.12","proId":"130060000016","currType":"029","currTypeName":"澳大利亚元","rmbTypeName":"","standSellRate":"483.60"},{"rmbmdrat":"84.64","standBuyRate":"84.64","rmbType":"","sellcPrice":"84.83","rmbebrat":"84.48","sellePrice":"84.83","rmbcbrat":"83.79","proId":"130060000016","currType":"013","currTypeName":"港币","rmbTypeName":"","standSellRate":"84.64"},{"rmbmdrat":"463.12","standBuyRate":"463.12","rmbType":"","sellcPrice":"464.93","rmbebrat":"461.50","sellePrice":"464.93","rmbcbrat":"448.30","proId":"130060000016","currType":"087","currTypeName":"新西兰元","rmbTypeName":"","standSellRate":"463.12"},{"rmbmdrat":"490.58","standBuyRate":"490.58","rmbType":"","sellcPrice":"492.49","rmbebrat":"488.86","sellePrice":"492.49","rmbcbrat":"474.88","proId":"130060000016","currType":"018","currTypeName":"新加坡元","rmbTypeName":"","standSellRate":"490.58"},{"rmbmdrat":"21.72","standBuyRate":"21.72","rmbType":"","sellcPrice":"21.80","rmbebrat":"21.64","sellePrice":"21.80","rmbcbrat":"20.97","proId":"130060000016","currType":"084","currTypeName":"泰国铢","rmbTypeName":"","standSellRate":"21.72"},{"rmbmdrat":"0.5940","standBuyRate":"0.5940","rmbType":"","sellcPrice":"0.5963","rmbebrat":"0.5919","sellePrice":"0.5963","rmbcbrat":"0.5750","proId":"130060000016","currType":"103","currTypeName":"韩元","rmbTypeName":"","standSellRate":"0.5940"},{"rmbmdrat":"8.64","standBuyRate":"8.64","rmbType":"","sellcPrice":"8.67","rmbebrat":"8.61","sellePrice":"8.67","rmbcbrat":"8.34","proId":"130060000016","currType":"070","currTypeName":"卢布","rmbTypeName":"","standSellRate":"8.64"},{"rmbmdrat":"729.73","standBuyRate":"729.73","rmbType":"","sellcPrice":"732.58","rmbebrat":"727.18","sellePrice":"732.58","rmbcbrat":"706.38","proId":"130060000016","currType":"015","currTypeName":"瑞士法郎","rmbTypeName":"","standSellRate":"729.73"},{"rmbmdrat":"77.34","standBuyRate":"77.34","rmbType":"","sellcPrice":"77.64","rmbebrat":"77.07","sellePrice":"77.64","rmbcbrat":"74.87","proId":"130060000016","currType":"021","currTypeName":"瑞典克朗","rmbTypeName":"","standSellRate":"77.34"},{"rmbmdrat":"82.17","standBuyRate":"82.17","rmbType":"","sellcPrice":"82.35","rmbebrat":"82.01","sellePrice":"82.35","rmbcbrat":"81.35","proId":"130060000016","currType":"081","currTypeName":"澳门元","rmbTypeName":"","standSellRate":"82.17"},{"rmbmdrat":"74.45","standBuyRate":"74.45","rmbType":"","sellcPrice":"74.74","rmbebrat":"74.19","sellePrice":"74.74","rmbcbrat":"72.07","proId":"130060000016","currType":"023","currTypeName":"挪威克朗","rmbTypeName":"","standSellRate":"74.45"},{"rmbmdrat":"106.39","standBuyRate":"106.39","rmbType":"","sellcPrice":"106.80","rmbebrat":"106.02","sellePrice":"106.80","rmbcbrat":"102.99","proId":"130060000016","currType":"022","currTypeName":"丹麦克朗","rmbTypeName":"","standSellRate":"106.39"},{"rmbmdrat":"42.90","standBuyRate":"42.90","rmbType":"","sellcPrice":"43.07","rmbebrat":"42.75","sellePrice":"43.07","rmbcbrat":"40.67","proId":"130060000016","currType":"088","currTypeName":"南非 兰特","rmbTypeName":"","standSellRate":"42.90"}],"TranErrorDisplayMsg":""}
    */
    public List<Exchangerate> prazhgjscontent(String bankname, String content){
        List<Exchangerate> exchangerates=new ArrayList<Exchangerate>();
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat format2 = new SimpleDateFormat("yyyyMMdd");
            Icbczhgjsrespmsg icbczhgjsrespmsg = null;
            //返回的是json，所以开始对json进行解析
            try {
                icbczhgjsrespmsg = new Gson().fromJson(content, Icbczhgjsrespmsg.class);
            } catch (Exception ee) {
                pubfuncs.printerrormsg("解析工银返回的账户贵金属信息出错：" + ee.getMessage());
            }
            if (icbczhgjsrespmsg != null && icbczhgjsrespmsg.getMarket().size()>0) {
                Date fbsj=null;
                int fbsjint=0;
                try{
                    fbsj = format.parse(icbczhgjsrespmsg.getSysdate());
                    fbsjint=Integer.parseInt(format2.format(fbsj));
                }catch (Exception eee){
                    pubfuncs.printerrormsg("解析工银返回的账户贵金属信息日期格式出错：本来格式：yyyy-MM-dd HH:mm:ss，返回:" + icbczhgjsrespmsg.getSysdate());
                    return exchangerates;
                }
                List<Icbczhgjsrespmarket> markets = icbczhgjsrespmsg.getMarket();
                if (markets.size() > 0) {
                    for (int i = 0; i < markets.size(); i++) {
                        Exchangerate exchangerate = new Exchangerate();
                        int bzidindex = sybizhongname2.indexOf(markets.get(i).getMetalname());
                        if (bzidindex < 0) {
                            String bizhong3=markets.get(i).getMetalname().indexOf("账户")>-1?markets.get(i).getMetalname().substring(0,markets.get(i).getMetalname().indexOf("账户")+3):markets.get(i).getMetalname().substring(0,markets.get(i).getMetalname().length()-1);
                            pubfuncs.printerrormsg("比较："+bizhong3);
                            bzidindex = sybizhongname3.indexOf(bizhong3);
                        }
                        if (bzidindex < 0) {
                            pubfuncs.printerrormsg("喔噢，找不到的贵金属：" + markets.get(i).getMetalname());
                        }else {
                            exchangerate.setBzid(sybizhongid.get(bzidindex));
                            exchangerate.setOrgid(2);
                            exchangerate.setXhmrj(markets.get(i).getBuyprice());
                            exchangerate.setXhmcj(markets.get(i).getSellprice());
                            exchangerate.setFbsj(fbsj);
                            exchangerate.setFbsjdate(fbsjint);
                            exchangerates.add(exchangerate);
                        }
                    }
                } else {
                    pubfuncs.printerrormsg("哇，工商银行汇率结售汇返回信息格式变了，赶紧解决哦，找不到rf");
                }
            } else {
                pubfuncs.printerrormsg("哇，工商银行结售汇返回信息格式变了，赶紧解决哦，找不到rf或返回null");
            }
        }catch (Exception ee){
            pubfuncs.printerrormsg("解析结售汇返回字串出错："+ee.getMessage()+"，"+ee.getStackTrace()+"，请赶紧处理");
        }
        return exchangerates;
    }


    //解析内容结束

    //银行汇率处理（拉取），包括获取报文解析报文
    //gettype：1-外汇（内嵌页面获取），2-账户外汇（接口获取）
    public void bankexchangerate(String bankname,String date,String time,int gettype){
        try {
            String getcontent = "";
            if (gettype == 1) {//结售汇接口获取
                getcontent = getwhcontent("icbc", date, time, para.urlgettype);
            } else if (gettype == 2) {//账户外汇接口获取
                getcontent = getzhwhcontent("icbc", para.urlgettype);
                pubfuncs.printerrormsg("返回的信息："+getcontent);
            } else if (gettype == 5) {//账户贵金属接口获取
                getcontent = getzhgjscontent("icbc", para.urlgettype);
            }
            //pubfuncs.printerrormsg("返回的信息："+getcontent);
            //部署到windows（默认编码格式为gbk的那种）时需要加入的代码，在开发环境时不要加入，否则以为乱码了-开始
            /*if(gettype!=5) {
                try {
                    getcontent = new String(getcontent.getBytes(), "GBK");
                } catch (Exception ee) {
                }
            }else{
                try {
                    getcontent = new String(getcontent.getBytes("GBK"));
                } catch (Exception ee) {
                }
            }*/
            try {
                getcontent = new String(getcontent.getBytes(), "utf8");
            } catch (Exception ee) {
            }
            /*try {
                getcontent = new String(getcontent.getBytes("GBK"));
            } catch (Exception ee) {
            }*/
            pubfuncs.printerrormsg(gettype+",,,解码后的信息："+getcontent);
            //部署到windows（默认编码格式为gbk的那种）时需要加入的代码，在开发环境时不要加入，否则以为乱码了-结束
            if (getcontent.length() > 0) {
                List<Exchangerate> exchangerates = null;
                //if(para.urlgettype==1) {//自己获取
                if (gettype == 1) {
                    //exchangerates = pracontent("icbc", getcontent);
                    exchangerates = prajshcontent("icbc", getcontent);
                } else if (gettype == 2) {
                    exchangerates = prazhwhcontent("icbc", getcontent);
                    //pubfuncs.printerrormsg("格式化后的数据："+new Gson().toJson(exchangerates));
                } else if (gettype == 5) {
                    exchangerates = prazhgjscontent("icbc", getcontent);
                }
            /*}else{//远程获取
                exchangerates=new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")//24小时制
                        .create().fromJson(getcontent,new TypeToken<List<Exchangerate>>() {
                        }.getType());
            }*/
                if (exchangerates.size() > 0) {
                    //pubfuncs.printerrormsg(gettype+"，，，获取的行情信息："+new Gson().toJson(exchangerates));
                    if (gettype == 2)
                        pubfuncs.printerrormsg("数据处理开始");
                    hlsjtodb(bankname, exchangerates, gettype, getcontent);
                    if (gettype == 2)
                        pubfuncs.printerrormsg("数据处理结束");
                }
            } else {
                pubfuncs.printerrormsg("啊哈，" + bankname + "银行，数据类型：" + gettype + "的数据取不到了，请赶紧处理");
            }
        }catch (Exception ee){
            pubfuncs.printerrormsg("银行汇率处理（拉取）出错：" + ee.getMessage() + "，，，" + ee.getStackTrace());
        }
    }

    //银行汇率处理（推送），包括获取报文解析报文
    //gettype：3-外汇（网页获取），4-账户外汇（网页获取）
    public void banktsexchangerate(String bankname,String getcontent,int gettype){
        List<Exchangerate> exchangerates=null;
        if(gettype==3) {
            exchangerates = prawebwhcontent("icbc", getcontent);
        }else if(gettype==4) {
            exchangerates = prawebzhwhcontent("icbc", getcontent);
        }
        if(exchangerates.size()>0) {
            hlsjtodb(bankname,exchangerates,gettype,getcontent);
        }
    }

    //银行汇率存入数据库，1-外汇（内嵌页面获取），2-账户外汇（接口获取），3-外汇（网页获取），4-账户外汇（网页获取）
    public void hlsjtodb(String bankname,List<Exchangerate> exchangerates,int gettype,String getcontent){
        try {
            pubfuncs.printerrormsg("比对重复");
            //先剔除发布时间相较上次没有变化的,然后才返回
            exchangerates = getdefexchangerates(exchangerates);
            pubfuncs.printerrormsg("比对重复结束");
            if (exchangerates.size() > 0) {
                pubfuncs.printerrormsg("啊哈，" + bankname + "银行，外汇类型：" + gettype + "，获取到新数据，计" + exchangerates.size() + "条，准备写入数据库");
                Exchangeratecontent exchangeratecontent = new Exchangeratecontent();
                exchangeratecontent.setOrgid(2);
                exchangeratecontent.setContent(getcontent);
                exchangeratecontent.setDoctype(gettype);
                //报文数据只在出问题的时候再开进行保存，不然不保存，否则占用的磁盘太大，吃不消
                /*if (sqlmapper.insertexchangeratecontent(exchangeratecontent) > 0) {
                    //加上报文主键
                    for (int i = 0; i < exchangerates.size(); i++) {
                        exchangerates.get(i).setBwid(exchangeratecontent.getId());
                    }*/
                    //加上报文主键
                    for (int i = 0; i < exchangerates.size(); i++) {
                        exchangerates.get(i).setBwid(0l);
                    }
                    //pubfuncs.printerrormsg("获取"+bankname+"银行的汇率内容;" + new Gson().toJson(exchangerates));
                    if (sqlmapper.insertexchangerates(exchangerates) <= 0) {
                        pubfuncs.printerrormsg("啊哈，" + bankname + "银行，外汇类型：" + gettype + "增加汇率数据到数据库出错，请赶紧处理：" + new Gson().toJson(exchangerates));
                    }
                /*} else {
                    pubfuncs.printerrormsg("啊哈，" + bankname + "银行，外汇类型：" + gettype + "增加汇率报文到数据库出错，请赶紧处理：" + new Gson().toJson(exchangeratecontent));
                }*/
            }
        }catch (Exception ee){
            pubfuncs.printerrormsg("银行汇率存入数据库出错：" + ee.getMessage()+"，，，"+ee.getStackTrace());
        }
    }
    //从汇率列表中获取跟上次汇率快照不一样的汇率数据
    public List<Exchangerate> getdefexchangerates(List<Exchangerate> exchangerates){
        List<Exchangerate> newexchangerates=new ArrayList<Exchangerate>();
        try {
            //pubfuncs.printerrormsg("过滤前"+exchangerates.size()+","+lastexchangerates.size());
            //获取10个交易日前的时间
            Date nowtime = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(nowtime);
            Date fromdate = pubfuncs.getnjyrqsj(nowtime, 10);//把日期往前推10个交易日
            DateFormat format = new SimpleDateFormat("yyyyMMdd");
            boolean todelete10jyrago = false;//是否删除10交易日前的数据
            if (!(exchangerate.delete11dayagotodate.equals(format.format(new Date())))) {//今天的删除工作还没有执行
                todelete10jyrago = true;
                exchangerate.delete11dayagotodate = format.format(new Date());
            }
            //否则先剔除发布时间相较上次没有变化的,然后才返回

            //剔除发布时间相较上次没有变化的
            for (int i = 0; i < exchangerates.size(); i++) {
                //校验看币种是否已经存在，存在则返回序号，不存在则直接累加
                int bizhongindex = -1;
                for (int j = 0; j < lastexchangerates.size(); j++) {
                    if (exchangerates.get(i).getBzid() == lastexchangerates.get(j).getBzid()) {
                        bizhongindex = j;
                        if (((lastexchangerates.get(j).getXhmrj() != exchangerates.get(i).getXhmrj())) ||
                                ((lastexchangerates.get(j).getXhmcj() != exchangerates.get(i).getXhmcj())) ||
                                ((lastexchangerates.get(j).getXcmrj() != exchangerates.get(i).getXcmrj())) ||
                                ((lastexchangerates.get(j).getXcmcj() != exchangerates.get(i).getXcmcj()))) {
                            //发布时间不一样，且4个关键数据不一样，才认为已经更新
                            lastexchangerates.set(j, exchangerates.get(i));
                            newexchangerates.add(exchangerates.get(i));
                            //manyexchangerates.add(exchangerates.get(i));
                            //新增汇率信息的内存处理
                            pubfuncs.addhlxx(exchangerates.get(i), todelete10jyrago, fromdate);
                        }
                        break;
                    }
                }
                //没找到则直接加入到列表中
                if (bizhongindex == -1) {
                    lastexchangerates.add(exchangerates.get(i));
                    newexchangerates.add(exchangerates.get(i));
                    //manyexchangerates.add(exchangerates.get(i));
                    //新增汇率信息的内存处理
                    pubfuncs.addhlxx(exchangerates.get(i), todelete10jyrago, fromdate);
                }
            }

            //pubfuncs.printerrormsg("过滤后"+newexchangerates.size());
        }catch (Exception ee){
            pubfuncs.printerrormsg("从汇率列表中获取跟上次汇率快照不一样的汇率数据出错："+ee.getMessage()+"，，，"+ee.getStackTrace());
        }
        return newexchangerates;
    }
    //从汇率列表中获取每个可看币种最新的汇率数据
    public List<Exchangerate> getlastexchangerates(List<Exchangerate> exchangerates){
        //否则先剔除发布时间相较上次没有变化的,然后才返回
        List<Exchangerate> newexchangerates=new ArrayList<Exchangerate>();
        for (int i=0;i<sybizhongs.size();i++) {
            for (int j = exchangerates.size() - 1; j > -1; j--) {
                if(exchangerates.get(j).getBzid()==sybizhongs.get(i).getId()){
                    newexchangerates.add(exchangerates.get(j));
                    break;
                }
            }
        }
        return newexchangerates;
    }
    //获取最近n天的汇率数据
    public void getlastyeardata(String bankname,int days){
        Date nowdate=new Date();
        Calendar calendar = new GregorianCalendar();
        for(int i=0;i<days;i++){
            //n为毫秒数
            try { Thread.sleep ( 3000 ) ;
            } catch (InterruptedException ie){}
            pubfuncs.printerrormsg("往回取到距今"+i+"天银行的汇率数据");
            calendar.setTime(nowdate);
            calendar.add(calendar.DATE,(-1)*i);//把日期往后增加一天.整数往后推,负数往前移动
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            bankexchangerate(bankname,format.format(calendar.getTime()),"",1);
        }
        pubfuncs.printerrormsg("往回取距今"+days+"天银行的汇率数据完毕");
    }

    //把汇率数据从头到尾按时间排序
    public List<Exchangerate> indexexchangerate(List<Exchangerate> inexchangerate){
        List<Exchangerate> newexchangerate=new ArrayList<Exchangerate>();
        for(int i=0;i<inexchangerate.size();i++){
            int biggeri=-1;
            for(int j=0;j<newexchangerate.size();j++){
                if(newexchangerate.get(j).getFbsj().after(inexchangerate.get(i).getFbsj())){
                    biggeri=j;
                    break;
                }
            }
            if(biggeri>-1){
                newexchangerate.add(biggeri,inexchangerate.get(i));
            }else{
                newexchangerate.add(inexchangerate.get(i));
            }
        }
        return newexchangerate;
    }


    //按币种和时间获取汇率信息
    //从累积汇率列表中获取单个币种大于某个时间的汇率数据
    //public List<Bizhong> gethlbybzandsj(String bizhongidstr,String gettype){
    public List<Bizhong> gethlbybzandsj(String bizhongidstr,String getdf,String getdt, String gettype){
        //pubfuncs.printerrormsg("单币种每天最终过滤前"+manyexchangerates.size());
        //获取过滤汇率信息的时间段
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //查询的起始时间
        Date fromdate = null;
        //查询的结束时间
        Date todate=null;
        if(getdf!=null && getdt!=null && getdf.length()>0 && getdt.length()>0) {
            try {
                fromdate = format.parse(getdf);
                todate = format.parse(getdt);
            } catch (Exception ee) {
                pubfuncs.printerrormsg("按时间获取汇率信息，转换输入的日期时间出错：" + ee.getMessage());
            }
        }else if((gettype!=null) && (gettype.length()>0) && (!gettype.equals("last"))) {
            Date nowtime = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(nowtime);
            if (gettype.endsWith("year")) {
                //获取年数
                int yearn = Integer.parseInt(gettype.substring(0, gettype.length() - 4));
                calendar.add(calendar.YEAR, (-1) * yearn);//把日期往前推一年
                fromdate = calendar.getTime();
            } else if (gettype.endsWith("month")) {
                //获取月数
                int monn = Integer.parseInt(gettype.substring(0, gettype.length() - 5));
                calendar.add(calendar.MONTH, (-1) * monn);//把日期往前推一月
                fromdate = calendar.getTime();
            } else if (gettype.endsWith("day")) {
                //获取交易日数
                int jyrn = Integer.parseInt(gettype.substring(0, gettype.length() - 3));
                fromdate = pubfuncs.getnjyrqsj(nowtime, jyrn);//把日期往前推n个交易日
            } else if (gettype.endsWith("hour")) {
                //获取交易小时数
                int jyxsn = Integer.parseInt(gettype.substring(0, gettype.length() - 4));
                fromdate = pubfuncs.getnjyxssj(nowtime, jyxsn);//把日期往前推n个交易小时
            }


            calendar.setTime(nowtime);
            calendar.add(calendar.DATE, 1);//把日期往后推1天
            todate = calendar.getTime();

        }
        //获取需要的币种
        List<Bizhong> tempbizhongs=new ArrayList<Bizhong>();
        //获取所有币种信息
        List<Bizhong> tempsybz=exchangerate.sybizhongs;
        if(bizhongidstr==null || bizhongidstr.length()<=0) {
            bizhongidstr = exchangerate.kkktzbizhongidstr;
        }
        int tempsybzcount=tempsybz.size();
        for(int i=0;i<tempsybzcount;i++) {
            if((","+bizhongidstr+",").indexOf(","+tempsybz.get(i).getId()+",")>-1){
                Bizhong tempbizhong=tempsybz.get(i);
                Bizhong newbizhong=new Bizhong();
                newbizhong.setId(tempbizhong.getId());
                newbizhong.setBzname(tempbizhong.getBzname());
                newbizhong.setBzname2(tempbizhong.getBzname2());
                newbizhong.setOrgid(tempbizhong.getOrgid());
                newbizhong.setBzstatus(tempbizhong.getBzstatus());
                newbizhong.setXmrhmclvrunalavalue(tempbizhong.getXmrhmclvrunalavalue());
                newbizhong.setXmrhmclvrunlvalavalue(tempbizhong.getXmrhmclvrunlvalavalue());
                newbizhong.setXmchmrlvrunalavalue(tempbizhong.getXmchmrlvrunalavalue());
                newbizhong.setXmchmrlvrunlvalavalue(tempbizhong.getXmchmrlvrunlvalavalue());
                newbizhong.setXmrhmcyhmrjalavalue(tempbizhong.getXmrhmcyhmrjalavalue());
                newbizhong.setXmrhmcyhmcjalavalue(tempbizhong.getXmrhmcyhmcjalavalue());
                newbizhong.setXmchmryhmrjalavalue(tempbizhong.getXmchmryhmrjalavalue());
                newbizhong.setXmchmryhmcjalavalue(tempbizhong.getXmchmryhmcjalavalue());
                newbizhong.setSjpyl(tempbizhong.getSjpyl());
                newbizhong.setSqmc(tempbizhong.getSqmc());
                newbizhong.setBzzdgl(tempbizhong.getBzzdgl());
                newbizhong.setZghlcs(tempbizhong.getZghlcs());
                newbizhong.setZdhlcs(tempbizhong.getZdhlcs());
                newbizhong.setXhmrj(tempbizhong.getXhmrj());
                newbizhong.setXcmrj(tempbizhong.getXcmrj());
                newbizhong.setXhmcj(tempbizhong.getXhmcj());
                newbizhong.setXcmcj(tempbizhong.getXcmcj());
                newbizhong.setFbsj(tempbizhong.getFbsj());
                newbizhong.setZdf1day(tempbizhong.getZdf1day());
                newbizhong.setZdf2day(tempbizhong.getZdf2day());
                newbizhong.setZdf3day(tempbizhong.getZdf3day());
                newbizhong.setZgxhmrj(tempbizhong.getZgxhmrj());
                newbizhong.setPjzgxhmrj(tempbizhong.getPjzgxhmrj());
                newbizhong.setTjzgxhmrj(tempbizhong.getTjzgxhmrj());
                newbizhong.setZdxhmcj(tempbizhong.getZdxhmcj());
                newbizhong.setPjzdxhmcj(tempbizhong.getPjzdxhmcj());
                newbizhong.setTjzdxhmcj(tempbizhong.getTjzdxhmcj());
                if(getdf!=null && getdt!=null && getdf.length()>0 && getdt.length()>0) {//按起止时间
                    if((todate.getTime()-fromdate.getTime())>(1000l*24l*3600l*26l)){//当截止时间比开始时间大26天以上时按天取获取数据
                        List<Exchangerate> tempdayexchangerate=tempbizhong.getDaydata();
                        //汇率数据条数
                        int tempdayexchangeratecount=tempdayexchangerate.size();
                        List<Exchangerate> newdayexchangerate=new ArrayList<Exchangerate>();
                        /*for(int j=tempdayexchangeratecount-1;j>-1;j--){
                            if(tempdayexchangerate.get(j).getFbsj().after(fromdate) && tempdayexchangerate.get(j).getFbsj().before(todate)){
                                newdayexchangerate.add(0,tempdayexchangerate.get(j));
                            }else{
                                break;
                            }
                        }*/
                        for(int j=0;j<tempdayexchangeratecount-1;j++){
                            if(tempdayexchangerate.get(j).getFbsj().before(todate)){
                                if(tempdayexchangerate.get(j).getFbsj().after(fromdate)) {
                                    newdayexchangerate.add(tempdayexchangerate.get(j));
                                }
                            }else{
                                break;
                            }
                        }
                        newbizhong.setFhhldata(newdayexchangerate);
                    }else{//否则取每个汇率值
                        List<Exchangerate> tempsecondexchangerate=tempbizhong.getSeconddata();
                        //汇率数据条数
                        int tempsecondexchangeratecount=tempsecondexchangerate.size();
                        List<Exchangerate> newsecondexchangerate=new ArrayList<Exchangerate>();
                        //如果是1-5个交易日则从后到前遍历符合条件的记录
                        /*for(int j=tempsecondexchangeratecount-1;j>-1;j--){
                            if(tempsecondexchangerate.get(j).getFbsj().after(fromdate) && tempsecondexchangerate.get(j).getFbsj().before(todate)){
                                newsecondexchangerate.add(0,tempsecondexchangerate.get(j));
                            }else{
                                break;
                            }
                        }*/
                        for(int j=0;j<tempsecondexchangeratecount-1;j++){
                            if(tempsecondexchangerate.get(j).getFbsj().before(todate)){
                                if(tempsecondexchangerate.get(j).getFbsj().after(fromdate)) {
                                    newsecondexchangerate.add(tempsecondexchangerate.get(j));
                                }
                            }else{
                                break;
                            }
                        }
                        newbizhong.setFhhldata(newsecondexchangerate);
                    }
                }else if(gettype!=null && gettype.length()>0) {//按标志，比如1交易日，一个月等
                    if(gettype.endsWith("day")||gettype.endsWith("hour")){//如果是按天或按小时获取的
                        List<Exchangerate> tempsecondexchangerate=tempbizhong.getSeconddata();
                        //汇率数据条数
                        int tempsecondexchangeratecount=tempsecondexchangerate.size();
                        List<Exchangerate> newsecondexchangerate=new ArrayList<Exchangerate>();
                        //如果是1-5个交易日则从后到前遍历符合条件的记录
                        for(int j=tempsecondexchangeratecount-1;j>-1;j--){
                            if(tempsecondexchangerate.get(j).getFbsj().after(fromdate)){
                                newsecondexchangerate.add(0,tempsecondexchangerate.get(j));
                            }else{
                                break;
                            }
                        }
                        newbizhong.setFhhldata(newsecondexchangerate);
                    }else if(gettype.equals("last")){//如果是只获取最新汇率的
                        /*List<Exchangerate> tempsecondexchangerate=tempbizhong.getSeconddata();
                        //汇率数据条数
                        int tempsecondexchangeratecount=tempsecondexchangerate.size();
                        List<Exchangerate> newsecondexchangerate=new ArrayList<Exchangerate>();
                        //如果是1-5个交易日则从后到前遍历符合条件的记录
                        if(tempsecondexchangeratecount>0)
                            newsecondexchangerate.add(tempsecondexchangerate.get(tempsecondexchangeratecount-1));*/
                        List<Exchangerate> newsecondexchangerate=new ArrayList<Exchangerate>();
                        for(int j=0;j<lastexchangerates.size();j++){
                            if(lastexchangerates.get(j).getBzid()==tempbizhong.getId()){
                                newsecondexchangerate.add(lastexchangerates.get(j));
                                break;
                            }
                        }
                        newbizhong.setFhhldata(newsecondexchangerate);
                    }else{//如果不是按天获取的
                        List<Exchangerate> tempdayexchangerate=tempbizhong.getDaydata();
                        //汇率数据条数
                        int tempdayexchangeratecount=tempdayexchangerate.size();
                        List<Exchangerate> newdayexchangerate=new ArrayList<Exchangerate>();
                        for(int j=tempdayexchangeratecount-1;j>-1;j--){
                            if(tempdayexchangerate.get(j).getFbsj().after(fromdate)){
                                newdayexchangerate.add(0,tempdayexchangerate.get(j));
                            }else{
                                break;
                            }
                        }
                        newbizhong.setFhhldata(newdayexchangerate);
                    }
                }else{//获取最新汇率
                    /*List<Exchangerate> tempsecondexchangerate=tempbizhong.getSeconddata();
                    //汇率数据条数
                    int tempsecondexchangeratecount=tempsecondexchangerate.size();
                    List<Exchangerate> newsecondexchangerate=new ArrayList<Exchangerate>();
                    //如果是1-5个交易日则从后到前遍历符合条件的记录
                    if(tempsecondexchangeratecount>0)
                        newsecondexchangerate.add(tempsecondexchangerate.get(tempsecondexchangeratecount-1));*/
                    List<Exchangerate> newsecondexchangerate=new ArrayList<Exchangerate>();
                    for(int j=0;j<lastexchangerates.size();j++){
                        if(lastexchangerates.get(j).getBzid()==tempbizhong.getId()){
                            newsecondexchangerate.add(lastexchangerates.get(j));
                            break;
                        }
                    }
                    newbizhong.setFhhldata(newsecondexchangerate);
                }
                tempbizhongs.add(newbizhong);
            }
        }

        return tempbizhongs;
    }
    //获取并解析中国人民银行每个交易日发布的汇率中间价
    /*public void getrmyhhlzjj(){
        String getzjjlist= webdoa.gethtmlfromurl("http://www.pbc.gov.cn/zhengcehuobisi/125207/125217/125925/index.html", "GET");
        pubfuncs.printerrormsg("获取到的内容："+getzjjlist);
        //DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        //获取今日中间价链接标题的起始位置
        int huilvzjjindex=getzjjlist.indexOf("2020年7月24日中国外汇交易中心受权公布人民币汇率中间价公告");
        if(huilvzjjindex>-1){//找到中间价标记
            //开始获取中间价链接
            String hrefinstr=getzjjlist.substring(0,huilvzjjindex);
            hrefinstr=hrefinstr.substring(hrefinstr.toLowerCase().lastIndexOf("href=\"")+6);
            hrefinstr=hrefinstr.substring(0,hrefinstr.indexOf("\""));
            pubfuncs.printerrormsg("获取的中间价链接："+hrefinstr);
        }else{
            pubfuncs.printerrormsg("找不到中间价链接");
        }
        //6.Jsoup解析html
        *//*Element element =document.getElementById("foreignEXList");
        Document document = Jsoup.parse(getzjjlist);
        Elements trs=element.getElementsByTag("tr");
        Elements ths=trs.get(0).getElementsByTag("th");
        //pubfuncs.printerrormsg("解析的数据："+tds.get(0).text()+","+tds.get(1).text()+","+tds.get(2).text()+","+tds.get(3).text()+","+tds.get(4).text()+","+tds.get(5).text());
        if(ths.size()==9) {
            if ((ths.get(0).text() + "," + ths.get(1).text() + "," + ths.get(2).text() + "," + ths.get(3).text() + "," + ths.get(4).text() + "," + ths.get(5).text()+ "," + ths.get(6).text() + "," + ths.get(7).text() + "," + ths.get(8).text()).equals("品种,涨跌,银行 买入价,银行 卖出价,中间价,当日 涨跌值,当日 涨跌幅,当年 涨跌幅,操作")) {
                if (trs.size() > 2) {*//*
    }*/
}
