package com.hcsk.financialt.configure;

import com.hcsk.financialt.publicfuncs.pubfuncs;

public class para {
    public static int urlgettype=Integer.parseInt(pubfuncs.getparafromprofile("urlgettype"));//获取网页内容的方式，1-自己获取，2-通过设定的网址获取
    public static String urlgetip=pubfuncs.getparafromprofile("urlgetip");//远程获取网页内容的服务器的ip
    public static String urlgetport=pubfuncs.getparafromprofile("urlgetport");//远程获取网页内容的服务器的端口
    public static String urlgetinfurl="http://"+urlgetip+":"+urlgetport;//远程获取网页内容的服务器的ip
    public static int supwebser=Integer.parseInt(pubfuncs.getparafromprofile("supwebser"));//是否提供网页服务，0-不提供，1-提供
}
