<%--
  Created by IntelliJ IDEA.
  User: Xuxm
  Date: 20/3/7
  Time: 下午6:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<div class="operate-label">按照</div>
<div id="search-items">
  <select class="select-items" id="sel-column" title="选择查找的字段">
    <c:forEach items="${searchColumns}" var="column">
      <option value="${column.key}">${column.value}</option>
    </c:forEach>
  </select>
</div>
<div class="operate-label">关键字：</div>
<div class="search-input">
  <input type="text" class="input-text" id="keyword" placeholder="请输入包含的内容">
</div>
<div class="search-btn">
  <button type="button" id="btn-search">
    <span class="button-span"></span> 查询
  </button>
</div>

<script type="text/javascript">
    function searchClick() {
        let column = $('#sel-column option:selected').val();
        let keyword = $('#keyword').val();
        search(column, keyword);
    }
    $('#btn-search').click(searchClick); // 这种添加事件方式需等DOM解析之后，否则访问不到#btn-search元素，window可用
</script>