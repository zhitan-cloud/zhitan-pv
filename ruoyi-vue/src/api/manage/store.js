import request from "@/utils/request"

// list
export function listStore(params) {
  return request({
    url: "/parts/list",
    method: "get",
    params: params,
  })
}

// 新增-入库
export function addStore(params) {
  return request({
    url: "/parts",
    method: "post",
    data: params,
  })
}

// 出库
export function updateStore(params) {
  return request({
    url: "/parts",
    method: "put",
    data: params,
  })
}

// 删除
export function deleteStore(id) {
  return request({
    url: "/parts/delete?id=" + id,
    method: "delete",
  })
}

// 详情
export function storeDetail(id) {
  return request({
    url: "/parts/" + id,
    method: "get",
  })
}

// 出入库记录
export function listOperationRecords(params) {
  return request({
    url: "/parts/listOperationRecords",
    method: "get",
    params,
  })
}

// 编辑
export function editPart(params) {
  return request({
    url: "/parts/update",
    method: "post",
    data: params,
  })
}

// list - 库存地点
export function listAddress() {
  return request({
    url: "/inv-loc/list",
    method: "get",
  })
}

// 新增-库存地点
export function addAddress(params) {
  return request({
    url: "/inv-loc/create",
    method: "post",
    data: params,
  })
}

// 编辑 库存地点
export function updateAddress(params) {
  return request({
    url: "/inv-loc/edit",
    method: "put",
    data: params,
  })
}

// 删除 库存地点
export function deleteAddress(id) {
  return request({
    url: "/inv-loc/delete/" + id,
    method: "delete",
  })
}
