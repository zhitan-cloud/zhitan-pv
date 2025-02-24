/**
 * @desc 函数节流
 * @param fn 函数
 * @param wait 延迟执行毫秒数
 */
const throttle = function (fn, wait) {
  let timer = null
  return function () {
    let context = this
    let args = arguments
    if (!timer) {
      // 当延迟时间结束后，执行函数
      timer = setTimeout(() => {
        timer = null
        fn.apply(context, args)
      }, wait)
    }
  }
}

let ro = null
let charts = []
// 创建并返回一个新的 ResizeObserver 对象
const newRO = function (callback) {
  // ResizeObserver 接口监视 Element 内容盒或边框盒或者 SVGElement 边界尺寸的变化。
  !ro && (ro = new ResizeObserver(callback))
}

/**
 *
 * @param {Object} chartVal 初始化的echarts实例
 * @param {Object} domElement 被监听尺寸变化的dom元素
 */
function resize(chartVal, domElement) {
  charts.push(chartVal)
  const throttledFun = throttle(
    () => {
      charts.forEach((item) => {
        item.resize() // 调整echarts尺寸
      })
    },
    1000,
    false
  )
  newRO(throttledFun)

  // 添加监听事件
  function initListener() {
    ro.observe(domElement) // 观察一个或多个元素
  }

  // 调用函数添加监听事件：
  initListener()
}

// 移除观察者对某一个元素的监听
function destroyListener(chartVal, domElement) {
  charts = charts.filter((item) => item !== chartVal)
  ro.unobserve(domElement) // 取消特定观察者目标上所有对 Element 的监听。
}

export { resize, destroyListener }
