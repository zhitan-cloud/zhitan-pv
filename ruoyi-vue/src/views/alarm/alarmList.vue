<template>
  <div class="app-container">
    <div class="page-view">
      <div class="left-statistic">
        <div class="alarm-btns">
          <div :class="queryParams.activeType === 0 ? 'active-btn' : 'btn'" @click="switchActive(0)">全部报警</div>
          <div :class="queryParams.activeType === 1 ? 'active-btn' : 'btn'" @click="switchActive(1)">未处理报警</div>
        </div>
        <!-- 共计报警 -->
        <div class="total-alarm">
          {{
            `共计${queryParams.activeType === 0 ? "报警" : "未处理报警"}${
              queryParams.activeType === 0 ? alarmInfo.alarmCount : alarmInfo.unprocessed
            }条`
          }}
        </div>
        <div class="chart-title">报警等级分析</div>
        <div ref="pieRef" style="height: 260px; width: 100%"></div>
        <!-- <div class="chart-title">处理情况分析</div>
        <div ref="gaugeRef" style="height: 260px; width: 100%"></div> -->
      </div>
      <div class="right-data" :style="'width:' + tableWidth + 'px'">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
          <el-row :gutter="12">
            <el-col :span="6">
              <el-form-item label="电站名称" prop="powerStationName">
                <el-input v-model="queryParams.powerStationName" placeholder="电站名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="设备名称" prop="deviceName">
                <el-input v-model="queryParams.deviceName" placeholder="设备名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="处理状态" prop="status">
                <el-select v-model="queryParams.status" placeholder="处理状态" clearable style="width: 100%">
                  <el-option v-for="dict in statusOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="报警等级" prop="level">
                <el-select v-model="queryParams.level" placeholder="报警等级" clearable style="width: 100%">
                  <el-option v-for="dict in levelOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="发生时间">
                <el-date-picker
                  v-model="dateRange"
                  value-format="YYYY-MM-DD"
                  type="daterange"
                  range-separator="-"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  style="width: 100%"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>

        <el-table
          v-loading="loading"
          :data="tableData"
          :height="tableHeight"
          highlight-current-row
          style="width: 100%"
          border
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column
            label="电站名称"
            align="center"
            prop="powerStationName"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            label="设备编码"
            align="center"
            prop="deviceCode"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            label="设备名称"
            align="center"
            width="160"
            prop="deviceName"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            label="报警编码"
            align="center"
            prop="errCode"
            width="120"
            :show-overflow-tooltip="true"
          ></el-table-column>
          <el-table-column
            label="报警描述"
            align="center"
            prop="errorDescription"
            width="180"
            :show-overflow-tooltip="true"
          />
          <el-table-column label="报警等级" align="center" prop="level" :show-overflow-tooltip="true">
            <template #default="scope">
              <div v-if="scope.row.level == 3" style="color: #20d2a3">普通</div>
              <div v-else-if="scope.row.level == 2" style="color: #deb650">严重</div>
              <div v-else style="color: #d22041">事故</div>
            </template>
          </el-table-column>
          <el-table-column label="处理状态" align="center" prop="remark">
            <template #default="scope">
              <div v-if="scope.row.status == 2">已处理</div>
              <div v-else style="color: red">未处理</div>
            </template>
          </el-table-column>
          <el-table-column label="发生时间" align="center" prop="createTime" width="160">
            <template #default="scope">
              <span>{{ parseTime(scope.row.dataTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" fixed="right" width="220" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-button link type="primary" @click="handleEdit(scope.row)"> 查看 </el-button>
              <el-button link type="primary" v-if="scope.row.status == 1" @click="handleEdit(scope.row)"
                >处理</el-button
              >
              <el-dropdown @command="(e) => handleCommand(e, scope.row)">
                <el-button type="primary" link style="margin: 2px 0 0 8px">
                  设置等级<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="3">普通</el-dropdown-item>
                    <el-dropdown-item command="2">严重</el-dropdown-item>
                    <el-dropdown-item command="1">事故</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
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
      </div>
    </div>

    <!-- 新增编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogOpen" width="700px" append-to-body>
      <el-form :model="currentForm" ref="userRef" label-width="100px" :rules="rules">
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属电站:" prop="powerStationName">
              {{ currentForm.powerStationName || "" }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发生时间:" prop="dataTime">
              {{ currentForm.dataTime || "" }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="逆变器名称:" prop="name"> {{ currentForm.deviceName }} </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="逆变器编码:" prop="code"> {{ currentForm.deviceCode }} </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报警等级:" prop="level">
              <div v-if="currentForm.level == 3" style="color: #20d2a3">普通</div>
              <div v-else-if="currentForm.level == 2" style="color: #deb650">严重</div>
              <div v-else style="color: #d22041">事故</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="处理状态:" prop="status">
              {{ currentForm.status == 1 ? "未处理" : "已处理" }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报警编码:" prop="type"> {{ currentForm.errCode }} </el-form-item>
          </el-col>
          <el-col :span="12" v-if="currentForm.processingTime">
            <el-form-item label="处理时间:" prop="type"> {{ currentForm.processingTime }} </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="报警描述:" prop="errorDescription"> {{ currentForm.errorDescription }} </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="处理意见:" prop="processingScenarios">
              <el-input
                v-if="currentForm.status == 1"
                type="textarea"
                v-model="currentForm.processingScenarios"
                placeholder="请输入处理意见"
                maxlength="200"
                style="width: 80%"
              />
              <div v-else>{{ currentForm.remark || "暂无" }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="上传照片:" prop="imageUrls">
              <div v-if="currentForm.status == 1">
                <ImageUpload v-model:modelValue="currentForm.imageUrls" :limit="3" @update="updateImages" />
              </div>
              <div v-else>
                <div v-if="imageUrlList && imageUrlList.length">
                  <img
                    v-for="(item, index) in imageUrlList"
                    :key="index"
                    :src="item"
                    style="
                      width: 145px;
                      height: 145px;
                      margin-right: 12px;
                      object-fit: contain;
                      border: 1px solid #eaeaea;
                      cursor: pointer;
                    "
                    @click="handlePictureCardPreview(item)"
                  />
                </div>
                <div v-else>暂未上传照片</div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" v-if="currentForm.status == 1" @click="submitForm">标记为已处理</el-button>
          <el-button @click="dialogOpen = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="dialogVisible" title="预览" width="800px" append-to-body>
      <img :src="dialogImageUrl" style="display: block; max-width: 100%; margin: 0 auto" />
    </el-dialog>
  </div>
</template>

<script setup name="LoadAnalysis">
import * as echarts from "echarts"
import { nextTick, reactive, ref } from "vue"
import { listHomeAlarm } from "@/api/analysis/home"
import ImageUpload from "../../components/ImageUpload"
import { getAlarmList, alarmHandling, getAlarmLevelAnalysis, editAlarmLevel } from "@/api/alarm/index"

const router = useRouter()
const { proxy } = getCurrentInstance()

const tableHeight = ref(window.innerHeight - 260)
const tableWidth = ref(window.innerWidth - 500)
const tableData = ref([])
const loading = ref(false)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(1)
const dateRange = ref([])

const statusOptions = ref([
  { label: `全部`, value: 0 },
  { label: `已处理`, value: 2 },
  { label: `未处理`, value: 1 },
])
const levelOptions = ref([
  { label: `全部`, value: 0 },
  { label: `普通`, value: 3 },
  { label: `严重`, value: 2 },
  { label: `事故`, value: 1 },
])

const data = reactive({
  form: {},
  queryParams: {
    deviceName: "",
    level: "",
    pageNum: 1,
    pageSize: 10,
    powerStationName: "",
    activeType: 0,
  },
})

const { queryParams, form } = toRefs(data)
const rules = ref({
  processingScenarios: [{ required: true, message: "请输入处理意见", trigger: "blur" }],
  imageUrls: [{ required: true, message: "请至少上传1张照片", trigger: "blur" }],
})

/** 查询列表 */
function getList() {
  loading.value = true
  let params = proxy.addDateRange(queryParams.value, dateRange.value)
  console.log(params)
  getAlarmList({ ...params, ...params.params }).then((res) => {
    loading.value = false
    tableData.value = res.rows
    total.value = res.total
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

function handleCommand(e, row) {
  console.log(e, row)
  if (e == row.level) {
    return
  }
  editAlarmLevel({
    alarmId: row.id,
    level: e,
  }).then((res) => {
    proxy.$modal.msgSuccess("设置成功")
    getList()
    getAnalysis()
  })
}

function switchActive(type) {
  queryParams.value.activeType = type
}

const pieData = ref([])
const alarmInfo = ref({})
function getAnalysis() {
  listHomeAlarm({}).then((res) => {
    alarmInfo.value = res.data
    pieData.value = [
      { name: "普通", value: res.data.level3 },
      { name: "严重", value: res.data.level2 },
      { name: "事故", value: res.data.level1 },
    ]
    drawEcharts()
  })
}
// 报警等级分析
const pieRef = ref(null)
let pieEchartsInstance = null
function drawEcharts() {
  nextTick(() => {
    pieEchartsInstance = echarts.init(pieRef.value, "macarons")
    let option = {
      title: false,
      tooltip: {
        trigger: "item",
      },
      legend: {
        // orient: 'vertical',
        left: "center",
      },
      series: [
        {
          name: "",
          type: "pie",
          radius: ["30%", "60%"],
          avoidLabelOverlap: false,
          top: 24,
          itemStyle: {
            borderRadius: 4,
            borderColor: "#fff",
            borderWidth: 1,
          },
          label: {
            formatter: "{b}\n{c}条\n{d}%",
          },
          labelLine: {
            length: 8,
            length2: 4,
          },
          color: ["#20d2a3", "#deb650", "#d22041"],
          data: pieData.value,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: "rgba(0, 0, 0, 0.5)",
            },
          },
        },
      ],
    }
    pieEchartsInstance.setOption(option)
  })
}

/** 新增编辑按钮操作 */
const dialogOpen = ref(false)
const dialogTitle = ref("")
const currentForm = ref({})

const imageUrlList = ref([])
function handleEdit(row) {
  dialogTitle.value = "报警详情"
  dialogOpen.value = true
  currentForm.value = row
  imageUrlList.value = row.imageUrl ? row.imageUrl.split(",") : []
  //   router.push("/system/user-auth/role/" + userId)
}
const dialogImageUrl = ref("")
const dialogVisible = ref(false)
// 预览
function handlePictureCardPreview(url) {
  dialogImageUrl.value = url
  dialogVisible.value = true
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
  ids.value = selection.map((item) => item.userId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}
/** 提交按钮 */
function submitForm() {
  console.log("imageUrls", currentForm.value)
  proxy.$refs["userRef"].validate((valid) => {
    if (valid) {
      alarmHandling({
        id: currentForm.value.id,
        processingScenarios: currentForm.value.processingScenarios,
        imageUrl: currentForm.value.imageUrls,
      }).then((response) => {
        proxy.$modal.msgSuccess("操作成功")
        dialogOpen.value = false
        getList()
        getAnalysis()
      })
    }
  })
}
onMounted(() => {
  handleQuery()
  getAnalysis()
  /* 解决图表宽度溢出问题 */
  // window.addEventListener("resize", setCharts)
})
</script>

<style lang="scss" scoped>
:deep(.el-form--inline .el-form-item) {
  margin-right: 0;
  width: 100%;
}
.page-view {
  display: flex;
  justify-content: space-between;
  // align-items: center;
  .left-statistic {
    width: 250px;
    border: 1px solid #3a71a8;
    min-height: 200px;
    margin-right: 12px;
    .alarm-btns {
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 2px solid #3a71a8;
      .btn,
      .active-btn {
        width: 50%;
        height: 40px;
        text-align: center;
        line-height: 40px;
        color: #3a71a8;
        cursor: pointer;
      }
      .active-btn {
        background: #3a71a8;
        color: #fff;
      }
    }
    .total-alarm {
      font-size: 18px;
      letter-spacing: 1px;
      text-align: center;
      line-height: 48px;
      margin: 12px auto;
    }
    .chart-title {
      width: 100%;
      height: 36px;
      text-align: left;
      padding: 0 12px;
      line-height: 36px;
      color: #fff;
      background: #7998b8;
      margin-bottom: 16px;
    }
  }
  .right-data {
    flex: 1;
    width: 100%;
  }
}
</style>
