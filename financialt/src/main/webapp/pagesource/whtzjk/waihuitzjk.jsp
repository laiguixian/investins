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
    <link rel="stylesheet" href="/pagesource/whtzjk/css/base.css">
    <script type="text/javascript"src="/pagesource/threepart/echarts-4.8.0/echarts.min.js"></script>
    <script type="text/javascript" src="/pagesource/threepart/jquery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="/pagesource/threepart/layui-v2.5.6/layui/layui.js"></script>
    <script type="text/javascript" src="/pagesource/comm/js/publicjs.js"></script>
    <script type="text/javascript"src="/pagesource/whtzjk/js/base.js"></script>
</head>
<body style="height: 100%; margin: 0">
<%--<audio id="alaaudio" autoplay>&lt;%&ndash;  controls="controls"&ndash;%&gt;--%>
    <%--<source src="/pagesource/comm/voice/alarm1.mp3"/>--%>
<%--</audio>--%>
<div class="layui-row layui-col-space10">
    <form class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">选择时间段</label>
            <div class="layui-input-inline">
                <select name="sjdselect" id="sjdselect">
                    <option value="1year">一年内</option>
                    <option value="1month">一月内</option>
                    <option value="10day">十交易日内</option>
                    <option value="9day">九交易日内</option>
                    <option value="8day">八交易日内</option>
                    <option value="7day">七交易日内</option>
                    <option value="6day">六交易日内</option>
                    <option value="5day">五交易日内</option>
                    <option value="4day">四交易日内</option>
                    <option value="3day">三交易日内</option>
                    <option value="2day">二交易日内</option>
                    <option value="1day" selected>一交易日内</option>
                    <option value="3hour">三交易小时内</option>
                    <option value="2hour">二交易小时内</option>
                    <option value="1hour">一交易小时内</option>
                </select>
            </div>
            <button type="button" class="layui-btn layui-btn-normal" onclick="jkquery()">查询</button>
            <button type="button" class="layui-btn layui-btn-normal" onclick="hlsetting()">配置</button>
            <button type="button" class="layui-btn layui-btn-normal" id="refreshbtn" onclick="refreshclick()">关闭刷新</button>
            <button type="button" class="layui-btn layui-btn-normal" id="alabtn" onclick="alaclick()">禁止警报</button>
            <button type="button" class="layui-btn layui-btn-normal" id="tryalabtn" onclick="tryalaclick()">试鸣警报</button>
            <button type="button" class="layui-btn layui-btn-normal" id="anabtn" onclick="anaclick()">数据分析</button>
            <button type="button" class="layui-btn layui-btn-normal" onclick="singlepage()">独立页面</button>
            <div class="layui-input-inline" id="nowtimelbl" title="北京时间" style="width:300px;float: right;font-size: 20px;font-weight: bold;margin-right: 100px;margin-top: 10px;color:#1E9FFF;"></div>
        </div>
    </form>
    <form class="layui-form" id="settingform" style="display:none">
        <input type="text" id="settingformbzid" style="display: none">
        <div class="layui-form-item">
            <label class="layui-form-label">先买入后卖出利润率警报值</label>
            <div class="layui-input-inline">
                <input type="text" id="xmrhmclvrunlvalavalue" lay-verify="required" placeholder="每个值用逗号隔开，如“>0.1,<0.01,>=0.02,=<0.001”" autocomplete="off" class="layui-input alavalueinput">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">先买入后卖出利润警报值</label>
            <div class="layui-input-inline">
                <input type="text" id="xmrhmclvrunalavalue" lay-verify="required" placeholder="每个值用逗号隔开，如“>102.1,<103,>=105.2,=<105”" autocomplete="off" class="layui-input alavalueinput">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">先买入后卖出银行买入价警报值</label>
            <div class="layui-input-inline">
                <input type="text" id="xmrhmcyhmrjalavalue" lay-verify="required" placeholder="每个值用逗号隔开，如“>102.5,<103,>=105.9,=<105”" autocomplete="off" class="layui-input alavalueinput">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">先买入后卖出银行卖出价警报值</label>
            <div class="layui-input-inline">
                <input type="text" id="xmrhmcyhmcjalavalue" lay-verify="required" placeholder="每个值用逗号隔开，如“>102,<103.32,>=105,=<105”" autocomplete="off" class="layui-input alavalueinput">
            </div>
        </div>
<br>
        <div class="layui-form-item">
            <label class="layui-form-label">先卖出后买入利润率警报值</label>
            <div class="layui-input-inline">
                <input type="text" id="xmchmrlvrunlvalavalue" lay-verify="required" placeholder="每个值用逗号隔开，如“>0.1,<0.01,>=0.02,=<0.001”" autocomplete="off" class="layui-input alavalueinput">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">先卖出后买入利润警报值</label>
            <div class="layui-input-inline">
                <input type="text" id="xmchmrlvrunalavalue" lay-verify="required" placeholder="每个值用逗号隔开，如“>102.1,<103,>=105.2,=<105”" autocomplete="off" class="layui-input alavalueinput">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">先卖出后买入银行买入价警报值</label>
            <div class="layui-input-inline">
                <input type="text" id="xmchmryhmrjalavalue" lay-verify="required" placeholder="每个值用逗号隔开，如“>102.5,<103,>=105.9,=<105”" autocomplete="off" class="layui-input alavalueinput">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">先卖出后买入银行卖出价警报值</label>
            <div class="layui-input-inline">
                <input type="text" id="xmchmryhmcjalavalue" lay-verify="required" placeholder="每个值用逗号隔开，如“>102,<103.32,>=105,=<105”" autocomplete="off" class="layui-input alavalueinput">
            </div>
        </div>
    </form>
</div>
<div style="padding: 5px; background-color: #F2F2F2;">
    <div class="layui-row layui-col-space15">
        <%--各币种监控放置的位置--%>
    </div>
</div>
<script>
    layui.use([ 'form', 'layedit','laydate','element', 'layer'], function(){
        var form = layui.form
            , layer = layui.layer
            , layedit = layui.layedit
            , laydate = layui.laydate
            ,element = layui.element;
        //监听折叠
        element.on('collapse(test)', function(data){
            layer.msg('展开状态：'+ data.show);
        });
    });
    //账户外汇id串
    var zhwhidarr='${zhwhidstr}'.split(',');
    //账户外汇名称串
    var zhwhnamearr='${zhwhnamestr}'.split(',');
</script>
</body>
</html>