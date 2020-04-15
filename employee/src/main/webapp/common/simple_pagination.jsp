<%--
  Created by IntelliJ IDEA.
  User: Xuxm
  Date: 20/3/7
  Time: 下午12:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="page-container">
  <div id="page-size">共&nbsp;<a id="totalRows">0</a>&nbsp;条</div>
  <div id="page-jump">
    &nbsp;&nbsp;共&nbsp;<a id="totalPages">0</a>&nbsp;页&nbsp;&nbsp;
    到第&nbsp;<input type="number" id="pageNumber" title="输入页码"/>&nbsp;页
    <input type="button" id="jumpPage" value="确定">
  </div>
  <div id="page-number">
    <ul class="pagination">
      <li id="firstPage"><a href="javascript:">首页</a></li>
      <li id="previousPage"><a href="javascript:">上一页</a></li>
      <li><a>2</a></li>
      <li id="nextPage"><a href="javascript:">下一页</a></li>
      <li id="lastPage"><a href="javascript:">尾页</a></li>
    </ul>
  </div>
</div>
