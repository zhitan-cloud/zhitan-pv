<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-row :gutter="12">
        <el-col :span="4">
          <el-form-item label="设备编码" prop="code">
            <el-input
              v-model="queryParams.code"
              placeholder="请输入设备编码"
              clearable
              style="width: 100%"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label="设备名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入设备名称"
              clearable
              style="width: 100%"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label="设备类型" prop="deviceTypeId">
            <el-select v-model="queryParams.deviceTypeId" placeholder="设备类型" clearable style="width: 100%">
              <el-option v-for="dict in typeOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label="所属电站" prop="powerStationId">
            <el-select v-model="queryParams.powerStationId" placeholder="所属电站" clearable style="width: 100%">
              <el-option v-for="dict in siteOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="4" class="operation-view">
          <el-form-item>
            <el-button type="success" plain icon="plus" @click="handleAdd"> 新增 </el-button>
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
      <el-table-column label="设备编码" align="center" prop="code" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="设备名称" align="center" prop="name" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column
        label="设备类型"
        align="center"
        prop="deviceType"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column
        label="所属电站"
        width="150"
        align="center"
        prop="powerStationName"
        :show-overflow-tooltip="true"
      />
      <el-table-column label="是否电表" align="center" prop="ammeter" width="">
        <template #default="scope">
          {{ scope.row.ammeter == "1" ? "是" : "否" }}
        </template>
      </el-table-column>
      <!-- <el-table-column label="容量" align="center" width="150" prop="capacity" :show-overflow-tooltip="true" /> -->
      <el-table-column label="生产厂家" align="center" width="" prop="factory" :show-overflow-tooltip="true" />
      <!-- <el-table-column label="电网类型" align="center" width="150" prop="gridType" :show-overflow-tooltip="true" /> -->
      <el-table-column
        label="额定交流功率(kW)"
        align="center"
        width=""
        prop="ratedAcPower"
        :show-overflow-tooltip="true"
      />
      <!-- <el-table-column label="组件峰值功率(kW)" align="center" prop="modulePeakPower" :show-overflow-tooltip="true" /> -->
      <el-table-column label="创建时间" align="center" prop="createTime" width="">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" @click="handleEdit(scope.row)"> 编辑 </el-button>
          <el-button link type="primary" @click="openRecordDialog(scope.row)"> 检修记录 </el-button>
          <el-button link type="danger" @click="handleDelete(scope.row)"> 删除 </el-button>
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
    <el-dialog :title="dialogTitle" v-model="dialogOpen" width="700px" append-to-body>
      <el-form :model="currentForm" :rules="rules" ref="userRef" label-width="150px">
        <el-row>
          <el-col :span="20">
            <el-form-item label="设备名称:" prop="name">
              <el-input v-model="currentForm.name" placeholder="请输入设备名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="设备编码:" prop="code">
              <el-input
                v-model="currentForm.code"
                placeholder="请输入设备编码"
                maxlength="30"
                :disabled="!!currentForm.id"
              />
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="设备类型:" prop="deviceTypeId">
              <el-select v-model="currentForm.deviceTypeId" placeholder="设备类型" clearable style="width: 100%">
                <el-option v-for="dict in typeOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="所属电站:" prop="powerStationId">
              <el-select v-model="currentForm.powerStationId" placeholder="所属电站" clearable style="width: 100%">
                <el-option v-for="dict in siteOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="是否电表:" prop="ammeter">
              <el-radio-group v-model="currentForm.ammeter">
                <el-radio label="1" border>是</el-radio>
                <el-radio label="0" border>否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="20">
            <el-form-item label="容量:" prop="capacity">
              <el-input type="number" v-model="currentForm.capacity" placeholder="请输入容量">
                <template #append>M</template>
              </el-input>
            </el-form-item>
          </el-col> -->
          <el-col :span="20">
            <el-form-item label="生产厂家:" prop="factory">
              <el-input v-model="currentForm.factory" placeholder="请输入生产厂家" maxlength="30" />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="20">
            <el-form-item label="电网类型:" prop="gridType">
              <el-input v-model="currentForm.gridType" placeholder="请输入电网类型" maxlength="30" />
            </el-form-item>
          </el-col> -->
          <el-col :span="20">
            <el-form-item label="额定交流功率:" prop="ratedAcPower">
              <el-input type="number" v-model="currentForm.ratedAcPower" placeholder="请输入额定交流功率">
                <template #append>kW</template>
              </el-input>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="20">
            <el-form-item label="组件峰值功率:" prop="modulePeakPower">
              <el-input type="number" v-model="currentForm.modulePeakPower" placeholder="请输入组件峰值功率">
                <template #append>kW</template>
              </el-input>
            </el-form-item>
          </el-col> -->
          <el-col :span="20">
            <el-form-item label="备注:" prop="remark">
              <el-input type="textarea" v-model="currentForm.remark" placeholder="请输入备注" maxlength="200" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="dialogOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 维修记录 -->
    <el-dialog title="设备检修记录" v-model="dialogRecordOpen" width="700px" append-to-body>
      <el-form :model="currentForm" ref="userRef" label-width="100px">
        <el-row>
          <el-col :span="20">
            <el-form-item label="设备名称:" prop="name">
              {{ currentForm.name }}
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="设备编码:" prop="code">
              {{ currentForm.code }}
            </el-form-item>
          </el-col>
          <el-col :span="23">
            <el-form-item label="检修记录:" prop="code">
              <el-timeline style="width: 100%">
                <el-timeline-item v-for="(activity, index) in timelineData" :key="index" type="primary" :hollow="true">
                  <div style="display: flex; justify-content: space-between">
                    <span>{{ activity.inspectionTime }}</span>
                  </div>
                  <el-card>
                    <div style="display: flex; justify-content: space-between">
                      <div>操作人：{{ activity.inspectionStaff || "--" }}</div>
                      <span>{{ activity.inspectionType == 0 ? "点检" : "维修" }}</span>
                    </div>

                    <div>结果：{{ activity.inspectionResult }}</div>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="dialogRecordOpen = false">确 定</el-button>
          <el-button @click="dialogRecordOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="SiteManage">
