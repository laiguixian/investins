<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en" style="height: 100%">
<head>
    <meta charset="UTF-8">
    <%--<link rel="shortcut icon" href="http://www.sd.com/lotec32.ico">--%>
    <title>
        外汇投资概览
    </title>
    <%----%>
    <link rel="stylesheet" href="/pagesource/threepart/layui-v2.5.6/layui/css/layui.css">
    <link rel="stylesheet" href="/pagesource/whtzgl/css/base.css">
    <script type="text/javascript"src="/pagesource/threepart/echarts-4.8.0/echarts.min.js"></script>
    <script type="text/javascript" src="/pagesource/threepart/jquery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="/pagesource/threepart/layui-v2.5.6/layui/layui.js"></script>
    <script type="text/javascript" src="/pagesource/comm/js/publicjs.js"></script>
    <script type="text/javascript"src="/pagesource/whtzgl/js/base.js"></script>
</head>
<body style="height: 100%; margin: 0">
<%--<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>外汇投资概览</legend>
</fieldset>--%>
<div style="padding: 20px; background-color: #F2F2F2;height: 100%" class="layui100">
    <div class="layui-row layui-col-space10 layui100">
        <%--<div class="layui-col-md6">--%>
        <div class="layui-col-md12 layui100">
            <div class="layui-card layui100">
                <div class="layui-card-body layui100">
                    <div id="container" class="layui100"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    /*var dom = document.getElementById("container");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    option = {
        title: {
            text: '某站点用户访问来源',
            subtext: '纯属虚构',
            left: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
        },
        series: [
            {
                name: '访问来源',
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data: [
                    {value: 335, name: '直接访问'},
                    {value: 310, name: '邮件营销'},
                    {value: 234, name: '联盟广告'},
                    {value: 135, name: '视频广告'},
                    {value: 1548, name: '搜索引擎'}
                ],
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    ;
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }*/
</script>
</body>
</html>