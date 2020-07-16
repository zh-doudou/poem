<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>唐诗-宋词系统</title>
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

            //加载所有数据
            $.get("${pageContext.request.contextPath}/elastic/findAllKeywords", function (result) {
                createIndex(result, "#div");
            }, "JSON");

            //搜索查询
            $("#searchBtn").click(function () {
                let content = $("#searchText").val();
                $.get("${pageContext.request.contextPath}/elastic/searchquery", {
                    index: "tpoem", //索引名称
                    searchFieldValue: content,
                }, function (result) {
                    console.log("搜索查询结果：" + result)
                    conditionquery(result, "#div");
                }, "JSON");
            });


            //a listener event
            $("#div").on("mouseover", "a", (e) => {
                console.log($(e.currentTarget).offset().top);
                console.log($(e.currentTarget).offset().left);
                let id = $(e.currentTarget).attr("poemid");
                let content = $("#content" + id).html();
                let authordes = $("#authordes" + id).html();
                let div = $("<div  />").attr("id", "contenttext").css({
                    "background": "#eee",
                    "border": "1px red solid",
                    "position": "absolute",
                    "width": "400",
                    "left": $(e.currentTarget).position().left + 30,
                    "top": $(e.currentTarget).position().top + 30,
                    "border-radius": "5px",
                    "display": "none",
                    "z-index": "9999",
                });
                let htmls = `<div class="panel panel-default">
                              <div class="panel-heading">
                                <h3 class="panel-title">正文:</h3>
                              </div>
                              <div class="panel-body">` + content + `</div>
                            </div>
                            <div class="panel panel-default">
                              <div class="panel-heading">
                                <h3 class="panel-title">作者简介:</h3>
                              </div>
                              <div class="panel-body">` + authordes + `</div>
                            </div>`;

                div.html(htmls);
                $(e.currentTarget).parent().parent().parent().parent().append(div);
                $("#contenttext").slideDown(3000);
            }).on("mouseout", "a", () => {
                $("#contenttext").remove();
            });


            //点击类别筛选
            $(".breadcrumb").on("click", "li", (e) => {
                $(e.currentTarget).siblings().removeClass("actives");
                $(e.currentTarget).addClass("actives");
                let typeval = $(".actives").first().text();
                let authorval = $(".actives").last().text();
                let content = $("#searchText").val();
                if ("所有" == typeval && "所有" == authorval) {
                    alert("都为所有")
                    $.get("${pageContext.request.contextPath}/elastic/findAllByCondition", {
                        index: "tpoem", //索引名称
                        searchFieldValue: content, //搜索查询的值
                        type: "type",     //查询的类型字段
                        typevalue: typeval, //类型字段的值
                        author: "author",  //查询的字段：作者
                        authorvalue: authorval,  //查询的字段：作者

                    }, function (result) {
                        console.log("条件查询结果为：" + result);
                        createIndex(result, "#div");
                    }, "JSON");
                } else {
                    alert("非所有查询")
                    $.get("${pageContext.request.contextPath}/elastic/findAllByCondition", {
                        index: "tpoem", //索引名称
                        type: "type",     //查询的类型字段
                        typevalue: typeval, //类型字段的值
                        author: "author",  //查询的字段：作者
                        authorvalue: authorval,  //查询的字段：作者

                    }, function (result) {
                        console.log("条件查询结果为：" + result);
                        conditionquery(result, "#div");
                    }, "JSON");
                }
            });

        });

        //------------------封装创建主页函数
        function createIndex(result, list) {
            console.log(result)
            $(list).empty();
            //1.类别集合去重
            var categoryhash = [];

            //2.作者集合去重
            var autorhash = [];

            $.each(result.content, function (index, poem) {
                let div = $("<div />").css({
                    "border": "1px black solid",
                    "float": "left",
                    "width": "230",
                    "height": "260",
                    "margin": "10px 50px 15px 0px",
                    "border-radius": "10px",
                    "position": "relative"
                });

                console.log(poem.type);
                //1.1类别去重 并重新添加集合
                if (categoryhash.indexOf(poem.type) == -1) {
                    categoryhash.push(poem.type);
                }

                //2.1作者去重 并重新添加集合
                if (autorhash.indexOf(poem.author) == -1) {
                    autorhash.push(poem.author);
                }

                let ul = $("<ul/>");
                let name = $("<li/>").html("<h4><a href='javascript:;' poemid=" + poem.id + ">" + poem.name + "</h4>");
                let author = $("<li/>").html("<h4>" + poem.author + "·" + poem.type + "</h4>");
                let content = $("<li/>").html(poem.content).css("display", "none").attr("id", "content" + poem.id);
                let authordes = $("<li/>").html(poem.authordes).css("display", "none").attr("id", "authordes" + poem.id);
                let imgpath = $("<img/>").attr("src", poem.imagepath).css("margin-top", "15px");
                ul.append(imgpath).append(name).append(author).append(content).append(authordes);
                div.append(ul);
                $(list).append(div);
            });

            console.log("去重后的分类集合为：" + categoryhash)
            //1.2类别
            var categoryui = $("#categoryui");
            categoryui.empty()
            categoryui.append(" <li class=\"actives\">所有</li>")
            $.each(categoryhash, function (index, type) {
                var type = $("<li></li>").text(type);
                categoryui.append(type)

            })

            console.log("去重后的作者集合为：" + autorhash)
            //2.2作者栏添加
            var authorui = $("#authorui");
            authorui.empty();
            authorui.append($("<li class=\"actives\" >所有</li>"))
            $.each(autorhash, function (index, author) {
                var jQuery = $("<li></li>").text(author);
                authorui.append(jQuery)


            })
        }

        //-------------其他条件筛选封装
        function conditionquery(result, list) {
            $(list).empty();
            console.log(result)
            let div = $("<div />").css({
                "border": "1px black solid",
                "float": "left",
                "width": "230",
                "height": "260",
                "margin": "10px 50px 15px 0px",
                "border-radius": "10px",
                "position": "relative"
            });
            //1.类别集合去重
            var categoryhash = [];
            //2.作者集合去重
            var autorhash = [];
            $.each(result, function (index, poem) {
                let div = $("<div />").css({
                    "border": "1px black solid",
                    "float": "left",
                    "width": "230",
                    "height": "260",
                    "margin": "10px 50px 15px 0px",
                    "border-radius": "10px",
                    "position": "relative"
                });

                //1.1类别去重 并重新添加集合
                if (categoryhash.indexOf(poem.type) == -1) {
                    categoryhash.push(poem.type);
                }

                //2.1作者去重 并重新添加集合
                if (autorhash.indexOf(poem.author) == -1) {
                    autorhash.push(poem.author);
                }

                let ul = $("<ul/>");
                let name = $("<li/>").html("<h4><a href='javascript:;' poemid=" + poem.id + ">" + poem.name + "</h4>");
                let author = $("<li/>").html("<h4>" + poem.author + "·" + poem.type + "</h4>");
                let content = $("<li/>").html(poem.content).css("display", "none").attr("id", "content" + poem.id);
                let authordes = $("<li/>").html(poem.authordes).css("display", "none").attr("id", "authordes" + poem.id);
                let imgpath = $("<img/>").attr("src", poem.imagepath).css("margin-top", "15px");
                ul.append(imgpath).append(name).append(author).append(content).append(authordes);
                div.append(ul);
                $(list).append(div);
            });

            console.log("去重后的分类集合为：" + categoryhash)
            //1.2类别
            var categoryui = $("#categoryui");
            categoryui.empty()
            categoryui.append(" <li class=\"actives\">所有</li>")
            $.each(categoryhash, function (index, type) {
                var type = $("<li></li>").html(type);
                categoryui.append(type)

            });
            //2.2作者栏添加
            var authorui = $("#authorui");
            authorui.empty();
            authorui.append($("<li >所有</li>"))
            $.each(autorhash, function (index, author) {
                var jQuery = $("<li></li>").html(author);
                authorui.append(jQuery)
            });


        }
    </script>
    <style>
        .breadcrumb > li + li:before {
            content: "|";
        }

        .actives {
            color: #f50a0a;
        }

        em {
            color: #c12e2a;
        }

    </style>
