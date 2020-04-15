<%--
  Created by IntelliJ IDEA.
  User: Xuxm
  Date: 20/3/14
  Time: 上午10:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>错误信息</title>
  <style type="text/css">
    .outer-container {
      display: flex;
      align-items: center;
      width: 100%;
      height: 80%;
    }
    .inner-container {
      margin: auto;
    }
  </style>
</head>
<body>
<div class="outer-container">
  <div class="inner-container">
    <h3>错误状态：<b>${error.status}</b></h3>
    <h3>错误信息：${error.message}</h3>
  </div>
</div>
</body>
</html>
