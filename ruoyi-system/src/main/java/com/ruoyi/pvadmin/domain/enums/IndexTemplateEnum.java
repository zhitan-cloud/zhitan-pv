package com.ruoyi.pvadmin.domain.enums;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 点位模型枚举
 */
@Getter
@AllArgsConstructor
public enum IndexTemplateEnum {

    COLLECT("COLLECT", "采集点"),
    CALCULATE("CALCULATE", "计算点"),

    ;

    private final String code;

    private final String name;

    /**
     * 根据type找name
     *
     * @param code 查询条件
     * @return
     */
    public static String getNameByCode(String code) {
        String name = Constants.EMPTY;
        for (IndexTemplateEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return name;
    }
}