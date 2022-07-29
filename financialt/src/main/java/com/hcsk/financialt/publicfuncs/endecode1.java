package com.hcsk.financialt.publicfuncs;

import org.springframework.stereotype.Component;

import java.util.Base64;
@Component
public class endecode1 {
    static String letterstr="JYQPMSGRBX";
    static String numstr=   "0123456789";
    public static String lettertonum(String instr){
        instr=instr.trim();
        String result="";
        for(int i=0;i<instr.length();i++){
            result = result + numstr.substring(letterstr.indexOf(instr.substring(i, i + 1)), letterstr.indexOf(instr.substring(i, i + 1)) + 1);
        }
        return result;
    }

    public static String numtoletter(String instr){
        instr=instr.trim();
        String result="";
        for(int i=0;i<instr.length();i++){
            result=result+letterstr.substring(numstr.indexOf(instr.substring(i,i+1)),numstr.indexOf(instr.substring(i,i+1))+1);
        }
        return result;
    }

    public static String get3lenstr(String instr){
        String newstr="";
        switch (instr.length()){
            case 1:
                newstr= "00"+instr;
                break;
            case 2:
                newstr= "0"+instr;
                break;
            default:
                newstr=  instr;
        }
        return newstr;
    }

    public static String encodestr(String instr){
        String result="";
        try{
            //先累加两位0到9的随机数
            result=result+(int)(Math.random()*10);
            result=result+(int)(Math.random()*10);
            //String newinstr=new String(Base64.getEncoder().encode(instr.getBytes()),"gb2312");
            String newinstr=new String(Base64.getEncoder().encode(instr.getBytes()),"UTF-8");
            //String newinstr=new String(Base64.getEncoder().encode(instr.getBytes()));
            //Log.i("ltools","b64加密后："+newinstr.length());
            char[] newinstrchar=newinstr.toCharArray();
            for(int i=0;i<newinstrchar.length;i++){
                int newinstrcharint=(int)(newinstrchar[i]);
                newinstrcharint=newinstrcharint+169;
                result=result+get3lenstr(newinstrcharint+"");
            }
            result=result+(int)(Math.random()*10);
            result=result+(int)(Math.random()*10);
            result=result+(int)(Math.random()*10);
            result=numtoletter(result);
        }catch (Exception ee){}
        return result;
    }

    public static String decodestr(String instr){
        String result="";
        String tempstr="";
        int tempi=0;
        String newstr=lettertonum(instr);
        newstr=newstr.substring(2,newstr.length()-3);
        for(int i=0;i<newstr.length();i++){
            tempstr=tempstr+newstr.substring(i,i+1);
            tempi=tempi+1;
            if(tempi==3){
                result=result+(char)(Integer.parseInt(tempstr)-169);
                tempstr="";
                tempi=0;
            }
        }
        try {
            //里面的替换回车换行的代码（.replace("\r\n", "")）是为了防止java的base64因不能识别换行引起的“Illegal base64 character d”错误
            //result = new String(Base64.getDecoder().decode(result.replace("\r\n", "")), "UTF-8");
            result = new String(Base64.getDecoder().decode(result.replace("\r\n", "")), "GBK");
            //result = new String(Base64.getDecoder().decode(result));
            //result = new String(Base64.getDecoder().decode(result), "gb2312");
        }catch (Exception ee){
            pubfuncs.printerrormsg("b64解码出错："+ee.getMessage());
        }
        return result;
    }
}
