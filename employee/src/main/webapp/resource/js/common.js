// 统一ajax的错误处理
// 这里使用了jQuery的$，必须在这之前添加引用，后续添加的文件中虽然引用了jQuery但此时还不能使用
// off是先移除绑定事件（如果有），然后再绑定，防止引用的jsp文件中添加了该脚本重复绑定。也可$(document).ajaxError()
$(document).off('ajaxError').on('ajaxError', function (event, xhr) {
    if (xhr.status >= 5000) {
        alert(xhr.responseText);
    } else {
        // 当前页显示参数中的html代码
        document.write(xhr.responseText);
    }
});
