<template>
  <div class="app-container">
    <!--检索部分-->
    <el-form :model="queryParams" ref="queryRef" inline label-width="68px">
      <el-form-item label="电站名称" prop="siteId">
        <el-select v-model="queryParams.siteId" placeholder="请选择电站" size="" clearable>
          <el-option v-for="dict in siteOptions" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="设备名称" prop="deviceId">
        <el-select v-model="queryParams.deviceId" placeholder="请选择设备" size="" clearable>
          <el-option
            v-for="dict in deviceOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
        </el-select>
      </el-form-item> -->
      <el-form-item label="" prop="type" label-width="0">
        <el-radio-group v-model="queryParams.timeType" size="" style="margin-right: 12px" @change="changeTimeType">
          <!-- <el-radio-button label="hour">时</el-radio-button> -->
          <el-radio-button label="date">日</el-radio-button>
          <el-radio-button label="month">月</el-radio-button>
          <el-radio-button label="year">年</el-radio-button>
        </el-radio-group>
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
    <div ref="chartRef" style="height: 400px"></div>

    <!-- table -->
    <el-table
      v-loading="loading"
      ref="multipleTableRef"
      :data="chartData"
      border
      :height="tableHeight"
      highlight-current-row
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="55" /> -->
      <el-table-column
        label="电站名称"
        align="center"
        prop="powerStationName"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column prop="tableCurrentTime" label="本期时间" />
      <el-table-column prop="currentValue" label="本期发电量" />
      <el-table-column prop="tableCompareTime" label="同期时间" />
      <el-table-column prop="contrastValues" label="同期发电量" />
      <el-table-column prop="ratio" :label="`${pageType}(%)`" />
    </el-table>
  </div>
</template>

<script setup name="LoopAnalysis">
import * as echarts from "echarts"
import { formatDate, formatDateObj, formatChartTime, formatLongTime } from "@/utils/index"
import { listSite } from "@/api/manage/site"
import { listDevice } from "@/api/manage/device"
import { getLoopCompareList, getSameCompareList } from "@/api/analysis/same"
import { nextTick, reactive, ref, onMounted, onUnmounted } from "vue"
// import { useRoute } from "vue-router"

const route = useRoute()
const { proxy } = getCurrentInstance()

const tableHeight = ref(window.innerHeight - 560)
// 查询所有电站
const siteOptions = ref()
function getSiteData() {
  listSite({
    pageNum: 1,
    pageSize: 10000,
  }).then((res) => {
    siteOptions.value = res.rows
      ? res.rows.map((item) => {
          return { label: item.name, value: item.id }
        })
      : []
  })
}
// 查询所有电站
const deviceOptions = ref()
function getDeviceData() {
  listDevice({
    pageNum: 1,
    pageSize: 10000,
  }).then((res) => {
    deviceOptions.value = res.rows
      ? res.rows.map((item) => {
          return { label: item.name, value: item.id }
        })
      : []
  })
}
const tableData = ref([])
const chartData = ref([])
const loading = ref(false)
const pageType = ref("")
function getPageType() {
  const path = route.path
  if (path == "/statistics/loopAnalysis") {
    pageType.value = "环比"
  } else {
    pageType.value = "同比"
  }
}
const multipleSelection = ref([])
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const dateRange = ref([])

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    timeType: "month",
    timeData: formatDate(new Date()),
    timeStr: "",
    siteId: undefined,
    deviceId: undefined,
  },
})
const { queryParams } = toRefs(data)
function changeTimeType(e) {
  queryParams.value.timeData = formatDate(new Date())
  handleQuery()
}
/** 查询列表 */
function getList() {
  loading.value = true
  let apiUrl = pageType.value == "环比" ? getLoopCompareList : getSameCompareList
  let dateObj = formatDateObj(queryParams.value.timeData)
  let queryTime = dateObj.year + "-" + dateObj.month + "-" + dateObj.day
  apiUrl({
    queryTime: queryTime,
    timeType: queryParams.value.timeType == "date" ? "DAY" : queryParams.value.timeType.toUpperCase(),
    powerStationId: queryParams.value.siteId,
    dataItemTimeType: queryParams.value.deviceId,
  }).then((res) => {
    loading.value = false
    // chart
    chartData.value = res.data.map((item) => {
      return {
        ...item,
        time: formatChartTime(queryParams.value.timeType, item.currentTime),
        tableCurrentTime: formatLongTime(queryParams.value.timeType, item.currentTime),
        tableCompareTime: formatLongTime(queryParams.value.timeType, item.compareTime),
        value: item.currentValue,
        oldValue: item.contrastValues,
        ratio: item.ratio,
      }
    })
    drawChart()
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  // columns.value = getColumnsForDate(queryParams.value.timeData, queryParams.value.timeType)
  getList()
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}
// 展示图表
const chartRef = ref(null)
let echartsInstance = null
function drawChart() {
  echartsInstance = echarts.init(chartRef.value, "macarons")
  let option = {
    title: {
      text: `${pageType.value}分析`,
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
      data: ["本期值", "同期值", `${pageType.value}值`],
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
        name: "单位：kWh",
        // interval: 5,
        axisLabel: {
          formatter: function (a) {
            a = +a
            return a
            // return isFinite(a) ? echarts.format.addCommas(+a / 1000) : ""
          },
        },
      },
      {
        type: "value",
        name: "",
        // min: 0,
        // max: 100,
        // interval: 5,
        axisLabel: {
          formatter: "{value} %",
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
        name: "本期值",
        type: "bar",
        smooth: true,
        color: "#56C6C8",
        data: chartData.value.map((item) => {
          return item.value
        }),
      },
      {
        name: "同期值",
        type: "bar",
        smooth: true,
        color: "#eaeaea",
        data: chartData.value.map((item) => {
          return item.oldValue
        }),
      },
      {
        name: `${pageType.value}值`,
        type: "line",
        smooth: true,
        yAxisIndex: 1,
        color: "#337ecc",
        tooltip: {
          valueFormatter: function (value) {
            return value + "%"
          },
        },
        data: chartData.value.map((item) => {
          return item.ratio
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

/** 选择条数  */
function handleSelectionChange(selection) {
  multipleSelection.value = selection
  ids.value = selection.map((item) => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

getPageType()
onMounted(() => {
  getSiteData()
  // getDeviceData()
  handleQuery()
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
