package pers.xxm.empl.vo;

import lombok.Getter;
import lombok.Setter;
import pers.xxm.bean.BeanObject;

/**
 * Created by XuXuemin on 20/3/16
 * 处理结果，用于服务层和客户端的处理结果交互，包括业务层的返回也要用Result，这种处理不够优雅，暂时不用
 */
public class Result extends BeanObject {
    @Getter @Setter
    private Integer status;
    @Getter @Setter
    private Object data;
}
