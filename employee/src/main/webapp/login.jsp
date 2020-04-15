<%--
  Created by IntelliJ IDEA.
  User: Xuxm
  Date: 20/2/22
  Time: 下午5:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
  <meta charset="UTF-8">
  <title>员工管理系统</title>
  <style type="text/css">
    body {
      background: yellowgreen url("${ctx}/resource/images/nature_meteor.jpg");
      background-size: cover;
    }

    #login_box {
      background-color: rgba(255, 255, 0, 0.5); /*opacity影响子元素*/
      float: right;
      margin-top: 100px;
      margin-right: 150px;
      width: 400px;
    }

    .form_group {
      margin: 30px 20px 20px 20px;
    }
  </style>
</head>
<body>
<div id="container">
  <div id="show_box"></div>
  <div id="login_box">
    <form method="post">
      <div class="form_group">
        <label class="lbl">用户名：</label>
        <input class="txt" id="user_name" value="" name="user_name" type="text">
      </div>
      <div class="form_group">
        <label class="lbl">用户名：</label>
        <input class="pwd" id="password" value="" name="password" type="password">
      </div>
      <div class="form_group">
        <label class="lbl">验证码：</label>
        <input class="txt" id="aptcha" name="aptcha" type="text" value="" title="输入验证码">
        <img class="img" id="captcha_img" alt="点击更换" title="点击更换" src="images/captcha.jpeg">
      </div>
      <div class="form_group">
        <label><input id="remember" type="checkbox" value="true">&nbsp;记住登陆账号</label>
        <button>提交</button>
      </div>
    </form>
  </div>
</div>
</body>
</html>
