<%--
  Created by IntelliJ IDEA.
  User: Xuxm
  Date: 20/3/27
  Time: 下午8:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
  <meta charset="utf-8">
  <title>三级导航</title>

  <link rel="stylesheet" type="text/css" href="${ctx}/resource/css/bootstrap.css">
  <link rel="stylesheet" type="text/css" href="${ctx}/resource/css/iconfont.css">
  <link rel="stylesheet" type="text/css" href="${ctx}/resource/css/main-frame.css">

  <script type="text/javascript" src="${ctx}/resource/js/jquery.min.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/bootstrap.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/jquery.slimscroll.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/main-frame.js"></script>
</head>
<body>
<div class="top-side-nav">
  <nav class="navbar navbar-inverse" role="navigation">
    <div class="container">
      <div class="navbar-header">
        <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="javascript:"><i>倍利尔</i>&nbsp;科技</a>
      </div>
      <div id="navbar" class="navbar-collapse collapse">
        <ul class="nav navbar-nav">
          <li><a href="javascript:">公司简介</a></li>
          <li><a href="javascript:">组织架构</a></li>
          <li class="active"><a href="javascript:">产品</a></li>
          <li><a href="javascript:" class="dropdown-toggle" data-toggle="dropdown">友情链接
            <span class="caret"></span></a>
            <ul class="dropdown-menu">
              <li class="dropdown-header">搜索</li>
              <li><a href="https://www.google.com" target="_blank">谷歌搜索</a></li>
              <li><a href="https://www.baidu.com" target="_blank">百度搜索</a></li>
              <li><a href="https://www.bing.cn" target="_blank">必应搜索</a></li>
              <li class="divider"></li>
              <li class="dropdown-header" target="_blank">购物</li>
              <li><a href="https://www.taobao.com" target="_blank">淘宝</a></li>
              <li><a href="https://www.jd.com" target="_blank">京东</a></li>
            </ul>
          </li>
        </ul>
        <%--放到该div外就成下一行了--%>
        <ul class="nav navbar-nav navbar-user">
          <li>
            <a href="javascript:" class="dropdown-toggle" data-toggle="dropdown">
              <i class="my-icon icon_huiyuanguanli"></i>&nbsp;&nbsp;没意思
              <b class="caret"></b>
            </a>
            <ul class="dropdown-menu">
              <li><a href="javascript:">账号设置</a></li>
              <li><a href="javascript:">修改密码</a></li>
              <li><a href="javascript:">退出</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</div>
