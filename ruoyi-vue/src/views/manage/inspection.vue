<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-row :gutter="12">
        <el-col :span="4">
          <el-form-item label="电站名称" prop="powerStationName">
            <el-input
              v-model="queryParams.powerStationName"
              placeholder="请输入电站名称"
              clearable
              style="width: 100%"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label="设备名称" prop="deviceName">
            <el-input
              v-model="queryParams.deviceName"
              placeholder="请输入设备名称"
              clearable
              style="width: 100%"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="4">
          <el-form-item label="设备编码" prop="deviceCode">
            <el-input
              v-model="queryParams.deviceCode"
              placeholder="请输入设备编码"
              clearable
              style="width: 100%"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="5">
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="11" class="operation-view">
          <el-form-item>
            <el-button type="primary" plain icon="plus" @click="handleAdd"> 新增 </el-button>
            <!-- <el-button type="warning" plain icon="Download" @click="handleExport">导出</el-button> -->
            <el-button type="success" plain icon="download" @click="handleExport"> 导出 </el-button>
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
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="电站名称" align="center" prop="powerStationName"></el-table-column>
      <el-table-column label="设备名称" align="center" prop="deviceName"></el-table-column>
      <!-- <el-table-column label="设备编码" align="center" prop="deviceCode"></el-table-column> -->
      <el-table-column
        label="检修结果"
        align="center"
        prop="inspectionResult"
        :show-overflow-tooltip="true"
      ></el-table-column>
      <el-table-column label="检修类型" align="center" prop="inspectionType" width="160">
        <template #default="scope">
          <span v-if="scope.row.inspectionType == 0">设备点检</span>
          <span v-if="scope.row.inspectionType == 1">设备维修</span>
          <span v-if="scope.row.inspectionType == 2">甲方停机</span>
        </template>
      </el-table-column>
      <el-table-column label="持续时长" align="center" prop="downtime" width="160">
        <template #default="scope">
          <span>{{ scope.row.downtime }}小时</span>
        </template>
      </el-table-column>
      <el-table-column label="操作人员" align="center" prop="inspectionStaff" :show-overflow-tooltip="true">
      </el-table-column>
      <el-table-column label="操作起止时间" align="center" prop="inspectionTime">
        <template #default="scope">
          <span v-if="scope.row.inspectionStartTime">{{
            scope.row.inspectionStartTime + "至" + scope.row.inspectionEndTime
          }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <!-- <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
       -->
      <el-table-column label="操作" align="center" fixed="right" width="150" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" @click="handleEdit(scope.row)"> 编辑 </el-button>
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
    <el-dialog :title="dialogTitle" v-model="dialogOpen" width="900px" append-to-body>
      <el-form :model="currentForm" :rules="rules" ref="userRef" label-width="200px">
        <el-row>
          <el-col :span="20">
            <el-form-item label="项目名称:" prop="powerStationName">
              <!-- <el-select
                v-model="currentForm.powerStationId"
                placeholder="请选择电站"
                size=""
                clearable
                style="width: 100%"
                @change="(e) => getDeviceData(e)"
              >
                <el-option
                  v-for="dict in siteOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select> -->
              <el-input v-model="currentForm.powerStationName" placeholder="请输入项目名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="设备名称:" prop="deviceName">
              <!-- <el-select
                v-model="currentForm.deviceCode"
                placeholder="请选择设备"
                size=""
                clearable
                style="width: 100%"
              >
                <el-option
                  v-for="dict in deviceOptions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select> -->
              <el-input v-model="currentForm.deviceName" placeholder="请输入设备名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="20">
            <el-form-item label="设备编码:" prop="code">
              <el-input v-model="currentForm.code" placeholder="请输入设备编码" maxlength="30" />
            </el-form-item>
          </el-col> -->
          <el-col :span="20">
            <el-form-item label="操作时间:" prop="inspectionStartTime">
              <!-- <el-date-picker
                style="width: 100%"
                v-model="currentForm.inspectionTime"
                type="datetime"
                placeholder="请选择操作日期"
                value-format="YYYY-MM-DD HH:mm:ss"
              /> -->
              <el-date-picker
                v-model="dateRange"
                value-format="YYYY-MM-DD HH:mm:ss"
                type="datetimerange"
                range-separator="-"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                style="width: 100%"
                @change="changeDatePicker"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="操作人:" prop="inspectionStaff">
              <el-input v-model="currentForm.inspectionStaff" placeholder="请输入操作人" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="操作类型" prop="type">
              <el-radio-group v-model="currentForm.type" size="" style="margin-right: 12px">
                <el-radio label="0">设备点检</el-radio>
                <el-radio label="1">设备维修</el-radio>
                <el-radio label="2">甲方停机</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="更换备件名称/数量:" prop="sparePartNameOrNumber">
              <el-input v-model="currentForm.sparePartNameOrNumber" placeholder="更换备件名称/数量" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="预估损失电量/kWh:" prop="estimatedPowerLoss">
              <el-input-number v-model="currentForm.estimatedPowerLoss" placeholder="预估损失电量" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="检修结果:" prop="inspectionResult">
              <el-input
                type="textarea"
                v-model="currentForm.inspectionResult"
                placeholder="请输入检修结果"
                maxlength="200"
              />
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="维修/更换(新/旧)备件照片:" prop="imageUrls">
              <!-- <el-upload
                class="avatar-uploader"
                action="/prod-api/common/upload"
                :headers="headers"
                :on-success="handleAvatarSuccess"
                :show-file-list="false"
                :before-upload="beforeUpload"
              >
                <img v-if="imageUrl" :src="imageUrl" class="avatar" />
                <el-button v-else type="primary" plain> 选择图片 </el-button>
              </el-upload> -->
              <div>
                <ImageUpload v-model:modelValue="currentForm.imageUrls" :limit="5" @update="updateImages" />
              </div>
            </el-form-item>
          </el-col>
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
  </div>
</template>

<script setup name="Inspection">
import { listInspection, addInspection, updateInspection, deleteInspection } from "@/api/manage/inspection"
import { listSite } from "@/api/manage/site"
import { listDevice } from "@/api/manage/device"
import { reactive, ref } from "vue"
import { downloadFile } from "@/api/download"
import { getToken } from "@/utils/auth"

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
const dateRange = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    type: undefined,
  },
})
const { queryParams, form } = toRefs(data)
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

      // queryParams.value.powerStationId = siteOptions.value[0].value
      // getDeviceData(siteOptions.value[0].value)
    }
  })
}
// 查询所有设备
const deviceOptions = ref()
function getDeviceData(powerStationId) {
  deviceOptions.value = []
  currentForm.value.deviceCode = ""
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

      // queryParams.value.deviceId = deviceOptions.value[0].value
      // getList()
    }
  })
}
/** 查询列表 */
function getList() {
  loading.value = true
  let params = proxy.addDateRange(queryParams.value, dateRange.value)
  console.log(params)
  listInspection({ ...params }).then((res) => {
    loading.value = false
    tableData.value = res.rows
    total.value = res.total
  })
}

