package com.ruoyi.web.weixin.mp.response;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class WXBaseResponse implements Serializable {
    /**
     * 错误码
     */
    @JSONField(name = "errcode")
    private int errCode;
    /**
     * 错误信息
     */
    @JSONField(name = "errmsg")
    private int errMsg;
}
