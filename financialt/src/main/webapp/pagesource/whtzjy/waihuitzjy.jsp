<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en" style="height: 100%">
<head>
    <meta charset="UTF-8">
    <%--<link rel="shortcut icon" href="http://www.sd.com/lotec32.ico">--%>
    <title>
        外汇投资交易
    </title>
    <%----%>
    <link rel="stylesheet" href="/pagesource/threepart/layui-v2.5.6/layui/css/layui.css">
    <%--<link rel="stylesheet" href="/pagesource/viewhl/css/base.css">--%>
    <script type="text/javascript" src="/pagesource/threepart/jquery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="/pagesource/threepart/layui-v2.5.6/layui/layui.js"></script>
    <script type="text/javascript" src="/pagesource/comm/js/publicjs.js"></script>
    <script type="text/javascript"src="/pagesource/whtzjy/js/base.js"></script>
</head>
<body style="height: 100%; margin: 0">
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>外汇投资交易</legend>
</fieldset>
<div style="padding: 20px; background-color: #F2F2F2;">
    <div class="layui-row layui-col-space10">
        <%--<div class="layui-col-md6">--%>
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-card-body">
                    <form class="layui-form" action="/addwhjyjl" id="whjyjlform" method="post">
                        <div class="layui-form-item">
                            <label class="layui-form-label">交易币种</label>
                            <div class="layui-input-inline">
                                <select name="jybzid" id="jybzid" lay-filter="jybzid" lay-verify="required">
                                    <c:forEach items="${bizhongs}" var="bizhong">
                                        <%--<option value="${bizhong}" <c:if test="${bizhong=='美元(USD)'}">selected</c:if>>${bizhong}</option>--%>
                                        <option value="${bizhong.id}">${bizhong.bzname}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <label class="layui-form-label">交易类型</label>
                            <div class="layui-input-inline">
                                <select name="jyleixing" id="jyleixing" lay-verify="required"  lay-filter="jyleixing">
                                    <option value="" selected>请选择</option>
                                    <option value="1">买入开仓</option>
                                    <option value="2">卖出平仓</option>
                                    <option value="3">卖出开仓</option>
                                    <option value="4">买入平仓</option>
                                    <option value="30">交账户外汇保证金</option>
                                    <option value="31">交账户贵金属保证金</option>
                                    <option value="40">退账户外汇保证金</option>
                                    <option value="41">退账户贵金属保证金</option>
                                    <option value="50">转微信</option>
                                    <option value="51">转支付宝</option>
                                    <option value="52">转翼支付</option>
                                    <option value="69">理财购买</option>
                                    <option value="70">微信转入</option>
                                    <option value="71">支付宝转入</option>
                                    <option value="72">翼支付转入</option>
                                    <option value="89">理财赎回</option>
                                    <option value="90">现金存款</option>
                                    <option value="91">转账入</option>
                                    <option value="92">利息</option>
                                    <option value="110">取现</option>
                                    <option value="111">转账出</option>
                                    <option value="112">消费</option>
                                    <option value="113">短信费</option>
                                    <option value="114">开卡费</option>
                                    <option value="115">换卡费</option>
                                    <option value="116">开户费</option>
                                </select>
                            </div>

                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">交易金额</label>
                            <div class="layui-input-inline">
                                <input type="text" name="jyjine" id="jyjine" lay-verify="jyjine" autocomplete="off" placeholder="请输入交易金额" class="layui-input">
                            </div>
                            <label class="layui-form-label">成交金额（人民币）</label>
                            <div class="layui-input-inline">
                                <input type="text" name="cjjine" id="cjjine" lay-verify="cjjine" autocomplete="off" placeholder="请输入成交金额" class="layui-input">
                            </div>
                        </div>
                        <div class="layui-form-item">

                            <label class="layui-form-label">交易时间</label>
                            <div class="layui-input-inline">
                                <input type="text" class="layui-input" lay-verify="required" id="jiaoyisj" name="jiaoyisj" placeholder="yyyy-MM-dd HH:mm:ss">
                            </div>
                            <label class="layui-form-label">备注</label>
                            <div class="layui-inline">
                                <div class="layui-input-inline">
                                    <input type="text" class="layui-input" id="remark" name="remark">
                                </div>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <div class="layui-input-inline">
                                    <button type="button" class="layui-btn" onclick="" lay-submit="" lay-filter="whjyjlform" id="whjyjlformsubmitbtn">提交</button>
                                </div>
                                <div class="layui-input-inline">
                                    <button type="button" class="layui-btn" onclick="">取消</button>
                                </div>
                            </div>
                        </div>
                        <input id="userid" name="userid" style="display: none" value="1">
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>