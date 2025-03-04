import request from "@/utils/request"

// list
export function listDeviceType(params) {
  return request({
    url: "/deviceType/list",
    method: "get",
    params: params,
  })
}

// 新增
export function addDeviceType(params) {
  return request({
    url: "/deviceType/create",
    method: "post",
    data: params,
  })
}

// 编辑
export function updateDeviceType(params) {
  return request({
    url: "/deviceType/edit",
    method: "post",
    data: params,
  })
}

// 删除
export function deleteDeviceType(params) {
  return request({
    url: "/deviceType/" + params,
    method: "delete",
  })
}

// 点位list
export function indexTemplateList(params) {
  return request({
    url: "/deviceType/index/" + params,
    method: "get",
  })
}

// 新增编辑点位
export function updateTemplate(params) {
  return request({
    url: "/deviceType/index/" + params.id,
    method: "post",
    data: params.data,
  })
}
