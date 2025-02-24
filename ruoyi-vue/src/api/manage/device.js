import request from "@/utils/request"

// list
export function listDevice(params) {
  return request({
    url: "/device/list",
    method: "get",
    params: params,
  })
}

// 新增
export function addDevice(params) {
  return request({
    url: "/device",
    method: "post",
    data: params,
  })
}

// 编辑
export function updateDevice(params) {
  return request({
    url: "/device",
    method: "put",
    data: params,
  })
}

// 删除
export function deleteDevice(id) {
  return request({
    url: "/device/" + id,
    method: "delete",
  })
}

// 详情
export function deviceDetail(id) {
  return request({
    url: "/device/" + id,
    method: "get",
  })
}
// 设备信息
export function getInverterInfo(params) {
  return request({
    url: "/device/getInverterInfo",
    method: "get",
    params: params,
  })
}
// 设备发电统计
export function getInverterGenerationStats(params) {
  return request({
    url: "/device/getInverterGenerationStats",
    method: "get",
    params: params,
  })
}
// 交流测
export function getACMeasurementsByDeviceId(params) {
  return request({
    url: "/device/getACMeasurementsByDeviceId",
    method: "get",
    params: params,
  })
}
// 根据设备id获取实时功率，功率、发电趋势信息
export function getImplementedPower(params) {
  return request({
    url: "/device/getImplementedPower",
    method: "get",
    params: params,
  })
}

// 根据设备id查询发电信息、收益信息
export function getPowerGenerationInfo(params) {
  return request({
    url: "/device/getPowerGenerationInfo",
    method: "get",
    params: params,
  })
}

// 根据设备id查询检修记录
export function listDeviceInspectionByDeviceId(params) {
  return request({
    url: "/device/listDeviceInspectionByDeviceId",
    method: "get",
    params: params,
  })
}
