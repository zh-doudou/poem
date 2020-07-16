<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>唐诗-宋词后台管理系统</title>
    <link rel="stylesheet" href="boot/css/bootstrap.min.css">
    <link rel="stylesheet" href="jqgrid/css/trirand/ui.jqgrid-bootstrap.css">
    <%-- jquery核心.js --%>
    <script src="js/jquery-3.3.1.min.js"></script>
    <%-- boostrap核心.js --%>
    <script src="boot/js/bootstrap.min.js"></script>
    <%-- jqGrid核心.js --%>
    <script src="../jqgrid/js/trirand/jquery.jqGrid.min.js"></script>
    <%-- jqGrid国际化.js --%>
    <script src="../jqgrid/js/trirand/i18n/grid.locale-cn.js"></script>
    <script>
        $(function () {
            /*$.get("\\${pageContext.request.contextPath}/poem/findAll",function (result) {
                console.log(result);
                $.each(result,function (i,poem) {
                    console.log(poem);
                    let ul = $("<ul/>");
                    let name = $("<li/>").html("<a href=''>"+poem.name+"</a>");
                    let author = $("<li/>").text(poem.author+"·"+poem.type);
                    let content = $("<li/>").text(poem.content);
                    let authordes = $("<li/>").text(poem.authordes);
                    let categoryname= $("<li/>").text(poem.category);
                    ul.append(name).append(author).append(content).append(authordes).append(categoryname.name)
                    $("#list").append(ul);
                });
            },"JSON");*/
            $("#contentList").jqGrid({
                styleUI: "Bootstrap",
                caption: "唐诗宋词列表",
                autowidth: true,
                height: 380,
                url: "${pageContext.request.contextPath}/poem/findByPage",
                editurl: "${pageContext.request.contextPath}/poem/edit",
                datatype: "json",
                colNames: ["id", "诗词名", "作者", "类型", "来源", "内容", "作者简介"],
                colModel: [
                    {name: "id", editable: true,},
                    {name: "name", editable: true, width: 100},
                    {name: "author", editable: true, width: 40},
                    {name: "type", editable: true, width: 40},
                    {name: "origin", editable: true, width: 40},
                    {name: "content", editable: true, width: 400},
                    {name: "authordes", editable: true, width: 400},
                ],
                pager: "#pager",
                page: 1,
                rows: 10,
                rowList: [10, 15, 30, 40, 50, 70],
                viewrecords: true
            }).jqGrid("navGrid", "#pager", {edit: true, add: true, del: true, search: true, refresh: true});


            //初始化热词
            initHotRemoteDic();

            //添加热词
            $("#saveDic").click(function () {
                //获取热词
                let keyword = $.trim($("#remotekeyword").val());
                if (!keyword.length) {
                    alert("请输入热词!!!");
                    return false;
                }
                //添加热词
                $.post("${pageContext.request.contextPath}/dic/save", {keyword: keyword}, (result) => {
                    console.log(result);
                    if (result.success) initHotRemoteDic();//重新加载数据
                    if (!result.success) {
                        alert(result.message);
                    }
                });
            });


            //获取排行榜
            $.post("${pageContext.request.contextPath}/dic/findRedisKeywords", (result) => {
                $.each(result, (i, value) => {
                    console.log("热搜查询结果：" + result)
                    //console.log(value.score);
                    //console.log(value.value)
                    //console.log(parseFloat(value.score).toFixed(1));
                    let button = $("<button/>").addClass("btn btn-primary").css({"margin-right": "4px"}).html("&nbsp;" + value.value + "&nbsp;&nbsp;");
                    let span = $("<span/>").addClass("badge").text(parseFloat(value.score).toFixed(2));
                    if (value.score >= 0.5) {
                        span.css("color", "red");
                    }
                    button.append(span);
                    $("#rediskeywordlists").append(button);
                });
            }, "JSON");


            //清空所有文档
            $("#flushDocuments").click(() => {
                if (window.confirm("此操作不能恢复,确定清空所有文档?")) {
                    $("#loads").show();
                    $.post("${pageContext.request.contextPath}/elastic/deleteAll", (result) => {
                        if (result.success) {
                            $("#loads").hide();
                            $("#oks").show();
                            setTimeout(() => {
                                $("#oks").hide();
                            }, 1000);
                        }
                    });
                }
            });


            //基础数据重建索引
            $("#createIndex").click(() => {
                if (window.confirm("确定要重建索引吗?重建索引需要一段时间,请耐心等待!!!")) {
                    $("#loads1").show();
                    $.get("${pageContext.request.contextPath}/elastic/insert", (result) => {
                        if (result.success) {
                            $("#loads1").hide();
                            $("#oks1").show();
                            setTimeout(() => {
                                $("#oks1").hide();
                            }, 1000);
                        }
                    }, "JSON");
                }
            });


            //删除指定热词
            $("#keywordLists").on("click", ".close", (e) => {
                console.log(e.currentTarget);
                let keyword = $(e.currentTarget).attr("name");
                $.get("${pageContext.request.contextPath}/dic/delete", {keyword: keyword}, (result) => {
                    console.log(result);
                    if (result.success) {
                        initHotRemoteDic();
                    }
                }, "JSON");
            });

        });


        //封装初始化数据函数
        function initHotRemoteDic() {
            //获取所有热词
            $.get("${pageContext.request.contextPath}/dic/findAll", function (results) {
                $("#keywordLists").empty();//清空数据
                $.each(results, (i, keyword) => {
                    console.log("热搜：" + results)
                    let div = $("<div/>").css({"width": "120px", "float": "left", "margin-right": "10px"});
                    if (keyword.length <= 2) {
                        div.addClass("alert alert-success");
                    }
                    if (keyword.length >= 3) {
                        div.addClass("alert alert-info");
                    }
                    let button = $("<button/>").addClass("close").attr("name", keyword);
                    let span = $("<span/>").html("&times;");
                    button.append(span);
                    div.append(button).append(keyword);
                    $("#keywordLists").append(div);
                });
            }, "JSON");
        }
    </script>
