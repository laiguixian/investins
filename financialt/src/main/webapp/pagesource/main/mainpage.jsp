<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/6/22
  Time: 8:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%--<!DOCTYPE html>--%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>投资管理工具</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <link rel="stylesheet" href="/pagesource/threepart/layui-v2.5.6/layui/css/layui.css">
    <script type="text/javascript" src="/pagesource/threepart/jquery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="/pagesource/threepart/layui-v2.5.6/layui/layui.js"></script>
    <script type="text/javascript"src="/pagesource/main/js/base.js"></script>
</head>
<body class="layui-layout-body"style="width:100%;height:100%;margin:0;padding:0;">
<div class="layui-layout layui-layout-admin">
    <div class="layui-header">
        <div class="layui-logo">投资管理工具</div>
        <!-- 头部区域（可配合layui已有的水平导航） -->
        <ul class="layui-nav layui-layout-left">
            <li class="layui-nav-item"><a href="javascript:changemainframe('/whtzgl','gettype=yedy0');">投资概览</a></li>
            <li class="layui-nav-item"><a href="">投资建议</a></li>
            <li class="layui-nav-item"><a href="">投资警报</a></li>
            <%--<li class="layui-nav-item">
                <a href="javascript:;">投资详情</a>
                <dl class="layui-nav-child">
                    <dd><a href="">外汇</a></dd>
                    <dd><a href="">股票</a></dd>
                    <dd><a href="">基金</a></dd>
                </dl>
            </li>--%>
        </ul>
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <img src="/pagesource/comm/images/defaultpico.jpg" class="layui-nav-img">
                    阿赖啊
                </a>
                <dl class="layui-nav-child">
                    <dd><a href="">基本资料</a></dd>
                    <dd><a href="">安全设置</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item"><a href="">退出</a></li>
        </ul>
    </div>

    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
            <ul class="layui-nav layui-nav-tree"  lay-filter="test">
                <li class="layui-nav-item">
                    <a class="" href="javascript:;">行情详情</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:changemainframe('/huilv','');">外汇</a></dd>
                        <dd><a href="javascript:;">股票</a></dd>
                        <dd><a href="javascript:;">基金</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;">投资交易</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:changemainframe('/whtzjylb','');">外汇</a></dd>
                        <dd><a href="javascript:;">股票</a></dd>
                        <dd><a href="">基金</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item layui-nav-itemed">
                    <a href="javascript:;">投资监控</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:changemainframe('/whtzjk','');">外汇</a></dd>
                        <dd><a href="javascript:;">股票</a></dd>
                        <dd><a href="">基金</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;">利润统计</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:changemainframe('/whtzlrtj','');">外汇</a></dd>
                        <dd><a href="javascript:;">股票</a></dd>
                        <dd><a href="">基金</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;">利润分析</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:changemainframe('/viewlrdata','');">外汇</a></dd>
                        <dd><a href="javascript:;">股票</a></dd>
                        <dd><a href="">基金</a></dd>
                    </dl>
                </li>
            </ul>
        </div>
    </div>

    <div class="layui-body">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;height:100%;margin:0;padding:0;" id="mainpanel">
            <iframe src="/whtzgl" id="mainpanelpage"style="width:100%;height:100%;margin:0;padding:0;"></iframe>
        </div>
    </div>

    <div class="layui-footer">
        <!-- 底部固定区域 -->
        itdk.net 阿赖啊
    </div>
</div>
<script>
    //JavaScript代码区域
    layui.use('element', function(){
        var element = layui.element;
    });

</script>
</body>
</html>
