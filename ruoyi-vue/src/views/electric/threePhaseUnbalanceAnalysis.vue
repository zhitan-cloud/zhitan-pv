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

    <el-tabs v-model="activeTabKey" @tab-click="changeTabs" type="border-card">
      <el-tab-pane name="first" label="电压不平衡">
        <div class="display-buttons">
          <div class="display-btn" @click="activeKeyV = 1" :class="{ 'active-display-btn': activeKeyV === 1 }">
            图形
          </div>
          <div class="display-btn" @click="activeKeyV = 2" :class="{ 'active-display-btn': activeKeyV === 2 }">
            数据
          </div>
        </div>
        <div v-show="activeKeyV === 1">
          <div ref="chartRef" style="height: 500px" />
          <div style="margin-top: 16px">
            <!-- table -->
            <el-table v-loading="loading" border :data="tableData" highlight-current-row style="width: 100%">
              <el-table-column label="类型" align="center" prop="type" />
              <el-table-column label="三相不平衡极值" align="center" prop="value" />
              <el-table-column label="发生时间" align="center" prop="time" />
              <el-table-column label="A相电压(V)" align="center" prop="valueA" />
              <el-table-column label="B相电压(V)" align="center" prop="valueB" />
              <el-table-column label="C相电压(V)" align="center" prop="valueC" />
            </el-table>
          </div>
        </div>
        <div v-show="activeKeyV === 2">
          <div style="margin-top: 16px">
            <!-- table -->
            <el-table v-loading="loading" border :data="chartData" highlight-current-row style="width: 100%">
              <el-table-column label="时间" align="center" prop="tableTime" />
              <el-table-column label="A相电压(V)" align="center" prop="valueA" />
              <el-table-column label="B相电压(V)" align="center" prop="valueB" />
              <el-table-column label="C相电压(V)" align="center" prop="valueC" />
            </el-table>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane name="second" label="电流不平衡">
        <div class="display-buttons">
          <div class="display-btn" @click="activeKeyA = 1" :class="{ 'active-display-btn': activeKeyA === 1 }">
            图形
          </div>
          <div class="display-btn" @click="activeKeyA = 2" :class="{ 'active-display-btn': activeKeyA === 2 }">
            数据
          </div>
        </div>
        <div v-show="activeKeyA === 1">
          <div ref="chartRef2" style="height: 500px" />
          <div style="margin-top: 16px; width: 100%">
            <!-- table -->
            <el-table v-loading="loading" border :data="tableData" highlight-current-row style="width: 100%">
              <el-table-column label="类型" align="center" prop="type" />
              <el-table-column label="三相不平衡极值" align="center" prop="value" />
              <el-table-column label="发生时间" align="center" prop="time" />
              <el-table-column label="A相电流(A)" align="center" prop="valueA" />
              <el-table-column label="B相电流(A)" align="center" prop="valueB" />
              <el-table-column label="C相电流(A)" align="center" prop="valueC" />
            </el-table>
          </div>
        </div>
        <div v-show="activeKeyA === 2">
          <div style="margin-top: 16px; width: 100%">
            <!-- table -->
            <el-table v-loading="loading" border :data="chartData" highlight-current-row style="width: 100%">
              <el-table-column label="时间" align="center" prop="tableTime" />
              <el-table-column label="A相电流(A)" align="center" prop="valueA" />
              <el-table-column label="B相电流(A)" align="center" prop="valueB" />
              <el-table-column label="C相电流(A)" align="center" prop="valueC" />
            </el-table>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup name="ThreePhase">
import * as echarts from "echarts"
import { formatDate, formatDateObj } from "@/utils/index"
import { getThreePhaseUnbalance, getThreePhaseUnbalanceDetail } from "@/api/analysis/load"
import { nextTick, reactive, ref, onMounted, onUnmounted } from "vue"
import { listSite } from "@/api/manage/site"
import { listDevice } from "@/api/manage/device"
import { formatChartTime } from "../../utils"

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

