<template>
  <div class="app-container">
    <el-row :gutter="12">
      <el-col :span="8">
        <el-card>
          <div class="card-one">
            <div class="search-view">
              <span>电站：</span>
              <el-select
                v-model="powerStationId"
                filterable
                placeholder="请选择电站"
                size=""
                @change="(e) => getDeviceData(e)"
                style="width: 140px"
              >
                <el-option
                  v-for="dict in siteOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
              <span style="margin-left: 15px">设备：</span>
              <el-select
                filterable
                v-model="queryParamsDeviceId"
                placeholder="请选择设备"
                size=""
                @change="getData"
                style="width: 140px"
              >
                <el-option
                  v-for="dict in deviceOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
              <el-button style="margin-left: 12px" icon="Refresh" type="primary" @click="getData"></el-button>
            </div>
            <div class="content-view">
              <img src="../../assets/images/device.png" alt="" />
              <div class="info-view">
                <div>厂家：{{ deviceInfo.factory || "--" }}</div>
                <div>设备编码：{{ deviceInfo.code }}</div>
                <!-- <div>状态：运行正常</div> -->
                <div>额定交流功率：{{ deviceInfo.ratedAcPower || "--" }}</div>
                <!-- <div>实时发电功率：0.06kW</div> -->
                <div>累计发电量：{{ deviceInfo.sumValue }}kWh</div>
                <!-- <div>组件峰值功率：38kWp</div> -->
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>发电量信息(kWh)</span>
            </div>
          </template>
          <div class="list-1" style="height: 150px">
            <div v-for="(item, index) in list1" :key="index" class="list-item-1">
              <img :src="getImageUrl(`img-${index + 1}`)" alt="" />
              <div class="img-label">
                {{ item.label }}
              </div>
              <div class="value-div">{{ item.value }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>收益信息(元)</span>
              <!-- <el-button class="button" text>Operation</el-button> -->
            </div>
          </template>
          <div class="list-1" style="height: 150px">
            <div v-for="(item, index) in list11" :key="index" class="list-item-1">
              <img :src="getImageUrl(`img-${index + 1}`)" alt="" />
              <div class="img-label">
                <span>{{ item.label }}</span>
              </div>
              <div class="value-div">{{ item.value }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" style="margin-top: 12px">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>实时发电功率</span>
            </div>
          </template>
          <div ref="gaugeRef" style="height: 260px; width: 100%"></div>
        </el-card>
      </el-col>
      <el-col :span="16" style="margin-top: 12px">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>发电量趋势</span>
              <div class="date-wrap" style="width: 180px">
                <div :class="timeType == 'year' ? 'switch-date-active' : 'switch-date'" @click="switchLoopDate('year')">
                  本年
                </div>
                <div
                  :class="timeType == 'month' ? 'switch-date-active' : 'switch-date'"
                  @click="switchLoopDate('month')"
                >
                  本月
                </div>
                <div :class="timeType == 'day' ? 'switch-date-active' : 'switch-date'" @click="switchLoopDate('day')">
                  本日
                </div>
              </div>
            </div>
          </template>
          <div ref="lineChartRef" style="height: 260px; width: 100%"></div>
        </el-card>
      </el-col>
    </el-row>
    <div>
      <el-table
        :data="tableData"
        :span-method="arraySpanMethod"
        highlight-current-row
        style="width: 100%; margin-top: 12px"
      >
        <el-table-column label="交流侧" align="center" prop="a" :show-overflow-tooltip="true">
          <el-table-column label="参数" align="center" prop="name" :show-overflow-tooltip="true" />
          <el-table-column label="A相" align="center" prop="valueA" :show-overflow-tooltip="true" />
          <el-table-column label="B相" align="center" prop="valueB" :show-overflow-tooltip="true" />
          <el-table-column label="C相" align="center" prop="valueC" :show-overflow-tooltip="true" />
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup name="Site">
import { getImageUrl } from "@/utils/index"
import * as echarts from "echarts"
import { nextTick, onMounted, onUnmounted } from "vue"
import {
  getPowerGenerationInfo,
  getImplementedPower,
  getACMeasurementsByDeviceId,
  deviceDetail,
  getInverterInfo,
} from "@/api/manage/device"
import { listSite } from "@/api/manage/site"
import { listDevice } from "@/api/manage/device"
import { formatChartTime } from "../../utils"
import { useRoute, useRouter } from "vue-router"

const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()
const queryParamsDeviceId = ref("")
const powerStationId = ref("")
// 查询所有电站
const siteOptions = ref()
function getSiteData() {
  if (route.query.device) {
    deviceDetail(route.query.device).then((res) => {
      powerStationId.value = res.data.powerStationId
    })
  }
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
      if (!route.query.device) {
        powerStationId.value = siteOptions.value[0].value
      }
      getDeviceData(powerStationId.value)
    }
  })
}
// 查询所有设备
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

      queryParamsDeviceId.value = route.query.device || deviceOptions.value[0].value
      getData()
    } else {
      deviceOptions.value = []
      resetData()
    }
  })
}
const list1 = ref([
  { label: "日发电量", value: "0" },
  { label: "月发电量", value: "0" },
  { label: "年发电量", value: "0" },
])
const list11 = ref([
  { label: "日收益", value: "0" },
  { label: "月收益", value: "0" },
  { label: "年收益", value: "0" },
])
const timeType = ref("month")
function switchLoopDate(type) {
  timeType.value = type
  getLineData()
}
function getLineData() {
  getImplementedPower({ id: queryParamsDeviceId.value, timeType: timeType.value.toUpperCase() }).then((res) => {
    drawEcharts(res.data.realTimePower)

    let arr = res.data.itemList.map((item) => {
      return {
        ...item,
        time: formatChartTime(timeType.value, timeType.value == "day" ? item.time + ":00:00" : item.time),
      }
    })
    drawLineChart(arr)
  })
}
const tableData = ref([])
const deviceInfo = ref({})
const arraySpanMethod = ({ row, column, rowIndex, columnIndex }) => {
  if (rowIndex === 2 || rowIndex === 3 || rowIndex === 4) {
    if (columnIndex === 1) {
      return [1, 3]
    }
    // else if (columnIndex === 1) {
    //   return [0, 0]
    // }
  }
}
function getData() {
  let id = queryParamsDeviceId.value
  getInverterInfo({ id }).then((res) => {
    deviceInfo.value = res.data
  })
  getACMeasurementsByDeviceId({ id }).then((res) => {
    tableData.value = [
      {
        name: "电压(V)",
        valueA: res.data.avoltage || "--",
        valueB: res.data.bvoltage || "--",
        valueC: res.data.bvoltage || "--",
      },
      {
        name: "电流(A)",
        valueA: res.data.acurrent || "--",
        valueB: res.data.bcurrent || "--",
        valueC: res.data.bcurrent || "--",
      },
      {
        name: "总有功功率(kW)",
        valueA: res.data.totalActivePower || "--",
      },
      {
        name: "功率因数",
        valueA: res.data.factor,
      },
      {
        name: "频率(Hz)",
        valueA: res.data.frequency || "--",
      },
    ]
  })

  getPowerGenerationInfo({ id }).then((res) => {
    list1.value[0].value = res.data.dayValue || 0
    list1.value[1].value = res.data.monthValue || 0
    list1.value[2].value = res.data.yearValue || 0
    list11.value[0].value = res.data.dayCost || 0
    list11.value[1].value = res.data.monthCost || 0
    list11.value[2].value = res.data.yearCost || 0
  })

  getLineData()
}

