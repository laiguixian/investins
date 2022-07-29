<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en" style="height: 100%">
<head>
    <meta charset="UTF-8">
    <%--<link rel="shortcut icon" href="http://www.sd.com/lotec32.ico">--%>
    <title>
        查看汇率
    </title>
    <%----%>
    <link rel="stylesheet" href="/pagesource/threepart/layui-v2.5.6/layui/css/layui.css">
    <link rel="stylesheet" href="/pagesource/viewhl/css/base.css">
    <script type="text/javascript"src="/pagesource/threepart/echarts-4.8.0/echarts.min.js"></script>
    <script type="text/javascript" src="/pagesource/threepart/jquery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="/pagesource/threepart/layui-v2.5.6/layui/layui.js"></script>
    <script type="text/javascript" src="/pagesource/comm/js/publicjs.js"></script>
    <script type="text/javascript"src="/pagesource/viewhl/js/base.js"></script>
</head>
<style>
    .label-danheng{
        width: 30px;
        padding:0px;
        text-align:center;
    }
</style>
<body style="height: 100%; margin: 0">
<div style="padding: 20px; background-color: #F2F2F2;">
    <div class="layui-row layui-col-space10">
        <%--<div class="layui-col-md6">--%>
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body">
                    <form class="layui-form" action="">
                        <div class="layui-form-item">
                            <label class="layui-form-label">币种</label>
                            <div class="layui-input-inline">
                                <select name="bzselect" id="bzselect">
                                    <c:forEach items="${bizhongs}" var="bizhong">
                                        <%--<option value="${bizhong}" <c:if test="${bizhong=='美元(USD)'}">selected</c:if>>${bizhong}</option>--%>
                                        <option value="${bizhong.id}">${bizhong.bzname}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <label class="layui-form-label">时间</label>
                            <div class="layui-input-inline">
                                <select name="datetypeselect" id="datetypeselect"  lay-filter="datetype">
                                    <option value=365>一年内</option>
                                    <option value=30>一月内</option>
                                    <option value=10>10交易日内</option>
                                    <option value=9>9交易日内</option>
                                    <option value=8>8交易日内</option>
                                    <option value=7>7交易日内</option>
                                    <option value=6>6交易日内</option>
                                    <option value=5>5交易日内</option>
                                    <option value=4>4交易日内</option>
                                    <option value=3>3交易日内</option>
                                    <option value=2>2交易日内</option>
                                    <option value=1 selected>1交易日内</option>
                                </select>
                            </div>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" lay-verify="required" id="jiaoyisjf" name="jiaoyisjf" placeholder="yyyy-MM-dd HH:mm:ss">
                            </div>
                            <label class="layui-form-label label-danheng">-</label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" lay-verify="required" id="jiaoyisjt" name="jiaoyisjt" placeholder="yyyy-MM-dd HH:mm:ss">
                            </div>
                            <div class="layui-inline">
                                <div class="layui-input-inline">
                                    <button type="button" class="layui-btn" onclick="inizxtbybzxlk()">查询</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body" id="explaincontent">
                </div>
            </div>
        </div>
    </div>
</div>
<div id="container" style="height: 100%"></div>
<script>
    layui.use([ 'form', 'layedit','laydate'], function() {
            var form = layui.form
                , layer = layui.layer
                , layedit = layui.layedit
                , laydate = layui.laydate;
        //日期时间选择器
        laydate.render({
            elem: '#jiaoyisjf'
            ,type: 'datetime'
            ,trigger: 'click'
        });
        //日期时间选择器
        laydate.render({
            elem: '#jiaoyisjt'
            ,type: 'datetime'
            ,trigger: 'click'
        });
        form.on('select(datetype)', function(data){
            //console.log(data.elem); //得到select原始DOM对象
            //console.log(data.value); //得到被选中的值
            //console.log(data.othis); //得到美化后的DOM对象
            console.log("hello:"+data.value);
            var nowtime=new Date();
            $("#jiaoyisjt").val(dateFtt('yyyy-MM-dd hh:mm:ss',nowtime));
            switch (data.value){
                case '365':
                    nowtime.setFullYear((nowtime.getFullYear()-1));
                    $("#jiaoyisjf").val(dateFtt('yyyy-MM-dd hh:mm:ss',nowtime));//往前推一年
                    console.log("hello2:"+$("#jiaoyisjf").val());
                    break;
                case '30':
                    nowtime.setMonth((nowtime.getMonth()-1));
                    $("#jiaoyisjf").val(dateFtt('yyyy-MM-dd hh:mm:ss',nowtime));//往前推一个月
                    break;
                default:
                    $("#jiaoyisjf").val(dateFtt('yyyy-MM-dd hh:mm:ss',getnjyrqsj(new Date(),data.value)));//往前推一个月
                    break;
            }
            //form.render('datetime');
        });
        });
    //账户外汇id串
    var zhwhidstr='${zhwhidstr}';
</script>
<%--<script type="text/javascript">
    var dom = document.getElementById("container");
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    option = {
        title: {
            text: '折线图堆叠'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['邮件营销', '联盟广告', '视频广告', '直接访问', '搜索引擎']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: '邮件营销',
                type: 'line',
                stack: '总量',
                data: [120, 132, 101, 134, 90, 230, 210]
            },
            {
                name: '联盟广告',
                type: 'line',
                stack: '总量',
                data: [220, 182, 191, 234, 290, 330, 310]
            },
            {
                name: '视频广告',
                type: 'line',
                stack: '总量',
                data: [150, 232, 201, 154, 190, 330, 410]
            },
            {
                name: '直接访问',
                type: 'line',
                stack: '总量',
                data: [320, 332, 301, 334, 390, 330, 320]
            },
            {
                name: '搜索引擎',
                type: 'line',
                stack: '总量',
                data: [820, 932, 901, 934, 1290, 1330, 1320]
            }
        ]
    };
    ;
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
</script>--%>
</body>
</html>