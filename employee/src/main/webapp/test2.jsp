<%--
  Created by IntelliJ IDEA.
  User: Xuxm
  Date: 20/3/7
  Time: 上午10:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
  <title>新增部门</title>
  <link type="text/css" rel="stylesheet" href="${ctx}/resource/css/bootstrap.min.css">
  <link type="text/css" rel="stylesheet" href="${ctx}/resource/css/form.css">
  <script type="text/javascript" src="${ctx}/resource/js/jquery.min.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/jquery.validate.min.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/messages_zh.min.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/form.js"></script>
  <script type="text/javascript">
      $(function () {
          $('#showDept').click(function (e) {
              myModal.find('.modal-body').load('${ctx}/page/dept/select');
              myModal.modal('show');
          });
      });

      function returnValueProcess(result) {
          $('#deptNo').val(result.deptNo); // 需要隐藏，display:none不占空间，visibility:hidden占空间
          $('#deptName').val(result.deptName);
      }
  </script>
</head>
<body>
<div>
  <div class="container">
    <h2 class="text-center text-info">添加部门</h2>
    <br>
    <form role="form" id="addForm" class="form-horizontal" method="post">
      <div class="form-group">
        <%--似乎总共12列，md是middle，sm是small，lg是large--%>
        <label class="col-md-4 control-label" for="deptNo">部门编号</label>
        <div class="col-md-4">
          <input class="form-control" type="text" name="deptNo" id="deptNo" placeholder="输入部门编号"/>
        </div>
      </div>
      <div class="form-group">
        <label class="col-md-4 control-label" for="deptName">部门名称</label>
        <div class="col-md-4">
          <input class="form-control" type="text" name="deptName" id="deptName" placeholder="输入部门名称"/>
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

      <button class="btn btn-primary btn-lg" data-toggle="modal" type="button" id="showDept">
        开始演示模态框
      </button>
      <!-- 模态框（Modal） -->
      <%@include file="/common/modal.jsp" %>
    </form>
  </div>
</div>
</body>
</html>
