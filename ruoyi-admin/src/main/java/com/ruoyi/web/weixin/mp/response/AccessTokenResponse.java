package com.ruoyi.web.weixin.mp.response;

import lombok.Data;

/**
 * access token的实体类
 */
@Data
public class AccessTokenResponse {

    /**
     * access_token访问码
     */
    private String access_token;
    /**
     * 过期时间 秒
     */
    private int expires_in;
}
