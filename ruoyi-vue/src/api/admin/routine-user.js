import request from "@/utils/request"

// 查询小程序用户
export function listUser(params) {
  return request({
    url: "/system/user/listWxUser",
    method: "get",
    params: params,
  })
}
// 分配管理员
export function userAuthorization(params) {
  return request({
    url: "/system/user/userAuthorization",
    method: "post",
    data: params,
  })
}
