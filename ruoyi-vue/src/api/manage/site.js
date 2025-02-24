import request from "@/utils/request"

// list
export function listSite(params) {
  return request({
    url: "/powerStation/list",
    method: "get",
    params: params,
  })
}

// 新增
export function addSite(params) {
  return request({
    url: "/powerStation",
    method: "post",
    data: params,
  })
}

// 编辑
export function updateSite(params) {
  return request({
    url: "/powerStation",
    method: "put",
    data: params,
  })
}

// 删除
export function deleteSite(params) {
  return request({
    url: "/powerStation/" + params,
    method: "delete",
  })
}

// 详情
export function siteDetail(params) {
  return request({
    url: "/powerStation/" + params.id,
    method: "get",
  })
}

// 电站发电统计
export function siteStatistics(params) {
  return request({
    url: "/powerStation/listGenerationStatistics",
    method: "get",
    params,
  })
}

// 电站状态-根据电站id获取发电趋势信息
export function getImplementedPower(params) {
  return request({
    url: "/powerStation/getImplementedPower",
    method: "get",
    params,
  })
}

// 电站状态-根据电站id查询发电信息、收益信息
export function getPowerGenerationInfo(params) {
  return request({
    url: "/powerStation/getPowerGenerationInfo",
    method: "get",
    params,
  })
}
// 电站状态-查询电站发电统计
export function getPowerStationInfoById(params) {
  return request({
    url: "/powerStation/getPowerStationInfoById",
    method: "get",
    params,
  })
}

// 电站状态-根据电站id获取设备信息
export function listDeviceById(params) {
  return request({
    url: "/powerStation/listDeviceById",
    method: "get",
    params,
  })
}
