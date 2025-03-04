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

    <!-- 图表部分 -->
    <div ref="chartRef" style="height: 500px"></div>

    <!-- table -->
    <el-descriptions class="margin-top" title="" :column="2" border size="large">
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">最大负荷</div>
        </template>
        {{ detailData.max || "--" }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">发生时间</div>
        </template>
        {{ detailData.maxTime || "--" }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">最小负荷</div>
        </template>
        {{ detailData.min || "--" }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">发生时间</div>
        </template>
        {{ detailData.minTime || "--" }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">平均负荷</div>
        </template>
        {{ detailData.avg || "--" }}
      </el-descriptions-item>
      <el-descriptions-item>
        <template #label>
          <div class="cell-item">负荷率</div>
        </template>
        {{ detailData.rate || "--" }}
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script setup name="LoadAnalysis">
import * as echarts from "echarts"
import { formatDate, formatDateObj, formatChartTime } from "@/utils/index"
import { getLoadAnalysis, getLoadAnalysisDetail } from "@/api/analysis/load"
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
const tableData = ref()
const loading = ref(false)
const showSearch = ref(true)
const data = reactive({
  queryParams: {
    timeType: "date",
    timeData: formatDate(new Date()),
    timeStr: "",
    powerStationId: undefined,
    deviceId: undefined,
  },
})
const { queryParams } = toRefs(data)
const detailData = ref({})
const chartData = ref({})

function changeTimeType(e) {
  getList()
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
  }
  getLoadAnalysis(params).then((res) => {
    loading.value = false
    let itemList = res.data || []
    tableData.value = itemList.map((item) => {
      return {
        ...item,
        tableTime: queryParams.value.timeType == "date" ? item.timeCode + "时" : item.timeCode,
      }
    })

    chartData.value = {}
    if (tableData.value.length) {
      if (queryParams.value.timeType == "date") {
        let arr = []
        itemList.forEach((item) => {
          let time = formatChartTime(
            queryParams.value.timeType,
            queryParams.value.timeType == "date" ? item.timeCode + ":00:00" : item.timeCode
          )
          arr.push({
            time,
            value: item.value === "--" ? null : +item.value,
            name: "负荷值",
          })
        })
        chartData.value = {
          time: arr.map((item) => {
            return item.time
          }),
          yAxis: [
            {
              name: "负荷值",
              data: arr.map((item) => {
                return item.value
              }),
            },
          ],
        }
      } else {
        let avgArr = []
        let maxArr = []
        let minArr = []
        itemList.forEach((item) => {
          let time = formatChartTime(
            queryParams.value.timeType,
            queryParams.value.timeType == "date" ? item.timeCode + ":00:00" : item.timeCode
          )
          avgArr.push({
            time,
            value: item.avg === "--" ? null : +item.avg,
            name: "平均负荷",
          })
          maxArr.push({
            time,
            value: item.max === "--" ? null : +item.max,
            name: "最大负荷",
          })
          minArr.push({
            time,
            value: item.min === "--" ? null : +item.min,
            name: "最小负荷",
          })
        })
        chartData.value = {
          time: avgArr.map((item) => {
            return item.time
          }),
          yAxis: [
            {
              name: "平均负荷",
              data: avgArr.map((item) => {
                return item.value
              }),
            },
            {
              name: "最大负荷",
              data: maxArr.map((item) => {
                return item.value
              }),
            },
            {
              name: "最小负荷",
              data: minArr.map((item) => {
                return item.value
              }),
            },
          ],
        }
      }
    }

    nextTick(() => {
      drawChart()
    })
  })
  getLoadAnalysisDetail(params).then((res) => {
    detailData.value = res.data
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
// 展示图表--图表弹窗
const chartRef = ref(null)
let echartsInstance = null
function drawChart() {
  echartsInstance = echarts.init(chartRef.value, "macarons")
  let option = {
    title: {
      text: "负荷分析",
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
      data: chartData.value.yAxis.map((item) => {
        return item.name
      }),
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
        data: chartData.value.time,
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
    series: chartData.value.yAxis.map((item) => {
      return {
        name: item.name,
        type: "line",
        smooth: true,
        label: {
          show: true,
        },
        data: item.data,
      }
    }),
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
