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
    $("#bzselect").val(112);
    $("#jiaoyisjf").val(dateFtt('yyyy-MM-dd hh:mm:ss',getnjyrqsj(new Date(),1)));
    $("#jiaoyisjt").val(dateFtt('yyyy-MM-dd hh:mm:ss',new Date()));
    //按币种下拉框加载汇率折线图
    inizxtbybzxlk();
}
//按币种和获取类型加载汇率折线图
function inizxtbybzxlk() {
    console.log('/gethuilv?getdf='+$("#jiaoyisjf").val()+'&getdt='+$("#jiaoyisjt").val()+'&bzid='+$("#bzselect").val());
    $.ajax({
        type:"POST",
        url:'/gethuilv?getdf='+$("#jiaoyisjf").val()+'&getdt='+$("#jiaoyisjt").val()+'&bzid='+$("#bzselect").val(),
        /*dataType:'json',*/
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
            //closeLoading();
            //doingAjax = false;
            data=eval(data);
            //将获取到的汇率信息展示到折线图上
            if(data.length>0) {
                pubhltozxt('container', data[0].fhhldata, $('#bzselect option:selected').text().indexOf("账户")>-1 ? '账户外汇' : '外汇');
            }
        }
    });
}