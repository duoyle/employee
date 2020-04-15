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
  <title>员工管理</title>
  <link type="text/css" rel="stylesheet" href="${ctx}/resource/css/bootstrap.min.css">
  <link type="text/css" rel="stylesheet" href="${ctx}/resource/css/content.css">
  <script type="text/javascript" src="${ctx}/resource/js/jquery.min.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="${ctx}/resource/js/pagination.js"></script>
  <style type="text/css">
    /* row-key的显示 */
    .table tr > .row-key {
      display: block;
    }

    .caret {
      margin-top: -3px; /* 箭头向上移动3px */
    }

    .dropdown-menu {
      min-width: 50px;
    }
  </style>

</head>
<body>
<div id="content">
  <div id="operate-container">
    <div id="insert-container">
      <button type="button" id="btn-insert">
        <span class="button-span"></span> 添加
      </button>
    </div>
    <div id="search-container">
      <%@ include file="/common/search.jsp" %>
    </div>
  </div>

  <div id="table-container">
    <table class="table table-bordered" id="table-data">
      <thead>
      <tr>
        <th class="sel-<col" width="80"><input type="checkbox" title="选择">&nbsp;&nbsp;全选</th>
        <th class="row-key" id="empNo">员工编号</th>
        <th id="empName">员工姓名</th>
        <th id="dept.deptName">部门名称</th>
        <th id="empSexShow">性别</th>
        <th id="empPhone">电话</th>
        <th id="empAddr">地址</th>
        <th id="entryDate">入职日期</th>
        <th id="salary">月薪</th>
        <th id="remark">备注</th>
        <th class="operation" width="100">操作</th>
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
        <td class="operation">
          <div class="dropdown">
            <a data-toggle="dropdown" href="javascript:">操作<b class="caret"></b></a>
            <ul class="dropdown-menu">
              <li><a name="update">修改</a></li>
              <li><a name="remove">删除</a></li>
              <li><a name="leave">离职</a></li>
            </ul>
          </div>
        </td>
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
    $('#btn-insert').click(insertClick);
    initPagination({
        listUrl: '${ctx}/employee/list.do',
        action: {
            update: update,
            remove: remove,
            leave: leave
        }
    });

    function update(rowKey) {
        window.location.href = '${ctx}/employee/update?empNo=' + rowKey;
    }

    function remove(rowkey) {
        if (confirm('确定要删除吗？')) {
            $.ajax({
                url: '${ctx}/employee/delete.do',
                type: 'post',
                dataType: 'json',
                data: {'empNo': rowkey},
                success: function (message) {
                    searchClick();
                    alert(message);
                }
            });
        }
    }

    function leave(rowkey) {
        if (confirm('确定要离职吗？')) {
            $.ajax({
                url: '${ctx}/employee/leave.do',
                type: 'post',
                dataType: 'json',
                data: {'empNo': rowkey},
                success: function (message) {
                    searchClick();
                    alert(message);
                }
            });
        }
    }

    function insertClick() {
        window.location.href = '${ctx}/employee/insert';
    }
</script>
</html>
