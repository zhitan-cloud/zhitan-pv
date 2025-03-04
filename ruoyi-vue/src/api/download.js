import { ElMessage } from "element-plus"
import request from "@/utils/request"
/**
 * 下载文件 用于excel导出
 * @param url
 * @param parameter
 * @returns {*}
 */
export function downFile(url, parameter) {
  return request({
    url: url,
    params: parameter,
    method: "get",
    responseType: "blob",
  })
}

/**
 * 下载文件
 * @param url 文件路径
 * @param fileName 文件名
 * @param parameter
 * @returns {*}
 */
export function downloadFile(url, fileName, parameter) {
  return downFile(url, parameter).then((data) => {
    if (!data || data.size === 0) {
      ElMessage.error("文件下载失败")
      return
    }
    if (typeof window.navigator.msSaveBlob !== "undefined") {
      window.navigator.msSaveBlob(new Blob([data]), fileName)
    } else {
      let url = window.URL.createObjectURL(new Blob([data]))
      let link = document.createElement("a")
      link.style.display = "none"
      link.href = url
      link.setAttribute("download", fileName)
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link) //下载完成移除元素
      window.URL.revokeObjectURL(url) //释放掉blob对象
    }
  })
}
