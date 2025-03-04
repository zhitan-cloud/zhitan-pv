import request from "@/utils/request"
// 负荷分析
export function getLoadAnalysis(params) {
  return request({
    url: "/realTime/listLoadAnalysis",
    method: "get",
    params: params,
  })
}

// 功率因数
export function getPowerFactorAnalysis(params) {
  return request({
    url: "/realTime/getPowerFactorAnalysis",
    method: "get",
    params: params,
  })
}

// 三相不平衡
export function getThreePhaseUnbalance(params) {
  return request({
    url: "/realTime/listThreePhaseUnbalanceAnalysis",
    method: "get",
    params: params,
  })
}

// 负荷分析-详情
export function getLoadAnalysisDetail(params) {
  return request({
    url: "/realTime/getLoadAnalysisDetail",
    method: "get",
    params: params,
  })
}

// 三相不平衡-详情
export function getThreePhaseUnbalanceDetail(params) {
  return request({
    url: "/realTime/getThreePhaseUnbalanceAnalysisDetail",
    method: "get",
    params: params,
  })
}

// 电表实时数据
export function listRealTime(params) {
  return request({
    url: "/realTime/listRealTime",
    method: "get",
    params: params,
  })
}
