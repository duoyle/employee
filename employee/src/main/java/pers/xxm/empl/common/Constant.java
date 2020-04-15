package pers.xxm.empl.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by XuXuemin on 20/3/5
 */
public final class Constant {
    public static final int KB = 1024;
    public static final int MB = KB * 1024;
    public static final int GB = MB * 1024;

    public static final int PAGE_SIZE = 20; // 每页行数
    public static final int PAGINATION_SIZE = 7; // 奇数更准确，中间一位表示当前页

    public static final int PAGE_MISSING = -1;

    public static final int ON_JOB = 1;
    public static final int LEAVE_JOB = 2;
    public static final int REDUCE_EMPLOYEE = 4;

    public static final String SERVLET_PATH = "/jsp";
    public static final String RESOURCE_PREFIX = "/resource/";
    public static final String PAGE_PATH = "/page";
    public static final String PAGE_SUFFIX = ".jsp";

    public static final String MISSING = "--";

}