const headers = ref({
  Authorization: "Bearer " + getToken(),
})
const imageUrl = ref("")
/** 上传预处理 */
function beforeUpload(file) {
  if (file.type.indexOf("image/") == -1) {
    proxy.$modal.msgError("文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。")
    return false
  }
  return true
  //  else {
  //   const reader = new FileReader();
  //   reader.readAsDataURL(file);
  //   reader.onload = () => {
  //     options.img = reader.result;
  //   };
  // }
}
const handleAvatarSuccess = (response, uploadFile) => {
  if (response.code === 200) {
    imageUrl.value = response.url
  } else {
    proxy.$modal.msgError("文件上传失败")
  }
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
const currentForm = ref({
  type: "0",
})
const rules = ref({
  //powerStationId: [{ required: true, message: "电站不能为空", trigger: "change" }],
  //deviceCode: [{ required: true, message: "设备不能为空", trigger: "change" }],
  inspectionStartTime: [{ required: true, message: "操作时间不能为空", trigger: "change" }],
  inspectionStaff: [{ required: true, message: "操作人不能为空", trigger: "blur" }],
  sparePartNameOrNumber: [{ required: true, message: "更换备件不能为空", trigger: "blur" }],
  estimatedPowerLoss: [{ required: true, message: "预估损失电量不能为空", trigger: "blur" }],
  imageUrls: [{ required: true, message: "请至少上传1张照片", trigger: "blur" }],
  powerStationName: [{ required: true, message: "请填写项目名称", trigger: "blur" }],
  deviceName: [{ required: true, message: "请填写设备名称", trigger: "blur" }],
})

const changeDatePicker = (e) => {
  currentForm.value.inspectionStartTime = e[0]
  currentForm.value.inspectionEndTime = e[1]
}
function handleAdd() {
  dialogTitle.value = "新增记录"
  dialogOpen.value = true
  currentForm.value = {
    type: "0",
  }
  imageUrl.value = ""
}

function handleEdit(row) {
  dialogTitle.value = "编辑记录"
  dialogOpen.value = true
  currentForm.value = {
    ...row,
    type: row.inspectionType,
    imageUrls: row.annex,
  }
  dateRange.value = [row.inspectionStartTime, row.inspectionEndTime]
  imageUrl.value = row.annex || ""
}

/** 删除按钮操作 */
function handleDelete(row) {
  const jobIds = row.id
  proxy.$modal
    .confirm("是否确认删除该条记录?")
    .then(function () {
      return deleteInspection(jobIds)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => {})
}
/** 导出按钮操作 */
function handleExport() {
  downloadFile("/inspection/export", `设备点检记录_${new Date().getTime()}.xlsx`, queryParams.value)
  // proxy.download(
  //   "inspection/export",
  //   {
  //     ...queryParams.value,
  //   },
  //   `设备点检记录_${new Date().getTime()}.xlsx`
  // )
}

/** 选择条数  */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.userId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 提交按钮 */
function submitForm() {
  console.log(currentForm.value)
  proxy.$refs["userRef"].validate((valid) => {
    if (valid) {
      // let arr = deviceOptions.value.filter((item) => {
      //   return item.value == currentForm.value.deviceCode
      // })
      // let deviceName = ""
      // if (arr.length !== 0) {
      //   deviceName = arr[0].label
      // }
      let params = proxy.addDateRange({}, dateRange.value, "Time")
      let newParams = {
        ...currentForm.value,
        inspectionType: currentForm.value.type,
        deviceCode: currentForm.value.deviceName,
        annex: currentForm.value.imageUrls,
        inspectionStartTime: params.params.beginTime,
        inspectionEndTime: params.params.endTime,
      }

      if (currentForm.value.id != undefined) {
        updateInspection(newParams).then((response) => {
          proxy.$modal.msgSuccess("修改成功")
          dialogOpen.value = false
          getList()
        })
      } else {
        addInspection(newParams).then((response) => {
          proxy.$modal.msgSuccess("新增成功")
          dialogOpen.value = false
          getList()
        })
      }
    }
  })
}
onMounted(() => {
  getSiteData()
  getDeviceData("")
  handleQuery()
})
</script>

<style lang="scss" scoped>
:deep .el-form--inline .el-form-item {
  margin-right: 0;
  width: 100%;
}

.avatar-uploader {
  .avatar {
    // width: 178px;
    height: 178px;
    display: block;
  }
}
</style>
