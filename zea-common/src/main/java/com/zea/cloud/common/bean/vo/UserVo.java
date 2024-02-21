package com.zea.cloud.common.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserVo implements Serializable {
    private Integer id;
    private String userName;
    private String nickName;
    private String phone;
    private String email;
    private String address;
    private String department;
    private Date createTime;
    private Date updateTime;
}
