<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en" style="height: 100%">
<head>
    <meta charset="UTF-8">
    <%--<link rel="shortcut icon" href="http://www.sd.com/lotec32.ico">--%>
    <title>
        外汇投资交易列表
    </title>
    <%----%>
    <link rel="stylesheet" href="/pagesource/threepart/layui-v2.5.6/layui/css/layui.css">
    <%--<link rel="stylesheet" href="/pagesource/viewhl/css/base.css">--%>
    <script type="text/javascript" src="/pagesource/threepart/layui-v2.5.6/layui/layui.js"></script>
    <%--<script type="text/javascript" src="/pagesource/threepart/jquery/jquery-1.7.2.min.js"></script>--%>
    <script type="text/javascript" src="/pagesource/comm/js/publicjs.js"></script>
    <script type="text/javascript"src="/pagesource/whtzjylb/js/base.js"></script>
</head>
<body style="height: 100%; margin: 0">
<table id="demo" lay-filter="test"></table>
<script type="text/html" id="tabletoolbar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm" lay-event="add">添加</button>
        <button class="layui-btn layui-btn-sm" lay-event="delete">删除</button>
        <button class="layui-btn layui-btn-sm" lay-event="count">清算</button>
        <%--<button class="layui-btn layui-btn-sm" lay-event="update">编辑</button>--%>
    </div>
</script>
</body>
</html>