package com.ruoyi.pvadmin.service;

public interface IWXUserService {
    /**
     * 用户绑定微信
     *
     * @param userId 用户id
     * @param openId 微信的openid
     */
    void bindWX(Long userId, String openId);

    /**
     * 用户解绑微信
     *
     * @param userId 用户id
     * @param openId 微信的openid
     */
    void unbindWX(Long userId, String openId);
}
