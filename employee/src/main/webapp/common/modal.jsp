<%--
  Created by IntelliJ IDEA.
  User: Xuxm
  Date: 20/4/2
  Time: 下午12:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<style type="text/css">
  .modal-body {
    margin-top: -22px;
    margin-bottom: 42px;
  }
</style>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
          &times;
        </button>
        <h4 class="modal-title" id="myModalLabel"></h4>
      </div>
      <div class="modal-body">

      </div>
      <div class="modal-footer">
        <%--<button type="button" class="btn btn-default float-right" data-dismiss="modal">取消</button>--%>
        <button type="button" class="btn btn-primary" data-dismiss="modal" id="okBtn">确定</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal -->
</div>

<script type="text/javascript">
    // 被静态包含之后，该部分脚本只执行一次
    let myModal = $('#myModal'); // 父窗口中可能还用到
    myModal.on("hidden.bs.modal", function () {
        $(this).removeData("bs.modal");
    });

    // 由内容设置宽度
    function setWidth(width) {
        $('.modal-dialog').width(width);
    }

    // 由内容设置宽度
    function setTitle(title) {
        $('#myModalLabel').text(title);
    }

    // 这里的okBtn如果绑定click事件放在load的页面中，则会多次绑定，执行多次方法
    $('#okBtn').click(function (element) {
        let returnValue = getReturnValue(); // 调用加载数据页中的返回值
        if ($.isEmptyObject(returnValue)) {
            alert('你还未选择任何数据');
            element.stopPropagation();
            return;
        }
        returnProcess(returnValue); // 调用父类的对返回值的处理方法
    });
</script>