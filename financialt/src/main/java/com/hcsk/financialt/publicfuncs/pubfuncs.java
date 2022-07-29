package com.hcsk.financialt.publicfuncs;


import com.google.gson.Gson;
import com.hcsk.financialt.controller.timerdo;
import com.hcsk.financialt.masterdb.dao.sqlMapper;
import com.hcsk.financialt.pojo.Bizhong;
import com.hcsk.financialt.pojo.Exchangerate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class pubfuncs {
    @Autowired
    sqlMapper sqlmapper;
    @Autowired
    exchangerate exchangeratea;
    //读取文本文件
    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    //从nginx配置文件里获取除特定ip外的第一个ip
    public static String getoneipfromnginx(String webname){
        //String nginxstr=readFileContent("D:/nginx.conf");
        String nginxstr="";
        if(new File("/etc/nginx/nginx.conf").exists()) {
            readFileContent("/etc/nginx/nginx.conf");
            boolean findupstream = false;
            boolean findupserver = false;
            String tempstr = "";
            while (nginxstr.indexOf("upstream") > -1) {
                nginxstr = nginxstr.substring(nginxstr.indexOf("upstream") + 8);
                if (nginxstr.indexOf(" " + webname) > -1 && nginxstr.indexOf("{") > -1) {
                    nginxstr = nginxstr.substring(nginxstr.indexOf("{") + 1, nginxstr.indexOf("}"));
                }
            }
        }
        return nginxstr;
    }

    public static String getparafromprofile(String proname) {//按属性名称获取配置文件里的参数
        try{
            Resource resource = new ClassPathResource("application.properties");
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            return props.getProperty(proname);
        }catch(Exception ee){
            pubfuncs.printerrormsg("按属性名称获取配置文件里的参数出错："+ proname+"，"+ee.getMessage());
        }
        return "";
    }

    public static String getosname() {//获取操作系统
        //获取操作系统系统信息
        Properties prop = System.getProperties();
        String osname = prop.getProperty("os.name");
        //pubfuncs.printerrormsg("操作系统："+osname);
        if (osname != null && osname.toLowerCase().indexOf("linux") > -1) {
            return "linux";
        }else
            return osname;
    }
    public static String toChinese(String str) { // 进行转码操作
        if (str == null)
            str = "";
        try {
            str = new String(str.getBytes("ISO-8859-1"), "gb2312");
        } catch (Exception e) {
            str = "";
            e.printStackTrace();
        }
        return str;
    }

    public static String decodeUTF8(String str) { // 进行转码操作
        if (str == null)
            str = "";
        try {
            if (!(str.equals(""))) {
                str = str.trim();
            }
            if (str.equals(new String(str.getBytes("ISO-8859-1"), "ISO-8859-1"))) {
                str = new String(str.getBytes("ISO-8859-1"), "utf-8");
            }
        } catch (Exception e) {
            str = "";
            e.printStackTrace();
        }
        return str;
    }
    //判断输入的字符串是否全部由数字组成
    public static boolean isallnum(String instr) {
        boolean resultvalue=true;
        int instrlen=instr.length()-1;
        for(int i=0;i<instrlen;i++) {
            if("0123456789".indexOf(instr.substring(i, i+1))<0) {
                resultvalue=false;
                break;
            }
        }
        return resultvalue;
    }

    /**
     * 字符串替换，从头到尾查询一次，替换后的字符串不检查
     * @param str     源字符串
     * @param oldStr  目标字符串
     * @param newStr  替换字符串
     * @return        替换后的字符串
     */
    public static String replaceAll(String str, String oldStr, String newStr){
        if(newStr!=null) {
            int i = str.indexOf(oldStr);
            while (i != -1) {
                str = str.substring(0, i) + newStr + str.substring(i + oldStr.length());
                i = str.indexOf(oldStr, i + newStr.length());
            }
        }
        return str;
    }
    /**
     * 字符串替换，左边第一个。
     * @param str     源字符串
     * @param oldStr  目标字符串
     * @param newStr  替换字符串
     * @return        替换后的字符串
     */
    public static String replaceFirst(String str, String oldStr, String newStr){
        int i = str.indexOf(oldStr);
        if (i == -1)
            return str;
        str = str.substring(0, i) + newStr + str.substring(i + oldStr.length());
        return str;
    }
    //生成md5加密值
    public static String getmd5(String instr) {
        return org.springframework.util.DigestUtils.md5DigestAsHex((instr).getBytes());
    }

    //是否正确的手机号，直接根据运营商已经开放的手机号段来判断
    public static boolean isvalidcell(String cellphone) {
        if((cellphone.length()==11)) {
            if(
                //移动号段
                    cellphone.startsWith("134") || cellphone.startsWith("135") || cellphone.startsWith("136")
                            || cellphone.startsWith("137") || cellphone.startsWith("138") || cellphone.startsWith("139")
                            || cellphone.startsWith("147") || cellphone.startsWith("148") || cellphone.startsWith("150")
                            || cellphone.startsWith("151") || cellphone.startsWith("152") || cellphone.startsWith("157")
                            || cellphone.startsWith("158") || cellphone.startsWith("159") || cellphone.startsWith("165")
                            || cellphone.startsWith("172") || cellphone.startsWith("178") || cellphone.startsWith("182")
                            || cellphone.startsWith("183") || cellphone.startsWith("184") || cellphone.startsWith("187")
                            || cellphone.startsWith("188") || cellphone.startsWith("198")
                            //联通号段
                            || cellphone.startsWith("130") || cellphone.startsWith("131") || cellphone.startsWith("132")
                            || cellphone.startsWith("145") || cellphone.startsWith("146") || cellphone.startsWith("155")
                            || cellphone.startsWith("156") || cellphone.startsWith("166") || cellphone.startsWith("171")
                            || cellphone.startsWith("175") || cellphone.startsWith("176") || cellphone.startsWith("185")
                            || cellphone.startsWith("186")
                            //电信号段
                            || cellphone.startsWith("133") || cellphone.startsWith("149") || cellphone.startsWith("153")
                            || cellphone.startsWith("173") || cellphone.startsWith("174") || cellphone.startsWith("177")
                            || cellphone.startsWith("180") || cellphone.startsWith("181") || cellphone.startsWith("189")
                            || cellphone.startsWith("199")) {
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    //简单的换位加密（确切的说算不上加密，只能说混淆吧）
    public static String getnewstr(String instr) {
        String returnvalue="";
        for(int i = 0;i<instr.length();i++) {
            String tempstr=instr.substring(i, i+1);
            if (tempstr.equals("0"))
                returnvalue=returnvalue+"5";
            else if (tempstr.equals("1"))
                returnvalue=returnvalue+"7";
            else if (tempstr.equals("2"))
                returnvalue=returnvalue+"1";
            else if (tempstr.equals("3"))
                returnvalue=returnvalue+"6";
            else if (tempstr.equals("4"))
                returnvalue=returnvalue+"2";
            else if (tempstr.equals("5"))
                returnvalue=returnvalue+"3";
            else if (tempstr.equals("6"))
                returnvalue=returnvalue+"9";
            else if (tempstr.equals("7"))
                returnvalue=returnvalue+"4";
            else if (tempstr.equals("8"))
                returnvalue=returnvalue+"0";
            else if (tempstr.equals("9"))
                returnvalue=returnvalue+"8";
        }
        return returnvalue;
    }
    //判断传入的是否是IP地址
    public static boolean isip(String instr) {
        int len=instr.length();
        for(int i=0;i<len;i++)
            if("0123456789.".indexOf(instr.substring(i, i+1))<0)
                return false;
        return true;
    }

    /**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     *
     */
    public static String subString(String str, String strStart, String strEnd) {
        String returnvalue="";
        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex > -1 && strEndIndex > -1) {
            returnvalue = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        }

        return returnvalue;
    }


    //打印错误信息
    public static void printerrormsg(String inmsg){
        Logger logger = LoggerFactory.getLogger(pubfuncs.class);
        //System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+":"+inmsg);
        logger.debug(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+":"+inmsg);
    }
    //判断字符串里是否包含指定的关键字字符串数组里的一个
    public static boolean isincludekeyword(String[] keywords,String instr){
        for(int i=0;i<keywords.length;i++){
            if(instr.indexOf(keywords[i])>-1)
                return true;
        }
        return false;
    }

    //获取某个时间的n个交易日前的时间
    public static Date getnjyrqsj(Date nowtime,int n){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(nowtime);
        int i=0;
        while(i<n){
            //时间减去1天
            calendar.add(calendar.DATE, -1);//把日期往前推1天
            //获取时间的星期
            int dnday=calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if(dnday!=0 && dnday!=6){
                i=i+1;
            }
        }
        return calendar.getTime();
    }
    //判断一个时间是否休市时间， gettype：1-警报用的休市时间，这个时间跟银行协议的一样，2-获取汇率用的休市时间，因为在周一
    //银行的结售汇其实早于7点就开始提供数据了，在周六银行的结售汇迟于4点还在提供数据
    public static boolean adateisres(Date indate,int gettype){
        boolean tempisrestdate = true;
        try{
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(indate);
            //获取时间的星期
            int dnday = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            if (dnday >= 2 && dnday <= 5) {//周二到周五
                tempisrestdate = false;
            } else if (dnday == 1) {//周一
                DateFormat format = new SimpleDateFormat("HH");
                int hourint = Integer.parseInt(format.format(indate));
                if ((gettype==1 && hourint >= 7 && hourint <= 24)||(gettype==2 && hourint >= 6 && hourint <= 24)) {
                    tempisrestdate = false;
                }
            } else {
                if (dnday == 6) {//周六
                    DateFormat format = new SimpleDateFormat("HH");
                    int hourint = Integer.parseInt(format.format(indate));
                    if ((gettype==1 && hourint >= 0 && hourint <= 3)||(gettype==2 && hourint >= 0 && hourint <= 4)) {
                        tempisrestdate = false;
                    }
                }
            }
        }catch (Exception ee){
            pubfuncs.printerrormsg("休市获取出错："+ee.getMessage());
        }
        return tempisrestdate;
    }
    //获取某个时间的n个交易小时前的时间
    public static Date getnjyxssj(Date nowtime,int n){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(nowtime);
        int i=0;
        while(i<n){
            //时间减去1小时
            calendar.add(calendar.HOUR, -1);//把日期往前推1个小时
            //获取时间的星期
            if(!adateisres(calendar.getTime(),1)){
                i=i+1;
            }
        }
        return calendar.getTime();
    }
    //加载各币种
    public void inibizhong(){
        exchangerate.sybizhongs=sqlmapper.getbizhongs(0,"");

        //可看不可投资币种
        exchangerate.kkbktzbizhongs.clear();
        exchangerate.kkbktzbizhongidstr="";

        //可看可投资币种
        exchangerate.kkktzbizhongs.clear();
        exchangerate.kkktzbizhongidstr="";
        exchangerate.kkktzbizhongnamestr="";
        //所有可看币种
        exchangerate.sykkbizhongs.clear();
        exchangerate.sykkbizhongidstr="";

        //所有币种的id累加字符串
        exchangerate.sybizhongidstr="";
        //所有币种名称集合
        exchangerate.sybizhongname.clear();
        //所有币种的主键集合
        exchangerate.sybizhongid.clear();

        for(int i=0;i<exchangerate.sybizhongs.size();i++){
            //所有币种的id累加字符串
            exchangerate.sybizhongidstr=exchangerate.sybizhongidstr+(exchangerate.sybizhongidstr.length()>0?",":"")+exchangerate.sybizhongs.get(i).getId();
            //所有币种名称集合
            exchangerate.sybizhongname.add(exchangerate.sybizhongs.get(i).getBzname());
            //所有币种名称2集合
            exchangerate.sybizhongname2.add(exchangerate.sybizhongs.get(i).getBzname2());
            //所有币种名称3集合
            //exchangerate.sybizhongname3.add(exchangerate.sybizhongs.get(i).getBzname2().substring(0,exchangerate.sybizhongs.get(i).getBzname2().length()-1));
            String bzname=exchangerate.sybizhongs.get(i).getBzname2();
            exchangerate.sybizhongname3.add(bzname.indexOf("账户")>-1?bzname.substring(0,bzname.indexOf("账户")+3):bzname.substring(0,bzname.length()-1));
            //pubfuncs.printerrormsg("币种3："+exchangerate.sybizhongs.get(i).getBzname2().substring(0,exchangerate.sybizhongs.get(i).getBzname2().length()-1));
            //所有币种的主键集合
            exchangerate.sybizhongid.add(exchangerate.sybizhongs.get(i).getId());
            if(exchangerate.sybizhongs.get(i).getBzstatus()==1){//可看可投资
                exchangerate.kkktzbizhongs.add(exchangerate.sybizhongs.get(i));
                exchangerate.kkktzbizhongidstr=exchangerate.kkktzbizhongidstr+(exchangerate.kkktzbizhongidstr.length()>0?",":"")+exchangerate.sybizhongs.get(i).getId();
                exchangerate.kkktzbizhongnamestr=exchangerate.kkktzbizhongnamestr+(exchangerate.kkktzbizhongnamestr.length()>0?",":"")+exchangerate.sybizhongs.get(i).getBzname();
                //所有可看币种
                exchangerate.sykkbizhongs.add(exchangerate.sybizhongs.get(i));
                exchangerate.sykkbizhongidstr=exchangerate.sykkbizhongidstr+(exchangerate.sykkbizhongidstr.length()>0?",":"")+exchangerate.sybizhongs.get(i).getId();
            }else if(exchangerate.sybizhongs.get(i).getBzstatus()==2) {//可看不可投资
                exchangerate.kkbktzbizhongs.add(exchangerate.sybizhongs.get(i));
                exchangerate.kkbktzbizhongidstr=exchangerate.kkbktzbizhongidstr+(exchangerate.kkbktzbizhongidstr.length()>0?",":"")+exchangerate.sybizhongs.get(i).getId();
                //所有可看币种
                exchangerate.sykkbizhongs.add(exchangerate.sybizhongs.get(i));
                exchangerate.sykkbizhongidstr=exchangerate.sykkbizhongidstr+(exchangerate.sykkbizhongidstr.length()>0?",":"")+exchangerate.sybizhongs.get(i).getId();
            }
        }
    }
    //获取每个币种每天汇率最高和最低的分布时段
    public void getbzhighhlhour(){
        DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String datestr=format1.format(new Date());
        if(!timerdo.fxtodate.equals(datestr)) {
            timerdo.fxtodate=datestr;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = new GregorianCalendar();
            //每个币种每天汇率最高的一条记录
            List<String> bzhhlhours = sqlmapper.getbzhighhleveryday();
            //每个币种每天汇率最低的一条记录
            List<String> bzlhlhours = sqlmapper.getbzlowhleveryday();
            //按币种来一个个计算
            for (int i = 0; i < exchangerate.kkktzbizhongs.size(); i++) {

                //分析最高汇率

                //获取每个币种的最高汇率时段分布数组
                int[] bzhhls = exchangerate.kkktzbizhongs.get(i).getZghlcs();
                //重置每个币种的最高汇率时段分布数组的每时段的值为0
                for (int j = 0; j < bzhhls.length; j++) {
                    bzhhls[j] = 0;
                }
                //总的有效次数
                int totaltimes = 0;
                //开始进行汇总
                for (int j = 0; j < bzhhlhours.size(); j++) {
                    String hourhhl = bzhhlhours.get(j);
                    if (hourhhl.startsWith(exchangerate.kkktzbizhongs.get(i).getId() + ";")) {//币种id一样
                        try {
                            Date hhlsj = format.parse(hourhhl.substring(hourhhl.indexOf(";") + 1) + ":00:01");
                            calendar.setTime(hhlsj);
                            //获取时间的星期
                            int dnday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                            if (dnday != 6) {//要除去星期六，因为星期六只有0-4小时，没有代表性
                                int houri = Integer.parseInt(hourhhl.substring(hourhhl.indexOf(" ") + 1));//获取时间的小时部分
                                bzhhls[houri] = bzhhls[houri] + 1;
                                totaltimes = totaltimes + 1;
                            }
                        } catch (Exception ee) {
                            pubfuncs.printerrormsg("最高汇率时间转换出错：" + ee.getMessage());
                        }
                    }
                }
                //生成分析结果
                String fxjg = "";
                for (int j = 0; j < bzhhls.length; j++) {
                    if (bzhhls[j] > 0) {
                        fxjg = fxjg + (fxjg.length() > 0 ? "," : "") + j + "时次数：" + bzhhls[j];
                    }
                }
                //生成分析结果
                fxjg = "汇率最高的总次数：" + totaltimes + ";" + fxjg;
                exchangerate.kkktzbizhongs.get(i).setBzzdgl(fxjg);

                //分析最低汇率

                //获取每个币种的最低汇率时段分布数组
                int[] bzlhls = exchangerate.kkktzbizhongs.get(i).getZdhlcs();
                //重置每个币种的最低汇率时段分布数组的每时段的值为0
                for (int j = 0; j < bzlhls.length; j++) {
                    bzlhls[j] = 0;
                }
                //总的有效次数
                totaltimes = 0;
                //开始进行汇总
                for (int j = 0; j < bzlhlhours.size(); j++) {
                    String hourlhl = bzlhlhours.get(j);
                    if (hourlhl.startsWith(exchangerate.kkktzbizhongs.get(i).getId() + ";")) {//币种id一样
                        try {
                            Date hhlsj = format.parse(hourlhl.substring(hourlhl.indexOf(";") + 1) + ":00:01");
                            calendar.setTime(hhlsj);
                            //获取时间的星期
                            int dnday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                            if (dnday != 6) {//要除去星期六，因为星期六只有0-4小时，没有代表性
                                int houri = Integer.parseInt(hourlhl.substring(hourlhl.indexOf(" ") + 1));//获取时间的小时部分
                                bzlhls[houri] = bzlhls[houri] + 1;
                                totaltimes = totaltimes + 1;
                            }
                        } catch (Exception ee) {
                            pubfuncs.printerrormsg("最低汇率时间转换出错：" + ee.getMessage());
                        }
                    }
                }
                //生成分析结果
                fxjg = "";
                for (int j = 0; j < bzlhls.length; j++) {
                    if (bzlhls[j] > 0) {
                        fxjg = fxjg + (fxjg.length() > 0 ? "," : "") + j + "时次数：" + bzlhls[j];
                    }
                }
                //生成分析结果
                fxjg = "汇率最低的总次数：" + totaltimes + ";" + fxjg;
                exchangerate.kkktzbizhongs.get(i).setBzzdgl(exchangerate.kkktzbizhongs.get(i).getBzzdgl() + ";" + fxjg);
            }
            sqlmapper.updatehlfxsj(exchangerate.kkktzbizhongs);
        }
    }
    //每个币种即将到达的最高点和最低点预测
    public static void bzzgzdyuce(){

        //只利用30个交易日内的数据来进行计算
        Date fromdate=getnjyrqsj(new Date(),30);
        List<Bizhong> tempsybizhongs=exchangerate.sybizhongs;

        int tempsybizhongscount=tempsybizhongs.size();
        for(int i=0;i<tempsybizhongscount;i++){
            float zgxhmrjyinzi=0;//最高现汇买入价因子
            float zdxhmcjyinzi=1000000;//最低现汇卖出价因子
            float zgxhmrj=0;//最高现汇买入价
            float zdxhmcj=100000;//最低现汇卖出价
            List<Float> xhmrjzgjs=new ArrayList<Float>();//现汇买入价最高点集合
            List<Float> xhmcjzdjs=new ArrayList<Float>();//现汇卖出价最高点集合
            List<Exchangerate> tempdaydata=tempsybizhongs.get(i).getDaydata();
            int tempdaydatacount=tempdaydata.size();
            for(int j=tempdaydatacount-1;j>-1;j--){
                if(tempdaydata.get(j).getFbsj().after(fromdate)){
                    if(tempdaydata.get(j).getXhmrj()>zgxhmrj){//获取现汇最高买入价
                        zgxhmrj=tempdaydata.get(j).getXhmrj();
                    }
                    //获取周期内现汇最高买入价集合
                    if(tempdaydata.get(j).getXhmrj()>zgxhmrjyinzi){
                        zgxhmrjyinzi=tempdaydata.get(j).getXhmrj();
                    }else{
                        if((j-1>-1 && tempdaydata.get(j-1).getXhmrj()>zgxhmrjyinzi)){

                        }else{
                            xhmrjzgjs.add(zgxhmrjyinzi);
                            zgxhmrjyinzi=0;
                        }
                    }
                    if(tempdaydata.get(j).getXhmcj()<zdxhmcj){//获取现汇最高买入价
                        zdxhmcj=tempdaydata.get(j).getXhmcj();
                    }
                    //获取周期内现汇最低卖出价集合
                    if(tempdaydata.get(j).getXhmrj()<zdxhmcjyinzi){
                        zdxhmcjyinzi=tempdaydata.get(j).getXhmrj();
                    }else{
                        if((j-1>-1 && tempdaydata.get(j-1).getXhmrj()<zdxhmcjyinzi)){

                        }else{
                            xhmcjzdjs.add(zdxhmcjyinzi);
                            zdxhmcjyinzi=1000000;
                        }
                    }
                }else{
                    break;
                }
            }
            float zgxmmrjtotal=0;//最高现汇买入价总和
            float zdxmmcjtotal=0;//最低现汇卖出价总和
            for(int k=0;k<xhmrjzgjs.size();k++){
                zgxmmrjtotal=zgxmmrjtotal+xhmrjzgjs.get(k);//累加最高现汇买入价
            }
            for(int k=0;k<xhmcjzdjs.size();k++){
                zdxmmcjtotal=zdxmmcjtotal+xhmcjzdjs.get(k);//累加最高现汇买入价
            }
            //pubfuncs.printerrormsg(tempsybizhongs.get(i).getBzname()+","+zdxmmcjtotal+","+xhmcjzdjs.size());
            //pubfuncs.printerrormsg(tempsybizhongs.get(i).getBzname()+","+zgxmmrjtotal+","+xhmrjzgjs.size());
            float zgxmmrjpjz=xhmrjzgjs.size()>0?zgxmmrjtotal/xhmrjzgjs.size():0;//最高现汇买入价平均值
            float zdxmmcjpjz=xhmcjzdjs.size()>0?zdxmmcjtotal/xhmcjzdjs.size():0;////最低现汇卖出价平均值
            float zgxmmrjtjz=zgxmmrjpjz+((zgxhmrj-zgxmmrjpjz)*0.3f);//最高现汇买入价平均值往最高值上靠最高值与平均值差值的30%
            float zdxmmcjtjz=zdxmmcjpjz-((zdxmmcjpjz-zdxhmcj)*0.3f);//最低现汇卖出价推荐值（平均值往最低值下靠最低值与平均值差值的30%）
            tempsybizhongs.get(i).setZgxhmrj(zgxhmrj);
            tempsybizhongs.get(i).setPjzgxhmrj(zgxmmrjpjz);
            tempsybizhongs.get(i).setTjzgxhmrj(zgxmmrjtjz);
            tempsybizhongs.get(i).setZdxhmcj(zdxhmcj);
            tempsybizhongs.get(i).setPjzdxhmcj(zdxmmcjpjz);
            tempsybizhongs.get(i).setTjzdxhmcj(zdxmmcjtjz);
        }
    }
    //新增汇率信息的内存处理,inexchangerate-传入的汇率信息，todelete-是否清除10交易日前的数据，jyr10ago-10交易日前的时间
    public static void addhlxx(Exchangerate inexchangerate,boolean todelete,Date jyr10ago){
        try {
            DateFormat format = new SimpleDateFormat("yyyyMMdd");
            //获取输入币种在所有币种内存中的位置
            int exchangeratei = exchangerate.sybizhongid.indexOf(inexchangerate.getBzid());
            //获取币种数据
            Bizhong tempbizhong = exchangerate.sybizhongs.get(exchangeratei);
            Bizhong newbizhong = new Bizhong();

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
            newbizhong.setFbsj(inexchangerate.getFbsj());
            newbizhong.setXhmrj(inexchangerate.getXhmrj());
            newbizhong.setXcmrj(inexchangerate.getXcmrj());
            newbizhong.setXhmcj(inexchangerate.getXhmcj());
            newbizhong.setXcmcj(inexchangerate.getXcmcj());
            newbizhong.setDaydata(tempbizhong.getDaydata());
            newbizhong.setSeconddata(tempbizhong.getSeconddata());
            newbizhong.setZgxhmrj(tempbizhong.getZgxhmrj());
            newbizhong.setPjzgxhmrj(tempbizhong.getPjzgxhmrj());
            newbizhong.setTjzgxhmrj(tempbizhong.getTjzgxhmrj());
            newbizhong.setZdxhmcj(tempbizhong.getZdxhmcj());
            newbizhong.setPjzdxhmcj(tempbizhong.getPjzdxhmcj());
            newbizhong.setTjzdxhmcj(tempbizhong.getTjzdxhmcj());
            //先获取按天的数据
            List<Exchangerate> tempdaydata = tempbizhong.getDaydata();
            if (tempdaydata.size()>0 && format.format(inexchangerate.getFbsj()).equals(format.format(tempdaydata.get(tempdaydata.size() - 1).getFbsj()))) {
                if (tempdaydata.size()>1 && format.format(inexchangerate.getFbsj()).equals(format.format(tempdaydata.get(tempdaydata.size() - 2).getFbsj()))) {
                    //如果倒数第二个也是当天的数据
                    if (inexchangerate.getXhmrj() < tempdaydata.get(tempdaydata.size() - 1).getXhmrj() && inexchangerate.getXhmrj() < tempdaydata.get(tempdaydata.size() - 2).getXhmrj()) {
                        //是比当天已存在数据都小的数据
                        if (tempdaydata.get(tempdaydata.size() - 1).getXhmrj() < tempdaydata.get(tempdaydata.size() - 2).getXhmrj()) {
                            //如果比倒数第一个大，则删除倒数第一个，并在末尾加上
                            tempdaydata.remove(tempdaydata.size() - 1);
                            tempdaydata.add(inexchangerate);
                        } else {
                            tempdaydata.remove(tempdaydata.size() - 2);
                            tempdaydata.add(inexchangerate);
                        }
                    } else if (inexchangerate.getXhmrj() > tempdaydata.get(tempdaydata.size() - 1).getXhmrj() && inexchangerate.getXhmrj() > tempdaydata.get(tempdaydata.size() - 2).getXhmrj()) {
                        //是比当天已存在数据都大的数据
                        if (tempdaydata.get(tempdaydata.size() - 1).getXhmrj() > tempdaydata.get(tempdaydata.size() - 2).getXhmrj()) {
                            //如果比倒数第一个大，则删除倒数第一个，并在末尾加上
                            tempdaydata.remove(tempdaydata.size() - 1);
                            tempdaydata.add(inexchangerate);
                        } else {
                            tempdaydata.remove(tempdaydata.size() - 2);
                            tempdaydata.add(inexchangerate);
                        }
                    }
                } else {
                    if (inexchangerate.getXhmrj() != tempdaydata.get(tempdaydata.size() - 1).getXhmrj()) {//不等于当天仅有的一个价格则在末尾直接加上
                        tempdaydata.add(inexchangerate);
                    }
                }
                //如果是同一天的数据，则直接替换
                //tempdaydata.set(tempdaydata.size()-1,inexchangerate);
            } else {
                //如果不是同一天的数据则在末尾加入
                tempdaydata.add(inexchangerate);
                //加入后把最前面一天数据去除
                String deletetodate = format.format(tempdaydata.get(0).getFbsj());
                while (format.format(tempdaydata.get(0).getFbsj()).equals(deletetodate)) {
                    tempdaydata.remove(0);
                }
            }
            newbizhong.setDaydata(tempdaydata);

            //获取按秒的数据
            List<Exchangerate> tempseconddata = tempbizhong.getSeconddata();
            tempseconddata.add(inexchangerate);
            if (todelete) {//需要删除10交易日前的秒汇率数据
                while (tempseconddata.get(0).getFbsj().before(jyr10ago)) {
                    tempseconddata.remove(0);
                }
            }
            newbizhong.setSeconddata(tempseconddata);
            exchangerate.sybizhongs.set(exchangeratei, newbizhong);
            count3jyrzdf();
        }catch (Exception ee){
            pubfuncs.printerrormsg("新增汇率信息的内存处理出错："+ee.getMessage()+"，，，"+ee.getStackTrace());
        }
    }
    //计算各币种3交易日内的涨跌幅
    public static void count3jyrzdf() {
        try {
            for (int i = 0; i < exchangerate.sybizhongs.size(); i++) {
                List<Exchangerate> tempxchangerates = exchangerate.sybizhongs.get(i).getSeconddata();
                //3个交易日前
                Date zdfjyr3ago = pubfuncs.getnjyrqsj(new Date(), 3);//把日期往前推3个交易日
                //2个交易日前
                Date zdfjyr2ago = pubfuncs.getnjyrqsj(new Date(), 2);//把日期往前推3个交易日
                //1个交易日前
                Date zdfjyr1ago = pubfuncs.getnjyrqsj(new Date(), 1);//把日期往前推3个交易日
                //3个交易日前的现汇买入价
                float jyr3agoxhmrj = 0;
                //2个交易日前的现汇买入价
                float jyr2agoxhmrj = 0;
                //1个交易日前的现汇买入价
                float jyr1agoxhmrj = 0;
                //汇率记录数
                int hlcount = tempxchangerates.size();
                for (int j = hlcount - 1; j > -1; j--) {
                    if (tempxchangerates.get(j).getFbsj().after(zdfjyr1ago)) {
                        jyr1agoxhmrj = tempxchangerates.get(j).getXhmrj();
                    } else if (tempxchangerates.get(j).getFbsj().after(zdfjyr2ago)) {
                        jyr2agoxhmrj = tempxchangerates.get(j).getXhmrj();
                    } else if (tempxchangerates.get(j).getFbsj().after(zdfjyr3ago)) {
                        jyr3agoxhmrj = tempxchangerates.get(j).getXhmrj();
                    } else {
                        break;
                    }
                }

                //现在的现汇买入价
                float nowxhmrj = exchangerate.sybizhongs.get(i).getXhmrj();
                if (jyr1agoxhmrj != 0)
                    exchangerate.sybizhongs.get(i).setZdf1day(((nowxhmrj - jyr1agoxhmrj) / jyr1agoxhmrj) * 100);
                if (jyr2agoxhmrj != 0)
                    exchangerate.sybizhongs.get(i).setZdf2day(((nowxhmrj - jyr2agoxhmrj) / jyr2agoxhmrj) * 100);
                if (jyr3agoxhmrj != 0)
                    exchangerate.sybizhongs.get(i).setZdf3day(((nowxhmrj - jyr3agoxhmrj) / jyr3agoxhmrj) * 100);
                //pubfuncs.printerrormsg("涨跌幅："+exchangerate.sybizhongs.get(i).getZdf1day()+","+exchangerate.sybizhongs.get(i).getZdf2day()+","+exchangerate.sybizhongs.get(i).getZdf3day());
            }
        }catch (Exception ee){
            pubfuncs.printerrormsg("计算各币种3交易日内的涨跌幅出错："+ee.getMessage()+"，，，"+ee.getStackTrace());
        }
    }
    //加载各币种汇率数据
    public void inibzhldata(){
        Calendar calendar = new GregorianCalendar();
        //先载入大于1年前，小于10天前的汇率数据（只加载每天的最终汇率数据）
        calendar.setTime(new Date());
        calendar.add(calendar.YEAR, -1);//把日期往前推一年
        Date yearhlfrom = calendar.getTime();
        Date jyr10ago =pubfuncs.getnjyrqsj(new Date(),10);//把日期往前推5个交易日
        //载入1年内每天的汇率数据
        pubfuncs.printerrormsg("载入最近一年的汇率数据并按币种赋予汇率数据");
        //List<Exchangerate> dayexchangerate = sqlmapper.selecteverydaylastexchangerates(yearhlfrom, new Date(),"");
        List<Exchangerate> dayexchangerate = sqlmapper.selecteverydayhandlhls(yearhlfrom, new Date(),"");
        for(int i=0;i<exchangerate.sybizhongs.size();i++){
            Bizhong newbizhong=exchangerate.sybizhongs.get(i);
            List<Exchangerate> dayhldata=new ArrayList<Exchangerate>();
            for(int j=0;j<dayexchangerate.size();j++) {
                if(exchangerate.sybizhongs.get(i).getId()==dayexchangerate.get(j).getBzid()){
                    dayhldata.add(dayexchangerate.get(j));
                }
            }
            newbizhong.setDaydata(dayhldata);
            exchangerate.sybizhongs.set(i,newbizhong);
        }
        //然后载入10个交易日内的汇率数据（加载每一条数据）
        pubfuncs.printerrormsg("载入最近10个交易日的汇率数据并按币种赋予汇率数据");
        List<Exchangerate> secondexchangerate = sqlmapper.selectfromdateexchangerates(jyr10ago);
        for(int i=0;i<exchangerate.sybizhongs.size();i++){
            Bizhong newbizhong=exchangerate.sybizhongs.get(i);
            List<Exchangerate> seconghldata=new ArrayList<Exchangerate>();
            for(int j=0;j<secondexchangerate.size();j++) {
                if(exchangerate.sybizhongs.get(i).getId()==secondexchangerate.get(j).getBzid()){
                    seconghldata.add(secondexchangerate.get(j));
                }
            }
            newbizhong.setSeconddata(seconghldata);
            exchangerate.sybizhongs.set(i,newbizhong);
        }

        //过滤并加载出每种币种的实时汇率
        pubfuncs.printerrormsg("过滤并加载出每种币种的实时汇率");
        if (dayexchangerate.size() > 0) {
            exchangerate.lastexchangerates = exchangeratea.getlastexchangerates(secondexchangerate);
            for(int i=0;i<exchangerate.lastexchangerates.size();i++){
                for(int j=0;j<exchangerate.sybizhongs.size();j++){
                    if(exchangerate.lastexchangerates.get(i).getBzid()==exchangerate.sybizhongs.get(j).getId()){
                        exchangerate.sybizhongs.get(j).setFbsj(exchangerate.lastexchangerates.get(i).getFbsj());
                        exchangerate.sybizhongs.get(j).setXhmrj(exchangerate.lastexchangerates.get(i).getXhmrj());
                        exchangerate.sybizhongs.get(j).setXcmrj(exchangerate.lastexchangerates.get(i).getXcmrj());
                        exchangerate.sybizhongs.get(j).setXhmcj(exchangerate.lastexchangerates.get(i).getXhmcj());
                        exchangerate.sybizhongs.get(j).setXcmcj(exchangerate.lastexchangerates.get(i).getXcmcj());
                        break;
                    }
                }
            }
        }
        pubfuncs.printerrormsg("计算各币种近3天的涨跌幅");
        count3jyrzdf();
    }

    //解析币种的警报值设置数据并进行判断
    public static String prabzala(String aladata,Float indata,String datalabel) {
        String returnvalue="";
        String[] aladataarr=aladata.split(",");
        for(int i=0;i<aladataarr.length;i++){
            //console.log(aladataarr[i]);
            if((aladataarr[i].indexOf(">")>-1 || (aladataarr[i].indexOf("<")>-1) || (aladataarr[i].indexOf("=")>-1)) && aladataarr[i].length()>=2){//至少包含一个符号（比如大于号）和一个数字
                if(aladataarr[i].indexOf(">=")==0 || aladataarr[i].indexOf("=>")==0){//大于等于
                    if(indata>=Float.parseFloat(aladataarr[i].substring(2))){
                        //console.log(indata+">="+aladataarr[i].substring(2));
                        returnvalue=returnvalue+(returnvalue.length()>0?"，":"")+datalabel+"当前值为"+indata+"，大于或等于设置的"+aladataarr[i].substring(2);
                    }
                }else if(aladataarr[i].indexOf("<=")==0 || aladataarr[i].indexOf("=<")==0){//小于等于
                    if(indata<=Float.parseFloat(aladataarr[i].substring(2))){
                        //console.log(indata+"<="+aladataarr[i].substring(2));
                        returnvalue=returnvalue+(returnvalue.length()>0?"，":"")+datalabel+"当前值为"+indata+"，小于或等于设置的"+aladataarr[i].substring(2);
                    }
                }else if(aladataarr[i].indexOf(">")==0){//大于
                    if(indata>Float.parseFloat(aladataarr[i].substring(1))){
                        //console.log(indata+"x>"+aladataarr[i].substring(1));
                        returnvalue=returnvalue+(returnvalue.length()>0?"，":"")+datalabel+"当前值为"+indata+"，大于设置的"+aladataarr[i].substring(1);
                    }
                }else if(aladataarr[i].indexOf("<")==0){//小于
                    if(indata<Float.parseFloat(aladataarr[i].substring(1))){
                        //console.log(indata+"x<"+aladataarr[i].substring(2));
                        returnvalue=returnvalue+(returnvalue.length()>0?"，":"")+datalabel+"当前值为"+indata+"，小于设置的"+aladataarr[i].substring(1);
                    }
                }
            }
        }
        return returnvalue;
    }
}

