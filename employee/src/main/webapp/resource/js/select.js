/* 该脚本用于从弹出框的数据列表中选择一个 */
/* 下面方法用于给使用者调用 */

// 绑定事件
function initSelect() {
    // on支持动态创建的新的tr元素，只要table>tbody实现存在即可。先解除绑定，否则多次绑定会多次执行
    $('.table>tbody').on('click', 'tr', tableRowClick); // 多次运行只绑定一次事件，因为tr每次都会清除重新添加
}

function tableRowClick() {
    $(this).find('input[type="radio"]').prop('checked', true);
    $('.table tr').removeClass('tr-selected');
    $(this).addClass('tr-selected');
}

// 获取要返回的值
function getReturnValue() {
    let returnValue = {};
    let columns = $('#table-data>thead>tr>th');
    $('#table-data>tbody>tr').each(function (rn, tr) {
        let $tr = $(tr);
        if ($tr.find('input:radio:checked').length > 0) {
            $tr.children().each(function (cn, ceil) {
                let field = columns.eq(cn).attr('id');
                if (field !== undefined) {
                    returnValue[field] = $(ceil).text();
                }
            });
        }
    });
    return returnValue;
}
