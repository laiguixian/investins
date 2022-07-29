package com.hcsk.financialt.publicfuncs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.hcsk.financialt.publicfuncs.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class hostdeal {
    @Autowired
    public RedisUtil rds;

    //获取站点负载
    public String gethosts(String webname,int gettype){//gettype:1-所有节点，2-取1个节点
        try {
            if (webname != null && webname.length() > 0) {
                Object webnamehostsobj=rds.getnorf(webname + "hostsrddnss");
                if(webnamehostsobj!=null) {
                    String webnamehosts=webnamehostsobj.toString();
                    if(gettype==1) {
                        List<String> hosts = new Gson().fromJson(webnamehosts, new TypeToken<ArrayList<String>>() {
                        }.getType());
                        if(hosts.size()>0) {
                            return hosts.get(0);
                        }else{
                            return "no";
                        }
                    }else{
                        return webnamehosts;
                    }
                }else{
                    return "no";
                }
            }
        } catch (Exception ee) {
            pubfuncs.printerrormsg(":获取站点负载时出现问题：" + ee.getMessage());
            return "";
        }
        return "";
    }
}
