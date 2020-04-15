<%--
  Created by IntelliJ IDEA.
  User: Xuxm
  Date: 20/3/7
  Time: 上午10:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
  <meta charset="UTF-8">
  <title>变更记录</title>
  <link type="text/css" rel="stylesheet" href="${ctx}/resource/css/bootstrap.min.css">
  <link type="text/css" rel="stylesheet" href="${ctx}/resource/css/content.css">
  <script type="text/javascript" src="${ctx}/resource/js/jquery.min.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/pagination.js"></script>
</head>
<body>
<div id="content">
  <div id="operate-container">
    <div id="insert-container">
    </div>
    <div id="search-container">
      <%@ include file="/common/search.jsp"%>
    </div>
  </div>

  <div id="table-container">
    <table class="table table-bordered" id="table-data">
      <thead>
      <tr>
        <th class="sel-<col" width="80"><input type="checkbox" title="选择">&nbsp;&nbsp;全选</th>
        <th class="row-key" id="changeNo">ID</th>
        <th id="empl.empNo">员工编号</th>
        <th id="empl.empName">员工姓名</th>
        <th id="empl.dept.deptName">部门名称</th>
        <th id="empl.empSexShow">性别</th>
        <th id="empl.empPhone">电话</th>
        <th id="empl.entryDate">入职日期</th>
        <th id="empl.leaveDate">入职日期</th>
        <th id="empl.stateShow">入职日期</th>
        <th id="salary">月薪</th>
        <th id="changeDate">变更日期</th>
        <th id="changeReason">变更原因</th>
      </tr>
      </thead>
      <tbody>
      <tr>
        <td><input type="checkbox" title="选择"></td>
        <td class="row-key"></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      </tbody>
    </table>
  </div>

  <%@ include file="/common/pagination.jsp" %>

  <%@ include file="/common/modal.jsp" %>
</div>
</body>
<script type="text/javascript" src="${ctx}/resource/js/common.js"></script>
<script type="text/javascript">
    $(function () { // 这里使用了jQuery的$，必须在这之前添加引用，后续添加的文件中虽然引用了jQuery但此时还不能使用

    });
    // 这种方式经测试后发现解决了select改变后的取不到新值问题，该事件去能取到新值
    window.addEventListener('pageshow', searchClick);
    initPagination({
        listUrl: '${ctx}/history/list.do'
    });
</script>
</html>
