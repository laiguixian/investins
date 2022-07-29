/**
 * 基础js
 */

//选择的币种
var bzsel ="";
//已经开始获取最新汇率数据
var havegetnewhl=false;

$(function () {
    hlquery();
});
//查询汇率事件
function hlquery() {
    $("#jiaoyisjf").val(dateFtt('yyyy-MM-dd hh:mm:ss',getnjyrqsj(new Date(),30)));
    $("#jiaoyisjt").val(dateFtt('yyyy-MM-dd hh:mm:ss',new Date()));
    //按币种下拉框加载汇率折线图
    inizxtbybzxlk();
}
//按币种和获取类型加载汇率折线图
function inizxtbybzxlk() {
    console.log('/viewlrdata?kssj='+$("#jiaoyisjf").val()+'&jssj='+$("#jiaoyisjt").val());
    $.ajax({
        type:"POST",
        url:'/viewlrdata?kssj='+$("#jiaoyisjf").val()+'&jssj='+$("#jiaoyisjt").val(),
        dataType:'json',
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        //data:data,
        beforeSend:function(){
            //showLoading();
        },
        error : function(request) {
            //closeLoading();
            promptBox('提示','无法连接到服务器！','2');
        },
        success:function(data){
            console.log(data);
            //closeLoading();
            //doingAjax = false;

            //将获取到的汇率信息展示到折线图上
            if(data!=undefined) {
                data=eval(data);
                //console.log("投资额");
                //console.log("平均投资额："+data.pjtze);
                $("#explaincontent").text("平均投资额："+data.pjtze+"，利润数额："+data.lrse+"，时间跨度（单位：年）："+data.years+"年，投资笔数："+data.tzbs
                +"笔，收益率："+data.syl+"%，年化收益率："+data.nhsyl+"%");
            }
        }
    });
}