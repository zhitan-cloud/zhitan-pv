import request from "@/utils/request"

// list
export function listSetting(params) {
  return request({
    url: "/electricityTypeSetting/list",
    method: "get",
    params: params,
  })
}

// 根据主项id查询峰平谷子项配置
export function listSettingItem(id) {
  return request({
    url: "/electricityTypeSetting/listBySettingId/" + id,
    method: "get",
  })
}

// 新增
export function addSetting(params) {
  return request({
    url: "/electricityTypeSetting",
    method: "post",
    data: params,
  })
}

// 新增子项
export function addSettingItem(params) {
  return request({
    url: "/electricityTypeSetting/addItem",
    method: "post",
    data: params,
  })
}

// 编辑
export function updateSetting(params) {
  return request({
    url: "/electricityTypeSetting",
    method: "put",
    data: params,
  })
}

// 删除
export function deleteSetting(id) {
  return request({
    url: "/electricityTypeSetting/" + id,
    method: "delete",
  })
}

// 详情
export function settingDetail(id) {
  return request({
    url: "/electricityTypeSetting/" + id,
    method: "get",
  })
}
