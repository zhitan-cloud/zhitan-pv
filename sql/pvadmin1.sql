/*
 Navicat Premium Data Transfer

 Source Server         : 光伏开源
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : 1.95.85.239:3306
 Source Schema         : pvadmin

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 04/03/2025 15:53:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for alarm
-- ----------------------------
DROP TABLE IF EXISTS `alarm`;
CREATE TABLE `alarm`  (
  `id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `time_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '时间code 每10分钟一个',
  `data_time` datetime NOT NULL COMMENT '发生时间',
  `device_code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '设备code',
  `err_code` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '错误码',
  `error_description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '错误描述',
  `solution` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '建议解决方案',
  `level` tinyint(1) NOT NULL DEFAULT 1 COMMENT '报警等级（1：一级，2：二级，3：三级）',
  `status` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '1' COMMENT '状态（1未解决、2已解决）',
  `handlers` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '处理人',
  `handlers_name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '处理人姓名',
  `processing_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `image_url` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '反馈图片',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `DEVICE_TIME_ERR_UNIQUE_INDEX`(`time_code` ASC, `device_code` ASC, `err_code` ASC) USING BTREE COMMENT '设备编码+时间（10分钟一次）+错误码 唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '报警表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_item
-- ----------------------------
DROP TABLE IF EXISTS `data_item`;
CREATE TABLE `data_item`  (
  `device_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '计量器具id',
  `time_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '时间编码',
  `time_type` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '时间类型（HOUR、DAY、MONTH、YEAR）',
  `data_time` datetime NOT NULL COMMENT '业务时间',
  `begin_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '截止时间',
  `value` decimal(16, 2) NULL DEFAULT NULL COMMENT '值',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`device_id`, `time_code`, `time_type`) USING BTREE,
  UNIQUE INDEX `UNIQUE_INDEX`(`device_id` ASC, `time_code` ASC, `time_type` ASC) USING BTREE COMMENT '唯一索引',
  INDEX `idx3`(`data_time` ASC) USING BTREE,
  INDEX `idx2`(`device_id` ASC, `data_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device`  (
  `id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `power_station_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '电站id',
  `code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '设备名称',
  `device_type_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '器具类型id',
  `capacity` float(12, 2) NULL DEFAULT 0.00 COMMENT '容量',
  `factory` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '厂家',
  `rated_ac_power` float(12, 2) NULL DEFAULT 0.00 COMMENT '额定交流功率',
  `grid_type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电网类型',
  `module_peak_power` float(12, 2) NULL DEFAULT NULL COMMENT '组件峰值功率',
  `ammeter` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否电表0：否，1：是',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `CODE_NORMAL_INDEX`(`code` ASC) USING BTREE COMMENT 'CODE加索引，增加查询性能',
  INDEX `INDEX_TYPE`(`device_type_id` ASC) USING BTREE,
  INDEX `INDEX_POWER_STATION_ID`(`power_station_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '设备管理表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for device_copy1
-- ----------------------------
DROP TABLE IF EXISTS `device_copy1`;
CREATE TABLE `device_copy1`  (
  `id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `power_station_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '电站id',
  `code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '设备名称',
  `device_type_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '器具类型id',
  `capacity` float(12, 2) NULL DEFAULT 0.00 COMMENT '容量',
  `factory` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '厂家',
  `rated_ac_power` float(12, 2) NULL DEFAULT 0.00 COMMENT '额定交流功率',
  `grid_type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电网类型',
  `module_peak_power` float(12, 2) NULL DEFAULT NULL COMMENT '组件峰值功率',
  `ammeter` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否电表0：否，1：是',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `CODE_NORMAL_INDEX`(`code` ASC) USING BTREE COMMENT 'CODE加索引，增加查询性能',
  INDEX `INDEX_TYPE`(`device_type_id` ASC) USING BTREE,
  INDEX `INDEX_POWER_STATION_ID`(`power_station_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '设备管理表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for device_index
-- ----------------------------
DROP TABLE IF EXISTS `device_index`;
CREATE TABLE `device_index`  (
  `id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键',
  `index_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '模板id',
  `device_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '设备id',
  `index_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '点位名称',
  `index_code` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '点位编号',
  `index_type` varchar(12) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '点位类型（采集点 -COLLECT、计算点-CALCULATE）',
  `calc_index` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '计算点公式（就是计算指标的累计量所需的采集点指标编码 indexTemplate.code)，这里有一个存储规则[富经理给] 这里会带到 energyIndex.calcText',
  `factor` float NULL DEFAULT 1 COMMENT '变比/倍率/系数/等',
  `tag_key` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '网关用来获取对应数据',
  `unit` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '计量单位',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `DEVICE_ID_NORMAL_INDEX`(`device_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '设备与点位关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for device_inspection
-- ----------------------------
DROP TABLE IF EXISTS `device_inspection`;
CREATE TABLE `device_inspection`  (
  `id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `power_station_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电站Id',
  `power_station_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电站名称',
  `device_code` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '设备编号',
  `device_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '设备名称',
  `inspection_staff` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '点检人员',
  `inspection_result` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '点检结果',
  `inspection_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '类型（0：点检，1：维修）',
  `annex` varchar(20000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '附件地址',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门id',
  `inspection_start_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点检维修开始时间',
  `inspection_end_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点检维修结束时间',
  `spare_part_name_or_number` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '更换备件的名称/数量',
  `estimated_power_loss` double NOT NULL DEFAULT 0 COMMENT '预估损失电量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '设备点检' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for device_inspection_annex
-- ----------------------------
DROP TABLE IF EXISTS `device_inspection_annex`;
CREATE TABLE `device_inspection_annex`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_inspection_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备点检的id',
  `annex` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_inspection_annex
-- ----------------------------

-- ----------------------------
-- Table structure for device_type
-- ----------------------------
DROP TABLE IF EXISTS `device_type`;
CREATE TABLE `device_type`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `remark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device_type
-- ----------------------------
INSERT INTO `device_type` VALUES ('100440185434537986', '科诺逆变器', '备注', '科诺逆变器', 'admin', '2023-09-01 15:44:28', NULL, '2023-09-04 17:26:30');
INSERT INTO `device_type` VALUES ('100440185434537987', '电表', '备注', '电表', 'admin', '2023-09-01 15:44:28', NULL, '2023-09-05 10:58:25');
INSERT INTO `device_type` VALUES ('1698627923435794433', '阳光电源逆变器', NULL, '阳光电源逆变器', 'admin', '2023-09-04 17:23:51', NULL, '2023-11-11 00:36:38');
INSERT INTO `device_type` VALUES ('1698635880143728642', '追日逆变器', NULL, '追日逆变器', 'admin', '2023-09-04 17:55:28', 'admin', '2023-09-04 17:55:28');
INSERT INTO `device_type` VALUES ('1698635947965624322', '北电能源逆变器', NULL, '北电能源逆变器', 'admin', '2023-09-04 17:55:44', NULL, '2025-02-17 17:01:35');

-- ----------------------------
-- Table structure for device_type_index_template
-- ----------------------------
DROP TABLE IF EXISTS `device_type_index_template`;
CREATE TABLE `device_type_index_template`  (
  `id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键',
  `device_type_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '设备类型id',
  `device_type_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '设备类型名称',
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '模板点位名称',
  `code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '模板点位编码',
  `index_type` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '点位类型（采集点 -COLLECT、计算点-CALCULATE）',
  `unit` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '计量单位',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `tag_key` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '关键点',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `DEVICE_TYPE_ID_NORMAL_INDEX`(`device_type_id` ASC) USING BTREE,
  INDEX `CODE_NORMAL_INDEX`(`code` ASC) USING BTREE,
  INDEX `INDEX_TYPE_NORMAL_INDEX`(`index_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '平台能源折标模板；平台预置的 指标模板，不提供租户给，下面预设的采集点、计算点信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device_type_index_template
-- ----------------------------
INSERT INTO `device_type_index_template` VALUES ('1704036315512729602', '1698635880143728642', '追日逆变器', '直流电压', 'DCV', 'COLLECT', 'V', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'DCV');
INSERT INTO `device_type_index_template` VALUES ('1704036315525312514', '1698635880143728642', '追日逆变器', '直流电流', 'DCI', 'COLLECT', 'A', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'DCI');
INSERT INTO `device_type_index_template` VALUES ('1704036315525312515', '1698635880143728642', '追日逆变器', 'AB线电压', 'UAB', 'COLLECT', 'V', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'UAB');
INSERT INTO `device_type_index_template` VALUES ('1704036315525312516', '1698635880143728642', '追日逆变器', 'BC线电压', 'UBC', 'COLLECT', 'V', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'UBC');
INSERT INTO `device_type_index_template` VALUES ('1704036315554672642', '1698635880143728642', '追日逆变器', 'CA线电压', 'UCA', 'COLLECT', 'V', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'UCA');
INSERT INTO `device_type_index_template` VALUES ('1704036315554672643', '1698635880143728642', '追日逆变器', 'A相电流', 'IA', 'COLLECT', 'A', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'IA');
INSERT INTO `device_type_index_template` VALUES ('1704036315567255553', '1698635880143728642', '追日逆变器', 'B相电流', 'IB', 'COLLECT', 'A', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'IB');
INSERT INTO `device_type_index_template` VALUES ('1704036315575644161', '1698635880143728642', '追日逆变器', 'C相电流', 'IC', 'COLLECT', 'A', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'IC');
INSERT INTO `device_type_index_template` VALUES ('1704036315579838465', '1698635880143728642', '追日逆变器', '控制板温度', 'CBT', 'COLLECT', '℃', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'CBT');
INSERT INTO `device_type_index_template` VALUES ('1704036315588227073', '1698635880143728642', '追日逆变器', '模块温度', 'TM', 'COLLECT', '℃', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'TM');
INSERT INTO `device_type_index_template` VALUES ('1704036315592421378', '1698635880143728642', '追日逆变器', '功率因数', 'Q', 'COLLECT', NULL, NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'Q');
INSERT INTO `device_type_index_template` VALUES ('1704036315600809985', '1698635880143728642', '追日逆变器', '逆变效率', 'IE', 'COLLECT', NULL, NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'IE');
INSERT INTO `device_type_index_template` VALUES ('1704036315605004290', '1698635880143728642', '追日逆变器', '电网频率', 'HZ', 'COLLECT', 'Hz', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'HZ');
INSERT INTO `device_type_index_template` VALUES ('1704036315609198593', '1698635880143728642', '追日逆变器', '输出有功功率', 'PW', 'COLLECT', 'W', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'PW');
INSERT INTO `device_type_index_template` VALUES ('1704036315617587202', '1698635880143728642', '追日逆变器', '输出无功功率', 'PQ', 'COLLECT', 'Var', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'PQ');
INSERT INTO `device_type_index_template` VALUES ('1704036315630170114', '1698635880143728642', '追日逆变器', '当日发电量', 'DGL', 'COLLECT', 'KWh', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'DGL');
INSERT INTO `device_type_index_template` VALUES ('1704036315638558721', '1698635880143728642', '追日逆变器', '累积发电量', 'CGL', 'COLLECT', 'KWh', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'CGL');
INSERT INTO `device_type_index_template` VALUES ('1704036315646947329', '1698635880143728642', '追日逆变器', '信息代码', 'ERR', 'COLLECT', NULL, NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'ERR');
INSERT INTO `device_type_index_template` VALUES ('1704036315651141634', '1698635880143728642', '追日逆变器', '运行状态', 'STATUS', 'COLLECT', NULL, NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'STATUS');
INSERT INTO `device_type_index_template` VALUES ('1704036315659530242', '1698635880143728642', '追日逆变器', '累积量', 'Acc', 'CALCULATE', 'KWh', NULL, '2023-09-19 15:34:52', '华控管理员', '2023-09-19 15:34:52', '华控管理员', 'Acc');
INSERT INTO `device_type_index_template` VALUES ('1704036437252403201', '1698635947965624322', '北电能源逆变器', '错误代码', 'ERR', 'COLLECT', NULL, NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'ERR');
INSERT INTO `device_type_index_template` VALUES ('1704036437264986113', '1698635947965624322', '北电能源逆变器', '光伏(直流)电压', 'DCV', 'COLLECT', 'V', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'DCV');
INSERT INTO `device_type_index_template` VALUES ('1704036437264986114', '1698635947965624322', '北电能源逆变器', '光伏(直流)电流', 'DCI', 'COLLECT', 'A', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'DCI');
INSERT INTO `device_type_index_template` VALUES ('1704036437264986115', '1698635947965624322', '北电能源逆变器', 'A相电流', 'IA', 'COLLECT', 'A', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'IA');
INSERT INTO `device_type_index_template` VALUES ('1704036437264986116', '1698635947965624322', '北电能源逆变器', 'B相电流', 'IB', 'COLLECT', 'A', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'IB');
INSERT INTO `device_type_index_template` VALUES ('1704036437264986117', '1698635947965624322', '北电能源逆变器', 'C相电流', 'IC', 'COLLECT', 'A', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'IC');
INSERT INTO `device_type_index_template` VALUES ('1704036437306929154', '1698635947965624322', '北电能源逆变器', '逆变器功率', 'TP', 'COLLECT', NULL, NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'TP');
INSERT INTO `device_type_index_template` VALUES ('1704036437315317762', '1698635947965624322', '北电能源逆变器', 'A相电压', 'UA', 'COLLECT', 'V', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'UA');
INSERT INTO `device_type_index_template` VALUES ('1704036437327900673', '1698635947965624322', '北电能源逆变器', 'B相电压', 'UB', 'COLLECT', 'V', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'UB');
INSERT INTO `device_type_index_template` VALUES ('1704036437336289281', '1698635947965624322', '北电能源逆变器', 'C相电压', 'UC', 'COLLECT', 'V', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'UC');
INSERT INTO `device_type_index_template` VALUES ('1704036437348872194', '1698635947965624322', '北电能源逆变器', '电网频率', 'HZ', 'COLLECT', 'Hz', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'HZ');
INSERT INTO `device_type_index_template` VALUES ('1704036437353066497', '1698635947965624322', '北电能源逆变器', '环境温度', 'TIA', 'COLLECT', '℃', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'TIA');
INSERT INTO `device_type_index_template` VALUES ('1704036437365649410', '1698635947965624322', '北电能源逆变器', '逆变器温度', 'TI', 'COLLECT', '℃', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'TI');
INSERT INTO `device_type_index_template` VALUES ('1704036437374038018', '1698635947965624322', '北电能源逆变器', '散热器温度', 'TR', 'COLLECT', '℃', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'TR');
INSERT INTO `device_type_index_template` VALUES ('1704036437382426625', '1698635947965624322', '北电能源逆变器', '当日发电量', 'DGL', 'COLLECT', 'KWh', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'DGL');
INSERT INTO `device_type_index_template` VALUES ('1704036437390815234', '1698635947965624322', '北电能源逆变器', '累计发电量', 'CGL', 'COLLECT', 'KWh', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'CGL');
INSERT INTO `device_type_index_template` VALUES ('1704036437395009537', '1698635947965624322', '北电能源逆变器', '变压器温度', 'TT', 'COLLECT', '℃', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'TT');
INSERT INTO `device_type_index_template` VALUES ('1704036437403398146', '1698635947965624322', '北电能源逆变器', '逆变器状态', 'STATUS', 'COLLECT', NULL, NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'STATUS');
INSERT INTO `device_type_index_template` VALUES ('1704036437407592449', '1698635947965624322', '北电能源逆变器', '累积量', 'Acc', 'CALCULATE', 'KWh', NULL, '2023-09-19 15:35:21', '华控管理员', '2023-09-19 15:35:21', '华控管理员', 'Acc');
INSERT INTO `device_type_index_template` VALUES ('1704036703179665409', '1698627923435794433', '阳关电源逆变器', '逆变器状态', 'STATUS', 'COLLECT', NULL, NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'STATUS');
INSERT INTO `device_type_index_template` VALUES ('1704036703188054018', '1698627923435794433', '阳关电源逆变器', '状态时间年', 'ERRYEAR', 'COLLECT', NULL, NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'ERRYEAR');
INSERT INTO `device_type_index_template` VALUES ('1704036703196442626', '1698627923435794433', '阳关电源逆变器', '状态时间月', 'ERRMON', 'COLLECT', NULL, NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'ERRMON');
INSERT INTO `device_type_index_template` VALUES ('1704036703204831234', '1698627923435794433', '阳关电源逆变器', '状态时间日', 'ERRDAY', 'COLLECT', NULL, NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'ERRDAY');
INSERT INTO `device_type_index_template` VALUES ('1704036703209025538', '1698627923435794433', '阳关电源逆变器', '状态时间时', 'ERRH', 'COLLECT', NULL, NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'ERRH');
INSERT INTO `device_type_index_template` VALUES ('1704036703217414145', '1698627923435794433', '阳关电源逆变器', '状态时间分', 'ERRMIN', 'COLLECT', NULL, NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'ERRMIN');
INSERT INTO `device_type_index_template` VALUES ('1704036703225802754', '1698627923435794433', '阳关电源逆变器', '状态时间秒', 'ERRSEC', 'COLLECT', NULL, NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'ERRSEC');
INSERT INTO `device_type_index_template` VALUES ('1704036703234191362', '1698627923435794433', '阳关电源逆变器', '设备类型编码', 'SN', 'COLLECT', NULL, NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'SN');
INSERT INTO `device_type_index_template` VALUES ('1704036703242579969', '1698627923435794433', '阳关电源逆变器', '额定输出功率', 'POUT', 'COLLECT', 'kW', NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'POUT');
INSERT INTO `device_type_index_template` VALUES ('1704036703246774273', '1698627923435794433', '阳关电源逆变器', '输出类型', 'POUTTYPE', 'COLLECT', NULL, NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'POUTTYPE');
INSERT INTO `device_type_index_template` VALUES ('1704036703255162882', '1698627923435794433', '阳关电源逆变器', '日发电量', 'DGL', 'COLLECT', 'KWh', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'DGL');
INSERT INTO `device_type_index_template` VALUES ('1704036703263551490', '1698627923435794433', '阳关电源逆变器', '总发电量', 'CGL', 'COLLECT', 'KWh', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'CGL');
INSERT INTO `device_type_index_template` VALUES ('1704036703271940097', '1698627923435794433', '阳关电源逆变器', '总运行时间', 'TOT', 'COLLECT', 'h', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'TOT');
INSERT INTO `device_type_index_template` VALUES ('1704036703280328706', '1698627923435794433', '阳关电源逆变器', '机内空气温度', 'TIA', 'COLLECT', '℃', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'TIA');
INSERT INTO `device_type_index_template` VALUES ('1704036703288717313', '1698627923435794433', '阳关电源逆变器', '直流电压', 'DCV', 'COLLECT', 'V', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'DCV');
INSERT INTO `device_type_index_template` VALUES ('1704036703297105921', '1698627923435794433', '阳关电源逆变器', '直流电流', 'DCI', 'COLLECT', 'A', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'DCI');
INSERT INTO `device_type_index_template` VALUES ('1704036703301300226', '1698627923435794433', '阳关电源逆变器', '总直流功率', 'PTDC', 'COLLECT', 'W', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'PTDC');
INSERT INTO `device_type_index_template` VALUES ('1704036703309688834', '1698627923435794433', '阳关电源逆变器', 'AB线电压/A相电压', 'UAB', 'COLLECT', 'V', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'UAB');
INSERT INTO `device_type_index_template` VALUES ('1704036703318077441', '1698627923435794433', '阳关电源逆变器', 'BC线电压/B相电压', 'UBC', 'COLLECT', 'V', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'UBC');
INSERT INTO `device_type_index_template` VALUES ('1704036703326466049', '1698627923435794433', '阳关电源逆变器', 'CA线电压/C相电压', 'UCA', 'COLLECT', 'V', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'UCA');
INSERT INTO `device_type_index_template` VALUES ('1704036703334854658', '1698627923435794433', '阳关电源逆变器', 'A相电流', 'IA', 'COLLECT', 'A', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'IA');
INSERT INTO `device_type_index_template` VALUES ('1704036703343243265', '1698627923435794433', '阳关电源逆变器', 'B相电流', 'IB', 'COLLECT', 'A', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'IB');
INSERT INTO `device_type_index_template` VALUES ('1704036703351631874', '1698627923435794433', '阳关电源逆变器', 'C相电流', 'IC', 'COLLECT', 'A', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'IC');
INSERT INTO `device_type_index_template` VALUES ('1704036703360020482', '1698627923435794433', '阳关电源逆变器', '总有功功率', 'PW', 'COLLECT', 'W', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'PW');
INSERT INTO `device_type_index_template` VALUES ('1704036703368409089', '1698627923435794433', '阳关电源逆变器', '无功功率', 'PQ', 'COLLECT', 'Var', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'PQ');
INSERT INTO `device_type_index_template` VALUES ('1704036703380992001', '1698627923435794433', '阳关电源逆变器', '功率因数', 'Q', 'COLLECT', NULL, NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'Q');
INSERT INTO `device_type_index_template` VALUES ('1704036703385186305', '1698627923435794433', '阳关电源逆变器', '电网频率', 'HZ', 'COLLECT', 'Hz', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'HZ');
INSERT INTO `device_type_index_template` VALUES ('1704036703393574913', '1698627923435794433', '阳关电源逆变器', '逆变器效率', 'IE', 'COLLECT', '%', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'IE');
INSERT INTO `device_type_index_template` VALUES ('1704036703401963521', '1698627923435794433', '阳关电源逆变器', '额定无功功率', 'RQ', 'COLLECT', 'W', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'RQ');
INSERT INTO `device_type_index_template` VALUES ('1704036703410352129', '1698627923435794433', '阳关电源逆变器', '电抗器温度', 'TK', 'COLLECT', '℃', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'TK');
INSERT INTO `device_type_index_template` VALUES ('1704036703422935041', '1698627923435794433', '阳关电源逆变器', '模块温度1', 'TM1', 'COLLECT', '℃', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '智碳管理员', 'TM1');
INSERT INTO `device_type_index_template` VALUES ('1704036703431323649', '1698627923435794433', '阳关电源逆变器', '模块温度2', 'TM2', 'COLLECT', '℃', NULL, '2023-09-19 15:36:24', '智碳管理员', '2023-09-19 15:36:24', '华控管理员', 'TM2');
INSERT INTO `device_type_index_template` VALUES ('1704036703435517953', '1698627923435794433', '阳关电源逆变器', '模块温度3', 'TM3', 'COLLECT', '℃', NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'TM3');
INSERT INTO `device_type_index_template` VALUES ('1704036703443906561', '1698627923435794433', '阳关电源逆变器', '模块温度4', 'TM4', 'COLLECT', '℃', NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'TM4');
INSERT INTO `device_type_index_template` VALUES ('1704036703452295170', '1698627923435794433', '阳关电源逆变器', '模块温度5', 'TM5', 'COLLECT', '℃', NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'TM5');
INSERT INTO `device_type_index_template` VALUES ('1704036703460683778', '1698627923435794433', '阳关电源逆变器', '模块温度6', 'TM6', 'COLLECT', '℃', NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'TM6');
INSERT INTO `device_type_index_template` VALUES ('1704036703464878081', '1698627923435794433', '阳关电源逆变器', '累积量', 'Acc', 'CALCULATE', 'KWh', NULL, '2023-09-19 15:36:24', '华控管理员', '2023-09-19 15:36:24', '华控管理员', 'Acc');
INSERT INTO `device_type_index_template` VALUES ('1704036821475815426', '100440185434537986', '科诺逆变器', '逆变器地址码', 'CODE', 'COLLECT', NULL, NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'CODE');
INSERT INTO `device_type_index_template` VALUES ('1704036821484204034', '100440185434537986', '科诺逆变器', '产品类型', 'PTYPE', 'COLLECT', NULL, NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'PTYPE');
INSERT INTO `device_type_index_template` VALUES ('1704036821488398337', '100440185434537986', '科诺逆变器', '序列号A', 'SNA', 'COLLECT', NULL, NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'SNA');
INSERT INTO `device_type_index_template` VALUES ('1704036821496786946', '100440185434537986', '科诺逆变器', '序列号B', 'SNB', 'COLLECT', NULL, NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'SNB');
INSERT INTO `device_type_index_template` VALUES ('1704036821500981249', '100440185434537986', '科诺逆变器', '逆变器状态码', 'STATUS', 'COLLECT', NULL, NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'STATUS');
INSERT INTO `device_type_index_template` VALUES ('1704036821509369857', '100440185434537986', '科诺逆变器', '逆变器故障码A', 'ERRA', 'COLLECT', NULL, NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'ERRA');
INSERT INTO `device_type_index_template` VALUES ('1704036821513564162', '100440185434537986', '科诺逆变器', '逆变器故障码B', 'ERRB', 'COLLECT', NULL, NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'ERRB');
INSERT INTO `device_type_index_template` VALUES ('1704036821517758465', '100440185434537986', '科诺逆变器', '直流电压', 'DCV', 'COLLECT', 'V', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'DCV');
INSERT INTO `device_type_index_template` VALUES ('1704036821526147074', '100440185434537986', '科诺逆变器', '直流电流', 'DCI', 'COLLECT', 'A', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'DCI');
INSERT INTO `device_type_index_template` VALUES ('1704036821526147075', '100440185434537986', '科诺逆变器', '瞬时直流功率', 'PDC', 'COLLECT', 'KW', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'PDC');
INSERT INTO `device_type_index_template` VALUES ('1704036821526147076', '100440185434537986', '科诺逆变器', '累计发电时间', 'CT', 'COLLECT', 'h', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'CT');
INSERT INTO `device_type_index_template` VALUES ('1704036821526147077', '100440185434537986', '科诺逆变器', '累计发电量', 'CGL', 'COLLECT', 'KWh', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'CGL');
INSERT INTO `device_type_index_template` VALUES ('1704036821526147078', '100440185434537986', '科诺逆变器', '总有功功率', 'PW', 'COLLECT', 'KW', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'PW');
INSERT INTO `device_type_index_template` VALUES ('1704036821563895810', '100440185434537986', '科诺逆变器', '当日发电量', 'DGL', 'COLLECT', 'KWh', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'DGL');
INSERT INTO `device_type_index_template` VALUES ('1704036821576478722', '100440185434537986', '科诺逆变器', '当日发电时间', 'DT', 'COLLECT', 'h', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'DT');
INSERT INTO `device_type_index_template` VALUES ('1704036821584867329', '100440185434537986', '科诺逆变器', 'A相电压', 'UA', 'COLLECT', 'V', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'UA');
INSERT INTO `device_type_index_template` VALUES ('1704036821593255938', '100440185434537986', '科诺逆变器', 'B相电压', 'UB', 'COLLECT', 'V', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'UB');
INSERT INTO `device_type_index_template` VALUES ('1704036821597450242', '100440185434537986', '科诺逆变器', 'C相电压', 'UC', 'COLLECT', 'V', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'UC');
INSERT INTO `device_type_index_template` VALUES ('1704036821605838850', '100440185434537986', '科诺逆变器', 'AB相间电压', 'UAB', 'COLLECT', 'V', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'UAB');
INSERT INTO `device_type_index_template` VALUES ('1704036821614227457', '100440185434537986', '科诺逆变器', 'BC相间电压', 'UBC', 'COLLECT', 'V', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'UBC');
INSERT INTO `device_type_index_template` VALUES ('1704036821622616065', '100440185434537986', '科诺逆变器', 'CA相间电压', 'UCA', 'COLLECT', 'V', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'UCA');
INSERT INTO `device_type_index_template` VALUES ('1704036821631004673', '100440185434537986', '科诺逆变器', 'A相电流', 'IA', 'COLLECT', 'A', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'IA');
INSERT INTO `device_type_index_template` VALUES ('1704036821639393281', '100440185434537986', '科诺逆变器', 'B相电流', 'IB', 'COLLECT', 'A', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'IB');
INSERT INTO `device_type_index_template` VALUES ('1704036821647781890', '100440185434537986', '科诺逆变器', 'C相电流', 'IC', 'COLLECT', 'A', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'IC');
INSERT INTO `device_type_index_template` VALUES ('1704036821656170497', '100440185434537986', '科诺逆变器', 'A相有功功率', 'PA', 'COLLECT', 'KW', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'PA');
INSERT INTO `device_type_index_template` VALUES ('1704036821664559106', '100440185434537986', '科诺逆变器', 'B相有功功率', 'PB', 'COLLECT', 'KW', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'PB');
INSERT INTO `device_type_index_template` VALUES ('1704036821668753410', '100440185434537986', '科诺逆变器', 'C相有功功率', 'PC', 'COLLECT', 'KW', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'PC');
INSERT INTO `device_type_index_template` VALUES ('1704036821677142017', '100440185434537986', '科诺逆变器', '总无功功率', 'PQ', 'COLLECT', 'KVar', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'PQ');
INSERT INTO `device_type_index_template` VALUES ('1704036821685530625', '100440185434537986', '科诺逆变器', 'A相无功功率', 'PQA', 'COLLECT', 'KVar', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'PQA');
INSERT INTO `device_type_index_template` VALUES ('1704036821693919234', '100440185434537986', '科诺逆变器', 'B相无功功率', 'PQB', 'COLLECT', 'KVar', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'PQB');
INSERT INTO `device_type_index_template` VALUES ('1704036821702307841', '100440185434537986', '科诺逆变器', 'C相无功功率', 'PQC', 'COLLECT', 'KVar', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'PQC');
INSERT INTO `device_type_index_template` VALUES ('1704036821710696450', '100440185434537986', '科诺逆变器', '总视在功率', 'S', 'COLLECT', 'KVA', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'S');
INSERT INTO `device_type_index_template` VALUES ('1704036821719085058', '100440185434537986', '科诺逆变器', 'A相视在功率', 'SA', 'COLLECT', 'KVA', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'SA');
INSERT INTO `device_type_index_template` VALUES ('1704036821727473665', '100440185434537986', '科诺逆变器', 'B相视在功率', 'SB', 'COLLECT', 'KVA', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'SB');
INSERT INTO `device_type_index_template` VALUES ('1704036821735862274', '100440185434537986', '科诺逆变器', 'C相视在功率', 'SC', 'COLLECT', 'KVA', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'SC');
INSERT INTO `device_type_index_template` VALUES ('1704036821744250882', '100440185434537986', '科诺逆变器', '电网频率', 'HZ', 'COLLECT', 'Hz', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'HZ');
INSERT INTO `device_type_index_template` VALUES ('1704036821752639490', '100440185434537986', '科诺逆变器', '功率因数', 'Q', 'COLLECT', NULL, NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'Q');
INSERT INTO `device_type_index_template` VALUES ('1704036821761028098', '100440185434537986', '科诺逆变器', '机柜温度', 'TC', 'COLLECT', '℃', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'TC');
INSERT INTO `device_type_index_template` VALUES ('1704036821769416705', '100440185434537986', '科诺逆变器', '累积量', 'Acc', 'CALCULATE', 'KWh', NULL, '2023-09-19 15:36:52', '华控管理员', '2023-09-19 15:36:52', '华控管理员', 'Acc');
INSERT INTO `device_type_index_template` VALUES ('1704036975218028545', '100440185434537987', '电表', '反向有功总电能量', 'PR', 'COLLECT', 'KWh', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'PR');
INSERT INTO `device_type_index_template` VALUES ('1704036975226417154', '100440185434537987', '电表', '反向无功总电能量', 'QR', 'COLLECT', 'KWh', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'QR');
INSERT INTO `device_type_index_template` VALUES ('1704036975234805761', '100440185434537987', '电表', 'A相电压', 'UA', 'COLLECT', 'V', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'UA');
INSERT INTO `device_type_index_template` VALUES ('1704036975239000066', '100440185434537987', '电表', 'B相电压', 'UB', 'COLLECT', 'V', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'UB');
INSERT INTO `device_type_index_template` VALUES ('1704036975247388673', '100440185434537987', '电表', 'C相电压', 'UC', 'COLLECT', 'V', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'UC');
INSERT INTO `device_type_index_template` VALUES ('1704036975251582978', '100440185434537987', '电表', 'AB线电压', 'UAB', 'COLLECT', 'V', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'UAB');
INSERT INTO `device_type_index_template` VALUES ('1704036975259971585', '100440185434537987', '电表', 'BC线电压', 'UBC', 'COLLECT', 'V', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'UBC');
INSERT INTO `device_type_index_template` VALUES ('1704036975264165889', '100440185434537987', '电表', 'CA线电压', 'UCA', 'COLLECT', 'V', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'UCA');
INSERT INTO `device_type_index_template` VALUES ('1704036975272554497', '100440185434537987', '电表', 'A相电流', 'IA', 'COLLECT', 'A', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'IA');
INSERT INTO `device_type_index_template` VALUES ('1704036975276748802', '100440185434537987', '电表', 'B相电流', 'IB', 'COLLECT', 'A', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'IB');
INSERT INTO `device_type_index_template` VALUES ('1704036975285137410', '100440185434537987', '电表', 'C相电流', 'IC', 'COLLECT', 'A', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'IC');
INSERT INTO `device_type_index_template` VALUES ('1704036975289331714', '100440185434537987', '电表', '总有功功率', 'PW', 'COLLECT', 'W', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'PW');
INSERT INTO `device_type_index_template` VALUES ('1704036975293526017', '100440185434537987', '电表', 'A相有功功率', 'PWA', 'COLLECT', 'W', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'PWA');
INSERT INTO `device_type_index_template` VALUES ('1704036975306108930', '100440185434537987', '电表', 'B相有功功率', 'PWB', 'COLLECT', 'W', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'PWB');
INSERT INTO `device_type_index_template` VALUES ('1704036975310303233', '100440185434537987', '电表', 'C相有功功率', 'PWC', 'COLLECT', 'W', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'PWC');
INSERT INTO `device_type_index_template` VALUES ('1704036975318691842', '100440185434537987', '电表', '总无功功率', 'PQ', 'COLLECT', 'W', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'PQ');
INSERT INTO `device_type_index_template` VALUES ('1704036975327080449', '100440185434537987', '电表', 'A相无功功率', 'PQA', 'COLLECT', 'W', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'PQA');
INSERT INTO `device_type_index_template` VALUES ('1704036975331274753', '100440185434537987', '电表', 'B相无功功率', 'PQB', 'COLLECT', 'W', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'PQB');
INSERT INTO `device_type_index_template` VALUES ('1704036975339663362', '100440185434537987', '电表', 'C相无功功率', 'PQC', 'COLLECT', 'W', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'PQC');
INSERT INTO `device_type_index_template` VALUES ('1704036975339663363', '100440185434537987', '电表', '总功率因数', 'Q', 'COLLECT', '', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'Q');
INSERT INTO `device_type_index_template` VALUES ('1704036975352246273', '100440185434537987', '电表', 'A相功率因数', 'QA', 'COLLECT', '', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'QA');
INSERT INTO `device_type_index_template` VALUES ('1704036975360634881', '100440185434537987', '电表', 'B相功率因数', 'QB', 'COLLECT', '', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'QB');
INSERT INTO `device_type_index_template` VALUES ('1704036975364829186', '100440185434537987', '电表', 'C相功率因数', 'QC', 'COLLECT', '', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'QC');
INSERT INTO `device_type_index_template` VALUES ('1704036975373217793', '100440185434537987', '电表', '视在功率', 'S', 'COLLECT', 'KVA', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'S');
INSERT INTO `device_type_index_template` VALUES ('1704036975377412097', '100440185434537987', '电表', 'A相视在功率', 'SA', 'COLLECT', 'KVA', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'SA');
INSERT INTO `device_type_index_template` VALUES ('1704036975381606401', '100440185434537987', '电表', 'B相视在功率', 'SB', 'COLLECT', 'KVA', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'SB');
INSERT INTO `device_type_index_template` VALUES ('1704036975389995009', '100440185434537987', '电表', 'C相视在功率', 'SC', 'COLLECT', 'KVA', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'SC');
INSERT INTO `device_type_index_template` VALUES ('1704036975398383618', '100440185434537987', '电表', '正向有功总电能量', 'PT', 'COLLECT', 'KWh', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'PT');
INSERT INTO `device_type_index_template` VALUES ('1704036975402577922', '100440185434537987', '电表', '正向无功总电能量', 'QP', 'COLLECT', 'KWh', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'QP');
INSERT INTO `device_type_index_template` VALUES ('1704036975410966530', '100440185434537987', '电表', '累积量', 'Acc', 'CALCULATE', 'KWh', NULL, '2023-09-19 15:37:29', '华控管理员', '2023-09-19 15:37:29', '华控管理员', 'Acc');

-- ----------------------------
-- Table structure for device_type_template
-- ----------------------------
DROP TABLE IF EXISTS `device_type_template`;
CREATE TABLE `device_type_template`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备类型名称',
  `description` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备描述',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '设备类型管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device_type_template
-- ----------------------------
INSERT INTO `device_type_template` VALUES ('1696350911182561281', '逆变器001', '逆变器001', NULL, '2023-08-29 10:35:49', NULL, '2023-08-29 10:35:49', NULL);
INSERT INTO `device_type_template` VALUES ('1697486675849203713', '海科电表', NULL, NULL, '2023-09-01 13:48:56', '若依', '2023-09-01 13:48:56', '若依');

-- ----------------------------
-- Table structure for electricity_data_item
-- ----------------------------
DROP TABLE IF EXISTS `electricity_data_item`;
CREATE TABLE `electricity_data_item`  (
  `device_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备id',
  `data_time` datetime NOT NULL COMMENT '时间具体到小时',
  `type` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用电类型峰、平、谷等',
  `begin_time` datetime NULL DEFAULT NULL COMMENT '数据开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '数据结束时间',
  `value` decimal(18, 4) NULL DEFAULT NULL COMMENT '用电量',
  `cost` decimal(18, 4) NULL DEFAULT NULL COMMENT '电费',
  `price` decimal(18, 4) NULL DEFAULT NULL COMMENT '单价',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL,
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`data_time`, `type`, `device_id`) USING BTREE,
  INDEX `idx1`(`type` ASC) USING BTREE,
  INDEX `idx2`(`data_time` ASC) USING BTREE,
  INDEX `idx_device_id_data_time`(`device_id` ASC, `data_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '电力-峰平谷数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for electricity_type_setting
-- ----------------------------
DROP TABLE IF EXISTS `electricity_type_setting`;
CREATE TABLE `electricity_type_setting`  (
  `id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `begin_time` date NOT NULL COMMENT '开始时间',
  `end_time` date NOT NULL COMMENT '截止时间',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '峰平谷配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of electricity_type_setting
-- ----------------------------
INSERT INTO `electricity_type_setting` VALUES ('1696395770826039298', '2024-01-01', '2024-02-29', '冬季', '2023-08-29 13:34:04', '若依', '2024-01-18 16:38:54', '若依');
INSERT INTO `electricity_type_setting` VALUES ('1696395821686169601', '2024-03-01', '2024-05-31', '春季', '2023-08-29 13:34:16', '若依', '2024-01-03 14:11:45', '若依');
INSERT INTO `electricity_type_setting` VALUES ('1699710295979847682', '2024-06-01', '2024-08-31', '夏季', '2023-09-07 17:04:48', '若依', '2024-01-03 14:12:54', '若依');
INSERT INTO `electricity_type_setting` VALUES ('1704023587389730818', '2024-09-01', '2024-11-30', '秋季', '2023-09-19 14:44:17', '电站运维人员', '2024-01-03 14:13:12', '电站运维人员');
INSERT INTO `electricity_type_setting` VALUES ('1704024611655544834', '2024-12-01', '2024-12-31', '冬季', '2023-09-19 14:48:21', '电站运维人员', '2024-01-03 14:12:37', '电站运维人员');
INSERT INTO `electricity_type_setting` VALUES ('1881257338178912258', '2025-01-20', '2025-01-31', '春节期间尖峰平谷配置', '2025-01-20 16:27:56', '管理员', '2025-01-20 16:27:56', '管理员');
INSERT INTO `electricity_type_setting` VALUES ('1892412101188694017', '2025-02-01', '2025-12-31', '2025年尖峰平谷', '2025-02-20 11:12:59', '管理员', '2025-02-20 13:45:10', '管理员');

-- ----------------------------
-- Table structure for electricity_type_setting_item
-- ----------------------------
DROP TABLE IF EXISTS `electricity_type_setting_item`;
CREATE TABLE `electricity_type_setting_item`  (
  `id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `parent_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '父级id',
  `type` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用电类型（尖、峰、平、谷）',
  `begin_time` time NOT NULL COMMENT '开始时间',
  `end_time` time NOT NULL COMMENT '截止时间',
  `electricity_price` decimal(12, 2) NOT NULL COMMENT '电价',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '峰平谷配置子项' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of electricity_type_setting_item
-- ----------------------------
INSERT INTO `electricity_type_setting_item` VALUES ('1696400906252062722', '1696395821686169601', 'flat', '00:00:00', '10:00:00', 1.00, '', '2024-01-03 14:22:27', '华控管理员', '2024-01-03 14:22:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1696416553386573825', '1696407985941839873', 'tip', '00:00:00', '01:00:00', 1.00, NULL, '2023-08-29 14:56:39', '若依', '2023-08-29 14:56:39', '若依');
INSERT INTO `electricity_type_setting_item` VALUES ('1699710804639870978', '1699710295979847682', 'trough', '00:00:00', '06:00:00', 1.00, '', '2024-01-03 14:26:30', '华控管理员', '2024-01-03 14:26:30', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704021032760799234', '1696395770826039298', 'flat', '00:00:00', '10:00:00', 1.00, NULL, '2024-01-03 14:19:29', '华控管理员', '2024-01-03 14:19:29', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704021032844685313', '1696395770826039298', 'trough', '10:00:00', '11:00:00', 1.00, NULL, '2024-01-03 14:19:29', '华控管理员', '2024-01-03 14:19:29', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704021032861462530', '1696395770826039298', 'deep', '11:00:00', '14:00:00', 1.00, NULL, '2024-01-03 14:19:29', '华控管理员', '2024-01-03 14:19:29', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704021032869851138', '1696395770826039298', 'trough', '14:00:00', '15:00:00', 1.00, NULL, '2024-01-03 14:19:29', '华控管理员', '2024-01-03 14:19:29', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704021032878239745', '1696395770826039298', 'flat', '15:00:00', '16:00:00', 1.00, NULL, '2024-01-03 14:19:29', '华控管理员', '2024-01-03 14:19:29', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704021032890822658', '1696395770826039298', 'tip', '16:00:00', '19:00:00', 1.00, NULL, '2024-01-03 14:19:29', '华控管理员', '2024-01-03 14:19:29', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704021032899211266', '1696395770826039298', 'peak', '19:00:00', '21:00:00', 1.00, NULL, '2024-01-03 14:19:29', '华控管理员', '2024-01-03 14:19:29', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704022388246605826', '1696395821686169601', 'trough', '10:00:00', '11:00:00', 1.00, NULL, '2024-01-03 14:22:27', '华控管理员', '2024-01-03 14:22:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704022388254994434', '1696395821686169601', 'deep', '11:00:00', '14:00:00', 1.00, NULL, '2024-01-03 14:22:27', '华控管理员', '2024-01-03 14:22:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704022388263383041', '1696395821686169601', 'trough', '14:00:00', '15:00:00', 1.00, NULL, '2024-01-03 14:22:27', '华控管理员', '2024-01-03 14:22:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704022388271771650', '1696395821686169601', 'flat', '15:00:00', '17:00:00', 1.00, NULL, '2024-01-03 14:22:27', '华控管理员', '2024-01-03 14:22:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704022388280160258', '1696395821686169601', 'tip', '17:00:00', '20:00:00', 1.00, NULL, '2024-01-03 14:22:27', '华控管理员', '2024-01-03 14:22:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704022388296937473', '1696395821686169601', 'peak', '20:00:00', '22:00:00', 1.00, NULL, '2024-01-03 14:22:27', '华控管理员', '2024-01-03 14:22:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704022388305326081', '1696395821686169601', 'flat', '22:00:00', '00:00:00', 1.00, NULL, '2024-01-03 14:22:27', '华控管理员', '2024-01-03 14:22:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704023462139424769', '1699710295979847682', 'flat', '06:00:00', '16:00:00', 1.00, NULL, '2024-01-03 14:26:30', '华控管理员', '2024-01-03 14:26:30', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704023462152007681', '1699710295979847682', 'peak', '16:00:00', '17:00:00', 1.00, NULL, '2024-01-03 14:26:30', '华控管理员', '2024-01-03 14:26:30', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704023462156201985', '1699710295979847682', 'tip', '17:00:00', '22:00:00', 1.00, NULL, '2024-01-03 14:26:30', '华控管理员', '2024-01-03 14:26:30', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704023462164590593', '1699710295979847682', 'flat', '22:00:00', '00:00:00', 1.00, NULL, '2024-01-03 14:26:30', '华控管理员', '2024-01-03 14:26:30', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024506630172673', '1704023587389730818', 'flat', '00:00:00', '10:00:00', 1.00, NULL, '2024-01-03 14:28:35', '华控管理员', '2024-01-03 14:28:35', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024506638561282', '1704023587389730818', 'trough', '10:00:00', '11:00:00', 1.00, NULL, '2024-01-03 14:28:35', '华控管理员', '2024-01-03 14:28:35', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024506642755586', '1704023587389730818', 'deep', '11:00:00', '14:00:00', 1.00, NULL, '2024-01-03 14:28:35', '华控管理员', '2024-01-03 14:28:35', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024506651144193', '1704023587389730818', 'trough', '14:00:00', '15:00:00', 1.00, NULL, '2024-01-03 14:28:35', '华控管理员', '2024-01-03 14:28:35', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024506663727105', '1704023587389730818', 'flat', '15:00:00', '16:00:00', 1.00, NULL, '2024-01-03 14:28:35', '华控管理员', '2024-01-03 14:28:35', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024506676310018', '1704023587389730818', 'peak', '16:00:00', '17:00:00', 1.00, NULL, '2024-01-03 14:28:35', '华控管理员', '2024-01-03 14:28:35', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024506684698625', '1704023587389730818', 'tip', '17:00:00', '19:00:00', 1.00, NULL, '2024-01-03 14:28:35', '华控管理员', '2024-01-03 14:28:35', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024506697281537', '1704023587389730818', 'peak', '19:00:00', '21:00:00', 1.00, NULL, '2024-01-03 14:28:35', '华控管理员', '2024-01-03 14:28:35', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024506709864450', '1704023587389730818', 'flat', '21:00:00', '00:00:00', 1.00, NULL, '2024-01-03 14:28:35', '华控管理员', '2024-01-03 14:28:35', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024962139975681', '1704024611655544834', 'flat', '00:00:00', '10:00:00', 1.00, NULL, '2024-01-03 14:36:27', '华控管理员', '2024-01-03 14:36:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024962148364289', '1704024611655544834', 'trough', '10:00:00', '11:00:00', 1.00, NULL, '2024-01-03 14:36:27', '华控管理员', '2024-01-03 14:36:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024962152558594', '1704024611655544834', 'deep', '11:00:00', '14:00:00', 1.00, NULL, '2024-01-03 14:36:27', '华控管理员', '2024-01-03 14:36:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024962160947201', '1704024611655544834', 'trough', '14:00:00', '15:00:00', 1.00, NULL, '2024-01-03 14:36:27', '华控管理员', '2024-01-03 14:36:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024962165141506', '1704024611655544834', 'flat', '15:00:00', '16:00:00', 1.00, NULL, '2024-01-03 14:36:27', '华控管理员', '2024-01-03 14:36:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024962169335809', '1704024611655544834', 'tip', '16:00:00', '19:00:00', 1.00, NULL, '2024-01-03 14:36:27', '华控管理员', '2024-01-03 14:36:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1704024962169335812', '1704024611655544834', 'peak', '19:00:00', '21:00:00', 1.00, NULL, '2024-01-03 14:36:27', '华控管理员', '2024-01-03 14:36:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1742430459431780354', '1696395770826039298', 'flat', '21:00:00', '00:00:00', 1.00, NULL, '2024-01-03 14:19:29', '华控管理员', '2024-01-03 14:19:29', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1742434729203892226', '1704024611655544834', 'flat', '21:00:00', '00:00:00', 1.00, NULL, '2024-01-03 14:36:27', '华控管理员', '2024-01-03 14:36:27', '华控管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1881258069887827970', '1881257338178912258', 'tip', '08:00:00', '22:00:00', 1.00, NULL, '2025-01-20 16:30:51', '管理员', '2025-01-20 16:30:51', '管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1881258069896216578', '1881257338178912258', 'peak', '22:00:00', '00:00:00', 0.80, NULL, '2025-01-20 16:30:51', '管理员', '2025-01-20 16:30:51', '管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1881258069904605186', '1881257338178912258', 'flat', '00:00:00', '02:00:00', 0.60, NULL, '2025-01-20 16:30:51', '管理员', '2025-01-20 16:30:51', '管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1881258069912993794', '1881257338178912258', 'trough', '02:00:00', '08:00:00', 0.40, NULL, '2025-01-20 16:30:51', '管理员', '2025-01-20 16:30:51', '管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1892453000769781762', '1892412101188694017', 'trough', '00:00:00', '03:00:00', 0.35, NULL, '2025-02-20 14:00:14', '管理员', '2025-02-20 14:00:14', '管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1892453000807530497', '1892412101188694017', 'flat', '03:00:00', '08:00:00', 0.70, NULL, '2025-02-20 14:00:14', '管理员', '2025-02-20 14:00:14', '管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1892453000832696321', '1892412101188694017', 'peak', '08:00:00', '10:00:00', 1.00, NULL, '2025-02-20 14:00:14', '管理员', '2025-02-20 14:00:14', '管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1892454190886764546', '1892412101188694017', 'tip', '10:00:00', '14:00:00', 1.30, NULL, '2025-02-20 14:00:14', '管理员', '2025-02-20 14:00:14', '管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1892454190907736065', '1892412101188694017', 'peak', '14:00:00', '16:00:00', 1.00, NULL, '2025-02-20 14:00:14', '管理员', '2025-02-20 14:00:14', '管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1892454190924513281', '1892412101188694017', 'flat', '16:00:00', '22:00:00', 0.70, NULL, '2025-02-20 14:00:14', '管理员', '2025-02-20 14:00:14', '管理员');
INSERT INTO `electricity_type_setting_item` VALUES ('1892454190941290498', '1892412101188694017', 'trough', '22:00:00', '00:00:00', 0.35, NULL, '2025-02-20 14:00:14', '管理员', '2025-02-20 14:00:14', '管理员');

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (1, 'data_item', '', NULL, NULL, 'DataItem', 'crud', 'com.ruoyi.system', 'system', 'item', NULL, 'ruoyi', '0', '/', NULL, 'admin', '2023-08-23 14:23:53', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (2, 'electricity_data_item', '电力-峰平谷数据', NULL, NULL, 'ElectricityDataItem', 'crud', 'com.ruoyi.system', 'system', 'item', '电力-峰平谷数据', 'ruoyi', '0', '/', NULL, 'admin', '2023-08-23 14:23:55', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (3, 'energy_index_template', '平台能源折标模板；平台预置的 指标模板，不提供租户给，下面预设的采集点、计算点信息', NULL, NULL, 'EnergyIndexTemplate', 'crud', 'com.ruoyi.system', 'system', 'template', '平台能源折标模板；平台预置的 指标模板，不提供租户给，下面预设的采集点、计算点信息', 'ruoyi', '0', '/', NULL, 'admin', '2023-08-23 14:23:57', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (4, 'alarm', '报警表', NULL, NULL, 'Alarm', 'crud', 'com.ruoyi.system', 'system', 'alarm', '报警', 'ruoyi', '0', '/', NULL, 'admin', '2023-08-24 10:39:59', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (5, 'device', '设备管理表', NULL, NULL, 'Device', 'crud', 'com.ruoyi.system', 'system', 'device', '设备管理', 'ruoyi', '0', '/', NULL, 'admin', '2023-08-24 10:39:59', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (6, 'device_index', '设备与点位关系表', NULL, NULL, 'DeviceIndex', 'crud', 'com.ruoyi.system', 'system', 'index', '设备与点位关系', 'ruoyi', '0', '/', NULL, 'admin', '2023-08-24 10:39:59', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (7, 'device_inspection', '设备点检', NULL, NULL, 'DeviceInspection', 'crud', 'com.ruoyi.system', 'system', 'inspection', '设备点检', 'ruoyi', '0', '/', NULL, 'admin', '2023-08-24 10:39:59', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (8, 'electricity_type_setting', '峰平谷配置表', NULL, NULL, 'ElectricityTypeSetting', 'crud', 'com.ruoyi.system', 'system', 'setting', '峰平谷配置', 'ruoyi', '0', '/', NULL, 'admin', '2023-08-24 10:39:59', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (9, 'electricity_type_setting_item', '峰平谷配置子项', NULL, NULL, 'ElectricityTypeSettingItem', 'crud', 'com.ruoyi.system', 'system', 'item', '峰平谷配置子项', 'ruoyi', '0', '/', NULL, 'admin', '2023-08-24 10:39:59', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (10, 'power_station', '电站维护表', NULL, NULL, 'PowerStation', 'crud', 'com.ruoyi.system', 'system', 'station', '电站维护', 'ruoyi', '0', '/', NULL, 'admin', '2023-08-24 10:39:59', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (11, 'spare_parts', '备品备件', NULL, NULL, 'SpareParts', 'crud', 'com.ruoyi.system', 'system', 'parts', '备品备件', 'ruoyi', '0', '/', NULL, 'admin', '2023-08-31 16:16:26', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (12, 'spare_parts_record', '备品备件-操作记录表', NULL, NULL, 'SparePartsRecord', 'crud', 'com.ruoyi.system', 'system', 'record', '备品备件-操作记录', 'ruoyi', '0', '/', NULL, 'admin', '2023-08-31 16:16:26', '', NULL, NULL);

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 136 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (1, 1, 'device_id', '计量器具id', 'varchar(36)', 'String', 'deviceId', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2023-08-23 14:23:53', '', NULL);
INSERT INTO `gen_table_column` VALUES (2, 1, 'time_code', '时间编码', 'varchar(20)', 'String', 'timeCode', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 2, 'admin', '2023-08-23 14:23:54', '', NULL);
INSERT INTO `gen_table_column` VALUES (3, 1, 'time_type', '时间类型（HOUR、DAY、MONTH、YEAR）', 'varchar(10)', 'String', 'timeType', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'select', '', 3, 'admin', '2023-08-23 14:23:54', '', NULL);
INSERT INTO `gen_table_column` VALUES (4, 1, 'data_time', '业务时间', 'datetime', 'Date', 'dataTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 4, 'admin', '2023-08-23 14:23:54', '', NULL);
INSERT INTO `gen_table_column` VALUES (5, 1, 'begin_time', '开始时间', 'datetime', 'Date', 'beginTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 5, 'admin', '2023-08-23 14:23:54', '', NULL);
INSERT INTO `gen_table_column` VALUES (6, 1, 'end_time', '截止时间', 'datetime', 'Date', 'endTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 6, 'admin', '2023-08-23 14:23:54', '', NULL);
INSERT INTO `gen_table_column` VALUES (7, 1, 'value', '值', 'double', 'Long', 'value', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2023-08-23 14:23:54', '', NULL);
INSERT INTO `gen_table_column` VALUES (8, 1, 'remark', '备注', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 8, 'admin', '2023-08-23 14:23:54', '', NULL);
INSERT INTO `gen_table_column` VALUES (9, 1, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2023-08-23 14:23:54', '', NULL);
INSERT INTO `gen_table_column` VALUES (10, 1, 'create_by', '创建人', 'varchar(255)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 10, 'admin', '2023-08-23 14:23:55', '', NULL);
INSERT INTO `gen_table_column` VALUES (11, 1, 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 11, 'admin', '2023-08-23 14:23:55', '', NULL);
INSERT INTO `gen_table_column` VALUES (12, 1, 'update_by', '修改人', 'varchar(36)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 12, 'admin', '2023-08-23 14:23:55', '', NULL);
INSERT INTO `gen_table_column` VALUES (13, 2, 'device_id', '设备id', 'varchar(32)', 'String', 'deviceId', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2023-08-23 14:23:55', '', NULL);
INSERT INTO `gen_table_column` VALUES (14, 2, 'data_time', '时间具体到小时', 'datetime', 'Date', 'dataTime', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 2, 'admin', '2023-08-23 14:23:55', '', NULL);
INSERT INTO `gen_table_column` VALUES (15, 2, 'type', '用电类型峰、平、谷等', 'varchar(10)', 'String', 'type', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'select', '', 3, 'admin', '2023-08-23 14:23:55', '', NULL);
INSERT INTO `gen_table_column` VALUES (16, 2, 'begin_time', '数据开始时间', 'datetime', 'Date', 'beginTime', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 4, 'admin', '2023-08-23 14:23:55', '', NULL);
INSERT INTO `gen_table_column` VALUES (17, 2, 'end_time', '数据结束时间', 'datetime', 'Date', 'endTime', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 5, 'admin', '2023-08-23 14:23:56', '', NULL);
INSERT INTO `gen_table_column` VALUES (18, 2, 'electricity', '用电量', 'decimal(18,4)', 'BigDecimal', 'electricity', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2023-08-23 14:23:56', '', NULL);
INSERT INTO `gen_table_column` VALUES (19, 2, 'cost', '电费', 'decimal(18,4)', 'BigDecimal', 'cost', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2023-08-23 14:23:56', '', NULL);
INSERT INTO `gen_table_column` VALUES (20, 2, 'price', '单价', 'decimal(18,4)', 'BigDecimal', 'price', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2023-08-23 14:23:56', '', NULL);
INSERT INTO `gen_table_column` VALUES (21, 2, 'remark', '备注', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 9, 'admin', '2023-08-23 14:23:56', '', NULL);
INSERT INTO `gen_table_column` VALUES (22, 2, 'create_time', NULL, 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2023-08-23 14:23:56', '', NULL);
INSERT INTO `gen_table_column` VALUES (23, 2, 'create_by', '创建人', 'varchar(255)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 11, 'admin', '2023-08-23 14:23:56', '', NULL);
INSERT INTO `gen_table_column` VALUES (24, 2, 'update_time', NULL, 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 12, 'admin', '2023-08-23 14:23:56', '', NULL);
INSERT INTO `gen_table_column` VALUES (25, 2, 'update_by', '修改人', 'varchar(36)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 13, 'admin', '2023-08-23 14:23:56', '', NULL);
INSERT INTO `gen_table_column` VALUES (26, 3, 'id', '主键', 'varchar(36)', 'String', 'id', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2023-08-23 14:23:57', '', NULL);
INSERT INTO `gen_table_column` VALUES (27, 3, 'name', '模板点位名称', 'varchar(20)', 'String', 'name', '0', '0', NULL, '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2023-08-23 14:23:57', '', NULL);
INSERT INTO `gen_table_column` VALUES (28, 3, 'code', '模板点位编码', 'varchar(50)', 'String', 'code', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2023-08-23 14:23:57', '', NULL);
INSERT INTO `gen_table_column` VALUES (29, 3, 'type', '表具类型（电表、逆变器）', 'varchar(20)', 'String', 'type', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'select', '', 4, 'admin', '2023-08-23 14:23:57', '', NULL);
INSERT INTO `gen_table_column` VALUES (30, 3, 'index_type', '点位类型（采集点 -COLLECT、计算点-CALCULATE）', 'varchar(10)', 'String', 'indexType', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'select', '', 5, 'admin', '2023-08-23 14:23:57', '', NULL);
INSERT INTO `gen_table_column` VALUES (31, 3, 'calc_index', '计算点公式（就是计算指标的累计量所需的采集点指标编码 indexTemplate.code)，这里有一个存储规则[富经理给] 这里会带到 energyIndex.calcText', 'varchar(100)', 'String', 'calcIndex', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2023-08-23 14:23:57', '', NULL);
INSERT INTO `gen_table_column` VALUES (32, 3, 'calc_type', '计算点类型（0:累积量，1综合能耗，2能源价格）', 'tinyint', 'Long', 'calcType', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'select', '', 7, 'admin', '2023-08-23 14:23:58', '', NULL);
INSERT INTO `gen_table_column` VALUES (33, 3, 'unit', '计量单位', 'varchar(10)', 'String', 'unit', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2023-08-23 14:23:58', '', NULL);
INSERT INTO `gen_table_column` VALUES (34, 3, 'remark', '备注', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 9, 'admin', '2023-08-23 14:23:58', '', NULL);
INSERT INTO `gen_table_column` VALUES (35, 3, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2023-08-23 14:23:58', '', NULL);
INSERT INTO `gen_table_column` VALUES (36, 3, 'create_by', '创建人', 'varchar(255)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 11, 'admin', '2023-08-23 14:23:58', '', NULL);
INSERT INTO `gen_table_column` VALUES (37, 3, 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 12, 'admin', '2023-08-23 14:23:58', '', NULL);
INSERT INTO `gen_table_column` VALUES (38, 3, 'update_by', '修改人', 'varchar(36)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 13, 'admin', '2023-08-23 14:23:58', '', NULL);
INSERT INTO `gen_table_column` VALUES (39, 4, 'id', NULL, 'varchar(36)', 'String', 'id', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (40, 4, 'data_time', '发生时间', 'datetime', 'Date', 'dataTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 2, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (41, 4, 'err_code', '错误码', 'varchar(36)', 'String', 'errCode', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (42, 4, 'error_description', '错误描述', 'varchar(64)', 'String', 'errorDescription', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (43, 4, 'solution', '建议解决方案', 'varchar(255)', 'String', 'solution', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (44, 4, 'status', '状态（1未解决、2已解决）', 'char(1)', 'String', 'status', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'radio', '', 6, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (45, 4, 'handlers', '处理人', 'varchar(64)', 'String', 'handlers', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (46, 4, 'processing_time', '处理时间', 'datetime', 'Date', 'processingTime', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'datetime', '', 8, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (47, 4, 'remark', '备注', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 9, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (48, 4, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (49, 4, 'create_by', '创建人', 'varchar(255)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 11, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (50, 4, 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 12, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (51, 4, 'update_by', '修改人', 'varchar(36)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 13, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (52, 5, 'id', NULL, 'varchar(36)', 'String', 'id', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (53, 5, 'code', '设备编号', 'varchar(64)', 'String', 'code', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (54, 5, 'name', '设备名称', 'varchar(64)', 'String', 'name', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (55, 5, 'type', '器具类型（1:逆变器、2:电表、3:反送表）', 'char(1)', 'String', 'type', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'select', '', 4, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (56, 5, 'capacity', '容量', 'float(12,2)', 'BigDecimal', 'capacity', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (57, 5, 'factory', '厂家', 'varchar(64)', 'String', 'factory', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (58, 5, 'rated_ac_power', '额定交流功率', 'float(12,2)', 'BigDecimal', 'ratedAcPower', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (59, 5, 'grid_type', '电网类型', 'varchar(64)', 'String', 'gridType', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'select', '', 8, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (60, 5, 'module_peak_power', '组件峰值功率', 'float(12,2)', 'BigDecimal', 'modulePeakPower', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (61, 5, 'remark', '备注', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 10, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (62, 5, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 11, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (63, 5, 'create_by', '创建人', 'varchar(255)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 12, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (64, 5, 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 13, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (65, 5, 'update_by', '修改人', 'varchar(36)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 14, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (66, 6, 'index_id', '点位id', 'varchar(36)', 'String', 'indexId', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (67, 6, 'device_id', '设备id', 'varchar(36)', 'String', 'deviceId', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 2, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (68, 6, 'index_type', '点位类型（采集点 -COLLECT、计算点-CALCULATE）', 'varchar(12)', 'String', 'indexType', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'select', '', 3, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (69, 6, 'remark', '备注', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 4, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (70, 6, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 5, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (71, 6, 'create_by', '创建人', 'varchar(255)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (72, 6, 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (73, 6, 'update_by', '修改人', 'varchar(36)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (74, 7, 'id', NULL, 'varchar(36)', 'String', 'id', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (75, 7, 'device_code', '设备编号', 'varchar(36)', 'String', 'deviceCode', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (76, 7, 'device_name', '设备名称', 'varchar(255)', 'String', 'deviceName', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (77, 7, 'inspection_time', '点检日期', 'datetime', 'Date', 'inspectionTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 4, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (78, 7, 'inspection_staff', '点检人员', 'varchar(64)', 'String', 'inspectionStaff', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (79, 7, 'inspection_result', '点检结果', 'varchar(64)', 'String', 'inspectionResult', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (80, 7, 'remark', '备注', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 7, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (81, 7, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (82, 7, 'create_by', '创建人', 'varchar(255)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (83, 7, 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (84, 7, 'update_by', '修改人', 'varchar(36)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 11, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (85, 8, 'id', NULL, 'varchar(36)', 'String', 'id', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (86, 8, 'begin_time', '开始时间', 'date', 'Date', 'beginTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 2, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (87, 8, 'end_time', '截止时间', 'date', 'Date', 'endTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 3, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (88, 8, 'remark', '备注', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 4, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (89, 8, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 5, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (90, 8, 'create_by', '创建人', 'varchar(255)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (91, 8, 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (92, 8, 'update_by', '修改人', 'varchar(36)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (93, 9, 'id', NULL, 'varchar(36)', 'String', 'id', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (94, 9, 'paremt_id', '父级id', 'varchar(36)', 'String', 'paremtId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (95, 9, 'type', '用电类型（尖、峰、平、谷）', 'varchar(36)', 'String', 'type', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'select', '', 3, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (96, 9, 'begin_time', '开始时间', 'datetime', 'Date', 'beginTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 4, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (97, 9, 'end_time', '截止时间', 'datetime', 'Date', 'endTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 5, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (98, 9, 'electricity_price', '电价', 'decimal(12,2)', 'BigDecimal', 'electricityPrice', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (99, 9, 'remark', '备注', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 7, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (100, 9, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (101, 9, 'create_by', '创建人', 'varchar(255)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (102, 9, 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (103, 9, 'update_by', '修改人', 'varchar(36)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 11, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (104, 10, 'id', NULL, 'varchar(36)', 'String', 'id', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (105, 10, 'code', '编号', 'varchar(32)', 'String', 'code', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (106, 10, 'name', '名称', 'varchar(64)', 'String', 'name', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (107, 10, 'subsidized_prices', '补贴电价', 'decimal(12,2)', 'BigDecimal', 'subsidizedPrices', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (108, 10, 'installed_capacity', '电站装机容量', 'float(12,2)', 'BigDecimal', 'installedCapacity', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (109, 10, 'grid_voltage', '并网电压', 'float(12,2)', 'BigDecimal', 'gridVoltage', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (110, 10, 'remark', '备注', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 7, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (111, 10, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (112, 10, 'create_by', '创建人', 'varchar(255)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (113, 10, 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (114, 10, 'update_by', '修改人', 'varchar(36)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 11, 'admin', '2023-08-24 10:39:59', '', NULL);
INSERT INTO `gen_table_column` VALUES (115, 11, 'id', NULL, 'varchar(36)', 'String', 'id', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2023-08-31 16:16:26', '', NULL);
INSERT INTO `gen_table_column` VALUES (116, 11, 'code', '编号', 'varchar(64)', 'String', 'code', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2023-08-31 16:16:26', '', NULL);
INSERT INTO `gen_table_column` VALUES (117, 11, 'name', '名称', 'varchar(64)', 'String', 'name', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2023-08-31 16:16:26', '', NULL);
INSERT INTO `gen_table_column` VALUES (118, 11, 'specs', '规格型号', 'varchar(64)', 'String', 'specs', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2023-08-31 16:16:26', '', NULL);
INSERT INTO `gen_table_column` VALUES (119, 11, 'amount', '库存数量', 'int', 'Long', 'amount', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2023-08-31 16:16:26', '', NULL);
INSERT INTO `gen_table_column` VALUES (120, 11, 'remark', '备注', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 6, 'admin', '2023-08-31 16:16:26', '', NULL);
INSERT INTO `gen_table_column` VALUES (121, 11, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2023-08-31 16:16:26', '', NULL);
INSERT INTO `gen_table_column` VALUES (122, 11, 'create_by', '创建人', 'varchar(255)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2023-08-31 16:16:26', '', NULL);
INSERT INTO `gen_table_column` VALUES (123, 11, 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2023-08-31 16:16:26', '', NULL);
INSERT INTO `gen_table_column` VALUES (124, 11, 'update_by', '修改人', 'varchar(36)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 10, 'admin', '2023-08-31 16:16:26', '', NULL);
INSERT INTO `gen_table_column` VALUES (125, 12, 'id', NULL, 'varchar(36)', 'String', 'id', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2023-08-31 16:16:26', '', NULL);
INSERT INTO `gen_table_column` VALUES (126, 12, 'code', '编号', 'varchar(64)', 'String', 'code', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2023-08-31 16:16:26', '', NULL);
INSERT INTO `gen_table_column` VALUES (127, 12, 'name', '名称', 'varchar(64)', 'String', 'name', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2023-08-31 16:16:26', '', NULL);
INSERT INTO `gen_table_column` VALUES (128, 12, 'specs', '规格型号', 'varchar(64)', 'String', 'specs', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2023-08-31 16:16:27', '', NULL);
INSERT INTO `gen_table_column` VALUES (129, 12, 'opera_amount', '操作数量', 'int', 'Long', 'operaAmount', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2023-08-31 16:16:27', '', NULL);
INSERT INTO `gen_table_column` VALUES (130, 12, 'status', '状态（0入库，1出库）', 'char(1)', 'String', 'status', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'radio', '', 6, 'admin', '2023-08-31 16:16:27', '', NULL);
INSERT INTO `gen_table_column` VALUES (131, 12, 'remark', '备注', 'varchar(255)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 7, 'admin', '2023-08-31 16:16:27', '', NULL);
INSERT INTO `gen_table_column` VALUES (132, 12, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2023-08-31 16:16:27', '', NULL);
INSERT INTO `gen_table_column` VALUES (133, 12, 'create_by', '创建人', 'varchar(255)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2023-08-31 16:16:27', '', NULL);
INSERT INTO `gen_table_column` VALUES (134, 12, 'update_time', '修改时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2023-08-31 16:16:27', '', NULL);
INSERT INTO `gen_table_column` VALUES (135, 12, 'update_by', '修改人', 'varchar(36)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 11, 'admin', '2023-08-31 16:16:27', '', NULL);

-- ----------------------------
-- Table structure for index_template
-- ----------------------------
DROP TABLE IF EXISTS `index_template`;
CREATE TABLE `index_template`  (
  `id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '模板点位名称',
  `code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '模板点位编码',
  `device_type_id` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '表具类型id',
  `index_type` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '点位类型（采集点 -COLLECT、计算点-CALCULATE）',
  `calc_index` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '计算点公式（就是计算指标的累计量所需的采集点指标编码 indexTemplate.code)，这里有一个存储规则[富经理给] 这里会带到 energyIndex.calcText',
  `unit` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '计量单位',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '平台能源折标模板；平台预置的 指标模板，不提供租户给，下面预设的采集点、计算点信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of index_template
-- ----------------------------
INSERT INTO `index_template` VALUES ('1696356958546845697', '电压', 'vvv', '1696350911182561281', 'COLLECT', NULL, 'V', NULL, '2023-08-29 10:59:50', NULL, '2023-08-29 10:59:50', NULL);

-- ----------------------------
-- Table structure for inventory_location
-- ----------------------------
DROP TABLE IF EXISTS `inventory_location`;
CREATE TABLE `inventory_location`  (
  `id` bigint NOT NULL COMMENT '主键',
  `location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '库存地点',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of inventory_location
-- ----------------------------
INSERT INTO `inventory_location` VALUES (1736655288934612994, '仓库', NULL);
INSERT INTO `inventory_location` VALUES (1736655329069907969, '崂山', NULL);
INSERT INTO `inventory_location` VALUES (1736655358224515073, '开发区1', NULL);
INSERT INTO `inventory_location` VALUES (1736655412163264513, '胶南', NULL);
INSERT INTO `inventory_location` VALUES (1736655446686580738, '开发区2', NULL);
INSERT INTO `inventory_location` VALUES (1736655478471016450, '胶州1', NULL);
INSERT INTO `inventory_location` VALUES (1736655522649620482, '胶州仓库', NULL);
INSERT INTO `inventory_location` VALUES (1736655557802082306, '平度仓库', NULL);
INSERT INTO `inventory_location` VALUES (1736655600349102081, '城阳仓库', NULL);
INSERT INTO `inventory_location` VALUES (1796450832758087681, '仓库1', NULL);
INSERT INTO `inventory_location` VALUES (1796450912626024449, '仓库2', NULL);

-- ----------------------------
-- Table structure for power_station
-- ----------------------------
DROP TABLE IF EXISTS `power_station`;
CREATE TABLE `power_station`  (
  `id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `parent_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '父级id',
  `code` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '名称',
  `subsidized_prices` decimal(12, 2) NULL DEFAULT NULL COMMENT '补贴电价',
  `installed_capacity` float(12, 4) NULL DEFAULT 0.0000 COMMENT '电站装机容量',
  `grid_voltage` float(12, 2) NULL DEFAULT NULL COMMENT '并网电压',
  `lon` decimal(12, 4) NULL DEFAULT NULL COMMENT '经度',
  `lat` decimal(12, 4) NULL DEFAULT NULL COMMENT '纬度',
  `owning_user_id` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '所属用户id',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '电站维护表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of power_station
-- ----------------------------
INSERT INTO `power_station` VALUES ('1696341345183096833', NULL, 'DZ12', '电站12', NULL, 4.3600, 200.00, 117.7734, 26.3131, NULL, '崂山海尔备注', '2023-08-29 09:57:48', NULL, '2025-02-17 14:41:12', NULL, NULL, 204);
INSERT INTO `power_station` VALUES ('1696343684614873089', NULL, 'DZ11', '电站11', NULL, 16.5523, 200.00, 112.9175, 34.7055, NULL, '海信电站-200-200', '2023-08-29 10:07:06', NULL, '2025-02-17 14:39:26', NULL, NULL, 205);
INSERT INTO `power_station` VALUES ('1699303477713518593', NULL, 'DZ10', '电站10', NULL, 4.5030, 100.00, 109.5996, 40.9135, NULL, NULL, '2023-09-06 14:08:15', '若依', '2025-02-17 14:41:45', NULL, NULL, 201);
INSERT INTO `power_station` VALUES ('1699694804166971393', NULL, 'DZ9', '电站9', NULL, 15.6784, NULL, 110.2148, 25.0856, NULL, NULL, '2023-09-07 16:03:15', '若依', '2025-02-17 14:38:29', NULL, NULL, 202);
INSERT INTO `power_station` VALUES ('1702655456461262849', NULL, 'DZ8', '电站8', NULL, 4.7550, NULL, 106.2598, 27.6835, NULL, NULL, '2023-09-15 20:07:49', '若依', '2025-02-17 14:38:05', NULL, NULL, 203);
INSERT INTO `power_station` VALUES ('1713017994440945665', NULL, 'DZ7', '电站7', NULL, 0.5832, NULL, 111.8848, 26.6671, NULL, NULL, '2023-10-14 10:24:51', '华控管理员', '2025-02-17 16:33:41', NULL, NULL, 209);
INSERT INTO `power_station` VALUES ('1713020744759324674', NULL, 'DZ6', '电站6', NULL, 1.0000, NULL, 118.5645, 29.5735, NULL, NULL, '2023-10-14 10:35:47', '华控管理员', '2025-02-17 14:40:39', NULL, NULL, 206);
INSERT INTO `power_station` VALUES ('1714265578149003266', NULL, 'DZ5', '电站5', NULL, 2.0000, NULL, 112.9395, 30.3729, NULL, NULL, '2023-10-17 21:02:18', '华控管理员', '2025-02-17 14:36:50', NULL, NULL, 208);
INSERT INTO `power_station` VALUES ('1715018977064796161', NULL, 'DZ4', '电站4', NULL, 4.8650, NULL, 102.7441, 29.8406, NULL, NULL, '2023-10-19 22:56:02', '华控管理员', '2025-02-17 14:36:27', NULL, NULL, 214);
INSERT INTO `power_station` VALUES ('1718428873219125249', NULL, 'DZ2', '电站2', NULL, 8.1200, 350000.00, 109.3359, 34.1254, NULL, NULL, '2023-10-29 08:45:45', '华控管理员', '2025-02-17 14:35:13', NULL, NULL, 217);
INSERT INTO `power_station` VALUES ('1727270041864384513', NULL, 'DZ3', '电站3', NULL, 1.0000, NULL, 86.7480, 38.7541, NULL, NULL, '2023-10-22 18:17:24', '华控管理员', '2025-02-17 14:35:59', NULL, NULL, 218);

-- ----------------------------
-- Table structure for spare_parts
-- ----------------------------
DROP TABLE IF EXISTS `spare_parts`;
CREATE TABLE `spare_parts`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '编号',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `specs` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规格型号',
  `power_station_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '电站id',
  `power_station_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电站名称',
  `amount` int NOT NULL DEFAULT 0 COMMENT '库存数量',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门id',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '库存地点',
  `location_id` bigint NULL DEFAULT NULL COMMENT '库存地点id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '备品备件' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of spare_parts
-- ----------------------------
INSERT INTO `spare_parts` VALUES ('1736675985945808898', '1', 'IGBT', 'FF600R12ME4', '', NULL, 2, NULL, '2023-12-18 17:13:16', '华控管理员', '2024-11-27 21:17:22', '华控管理员', NULL, NULL, '仓库', 1736655288934612994);

-- ----------------------------
-- Table structure for spare_parts_record
-- ----------------------------
DROP TABLE IF EXISTS `spare_parts_record`;
CREATE TABLE `spare_parts_record`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '编号',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `specs` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '规格型号',
  `power_station_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '电站id',
  `power_station_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '出入库到电站名称',
  `amount` int NOT NULL DEFAULT 0 COMMENT '操作数量',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0入库，1出库）',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门id',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '库存地点',
  `location_id` bigint NULL DEFAULT NULL COMMENT '库存地点id',
  `movement_date` date NULL DEFAULT NULL COMMENT '出入库日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '备品备件-操作记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of spare_parts_record
-- ----------------------------
INSERT INTO `spare_parts_record` VALUES ('1736675985908060161', '1', 'IGBT', 'FF600R12ME4', '', NULL, 2, '0', NULL, '2023-12-18 17:13:16', '华控管理员', '2023-12-18 17:13:16', '华控管理员', 1, 100, '仓库', 1736655288934612994, NULL);

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2023-08-23 11:07:53', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2023-08-23 11:07:53', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2023-08-23 11:07:53', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2023-08-23 11:07:53', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2023-08-23 11:07:53', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2023-08-23 11:07:53', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 219 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '能源光伏新能源事业部', 0, '王', '', '', '0', '0', 'admin', '2023-08-23 11:07:37', 'admin', '2024-01-05 09:37:18');
INSERT INTO `sys_dept` VALUES (200, 100, '0,100', '电站运维', 0, NULL, NULL, NULL, '0', '0', 'admin', '2023-09-19 13:50:04', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2023-08-23 11:07:51', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2023-08-23 11:07:51', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2023-08-23 11:07:51', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2023-08-23 11:07:51', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2023-08-23 11:07:51', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2023-08-23 11:07:51', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2023-08-23 11:07:51', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2023-08-23 11:07:51', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2023-08-23 11:07:51', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2023-08-23 11:07:51', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2023-08-23 11:07:52', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2023-08-23 11:07:53', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2023-08-23 11:07:53', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2023-08-23 11:07:53', '', NULL, '停用状态');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2023-08-23 11:07:50', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2023-08-23 11:07:50', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2023-08-23 11:07:50', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2023-08-23 11:07:50', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2023-08-23 11:07:50', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2023-08-23 11:07:50', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2023-08-23 11:07:51', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2023-08-23 11:07:51', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2023-08-23 11:07:51', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2023-08-23 11:07:51', '', NULL, '登录状态列表');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2023-08-23 11:07:54', '', NULL, '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2023-08-23 11:07:54', '', NULL, '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2023-08-23 11:07:54', '', NULL, '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_sys_logininfor_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_logininfor_lt`(`login_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2298 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统访问记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2026 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 98, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2023-08-23 11:07:39', 'admin', '2023-08-25 10:04:02', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 99, 'monitor', NULL, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2023-08-23 11:07:39', 'admin', '2025-02-18 14:20:44', '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 100, 'tool', NULL, '', 1, 0, 'M', '1', '0', '', 'tool', 'admin', '2023-08-23 11:07:39', 'admin', '2023-10-25 21:13:25', '系统工具目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2023-08-23 11:07:39', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2023-08-23 11:07:39', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2023-08-23 11:07:39', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2023-08-23 11:07:39', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2023-08-23 11:07:39', 'admin', '2025-02-18 14:21:11', '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2023-08-23 11:07:39', 'admin', '2025-02-18 14:19:42', '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2023-08-23 11:07:40', 'admin', '2025-02-18 14:20:06', '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2023-08-23 11:07:40', 'admin', '2025-02-18 14:20:12', '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2023-08-23 11:07:40', 'admin', '2025-02-18 14:08:11', '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2023-08-23 11:07:40', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2023-08-23 11:07:40', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` VALUES (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', '2023-08-23 11:07:40', '', NULL, '数据监控菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2023-08-23 11:07:40', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', '2023-08-23 11:07:40', '', NULL, '缓存监控菜单');
INSERT INTO `sys_menu` VALUES (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', '2023-08-23 11:07:40', '', NULL, '缓存列表菜单');
INSERT INTO `sys_menu` VALUES (115, '表单构建', 3, 1, 'build', 'tool/build/index', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2023-08-23 11:07:40', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` VALUES (116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2023-08-23 11:07:40', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (117, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2023-08-23 11:07:40', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2023-08-23 11:07:40', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2023-08-23 11:07:40', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2023-08-23 11:07:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2023-08-23 11:07:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2023-08-23 11:07:41', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2023-08-23 11:07:42', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1049, '任务查询', 110, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2023-08-23 11:07:43', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '任务新增', 110, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2023-08-23 11:07:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1051, '任务修改', 110, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2023-08-23 11:07:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1052, '任务删除', 110, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2023-08-23 11:07:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1053, '状态修改', 110, 5, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2023-08-23 11:07:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1054, '任务导出', 110, 6, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2023-08-23 11:07:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 116, 1, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2023-08-23 11:07:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 116, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2023-08-23 11:07:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 116, 3, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2023-08-23 11:07:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 116, 4, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2023-08-23 11:07:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 116, 5, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2023-08-23 11:07:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 116, 6, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2023-08-23 11:07:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2000, '实时监测', 0, 1, 'realTime', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'server', 'admin', '2023-08-25 10:04:38', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2001, '电站实时状态', 2000, 2, 'site', 'realTime/site', NULL, 1, 1, 'C', '0', '0', '', '#', 'admin', '2023-08-25 10:05:18', 'admin', '2023-09-06 09:36:45', '');
INSERT INTO `sys_menu` VALUES (2002, '设备实时状态', 2000, 3, 'device', 'realTime/device', NULL, 1, 1, 'C', '0', '0', '', '#', 'admin', '2023-08-25 10:05:47', 'admin', '2023-09-06 09:36:53', '');
INSERT INTO `sys_menu` VALUES (2003, '运维管理', 0, 10, 'manage', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'example', 'admin', '2023-08-25 10:06:32', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2004, '电站管理', 2003, 1, 'siteList', 'manage/siteList', NULL, 1, 0, 'C', '0', '0', NULL, '#', 'admin', '2023-08-25 10:08:19', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2005, '设备管理', 2003, 2, 'deviceList', 'manage/deviceList', NULL, 1, 0, 'C', '0', '0', NULL, '#', 'admin', '2023-08-25 10:08:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2007, '设备点检', 2003, 5, 'inspection', 'manage/inspection', NULL, 1, 0, 'C', '0', '0', '', '#', 'admin', '2023-08-25 10:11:03', 'admin', '2023-08-29 09:29:46', '');
INSERT INTO `sys_menu` VALUES (2008, '备品备件', 2003, 7, 'store', 'manage/store', NULL, 1, 0, 'C', '0', '0', '', '#', 'admin', '2023-08-25 10:11:39', 'admin', '2023-11-03 17:14:16', '');
INSERT INTO `sys_menu` VALUES (2009, '尖峰平谷配置', 2003, 10, 'peaksValleys', 'manage/PeaksValleySetting', NULL, 1, 0, 'C', '0', '0', '', '#', 'admin', '2023-08-25 10:12:40', 'admin', '2023-11-03 17:13:45', '');
INSERT INTO `sys_menu` VALUES (2010, '统计分析', 0, 2, 'statistics', NULL, NULL, 1, 0, 'M', '0', '0', '', 'date', 'admin', '2023-08-25 11:02:33', 'admin', '2023-09-01 16:58:01', '');
INSERT INTO `sys_menu` VALUES (2011, '电站发电统计', 2010, 1, 'siteSum', 'statistics/siteSum', NULL, 1, 1, 'C', '0', '0', NULL, '#', 'admin', '2023-08-25 11:03:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2012, '设备发电统计', 2010, 2, 'deviceSum', 'statistics/deviceSum', NULL, 1, 1, 'C', '0', '0', NULL, '#', 'admin', '2023-08-25 11:04:27', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2013, '电能质量分析', 0, 4, 'electric', NULL, NULL, 1, 0, 'M', '0', '0', '', 'swagger', 'admin', '2023-08-25 11:09:56', 'admin', '2023-09-01 16:57:55', '');
INSERT INTO `sys_menu` VALUES (2014, '负荷分析', 2013, 1, 'load', 'electric/loadAnalysis', NULL, 1, 1, 'C', '0', '0', '', '#', 'admin', '2023-08-25 11:11:39', 'admin', '2023-08-25 11:13:24', '');
INSERT INTO `sys_menu` VALUES (2015, '三相不平衡分析', 2013, 2, 'threePhaseUnbalanceAnalysis', 'electric/threePhaseUnbalanceAnalysis', NULL, 1, 1, 'C', '0', '0', NULL, '#', 'admin', '2023-08-25 11:13:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2016, '功率因数分析', 2013, 3, 'powerFactorAnalysis', 'electric/powerFactorAnalysis', NULL, 1, 1, 'C', '0', '0', NULL, '#', 'admin', '2023-08-25 11:16:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2017, '智能报警', 0, 5, 'alarm', 'alarm/alarmList', NULL, 1, 1, 'C', '0', '0', NULL, 'time-range', 'admin', '2023-08-28 14:07:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2018, '设备类型管理', 2003, 3, 'deviceType', 'manage/deviceType', NULL, 1, 1, 'C', '0', '0', NULL, '#', 'admin', '2023-08-29 09:29:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2019, '同比分析', 2010, 3, 'sameAnalysis', 'statistics/sameAnalysis', '', 1, 1, 'C', '0', '0', '', '#', 'admin', '2023-08-29 16:26:44', 'admin', '2023-08-29 17:53:49', '');
INSERT INTO `sys_menu` VALUES (2020, '环比分析', 2010, 4, 'loopAnalysis', 'statistics/sameAnalysis', '', 1, 0, 'C', '0', '0', '', '#', 'admin', '2023-08-29 17:35:47', 'admin', '2023-08-29 17:53:55', '');
INSERT INTO `sys_menu` VALUES (2021, '尖峰平谷', 0, 3, 'peaks', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'time-range', 'admin', '2023-09-01 16:59:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2022, '报表统计', 2021, 2, 'report', 'peaks/report', NULL, 1, 1, 'C', '0', '0', NULL, '#', 'admin', '2023-09-01 17:04:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2023, '图表统计', 2021, 1, 'charts', 'peaks/charts', NULL, 1, 1, 'C', '0', '0', NULL, '#', 'admin', '2023-09-01 17:05:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2024, '实时数据', 2000, 1, 'real-data', 'realTime/realData', NULL, 1, 1, 'C', '0', '0', NULL, '#', 'admin', '2023-09-06 09:36:37', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '0', 'admin', '2023-08-23 11:07:54', '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', 'admin', '2023-08-23 11:07:54', '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1656 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2023-08-23 11:07:38', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2023-08-23 11:07:38', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 113 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2023-08-23 11:07:39', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '2', 'admin', '2023-08-23 11:07:39', '', NULL, '普通角色');
INSERT INTO `sys_role` VALUES (112, '演示用户', 'guestUser', 0, '1', 1, 1, '0', '0', 'admin', '2025-02-17 14:28:25', 'admin', '2025-02-17 17:02:44', NULL);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (112, 2000);
INSERT INTO `sys_role_menu` VALUES (112, 2001);
INSERT INTO `sys_role_menu` VALUES (112, 2002);
INSERT INTO `sys_role_menu` VALUES (112, 2010);
INSERT INTO `sys_role_menu` VALUES (112, 2011);
INSERT INTO `sys_role_menu` VALUES (112, 2012);
INSERT INTO `sys_role_menu` VALUES (112, 2013);
INSERT INTO `sys_role_menu` VALUES (112, 2014);
INSERT INTO `sys_role_menu` VALUES (112, 2015);
INSERT INTO `sys_role_menu` VALUES (112, 2016);
INSERT INTO `sys_role_menu` VALUES (112, 2017);
INSERT INTO `sys_role_menu` VALUES (112, 2019);
INSERT INTO `sys_role_menu` VALUES (112, 2020);
INSERT INTO `sys_role_menu` VALUES (112, 2021);
INSERT INTO `sys_role_menu` VALUES (112, 2022);
INSERT INTO `sys_role_menu` VALUES (112, 2023);
INSERT INTO `sys_role_menu` VALUES (112, 2024);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 114 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 100, 'admin', '管理员', '00', '', '15888888888', '0', '', '$2a$10$x0SN7Mj8YNFvsjTwWaueHeevIgKwiZKpAg/j.Z7YgcDJLC8g/CDWW', '0', '0', '127.0.0.1', '2025-03-01 14:12:02', 'admin', '2023-08-23 11:07:38', '', '2025-03-01 14:12:01', '管理员');
INSERT INTO `sys_user` VALUES (113, 100, 'guestUser', 'guestUser', '00', '', '', '0', '', '$2a$10$x0SN7Mj8YNFvsjTwWaueHeevIgKwiZKpAg/j.Z7YgcDJLC8g/CDWW', '0', '0', '127.0.0.1', '2025-03-01 13:19:51', '管理员', '2025-02-17 14:28:34', 'admin', '2025-03-01 13:19:51', NULL);

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (111, 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (103, 106);
INSERT INTO `sys_user_role` VALUES (104, 107);
INSERT INTO `sys_user_role` VALUES (105, 105);
INSERT INTO `sys_user_role` VALUES (106, 109);
INSERT INTO `sys_user_role` VALUES (107, 108);
INSERT INTO `sys_user_role` VALUES (108, 110);
INSERT INTO `sys_user_role` VALUES (109, 100);
INSERT INTO `sys_user_role` VALUES (110, 111);
INSERT INTO `sys_user_role` VALUES (111, 100);
INSERT INTO `sys_user_role` VALUES (113, 112);

-- ----------------------------
-- Table structure for wx_subscribe
-- ----------------------------
DROP TABLE IF EXISTS `wx_subscribe`;
CREATE TABLE `wx_subscribe`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信的open_id',
  `template_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息模板id',
  `to_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wx_subscribe
-- ----------------------------

-- ----------------------------
-- Table structure for wx_user
-- ----------------------------
DROP TABLE IF EXISTS `wx_user`;
CREATE TABLE `wx_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信的id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户和微信的一对多绑定，一个系统账户可以绑定多个微信，但一个微信只能对应一个系统账户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wx_user
-- ----------------------------
INSERT INTO `wx_user` VALUES (2, 111, 'oZuR16yPGJKPf8rvtRTXaZoLrSFY');
INSERT INTO `wx_user` VALUES (3, 111, 'oZuR169pQ2ZR1IDd8-kScDroAVww');

SET FOREIGN_KEY_CHECKS = 1;
