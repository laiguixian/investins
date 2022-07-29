package com.hcsk.financialt.controller;

import com.google.gson.Gson;
import com.hcsk.financialt.configure.para;
import com.hcsk.financialt.masterdb.dao.sqlMapper;
import com.hcsk.financialt.publicfuncs.exchangerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class webcontroller {
    @Autowired
    sqlMapper sqlmapper;
    //主页面
    //@GetMapping("/article/**")
    @GetMapping("/main")
    public String viewmain(HttpServletRequest request,Model md){
        if(para.supwebser==1) {
            return "main/mainpage";
        }
        return "";
    }
    //查看汇率页面
    //@GetMapping("/article/**")
    @GetMapping("/huilv")
    public String viewhuilv(HttpServletRequest request,Model md){
        if(para.supwebser==1) {
            md.addAttribute("bizhongs", exchangerate.sykkbizhongs);
            md.addAttribute("zhwhidstr", exchangerate.kkktzbizhongidstr);
            return "viewhl/index";
        }
        return "";
    }
    //查看利润分析页面
    //@GetMapping("/article/**")
    @GetMapping("/viewlrdata")
    public String viewlrdata(HttpServletRequest request,Model md){
        if(para.supwebser==1) {
            return "lrsj/index";
        }
        return "";
    }
    //外汇投资交易页面
    @GetMapping("/whtzjy")
    public String viewwhtzjy(HttpServletRequest request,Model md){
        if(para.supwebser==1) {
            md.addAttribute("bizhongs", exchangerate.kkktzbizhongs);
            return "whtzjy/waihuitzjy";
        }
        return "";
    }
    //外汇投资交易列表页面
    @GetMapping("/whtzjylb")
    public String viewwhtzjylb(HttpServletRequest request,Model md){
        if(para.supwebser==1) {
            return "whtzjylb/waihuitzjylb";
        }
        return "";
    }
    //外汇投资交易概览页面
    @GetMapping("/whtzgl")
    public String viewwhtzgl(HttpServletRequest request,Model md){
        if(para.supwebser==1) {
            return "whtzgl/waihuitzgl";
        }
        return "";
    }
    //外汇投资监控页面
    @GetMapping("/whtzjk")
    public String viewwhtzjk(HttpServletRequest request,Model md){
        if(para.supwebser==1) {
            md.addAttribute("zhwhidstr", exchangerate.kkktzbizhongidstr);
            md.addAttribute("zhwhnamestr", exchangerate.kkktzbizhongnamestr);
            return "whtzjk/waihuitzjk";
        }
        return "";
    }
    //外汇投资利润统计
    @GetMapping("/whtzlrtj")
    public String viewwhtzlrtj(HttpServletRequest request,Model md){
        if(para.supwebser==1) {
            return "whlrtj/waihuilrtj";
        }
        return "";
    }
}
