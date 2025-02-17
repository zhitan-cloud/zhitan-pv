package com.ruoyi.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动填充处理类
 *
 * @author badao
 * @version 1.0
 * @see
 **/
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String nickName = user.getNickName();

        this.setFieldValByName("createBy", nickName, metaObject);
        this.setFieldValByName("updateBy", nickName, metaObject);

        this.setFieldValByName("userId", user.getUserId(), metaObject);
        this.setFieldValByName("deptId", user.getDeptId(), metaObject);

        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}