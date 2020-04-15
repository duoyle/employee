<%--
  Created by IntelliJ IDEA.
  User: Xuxm
  Date: 20/3/27
  Time: 下午2:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
    <title>自定义页面</title>
    <link rel="stylesheet"  href="${ctx}/resource/css/bootstrap.min.css">
    <link rel="stylesheet"  href="${ctx}/resource/css/font-awesome.min.css">
    <script src="${ctx}/resource/js/jquery.min.js"></script>
    <script src="${ctx}/resource/js/bootstrap.min.js"></script>
    <style rel="stylesheet" type="text/css">
        a,
        a:focus,
        a:hover {
            text-decoration: none
        }

        .main_menu .fa {
            -webkit-font-smoothing: antialiased;
            width: 24px;
            opacity: .99;
            display: inline-block;
            font-family: FontAwesome;
            font-style: normal;
            font-weight: 400;
            font-size: 18px;
        }

        .main_menu span.fa {
            float: right;
            text-align: center;
            margin-top: 5px;
            font-size: 10px;
            min-width: inherit;
            color: #C4CFDA
        }

        .active a span.fa {
            text-align: right!important;
            margin-right: 4px
        }

        .nav.side-menu>li {
            position: relative;
            display: block;
            cursor: pointer
        }

        .nav.side-menu>li>a {
            margin-bottom: 6px
        }

        .nav.side-menu>li>a:hover {
            color: #F2F5F7!important
        }

        .nav.side-menu>li>a:hover,
        .nav>li>a:focus {
            text-decoration: none;
            background: 0 0
        }

        .nav.child_menu {
            display: none
        }

        .nav.child_menu li.active,
        .nav.child_menu li:hover {
            background-color: rgba(255, 255, 255, .06)
        }

        .nav.child_menu li {
            padding-left: 36px
        }

        .nav-md ul.nav.child_menu li:before {
            background: #425668;
            bottom: auto;
            content: "";
            height: 8px;
            left: 23px;
            margin-top: 15px;
            position: absolute;
            right: auto;
            width: 8px;
            z-index: 1;
            border-radius: 50%
        }

        .nav-md ul.nav.child_menu li:after {
            border-left: 1px solid #425668;
            bottom: 0;
            content: "";
            left: 27px;
            position: absolute;
            top: 0
        }

        .nav>li>a {
            position: relative;
            display: block
        }

        .nav.child_menu>li>a,
        .nav.side-menu>li>a {
            color: #E7E7E7;
            font-weight: 500
        }

        .nav li li.current-page a,
        .nav.child_menu li li a.active,
        .nav.child_menu li li a:hover {
            color: #fff
        }

        .nav.child_menu li li.active,
        .nav.child_menu li li:hover {
            background: 0 0
        }

        .nav>li>a {
            padding: 13px 15px 12px
        }

        .nav.side-menu>li.active,
        .nav.side-menu>li.current-page {
            border-right: 5px solid #1ABB9C
        }

        .nav li.current-page {
            background: rgba(255, 255, 255, .05)
        }

        .nav li li li.current-page {
            background: 0 0
        }

        .nav.side-menu>li.active>a {
            text-shadow: rgba(0, 0, 0, .25) 0 -1px 0;
            background: linear-gradient(#334556, #2C4257), #2A3F54;
            box-shadow: rgba(0, 0, 0, .25) 0 1px 0, inset rgba(255, 255, 255, .16) 0 1px 0
        }

        .nav>li>a:focus,
        .nav>li>a:hover {
            background-color: transparent
        }


        /* alone */
        *{
            padding: 0;margin: 0;
        }
        body,html{
            width: 100%;
            height: 100%;
        }
        .contentLeft {
            float: left;
            width: 15%;
            height: 100%;
            background:black;
            opacity: 0.7;
        }

        .contentRight {
            float: left;
            width: 85%;
        }
    </style>
</head>
<body>
<div class="contentLeft">
    <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
        <ul class="nav side-menu" id="navmenu"></ul>
    </div>
</div>
<div class="contentRight">
    <div class="right_col" role="main" id="rightContent"></div>
</div>
<script type="text/javascript">
    let result = {
        "data": [
        {
            "menuId": "01",
            "menuName": "基础信息管理",
            "menuCode": "RR1234",
            "menuAction": "#",
            "children": [
                {
                    "menuId": "001",
                    "menuName": "岗位管理",
                    "menuCode": "RR1234",
                    "menuAction": "http://localhost:8080/page/dept/list"
                }, {
                    "menuId": "001",
                    "menuName": "技能等级",
                    "menuCode": "RR1234",
                    "menuAction": "http://localhost:8080/page/dept/list"
                }
            ]
        }, {
            "menuId": "01",
            "menuName": "系统管理",
            "menuCode": "RR1234",
            "menuAction": "#",
            "children": [
                {
                    "menuId": "001",
                    "menuName": "机构管理",
                    "menuCode": "RR1234",
                    "menuAction": "http://localhost:8080/page/dept/list"
                }, {
                    "menuId": "001",
                    "menuName": "菜单管理",
                    "menuCode": "RR1234",
                    "menuAction": "http://localhost:8080/page/dept/list"
                }, {
                    "menuId": "001",
                    "menuName": "角色人员",
                    "menuCode": "RR1234",
                    "menuAction": "http://localhost:8080/page/dept/list"
                }, {
                    "menuId": "001",
                    "menuName": "人员管理",
                    "menuCode": "RR1234",
                    "menuAction": "http://localhost:8080/page/dept/list"
                }
            ]
        },{
            "menuId": "01",
            "menuName": "人员动态管理",
            "menuCode": "RR1234",
            "menuAction": "#",
            "children": [
                {
                    "menuId": "001",
                    "menuName": "人员异动汇总",
                    "menuCode": "RR1234",
                    "menuAction": "http://localhost:8080/page/dept/list"
                }, {
                    "menuId": "001",
                    "menuName": "人员技能",
                    "menuCode": "RR1234",
                    "menuAction": "#",
                    "children": [
                        {
                            "menuId": "001",
                            "menuName": "岗位信息",
                            "menuCode": "RR1234",
                            "menuAction": "http://localhost:8080/page/dept/list"
                        },{
                            "menuId": "001",
                            "menuName": "学历信息",
                            "menuCode": "RR1234",
                            "menuAction": "http://localhost:8080/page/dept/list"
                        },{
                            "menuId": "001",
                            "menuName": "身份信息",
                            "menuCode": "RR1234",
                            "menuAction": "http://localhost:8080/page/dept/list"
                        },{
                            "menuId": "001",
                            "menuName": "现场测评",
                            "menuCode": "RR1234",
                            "menuAction": "http://localhost:8080/page/dept/list"
                        }
                    ]
                }
            ]
        }
    ]
    };
    $(function() {
        // $.ajax({
        //     type: "get",
        //     url: 'json/data.json',
        //     dataType: "json",
        //     success: function(result) {
        //         var res = eval(result.data);
        //         showSideMenu(res);
        //     }
        // });

        (function (result) {
            let res = eval(result.data);
            showSideMenu(res);
        })(result);

        $("#navmenu").on("click", ".liname", function(index) {
            var index = $("#navmenu .liname").index(this);
            $("#navmenu>li>.child_menu").eq(index).slideToggle();
            $("#navmenu>li>.child_menu").eq(index).parent().siblings("li").find(".child_menu").slideUp();
        });


    });

    function showSideMenu(res) {
        for(var i = 0; i < res.length; i++) {
            var html = "";
            html += "<li>";
            html += "<a class='liname'><i class='fa fa-home'></i>" + res[i].menuName + "<span class='fa fa-chevron-down'></span></a>";
            html += "<ul class='nav child_menu'>";
            for(var k = 0; k < res[i].children.length; k++) {
                if(typeof(res[i].children[k].children) != 'undefined') {
                    html += "<li class='three_menu'>";
                    html += "<a href='" + res[i].children[k].menuAction + "'>" + res[i].children[k].menuName + "</a>";
                    html += "<ul class='nav child_menu'>";
                    for(var j = 0; j < res[i].children[k].children.length; j++) {
                        html += "<li>";
                        html += "<a href='" + res[i].children[k].children[j].menuAction + "' target='myFrame' >" + res[i].children[k].children[j].menuName + "</a>";
                        html += "</li>";
                    }
                    html += "</ul>";
                    html += "</li>";
                } else if(typeof(res[i].children[k].children) == 'undefined'){
                    html += "<li>";
                    html += "<a href='" + res[i].children[k].menuAction + "' target='myFrame'>" + res[i].children[k].menuName + "</a>";
                    html += "</li>";
                }
            }
            html += "</ul>";
            html += "</li>";
            $("#navmenu").append(html);
        };
        $("#navmenu .child_menu").eq(0).css({
            "display": "block"
        });
        $("#navmenu .child_menu").eq(0).find("li:eq(0)").addClass("current-page");

        var fram = "<iframe src='http://localhost:8080/page/dept/list' name='myFrame' id='myIframe' width='100%' height='100%'  scrolling='auto' frameborder='0'></iframe>"
        $("#rightContent").append(fram);

        $("#navmenu .child_menu").on("click", "li", function() {
            var index = $("#navmenu .child_menu li").index(this);
            $("#navmenu .child_menu li").eq(index).parent().parent().siblings("li").find(".child_menu").slideUp();
            $("#navmenu .child_menu li").eq(index).parent().parent().siblings("li").find(".child_menu").find("li").removeClass("current-page");
            $("#navmenu .child_menu li").eq(index).addClass("current-page")
            $("#navmenu .child_menu li").eq(index).siblings().removeClass("current-page");
        });


        $("#navmenu").on("click", ".three_menu", function() {
            var index = $("#navmenu .three_menu").index(this);
            $("#navmenu .three_menu .child_menu").eq(index).slideDown();
        });



        $("#navmenu .three_menu").on("click", "li", function() {
            var index = $("#navmenu .three_menu li").index(this);
            $("#navmenu .three_menu li a").eq(index).css({
                "color": "beige"
            });
            $("#navmenu .three_menu li a").eq(index).parent().siblings("li").find("a").css({
                "color": "white"
            });

        });


        var str = window.screen.availHeight - 170;
        $("#rightContent").css({
            "min-height": str
        });

        var bdHeight = document.documentElement.clientHeight;
        $("#rightContent").height(bdHeight - 65);

    }
</script>
</body>
</html>