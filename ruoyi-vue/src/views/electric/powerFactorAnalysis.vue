<template>
  <div class="app-container">
    <!--检索部分-->
    <el-form :model="queryParams" ref="queryRef" inline v-show="showSearch" label-width="68px">
      <el-form-item label="电站名称" prop="powerStationId">
        <el-select
          v-model="queryParams.powerStationId"
          placeholder="请选择电站"
          size=""
          @change="(e) => getDeviceData(e)"
        >
          <el-option v-for="dict in siteOptions" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="设备名称" prop="deviceId">
        <el-select v-model="queryParams.deviceId" placeholder="请选择设备" size="">
          <el-option
            v-for="dict in deviceOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="日期" prop="type" label-width="40">
        <!-- <el-radio-group v-model="queryParams.timeType" size="" style="margin-right: 12px" @change="changeTimeType">
          <el-radio-button label="hour">时</el-radio-button>
          <el-radio-button label="date">日</el-radio-button>
          <el-radio-button label="month">月</el-radio-button>
          <el-radio-button label="year">年</el-radio-button>
        </el-radio-group> -->
        <el-date-picker
          v-model="queryParams.timeData"
          :type="queryParams.timeType"
          placeholder="请选择"
          style="width: 200px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </el-form-item>
      <!-- <el-form-item style="float: right; margin-right: 0">
        <el-button type="success" plain icon="download" @click="handleExport"> 导出 </el-button>
      </el-form-item> -->
    </el-form>

    <!-- 图表部分 -->
    <div ref="chartRef" style="height: 500px"></div>

    <!-- table -->
    <el-table v-loading="loading" border :data="tableData" highlight-current-row style="width: 100%">
      <el-table-column label="类型" align="center" prop="type" />
      <el-table-column label="功率因数" align="center" prop="value" />
      <el-table-column label="发生时间" align="center" prop="time" />
    </el-table>
  </div>
</template>

<script setup name="SiteSum">
import * as echarts from "echarts"
import { formatDate, formatDateObj, formatChartTime } from "@/utils/index"
import { getPowerFactorAnalysis } from "@/api/analysis/load"
import { nextTick, reactive, ref, onMounted, onUnmounted } from "vue"
import { listSite } from "@/api/manage/site"
import { listDevice } from "@/api/manage/device"

const { proxy } = getCurrentInstance()
// 查询所有电站
const siteOptions = ref()
function getSiteData() {
  listSite({
    pageNum: 1,
    pageSize: 10000,
  }).then((res) => {
    if (res.rows && res.rows.length) {
      siteOptions.value = res.rows
        ? res.rows.map((item) => {
            return { label: item.name, value: item.id }
          })
        : []

      queryParams.value.powerStationId = siteOptions.value[0].value
      getDeviceData(siteOptions.value[0].value)
    }
  })
}
// 查询所有电站
const deviceOptions = ref()
function getDeviceData(powerStationId) {
  listDevice({
    pageNum: 1,
    pageSize: 10000,
    powerStationId,
  }).then((res) => {
    if (res.rows && res.rows.length) {
      deviceOptions.value = res.rows
        ? res.rows.map((item) => {
            return { label: item.name, value: item.id }
          })
        : []

      queryParams.value.deviceId = deviceOptions.value[0].value
      getList()
    }
  })
}
const chartData = ref([])
const tableData = ref([])
const loading = ref(false)
const showSearch = ref(true)
const data = reactive({
  queryParams: {
    powerStationId: undefined,
    deviceId: undefined,
    timeType: "date",
    timeData: formatDate(new Date()),
    timeStr: "",
  },
})

const { queryParams } = toRefs(data)

