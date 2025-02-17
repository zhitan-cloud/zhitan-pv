package com.ruoyi.pvadmin.domain.enums;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 点位模型枚举
 */
@Getter
@AllArgsConstructor
public enum ElectricityTypeEnum {

    /**
     * 尖
     */
    tip("tip", "尖"),
    /**
     * 峰
     */
    peak("peak", "峰"),
    /**
     * 平
     */
    flat("flat", "平"),
    /**
     * 谷
     */
    trough("trough", "谷"),
    /**
     * 深谷
     */
    deep("deep", "深谷"),

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
        for (ElectricityTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return name;
    }

    /**
     * 根据code查询是否存在
     *
     * @param code 查询条件
     * @return
     */
    public static Boolean getExists(String code) {
        for (ElectricityTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}