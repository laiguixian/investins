package com.hcsk.financialt;

import com.google.gson.Gson;
import com.hcsk.financialt.configure.para;
import com.hcsk.financialt.masterdb.dao.sqlMapper;
import com.hcsk.financialt.pojo.Bizhong;
import com.hcsk.financialt.pojo.Exchangerate;
import com.hcsk.financialt.pojo.Exchangeratecontent;
import com.hcsk.financialt.publicfuncs.exchangerate;
import com.hcsk.financialt.publicfuncs.pubfuncs;
import com.hcsk.financialt.publicfuncs.webdo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;


@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Autowired
    exchangerate exchangeratea;
    @Autowired
    sqlMapper sqlMappera;
    @Autowired
    webdo webdoa;
    @Autowired
    pubfuncs pcs;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //pubfuncs.printerrormsg("开始进行启动时的数据加载任务");

        /*//载入外汇所有币种
        pubfuncs.printerrormsg("载入外汇所有币种");
        pcs.inibizhong();*/
        //exchangeratea.prawebwhcontent("icbc","");
        //exchangeratea.prawebzhwhcontent("icbc","");
        //String wdstr=exchangeratea.getwhcontent("icbc","","",2);
        //String wdstr=exchangeratea.getzhwhcontent("icbc",2);
        //pubfuncs.printerrormsg("获取的外汇信息："+wdstr);
        //获取银行账户外汇汇率
        //exchangeratea.bankexchangerate("icbc", "", "", 2);
        //获取银行外汇汇率
        //exchangeratea.bankexchangerate("icbc", "", "", 1);
        if(para.supwebser==1) {//如果提供网页服务

            //载入外汇所有币种
            pubfuncs.printerrormsg("载入外汇所有币种");
            pcs.inibizhong();
            //加载各币种汇率数据
            pcs.inibzhldata();
            //加载余额大于0的外汇快照
            pubfuncs.printerrormsg("加载余额大于0的外汇快照");
            while(exchangerate.whkuaizhaoisinuse){}
            exchangerate.whkuaizhaoisinuse=true;
            exchangerate.whkuaizhaolist=sqlMappera.getwhkuaizhaos();
            exchangerate.whkuaizhaoisinuse=false;

            //获取每个币种每天汇率最高和最低的分布时段
            //pubfuncs.printerrormsg("获取每个币种每天汇率最高和最低的分布时段");
            //pcs.getbzhighhlhour();
            //每个币种即将到达的最高点和最低点预测
            //pubfuncs.printerrormsg("每个币种即将到达的最高点和最低点预测");
            //pcs.bzzgzdyuce();
            //可以开始定时任务了
            exchangerate.lastexchangeratesisready = true;
            pubfuncs.printerrormsg("结束进行启动时的数据加载任务");

            //有条件执行，防止打包时直接执行了
            File keyfile = new File("./keyfile.txt");
            if (keyfile.exists()) {
                //往前获取365天的汇率数据
                //exchangeratea.getlastyeardata("icbc", 365);
            }

        }else{
            pubfuncs.printerrormsg("启动成功");
        }
        //exchangeratea.getrmyhhlzjj();
    }
}