const loading = ref(false)
const showSearch = ref(true)
const chartData = ref([])
const tableData = ref([
  {
    type: "最大值",
    value: "",
    time: "",
    valueA: "",
    valueB: "",
    valueC: "",
  },
  {
    type: "最小值",
    value: "",
    time: "",
    valueA: "",
    valueB: "",
    valueC: "",
  },
  // {
  //   type: "平均值",
  //   value: "",
  //   time: "--",
  // },
])
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
/** 查询列表 */
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
    requestType: activeTabKey.value == "first" ? 0 : 1,
  }
  getThreePhaseUnbalance(params).then((res) => {
    loading.value = false
    chartData.value = res.data
      ? res.data.map((item, index) => {
          let time = formatChartTime(
            queryParams.value.timeType,
            queryParams.value.timeType == "date" ? item.timeCode + ":00:00" : item.timeCode
          )
          return {
            time,
            tableTime: queryParams.value.timeType == "date" ? item.timeCode + "时" : item.timeCode,
            valueA: item.valueA == "--" ? null : +item.valueA,
            valueB: item.valueB == "--" ? null : +item.valueB,
            valueC: item.valueC == "--" ? null : +item.valueC,
          }
        })
      : []
    drawChart()
  })

  getThreePhaseUnbalanceDetail(params).then((res) => {
    tableData.value[0].time = res.data.maxTime
    tableData.value[0].value = res.data.max
    tableData.value[0].valueA = res.data.valueMaxA
    tableData.value[0].valueB = res.data.valueMaxB
    tableData.value[0].valueC = res.data.valueMaxC

    tableData.value[1].time = res.data.minTime
    tableData.value[1].value = res.data.min
    tableData.value[1].valueA = res.data.valueMinA
    tableData.value[1].valueB = res.data.valueMinB
    tableData.value[1].valueC = res.data.valueMinC
  })
}
/** 搜索按钮操作 */
function handleQuery() {
  getList()
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

// tabs
const activeTabKey = ref("first")
const activeKeyV = ref(1)
const activeKeyA = ref(1)
function changeTabs(e) {
  getList()
}

// 展示图表
const chartRef = ref(null)
const chartRef2 = ref(null)
let echartsInstance = null
let echartsInstance2 = null
function drawChart() {
  if (activeTabKey.value == "first") {
    echartsInstance = echarts.init(chartRef.value, "macarons")
  } else {
    echartsInstance2 = echarts.init(chartRef2.value, "macarons")
  }
  let option = {
    title: {
      text: "三相不平衡分析",
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
      data: ["A相", "B相", "C相"],
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
        name: "A相",
        type: "line",
        smooth: true,
        color: "#eec048",
        data: chartData.value.map((item) => {
          return item.valueA
        }),
      },
      {
        name: "B相",
        type: "line",
        smooth: true,
        color: "#56C6C8",
        data: chartData.value.map((item) => {
          return item.valueB
        }),
      },
      {
        name: "C相",
        type: "line",
        smooth: true,
        color: "#e7534f",
        data: chartData.value.map((item) => {
          return item.valueC
        }),
      },
    ],
  }

  if (activeTabKey.value == "first") {
    echartsInstance.setOption(option)
    echartsInstance.resize()
  } else {
    echartsInstance2.setOption(option)
    echartsInstance2.resize()
  }
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
  if (echartsInstance2) {
    echartsInstance2.resize()
  }
}
</script>

<style lang="scss" scoped>
:deep .el-form--inline .el-form-item {
  margin-right: 24px;
  // width: 100%;
}
.display-buttons {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-bottom: 16px;
  .display-btn,
  .active-display-btn {
    width: 78px;
    height: 34px;
    background: #fff;
    color: #409eff;
    border: 2px solid #409eff;
    border-radius: 4px;
    margin-left: 10px;
    text-align: center;
    line-height: 31px;
    font-size: 14px;
    font-weight: 400;
    cursor: pointer;
    &:hover {
      opacity: 0.9;
    }
  }
  .active-display-btn {
    background: #409eff;
    color: #fff;
  }
}
</style>
