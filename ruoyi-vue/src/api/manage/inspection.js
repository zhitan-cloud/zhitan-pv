import request from "@/utils/request"

// list
export function listInspection(params) {
  return request({
    url: "/inspection/list",
    method: "get",
    params: params,
  })
}

// 新增
export function addInspection(params) {
  return request({
    url: "/inspection",
    method: "post",
    data: params,
  })
}

// 编辑
export function updateInspection(params) {
  return request({
    url: "/inspection",
    method: "put",
    data: params,
  })
}

// 删除
export function deleteInspection(id) {
  return request({
    url: "/inspection/" + id,
    method: "delete",
  })
}

// 详情
export function inspectionDetail(id) {
  return request({
    url: "/inspection/" + id,
    method: "get",
  })
}
