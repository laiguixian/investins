/**
 * 基础js
 */
//是否允许刷新
var canres=true;
//币种警报字符串
//var bzalastr="";
//账户外汇警报字符串
var zhwhalastr="";
//账户贵金属警报字符串
var zhgjsalastr="";
//是否允许警报
var canala=true;
//警报音频
//var audio = new Audio("/pagesource/comm/voice/alarm.mp3");//这里的路径写上mp3文件在项目中的绝对路径
//获取数据超时
var hqsjcsaudio = new Audio("/pagesource/comm/voice/hqsjcs.mp3");
//无法连接到服务器
var wfljdfwqaudio = new Audio("/pagesource/comm/voice/wfljdfwq.mp3");
//账户贵金属开仓提示
//var zhgjskctsaudio = new Audio("/pagesource/comm/voice/zhgjskcts.mp3");
//账户贵金属平仓提示
//var zhgjspctsaudio = new Audio("/pagesource/comm/voice/zhgjspcts.mp3");
//账户外汇开仓提示
//var zhwhkctsaudio = new Audio("/pagesource/comm/voice/zhwhkcts.mp3");
//账户外汇平仓提示
//var zhwhpctsaudio = new Audio("/pagesource/comm/voice/zhwhpcts.mp3");
//账户贵金属触及临界值
var zhgjscjljzaudio = new Audio("/pagesource/comm/voice/zhgjscjljz.mp3");
//账户外汇触及临界值
var zhwhcjljzaudio = new Audio("/pagesource/comm/voice/zhwhcjljz.mp3");
//触及临界值
var cjljzaudio = new Audio("/pagesource/comm/voice/cjljz.mp3");
//试鸣警报
var smjbaudio = new Audio("/pagesource/comm/voice/smjb.mp3");
//已经获取汇率全量数据
var havegetallhl=false;
//已经开始获取最新汇率数据
var havegetnewhl=false;
//最新汇率数据
var newhldata;
//汇率选点
var huilvsel=[];
//用于记录鼠标移动时当前指向的时间各价格
var pointhuilv=new Object();
//当前进入的币种汇率图表的币种id
var enterbzid;
//汇率刷新间隔时间
var hlsxjgsj=3000;

