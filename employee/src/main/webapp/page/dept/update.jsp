<%--
  Created by IntelliJ IDEA.
  User: Xuxm
  Date: 20/3/7
  Time: 下午12:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
  <title>修改部门</title>
  <link type="text/css" rel="stylesheet" href="${ctx}/resource/css/bootstrap.min.css">
  <link type="text/css" rel="stylesheet" href="${ctx}/resource/css/form.css">
  <script type="text/javascript" src="${ctx}/resource/js/jquery.min.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/jquery.validate.min.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/messages_zh.min.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/form.js"></script>
  <script type="text/javascript">
      $(function () {
          formProcess('#updForm', {
              deptNo: {
                  required: true, // 从messages先找该字段对应的，若没有则找required对应的
                  maxlength: 10
              },
              deptName: {
                  required: true,
                  maxlength: 20
              }
          }, '${ctx}/dept/update.do');
      });

  </script>
</head>
<body>
<div class="container">
  <h2 class="text-center text-info">修改部门</h2>
  <br>
  <form role="form" id="updForm" class="form-horizontal" method="post">
    <div class="form-group">
      <%--似乎总共12列，md是middle，sm是small，lg是large--%>
      <label class="col-md-4 control-label" for="deptNo">部门编号</label>
      <div class="col-md-4">
        <input class="form-control" type="text" name="deptNo" id="deptNo" value="${deptNo}" readonly/>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-4 control-label" for="deptName">部门名称</label>
      <div class="col-md-4">
        <input class="form-control" type="text" name="deptName" id="deptName" value="${deptName}"/>
      </div>
    </div>
    <div class="form-group">
      <%--offset是从左开始的偏移列数，col-md-4是占用4列宽度--%>
      <div class="col-md-4"></div>
      <label class="col-md-3 text-left success" id="resultTip"></label>
      <div class="col-md-1 text-right">
        <button type="submit" class="btn btn-primary">提交</button>
      </div>
    </div>
  </form>
</div>
</body>
<script type="text/javascript" src="${ctx}/resource/js/common.js"></script>
</html>
