package com.ruoyi.pvadmin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.pvadmin.domain.dto.AlarmQueryDTO;
import com.ruoyi.pvadmin.domain.entity.Alarm;
import com.ruoyi.pvadmin.domain.vo.AlarmVO;

import java.util.List;

/**
 * 报警Mapper接口
 */
public interface AlarmMapper extends BaseMapper<Alarm> {

    /**
     * 根据查询条件查询报警列表
     *
     * @param dto 查询条件
     * @return 结果
     */
    List<AlarmVO> listByQueryDto(AlarmQueryDTO dto);

    /**
     * 根据id查询报警信息详情
     *
     * @param id 报警信息id
     * @return 结果
     */
    AlarmVO getInfoById(String id);
}