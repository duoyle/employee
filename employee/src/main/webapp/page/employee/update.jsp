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
          $('#selBtn').click(selDept); // 选择部门绑定
          formProcess('#updForm', {
              empNo: {
                  required: true, // 从messages先找该字段对应的，若没有则找required对应的
                  maxlength: 10
              },
              empName: {
                  required: true,
                  maxlength: 20
              },
              'dept\.deptName': {
                  required: true
              },
              empSex: {
                  required: true
              },
              entryDate: {
                  isDate: true
              },
              empPhone: {
                  isPhone: true
              },
              salary: {
                  isNumber: true
              }
          }, '${ctx}/employee/update.do');
          loadData();
      });

      function selDept() {
          myModal.find('.modal-body').load('${ctx}/dept/select');
          myModal.modal('show'); // 或者{show: true}都可
      }

      function returnProcess(returnValue) {
          $('#dept\\.deptNo').val(returnValue.deptNo); // 需要隐藏，display:none不占空间，visibility:hidden占空间
          $('#dept\\.deptName').val(returnValue.deptName);
      }

      function loadData() {
          $.ajax({
              url: '${ctx}/employee/get.do',
              type: 'post',
              dataType: 'json',
              data: {'empNo': getParameter('empNo')},
              success: function (data) {
                  deserialize(data);
              }
          });
      }
  </script>
</head>
<body>
<div class="container">
  <h2 class="text-center text-info">添加员工</h2>
  <br>
  <form role="form" id="updForm" class="form-horizontal" method="post">
    <div class="form-group">
      <%--似乎总共12列，md是middle，sm是small，lg是large--%>
      <label class="col-md-4 control-label" for="empNo">员工编号</label>
      <div class="col-md-4">
        <input class="form-control" type="text" name="empNo" id="empNo" readonly/>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-4 control-label" for="empName">员工姓名</label>
      <div class="col-md-4">
        <input class="form-control" type="text" name="empName" id="empName" placeholder="输入员工名称"/>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-4 control-label" for="dept.deptName">所属部门</label>
      <div class="col-md-4">
        <div class="input-group">
          <input type="text" style="display: none" name="dept.deptNo" id="dept.deptNo" title=""/>
          <input class="form-control" type="text" name="dept.deptName" id="dept.deptName"
                 placeholder="选择所属部门" readonly/>
          <span class="input-group-btn"><button class="btn btn-default" type="button" id="selBtn">选择</button></span>
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-4 control-label">性别</label>
      <div class="col-md-4">
        <label class="radio-inline"><input type="radio" name="empSex" value="1"/>男</label>
        <label class="radio-inline"><input type="radio" name="empSex" value="2"/>女</label>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-4 control-label" for="entryDate">入职日期</label>
      <div class="col-md-4">
        <input class="form-control" type="date" name="entryDate" id="entryDate"/>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-4 control-label" for="empPhone">手机号</label>
      <div class="col-md-4">
        <input class="form-control" type="text" name="empPhone" id="empPhone" placeholder="输入手机号"/>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-4 control-label" for="salary">月薪</label>
      <div class="col-md-4">
        <input class="form-control" type="number" name="salary" id="salary" placeholder="输入月薪"/>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-4 control-label" for="empAddr">通讯地址</label>
      <div class="col-md-4">
        <input class="form-control" type="text" name="empAddr" id="empAddr" placeholder="输入地址"/>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-4 control-label">兴趣爱好</label>
      <div class="col-md-4">
        <div class="checkbox">
          <label><input type="checkbox" name="hobbies" value="1">运动</label>
          <label><input type="checkbox" name="hobbies" value="2">音乐</label>
          <label><input type="checkbox" name="hobbies" value="4">读书</label>
          <label><input type="checkbox" name="hobbies" value="8">写作</label>
          <label><input type="checkbox" name="hobbies" value="16">画画</label>
          <label><input type="checkbox" name="hobbies" value="32">养殖</label>
          <label><input type="checkbox" name="hobbies" value="64">旅行</label>
          <label><input type="checkbox" name="hobbies" value="128">喝茶</label>
          <label><input type="checkbox" name="hobbies" value="256">游戏</label>
          <label><input type="checkbox" name="hobbies" value="512">影视</label>
          <label><input type="checkbox" name="hobbies" value="1024">学习</label>
        </div>
      </div>
    </div>
    <div class="form-group">
      <label class="col-md-4 control-label" for="remark">备注</label>
      <div class="col-md-4">
        <%--还可使用文本区域（多行）框，使用样式resize:none可以禁止拖拽--%>
        <textarea class="form-control" name="remark" id="remark" rows="3"></textarea>
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
  <%--服务端自动在ContextPath下查找--%>
  <%@ include file="/common/modal.jsp" %>
</div>
</body>
<script type="text/javascript" src="${ctx}/resource/js/common.js"></script>
</html>
