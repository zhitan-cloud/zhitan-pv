import request from "@/utils/request"

// list
export function getAlarmList(params) {
  return request({
    url: "/alarm/list",
    method: "get",
    params: params,
  })
}

// 处理
export function alarmHandling(params) {
  return request({
    url: "/alarm/alarmHandling",
    method: "post",
    data: params,
  })
}

// 详情
export function alarmDetail(params) {
  return request({
    url: "/alarm/" + params.id,
    method: "get",
  })
}

export function getAlarmLevelAnalysis(params) {
  return request({
    url: "/alarm/getAlarmLevelAnalysis",
    method: "get",
    params: params,
  })
}

//
export function editAlarmLevel(params) {
  return request({
    url: "/alarm/editAlarmLevel",
    method: "get",
    params: params,
  })
}
