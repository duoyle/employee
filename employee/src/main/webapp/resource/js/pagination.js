// export {loadPage, search}; // 可使用as定义别名，暴漏给引用方（import该模块或者引用该模块的jsp文件）

// 一个页面中不能有两个pagination，否则会乱，这里可能需要封装

// 下面三个是定义的三个接口函数，供使用方的调用。一下var换做let在select.jsp中提示未定义
var initPagination; // 参数是json对象，listUrl:字符串；update(key):更新函数；remove(key):删除函数
var search; // 参数column:查询列名（含表名）；keyword:模糊查询的关键字
var action; // 使用端的操作，必须是json对象，也就是object
var toPage;

(function () {
    let totalPages;
    let tableLine; // 表中行数据结构
    let listUrl;
    let pageNumberInput;

    // 分页参数
    const pageOptions = {
        currentPage: 1,
        pageSize: 20,
        column: "",
        keyword: ""
    };
    let selPageSize;
    // 首次打开调用入口
    initPagination = function (arguments) {
        listUrl = arguments.listUrl;
        action = arguments.action;
        pageOptions.pageSize = arguments.pageSize === undefined ? pageOptions.pageSize : arguments.pageSize;

        let tableBody = $('#table-data>tbody').eq(0);
        tableLine = tableBody.children('tr').eq(0); // 保存提供的行结构
        tableBody.empty(); // 清空提供的行结构

        // 检查和设置每页行数
        // 绑定事件，修改每页行数。函数定义后立即执行将括号括起后加调用
        // pageshow/pagehide会在前进后退按钮页面显示时触发，load事件触发之后也会触发pageshow。
        selPageSize = $('#sel-pageSize'); // 不存在也返回object，这里单独定义
        if (selPageSize.length > 0) {
            pageOptions.pageSize = Number(selPageSize.val()); // 获取当前页大小
            // select改变后，跳转另一链接点击后退之后，选择框中是修改后的值，没有触发change事件，但element.value取的也是初始值（这个怪异）
            // 经测试之后发现element.value的值在pageshow事件触发之后成功取到了。
            selPageSize.change(function () {
                pageOptions.pageSize = Number(this.value); // this表示当前select标签
                pageOptions.currentPage = 1;
                beginLoadData();
            });
        }

        pageNumberInput = $('#pageNumber'); // 保存起来，下次用更快取到
        // 跳转到指定页
        $('#jumpPage').click(function () {
            toPage(Number(pageNumberInput.val()));
        });
    };

    // 查询数据调用入口，如果有复杂条件，可以使用condition，arguments方式
    search = function (column, keyword) {
        if (selPageSize.length > 0) {
            pageOptions.pageSize = Number(selPageSize.val()); // 再取一次页大小，防止load事件中取得后退页中的不准
        }
        pageOptions.column = column;
        pageOptions.keyword = keyword;
        pageOptions.currentPage = 1; // 查询后从第一页开始
        pageNumberInput.val(1); // 设置输入框页码跟着变动
        beginLoadData();
    };

    toPage = function (number) {
        if (number <= 0 || number > Number($("#totalPages").text())) {
            alert("请输入正确的页码");
        } else {
            pageOptions.currentPage = number;
            pageNumberInput.val(number); // 设置输入框页码跟着变动
            beginLoadData();
        }
    };

    // 异步读取数据
    function beginLoadData() {
        $.ajax({
            url: listUrl,
            type: 'post',
            dataType: 'json',
            data: pageOptions,
            success: function (page) {
                loadTable(page.rows);
                loadPagination(page);
            }
        });
    }

    // 装载数据
    function loadTable(rows) {
        let tbody = $('#table-data>tbody').eq(0); // 取得表格body部分
        let columns = $('#table-data>thead>tr>th');

        let lines = $.map(rows, function (row, num) {
            let line = tableLine.clone();
            let clazz = num % 2 === 0 ? 'tr-odd' : 'tr-even';
            line.addClass(clazz);
            line.children('td').each(function (index, ceil) {
                let td = $(ceil); // ceil是HTML对象
                let headCeil = columns.eq(index);
                let field = headCeil.attr('id');
                if (field !== undefined) {
                    td.text(getAttribute(row, field)); // 设置多个的text（内容）
                } else if (headCeil.attr('class') === 'operation') {
                    // 这里本使用:hidden方式但是jQuery对象无法使用，$(':hidden')此方式可以
                    let id = columns.filter('.row-key').attr('id');
                    if (!$.isEmptyObject(action)) {
                        for (let name in action) {
                            let e = td.find('a[name="' + name + '"]');
                            if (e.length > 0) {
                                e.attr('href', 'javascript:action.' + name + '("' + getAttribute(row, id) + '");');
                            }
                        }
                    }
                }

            });
            return line;
        });
        tbody.empty(); // 先清空原来的
        tbody.append(lines); // 对象更新后会更新到页面
    }

    function loadPagination(page) {
        if (page.currentPage > 1) {
            // let firstPage = $('.pagination #firstPage');
            // let previousPage = $('.pagination #previousPage');
            $('#firstPage').removeAttr('class');
            $('#previousPage').removeAttr('class');
            $('#firstPage>a').attr('href', 'javascript:toPage(' + 1 + ')');
            $('#previousPage>a').attr('href', 'javascript:toPage(' + (page.currentPage - 1) + ')');

        } else {
            $('#firstPage').attr('class', 'disabled');
            $('#previousPage').attr('class', 'disabled');
        }

        let nextPage = $('#nextPage'); // 下一页后面使用，这里存下来
        if (page.currentPage < page.totalPages) {
            nextPage.removeAttr('class');
            $('#lastPage').removeAttr('class');
            $('#nextPage>a').attr('href', 'javascript:toPage(' + (page.currentPage + 1) + ')');
            $('#lastPage>a').attr('href', 'javascript:toPage(' + page.totalPages + ')');
        } else {
            nextPage.attr('class', 'disabled');
            $('#lastPage').attr('class', 'disabled');
        }
        setPageNumber(page); // 设置页码数字
    }

    let setPageNumber = function (page) {
        // 设置数字页码
        $('.pagination li').remove(':not([id])'); // 删除所有数字页码（未设置ID）
        for (let i = page.beginNumber; i <= page.endNumber; i++) { // let更适合局部变量，var变量共享，赋值也会改变
            let pageNumber = getPage(i);
            if (i === page.currentPage) {
                pageNumber.attr('class', 'active');
            } else {
                pageNumber.children('a').attr('href', 'javascript:toPage(' + i + ')');
            }
            pageNumber.insertBefore(nextPage);
        }
        // 更新总页数也总行数
        totalPages = page.totalPages; // 记录总页数
        $('#totalPages').text(page.totalPages); // 总页数
        $('#totalRows').text(page.totalRows); // 总条数
    };

    // 生成一个页码项
    function getPage(number) {
        return $('<li><a>' + number + '</a></li>');
    }

    // 读取json对象的指定字段的值
    function getAttribute(json, field) {
        let fields = field.split('.');
        return parseAttribute(json, fields);
    }
    // 递归解析json对象
    function parseAttribute(json, fields) {
        if (fields.length === 1) {
            return json[fields[0]];
        } else {
            return parseAttribute(json[fields[0]], fields.slice(1));
        }
    }

})();