<div>
  <div class="right-side-content">
    <iframe name="contentFrame" src="${ctx}/page/home.jsp" width="100%" height="100%" scrolling="auto"
            frameborder="0"></iframe>
  </div>
  <div class="left-side-menu">
    <div class="lsm-expand-btn">
      <div class="lsm-mini-btn">
        <label class="lsm-mini-btn-label">
          <input type="checkbox" checked="checked">
          <svg viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
            <circle cx="50" cy="50" r="30"></circle>
            <path class="line--1" d="M0 40h62c18 0 18-20-17 5L31 55"></path>
            <path class="line--2" d="M0 50h80"></path>
            <path class="line--3" d="M0 60h62c18 0 18 20-17-5L31 45"></path>
          </svg>
        </label>
      </div>
    </div>
    <%--二级菜单如果需要图标，在后面添加icon-对应的class即可--%>
    <div class="lsm-container">
      <div class="lsm-scroll">
        <div class="lsm-sidebar">
          <ul>
            <li id="F100" class="lsm-sidebar-item">
              <a href="javascript:"><i class="my-icon lsm-sidebar-icon icon-users"></i>
                <span>部门管理</span><i class="my-icon lsm-sidebar-more"></i></a>
              <ul>
                <li id="F110"><a href="${ctx}/page/dept/list" target="contentFrame"><span>部门列表</span></a></li>
                <li id="F120"><a href="${ctx}/page/dept/insert" target="contentFrame"><span>添加部门</span></a></li>
                <li id="F130"><a href="javascript:"><span>修改部门</span></a></li>
              </ul>
            </li>
            <li id="F200" class="lsm-sidebar-item">
              <a href="javascript:"><i class="my-icon lsm-sidebar-icon icon-user"></i>
                <span>员工管理</span><i class="my-icon lsm-sidebar-more"></i></a>
              <ul>
                <li id="F210"><a href="${ctx}/page/employee/list" target="contentFrame"><span>员工列表</span></a></li>
                <li id="F220"><a href="${ctx}/page/employee/insert" target="contentFrame"><span>添加员工</span></a></li>
                <li id="F230"><a href="javascript:"><span>修改信息</span></a></li>
              </ul>
            </li>
            <li id="F300" class="lsm-sidebar-item">
              <a href="javascript:"><i class="my-icon lsm-sidebar-icon icon-record"></i>
                <span>历史记录</span><i class="my-icon lsm-sidebar-more"></i></a>
              <ul>
                <li id="F310"><a href="${ctx}/page/history/list" target="contentFrame"><span>查看</span></a></li>
                <li id="F320"><a href="javascript:"><span>删除</span></a></li>
              </ul>
            </li>
            <li id="F400" class="lsm-sidebar-item">
              <a href="javascript:"><i class="my-icon lsm-sidebar-icon icon-system"></i>
                <span>系统管理</span><i class="my-icon lsm-sidebar-more"></i></a>
              <ul>
                <li id="F410"><a href="javascript:"><span>数据备份</span></a></li>
                <li id="F420"><a class="active" href="javascript:"><span>数据还原</span></a></li>
                <li id="F430"><a href="javascript:"><span>用户管理</span></a></li>
                <li id="F440" class="lsm-sidebar-item">
                  <a href="javascript:"><i class="my-icon lsm-sidebar-icon"></i>
                    <span>权限管理</span><i class="my-icon lsm-sidebar-more"></i></a>
                  <ul>
                    <li id="F441"><a href="javascript:"><span>角色管理</span></a></li>
                    <li id="F442"><a href="javascript:"><span>分配权限</span></a></li>
                    <li id="F443"><a href="javascript:"><span>分配角色</span></a></li>
                    <li id="F444"><a href="javascript:"><span>数据权限</span></a></li>
                  </ul>
                </li>
                <li id="F450" class="lsm-sidebar-item">
                  <a href="javascript:"><i class="my-icon lsm-sidebar-icon"></i>
                    <span>系统设置</span><i class="my-icon lsm-sidebar-more"></i></a>
                  <ul>
                    <li id="F451"><a href="javascript:"><span>主题设置</span></a></li>
                    <li id="F452"><a href="javascript:"><span>语言设置</span></a></li>
                    <li id="F453"><a href="javascript:"><span>高级设置</span></a></li>
                    <li id="F454"><a href="javascript:"><span>牛魔王</span></a></li>
                    <li id="F455"><a href="javascript:"><span>齐天大圣</span></a></li>
                    <li id="F456"><a href="javascript:"><span>白骨精</span></a></li>
                    <li id="F457"><a href="javascript:"><span>猪八戒</span></a></li>
                    <li id="F458"><a href="javascript:"><span>如来佛祖</span></a></li>
                    <li id="F459"><a href="javascript:"><span>观音菩萨</span></a></li>
                    <li id="F45A"><a href="javascript:"><span>玉皇大帝</span></a></li>
                    <li id="F45B"><a href="javascript:"><span>太上老君</span></a></li>
                    <li id="F45C"><a href="javascript:"><span>唐玄奘</span></a></li>
                    <li id="F45D"><a href="javascript:"><span>白龙马</span></a></li>
                    <li id="F45E"><a href="javascript:"><span>沙和尚</span></a></li>
                    <li id="F45F"><a href="javascript:"><span>西天取经</span></a></li>
                  </ul>
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
