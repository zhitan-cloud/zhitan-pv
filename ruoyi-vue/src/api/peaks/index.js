import request from "@/utils/request"

// list
export function peakValleyReport(params) {
  return request({
    url: "/peakValley/report",
    method: "get",
    params: params,
  })
}

// list
export function peakValleySegment(params) {
  return request({
    url: "/peakValley/segment",
    method: "get",
    params: params,
  })
}

// list
export function exportPeaks(params) {
  return request({
    url: "/peakValley/export",
    method: "get",
    responseType: "blob",
    params: params,
  })
}
