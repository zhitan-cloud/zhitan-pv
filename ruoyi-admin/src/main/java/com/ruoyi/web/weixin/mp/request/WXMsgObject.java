package com.ruoyi.web.weixin.mp.request;

import lombok.Data;

@Data
public class WXMsgObject {
    private String value;

    public WXMsgObject(String value) {
        this.value = value;
    }
}
