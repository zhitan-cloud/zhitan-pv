package com.ruoyi.pvadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.pvadmin.domain.entity.DeviceTypeIndexTemplate;
import com.ruoyi.pvadmin.domain.model.IndexTemplateModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 平台能源折标模板；平台预置的 指标模板，不提供租户给，下面预设的采集点、计算点信息Mapper接口
 *
 * @author ruoyi
 * @date 2023-08-23
 */
public interface DeviceTypeIndexTemplateMapper extends BaseMapper<DeviceTypeIndexTemplate> {
    /**
     * 根据类型查询点位模板
     *
     * @param type 类型
     * @return 结果
     */
    List<IndexTemplateModel> listByType(@Param("type") String type);
}