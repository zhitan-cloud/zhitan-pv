package com.ruoyi.pvadmin.domain.model;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.pvadmin.influxdb.Quality;
import lombok.Data;

import java.util.Date;

/**
 * @author 范新富
 * <p>
 * 测点数据实体类.
 */
@Data
public class TagValue {

  /**
   * 测点编码.
   */
  @Excel(name = "测点编码", width = 15)
  private String tagCode;

  /**
   * 测点数据时间.
   */
  @Excel(name = "测点数据时间", width = 15)
  private Date dataTime;

  /**
   * 测点值.
   */
  @Excel(name = "测点值", width = 15)
  private Double value;

  /**
   * 测点数据质量.
   */
  @Excel(name = "测点数据质量", width = 15)
  private Quality quality;
}