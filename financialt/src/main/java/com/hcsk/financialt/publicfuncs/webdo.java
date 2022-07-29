package com.hcsk.financialt.publicfuncs;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class webdo {
	public String gethtmlfromurl(String inurl,String method) {
		String returnvalue="";
		//构建一URL对象  
        try {
			URL url = new URL(inurl);
			//使用openStream得到一输入流并由此构造一个BufferedReader对象  
            //BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
			HttpURLConnection newcon=(HttpURLConnection) url.openConnection();
			newcon.setConnectTimeout(20000);//连接超时：20秒
			newcon.setReadTimeout(20000);//读取超时：:20秒
			newcon.setRequestMethod(method);
			newcon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; ) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36");
			BufferedReader in = new BufferedReader(new InputStreamReader(newcon.getInputStream(),"UTF-8"));
            while(!in.ready()){ 
            	//阻塞，等待一段时间，直到Stream准备完毕
            }
            String line="";
            //读取www资源  
            while ((line = in.readLine()) != null) {  
            	returnvalue=returnvalue+line+"\r\n";
            }  
            in.close();
            newcon.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
            pubfuncs.printerrormsg("啊哈，在获取html请求数据时出错，错误信息："+e.getMessage()+"，"+e.getStackTrace()+"，请赶紧处理");
		}


        return returnvalue;
	}

	//获取网页内容，自带解密
    public String gethtmlfromurldecode(String inurl,String method) {
        String returnvalue="";
        //构建一URL对象
        try {
            URL url = new URL(inurl);
            //使用openStream得到一输入流并由此构造一个BufferedReader对象
            //BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
            HttpURLConnection newcon=(HttpURLConnection) url.openConnection();
            newcon.setConnectTimeout(20000);//连接超时：20秒
            newcon.setReadTimeout(20000);//读取超时：:20秒
            newcon.setRequestMethod(method);
            newcon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; ) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36");
            //BufferedReader in = new BufferedReader(new InputStreamReader(newcon.getInputStream(),"GBK"));
            BufferedReader in = new BufferedReader(new InputStreamReader(newcon.getInputStream()));
            while(!in.ready()){
                //阻塞，等待一段时间，直到Stream准备完毕
            }
            String line="";
            //读取www资源
            while ((line = in.readLine()) != null) {
                returnvalue=returnvalue+line+"\r\n";
            }
            //pubfuncs.printerrormsg("获取到的返回值："+returnvalue);
            returnvalue=endecode1.decodestr(returnvalue);
            //pubfuncs.printerrormsg("获取到的返回值解密后："+returnvalue);

            in.close();
            newcon.disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            pubfuncs.printerrormsg("啊哈，在带解密获取html请求数据时出错，错误信息："+e.getMessage()+"，"+e.getStackTrace()+"，请赶紧处理");
        }


        return returnvalue;
    }

	public String gethtmlfromurlzhwh(String inurl,String method) {
        String returnvalue = "";
        //构建一URL对象
        try {
            URL url = new URL(inurl);
            //使用openStream得到一输入流并由此构造一个BufferedReader对象
            //BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
            HttpURLConnection newcon = (HttpURLConnection) url.openConnection();
            newcon.setConnectTimeout(20000);//连接超时：20秒
            newcon.setReadTimeout(20000);//读取超时：:20秒
            newcon.setRequestMethod(method);
            //newcon.setRequestProperty("Accept","application/json, text/javascript, */*; q=0.01");
            //newcon.setRequestProperty("Content-Length","240");
            //newcon.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
            //newcon.setRequestProperty("Accept-Encoding","gzip, deflate, br");
            //newcon.setRequestProperty("Host","mybank.icbc.com.cn");
            //newcon.setRequestProperty("Connection","keep-alive");
            //newcon.setRequestProperty("X-Requested-With","XMLHttpRequest");
            //newcon.setRequestProperty("Referrer Policy","no-referrer-when-downgrade");
            //newcon.setRequestProperty("Origin","https://mybank.icbc.com.cn");
            //newcon.setRequestProperty("Sec-Fetch-Site","same-origin");
            //newcon.setRequestProperty("Sec-Fetch-Mode","cors");
            //newcon.setRequestProperty("Sec-Fetch-Dest","empty");
            //newcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            //newcon.setRequestProperty("Referer","https://mybank.icbc.com.cn/ctp/icbc/newperbank/acctfx/middle_nologin.jsp?proIdsIn=130060000931|130060000932|130060000933|130060000934|130060000935|130060001311|130060085591|130060085592|130060085593|130060085594");
            //newcon.setRequestProperty("Cookie","BIGipServerFEBANKP_gerenwangyinIPV4V6_80_pool=1644531978.20480.0000; isP3bank=1; isEn_US=0; isPri=; firstZoneNo=%E7%A6%8F%E5%BB%BA_1400");
            newcon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; ) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36");
            //BufferedReader in = new BufferedReader(new InputStreamReader(newcon.getInputStream(), "UTF-8"));
            BufferedReader in = new BufferedReader(new InputStreamReader(newcon.getInputStream(),"GBK"));
            while (!in.ready()) {
                //阻塞，等待一段时间，直到Stream准备完毕
            }
            String line = "";
            //读取www资源
            while ((line = in.readLine()) != null) {
                returnvalue = returnvalue + line + "\r\n";
            }
            in.close();
            newcon.disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            pubfuncs.printerrormsg("啊哈，在获取账户外汇html请求数据时出错，错误信息："+e.getMessage()+"，"+e.getStackTrace()+"，请赶紧处理");
        }


        return returnvalue;
    }

    //仅仅只是打开url
    public void doopenurl(String[] inurls,String method) {
        for(int i=0;i<inurls.length;i++) {
            //构建一URL对象
            try {
                URL url = new URL(inurls[i]);
                //使用openStream得到一输入流并由此构造一个BufferedReader对象
                //BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
                HttpURLConnection newcon = (HttpURLConnection) url.openConnection();
                newcon.setConnectTimeout(20000);//连接超时：20秒
                newcon.setReadTimeout(20000);//读取超时：:20秒
                newcon.setRequestMethod(method);
                newcon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; ) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36");
                //BufferedReader in = new BufferedReader(new InputStreamReader(newcon.getInputStream(), "UTF-8"));
                BufferedReader in = new BufferedReader(new InputStreamReader(newcon.getInputStream(), "GBK"));
                while (!in.ready()) {
                    //阻塞，等待一段时间，直到Stream准备完毕
                }
            /*String line = "";
            //读取www资源
            while ((line = in.readLine()) != null) {
                returnvalue = returnvalue + line + "\r\n";
            }*/
                in.close();
                newcon.disconnect();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                pubfuncs.printerrormsg("啊哈，在直接获取html请求数据时出错，错误信息："+e.getMessage()+"，"+e.getStackTrace()+"，请赶紧处理");
            }
        }
    }
}
