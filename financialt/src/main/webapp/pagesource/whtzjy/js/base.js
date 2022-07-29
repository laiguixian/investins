/**
 * 基础js
 */
var form;
$(function () {
    layui.use(['form', 'laydate'], function() {
        form = layui.form
            , layer = layui.layer
            , laydate = layui.laydate;
        //日期时间选择器
        laydate.render({
            elem: '#jiaoyisj'
            ,type: 'datetime'
            ,trigger: 'click'
            ,value: dateFtt('yyyy-MM-dd hh:mm:ss',new Date())
            ,isInitValue: true
        });
        //自定义验证规则
        form.verify({
            /*jyjine: function(value){
                if(value.length ==0){
                    return '请输入交易金额';
                }
            }*/
            jyjine: [
                /^(-?\d+)(\.\d+)?$/
                ,'交易金额输入有误'
            ]
            ,cjjine:[
                /^(-?\d+)(\.\d+)?$/
                ,'成交金额输入有误'
            ]
        });
        //监听提交
        form.on('submit(whjyjlform)', function(data){
            /*layer.alert(JSON.stringify(data), {
                title: '最终的提交信息'
            })*/
            //console.log("最终的提交信息："+JSON.stringify(data));
            //return false;
            //if((data.field.jyleixing=="1" || data.field.jyleixing=="2" || data.field.jyleixing=="3" || data.field.jyleixing=="4")&&
            if(data.field.cjjine=="0"){
                layer.alert("成交金额不能为0");
                return false;
            }
            if (data.field.jyjine == "0") {
                layer.alert("交易金额不能为0");
                return false;
            }
            if(data.field.jyleixing=="1" || data.field.jyleixing=="2" || data.field.jyleixing=="3" || data.field.jyleixing=="4"){
                if(data.field.jybzid=="157"){
                    layer.alert("参与外汇交易的币种不能为人民币");
                    return false;
                }
            }else {
                if(data.field.jyjine!=data.field.jyjine){
                    layer.alert("该交易类型下成交金额与交易金额必须一致");
                    return false;
                }
            }

            $.ajax({
                type:"POST",
                url:'/addwhjyjl',
                //dataType:'json',
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                data:data.field,
                beforeSend:function(){
                    //showLoading();
                },
                error : function(request) {
                    //closeLoading();
                    promptBox('提示','无法连接到服务器！','2');
                },
                success:function(data){
                    //closeLoading();
                    //doingAjax = false;
                    //显示返回信息
                    layer.alert(data.resultmsg, {
                        title: '提交信息'
                    },function () {
                        location.reload();  //页面重新加载
                    });
                }
            });
        });
        //监听卖出币种下拉选择事件
        /*form.on('select(mcbizhong)', function(data){
            //console.log(data.elem); //得到select原始DOM对象
            //console.log(data.value); //得到被选中的值
            //console.log(data.othis); //得到美化后的DOM对象
            if(mcbzid!=data.value){
                //console.log("hello:"+data.value);
                mcbzid=data.value;
            }
        });*/
        //监听交易类型选择事件
        form.on('select(jyleixing)', function(data){
            //console.log(data.elem); //得到select原始DOM对象
            //console.log(data.value); //得到被选中的值
            //console.log(data.othis); //得到美化后的DOM对象
            var jylxval=data.value;
            console.log("输出："+jylxval);
            if(jylxval!='1' && jylxval!='2' && jylxval!='3' && jylxval!='4'){
                $("#jybzid").val("157");
                form.render('select');
            }
        });
    });
});