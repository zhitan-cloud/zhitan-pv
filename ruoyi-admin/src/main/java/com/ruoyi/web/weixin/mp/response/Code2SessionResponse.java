package com.ruoyi.web.weixin.mp.response;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 微信 Code2Session 返回实体
 */
@Data
public class Code2SessionResponse extends WXBaseResponse {
    /**
     * 会话密钥
     */
    @JSONField(name = "session_key")
    private String session_key;
    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台账号下会返回
     */
    @JSONField(name = "unionid")
    private String unionId;
    /**
     * 用户唯一标识
     */
    @JSONField(name = "openid")
    private String openId;
}
