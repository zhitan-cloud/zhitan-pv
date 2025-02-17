package com.ruoyi.web.weixin.mp.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.DateTimeUtil;
import com.ruoyi.web.weixin.mp.common.WXCommonConst;
import com.ruoyi.web.weixin.mp.common.WXRedisCacheConst;
import com.ruoyi.web.weixin.mp.request.SendMsgRequest;
import com.ruoyi.web.weixin.mp.request.WXMsgObject;
import com.ruoyi.web.weixin.mp.response.AccessTokenResponse;
import com.ruoyi.web.weixin.mp.response.Code2SessionResponse;
import com.ruoyi.web.weixin.mp.service.IWeChatService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 微信service
 */
@Slf4j
@Service
public class WeChatService implements IWeChatService {
    @Value("${wx.token}")
    String token;
    @Value("${wx.appid}")
    String appId;
    @Value("${wx.secret}")
    String secret;
    @Value("${wx.encodingAesKey}")
    String encodingAesKey;
    @Value("${wx.page}")
    String page;
    @Value("${wx.template_id}")
    String templateId;
    @Value("${wx.env_version}")
    String miniProgramState;
    @Resource
    private RedisCache redisCache;

    private static final OkHttpClient httpClient;

    static {
        httpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 10, TimeUnit.SECONDS))
                .build();
    }

    /**
     * 微信登录
     */
    @Override
    public Code2SessionResponse login(String jsCode) {
        String url = String.format(WXCommonConst.WX_CODE_SESSION, "wx19a51997ec141e13", "a4dc9e6c9dea521a15edac7308b32abc", jsCode);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    String body = response.body().string();
                    return JSON.parseObject(body, Code2SessionResponse.class);
                }
            } else {
                throw new BaseException("微信登录失败: " + response.message());
            }
        } catch (IOException exception) {
            log.error("微信登录失败，{0}", exception);
            throw new BaseException("微信登录失败: " + exception);

        }
        return null;
    }

    /**
     * 获取 access token
     * 有缓存时 从缓存拿 没有的话 从微信获取
     *
     * @return 返回access token
     */
    @Override
    public String getAccessToken() {
        boolean exist = redisCache.hasKey(WXRedisCacheConst.WX_ACCESS_TOKEN_CACHE_KEY);
        if (exist) {
            String accessToken = redisCache.getCacheObject(WXRedisCacheConst.WX_ACCESS_TOKEN_CACHE_KEY);
            if (ObjectUtils.isNotEmpty(accessToken)) {
                return accessToken;
            }
        }
        String accessTokenUrl = String.format(WXCommonConst.WX_GET_ACCESS_TOKEN_URL, appId, secret);
        Request request = new Request.Builder()
                .url(accessTokenUrl)
                .get()
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    AccessTokenResponse body = JSON.parseObject(response.body().string(), AccessTokenResponse.class);
                    redisCache.setCacheObject(
                            WXRedisCacheConst.WX_ACCESS_TOKEN_CACHE_KEY,
                            body.getAccess_token(),
                            body.getExpires_in(),
                            TimeUnit.SECONDS);
                    return body.getAccess_token();
                }
            } else {
                throw new BaseException("获取微信access token失败: " + response.message());
            }
        } catch (IOException exception) {
            log.error("获取微信access token失败，{0}", exception);
            throw new BaseException("获取微信access token失败: " + exception);
        }
        return null;
    }

    /**
     * 发送订阅消息
     */
    @Override
    public void sendAlarmMessage() {
        String accessToken = getAccessToken();
        if (ObjectUtils.isEmpty(accessToken)) {
            throw new BaseException("发送订阅消息失败: access token为空");
        }
        String accessTokenUrl = String.format(WXCommonConst.WX_SEND_MESSAGE, accessToken);

        SendMsgRequest msgRequest = new SendMsgRequest();
        msgRequest.setPage(page);
        msgRequest.setTemplate_id(templateId);
        msgRequest.setMiniProgramState(miniProgramState);
        msgRequest.setData(createMsgItem());
        msgRequest.setToUser("oZuR169pQ2ZR1IDd8-kScDroAVww");
//        msgRequest.setToUser("oZuR16_qT9AYRXukd7jrJ1GOEOyE");
//        msgRequest.setToUser("oZuR16yPGJKPf8rvtRTXaZoLrSFY");

        String jsonStr = JSON.toJSONString(msgRequest);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType, jsonStr);

        Request request = new Request.Builder()
                .url(accessTokenUrl)
                .post(requestBody)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                System.out.println(response.body().string());
            }
            if (!response.isSuccessful()) {
                throw new BaseException("发送订阅消息失败: " + response.message());
            }
        } catch (IOException exception) {
            log.error("发送订阅消息失败，{0}", exception);
            throw new BaseException("发送订阅消息失败: " + exception);
        }
    }

    private HashMap<String, Object> createMsgItem() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("thing5", new WXMsgObject("崂山电站"));
        map.put("thing6", new WXMsgObject("模具A逆变器"));
        map.put("thing8", new WXMsgObject("温度过高"));
        map.put("time9", new WXMsgObject(DateTimeUtil.getNowDateTime()));
        map.put("thing13", new WXMsgObject("请尽快处理"));
        return map;
    }
}