//时间计时器是否正在运行任务
var timetimerisdoing=false;
//所有币种的信息
var allbizhongs;
//刷新数据计时器是否正在运行任务
var refdatatimerisdoing=false;
$(function () {
    getwhkz();
    settime();  //计时
    //console.log("n天前："+getnjyrqsj(new Date(),6));
});
function jkquery(){//查询按钮点击直接查询
    refdatatimerisdoing = false;
    //已经获取汇率全量数据
    havegetallhl=false;
    //已经开始获取最新汇率数据
    havegetnewhl=false;
    getwhkz();  //重新查询
}
function settime() {
    if(!timetimerisdoing) {
        timetimerisdoing = true;
        var bzidips = $(".bzidipclass");
        if (bzidips.length > 0) {
            bzidips.each(function () {
                var tempbzipid = $(this).attr("id").substring(6);
                var tempbzidvalue = $(this).val();
                if(havegetallhl) {
                    var tempallbizhongs = allbizhongs;
                    for (var j = 0; j < tempallbizhongs.length; j++) {
                        if (tempallbizhongs[j].id == tempbzidvalue) {
                            $("#ddsj" + tempbzipid).text(tempallbizhongs[j].sqmc + " " + dateFtt('yyyy-MM-dd hh:mm:ss', getto0sqsj(parseInt(tempallbizhongs[j].sjpyl))));//获取当地时间
                            break;
                        }
                    }
                }
            });
        }
        var bjshijian = getto0sqsj(480);//获取北京时间
        $("#nowtimelbl").text("北京 " + dateFtt('yyyy-MM-dd hh:mm:ss', bjshijian));
        timetimerisdoing = false;
        setTimeout(function () {
            settime();  //计时
        }, 1000);
    }
}
//获取余额大于0的外汇快照
function getwhkz(){
    if(!refdatatimerisdoing) {
        refdatatimerisdoing = true;
        if (canres) {
            $.ajax({
                type: "POST",
                url: '/getwhkuaizhaos?gettype=yedy0',
                /*dataType:'json',*/
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                //data:data,
                beforeSend: function () {
                    //showLoading();
                },
                error: function (request) {
                    refdatatimerisdoing = false;
                    setTimeout(function () {
                        getwhkz();  //重新查询
                    }, hlsxjgsj);
                    wfljdfwqaudio.play();
                    promptBox('提示', '无法连接到服务器！', '2');
                    //closeLoading();
                },
                success: function (data) {
                    getyedy0hl(data);
                }
            });
        }else{
            setTimeout(function () {
                getwhkz();  //重新查询
            }, hlsxjgsj);
        }
    }
}
//获取各币种的汇率信息
function getyedy0hl(kzdata) {
    var geturl="";
    if(havegetallhl){//只获取最新数据
        //geturl='/getlasthuilv';
        geturl='/gethuilv?gettype=last';
    }else{//最新全量数据
        geturl='/gethuilv?gettype='+$("#sjdselect").val();
    }
    if(geturl.length>0) {
        $.ajax({
            type: "POST",
            //url:'/gethuilv?gettype='+$("#sjdselect").val()+'&bizhong='+bizhongstr,
            url: geturl,
            /*dataType:'json',*/
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            //data:data,
            beforeSend: function () {
                //showLoading();
            },
            error: function (request) {
                //closeLoading();
                refdatatimerisdoing = false;
                setTimeout(function () {
                    getwhkz();  //重新查询
                }, hlsxjgsj);
                wfljdfwqaudio.play();
                promptBox('提示', '无法连接到服务器！', '2');
            },
            success: function (data) {
                data = eval(data);
                if (havegetallhl) {//只获取最新数据
                    newhldata=data;
                    //已经开始获取最新汇率数据
                    havegetnewhl=true;
                    //组装全量最新币种汇率数据
                    for(var i=0;i<allbizhongs.length; i++) {
                        for (var j = 0; j < data.length; j++) {
                            if (allbizhongs[i].id == data[j].id) {
                                var allbizhonglength=allbizhongs[i].fhhldata.length;
                                if(data[j].fhhldata.length>0) {
                                    if (allbizhonglength > 0) {//如果原来的汇率数据不为0个
                                        if (allbizhongs[i].fhhldata[allbizhonglength - 1].fbsj != data[j].fhhldata[0].fbsj) {
                                            //如果发布时间不相等，就是说汇率数据是新的
                                            //此时把第一个删除
                                            allbizhongs[i].fhhldata.splice(0, 1);
                                            //把新的汇率数据添加到币种汇率数据里
                                            //allbizhongs[i].fhhldata.push(data[j]);
                                            var allbzhuilv = allbizhongs[i].fhhldata;
                                            data[j].fhhldata = allbzhuilv.concat(data[j].fhhldata);
                                            allbizhongs[i] = data[j];
                                        }
                                        /*else{
                                                                                console.log(allbizhongs[i].bzname+"汇率没有变化");
                                                                            }*/
                                    } else {
                                        //把新的汇率数据添加到币种汇率数据里
                                        allbizhongs[i].fhhldata.push(data[j].fhhldata[0]);
                                    }
                                }
                            }
                            /*if (allbizhongs[i].id == data[j].bzid) {
                                var allbizhonglength=allbizhongs[i].fhhldata.length;
                                if(allbizhonglength>0) {//如果原来的汇率数据不为0个，此时把第一个删除
                                    if(allbizhongs[i].fhhldata[allbizhonglength-1].fbsj!=data[j].fbsj){
                                        allbizhongs[i].fhhldata.splice(0, 1);
                                        //把新的汇率数据添加到币种汇率数据里
                                        allbizhongs[i].fhhldata.push(data[j]);
                                    }/!*else{
                                        console.log(allbizhongs[i].bzname+"汇率没有变化");
                                    }*!/
                                }else {
                                    //把新的汇率数据添加到币种汇率数据里
                                    allbizhongs[i].fhhldata.push(data[j]);
                                }
                            }*/
                        }
                    }
                }else{//最新全量数据
                    allbizhongs = data;
                    //已经获取汇率全量数据
                    havegetallhl = true;
                }
                orghlbybz(allbizhongs,kzdata);
            }
        });
    }
}
//从最新数据中按币种id获取该币种汇率信息
function gethlinfofromnewbybzid(bzid) {
    for(var i=0;i<newhldata.length;i++){
        if(newhldata[i].id==bzid){
            return newhldata[i].fhhldata[0];
        }
    }
}
//将汇率信息按币种进行组装
function orghlbybz(data,kzdata){
    //总的高度
    var allheight=document.body.offsetHeight-58;
    //平均的高度
    var avgheight=parseInt((allheight*2/zhwhidarr.length))-30;
    //如果高度小于135，则取135
    if(avgheight<135){
        avgheight=135;
    }
    //账户外汇警报字符串
    zhwhalastr="";
    //账户贵金属警报字符串
    zhgjsalastr="";
    //快照币种使用到的序号
    var kzbzi=-1;
    //累加快照币种id字符串
    var ljkzbzzfc="";
    for(var i=0;i<kzdata.length;i++){
        ljkzbzzfc=ljkzbzzfc+","+kzdata[i].bzid;
        kzbzi = kzbzi + 1;
        //console.log(kzbzs[i]+","+kzdata[kzbzi].jine + "," + kzdata[kzbzi].rmbcb);
        //获取币种数据
        for(var j=0;j<data.length;j++) {
            if (data[j].id == kzdata[i].bzid){
                showbzzxt(data[j], kzbzi, avgheight, parseFloat(kzdata[i].xmrhmcjine), parseFloat(kzdata[i].xmrhmcrmbcb), parseFloat(kzdata[i].xmchmrjine), parseFloat(kzdata[i].xmchmrrmbcb));
                break;
            }
        }
    }
    for(var i=0;i<data.length;i++){
        if((ljkzbzzfc+",").indexOf(","+data[i].id+",")<0){
            kzbzi=kzbzi+1;
            showbzzxt(data[i], kzbzi, avgheight, 0, 0,0,0);
        }
    }
    //console.log("最后一个数据的发布时间;"+data[0].fbsj);
    var hqhlcs=true;//获取汇率超时标记
    for(var i=0;i<data.length;i++) {
        if(data[i].fbsj!=null) {
            var nowtime = new Date().getTime();
            var xchms = nowtime - new Date(data[i].fbsj.replace(/-/g, "/")).getTime();   //发布时间已经利用正则将横杠换成斜杠，不然ie兼容不了，时间差的毫秒数
            if (xchms < 180000) {//3分钟内没有新数据代表获取数据出现问题，即获取数据超时
                hqhlcs = false;
            }
        }
    }
    if(hqhlcs){
        //var audio =$("#alaaudio")[0];
        if(!adateisres(new Date(),2)) {
            refdatatimerisdoing = false;
            setTimeout(function () {
                getwhkz();  //重新查询
            }, hlsxjgsj);
            hqsjcsaudio.play();//播放
            layer.alert("获取汇率超时");
        }
    }else {
        if (zhwhalastr.length > 0 || zhgjsalastr.length > 0) {
            refdatatimerisdoing = false;
            setTimeout(function () {
                getwhkz();  //重新查询
            }, hlsxjgsj);
            if (canala) {
                //var audio =$("#alaaudio")[0];
                if (zhwhalastr.length > 0 && zhgjsalastr.length > 0) {
                    cjljzaudio.play();//播放
                }else if (zhwhalastr.length > 0 && zhgjsalastr.length <= 0) {
                    zhwhcjljzaudio.play();//播放
                }else if (zhwhalastr.length <= 0 && zhgjsalastr.length > 0) {
                    zhgjscjljzaudio.play();//播放
                }
                layer.alert("币种警报：" + zhwhalastr+"   "+zhgjsalastr);
            }
        }else {
            refdatatimerisdoing = false;
            setTimeout(function () {
                getwhkz();  //重新查询
            }, hlsxjgsj);
        }
    }
}
//将币种的汇率展示在折线图上
function showbzzxt(onebzdata,zxti,avgheight,xmrhmcjine,xmrhmcrmbcb,xmchmrjine,xmchmrrmbcb){
    if(onebzdata.bzname!="人民币(RMB)") {
        var hlxx = [];
        //一交易日前的银行买入价
        var yhmrj1d = 0;
        //二交易日前的银行买入价
        var yhmrj2d = 0;
        //三交易日前的银行买入价
        var yhmrj3d = 0;
        var bzid = onebzdata.id;
        var bzname = onebzdata.bzname;
        var bzzdgl = onebzdata.bzzdgl;

        //获取币种的汇率信息
        var onebzdatahl = onebzdata.fhhldata;

        if (onebzdatahl.length > 0) {
            if ($("#container" + zxti).length <= 0) {//如果该序号的元素没有创建过，则创建
                $(".layui-row.layui-col-space15").append('<div class="layui-col-md6">' +
                    '<input class="bzidipclass" id="bzidip' + zxti + '" value="" style="display: none">' +
                    '<input class="bzfxipclass" id="bzfxip' + zxti + '" value="" style="display: none">' +
                    '<input class="bzfxipclass" id="bzname' + zxti + '" value="" style="display: none">' +
                    '            <div class="layui-card">' +
                    '                <div class="layui-card-header" id="contitle' + zxti + '" style="font-size: 10px;line-height: 12px"></div>' +
                    '                <div class="layui-card-body"  id="bztb'+ zxti+'"  onmouseenter="bztbenter(this)">' +
                    '                    <div id="container' + zxti + '"  style="min-height: ' + avgheight + 'px" onclick="bztbclick()" onmouseleave="bztbleave()"></div>' +
                    '                    <div id="bztimelbl' + zxti + '" style="text-align: center;font-size: 10px;line-height: 12px;height: 27px">' +
                    '发布时间：<al id="bzfbsj' + zxti + '"></al>，非夏令时：<gx id="ddsj' + zxti + '"></gx>' +
                    '，预测：<yc id="jiageyuce' + zxti + '"></yc>' +
                    '</div>' +
                    '                </div>' +
                    '            </div>' +
                    '        </div>');
                $(".layui-card").css("min-height", avgheight + 30);
            }
            //1交易日内涨跌幅
            var zdf1day = parseFloat(onebzdata.zdf1day).toFixed(2);
            //2交易日内涨跌幅
            var zdf2day = parseFloat(onebzdata.zdf2day).toFixed(2);
            //3交易日内涨跌幅
            var zdf3day = parseFloat(onebzdata.zdf3day).toFixed(2);
            //发布时间
            var fbsjstr = onebzdata.fbsj;
            $("#bzfbsj" + zxti).text(fbsjstr);
            $("#bzidip" + zxti).val(bzid);
            //逗号替换成回车
            var reg = /[,，]/g;
            bzzdgl = bzzdgl.replace(reg, "<br/>");
            //分号替换成回车
            reg = /[;；]/g;
            bzzdgl = bzzdgl.replace(reg, "<br/>");
            $("#bzfxip" + zxti).val(bzzdgl);
            $("#bzname" + zxti).val(bzname);
            $("#jiageyuce" + zxti).text("买：高：" + onebzdata.zgxhmrj.toFixed(4) + "，均：" + onebzdata.pjzgxhmrj.toFixed(4) + "，荐：" + onebzdata.tjzgxhmrj.toFixed(4) +
                "；卖：低：" + onebzdata.zdxhmcj.toFixed(4) + "，均：" + onebzdata.pjzdxhmcj.toFixed(4) + "，荐：" + onebzdata.tjzdxhmcj.toFixed(4));
            var xmrhmclralamsg = "";//先买后卖利润警报
            var xmrhmclrlalamsg = "";//先买后卖利润率警报
            var xmchmrlralamsg = "";//先卖后买利润警报
            var xmchmrlrlalamsg = "";//先卖后买利润率警报
            var xmrhmcxhmrjalamsg = prabzala(onebzdata.xmrhmcyhmrjalavalue, onebzdata.xhmrj, "先买入后卖出现汇买入价");
            var xmrhmcxhmcjalamsg = prabzala(onebzdata.xmrhmcyhmcjalavalue, onebzdata.xhmcj, "先买入后卖出现汇卖出价");
            var xmchmrxhmrjalamsg = prabzala(onebzdata.xmchmryhmrjalavalue, onebzdata.xhmrj, "先卖出后买入现汇买入价");
            var xmchmrxhmcjalamsg = prabzala(onebzdata.xmchmryhmcjalavalue, onebzdata.xhmcj, "先卖出后买入现汇卖出价");
            var xmrhmclirunxinxi = "";//先买后卖利润信息
            var xmchmrlirunxinxi = "";//先卖后买利润信息
            if (xmrhmcjine > 0) {
                //先买后卖相比买入时的利润
                var xmrhmcmoneyget = 0;

                //先买后卖持仓均价
                var xmrhmcholdwhpri = 0;
                if(bzid>=107 && bzid<=152) {//账户外汇
                    xmrhmcholdwhpri = (xmrhmcrmbcb * 100 / xmrhmcjine).toFixed(4);
                    xmrhmcmoneyget = ((xmrhmcjine * onebzdata.xhmrj) / 100 - xmrhmcrmbcb).toFixed(2);
                }else if(bzid>=162 && bzid<=197) {//账户贵金属
                    xmrhmcholdwhpri = (xmrhmcrmbcb / xmrhmcjine).toFixed(4);
                    xmrhmcmoneyget = ((xmrhmcjine * onebzdata.xhmrj) - xmrhmcrmbcb).toFixed(2);
                }
                //先买后卖相比买入时的涨跌幅
                var xmrhmcxbmrzdf = ((xmrhmcmoneyget / xmrhmcrmbcb) * 100).toFixed(2);
                //判断有否触发币种设置的警报条件
                //console.log(onebzdata.xmrhmclvrunalavalue+","+onebzdata.xmrhmclvrunlvalavalue);
                xmrhmclralamsg = prabzala(onebzdata.xmrhmclvrunalavalue, xmrhmcmoneyget, "先买入后卖出利润");
                xmrhmclrlalamsg = prabzala(onebzdata.xmrhmclvrunlvalavalue, xmrhmcxbmrzdf, "先买入后卖出利润率");

                //对利润信息赋值
                xmrhmclirunxinxi = '，先买后卖：均：' + xmrhmcholdwhpri + '，总：' + xmrhmcjine + '，' + xmrhmcrmbcb + '，利：' + xmrhmcmoneyget + '，' + xmrhmcxbmrzdf + '%';
            }
            if (xmchmrjine > 0) {
                //先卖后买相比买入时的利润
                var xmchmrmoneyget = 0;

                //先卖后买持仓均价
                var xmchmrholdwhpri = 0;
                if(bzid>=107 && bzid<=152) {//账户外汇
                    xmchmrholdwhpri = (xmchmrrmbcb * 100 / xmchmrjine).toFixed(4);
                    xmchmrmoneyget = (xmchmrrmbcb - ((xmchmrjine * onebzdata.xhmcj) / 100)).toFixed(2);
                }else if(bzid>=162 && bzid<=197) {//账户贵金属
                    xmchmrholdwhpri = (xmchmrrmbcb / xmchmrjine).toFixed(4);
                    xmchmrmoneyget = (xmchmrrmbcb - ((xmchmrjine * onebzdata.xhmcj))).toFixed(2);
                }

                //先卖后买相比买入时的涨跌幅
                var xmchmrxbmrzdf = ((xmchmrmoneyget / xmchmrrmbcb) * 100).toFixed(2);
                //判断有否触发币种设置的警报条件
                //console.log(onebzdata.xmchmrlvrunalavalue+","+onebzdata.xmchmrlvrunlvalavalue);
                xmchmrlralamsg = prabzala(onebzdata.xmchmrlvrunalavalue, xmchmrmoneyget, "先卖出后买入利润");
                xmchmrlrlalamsg = prabzala(onebzdata.xmchmrlvrunlvalavalue, xmchmrxbmrzdf, "先卖出后买入利润率");

                //对利润信息赋值
                xmchmrlirunxinxi = '，先卖后买：均：' + xmchmrholdwhpri + '，总：' + xmchmrjine + '，' + xmchmrrmbcb + '，利：' + xmchmrmoneyget + '，' + xmchmrxbmrzdf + '%';
            }
            //将说明赋予title
            $("#contitle" + zxti).html('<input type="radio" class="settingcheck" name="bzrd" value="' + zxti + '">' + bzname + xmrhmclirunxinxi + xmchmrlirunxinxi + '，买：' + onebzdata.xhmrj + '，卖：' + onebzdata.xhmcj +
                '，三：' + zdf3day + '%，两：' + zdf2day + '%，一：' + zdf1day + '%');
            //累积警报信息
            if (xmrhmclralamsg.length > 0 || xmrhmclrlalamsg.length > 0 || xmchmrlralamsg.length > 0 || xmchmrlrlalamsg.length > 0 ||
                xmchmrxhmrjalamsg.length > 0 || xmchmrxhmcjalamsg.length > 0 || xmrhmcxhmrjalamsg.length > 0 || xmrhmcxhmcjalamsg.length > 0) {
                if(bzid>=107 && bzid<=152) {//账户外汇
                    zhwhalastr = zhwhalastr + bzname + "：" + (xmrhmclralamsg.length > 0 ? xmrhmclralamsg : "") + (xmrhmclrlalamsg.length > 0 ? "," + xmrhmclrlalamsg : "")
                        + (xmchmrlralamsg.length > 0 ? xmchmrlralamsg : "") + (xmchmrlrlalamsg.length > 0 ? "," + xmchmrlrlalamsg : "")
                        + (xmchmrxhmrjalamsg.length > 0 ? "," + xmchmrxhmrjalamsg : "") + (xmchmrxhmcjalamsg.length > 0 ? "," + xmchmrxhmcjalamsg : "")
                        + (xmrhmcxhmrjalamsg.length > 0 ? "," + xmrhmcxhmrjalamsg : "") + (xmrhmcxhmcjalamsg.length > 0 ? "," + xmrhmcxhmcjalamsg : "");
                }else if(bzid>=162 && bzid<=197) {//账户贵金属
                    zhgjsalastr = zhgjsalastr + bzname + "：" + (xmrhmclralamsg.length > 0 ? xmrhmclralamsg : "") + (xmrhmclrlalamsg.length > 0 ? "," + xmrhmclrlalamsg : "")
                        + (xmchmrlralamsg.length > 0 ? xmchmrlralamsg : "") + (xmchmrlrlalamsg.length > 0 ? "," + xmchmrlrlalamsg : "")
                        + (xmchmrxhmrjalamsg.length > 0 ? "," + xmchmrxhmrjalamsg : "") + (xmchmrxhmcjalamsg.length > 0 ? "," + xmchmrxhmcjalamsg : "")
                        + (xmrhmcxhmrjalamsg.length > 0 ? "," + xmrhmcxhmrjalamsg : "") + (xmrhmcxhmcjalamsg.length > 0 ? "," + xmrhmcxhmcjalamsg : "");
                }

            }
            //将获取到的汇率信息展示到折线图上
            pubhltozxt('container' + zxti, onebzdatahl, bzname.indexOf("账户") > - 1 ? '账户外汇' : '外汇');
        } else {
            console.log(onebzdata.bzname + "没有汇率信息");
        }
    }
}
//币种图表移入事件
function bztbenter(self){
    //console.log("移入"+self.id);
    enterbzid=$("#bzidip"+self.id.substring(4)).val();
}
//币种图表移出事件
function bztbleave(){
    huilvsel=[];
}
//币种图表点击事件
function bztbclick(){
    var newhlobj=new Object();
    newhlobj.fbsj=pointhuilv.fbsj;
    newhlobj.xhmrj=pointhuilv.xhmrj;
    newhlobj.xhmcj=pointhuilv.xhmcj;
    if(huilvsel.length==0){
        huilvsel.push(newhlobj);
    }else if(huilvsel.length==1){
        huilvsel.push(newhlobj);
        layer.alert("点1：时间："+huilvsel[0].fbsj+"，现汇买入价："+huilvsel[0].xhmrj+"，现汇卖出价："+huilvsel[0].xhmcj+
            "</br>点2：时间："+huilvsel[1].fbsj+"，现汇买入价："+huilvsel[1].xhmrj+"，现汇卖出价："+huilvsel[1].xhmcj+
            "</br>现汇买入价 涨跌幅："+(huilvsel[1].xhmrj-huilvsel[0].xhmrj)+"，涨跌率："+((huilvsel[1].xhmrj-huilvsel[0].xhmrj)*100/huilvsel[0].xhmrj).toFixed(2)+
            "</br>现汇卖出价 涨跌幅："+(huilvsel[1].xhmcj-huilvsel[0].xhmcj)+"，涨跌率："+((huilvsel[1].xhmcj-huilvsel[0].xhmcj)*100/huilvsel[0].xhmcj).toFixed(2));
        huilvsel=[];
    }
}
//打开分析窗口
function anaclick(){
    var val=$('input:radio[name="bzrd"]:checked').val();
    if(val!=null) {
        var selfxip = $("#bzfxip" + val);
        var selfbzname = $("#bzname" + val);
        layer.alert(selfbzname.val()+"<br/>"+selfxip.val());
    }else{
        layer.alert("没有选中任何币种");
    }
}
//打开配置
function hlsetting() {
    var val=$("#bzidip"+$('input:radio[name="bzrd"]:checked').val()).val();
    if(val!=null){
        $("#settingformbzid").val(val);
        var tempallbizhongs=allbizhongs;
        for(var i=0;i<tempallbizhongs.length;i++){
            if(tempallbizhongs[i].id==val){
                $("#xmrhmclvrunlvalavalue").val(tempallbizhongs[i].xmrhmclvrunlvalavalue);
                $("#xmrhmclvrunalavalue").val(tempallbizhongs[i].xmrhmclvrunalavalue);
                $("#xmrhmcyhmrjalavalue").val(tempallbizhongs[i].xmrhmcyhmrjalavalue);
                $("#xmrhmcyhmcjalavalue").val(tempallbizhongs[i].xmrhmcyhmcjalavalue);

                $("#xmchmrlvrunlvalavalue").val(tempallbizhongs[i].xmchmrlvrunlvalavalue);
                $("#xmchmrlvrunalavalue").val(tempallbizhongs[i].xmchmrlvrunalavalue);
                $("#xmchmryhmrjalavalue").val(tempallbizhongs[i].xmchmryhmrjalavalue);
                $("#xmchmryhmcjalavalue").val(tempallbizhongs[i].xmchmryhmcjalavalue);
                break;
            }
        }

        layer.open({
            type:1,
            area:['800px','500px'],
            title: '汇率配置'
            ,content: $("#settingform"),
            shade: 0,
            btnAlign:'c',
            btn: ['保存', '重置',"取消"]
            ,btn1: function(index, layero){
                $.ajax({
                    type: "POST",
                    url: '/bzset?bzid='+$("#settingformbzid").val()+
                    '&xmrhmclvrunlvalavalue='+$("#xmrhmclvrunlvalavalue").val()+"&xmrhmclvrunalavalue="+$("#xmrhmclvrunalavalue").val()+
                    "&xmrhmcyhmrjalavalue="+$("#xmrhmcyhmrjalavalue").val()+"&xmrhmcyhmcjalavalue="+$("#xmrhmcyhmcjalavalue").val()+
                    '&xmchmrlvrunlvalavalue='+$("#xmchmrlvrunlvalavalue").val()+"&xmchmrlvrunalavalue="+$("#xmchmrlvrunalavalue").val()+
                    "&xmchmryhmrjalavalue="+$("#xmchmryhmrjalavalue").val()+"&xmchmryhmcjalavalue="+$("#xmchmryhmcjalavalue").val(),
                    /*dataType:'json',*/
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                    //data:data,
                    beforeSend: function () {
                        //showLoading();
                    },
                    error: function (request) {
                        //closeLoading();
                        promptBox('提示', '无法连接到服务器！', '2');
                    },
                    success: function (data) {
                        if(data.resultcode=='101') {
                            for (var i = 0; i < allbizhongs.length; i++) {
                                if (allbizhongs[i].id == val) {
                                    allbizhongs[i].xmrhmclvrunlvalavalue=$("#xmrhmclvrunlvalavalue").val();
                                    allbizhongs[i].xmrhmclvrunalavalue=$("#xmrhmclvrunalavalue").val();
                                    allbizhongs[i].xmrhmcyhmrjalavalue=$("#xmrhmcyhmrjalavalue").val();
                                    allbizhongs[i].xmrhmcyhmcjalavalue=$("#xmrhmcyhmcjalavalue").val();

                                    allbizhongs[i].xmchmrlvrunlvalavalue=$("#xmchmrlvrunlvalavalue").val();
                                    allbizhongs[i].xmchmrlvrunalavalue=$("#xmchmrlvrunalavalue").val();
                                    allbizhongs[i].xmchmryhmrjalavalue=$("#xmchmryhmrjalavalue").val();
                                    allbizhongs[i].xmchmryhmcjalavalue=$("#xmchmryhmcjalavalue").val();
                                    break;
                                }
                            }
                        }
                        //closeLoading();
                        layer.alert(data.resultmsg);
                    }
                });
            },
            btn2: function(index, layero){
                $("#xmrhmclvrunlvalavalue").val("0");
                $("#xmrhmclvrunalavalue").val("0");
                $("#xmrhmcyhmrjalavalue").val("0");
                $("#xmrhmcyhmcjalavalue").val("0");

                $("#xmchmrlvrunlvalavalue").val("0");
                $("#xmchmrlvrunalavalue").val("0");
                $("#xmchmryhmrjalavalue").val("0");
                $("#xmchmryhmcjalavalue").val("0");
                return false;
            },
            btn3: function(index, layero){
                layer.closeAll();
            },
            cancel: function(layero,index){
                layer.closeAll();
            }

        });
    }else{
        layer.alert("没有选中任何币种");
    }
}
//全屏显示
function fullscream() {
    var index = layer.open({
        type: 2,          //默认弹出层类型
        title:"监控页面",  //弹出层角标名称
        content: "/whtzjk",  //弹出层页面地址
        area: ['1400px', '700px'],    //弹出层大小
        maxmin: true   //弹出层是否全屏
    });
    layer.full(index);
}
//独立页面打开
function singlepage() {
    window.open("/whtzjk");
}
//查询
function hlquery(){
    //已经获取汇率全量数据
    havegetallhl=false;
    //已经开始获取最新汇率数据
    havegetnewhl=false;
    getwhkz();
}
//刷新按钮点击
function refreshclick(){
    if(canres) {
        canres = false;
        $("#refreshbtn").text("开启刷新");
    }else{
        canres=true;
        $("#refreshbtn").text("关闭刷新");
        refdatatimerisdoing = false;
        setTimeout(function () {
            getwhkz();  //重新查询
        }, hlsxjgsj);
    }
}
//是否允许警报按钮点击
function alaclick(){
    if(canala) {
        //var audio =$("#alaaudio")[0];
        hqsjcsaudio.pause();//停止播放
        //无法连接到服务器
        wfljdfwqaudio.pause();//停止播放
        //账户贵金属触及临界值
        zhgjscjljzaudio.pause();//停止播放
        //账户外汇触及临界值
        zhwhcjljzaudio.pause();//停止播放
        //触及临界值
        cjljzaudio.pause();//停止播放
        //试鸣警报
        smjbaudio.pause();//停止播放
        canala = false;
        $("#alabtn").text("允许警报");
    }else{
        canala=true;
        $("#alabtn").text("禁止警报");
    }
}
//试鸣警报
function tryalaclick() {
    if($("#tryalabtn").text()=="试鸣警报") {
        smjbaudio.play();
        $("#tryalabtn").text("关闭试鸣");
    }else{
        smjbaudio.pause();
        $("#tryalabtn").text("试鸣警报");
    }
}