import {
  listDevice,
  addDevice,
  updateDevice,
  deleteDevice,
  deviceDetail,
  listDeviceInspectionByDeviceId,
} from "@/api/manage/device"
import { reactive, ref } from "vue"
import { listDeviceType } from "@/api/manage/deviceType"
import { listSite } from "@/api/manage/site"

const router = useRouter()
const { proxy } = getCurrentInstance()

const tableHeight = ref(window.innerHeight - 210)
const tableData = ref([])
const loading = ref(false)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(1)

const typeOptions = ref([])
const siteOptions = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    code: "",
    name: undefined,
    deviceTypeId: undefined,
  },
})

const { queryParams, form } = toRefs(data)
const dialogRecordOpen = ref(false)
const timelineData = ref([])
function openRecordDialog(row) {
  currentForm.value = row
  listDeviceInspectionByDeviceId({ id: row.id }).then((res) => {
    dialogRecordOpen.value = true
    timelineData.value = res.data.reverse()
  })
}

/** 查询用户列表 */
function getList() {
  loading.value = true
  listDevice(queryParams.value).then((res) => {
    loading.value = false
    tableData.value = res.rows
    total.value = res.total
  })
}

// 查询设备类型
function getDeviceType() {
  listDeviceType({
    pageNum: 1,
    pageSize: 10000,
  }).then((res) => {
    typeOptions.value = res.rows
      ? res.rows.map((item) => {
          return { label: item.name, value: item.id }
        })
      : []
  })
}
// 查询所有电站
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

/** 新增编辑按钮操作 */
const dialogOpen = ref(false)
const dialogTitle = ref("")
const currentForm = ref({})
const rules = ref({
  name: [{ required: true, message: "设备名称不能为空", trigger: "blur" }],
  code: [{ required: true, message: "设备编码不能为空", trigger: "blur" }],
  deviceTypeId: [{ required: true, message: "设备类型不能为空", trigger: "change" }],
  powerStationId: [{ required: true, message: "所属电站不能为空", trigger: "change" }],
})
function handleAdd() {
  dialogTitle.value = "新增设备"
  dialogOpen.value = true
  currentForm.value = {
    ammeter: "0",
  }
}

function handleEdit(row) {
  deviceDetail(row.id).then((res) => {
    dialogTitle.value = "编辑设备"
    dialogOpen.value = true
    currentForm.value = { ...res.data, ammeter: res.data.ammeter ? "1" : "0" }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const jobIds = row.id
  proxy.$modal
    .confirm("是否确认删除该设备?")
    .then(function () {
      return deleteDevice(jobIds)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}

/** 选择条数  */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.userId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["userRef"].validate((valid) => {
    if (valid) {
      let params = {
        ...currentForm.value,
        ammeter: currentForm.value.ammeter == "1",
      }
      if (currentForm.value.id != undefined) {
        updateDevice(params).then((response) => {
          proxy.$modal.msgSuccess("修改成功")
          dialogOpen.value = false
          getList()
        })
      } else {
        addDevice(params).then((response) => {
          proxy.$modal.msgSuccess("新增成功")
          dialogOpen.value = false
          getList()
        })
      }
    }
  })
}
onMounted(() => {
  getDeviceType()
  getSiteData()
  handleQuery()
})
</script>

<style lang="scss" scoped>
:deep .el-form--inline .el-form-item {
  margin-right: 0;
  width: 100%;
}
:deep .el-dialog__body {
  height: 70vh;
  overflow: hidden;
  overflow-y: auto;
}
:deep .el-timeline-item__node {
  top: 8px;
}
:deep .el-timeline-item__tail {
  top: 16px;
}
</style>
