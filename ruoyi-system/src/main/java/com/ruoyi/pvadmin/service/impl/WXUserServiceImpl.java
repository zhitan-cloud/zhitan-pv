package com.ruoyi.pvadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.pvadmin.domain.entity.WXUser;
import com.ruoyi.pvadmin.mapper.WXUserMapper;
import com.ruoyi.pvadmin.service.IWXUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
public class WXUserServiceImpl extends ServiceImpl<WXUserMapper, WXUser> implements IWXUserService {

    /**
     * 绑定微信
     *
     * @param userId 用户id
     * @param openId 微信的openid
     */
    @Override
    public void bindWX(Long userId, String openId) {
        try {
            if (ObjectUtils.isEmpty(userId)) {
                throw new BaseException("请先登录后再进行绑定操作！");
            }
            if (ObjectUtils.isEmpty(openId)) {
                throw new BaseException("获取微信用户信息失败！");
            }
            // 判定此微信的openId是否已绑定其他账号
            LambdaQueryWrapper<WXUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WXUser::getOpenId, openId);
            Integer count = baseMapper.selectCount(queryWrapper);
            if (count != null && count > 0) {
                throw new BaseException("此微信已绑定过用户，绑定失败！");
            }
            WXUser entity = new WXUser();
            entity.setUserId(userId);
            entity.setOpenId(openId);
            baseMapper.insert(entity);
        } catch (Exception exception) {
            throw new BaseException("绑定微信失败：" + exception);
        }
    }

    /**
     * 解绑微信
     *
     * @param userId 用户id
     * @param openId 微信的openid
     */
    @Override
    public void unbindWX(Long userId, String openId) {
        try {
            if (ObjectUtils.isEmpty(userId)) {
                throw new BaseException("获取用户失败，解绑失败！");
            }
            if (ObjectUtils.isEmpty(openId)) {
                throw new BaseException("获取微信用户信息失败！");
            }
            LambdaQueryWrapper<WXUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WXUser::getUserId, userId);
            queryWrapper.eq(WXUser::getOpenId, openId);
            baseMapper.delete(queryWrapper);
        } catch (Exception exception) {
            throw new BaseException("解绑微信失败：" + exception);
        }
    }
}