function resetData() {
  queryParamsDeviceId.value = ""
  deviceInfo.value = {}
  list1.value[0].value = 0
  list1.value[1].value = 0
  list1.value[2].value = 0
  list11.value[0].value = 0
  list11.value[1].value = 0
  list11.value[2].value = 0
  tableData.value = []

  drawEcharts(0)
  drawLineChart([])
}

// echarts-gauge
const gaugeRef = ref(null)
let echartsInstance = null
function drawEcharts(data) {
  nextTick(() => {
    echartsInstance = echarts.init(gaugeRef.value, "macarons")
    echartsInstance.setOption({
      tooltip: {
        formatter: "{a} <br/> {c}",
      },
      series: [
        {
          name: "实时发电功率(kW)",
          type: "gauge",
          progress: {
            show: true,
          },
          detail: {
            valueAnimation: true,
            formatter: "{value}",
            fontSize: 18,
          },
          min: 0,
          max: 2400,
          data: [
            {
              value: data,
              name: "",
            },
          ],
          title: {
            fontSize: 14,
          },
        },
      ],
    })
  })
}

// echarts-line
const lineChartRef = ref(null)
let lineInstance = null
const drawLineChart = (data) => {
  nextTick(() => {
    lineInstance = echarts.init(lineChartRef.value)
    lineInstance.setOption({
      title: {
        text: "",
      },
      tooltip: {
        trigger: "axis",
      },
      legend: {
        data: ["发电量"],
      },
      grid: {
        left: 12,
        right: 12,
        bottom: 12,
        top: 36,
        containLabel: true,
      },
      // toolbox: {
      //   feature: {
      //     saveAsImage: {},
      //     dataView: {},
      //     restore: {},
      //     magicType: {
      //       type: ["line", "bar", "stack"],
      //     },
      //   },
      // },
      xAxis: {
        type: "category",
        boundaryGap: false,
        data: data.map((item) => {
          return item.time
        }),
      },
      yAxis: {
        type: "value",
        name: "单位：kWh",
      },
      series: [
        {
          name: "发电量",
          type: "line",
          stack: "Total",
          data: data.map((item) => {
            return item.value
          }),
        },
      ],
    })
    lineInstance.resize()
  })
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
  if (lineInstance) {
    lineInstance.resize()
  }
  if (echartsInstance) {
    echartsInstance.resize()
  }
}
</script>

