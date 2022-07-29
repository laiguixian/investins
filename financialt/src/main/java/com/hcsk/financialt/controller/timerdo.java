package com.hcsk.financialt.controller;

import com.hcsk.financialt.configure.para;
import com.hcsk.financialt.publicfuncs.exchangerate;
import com.hcsk.financialt.publicfuncs.pubfuncs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.naming.Context;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class timerdo {
    public static boolean waihuiTasksisdoing=false;//获取外汇数据任务正在进行
    public static boolean zhwhTasksisdoing=false;//获取账户外汇数据任务正在进行
    public static boolean isrestTasksisdoing=false;//获取是否是休市时间任务正在进行
    public static boolean isrestdate=false;//是否休市时间，休市时间则不采集数据
    public static boolean isgetdata=false;//是否抓取数据，如果是第2类型的休市时间则不采集数据
    public static String fxtodate="";//分析涨跌规律到的日期
    @Autowired
    exchangerate exchangeratea;
    @Autowired
    pubfuncs pcs;

    /*首先使用 @Scheduled 注解开启一个定时任务。
    fixedRate 表示任务执行之间的时间间隔，具体是指两次任务的开始时间间隔，即第二次任务开始时，第一次任务可能还没结束。
    fixedDelay 表示任务执行之间的时间间隔，具体是指本次任务结束到下次任务开始之间的时间间隔。
    initialDelay 表示首次任务启动的延迟时间。
    所有时间的单位都是毫秒。
    @Scheduled(fixedRate = 2000)
    public void fixedRate() {
        pubfuncs.printerrormsg("fixedRate>>>"+new Date());
    }
    @Scheduled(fixedDelay = 2000)
    public void fixedDelay() {
        pubfuncs.printerrormsg("fixedDelay>>>"+new Date());
    }
    @Scheduled(initialDelay = 2000,fixedDelay = 2000)
    public void initialDelay() {
        pubfuncs.printerrormsg("initialDelay>>>"+new Date());
    }
    springboot的@Scheduled 注解也支持 cron 表达式，具体使用可以去查
    */
    //定时判断开市休市的任务，每10秒执行一次
    @Scheduled(fixedDelay = 10*1000)
    private void getisrestdateTasks() {
        if(!isrestTasksisdoing) {
            isrestTasksisdoing=true;
            boolean tempisrestdate = pubfuncs.adateisres(new Date(),1);
            boolean tempisgetdata=pubfuncs.adateisres(new Date(),2);
            if(isrestdate!=tempisrestdate){
                if(tempisrestdate){
                    pubfuncs.printerrormsg("休市了");
                }else{
                    pubfuncs.printerrormsg("开市了");
                }
            }
            if(isgetdata!=tempisgetdata){
                if(tempisgetdata){
                    pubfuncs.printerrormsg("停止获取数据");
                }else{
                    pubfuncs.printerrormsg("开始获取数据");
                }
            }
            //将是否是休市时间赋予全局变量
            isgetdata=tempisgetdata;
            isrestdate = tempisrestdate;
            isrestTasksisdoing=false;
        }
    }

    //定时计算任务，每5分钟执行一次
    @Scheduled(fixedDelay = 5*60*1000)
    private void getwaihuiTasks() {
        if((para.supwebser==1) && !isrestdate && !waihuiTasksisdoing) {
            waihuiTasksisdoing=true;
            try {

                if (exchangerate.lastexchangeratesisready) {
                    //获取每个币种每天汇率最高和最低的分布时段，以及最高点和最低点预测
                    pcs.getbzhighhlhour();
                    //每个币种即将到达的最高点和最低点预测
                    pcs.bzzgzdyuce();
                    //pubfuncs.printerrormsg("执行定时获取外汇任务");
                    //获取银行外汇汇率
                    //exchangeratea.bankexchangerate("icbc", "", "", 1);
                }
            }catch (Exception ee){
                pubfuncs.printerrormsg("获取外汇出错："+ee.getMessage());
            }
            waihuiTasksisdoing=false;
        }
    }
    //随机数，执行获取时获得随机数，每次定时任务减1，到0时执行
    //外汇的随机数
    public static int waihuiromint=1;
    //账户外汇的随机数
    public static int zhwhromint=1;
    //账户贵金属的随机数
    public static int zhgjsromint=1;
    //远程外汇获取的时间，每20秒获取一次
    public static int waihuihqsj=20;
    //添加定时执行获取外汇信息的任务，定时任务每2秒执行一次
    @Scheduled(fixedDelay = 2*1000)
    private void getzhwhTasks() {
        if(!isgetdata && !zhwhTasksisdoing) {
            //不提供网页服务（纯接口）且不是休息日及本任务不是正在执行时才执行本任务
            zhwhTasksisdoing=true;
            try {
                    //pubfuncs.printerrormsg("执行定时获取账户外汇任务");
                    if(para.urlgettype==2) {//通过指定的服务器获取
                        if (exchangerate.lastexchangeratesisready) {
                            waihuihqsj=waihuihqsj-1;
                            if(waihuihqsj<=0) {
                                waihuihqsj=20;
                                // 获取银行结售汇汇率
                                exchangeratea.bankexchangerate("icbc", "", "", 1);
                            }
                            //获取银行账户外汇汇率
                            exchangeratea.bankexchangerate("icbc", "", "", 2);
                            //获取贵金属价格
                            exchangeratea.bankexchangerate("icbc", "", "", 5);
                        }
                    }else {
                        //获取未解析汇率信息保存到内存
                        //外汇汇率信息未解析字符串
                        waihuiromint = waihuiromint - 1;
                        if (waihuiromint <= 0) {
                            waihuiromint = (int) (10 + Math.random() * (5));//生成10-15的随机数，也就是说每20-30秒向银行获取一次数据
                            exchangeratea.waihuinopstr = exchangeratea.getwhcontent("icbc", "", "", 1);
                        }

                        //账户外汇汇率信息未解析字符串
                        zhwhromint = zhwhromint - 1;
                        if (zhwhromint <= 0) {
                            zhwhromint = (int) (1 + Math.random() * (3));//生成1-3的随机数
                            exchangeratea.zhwhnopstr = exchangeratea.getzhwhcontent("icbc", 1);
                        }

                        //账户贵金属汇率信息未解析字符串
                        zhgjsromint = zhgjsromint - 1;
                        if (zhgjsromint <= 0) {
                            zhgjsromint = (int) (1 + Math.random() * (3));//生成1-3的随机数
                            exchangeratea.zhgjsnopstr = exchangeratea.getzhgjscontent("icbc", 1);
                        }
                    }
                //}
            }catch (Exception ee){
                pubfuncs.printerrormsg("获取账户外汇出错");
            }
            zhwhTasksisdoing=false;
        }
    }
    public static void main(String[] args) {
        //System.out.println("数字："+((1631747520000l-1573867920000l)/1000f/3600f/24f/365f));
    }
}
