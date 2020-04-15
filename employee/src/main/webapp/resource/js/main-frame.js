$(function () {
    $('.lsm-scroll').slimscroll({
        height: 'auto',
        position: 'right',
        railOpacity: 1,
        size: "5px",
        opacity: .4,
        color: '#fffafa',
        wheelStep: 5,
        touchScrollStep: 50
    });
    $('.lsm-container ul ul').css("display", "none");
    // lsm-sidebar收缩展开，如果$()内传入动态加入的标签元素则无法触发，传入已存在的父元素，然后将动态增加的放入第二个参数
    $('.lsm-container').on('click', '.lsm-sidebar a', function () {
        $('.lsm-scroll').slimscroll({
            height: 'auto',
            position: 'right',
            size: "8px",
            color: '#9ea5ab',
            wheelStep: 5,
            touchScrollStep: 50
        });
        if (!$('.left-side-menu').hasClass('lsm-mini')) {
            $(this).parent("li").siblings("li.lsm-sidebar-item").children('ul').slideUp(200);
            if ($(this).next().css('display') === 'none') {
                // 子菜单未展开
                //$('.lsm-sidebar-item').children('ul').slideUp(300);
                $(this).next('ul').slideDown(200);
                $(this).parent('li').addClass('lsm-sidebar-show').siblings('li').removeClass('lsm-sidebar-show');
            } else {
                // 子菜单已展开
                $(this).next('ul').slideUp(200);
                //$('.lsm-sidebar-item.lsm-sidebar-show').removeClass('lsm-sidebar-show');
                $(this).parent('li').removeClass('lsm-sidebar-show');
            }
        }
        // added 20200329，onclick事件优先于href跳转
        if ($(this).parent().hasClass('lsm-sidebar-item')
            || $(this).parent().parent().hasClass('lsm-popup')) {
            return;
        }
        $('.lsm-sidebar a').removeClass('active');
        let $id = $(this).parent().attr('id');
        $('.lsm-sidebar li[id="' + $id + '"]>a').addClass('active'); // 对应当前ID的选中
    });

    //lsm-mini
    $('.lsm-mini-btn svg').on('click', function () {
        let sideMenu = $('.left-side-menu');
        let container = $('.lsm-container');
        if ($('.lsm-mini-btn input[type="checkbox"]').prop("checked")) {
            // added 20200329，添加弹出菜单框架
            container.append("<div class='second lsm-popup lsm-sidebar'><div></div></div>");
            container.append("<div class='third lsm-popup lsm-sidebar'><div></div></div>");
            container.children('.lsm-popup.second').hide();
            container.children('.lsm-popup.third').hide();

            $('.lsm-sidebar-item.lsm-sidebar-show').removeClass('lsm-sidebar-show');
            container.find('ul').removeAttr('style'); // 所有菜单都取消收缩
            sideMenu.addClass('lsm-mini');
            contentExtend(true); // 右侧内容添加扩展
            sideMenu.stop().animate({width: 60}, 200); // 动画效果，其子元素也会缩小（除非设置固定大小）
        } else {
            // added 20200329，移除弹出菜单框架
            $('.lsm-sidebar.lsm-popup').remove();

            sideMenu.removeClass('lsm-mini');
            container.find('ul ul').css("display", "none"); // 二级以后菜单都收缩
            contentExtend(false); // 右侧内容删除扩展
            sideMenu.stop().animate({width: 212}, 200);
        }

    });

    // 收缩模式下的光标悬浮弹二级框
    $(document).on('mouseover', '.lsm-mini .lsm-container ul:first>li', function () {
        let popupSecond = $('.lsm-popup.second');
        let popupThird = $('.lsm-popup.third');
        popupThird.hide();
        // edited，将append放入点击mini-btn事件中，解决了第一次浮动时popup内容为空问题
        popupSecond.eq(0).children('div').html($(this).html());
        popupSecond.show();
        var top = $(this).offset().top;
        var d = $(window).height() - $(".lsm-popup.second>div").height();
        if (d - top <= 0) {
            top = d >= 0 ? d - 8 : 0;
        }
        popupSecond.stop().animate({"top": top}, 100);
    });

    // 收缩模式下，弹出框内的悬浮，三级框
    $(document).on('mouseover', '.second.lsm-popup.lsm-sidebar > div > ul > li', function () {
        let popupThird = $('.lsm-popup.third');
        if (!$(this).hasClass("lsm-sidebar-item")) {
            popupThird.hide();
            return;
        }
        // edited，将append放入点击mini-btn事件中，解决了第一次浮动时popup内容为空问题
        popupThird.eq(0).children('div').html($(this).html());
        popupThird.show();
        let top = $(this).offset().top;
        let d = $(window).height() - popupThird.height();
        if (d - top <= 0) {
            top = d >= 0 ? d - 8 : 0;
        }
        popupThird.stop().animate({"top": top}, 100);
    });

    // 收缩模式下，光标离开的缩回
    $(document).on('mouseleave',
        '.lsm-mini .lsm-container ul:first, .lsm-mini .slimScrollBar, .second.lsm-popup, .third.lsm-popup',
        function () {
            $(".lsm-popup.third").hide();
            $(".lsm-popup.second").hide();
        });

    // 收缩模式下的悬浮弹框
    $(document).on('mouseover', '.lsm-mini .slimScrollBar, .second.lsm-popup', function () {
        $(".lsm-popup.second").show();
    });
    // 收缩模式下第三极的弹框
    $(document).on('mouseover', '.third.lsm-popup', function () {
        $(".lsm-popup.second").show();
        $(".lsm-popup.third").show();
    });
});

// 设置内容部分的动态宽度变换
function contentExtend(extend) {
    let content = $('.right-side-content');
    if (extend) {
        content.stop().animate({width: '+=152'}, 200); // 伸展宽度是152
    } else {
        content.stop().animate({width: '-=152'}, 200);
    }
}