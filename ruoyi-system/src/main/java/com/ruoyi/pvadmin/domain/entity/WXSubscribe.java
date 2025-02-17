package com.ruoyi.pvadmin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 微信订阅
 */
@Data
@TableName(value = "wx_subscribe")
public class WXSubscribe {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 模板id
     */
    private String templateId;
    /**
     * 微信的唯一标识
     */
    private String openId;
    /**
     * 发送给谁
     */
    private String toUserName;
}
