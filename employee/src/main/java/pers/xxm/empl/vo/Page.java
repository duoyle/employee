package pers.xxm.empl.vo;

import lombok.Getter;
import lombok.Setter;
import pers.xxm.bean.BeanObject;
import pers.xxm.empl.common.Constant;

import java.util.List;

/**
 * Created by XuXuemin on 20/3/6
 */
public class Page<T> extends BeanObject {

    @Getter
    private long currentPage;
    @Getter
    private int pageSize;

    @Getter @Setter
    private List<T> rows;

    @Getter
    private long totalRows;
    @Getter
    private long totalPages;

    // 这里只支持一个字段的条件，如果复杂查询可定义一个类，生成带括号和逻辑符的条件，采用字符串格式化去填充值
    @Getter @Setter
    private String column;
    @Getter @Setter
    private String keyword;

    @Getter
    private int numberLength = Constant.PAGINATION_SIZE; // 分页条的长度，数字页码个数

    @Getter
    private long beginNumber;
    @Getter
    private long endNumber;

    /**
     * 创建Page对象
     */
    public Page() {
        this.currentPage = 1; // 默认第一页
        this.pageSize = Constant.PAGE_SIZE; // 默认每页显示20
        this.reset();
    }

    /**
     * 设置当前第几页
     * @param currentPage 要设置的当前页码
     */
    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
        this.updateNumbers();
    }

    /**
     * 设置每页行数，将导致所有数据清零
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        this.reset();
    }

    /**
     * 设置数字显示的长度
     * @param length 数字个数，建议奇数，偶数会多一页
     */
    public void setNumberLength(int length) {
        this.numberLength = length;
        this.updateNumbers();
    }

    public void setTotalRows(long rows) {
        this.totalRows = rows;
        this.updateTotalPages();
    }

    // 设置开始编号和结束编号
    private void updateNumbers() {
        if (this.totalPages < 0) {
            this.beginNumber = Constant.PAGE_MISSING;
            this.endNumber = Constant.PAGE_MISSING;
        } else {
            this.beginNumber = Math.max(1, this.currentPage - this.numberLength / 2);
            this.endNumber = Math.min(this.getTotalPages(), this.currentPage + this.numberLength / 2);
        }
    }

    // 更新总页数
    private void updateTotalPages() {
        if (this.totalRows >= 0) {
            // 设置总页数
            long times = this.totalRows / this.pageSize;
            this.totalPages = this.totalRows % this.pageSize > 0 ? times + 1 : times;
        }
        updateNumbers();
    }

    // 初始化参数
    private void reset() {
        this.rows = null;
        this.totalRows = Constant.PAGE_MISSING;
        this.totalPages = Constant.PAGE_MISSING;
        this.beginNumber = Constant.PAGE_MISSING;
        this.endNumber = Constant.PAGE_MISSING;
    }

}
