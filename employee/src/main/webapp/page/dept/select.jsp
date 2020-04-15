<%--
  Created by IntelliJ IDEA.
  User: Xuxm
  Date: 20/3/7
  Time: 上午10:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<link type="text/css" rel="stylesheet" href="${ctx}/resource/css/content.css">
<script type="text/javascript" src="${ctx}/resource/js/select.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/pagination.js"></script>
<style type="text/css">
  /* row-key的显示 */
  .table tr > .row-key {
    display: block;
  }
</style>

<div id="operate-container">
  <div id="search-container">
    <%@ include file="/common/search.jsp"%>
  </div>
</div>
<div id="table-container">
  <table class="table table-bordered" id="table-data">
    <thead>
    <tr class="th-color">
      <th class="sel-col" width="80">选择</th>
      <th class="row-key" id="deptNo">部门编号</th>
      <th id="deptName">部门名称</th>
    </tr>
    </thead>
    <tbody>
    <tr>
      <td class="sel-col"><input type="radio" title="选择" name="choose"></td>
      <td class="row-key"></td>
      <td></td>
    </tr>
    </tbody>
  </table>
</div>
<%@ include file="/common/simple_pagination.jsp"%>
<%--每次打开弹出框时加载该页面，下面代码和顶部链接的外部js都会执行一次。但下面的initPagination没出现多次事件绑定问题--%>
<script type="text/javascript" src="${ctx}/resource/js/common.js"></script>
<script type="text/javascript">
    initSelect(); // 选择操作的初始化，包括绑定事件
    initPagination({
        listUrl: '${ctx}/dept/list.do',
        pageSize: 10
    });
    setWidth(880); // 设置宽度
    setTitle('选择所属部门');

    searchClick(); // 加载数据
</script>