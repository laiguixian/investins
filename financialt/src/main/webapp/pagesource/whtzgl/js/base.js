/**
 * 基础js
 */
$(function () {
    inibztbywhkz("yedy0");
});
//按外汇快照加载投资饼状图
function inibztbywhkz(gettype) {
    $.ajax({
        type:"POST",
        url:'/getwhkuaizhaos?gettype='+gettype,
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
            //将获取到的汇率信息展示到折线图上
            pubhltobzt(data);
        }
    });
}
//将获取到的汇率信息展示到饼状图上
function pubhltobzt(data){
    var dom = document.getElementById("container");
    var myChart = echarts.init(dom);
    var app = {};
    var bizhongs=[];
    var bzjines=[];
    var seriesdatas=[];
    var zrmbjine=0;
    var explaintext="外汇资产情况：";
    for (var i = 0; i < data.length; i++) {
        var seriesdata = new Object();
        seriesdata.value=(data[i].xmrhmcrmbcb+data[i].xmchmrrmbcb);
        seriesdata.name=data[i].bzname+"，先买后卖："+data[i].xmrhmcjine+"，"+data[i].xmrhmcrmbcb+"，先卖后买："+data[i].xmchmrjine+"，"+data[i].xmchmrrmbcb;
        seriesdatas.push(seriesdata);
        zrmbjine=zrmbjine+data[i].xmrhmcrmbcb+data[i].xmchmrrmbcb;
    }
    option = null;
    option = {
        title: {
            text: '外汇投资概览图',
            subtext: '总的折合成人民币相当于'+zrmbjine+'元',
            left: 'center'
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: bizhongs
        },
        series: [
            {
                name: '币种折合人民币',
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data: seriesdatas,
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
    }
}