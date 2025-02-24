<template>
  <div class="app-container" v-loading="loading">
    <div class="search-view">
      <span>电站：</span>
      <el-select v-model="powerStationId" placeholder="请选择电站" size="" @change="changeSite">
        <el-option v-for="dict in siteOptions" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
      </el-select>
      <!-- <span style="margin-left: 24px">设备：</span>
      <el-select v-model="queryParamsDeviceId" placeholder="请选择设备" size="">
        <el-option v-for="dict in deviceOptions" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
      </el-select> -->
      <el-radio-group v-model="ammeterValue" size="" style="margin-left: 12px" @change="changeAmmeterValue">
        <el-radio-button label="all">全部</el-radio-button>
        <el-radio-button label="1">电表</el-radio-button>
        <el-radio-button label="0">逆变器</el-radio-button>
      </el-radio-group>
      <el-button style="margin-left: 16px" type="primary" icon="Search" @click="getData">搜索</el-button>
    </div>

    <div class="content-view" v-if="tableData.length">
      <el-collapse v-model="activeCollapse" accordion>
        <el-collapse-item :name="item.deviceName" v-for="(item, index) in tableData" :key="index">
          <template #title>
            <div :class="item.offline ? 'header-error' : 'header'">
              {{ item.deviceName }}
              <img v-if="item.offline" src="@/assets/images/error.png" class="error-img" alt="" />
            </div>
          </template>
          <div class="content-array">
            <div v-for="(e, i) in item.indexArray" :key="i" :class="e.offline ? 'array-item-error' : 'array-item'">
              <div class="name">
                {{ e.name + (e.unit ? "（" + e.unit + "）" : "") }}
              </div>
              <div class="value">
                {{ e.value }}
              </div>
              <div class="time" style="display: flex; align-items: center">
                <img
                  style="margin-right: 5px; width: 13px"
                  src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAFo9M/3AAAABGdBTUEAALGPC/xhBQAAADhlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAAqACAAQAAAABAAAAEKADAAQAAAABAAAAEAAAAAAXnVPIAAABL0lEQVQ4Ea2SvS5GQRBA10901KLR6emUao+g1GooPYBEgtIToP0qjUS8ga8mEo0EveqrnLPZ3czeXDQmOXdnZmfuzsxuSgN5GdgDcwl7Ml+cE9YZrBS7X86iuYjxWRzPrKYmf6as+pnzU8TUNZjCEayDB2W5qQrrctD3q/5elbB6RD4m+JIZp2MbMajTF4pl5W/gJA5gG+6gSe4P67J5UmoDaZWy+RgCcmdOaiM4X9Gd2BU4jyyj7bDTtX6PI/ZsTRbeiQHOINbU3UWMNngXan3WdQsf8KN4pKV4fCynJuhzz5jR8mxqmOjY4uVj5pgWW9/oIRvn8GuJZiPGXIA5+Wpd7XFLZSD6vsB3G9+u/ifoxKu3x7/EmPZM4l9NdDgnsAcPMAVlE3bgGo5hBv8j33ORMTPKIFEYAAAAAElFTkSuQmCC"
                  alt=""
                />
                {{ e.dataTime }}
              </div>
            </div>
          </div>
        </el-collapse-item>
      </el-collapse>
    </div>
    <div class="no-data" v-if="!tableData.length">暂无数据</div>
  </div>
</template>

<script setup name="Site">
import { nextTick, onMounted, onUnmounted } from "vue"
import { listRealTime } from "@/api/analysis/load"
import { listSite } from "@/api/manage/site"
import { listDevice } from "@/api/manage/device"

const { proxy } = getCurrentInstance()
const queryParamsDeviceId = ref("")
const powerStationId = ref("")
const loading = ref(false)
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
      powerStationId.value = siteOptions.value[0].value
      // getDeviceData(siteOptions.value[0].value)
      getData()
    }
  })
}
function changeSite() {
  getData()
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

      queryParamsDeviceId.value = deviceOptions.value[0].value
    }
    getData()
  })
}

const activeCollapse = ref("")
const ammeterValue = ref("all")
const tableData = ref([])
function getData() {
  loading.value = true
  listRealTime({
    powerStationId: powerStationId.value,
    ammeter: ammeterValue.value == "all" ? "" : ammeterValue.value,
  })
    .then((res) => {
      loading.value = false
      tableData.value = res.data
      if (res.data && res.data.length) {
        activeCollapse.value = res.data[0].deviceName
      }
    })
    .catch(() => {
      loading.value = false
    })
}

function changeAmmeterValue(e) {
  console.log(e)
  getData()
}

onMounted(() => {
  getSiteData()
})
</script>

<style scoped lang="scss">
.search-view {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
.no-data {
  margin-top: 200px;
  text-align: center;
  width: 100%;
}
.content-view {
  margin-top: 16px;
  border-left: 1px solid #eaeaea;
  border-right: 1px solid #eaeaea;
  .header,
  .header-error {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding: 0 28px;
    width: 100%;
    background: #f9f9f9;
    font-size: 15px;
  }
  .header-error {
    color: #eb3425;
    .error-img {
      width: 25px;
      height: 25px;
    }
  }

  .content-array {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    flex-wrap: wrap;
    .array-item,
    .array-item-error {
      background: #ffffff;
      border-radius: 8px;
      border: 1px solid #e5e5e5;
      margin-bottom: 5px;
      margin-right: 5px;
      height: 96px;
      // width: percentage(1/6);
      width: 230px;
      //border: 1px solid #e9e9e9;
      padding: 8px 0 0 8px;
      font-size: 18px;
      font-weight: 600;
      color: #000000;
      cursor: pointer;
      text-align: center;
      .name {
        font-size: 12px;
        font-weight: 400;
        color: #666;
        margin-bottom: 4px;
        text-align: left;
      }

      .value {
        font-size: 22px;
        font-weight: 500;
        color: #409eff;
      }

      .time {
        font-size: 12px;
        font-weight: 400;
        color: #999;
        margin-top: 0px;
        text-align: left;
      }
    }
    .array-item-error {
      border: 1px solid #eb3425;
    }
  }

  :deep(.el-collapse-item__arrow) {
    position: absolute;
    left: 20px;
  }
  :deep(.el-collapse-item__wrap) {
    padding: 12px 12px 0 12px;
  }
}
</style>
