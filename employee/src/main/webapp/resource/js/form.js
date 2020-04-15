// 添加验证方法
(function () {
    $.validator.addMethod('isPhone', function (value) {
        // this.optional(element)如果element的值为空返回true
        return /^1\d{10}$/.test(value);
    }, '请输入正确电话号码');
    $.validator.addMethod('isEmail', function (value, element) {
        return this.optional(element) || /^[a-zA-Z0-9_]+@[a-zA-Z0-9_]+(\.[a-zA-Z0-9_]+)+$/.test(value);
    }, '请输入正确EMail地址');
    $.validator.addMethod('isDate', function (value) {
        return /^\d{4}[-/]\d{1,2}[-/]\d{1,2}$/.test(value);
    }, '请输入正确日期');
    $.validator.addMethod('isNumber', function (value) {
        return /^\d{1,8}$/.test(value); // input为number后23.实际value就是23，所以输入23.也合法
    }, '请输入正确数字');
})();

// 表单处理
var formProcess; // 带#号的formId，验证规则，提交的url地址
var form; // 带#号的formId创建的JQuery对象

(function () {
    let url; // 表单提交地址
    formProcess = function (formId, rules, actionUrl) {
        form = $(formId);
        url = actionUrl;
        setDefault(); // 设置日期显示默认值等
        form.validate({
            rules: rules,
            // element是DOM对象。和unhighlight要成对出现，默认未通过验证高亮显示
            highlight: function (element) {
                $(element).closest('div').addClass('validate-failure'); // has-error has-feedback
            },

            unhighlight: function (element) {
                $(element).closest('div').removeClass('validate-failure');
            },

            // 只设定错误信息位置，所以首次验证执行，只执行一次，以后每次success就去设置该位置的label内容
            errorPlacement: function (error, object) {
                object.closest('.form-group').append(error);
            },
            submitHandler: submitForm,
            onfocusin: function (element) {
                hideResult();
            }
        });
    };

    function submitForm() { // form是HTML元素
        // 如果有文件，使用let formData = new FormData($('#addForm')[0]);，还需processData和contentType设为false
        // 因为id常用来取元素，所以如果非重复建议id和name属性设为一样，复选框可不设id属性
        let formData = form.serialize(); // 按name属性序列化，input的type为checkbox和radio的只有选中才被序列化
        // 还可设置cache是否缓存，默认false
        $.ajax({
            url: url,
            type: 'post',
            dataType: 'json',
            data: formData,
            // 服务端必须json转换后再发送
            success: function (message) {
                showResult(true, message);
            }
        });
    }

    function showResult(isSuccess, message) {
        let label = $('#resultTip');
        if (isSuccess) {
            label.removeClass('failure');
            label.addClass('success');
        } else {
            label.removeClass('success');
            label.addClass('failure');
        }
        label.text(message);
        // label.show();
    }

    function hideResult() {
        let label = $('#resultTip');
        // label.hide();
        label.text("");
    }

    // 设置日期框为当前日期
    function setDefault() {
        let date = formatDate(new Date());
        let inputDates = form.find('input[type^="date"]');
        if (inputDates.length > 0) {
            inputDates.each(function (index, element) {
                if (element.value === undefined || element.value === '') {
                    element.value = date;
                }
            })
        }
    }
})();

// 将1970依赖的毫秒时间戳生成yyyy-MM-dd格式日期
function formatTimestamp(timestamp) {
    let date = new Date();
    date.setTime(timestamp);
    return formatDate(date);
}

function formatDate(date) {
    // 必须是yyyy-MM-dd格式才能设置input为date类型的值
    let month = ('0' + (date.getMonth() + 1)).slice(-2); // 取右边两个
    let day = ('0' + date.getDate()).slice(-2);
    return date.getFullYear() + '-' + month + "-" + day;
}

// 自动将值填充到表单（反序列化）
function deserialize(data) {
    parseJson('', data);
    function parseJson(upper, json) {
        // Array.isArray(JSON.parse('["abc", "def"]')) 结果是true，传递字符串是false，也可$.isArray()
        // $.isPlainObject()判断是否纯粹的对象，new或者{}创建的，也可typeof obj === 'object'
        $.each(json, function (key, value) {
            let name = upper.length > 0 ? upper + '.' + key : key; // 带点的拼接一起作为name属性
            let $name = form.find('[name="' + name + '"]');
            if ($name.length === 0 && $.isPlainObject(value)) {
                parseJson(name, value);
                return;
            }
            if ($name.is('select') || $name.is('textarea')) {
                $name.val(value);
                return;
            }
            switch ($name.attr('type')) {
                case 'checkbox':
                    $name.each(function (i, item) {
                        for (let val of value) {
                            if (val === Number(item.value)) {
                                item.checked = true;
                                break;
                            }
                        }
                    });
                    break;
                case 'radio':
                    $name.each(function (i, item) {
                        item.checked = value === Number(item.value);
                    });
                    break;
                default:
                    $name.val(value); // 日期格式化成yyyy-MM-dd格式了，直接设置即可
                    break;
            }
        });
    }
}

// 取得URL地址中的指定参数值
function getParameter(name) {
    if (name === undefined || name.length === 0) {
        return '';
    }
    let search = window.location.search;
    let start = search.indexOf(name + '=');
    if (start === -1) {
        return '';
    }
    start += name.length + 1;
    let end = search.indexOf('&', start);
    if (end === -1) {
        return search.substring(start); // substr第二个参数表示长度，substring第二个参数表示结束索引（不含）
    } else {
        return search.substring(start, end);
    }
}
