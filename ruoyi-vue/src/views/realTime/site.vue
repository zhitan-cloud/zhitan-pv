<template>
  <div class="app-container">
    <el-row :gutter="12">
      <el-col :span="12">
        <el-card>
          <div class="card-one">
            <div class="search-view">
              <span>电站：</span>
              <el-select v-model="powerStationId" placeholder="请选择电站" size="" filterable @change="changeSite">
                <el-option
                  v-for="dict in siteOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
              <el-button style="margin-left: 12px" icon="Refresh" type="primary" @click="getData"></el-button>
            </div>
            <div class="content-view">
              <img src="../../assets/images/site.jpg" alt="" />
              <div class="info-view">
                <div>电站编码：{{ powerStationInfo.code }}</div>
                <div>补贴电价：{{ powerStationInfo.subsidizedPrices || "--" }} 元/kWh</div>
                <div>电站装机容量：{{ powerStationInfo.installedCapacity || "--" }} MW</div>
                <div>逆变器数量：{{ powerStationInfo.inverterCount || 0 }} 个</div>
                <div>总发电量：{{ powerStationInfo.sumValue || 0 }} kWh</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div class="card-one">
            <div class="card-header">
              <span>发电量信息(kWh)</span>
            </div>
            <div class="list-1">
              <div v-for="(item, index) in list1" :key="index" class="list-item-1">
                <div class="img-label">
                  <img :src="getImageUrl(`img-${index + 1}`)" alt="" />
                  <span>{{ item.label }}</span>
                </div>
                <div class="value-div">{{ item.value }}</div>
              </div>
            </div>
            <div class="card-header" style="margin-top: 16px">
              <span>收益信息(元)</span>
            </div>
            <div class="list-1">
              <div v-for="(item, index) in list11" :key="index" class="list-item-1">
                <div class="img-label">
                  <img :src="getImageUrl(`img-${index + 1}`)" alt="" />
                  <span>{{ item.label }}</span>
                </div>
                <div class="value-div">{{ item.value }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="24" style="margin-top: 12px">
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
          <div ref="lineChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="24" style="margin-top: 12px">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>设备</span>
              <!-- <el-button class="button" text>Operation</el-button> -->
            </div>
          </template>
          <div class="device-list" v-if="deviceList.length">
            <div v-for="item in deviceList" :key="item.id" class="device-item">
              <div class="header-view">
                <span>{{ item.name }}</span>
                <span>{{ item.type }}</span>
              </div>
              <div class="desc-view">
                <div class="left">
                  <img src="../../assets/images/device.png" alt="" />
                  <div @click="toDevice(item)" class="btn">查看详情</div>
                </div>
                <div class="right">
                  <div>厂家：{{ item.factory || "--" }}</div>
                  <div>设备编码：{{ item.code }}</div>
                  <!-- <div>状态：运行正常</div> -->
                  <div>额定交流功率：{{ item.ratedAcPower || "--" }}</div>
                  <!-- <div>实时发电功率：0.06kW</div> -->
                  <div>累计发电量：{{ item.sumValue }}kWh</div>
                  <!-- <div>组件峰值功率：38kWp</div> -->
                </div>
              </div>
            </div>
          </div>
          <div v-else style="margin: 30px auto; text-align: center">暂无设备</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Site">
import { getImageUrl } from "@/utils/index"
import { formatChartTime } from "../../utils"
import * as echarts from "echarts"
import { nextTick, onMounted } from "vue"
import {
  listSite,
  listDeviceById,
  getPowerStationInfoById,
  getPowerGenerationInfo,
  getImplementedPower,
} from "@/api/manage/site"
import { useRoute, useRouter } from "vue-router"

const route = useRoute()
const { proxy } = getCurrentInstance()
const powerStationId = ref("")
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
      powerStationId.value = route.query.site || siteOptions.value[0].value
      getData()
    }
  })
}
function changeSite() {
  getData()
}
const list1 = ref([
  { label: "日发电量", value: "0" },
  { label: "月发电量", value: "0" },
  { label: "年发电量", value: "" },
])
const list11 = ref([
  { label: "日收益", value: "0" },
  { label: "月收益", value: "0" },
  { label: "年收益", value: "0" },
])

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

const deviceList = ref([])
const powerStationInfo = ref({})
const timeType = ref("month")
function switchLoopDate(type) {
  timeType.value = type
  getLineData()
}
function getLineData() {
  getImplementedPower({ id: powerStationId.value, timeType: timeType.value.toUpperCase() }).then((res) => {
    let arr = res.data.map((item) => {
      return {
        ...item,
        time: formatChartTime(timeType.value, timeType.value == "day" ? item.time + ":00:00" : item.time),
      }
    })
    drawLineChart(arr)
  })
}
function getData() {
  let id = powerStationId.value
  listDeviceById({ id }).then((res) => {
    let inverterList = res.data.inverterList
      ? res.data.inverterList.map((item) => {
          return { ...item, type: "逆变器" }
        })
      : []
    let ammeterList = res.data.ammeterList
      ? res.data.ammeterList.map((item) => {
          return { ...item, type: "电表" }
        })
      : []

    deviceList.value = inverterList.concat(ammeterList)
  })
  getPowerStationInfoById({ id }).then((res) => {
    powerStationInfo.value = res.data
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
const router = useRouter()
function toDevice(item) {
  router.push("/realTime/device?device=" + item.id)
}
onMounted(() => {
  getSiteData()
  /* 解决图表宽度溢出问题 */
})
</script>

<style scoped lang="scss">
.card-one {
  height: 240px;
  .search-view {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }
  .content-view {
    display: flex;
    justify-content: flex-start;
    position: relative;

    img {
      width: 240px;
      // height: 200px;
      // margin-right: 16px;
    }
    .abs-title {
      position: absolute;
      bottom: 30px;
      left: 115px;
    }
    .info-view {
      margin-top: 16px;
      div {
        line-height: 36px;
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

.device-list {
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
  .device-item {
    width: 542px;
    // padding: 0 12px;
    margin-right: 12px;
    border: 1px solid #3a71a8;
    margin-bottom: 12px;
    .header-view {
      height: 40px;
      line-height: 40px;
      width: 100%;
      color: #fff;
      background-color: #3a71a8;
      padding: 0 10px;
      display: flex;
      justify-content: space-between;
    }
  }
  .desc-view {
    display: flex;
    justify-content: flex-start;
    align-items: flex-start;
    .left {
      padding: 0 24px 12px;
      text-align: center;
      img {
        width: 130px;
      }
      .btn {
        background: #6189b1;
        color: #fff;
        width: 100px;
        height: 30px;
        line-height: 30px;
        margin: 0px auto;
        border-radius: 2px;
        font-size: 14px;
        cursor: pointer;
      }
    }
    .right {
      margin-top: 12px;
      div {
        line-height: 30px;
      }
    }
  }
}
.list-1,
.list-2 {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  .list-item-1 {
    width: 30%;
    margin: 16px 0 8px;

    img {
      width: 30px;
      height: 30px;
      margin-right: 8px;
    }
    .img-label {
      display: flex;
      justify-content: center;
      align-items: center;
      margin-bottom: 8px;
    }
    .value-div {
      text-align: center;
      font-size: 22px;
    }
  }
}
</style>