</head>
<body>

<%--导航条--%>
<nav class="navbar navbar-default" style="margin-bottom: 5px">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <a class="navbar-brand" href="#">唐诗-宋词后台管理系统
                <small>V1.0</small>
            </a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <button type="button" class="btn btn-danger navbar-btn" id="flushDocuments">清空ES所有文档</button>
            <img src="${pageContext.request.contextPath}/boot/img/oks.ico" id="oks" style="width: 32px;display: none;">
            <img src="${pageContext.request.contextPath}/boot/img/load.gif" id="loads"
                 style="width: 32px;display: none;">
            &nbsp;&nbsp;&nbsp;
            <button type="button" class="btn btn-primary navbar-btn" id="createIndex">基于基础数据重建ES索引库</button>
            <img src="${pageContext.request.contextPath}/boot/img/oks.ico" id="oks1" style="width: 32px;display: none;">
            <img src="${pageContext.request.contextPath}/boot/img/load.gif" id="loads1"
                 style="width: 32px;display: none;">
        </div>
    </div>
</nav>

<%--中心内容栅格系统--%>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-12" style="padding-left: 0px;padding-right: 0px;">
            <table id="contentList"></table>
            <div id="pager"></div>
        </div>
    </div>
    <hr>
    <div class="row">

        <%--redis热词推荐榜--%>
        <div class="col-sm-6">
            <div class="panel panel-default">
                <div class="panel-heading">全网热搜榜:</div>
                <div class="panel-body" id="rediskeywordlists">


                </div>
            </div>
        </div>
        <%--更新远程热词--%>
        <div class="col-sm-6">
            <%--水平表单--%>
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="remotekeyword" placeholder="输入热词...">
                    </div>
                    <div class="col-sm-4">
                        <button class="btn btn-info" id="saveDic">添加远程词典</button>
                    </div>
                </div>
            </div>
            <%--当前系统扩展词--%>
            <div id="keywordLists">

            </div>
        </div>
    </div>
</div>

</body>
</html>