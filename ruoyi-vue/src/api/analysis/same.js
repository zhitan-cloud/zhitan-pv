import request from "@/utils/request"
// 环比
export function getLoopCompareList(params) {
  return request({
    url: "/statisticsAnalysis/queryLoopCompareList",
    method: "get",
    params: params,
  })
}

// 同比
export function getSameCompareList(params) {
  return request({
    url: "/statisticsAnalysis/querySameCompareList",
    method: "get",
    params: params,
  })
}
