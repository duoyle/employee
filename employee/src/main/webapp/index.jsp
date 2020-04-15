<%--
  Created by IntelliJ IDEA.
  User: Xuxm
  Date: 20/3/8
  Time: 下午8:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>
<html>
<head>
  <meta charset="utf-8">
  <title>欢迎</title>
</head>
<body>
<%--这种方式不走Servlet，Servlet暂时只服务虚拟路径/jsp下的，除非ForwardFilter接受forward类型的请求--%>
<jsp:forward page="/jsp/main"/>
</body>
</html>
