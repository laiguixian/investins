package com.hsck.tzapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class webdo {

	public String gethtmlfromurl(String inurl,String method) {
		String returnvalue="";
		//构建一URL对象
		try {
			URL url = new URL(inurl);
			//使用openStream得到一输入流并由此构造一个BufferedReader对象
			//BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
			HttpURLConnection newcon=(HttpURLConnection) url.openConnection();
			newcon.setConnectTimeout(3000);//连接超时：20秒
			newcon.setReadTimeout(6000);//读取超时：:20秒
			newcon.setRequestMethod(method);
			newcon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; ) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36");
			InputStream is=newcon.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"));

			/*while(!in.ready()){
				//阻塞，等待一段时间，直到Stream准备完毕
			}*/
			String line="";
			//读取www资源
			while ((line = in.readLine()) != null) {
				returnvalue=returnvalue+line+"\r\n";
			}
			in.close();
			newcon.disconnect();
		} catch (Exception e) {
			Log.i("tzapp","出现异常："+e.getMessage());
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//pubfuncs.printerrormsg("啊哈，在获取html请求数据时出错，错误信息："+e.getMessage()+"，"+e.getStackTrace()+"，请赶紧处理");
		}


		return returnvalue;
	}

	public String gethtmlfromurl(String inurl) {
		String returnvalue="";
		//构建一URL对象  
        try {
			/*URL url = new URL(inurl);
			//使用openStream得到一输入流并由此构造一个BufferedReader对象
            //BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
			HttpURLConnection newcon=(HttpURLConnection) url.openConnection();
			newcon.setConnectTimeout(20000);//连接超时：2000毫秒
			newcon.setReadTimeout(1200000);//读取超时：:120000毫秒，即2分钟
			newcon.setRequestMethod("GET");
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
            newcon.disconnect();*/

			//1,找水源--创建URL
			URL url = new URL(inurl);//放网站
			//2,开水闸--openConnection
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(20000);//连接超时：2000毫秒
			httpURLConnection.setReadTimeout(40000);//读取超时：:120000毫秒，即2分钟
			//3，建管道--InputStream
			InputStream inputStream = httpURLConnection.getInputStream();
			//4，建蓄水池蓄水-InputStreamReader
			InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
			//5，水桶盛水--BufferedReader
			BufferedReader bufferedReader = new BufferedReader(reader);
			/*while(!bufferedReader.ready()){
				//阻塞，等待一段时间，直到Stream准备完毕
			}*/
			StringBuffer buffer = new StringBuffer();
			String temp = null;

			while ((temp = bufferedReader.readLine()) != null) {
				//取水--如果不为空就一直取
				buffer.append(temp);
			}
			bufferedReader.close();//记得关闭
			reader.close();
			inputStream.close();
			returnvalue=buffer.toString();//打印结果

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Log.v("ust", "与服务器交互出现错误，错误信息：" + e.getMessage());
		}
        
        return returnvalue;
	}
}
