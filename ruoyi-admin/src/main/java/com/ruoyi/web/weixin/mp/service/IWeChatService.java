package com.ruoyi.web.weixin.mp.service;

import com.ruoyi.web.weixin.mp.response.Code2SessionResponse;

import java.io.IOException;

/**
 * 微信接口
 */
public interface IWeChatService {

    /**
     * 微信登录
     *
     * @param jsCode 小程序的JSCode
     * @return Code2SessionResponse 登录返回实体
     */
    Code2SessionResponse login(String jsCode) throws IOException;

    /**
     * 获取 AccessToken
     */
    String getAccessToken();

    /**
     * 发送订阅消息（仅用于程序发送报警消息）
     */
    void sendAlarmMessage();
}