function changeTimeType(e) {
  console.log(e)
  handleQuery()
}
/** 查询用户列表 */
function getList() {
  if (!queryParams.value.powerStationId) {
    return
  }
  if (!queryParams.value.deviceId) {
    return
  }
  loading.value = true
  let dateObj = formatDateObj(queryParams.value.timeData)
  let queryTime = ""
  if (queryParams.value.timeType == "date") {
    queryTime = dateObj.year + "-" + dateObj.month + "-" + dateObj.day
  } else if (queryParams.value.timeType == "month") {
    queryTime = dateObj.year + "-" + dateObj.month
  } else {
    queryTime = dateObj.year
  }
  let params = {
    ...queryParams.value,
    timeCode: queryTime,
    timeType: queryParams.value.timeType == "date" ? "DAY" : queryParams.value.timeType.toUpperCase(),
  }
  getPowerFactorAnalysis(params).then((res) => {
    loading.value = false
    let detailData = res.data.detail
    tableData.value = [
      {
        type: "最大值",
        value: detailData.max,
        time: detailData.maxTime,
      },
      {
        type: "最小值",
        value: detailData.min,
        time: detailData.minTime,
      },
      {
        type: "平均值",
        value: detailData.avg,
        time: "--",
      },
    ]
    // chart
    if (res.data.itemList && res.data.itemList.length !== 0) {
      let arr = []
      res.data.itemList.forEach((item) => {
        arr.push({
          time: formatChartTime(queryParams.value.timeType, params.timeCode + " " + item.timeCode + ":00"),
          value: item.value === "--" ? null : +item.value,
          name: "功率因数",
        })
      })
      chartData.value = arr
      nextTick(() => {
        drawChart()
      })
    }
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  console.log(queryParams.value)
  getList()
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}
// 展示图表--图表弹窗
const chartRef = ref(null)
let echartsInstance = null
function drawChart() {
  echartsInstance = echarts.init(chartRef.value, "macarons")
  let option = {
    title: {
      text: "功率因数分析",
    },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "shadow",
        label: {
          show: true,
        },
      },
    },
    toolbox: {
      show: true,
      feature: {
        mark: { show: true },
        dataView: { show: true, readOnly: false },
        magicType: { show: true, type: ["line", "bar"] },
        restore: { show: true },
        saveAsImage: { show: true },
      },
    },
    calculable: true,
    legend: {
      data: ["功率因数"],
      itemGap: 5,
    },
    // legend: false,
    grid: {
      top: 56,
      left: "1%",
      right: "1%",
      bottom: 16,
      containLabel: true,
    },
    xAxis: [
      {
        name: "",
        type: "category",
        data: chartData.value.map((item) => {
          return item.time
        }),
      },
    ],
    yAxis: [
      {
        type: "value",
        name: "",
        axisLabel: {
          formatter: function (a) {
            a = +a
            return a
            // return isFinite(a) ? echarts.format.addCommas(+a / 1000) : ""
          },
        },
      },
    ],
    // dataZoom: [
    //   {
    //     show: true,
    //     start: 0,
    //     end: 100,
    //   },
    //   {
    //     type: "inside",
    //     start: 94,
    //     end: 100,
    //     // show: false,
    //   },
    //   {
    //     show: false, // y轴
    //     yAxisIndex: 0,
    //     filterMode: "empty",
    //     width: 30,
    //     height: "80%",
    //     showDataShadow: false,
    //     left: "93%",
    //   },
    // ],
    series: [
      {
        name: "功率因数",
        type: "line",
        smooth: true,
        data: chartData.value.map((item) => {
          return item.value
        }),
      },
    ],
  }
  echartsInstance.setOption(option)
  echartsInstance.resize()
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "monitor/job/export",
    {
      ...queryParams.value,
    },
    `job_${new Date().getTime()}.xlsx`
  )
}

onMounted(() => {
  getSiteData()
  /* 解决图表宽度溢出问题 */
  window.addEventListener("resize", setCharts)
})
onUnmounted(() => {
  window.removeEventListener("resize", setCharts)
})
function setCharts() {
  if (echartsInstance) {
    echartsInstance.resize()
  }
}
</script>

<style lang="scss" scoped>
:deep .el-form--inline .el-form-item {
  margin-right: 24px;
  // width: 100%;
}
</style>
