<template>
  <div class="app-container">
    <!--检索部分-->
    <el-form :model="queryParams" ref="queryRef" inline label-width="68px">
      <el-form-item label="电站名称" prop="site">
        <el-select v-model="queryParams.site" placeholder="请选择电站" filterable size="" clearable>
          <el-option v-for="dict in siteOptions" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="设备名称" prop="deviceName">
        <el-select v-model="queryParams.device" placeholder="请选择设备" filterable size="" clearable>
          <el-option
            v-for="dict in deviceOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="" prop="type" label-width="0">
        <!-- <el-radio-group v-model="queryParams.timeType" size="" style="margin-right: 12px" @change="changeTimeType">
          <el-radio-button label="hour">时</el-radio-button>
          <el-radio-button label="date">日</el-radio-button>
          <el-radio-button label="month">月</el-radio-button>
          <el-radio-button label="year">年</el-radio-button>
        </el-radio-group> -->
        <el-date-picker
          v-model="queryParams.timeData"
          value-format="YYYY-MM"
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

    <div class="scrollx-list">
      <el-row :gutter="12" style="width: 100%">
        <el-col :span="4" v-for="(item, index) in totalList" :key="index">
          <div class="list-item">
            <div class="item-title" :style="`color:${item.color}`">
              <span v-if="index !== 0" :style="`background:${item.color}`"></span>
              {{ item.label }}
            </div>
            <div class="item-content">
              <div class="left">发电量/kWh</div>
              <div class="right">{{ item.consumption }}</div>
            </div>
            <div class="item-content" v-if="index !== 0">
              <div class="left">发电占比</div>
              <div class="right">{{ item.consumptionRate }}%</div>
            </div>
            <div class="item-content">
              <div class="left">收益/¥</div>
              <div class="right">{{ item.cost }}</div>
            </div>
            <div class="item-content" v-if="index !== 0">
              <div class="left">收益占比</div>
              <div class="right">{{ item.costRate }}%</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 图表部分 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span>发电量（单位：kWh）</span>
          <!-- <el-button class="button" text>Operation</el-button> -->
        </div>
      </template>
      <div ref="chartRef" style="height: 400px"></div>
    </el-card>

    <el-card style="margin-top: 12px">
      <template #header>
        <div class="card-header">
          <span>收益量（单位：元）</span>
          <!-- <el-button class="button" text>Operation</el-button> -->
        </div>
      </template>
      <div ref="costChartRef" style="height: 400px"></div>
    </el-card>
  </div>
</template>

<script setup name="LoopAnalysis">
import * as echarts from "echarts"
import { formatDate } from "@/utils/index"
import { listSite } from "@/api/manage/site"
import { listDevice } from "@/api/manage/device"
import { nextTick, reactive, ref, onMounted, onUnmounted } from "vue"
import { peakValleySegment } from "@/api/peaks/index"
// import { useRoute } from "vue-router"

const route = useRoute()
const { proxy } = getCurrentInstance()

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
const loading = ref(false)
const multipleSelection = ref([])
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(1)
const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    timeType: "month",
    timeData: formatDate(new Date()).substring(0, 7),
    timeStr: "",
    device: undefined,
    site: undefined,
  },
})

