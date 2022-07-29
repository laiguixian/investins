<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en" style="height: 100%">
<head>
    <meta charset="UTF-8">
    <%--<link rel="shortcut icon" href="http://www.sd.com/lotec32.ico">--%>
    <title>
        查看利润
    </title>
    <%----%>
    <link rel="stylesheet" href="/pagesource/threepart/layui-v2.5.6/layui/css/layui.css">
    <link rel="stylesheet" href="/pagesource/lrsj/css/base.css">
    <script type="text/javascript"src="/pagesource/threepart/echarts-4.8.0/echarts.min.js"></script>
    <script type="text/javascript" src="/pagesource/threepart/jquery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="/pagesource/threepart/layui-v2.5.6/layui/layui.js"></script>
    <script type="text/javascript" src="/pagesource/comm/js/publicjs.js"></script>
    <script type="text/javascript"src="/pagesource/lrsj/js/base.js"></script>
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

                            <label class="layui-form-label">时间</label>
                            <div class="layui-input-inline">
                                <select name="datetypeselect" id="datetypeselect"  lay-filter="datetype">
                                    <option value=1000002>三年内</option>
                                    <option value=1000001>二年内</option>
                                    <option value=1000000>一年内</option>
                                    <option value=100002>三月内</option>
                                    <option value=100001>二月内</option>
                                    <option value=100000>一月内</option>
                                    <option value=30 selected>30交易日内</option>
                                    <option value=15>15交易日内</option>
                                    <option value=10>10交易日内</option>
                                    <option value=9>9交易日内</option>
                                    <option value=8>8交易日内</option>
                                    <option value=7>7交易日内</option>
                                    <option value=6>6交易日内</option>
                                    <option value=5>5交易日内</option>
                                    <option value=4>4交易日内</option>
                                    <option value=3>3交易日内</option>
                                    <option value=2>2交易日内</option>
                                    <option value=1>1交易日内</option>
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
                case '1000000':
                    nowtime.setFullYear((nowtime.getFullYear()-1));
                    $("#jiaoyisjf").val(dateFtt('yyyy-MM-dd hh:mm:ss',nowtime));//往前推一年
                    console.log("hello2:"+$("#jiaoyisjf").val());
                    break;
                case '1000001':
                    nowtime.setFullYear((nowtime.getFullYear()-2));
                    $("#jiaoyisjf").val(dateFtt('yyyy-MM-dd hh:mm:ss',nowtime));//往前推二年
                    console.log("hello2:"+$("#jiaoyisjf").val());
                    break;
                case '1000002':
                    nowtime.setFullYear((nowtime.getFullYear()-3));
                    $("#jiaoyisjf").val(dateFtt('yyyy-MM-dd hh:mm:ss',nowtime));//往前推三年
                    console.log("hello2:"+$("#jiaoyisjf").val());
                    break;
                case '100000':
                    nowtime.setMonth((nowtime.getMonth()-1));
                    $("#jiaoyisjf").val(dateFtt('yyyy-MM-dd hh:mm:ss',nowtime));//往前推一个月
                    break;
                case '100001':
                    nowtime.setMonth((nowtime.getMonth()-2));
                    $("#jiaoyisjf").val(dateFtt('yyyy-MM-dd hh:mm:ss',nowtime));//往前推二个月
                    break;
                case '100002':
                    nowtime.setMonth((nowtime.getMonth()-3));
                    $("#jiaoyisjf").val(dateFtt('yyyy-MM-dd hh:mm:ss',nowtime));//往前推三个月
                    break;
                default:
                    $("#jiaoyisjf").val(dateFtt('yyyy-MM-dd hh:mm:ss',getnjyrqsj(new Date(),data.value)));//往前推一个月
                    break;
            }
            //form.render('datetime');
        });
        });
</script>
</body>
</html>