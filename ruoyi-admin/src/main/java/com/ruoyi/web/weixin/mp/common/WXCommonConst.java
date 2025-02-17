package com.ruoyi.web.weixin.mp.common;

/**
 * 微信常量
 */
public class WXCommonConst {
    /**
     * 获取access token的地址
     * 入参：
     * grant_type string	是	填写 client_credential
     * appid	string	是	小程序唯一凭证，即 AppID，可在「微信公众平台 - 设置 - 开发设置」页中获得。（需要已经成为开发者，且账号没有异常状态）
     * secret	string	是	小程序唯一凭证密钥，即 AppSecret，获取方式同 appid
     * 返回参数：
     * access_token	string	获取到的凭证
     * expires_in	number	凭证有效时间，单位：秒。目前是7200秒之内的值。
     */
    public static final String WX_GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    /**
     * 获取session
     */
    public static final String WX_CODE_SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    /**
     * checkSession接口 GET
     */
    public static final String WX_CHECK_SESSION_KEY = "https://api.weixin.qq.com/wxa/checksession?access_token=%s";
    /**
     * sendMessage 发送订阅消息 POST
     */
    public static final String WX_SEND_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=%s";
    /**
     * 微信的跳转page
     */
    public static final String WX_ALARM_PAGE = "/subPackage/pages/manage/alarmDetail?id=%s";
}