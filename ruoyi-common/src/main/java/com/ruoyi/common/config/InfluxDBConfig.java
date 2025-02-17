package com.ruoyi.common.config;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author fanxinfu
 */
@Component
@Configuration
@Data
public class InfluxDBConfig {

  @Value("${influxdb.host}")
  private String host;
  @Value("${influxdb.token}")
  private String token;
  @Value("${influxdb.org}")
  private String org;
  @Value("${influxdb.bucket}")
  private String bucket;
  @Value("${influxdb.measurement}")
  private String measurement;
}