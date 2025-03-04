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
      <el-form-item style="float: right; margin-right: 0">
        <el-button type="success" plain icon="download" @click="handleExport"> 导出 </el-button>
      </el-form-item>
    </el-form>

    <!-- table -->
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
      <!-- <el-table-column type="selection" width="55" /> -->
      <!-- <el-table-column label="电站名称" prop="siteName" :show-overflow-tooltip="true"></el-table-column> -->
      <el-table-column prop="timeNameStr" label="时段名称" align="center" fixed="left" />
      <el-table-column prop="timePeriod" label="时段" width="110" align="center" fixed="left" />
      <el-table-column prop="sumValue" label="合计" align="center" />
      <el-table-column :label="item.time" v-for="(item, index) in columns" :key="index" width="110" align="center">
        <template #default="scope">
          {{ scope.row.timeList[index].value }}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup name="LoopAnalysis">
import { formatDate } from "@/utils/index"
import { listSite } from "@/api/manage/site"
import { listDevice } from "@/api/manage/device"
import { peakValleyReport, exportPeaks } from "@/api/peaks/index"
import { downloadFile } from "@/api/download"
import { nextTick, reactive, ref, onMounted } from "vue"
import { useRoute } from "vue-router"

const route = useRoute()
const { proxy } = getCurrentInstance()

const tableHeight = ref(window.innerHeight - 160)
const typeOptionsObj = ref({
  tip: `尖`,
  peak: `峰`,
  flat: "平",
  trough: "谷",
  deep: "深谷",
})
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
const dateRange = ref([])

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
const columns = ref([])
/** 查询列表 */
function getList() {
  loading.value = true
  let params = {
    dateTime: queryParams.value.timeData,
    deviceId: queryParams.value.device,
    powerStationId: queryParams.value.site,
  }
  peakValleyReport(params).then((res) => {
    loading.value = false
    if (res.data.length) {
      columns.value = res.data[0].timeList
      tableData.value = res.data.map((item) => {
        return {
          ...item,
          timeNameStr: typeOptionsObj.value[item.timeName],
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

/** 导出按钮操作 */
function handleExport() {
  let params = {
    dateTime: queryParams.value.timeData,
    deviceId: queryParams.value.device,
    powerStationId: queryParams.value.site,
  }
  downloadFile("/peakValley/export", `尖峰平谷_${new Date().getTime()}.xlsx`, params)
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
})
</script>

<style lang="scss" scoped>
:deep .el-form--inline .el-form-item {
  margin-right: 24px;
  // width: 100%;
}
</style>
