package com.ruoyi.web.weixin.mp.exception;

/**
 * 微信服务端错误
 */
public class WXServerException extends Exception {
    public final static int OK = 0;
    public final static int SystemError = -1;
    public final static int InvalidCredentialAccessTokenIsInvalidOrNotLatest = 40001;
    public final static int InvalidAppId = 40013;
    public final static int InvalidGrantType = 40002;
    public final static int InvalidSecret = 40125;
    public final static int IpNotInWhitelist = 40164;
    public final static int AppSecretMissing = 41004;
    public final static int ProhibitedUseTokenInterface = 50004;
    public final static int TheAccountHasBeenFrozen = 50007;
    public final static int MustBeDedicatedToken = 61024;

    private final int code;

    private static String getMessage(int code) {
        switch (code) {
            case SystemError:
                return "系统繁忙";
            case InvalidCredentialAccessTokenIsInvalidOrNotLatest:
                return "AppSecret错误，或者access_token无效";
            case InvalidAppId:
                return "不合法的AppID";
            case InvalidGrantType:
                return "不合法的凭证类型";
            case InvalidSecret:
                return "不合法的secret";
            case IpNotInWhitelist:
                return "调用接口的IP地址不在白名单中";
            case AppSecretMissing:
                return "缺少 secret 参数";
            case ProhibitedUseTokenInterface:
                return "禁止使用 token 接口";
            case TheAccountHasBeenFrozen:
                return "账号已冻结";
            case MustBeDedicatedToken:
                return "使用专用token";
            default:
                return null;
        }
    }

    public int getCode() {
        return code;
    }

    public WXServerException(int code) {
        super(getMessage(code));
        this.code = code;
    }
}