</head>
<body>
<div class="container-fluid">

    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <h1 class="text-center">唐诗-宋词检索系统</h1>
        </div>
    </div>
    <br><br>

    <div class="row">
        <form class="form-horizontal">
            <div class="form-group">
                <label for="searchText" class="col-md-2 control-label">检索唐诗宋词</label>
                <div class="col-sm-8">
                    <input type="text" class="form-control" id="searchText" placeholder="输入检索条件....">
                </div>
                <div class="col-sm-2">
                    <button type="button" id="searchBtn" class="btn btn-default">检索</button>
                </div>
            </div>
        </form>
    </div>
    <div class="row">

        <div class="col-sm-8 col-sm-offset-2" style="margin-bottom: 10px;padding-left: 0px;">
            <ul class="breadcrumb" style="padding: 25px;" id="categoryui">

            </ul>
        </div>

        <div class="col-sm-8 col-sm-offset-2" style="padding-left: 0px;">
            <ul class="breadcrumb" style="padding: 25px;" id="authorui">
            </ul>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-8 col-sm-offset-2">
            <div id="div">

            </div>

        </div>
    </div>
    <%--<div class="row">
        <div class="col-sm-8 col-sm-offset-2">
            <iframe src="http://192.168.32.157:5601/app/kibana#/visualize/edit/40a232f0-0ff4-11ea-9893-17bbabd0f21d?embed=true&_g=()"
                    width="100%" height="800px;"></iframe>
        </div>
    </div>--%>
</div>
</body>
</html>