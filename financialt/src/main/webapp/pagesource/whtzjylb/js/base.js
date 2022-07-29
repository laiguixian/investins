/**
 * 基础js
 */
loadtable();//加载表格

function loadtable(){
    layui.use(['jquery','table'], function () {
        var $ = layui.$, //重点处
        table = layui.table;

        //第一个实例
        table.render({
            elem: '#demo'
            , url: '/getwhjyjllayui' //数据接口
            , height: 500 //容器高度
            , response: {
                statusName: 'resultcode' //规定数据状态的字段名称，默认：code
                , statusCode: 101 //规定成功的状态码，默认：0
                , msgName: 'resultmsg' //规定状态信息的字段名称，默认：msg
                , countName: 'totalcount' //规定数据总数的字段名称，默认：count
                , dataName: 'rows' //规定数据列表的字段名称，默认：data
            }
            , method: 'post' //如果无需自定义HTTP类型，可不加该参数
            , page: true //开启分页
            , totalRow: true //开启合计行
            , toolbar: '#tabletoolbar'
            , limit: 80 //每页80行
            , cols: [[ //表头
                //{type: 'checkbox', fixed: 'left'}
                {type: 'radio', fixed: 'left'}
                ,{field: 'id', title: '主键', width: 180, hide: true}
                , {field: 'jiaoyisj', title: '交易时间', width: 170, sort: true, fixed: 'left', totalRowText: '合计：'}
                , {field: 'jybzname', title: '交易币种', width: 130, sort: true}
                , {field: 'jyjine', title: '交易金额', width: 100, totalRow: true}
                , {field: 'cjjiage', title: '成交价格', width: 115, sort: true}
                , {field: 'cjjine', title: '成交金额', width: 100, totalRow: true}
                , {field: 'jslirun', title: '结算利润', width: 150}
                , {field: 'jyleixingname', title: '交易类型', width: 115, sort: true}
                , {field: 'xjzhyue', title: '现金', width: 100}
                , {field: 'whbzjyue', title: '账户外汇保证金', width: 140}
                , {field: 'zhgjsbzjyue', title: '账户贵金属保证金', width: 150}
                , {field: 'xmrhmccb', title: '先买入后卖出成本', width: 150}
                , {field: 'remark', title: '备注', width: 130}
            ]],
            done: function (res, curr, count) {
                /*!//如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                console.log(res);

                //得到当前页码
                console.log(curr);

                //得到数据总量
                console.log(count);*/
                /*for (var i = 0; i < res.rows.length; i++) {
                    if (res.rows[i].dangerstep == "情况危急") {
                        if(canala) {
                            audio.play();//播放
                        }
                    }
                }
                //console.log($("body").text());
                setTimeout(function () {
                    if(canres) {
                        loadtable();  //重新加载表格
                    }
                }, 20000);*/
            }
        });

        //监听工具栏事件
        table.on('toolbar(test)', function (obj) {
            //var checkStatus = table.checkStatus(obj.config.id);
            switch (obj.event) {
                case 'add':
                    //layer.msg('添加');
                    layer.open({
                        type: 2,
                        area: ['800px', '500px'],
                        cancel: function (index, layero) {
                            //if(confirm('确定要关闭么')){ //只有当点击confirm框的确定时，该层才会关闭
                            //    layer.close(index)
                            //}
                            //table.reload();
                            //layer.close(index);
                            location.reload();  //页面重新加载
                            return false;
                        },
                        content: ['/whtzjy', 'no'] //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
                    });
                    break;
                case 'delete':
                    //layer.msg('删除');
                    if(confirm('确定要删除该条记录吗？')) { //只有当点击confirm框的确定时，该层才会关闭
                        var checkStatus = table.checkStatus(obj.config.id); //获取选中行状态
                        var data = checkStatus.data;  //获取选中行数据
                        //layer.alert(JSON.stringify(data));
                        if (data.length > 0) {
                            //layer.alert(data[0].id);
                            $.ajax({
                                type: "POST",
                                url: '/deletewhjyjl?whjybid=' + data[0].id,
                                /*dataType:'json',*/
                                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                                data: data.field,
                                beforeSend: function () {
                                    //showLoading();
                                },
                                error: function (request) {
                                    //closeLoading();
                                    promptBox('提示', '无法连接到服务器！', '2');
                                },
                                success: function (data) {
                                    //closeLoading();
                                    //doingAjax = false;
                                    //显示返回信息
                                    layer.alert(data.resultmsg, {
                                        title: '删除信息'
                                    }, function () {
                                        location.reload();  //页面重新加载
                                    });
                                }
                            });
                        } else {
                            layer.alert("请选择要删除的记录");
                        }
                    }
                    break;
                case 'update':
                    layer.msg('编辑');
                    break;
                case 'count':
                    //layer.msg('删除');
                    if(confirm('确定要进行清算吗？这将花费较长的时间。')) { //只有当点击confirm框的确定时，该层才会关闭
                        $.ajax({
                            type: "POST",
                            url: '/accountcount',
                            /*dataType:'json',*/
                            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                            //data: data.field,
                            beforeSend: function () {
                                //showLoading();
                            },
                            error: function (request) {
                                //closeLoading();
                                promptBox('提示', '无法连接到服务器！', '2');
                            },
                            success: function (data) {
                                //closeLoading();
                                //doingAjax = false;
                                //显示返回信息
                                layer.alert(JSON.stringify(data.resultmsg), {
                                    title: '清算信息'
                                }, function () {
                                    location.reload();  //页面重新加载
                                });
                            }
                        });
                    }
                    break;
            }
        });
    });
}