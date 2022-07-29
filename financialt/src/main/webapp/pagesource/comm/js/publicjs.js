/**
 * 公共函数
 */
function endWith(holestr,endStr){
    var d = holestr.length - endStr.length;
    return (d >= 0&&holestr.lastIndexOf(endStr) == d);
}
/* 是否确认弹出框(确认和取消按钮)-----开始----tips:提示标题 *****content:提示内容****url:点击确认按钮后跳转的页面url */
	function isConfirm(tips,content,url){
		content=filtersomechar(content);
		layer.confirm(content, {title:tips, area: ['400px', '180px'], btnAlign: 'c'}, function(index){
			 window.location.href = url;
			 layer.close(index);
		})
	}
	/* 是否确认弹出框(确认按钮)-----结束---- */	
	
	/* 提示框(确认按钮)-----开始---- */
	function promptBox(tips,content,ico){
		if(ico == '-1'){
			ico = -1;
		}
		layer.alert(content, {
			title: tips,
			  icon: ico,
			  btn: ['确认'],
			  skin: 'layer-ext-moon'
		})
	}
	/* 提示框-----结束---- */	
	
	/*
	 * 自定义弹出框(确认和取消按钮)-----开始----title:弹出框标题 *****startStr:弹出框中定义的标签
	 * *****areaheight:弹出框的高度，例如'500px'****areawidth:弹出框的宽度例如：'210px'
	 */
	function customConfirm(title,content,areaheight,areawidth,url){
		content=filtersomechar(content);
		layer.confirm(
				content,
	            {
	                title: title,
	                closeBtn: false,
	                area: [areaheight, areawidth],
	                btnAlign: 'c'
	            },
	            function(index){
	            	parent.parent.showLoading();
	            	window.location.href = url;
	            	if(url.indexOf("_exceldo")>-1||url.indexOf("dlfile")>-1){
	            		parent.parent.closeLoading();
	            	}
	                // 确定操作
	                layer.close(index);
	        });
	}
	/* 自定义弹出框(确认按钮)-----结束---- */
	
	/*
	 * 自定义弹出框(确认和取消按钮)-----开始----title:弹出框标题 *****startStr:弹出框中定义的标签
	 * *****areaheight:弹出框的高度，例如'500px'****areawidth:弹出框的宽度例如：'210px'
	 */
	function customConfirm2(title,content,areaheight,areawidth,formobj){
		content=filtersomechar(content);
		layer.confirm(
				content,
	            {
	                title: title,
	                closeBtn: false,
	                area: [areaheight, areawidth],
	                btnAlign: 'c'
	            },
	            function(index){
	            	parent.parent.showLoading();
	            	// alert("action="+$(formobj).attr("action"));
	            	$(formobj).submit();
	                // 确定操作
	                layer.close(index);
	        });
	}
	/* 自定义弹出框(确认按钮)-----结束---- */
	//写入cookie到主域，设置过期时间单位为秒
    function SetCookie(name, value,second) {
        console.log(name, value)
        var str = name + "=" + escape(value) + ";domain="+thisdomain+";path=/";
        var date = new Date();
        date.setTime(date.getTime() + second * 1000); //设置date为当前时间加一年

        str += ";expires=" + date.toGMTString();
        console.log(str)
        document.cookie = str;
    }
    //获取cookie
    function getCookie(c_name, defvalue) {
    	if (document.cookie.length > 0) {
    		c_start = document.cookie.indexOf(c_name + "=");
    		if (c_start != -1) {
    			c_start = c_start + c_name.length + 1;
    			c_end = document.cookie.indexOf(";", c_start);
    			if (c_end == -1)
    				c_end = document.cookie.length;

    			return unescape(document.cookie.substring(c_start, c_end));
    		}
    	}
    	return defvalue;
    }
    //删除cookie
    function delCookie(c_name) {
    	var str = c_name + "=" + escape("") + ";domain="+thisdomain+";path=/";
        var date = new Date();
        date.setTime(date.getTime()-1); //设置date为过去的时间，这就相当于删除了

        str += ";expires=" + date.toGMTString();
        console.log(str)
        document.cookie = str;
    }
    //退出
    function exitsystem(tourl){
    	delCookie("itwebuserregcode");
		delCookie("itwebuserreg");
		delCookie("itwebudisplayname");
		top.location.href=tourl;
    }
    /* 是否是手机 */
    function isMobile(s) {
    	var regu = /^[1][0-9]{10}$/;
    	var re = new RegExp(regu);
    	if (re.test(s)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    function trim(str) { // 删除左右两端的空格
    	return str.replace(/(^\s*)|(\s*$)/g, "");
    }
    /*
     * 用途：检查输入对象的值是否符合E-Mail格式 输入：str 输入的字符串 返回：如果通过验证返回true,否则返回false
     * 
     */
    function isEmail(str) {
    	var myReg = /^[-_A-Za-z0-9]+@([_A-Za-z0-9]+\.)+[A-Za-z0-9]{2,3}$/;
    	if (myReg.test(str))
    		return true;
    	return false;
    }
    /*
     * 用途：检查输入字符串是否只由英文字母和数字组成 输入： s：字符串 返回： 如果通过验证返回true,否则返回false
     * 
     */
    function isNumberOrLetter(s) {// 判断是否是数字或字母

    	var regu = "^[0-9a-zA-Z]+$";
    	var re = new RegExp(regu);
    	if (re.test(s)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    /*
     * 用途：检查输入字符串是否只由汉字、字母、数字组成 输入： value：字符串 返回： 如果通过验证返回true,否则返回false
     * 
     */
    function isChinaOrNumbOrLett(s) {// 判断是否是汉字、字母、数字组成

    	var regu = "^[0-9a-zA-Z\u4e00-\u9fa5]+$";
    	var re = new RegExp(regu);
    	if (re.test(s)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    /* 是否全由数字组成 */
    function isNumber(s) {
    	var regu = "^[0-9]+$";
    	var re = new RegExp(regu);
    	if (s.search(re) != -1) {
    		return true;
    	} else {
    		return false;
    	}
    }
    function rvisit(pn,ur,ar) {
		$.ajax({		
			cache : false,
			type : "POST",
			url : commser+"/rjv?pn="+pn+"&ur="+ur+"&ar="+ar,
			data : "", //要发送的是ajaxFrm表单中的数据
			async : false,
			processData : false,
			contentType : false,
			beforeSend : function() {
				
			},
			error : function(request) {
				
			},
			success : function(data) {
				
			}
		});
    }
    function rovisit(pn,ur) {
		$.ajax({		
			cache : false,
			type : "POST",
			url : commser+"/rojv?pn="+pn+"&ur="+ur,
			data : "", //要发送的是ajaxFrm表单中的数据
			async : false,
			processData : false,
			contentType : false,
			beforeSend : function() {
				
			},
			error : function(request) {
				
			},
			success : function(data) {
				
			}
		});
    }
    function titr(){//tranimgsrctoreal
    	$("img").each(function(){
    	    var imgsrc=$(this).attr("src");
    		if((imgsrc.indexOf("http://articleuimg.lujishu.net/getimg?m=")>-1) || (imgsrc.toLowerCase().indexOf("?m=v1")>-1)){
    			var imgnamestr=imgsrc.substring(imgsrc.indexOf("=")+1);
    			var needtotran=false;
    			var tran201801=false;
    			var imgext="";
    			var imgextnum="";
    			if(endWith(imgnamestr,"aaa")){
    				imgext="bmp";
    				needtotran=true;
    				imgextnum="2";
    			}else if(endWith(imgnamestr,"bbb")){
    				imgext="jpg";
    				needtotran=true;
    				imgextnum="1";
    			}else if(endWith(imgnamestr,"ccc")){
    				imgext="gif";
    				needtotran=true;
    				imgextnum="3";
    			}else if(endWith(imgnamestr,"ddd")){
    				imgext="png";
    				needtotran=true;
    				imgextnum="4";
    			}
    			var imghan="";
    			if(needtotran){
    				tran201801=(!(imgnamestr.toLowerCase().indexOf("v")==0));
	    			if(tran201801){
	    				  imghan=imgnamestr.substring(0,imgnamestr.indexOf("2018"));
	    				  var hanstr="";
	    				  var midname=imgnamestr.substring(0,imgnamestr.length-3);
	    				  if (imghan=='1')
		    		        hanstr='a'+imghan;
		    		      else if (imghan=='2')
		    		        hanstr='b'+imghan;
		    		      else if (imghan=='3')
		    		        hanstr='c'+imghan;
		    		      else if (imghan=='4')
		    		        hanstr='d'+imghan;
		    		      else if (imghan=='5')
		    		        hanstr='e'+imghan;
		    		      else if (imghan=='6')
		    		        hanstr='f'+imghan;
		    		      else if (imghan=='7')
		    		        hanstr='g'+imghan;
		    		      else if (imghan=='8')
		    		        hanstr='h'+imghan;
		    		      else if (imghan=='9')
		    		        hanstr='i'+imghan;
		    		      else if (imghan=='0')
		    		        hanstr='z'+imghan;
                          if(imghan.length>0){
                            midname=imgnamestr.substring(1,imgnamestr.length-3);
                            year=midname.substring(1,5);
                            month=midname.substring(5,7);
                            day=midname.substring(7,9);
                          }
		    		      var year=midname.substring(0,4);
		    		      var month=midname.substring(4,6);
						  var day=midname.substring(6,8);


	    				  var newsrc=fileserverurl+"/uf/b/blog/"+imgext+"/"+year+"/"+month+"/"+day+"/"+imgextnum+midname+hanstr+"."+imgext;
		    		      console.log("图片地址="+newsrc);
	    				  $(this).attr("src",newsrc);
	    			}else{
	    				if(imgnamestr.toLowerCase().indexOf("v1")==0){
	    					var year=imgnamestr.substring(2,6);
		    				var month=imgnamestr.substring(6,8);
		    				var day=imgnamestr.substring(8,10);
		    				var newsrc=fileserverurl+"/uf/b/blog/"+imgext+"/"+year+"/"+month+"/"+day+"/V"+imgnamestr.substring(1)+"."+imgext;
		    				$(this).attr("src",newsrc);
	    				}
	    			}
    			}
    		}
    	});
    }

    //获取浏览器中的链接参数
	function getQueryVariable(variable)
	{
		var query = window.location.search.substring(1);
		var vars = query.split("&");
		for (var i=0;i<vars.length;i++) {
			var pair = vars[i].split("=");
			if(pair[0] == variable){return pair[1];}
		}
		return "";
	}
    
/**************************************时间格式化处理************************************/
function dateFtt(fmt,date)
{ //author: meizz
    var o = {
        "M+" : date.getMonth()+1,     //月份
        "d+" : date.getDate(),     //日
        "h+" : date.getHours(),     //小时
        "m+" : date.getMinutes(),     //分
        "s+" : date.getSeconds(),     //秒
        "q+" : Math.floor((date.getMonth()+3)/3), //季度
        "S" : date.getMilliseconds()    //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

//加载外汇汇率折线图
function inihlzxt(container,gname,legenddata,xAxisdatas,xhmrjs,xcmrjs,xhmcjs,xcmcjs){
    //加载到图表
    var dom = document.getElementById(container);
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    var colors = ['#ff0000', '#00ff00', '#0000ff', '#000000'];
    option = {
        color: colors,
        /*title: {
            text: gname
        },*/
        tooltip: {
            trigger: 'none',
            axisPointer: {
                type: 'cross'
            }
        },
        legend: {
            data: legenddata
        },
        /*grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },*/
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
            type: 'category',
            axisTick: {
                alignWithLabel: true
            },
            axisLine: {
                onZero: false

            },
            axisPointer: {
                label: {
                    formatter: function (params){
                        //console.log(JSON.stringify(params));
                        //console.log(JSON.stringify(xhmrjs));
                        //return JSON.stringify(params);
                        if(havegetnewhl && params.seriesData.length) {
                            //获取最新的汇率信息
                            var lasthl=gethlinfofromnewbybzid(enterbzid);
                            var datastr = "";
                            pointhuilv.fbsj=params.value;
                            for (var i = 0; i < params.seriesData.length; i++) {
                                datastr = datastr + ' ' + params.seriesData[i].seriesName + '：' + params.seriesData[i].data;
                                if (params.seriesData[i].seriesName == '现汇买入价') {
                                    pointhuilv.xhmrj=params.seriesData[i].data;
                                    datastr = datastr + ' ' + (lasthl.xhmrj - params.seriesData[i].data).toFixed(2)+' '+((lasthl.xhmrj - params.seriesData[i].data)*100/params.seriesData[i].data).toFixed(2);
                                } else if (params.seriesData[i].seriesName == '现钞买入价') {
                                    pointhuilv.xcmrj=params.seriesData[i].data;
                                    datastr = datastr + ' ' + (lasthl.xcmrj - params.seriesData[i].data).toFixed(2)+' '+((lasthl.xcmrj - params.seriesData[i].data)*100/params.seriesData[i].data).toFixed(2);
                                } else if (params.seriesData[i].seriesName == '现汇卖出价') {
                                    pointhuilv.xhmcj=params.seriesData[i].data;
                                    datastr = datastr + ' ' + (lasthl.xhmcj - params.seriesData[i].data).toFixed(2)+' '+((lasthl.xhmcj - params.seriesData[i].data)*100/params.seriesData[i].data).toFixed(2);
                                } else if (params.seriesData[i].seriesName == '现钞卖出价') {
                                    pointhuilv.xcmcj=params.seriesData[i].data;
                                    datastr = datastr + ' ' + (lasthl.xcmcj - params.seriesData[i].data).toFixed(2)+' '+((lasthl.xcmcj - params.seriesData[i].data)*100/params.seriesData[i].data).toFixed(2);
                                }
                            }
                            return '时间：' + params.value + '：' + datastr;
                        }else {
                            return '时间：' + params.value
                                + (params.seriesData.length ? '：' + ((params.seriesData.length > 0) ? ' ' + params.seriesData[0].seriesName + '：' + params.seriesData[0].data + ((params.seriesData.length > 1) ? ' ' + params.seriesData[1].seriesName + '：' + params.seriesData[1].data + ((params.seriesData.length > 2) ? ' ' + params.seriesData[2].seriesName + '：' + params.seriesData[2].data + ((params.seriesData.length > 3) ? ' ' + params.seriesData[3].seriesName + '：' + params.seriesData[3].data : '') : '') : '') : '') : '');
                        }
                    },
                    margin:-30
                    //type:"none"
                }
            },
            position:"top",
            //offset:30,
            data: xAxisdatas
        },
        yAxis: {
            type: 'value',
            /*按比例显示*/
            scale:true
        },
        series: [
            {
                name: legenddata[0],
                type: 'line',
                smooth: true,
                data: xhmrjs
            },
            {
                name: legenddata[1],
                type: 'line',
                smooth: true,
                data: xcmrjs
            },
            {
                name: legenddata[2],
                type: 'line',
                smooth: true,
                data: xhmcjs
            },
            {
                name: legenddata[3],
                type: 'line',
                smooth: true,
                data: xcmcjs
            }
        ]
    };
    ;
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
}

//加载账户外汇汇率折线图
//加载外汇汇率折线图
function inizhwhhlzxt(container,gname,legenddata,xAxisdatas,xhmrjs,xcmrjs,xhmcjs,xcmcjs){
    //加载到图表
    var dom = document.getElementById(container);
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    var colors = ['#ff0000', '#00ff00'];
    option = {
        color: colors,
        /*title: {
            text: gname
        },*/
        tooltip: {
            trigger: 'none',
            axisPointer: {
                type: 'cross'
            }
        },
        legend: {
            data: legenddata
        },
        /*grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },*/
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
            type: 'category',
            axisTick: {
                alignWithLabel: true
            },
            axisLine: {
                onZero: false

            },
            axisPointer: {
                label: {
                    formatter: function (params){
                        //console.log(JSON.stringify(params));
                        //console.log(JSON.stringify(xhmrjs));
                        //return JSON.stringify(params);
                        if(havegetnewhl && params.seriesData.length) {
                            //获取最新的汇率信息
                            var lasthl=gethlinfofromnewbybzid(enterbzid);
                            var datastr = "";
                            pointhuilv.fbsj=params.value;
                            for (var i = 0; i < params.seriesData.length; i++) {
                                datastr = datastr + ' ' + params.seriesData[i].seriesName + '：' + params.seriesData[i].data;
                                if (params.seriesData[i].seriesName == '现汇买入价') {
                                    pointhuilv.xhmrj=params.seriesData[i].data;
                                    datastr = datastr + ' ' + (lasthl.xhmrj - params.seriesData[i].data).toFixed(2)+' '+((lasthl.xhmrj - params.seriesData[i].data)*100/params.seriesData[i].data).toFixed(2);
                                } else if (params.seriesData[i].seriesName == '现钞买入价') {
                                    pointhuilv.xcmrj=params.seriesData[i].data;
                                    datastr = datastr + ' ' + (lasthl.xcmrj - params.seriesData[i].data).toFixed(2)+' '+((lasthl.xcmrj - params.seriesData[i].data)*100/params.seriesData[i].data).toFixed(2);
                                } else if (params.seriesData[i].seriesName == '现汇卖出价') {
                                    pointhuilv.xhmcj=params.seriesData[i].data;
                                    datastr = datastr + ' ' + (lasthl.xhmcj - params.seriesData[i].data).toFixed(2)+' '+((lasthl.xhmcj - params.seriesData[i].data)*100/params.seriesData[i].data).toFixed(2);
                                } else if (params.seriesData[i].seriesName == '现钞卖出价') {
                                    pointhuilv.xcmcj=params.seriesData[i].data;
                                    datastr = datastr + ' ' + (lasthl.xcmcj - params.seriesData[i].data).toFixed(2)+' '+((lasthl.xcmcj - params.seriesData[i].data)*100/params.seriesData[i].data).toFixed(2);
                                }
                            }
                            return '时间：' + params.value + '：' + datastr;
                        }else {
                            return '时间：' + params.value
                                + (params.seriesData.length ? '：' + ((params.seriesData.length > 0) ? ' ' + params.seriesData[0].seriesName + '：' + params.seriesData[0].data + ((params.seriesData.length > 1) ? ' ' + params.seriesData[1].seriesName + '：' + params.seriesData[1].data + ((params.seriesData.length > 2) ? ' ' + params.seriesData[2].seriesName + '：' + params.seriesData[2].data + ((params.seriesData.length > 3) ? ' ' + params.seriesData[3].seriesName + '：' + params.seriesData[3].data : '') : '') : '') : '') : '');
                        }
                    },
                    margin:-30
                    //type:"none"
                }
            },
            position:"top",
            //offset:30,
            data: xAxisdatas
        },
        yAxis: {
            type: 'value',
            /*按比例显示*/
            scale:true
        },
        series: [
            {
                name: legenddata[0],
                type: 'line',
                smooth: true,
                data: xhmrjs
            },
            {
                name: legenddata[1],
                type: 'line',
                smooth: true,
                data: xcmrjs
            },
            {
                name: legenddata[2],
                type: 'line',
                smooth: true,
                data: xhmcjs
            },
            {
                name: legenddata[3],
                type: 'line',
                smooth: true,
                data: xcmcjs
            }
        ]
    };
    ;
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
}

//将获取到的汇率信息展示到折线图上
function pubhltozxt(container,data,whlx) {
    if(data.length>0) {
        var legenddata;
        if(whlx=="账户外汇"){
            legenddata= ['现汇买入价','现汇卖出价'];
        }else{
            legenddata= ['现汇买入价', '现钞买入价', '现汇卖出价', '现钞卖出价'];
        }
        var xAxisdatas = [];
        var xhmrjs = [];
        var xcmrjs = [];
        var xhmcjs = [];
        var xcmcjs = [];
        //现汇最高买入价
        var xhzgmrj=0;
        //现汇最低卖出价
        var xhzdmcj=0;
        //现汇最低买入价
        var xhzdmrj=0;
        //时间在现汇最高买入价之前的现汇最低卖出价（用于计算最高可图利的金额）
        var sjzxhzgmrjzqxhzdmcj=0;
        //现汇最高买入价的序号
        var xhzgmrjindex=-1;
        for (var i = 0; i < data.length; i++) {
            xAxisdatas.push(dateFtt("yyyy-MM-dd hh:mm:ss",new Date(data[i].fbsj.replace(/-/g, "/"))));//发布时间已经利用正则将横杠换成斜杠，不然ie兼容不了
            xhmrjs.push(data[i].xhmrj);
            //获取现汇最高买入价
            if(xhzgmrj<data[i].xhmrj){
                xhzgmrj=data[i].xhmrj;
                xhzgmrjindex=i;
            }
            xcmrjs.push(data[i].xcmrj);
            xhmcjs.push(data[i].xhmcj);
            //现汇最低卖出价
            if(xhzdmcj>data[i].xhmcj || xhzdmcj==0){
                xhzdmcj=data[i].xhmcj;
            }
            //现汇最低买入价

            if(xhzdmrj>data[i].xhmrj || xhzdmrj==0){
                xhzdmrj=data[i].xhmrj;
            }
            xcmcjs.push(data[i].xcmcj);
        }
        //获取时间在现汇最高买入价之前的现汇最低卖出价（用于计算最高可图利的金额）
        for (var i = 0; i < xhzgmrjindex; i++) {
            if(sjzxhzgmrjzqxhzdmcj>data[i].xhmcj || sjzxhzgmrjzqxhzdmcj==0){
                sjzxhzgmrjzqxhzdmcj=data[i].xhmcj;
            }
        }
        if($("#explaincontent").length>0) {
            $("#explaincontent").text('按图形所示分析（仅供参考，投资需谨慎）：现汇最高买入价 ' + xhzgmrj + '，现汇最低卖出价 ' + xhzdmcj +
                '，现汇最低买入价 '+xhzdmrj+'，时间在现汇最高买入价之前的现汇最低卖出价 ' + sjzxhzgmrjzqxhzdmcj + '，理论图示可图利金额 ' +
                (xhzgmrj - xhzdmcj).toFixed(2) + '，利润率 ' + ((xhzgmrj - xhzdmcj) * 100 / xhzdmcj).toFixed(2) + '%，实际图示可图利金额 ' +
                (xhzgmrj - sjzxhzgmrjzqxhzdmcj).toFixed(2) + '，利润率 ' + ((xhzgmrj - sjzxhzgmrjzqxhzdmcj) * 100 / sjzxhzgmrjzqxhzdmcj)
                    .toFixed(2) + '%)，高低涨跌幅 '+(xhzgmrj-xhzdmrj).toFixed(2)+' 涨跌率 '+((xhzgmrj-xhzdmrj)*100/xhzdmrj).toFixed(2)+
            '，对比当前：高价位：跌'+(xhzgmrj-data[data.length-1].xhmrj).toFixed(2)+","+
                ((xhzgmrj-data[data.length-1].xhmrj)*100/data[data.length-1].xhmrj).toFixed(2)+
            '，低价位：涨'+(data[data.length-1].xhmrj-xhzdmrj).toFixed(2)+","+
                ((data[data.length-1].xhmrj-xhzdmrj)*100/xhzdmrj).toFixed(2));
        }
        //加载汇率折线图
        if(whlx=="账户外汇"){
            inizhwhhlzxt(container,'汇率展示图', legenddata, xAxisdatas, xhmrjs, xhmcjs);
        }else {
            inihlzxt(container,'汇率展示图', legenddata, xAxisdatas, xhmrjs, xcmrjs, xhmcjs, xcmcjs);
        }
    }
}
//获取某个时间的n个交易日前的时间
function getnjyrqsj(nowtime,n) {
    var i=0;
    while(i<n){
        //时间减去1天
        nowtime.setDate(nowtime.getDate()-1);
        //获取时间的星期
        var dnday=nowtime.getDay();
        //console.log("星期"+dnday);
        if(dnday>=1 && dnday<=5){
            i=i+1;
        }
    }
    return nowtime;
}

//获取某个时间的n分钟前的时间
function getnminsj(nowtime,n) {
    var temptime=nowtime;
    temptime.setMinutes(temptime.getMinutes()+n);
    return temptime;
}
//获取相对0时区n分钟偏移量地区的时间
function getto0sqsj(n) {
    var nowtime=new Date();
    var to0timen=nowtime.getTimezoneOffset();//计算出跟0时区相差的分钟偏移量
    var now0time=getnminsj(nowtime,to0timen);//获取0时区的时间
    var finaltime=getnminsj(now0time,n);//获取相对0时区偏移的时间
    return finaltime;
}

//解析币种的警报值设置数据并进行判断
function prabzala(aladata,indata,datalabel) {
    var returnvalue="";
    var aladataarr=aladata.split(",");
    for(var i=0;i<aladataarr.length;i++){
        //console.log(aladataarr[i]);
        if((aladataarr[i].indexOf(">")>-1 || (aladataarr[i].indexOf("<")>-1) || (aladataarr[i].indexOf("=")>-1)) && aladataarr[i].length>=2){//至少包含一个符号（比如大于号）和一个数字
            if(aladataarr[i].indexOf(">=")==0 || aladataarr[i].indexOf("=>")==0){//大于等于
                if(indata>=parseFloat(aladataarr[i].substring(2))){
                    //console.log(indata+">="+aladataarr[i].substring(2));
                    returnvalue=returnvalue+","+datalabel+"当前值为"+indata+"，大于或等于设置的"+aladataarr[i].substring(2);
                }
            }else if(aladataarr[i].indexOf("<=")==0 || aladataarr[i].indexOf("=<")==0){//小于等于
                if(indata<=parseFloat(aladataarr[i].substring(2))){
                    //console.log(indata+"<="+aladataarr[i].substring(2));
                    returnvalue=returnvalue+","+datalabel+"当前值为"+indata+"，小于或等于设置的"+aladataarr[i].substring(2);
                }
            }else if(aladataarr[i].indexOf(">")==0){//大于
                if(indata>parseFloat(aladataarr[i].substring(1))){
                    //console.log(indata+"x>"+aladataarr[i].substring(1));
                    returnvalue=returnvalue+","+datalabel+"当前值为"+indata+"，大于设置的"+aladataarr[i].substring(1);
                }
            }else if(aladataarr[i].indexOf("<")==0){//小于
                if(indata<parseFloat(aladataarr[i].substring(1))){
                    //console.log(indata+"x<"+aladataarr[i].substring(2));
                    returnvalue=returnvalue+","+datalabel+"当前值为"+indata+"，小于设置的"+aladataarr[i].substring(1);
                }
            }
        }
    }
    return returnvalue;
}

//根据给定的休市时间范围判断当前时间是否是休市时间
//判断一个时间是否休市时间，gettype-获取类型：1-采集时间（按银行实际发布时间），2-汇率超时警报时间（按银行规定开市，休市时间）
function isnowres(fromdate,todate,gettype){
    var tempisrestdate = false;
    var nowtime=new Date();
    var thisfromdate=fromdate;
    var thistodate=todate;
    if(gettype==1){//如果是采集时间则休市时间起始需要往后推1个小时，结束时间需要往前提一个小时
        thisfromdate.setHours(thisfromdate.getHours()+1);
        thistodate.setHours(thistodate.getHours()-1);
    }else if(gettype==2){//如果是警报时间则休市时间起始需要往前提1个小时，结束时间需要往后退一个小时
        thisfromdate.setHours(thisfromdate.getHours()-1);
        thistodate.setHours(thistodate.getHours()+1);
    }
    if(nowtime>thisfromdate && nowtime<thistodate){
        tempisrestdate=true;
    }
    return tempisrestdate;
}

//判断一个时间是否休市时间，gettype-获取类型：1-采集时间（按银行实际发布时间），2-汇率超时警报时间（按银行规定开市，休市时间）
function adateisres(indate,gettype){
    var tempisrestdate = true;
    //获取时间的星期
    var dnday=indate.getDay();
    //获取时间的月部分
    var monthint = dateFtt('MM',indate);
    //获取时间的天部分
    var dayint = dateFtt('dd',indate);
    //获取时间的小时部分
    var hourint = dateFtt('hh',indate);
    //特定的休市时间
    /*tempisrestdate = false;
    if(
        //2021年
        //一、元旦
        //2021年1月1日早4:00～1月2日早4:00暂停账户贵金属、账户贵金属指数、账户能源、账户基本金属、账户农产品、账户外汇和外汇买卖业务。
        isnowres(new Date("2021/01/01 04:00:00"),new Date("2021/01/02 04:00:00"))||
        //二、美国马丁路德金纪念日
        //2021年1月18日早9:30～1月19日早2:00暂停账户农产品业务。
　　    //2021年1月19日早2:00～1月19日早4:00暂停账户能源业务。
        //isnowres(new Date("2021/01/18 09:30:00"),new Date("2021/01/19 04:00:00"))||
　　　　//三、美国总统日
        //2021年2月15日早9:30～2月16日早2:00暂停账户农产品业务。
　　    //2021年2月16日早2:00～2月16日早4:00暂停账户能源业务。
        //isnowres(new Date("2021/02/15 09:30:00"),new Date("2021/02/16 04:00:00"))||
　　　　//四、复活节
        //2021年4月2日早4:00～4月3日早4:00暂停账户贵金属、账户贵金属指数、账户能源、账户基本金属、账户农产品业务。
        //isnowres(new Date("2021/04/02 04:00:00"),new Date("2021/04/03 04:00:00"))||
　　　　//五、美国阵亡将士纪念日
        //2021年5月31日早9:30～6月1日早2:00暂停账户农产品业务。
　　    //2021年6月1日早1:00～6月1日早4:00暂停账户能源、账户基本金属业务。
        //isnowres(new Date("2021/05/31 09:30:00"),new Date("2021/06/01 04:00:00"))||
　　　　//六、美国独立日
        //2021年7月5日早9:30～7月6日晚20:30暂停账户农产品业务。
　　    //2021年7月6日早1:00～7月6日早4:00暂停账户能源、账户基本金属业务。
        //isnowres(new Date("2021/07/05 09:30:00"),new Date("2021/07/06 04:00:00"))||
　　　　//七、美国劳动节
        //2021年9月6日早9:30～9月7日早2:00暂停账户农产品业务。
　　    //2021年9月7日早1:00～9月7日早4:00暂停账户能源、账户基本金属业务。
        //isnowres(new Date("2021/09/06 09:30:00"),new Date("2021/09/07 04:00:00"))||
　　　　//八、美国感恩节
        //2021年11月25日早9:30～11月26日晚20:30暂停账户农产品业务。
　　    //2021年11月26日早2:00～11月26日早4:00暂停账户能源业务。
　　    //2021年11月27日早2:45～11月27日早4:00暂停账户能源业务。
　　    //九、圣诞节
        //2021年12月24日早0:00～12月25日早4:00暂停账户贵金属、账户贵金属指数、账户能源、账户基本金属、账户农产品业务。
　　    //2021年12月25日早0:00～12月25日早4:00暂停账户外汇和外汇买卖业务。
        isnowres(new Date("2021/12/25 00:00:00"),new Date("2021/12/25 04:00:00"))
    )*/
    tempisrestdate = true;
    if (dnday >= 2 && dnday <= 5) {//周二到周五
        tempisrestdate = false;
    } else if (dnday == 1) {//周一
        //采集时间会比开市时间早，因为银行外汇数据发布的时间比开市的时间稍早，本来7点（但是汇率发布时间一般
        // 迟于7点，所以警报时间设置8点）开市，但是通常银行6点就开始发布汇率数据了
        if ((hourint >= 6 && hourint <= 24 && gettype==1) || (hourint >= 8 && hourint <= 24 && gettype==2)) {
            tempisrestdate = false;
        }
    } else {
        if (dnday == 6) {//周六
            //采集时间会比休市时间晚，因为银行外汇数据发布的时间比休市的时间稍晚，本来4点休市，但是通常银行4点还在发布汇率数据
            if ((hourint >= 0 && hourint <= 4 && gettype==1) || (hourint >= 0 && hourint <= 3 && gettype==2)) {
                tempisrestdate = false;
            }
        }
    }
    return tempisrestdate;
}