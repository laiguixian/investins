/**
 * 基础js
 */
iniwhlrtjzxt();
//加载外汇利润统计折线图
function iniwhlrtjzxt() {
    $.ajax({
        type:"POST",
        url:'/getwhjylrsj',
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
                //加载到图表
                var dom = document.getElementById("container");
                var myChart = echarts.init(dom);
                var legenddata=[];
                var xAxisdatas=[];
                var seriesitems=[];
                for(var i=0;i<data.length;i++){
                    var onebzdata=data[i];
                    //获取x轴
                    if(i==0){
                        var templirunlb=onebzdata.lirunlb;
                        for(var j=0;j<templirunlb.length;j++){
                            console.log(templirunlb[j].date);
                            xAxisdatas.push(templirunlb[j].date);
                        }
                    }
                    //获取币种名称
                    legenddata.push(onebzdata.bzname);
                    var seriesitem={};
                    seriesitem.name=onebzdata.bzname;
                    seriesitem.type='line';
                    /*//seriesitem.smooth=true;
                    //seriesitem.yAxisIndex=1;
                    seriesitem.symbol='circle';
                    seriesitem.symbolSize=1;*/
                    var pointdatas=[];
                    var lirunlb=onebzdata.lirunlb;
                    for(var j=0;j<lirunlb.length;j++){
                        var onepoint=[];
                        onepoint.push(lirunlb[j].date);
                        onepoint.push(lirunlb[j].lirun);
                        pointdatas.push(onepoint);
                    }
                    seriesitem.data=pointdatas;
                    seriesitems.push(seriesitem);
                }

                var app = {};
                option = null;
                var colors = ['#ff0000', '#00ff00', '#0000ff', '#000000'];
                option = {
                    /*color: colors,
                    /!*title: {
                        text: gname,
                        top:"1",
                    },*!/
                    tooltip: {
                        trigger: 'none',
                        axisPointer: {
                            type: 'cross'
                        },
                        top:"1"
                    },
                    legend: {
                        data: legenddata,
                        top:"1"
                    },
                    /!*grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },*!/
                    grid: {
                        top:'30',
                        left: '3',
                        right: '3',
                        bottom: '1',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    /!*xAxis: {
                        type: 'category',
                        axisTick: {
                            alignWithLabel: true
                        },
                        axisLine: {
                            onZero: false

                        },
                        axisPointer: {
                            /!*label: {
                                formatter: function (params){
                                    //console.log(JSON.stringify(params));
                                    //return JSON.stringify(params);
                                    return '时间：' + params.value
                                        + (params.seriesData.length ? '：' + ((params.seriesData.length>0)?' '+params.seriesData[0].seriesName+'：'+params.seriesData[0].data+ ((params.seriesData.length>1)?' '+params.seriesData[1].seriesName+'：'+params.seriesData[1].data+((params.seriesData.length>2)? ' '+params.seriesData[2].seriesName+'：'+params.seriesData[2].data+((params.seriesData.length>3)? ' '+params.seriesData[3].seriesName+'：' +params.seriesData[3].data:''):''):''):''):'');
                                },
                                margin:-30
                                //type:"none"
                            }*!/
                        },
                        position:"top",
                        //offset:30,
                        data: []//xAxisdatas
                    },
                    yAxis: {
                        type: 'value',
                        /!*按比例显示*!/
                        scale:true
                    },*!/
                    xAxis: {
                        //type: 'category',
                        data: []
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: seriesitems*/
                    /*xAxis: {
                        type: 'category',
                        data: ['星期一', '星期二', '星期三', '星期四']
                    },
                    yAxis: {
                        type: 'value',
                        scale:true,
                        //data: ['a', 'b', 'm', 'n', 'p', 'q']
                        data: []
                    },
                    legend: {
                        data: ['类型1','类型2'],
                        top:"1"
                    },
                    series: [{
                        name:'类型1',
                        type: 'line',
                        data: [
                            // xAxis    yAxis
                            [  '星期一',1,], // 意思是此点位于 xAxis: '星期一', yAxis: 'a'。
                            [  '星期四',3,], // 意思是此点位于 xAxis: '星期四', yAxis: 'm'。
                            [   '星期三',5,], // 意思是此点位于 xAxis: '星期三', yAxis: 'p'。
                            [   '星期二',-1,]
                        ]
                    },{
                        name:'类型2',
                        type: 'line',
                        data: [
                            // xAxis    yAxis
                            [  '星期一',2,], // 意思是此点位于 xAxis: '星期一', yAxis: 'a'。
                            [  '星期四',5,], // 意思是此点位于 xAxis: '星期四', yAxis: 'm'。
                            [   '星期三',3,], // 意思是此点位于 xAxis: '星期三', yAxis: 'p'。
                            [   '星期二',-2,]
                        ]
                    }]*/
                    tooltip: {
                        trigger: 'none',
                        axisPointer: {
                            type: 'cross'
                        },
                        top:"1"
                    },
                    grid: {
                        top:'30',
                        left: '3',
                        right: '3',
                        bottom: '1',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        axisTick: {
                            alignWithLabel: true
                        },
                        type: 'category',
                        axisPointer: {
                            label: {
                                formatter: function (params){
                                    //console.log(JSON.stringify(params));
                                    //return JSON.stringify(params);
                                    return params.seriesData[0].data;
                                },
                                margin:-30
                                //type:"none"
                            }
                        },
                        data: xAxisdatas
                    },
                    yAxis: {
                        type: 'value',
                        scale:true,
                        data: []
                    },
                    legend: {
                        data: legenddata,
                        top:"1"
                    },
                    series: seriesitems
                };
                ;
                if (option && typeof option === "object") {
                    myChart.setOption(option, true);
                }
            }
        }
    });
}