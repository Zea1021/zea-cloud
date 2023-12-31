package com.zea.cloud.common.bean.common;

import lombok.Data;

/**
 * 分页查询条件
 * @author jfzoyu
 */
@Data
public class PageCondition {

    private Integer pageNum = 1;

    private Integer pageSize = 20;
}
