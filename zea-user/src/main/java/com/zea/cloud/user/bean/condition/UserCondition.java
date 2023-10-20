package com.zea.cloud.user.bean.condition;

import com.zea.cloud.user.bean.po.PageCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * user分页查询条件
 * @author jfzou
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserCondition extends PageCondition {

    private String userName;

    private String nickName;

    private String phone;

    private String department;

}