const { queryParams } = toRefs(data)
const typeOptions = ref([
  { label: `合计`, value: "total", color: "#333" },
  { label: `尖`, value: "tip", color: "#e7534f" },
  { label: `峰`, value: "peak", color: "#eec048" },
  { label: `平`, value: "flat", color: "#56C6C8" },
  { label: `谷`, value: "trough", color: "#6590f2" },
  { label: `深谷`, value: "deep", color: "#3A71A8" },
])
const totalList = ref([])
const costList = ref([])
const powerConsumptionList = ref([])
/** 查询列表 */
function getList() {
  loading.value = true
  let params = {
    dateTime: queryParams.value.timeData,
    deviceId: queryParams.value.device,
    powerStationId: queryParams.value.site,
  }
  peakValleySegment(params).then((res) => {
    loading.value = false
    const resp = res.data
    costList.value = resp.costList
    powerConsumptionList.value = resp.powerConsumptionList

    totalList.value = []
    for (let i = 0; i < typeOptions.value.length; i++) {
      let label = typeOptions.value[i].label
      let value = typeOptions.value[i].value
      totalList.value.push({
        label,
        color: typeOptions.value[i].color,
        cost: resp[value + "PowerCost"] || 0,
        consumption:
          resp[value + "PowerConsumption"] > 10000
            ? (resp[value + "PowerConsumption"] / 10000).toFixed(2) + "万"
            : resp[value + "PowerConsumption"],
        costRate: resp[value + "PowerCostProportion"] || "0",
        consumptionRate: resp[value + "PowerProportion"] || "0",
      })
    }

    // 电量chart
    drawChart("chartRef", powerConsumptionList.value)
    // 费用chart
    drawChart("costChartRef", costList.value)
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}
// 展示图表
const chartRef = ref(null)
const costChartRef = ref(null)
let echartsInstance = null
let costEchartsInstance = null
function drawChart(dom, data) {
  let chartData = {
    yAxisName: "",
    xData: [],
    yData: [],
  }
  if (dom == "chartRef") {
    chartData.yAxisName = "发电量（单位：kWh）"
    echartsInstance = echarts.init(chartRef.value, "macarons")
  } else {
    chartData.yAxisName = "收益量（单位：元）"
    costEchartsInstance = echarts.init(costChartRef.value, "macarons")
  }

  for (let i = 1; i < typeOptions.value.length; i++) {
    let label = typeOptions.value[i].label
    let value = typeOptions.value[i].value
    chartData.xData = data.map((item) => {
      return new Date(item.xdata).getDate() + "日"
    })
    chartData.yData.push({
      color: typeOptions.value[i].color,
      name: label,
      data: data.map((item) => {
        return item[`y${value}`]
      }),
    })
  }
  let option = {
    tooltip: {
      trigger: "axis",
      axisPointer: {
        // Use axis to trigger tooltip
        type: "shadow", // 'shadow' as default; can also be 'line' or 'shadow'
      },
    },
    legend: {
      top: 0,
    },
    grid: {
      // top: 16,
      left: "10",
      right: "10",
      bottom: "12",
      containLabel: true,
    },
    yAxis: {
      type: "value",
      // name: chartData.yAxisName,
      // nameGap: 25,
      // nameTextStyle: {
      //   align: "left",
      //   fontSize: 16,
      //   color: "#000",
      //   // fontWeight: 600,
      // },
    },
    xAxis: {
      type: "category",
      data: chartData.xData,
    },
    // series: [
    // {
    //   name: "尖",
    //   type: "bar",
    //   stack: "total",
    //   label: {
    //     show: true,
    //   },
    //   emphasis: {
    //     focus: "series",
    //   },
    //   data: [320, 302, 301, 334, 390, 330, 320],
    // },
    // ],
    series: chartData.yData.map((item) => {
      return {
        name: item.name,
        type: "bar",
        stack: "total",
        label: {
          show: true,
        },
        emphasis: {
          focus: "series",
        },
        color: item.color,
        data: item.data,
      }
    }),
  }

  if (dom == "chartRef") {
    echartsInstance.setOption(option)
    echartsInstance.resize()
  } else {
    costEchartsInstance.setOption(option)
    costEchartsInstance.resize()
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

/** 选择条数  */
function handleSelectionChange(selection) {
  multipleSelection.value = selection
  ids.value = selection.map((item) => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

onMounted(() => {
  getSiteData()
  getDeviceData()
  handleQuery()
  // drawChart()
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
  if (costEchartsInstance) {
    costEchartsInstance.resize()
  }
}
</script>

<style lang="scss" scoped>
:deep .el-form--inline .el-form-item {
  margin-right: 24px;
  // width: 100%;
}
.scrollx-list {
  display: flex;
  justify-content: flex-start;
  width: 100%;
  margin-bottom: 12px;
  .list-item {
    width: 100%;
    height: 100%;
    padding-bottom: 4px;
    border: 1px solid #e9e9e9;
    &:last-child {
      margin-right: 0;
    }
    .item-title {
      font-size: 15px;
      letter-spacing: 2px;
      font-weight: 500;
      padding: 10px 12px;
      margin-bottom: 4px;
      border-bottom: 1px solid #e9e9e9;
      color: #333;
      span {
        display: inline-block;
        width: 10px;
        height: 10px;
        margin-right: 4px;
      }
    }
    .item-content {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      line-height: 32px;
      padding: 0 10px;
      font-size: 13px;
      .left {
        width: 90px;
        color: #666;
      }
      .right {
        color: #333;
        flex: 1;
        font-size: 20px;
        font-weight: 500;
        text-align: right;
      }
    }
  }
}
</style>
