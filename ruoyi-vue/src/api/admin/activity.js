import request from "@/utils/request"

// 查询商机
export function listWebOpportunity(params) {
  return request({
    url: "/crm/opportunity/listWebOpportunity",
    method: "get",
    params: params,
  })
}

// 查询任务
export function listWebActive(params) {
  return request({
    url: "/crm/active/listWebActive",
    method: "get",
    params: params,
  })
}

// 查询商机
export function usersBusiness(params) {
  return request({
    url: "/crm/opportunity/users/" + params.id,
    method: "get",
    params: params,
  })
}

// 查询任务
export function usersWork(params) {
  return request({
    url: "/crm/active/users/" + params.id,
    method: "get",
    params: params,
  })
}
