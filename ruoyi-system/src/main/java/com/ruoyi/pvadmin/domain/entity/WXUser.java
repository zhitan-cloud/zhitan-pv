package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统用户和微信的一对多绑定，一个系统账户可以绑定多个微信，但一个微信只能对应一个系统账户
 */
@Data
@TableName(value = "wx_user")
public class WXUser {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 微信的唯一标识
     */
    private String openId;
}