<style scoped lang="scss">
.card-one {
  height: 200px;
  .search-view {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
  .content-view {
    display: flex;
    justify-content: flex-start;
    img {
      width: 140px;
      // height: 200px;
      margin-right: 16px;
    }
    .info-view {
      margin-top: 12px;
      div {
        line-height: 30px;
      }
    }
  }
}

.card-header {
  width: 100%;
  height: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  &::before {
    content: " ";
    display: inline-block;
    width: 3px;
    height: 20px;
    border-radius: 4px;
    background-color: #3a71a8;
    position: absolute;
  }
  span {
    margin-left: 12px;
  }

  .date-wrap {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    background: #eaeaea;
    border-radius: 12px;
    height: 26px;
    float: right;
    cursor: pointer;
    .switch-date,
    .switch-date-active {
      // font-size: 30px;
      color: #333;
      width: 60px;
      height: 26px;
      line-height: 26px;
      text-align: center;
    }
    .switch-date-active {
      // background: center center #4a54ff;
      // background-image: linear-gradient(315deg, #6772ff 0, #00f9e5 100%);
      // background-size: 104% 104%;
      background-color: #5aa8f6;
      color: #fff;
      font-weight: 500;
      border-radius: 24px;
    }
  }
}

.list-1,
.list-2 {
  display: flex;
  // justify-content: space-around;
  flex-direction: column;
  flex-wrap: wrap;
  align-items: center;
  .list-item-1 {
    width: 30%;
    text-align: center;
    // margin: 16px 0 0;

    img {
      width: 52px;
      height: 52px;
      margin-top: 16px;
      margin-bottom: 8px;
    }
    .img-label {
      margin-bottom: 8px;
      text-align: center;
    }
    .value-div {
      font-size: 22px;
    }
  }
  .list-item-2 {
    width: 30%;
    margin-bottom: 24px;
    text-align: center;

    img {
      width: 70px;
      height: 70px;
      margin-top: 12px;
      margin-bottom: 12px;
    }
    .value-div {
      font-size: 20px;
      margin-top: 12px;
    }
  }
}
</style>
