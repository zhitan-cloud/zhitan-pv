<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form :model="queryParams" ref="queryRef" inline v-show="showSearch" label-width="68px">
      <el-form-item label="设备名称" prop="deviceId">
        <el-select v-model="queryParams.deviceId" filterable placeholder="请选择设备" size="" clearable>
          <el-option
            v-for="dict in deviceOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="" prop="ammeter" label-width="0">
        <el-radio-group v-model="ammeterValue" size="" style="margin-left: 0" @change="handleQuery">
          <el-radio-button label="all">全部</el-radio-button>
          <el-radio-button label="1">电表</el-radio-button>
          <el-radio-button label="0">逆变器</el-radio-button>
        </el-radio-group>
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
        <el-button type="primary" plain icon="Histogram" @click="handleChart">图表</el-button>
      </el-form-item>

      <!-- <el-form-item style="float: right; margin-right: 0">
        <el-button type="success" plain icon="download" @click="handleExport"> 导出 </el-button>
      </el-form-item> -->
    </el-form>

    <el-table
      v-loading="loading"
      ref="multipleTableRef"
      :data="tableData"
      border
      :height="tableHeight"
      highlight-current-row
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        label="设备名称"
        align="left"
        fixed="left"
        prop="deviceName"
        width="150"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column label="合计" align="center" fixed="left" prop="sumValue" />
      <el-table-column
        :label="item.time"
        width="110"
        align="center"
        v-for="(item, index) in columns"
        :prop="'value' + index"
      >
        <template #default="scope">
          {{ scope.row.timeList[index].value }}
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 新增编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogOpen" width="900px" append-to-body>
      <!-- <div style="font-size: 20px">{{ queryParams.timeStr }}</div> -->
      <div ref="chartRef" style="height: 540px"></div>
    </el-dialog>
  </div>
</template>

<script setup name="DeviceSum">
import * as echarts from "echarts"
import { formatDate, formatDateObj, formatChartTime } from "@/utils/index"
import { getInverterGenerationStats } from "@/api/manage/device"
import { listDevice } from "@/api/manage/device"
import { nextTick, reactive, ref, onMounted, onUnmounted } from "vue"

const router = useRouter()
const { proxy } = getCurrentInstance()

// 查询所有电站
const deviceOptions = ref()
function getDeviceData() {
  listDevice({
    pageNum: 1,
    pageSize: 10000,
  }).then((res) => {
    if (res.rows && res.rows.length) {
      deviceOptions.value = res.rows
        ? res.rows.map((item) => {
            return { label: item.name, value: item.id }
          })
        : []
    }
  })
}

const tableHeight = ref(window.innerHeight - 210)
const tableData = ref([])
const loading = ref(false)
const showSearch = ref(true)
const multipleSelection = ref([])
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(1)
const dateRange = ref([])

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 20,
    powerStationId: undefined,
    timeType: "month",
    timeData: formatDate(new Date()),
    timeStr: "",
  },
})

const { queryParams } = toRefs(data)
const columns = ref([])

function changeTimeType(e) {
  handleQuery()
}

const ammeterValue = ref("1")
/** 查询用户列表 */
function getList() {
  loading.value = true
  let dateObj = formatDateObj(queryParams.value.timeData)
  let queryTime = ""
  if (queryParams.value.timeType == "date") {
    queryTime = dateObj.year + "-" + dateObj.month + "-" + dateObj.day
  } else if (queryParams.value.timeType == "month") {
    queryTime = dateObj.year + "-" + dateObj.month
  } else {
    queryTime = dateObj.year + "-" + dateObj.month + "-" + dateObj.day
  }
  let params = {
    ...queryParams.value,
    dataTime: queryTime,
    timeTypeEnum: queryParams.value.timeType == "date" ? "DAY" : queryParams.value.timeType.toUpperCase(),
    ammeter: ammeterValue.value == "all" ? "" : ammeterValue.value,
  }
  getInverterGenerationStats(params).then((res) => {
    loading.value = false
    tableData.value = res.rows
    total.value = res.total
    if (res.rows && res.rows.length) {
      columns.value = res.rows[0].timeList.map((item) => {
        return {
          ...item,
          time: queryParams.value.timeType == "date" ? formatChartTime("day", item.time + ":00:00") : item.time,
        }
      })
    }
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}
// 展示图表--图表弹窗
const dialogOpen = ref(false)
const dialogTitle = ref("")
const chartRef = ref(null)
function handleChart() {
  let dateObj = formatDateObj(queryParams.value.timeData)
  if (queryParams.value.timeType == "year") {
    queryParams.value.timeStr = dateObj.year
  } else if (queryParams.value.timeType == "month") {
    queryParams.value.timeStr = dateObj.year + "-" + dateObj.month
  } else {
    queryParams.value.timeStr = dateObj.year + "-" + dateObj.month + "-" + dateObj.day
  }
  if (ids.value.length == 0) {
    proxy.$modal.msgError("请先选择一行数据!")
    return
  }
  dialogTitle.value = `设备发电统计（${queryParams.value.timeStr}）`
  dialogOpen.value = true
  nextTick(() => {
    drawChart()
  })
}
let echartsInstance = null
function drawChart() {
  echartsInstance = echarts.init(chartRef.value, "macarons")
  let option = {
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
      data: multipleSelection.value.map((item) => {
        return item.deviceName
      }),
      itemGap: 5,
    },
    grid: {
      top: 36,
      left: "1%",
      right: "2%",
      containLabel: true,
    },
    xAxis: [
      {
        type: "category",
        data: columns.value.map((item) => {
          return queryParams.value.timeType == "date"
            ? item.time
            : formatChartTime(queryParams.value.timeType, item.time)
        }),
      },
    ],
    yAxis: [
      {
        type: "value",
        name: "单位：kWh",
        axisLabel: {
          formatter: function (a) {
            a = +a
            return a
            // return isFinite(a) ? echarts.format.addCommas(+a / 1000) : ""
          },
        },
      },
    ],
    dataZoom: [
      {
        show: true,
        start: 0,
        end: 100,
      },
      {
        type: "inside",
        start: 94,
        end: 100,
        // show: false,
      },
      {
        show: false, // y轴
        yAxisIndex: 0,
        filterMode: "empty",
        width: 30,
        height: "80%",
        showDataShadow: false,
        left: "93%",
      },
    ],
    series: multipleSelection.value.map((item) => {
      let arr = []
      arr = item.timeList.map((item) => {
        return item.value
      })
      return {
        name: item.deviceName,
        type: "bar",
        data: arr,
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

/** 选择条数  */
function handleSelectionChange(selection) {
  multipleSelection.value = selection
  ids.value = selection.map((item) => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

onMounted(() => {
  getDeviceData()
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
