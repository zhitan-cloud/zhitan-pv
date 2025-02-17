package com.ruoyi.web.controller.pvadmin;

import com.ruoyi.pvadmin.service.IWXSubscribeService;
import com.ruoyi.web.weixin.mp.aes.AesException;
import com.ruoyi.web.weixin.mp.aes.SHA1;
import com.ruoyi.web.weixin.mp.service.IWeChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/wx")
@Api(value = "微信", tags = "微信")
@Slf4j
public class WXController {

    @Value("${wx.token}")
    String token;
    @Value("${wx.appid}")
    String appId;
    @Value("${wx.secret}")
    String secret;
    @Value("${wx.encodingAesKey}")
    String encodingAesKey;
    @Resource
    IWXSubscribeService wxSubscribeService;

    @Resource
    IWeChatService weChatService;

    /**
     * 微信平台验证接入
     */
    @GetMapping("/msg")
    @ApiOperation("微信-验证")
    public String msgVerify(@RequestParam(value = "signature") String msgSignature,
                            @RequestParam(value = "timestamp") String timestamp,
                            @RequestParam(value = "nonce") String nonce,
                            @RequestParam(value = "echostr") String echoStr) throws AesException {
        String signature = SHA1.getSHA1(token, timestamp, nonce);
        if (!signature.equals(msgSignature)) {
            throw new AesException(AesException.ValidateSignatureError);
        }
        return echoStr;
    }

    /**
     * 微信平台处理微信的消息
     */
    @PostMapping("/msg")
    @ApiOperation("微信-处理消息")
    @ResponseBody
    public String msg(HttpServletRequest request,
                      @RequestParam(value = "signature", required = false) String msgSignature,
                      @RequestParam(value = "timestamp", required = false) String timestamp,
                      @RequestParam(value = "nonce") String nonce) throws AesException {
        String signature = SHA1.getSHA1(token, timestamp, nonce);
        if (!signature.equals(msgSignature)) {
            throw new AesException(AesException.ValidateSignatureError);
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder requestContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestContent.append(line);
            }
            reader.close();
            log.debug("接收到推送的消息：" + requestContent);
            return "success";
        } catch (IOException e) {
            // 处理异常情况
            e.printStackTrace();
            log.error("异常：" + e.getMessage());
            return e.toString();
        }
    }

    @PostMapping("/send")
    @ApiOperation("微信-发送消息")
    @ResponseBody
    public String send() {
        weChatService.sendAlarmMessage();
        return "";
    }
}
