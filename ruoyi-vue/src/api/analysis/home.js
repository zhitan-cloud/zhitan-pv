import request from "@/utils/request"
// 电站排名及经纬度
export function getSiteRank() {
  return request({
    url: "/powerStation/listPowerStationRank",
    method: "get",
  })
}

// 首页-统计信息
export function getHomePowerStationInfo() {
  return request({
    url: "/powerStation/getHomePowerStationInfo",
    method: "get",
  })
}

// 环比
export function getLoopCompareList(params) {
  return request({
    url: "/statisticsAnalysis/queryLoopCompareList",
    method: "get",
    params: params,
  })
}

// 报警
export function listHomeAlarm(params) {
  return request({
    url: "/alarm/listHomeAlarm",
    method: "get",
    params: params,
  })
}
// 首页-峰平谷占比
export function getPeriodGenerationPercentage(params) {
  return request({
    url: "/peakValley/getPeriodGenerationPercentage",
    method: "get",
    params: params,
  })
}

// 首页-年发电量折线图
export function homepageGeneration(params) {
  return request({
    url: "/statisticsAnalysis/getHomepageGenerationStats",
    method: "get",
    params: params,
  })
}
