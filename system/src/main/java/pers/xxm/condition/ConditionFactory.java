package pers.xxm.condition;

import pers.xxm.trouble.BusinessException;
import pers.xxm.trouble.ErrorCode;
import pers.xxm.resource.ResourceManager;
import pers.xxm.util.StringUtil;

import java.util.Arrays;

/**
 * Created by XuXuemin on 20/3/7
 */
public class ConditionFactory {
    /**
     * 创建SQL条件，根据列名和关键字，模糊匹配条件
     *
     * @param column  列名
     * @param keyword 关键字
     * @return 条件对象
     */
    public static Condition newCondition(String column, String keyword) {
        // 没有参数时直接返回条件，条件字符串为空也直接返回
        if (StringUtil.isEmpty(column)) {
            throw new BusinessException(ErrorCode.PARAM_ILLEGAL, ResourceManager.getMessage("param.illegal"));
        }
        return new PlainCondition(column, keyword);
    }

    /**
     * 创建SQL条件，根据条件语句及其需要的参数
     *
     * @param statement 条件语句，参数用问号占位
     * @param args      所需参数
     * @return 条件对象
     */
    public static Condition newCondition(String statement, Object... args) {
        // 没有参数时直接返回条件，条件字符串为空也直接返回
        if (StringUtil.isEmpty(statement)) {
            throw new BusinessException(ErrorCode.PARAM_ILLEGAL, ResourceManager.getMessage("param.illegal"));
        }
        return new FancyCondition(statement, args);
    }

    // 简单条件对象
    private static class PlainCondition implements Condition {
        private String statement;
        private Object parameter;

        private PlainCondition(String column, String keyword) {
            this.statement = column.concat(" like ?");
            this.parameter = "%" + keyword + "%";
        }

        @Override
        public String getStatement() {
            return this.statement;
        }

        @Override
        public Object[] getParameters() {
            return new Object[]{this.parameter};
        }

        @Override
        public Object getParameter() {
            return this.parameter;
        }

        @Override
        public Object[] concatParameters(Object... params) {
            if (params == null || params.length == 0) {
                return new Object[]{this.parameter};
            }
            Object[] objects = new Object[1 + params.length];
            objects[0] = this.parameter;
            // 将params中的元素从第0开始，拷贝到objects中从第1开始处，长度指定拷贝的元素个数
            System.arraycopy(params, 0, objects, 1, params.length);
            return objects;
        }
    }

    // 复杂条件对象
    // 构造中：使用一个参数，大括号括起来值，然后解析成问号，值解析成params；使用两个参数，直接带着问号，值数组作为第二个参数
    private static class FancyCondition implements Condition {
        private String statement;
        private Object[] parameters;

        private FancyCondition(String expression, Object... params) {
            this.statement = expression;
            this.parameters = params;
        }

        @Override
        public String getStatement() {
            return this.statement;
        }

        @Override
        public Object[] getParameters() {
            return this.parameters;
        }

        @Override
        public Object getParameter() {
            if (this.parameters == null || this.parameters.length == 0) {
                return null;
            }
            return this.parameters[0];
        }

        @Override
        public Object[] concatParameters(Object... params) {
            if (params == null || params.length == 0) {
                return this.parameters;
            }
            if (this.parameters == null || this.parameters.length == 0) {
                return params;
            }
            Object[] objects = Arrays.copyOf(this.parameters, this.parameters.length + params.length);
            // 将params中的元素从第0开始，拷贝到objects中从第1开始处，长度指定拷贝的元素个数
            System.arraycopy(params, 0, objects, this.parameters.length, params.length);
            return objects;
        }
    }

}
