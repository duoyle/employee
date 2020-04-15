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
  <title>部门管理</title>
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
      <%@ include file="/common/search.jsp"%>
    </div>
  </div>

  <div id="table-container">
    <table class="table table-bordered" id="table-data">
      <thead>
      <tr>
        <th class="sel-<col" width="80"><input type="checkbox" title="选择">&nbsp;&nbsp;全选</th>
        <th class="row-key" id="deptNo">部门编号</th>
        <th id="deptName">部门名称</th>
        <th class="operation" width="100">操作</th>
      </tr>
      </thead>
      <tbody>
      <tr>
        <td><input type="checkbox" title="选择"></td>
        <td class="row-key"></td>
        <td></td>
        <td class="operation"><a name="update">修改</a>&nbsp;&nbsp;<a name="remove">删除</a></td>
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
    // 这种方式经测试后发现解决了select改变后的取不到新值问题，该事件去能取到新值
    window.addEventListener('pageshow', searchClick);
    $('#btn-insert').click(insertClick);
    initPagination({
        listUrl: '${ctx}/dept/list.do',
        action: {
            update: update,
            remove: remove
        }
    });

    function update(rowKey) {
        window.location.href = '${ctx}/dept/update?deptNo=' + rowKey;
    }

    function remove(rowkey) {
        if (confirm('确定要删除吗？')) {
            $.ajax({
                url: '${ctx}/dept/delete.do',
                type: 'post',
                dataType: 'json',
                data: {'deptNo': rowkey},
                success: function (message) {
                    searchClick();
                    alert(message);
                }
            });
        }
    }

    function insertClick() {
        window.location.href = '${ctx}/dept/insert';
    }
</script>
</html>
