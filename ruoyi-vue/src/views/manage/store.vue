<template>
  <div class="app-container">
    <el-tabs v-model="activeTabKey" @tab-click="changeTabs" type="border-card">
      <el-tab-pane name="first" label="备品备件">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
          <el-row :gutter="12">
            <el-col :span="5">
              <el-form-item label="产品名称" prop="name">
                <el-input v-model="queryParams.name" placeholder="请输入名称" clearable style="width: 100%"
                  @keyup.enter="handleQuery" />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
              </el-form-item>
            </el-col>
            <el-col :span="13" class="operation-view">
              <el-form-item>
                <el-button type="success" plain icon="plus" @click="handleAdd"> 新品入库 </el-button>
                <el-button type="primary" plain icon="download" @click="handleExport"> 导出 </el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>

        <el-table v-loading="loading" :data="tableData" :height="tableHeight" highlight-current-row style="width: 100%"
          @selection-change="handleSelectionChange">
          <el-table-column label="产品编码" align="center" prop="code" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="产品名称" align="center" prop="name" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="规格型号" align="center" prop="specs"></el-table-column>
          <el-table-column label="库存地点" align="center" prop="location" :show-overflow-tooltip="true" />
          <el-table-column label="库存数量" align="center" prop="amount" :show-overflow-tooltip="true" />
          <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
          <el-table-column label="创建时间" align="center" prop="createTime">
            <template #default="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-button link type="primary" @click="handleEdit(scope.row, 1)"> 入库 </el-button>
              <el-button link type="success" @click="handleEdit(scope.row, 2)"> 出库 </el-button>
              <el-button link type="primary" @click="handleEdit(scope.row, 3)"> 编辑产品 </el-button>
              <el-button link type="danger" @click="handleDelete(scope.row)"> 删除产品 </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize" @pagination="getList" />
      </el-tab-pane>
      <el-tab-pane name="second" label="出入库记录">
        <el-form :model="queryParams2" ref="queryRef2" :inline="true" v-show="showSearch" label-width="68px">
          <el-row :gutter="24">
            <el-col :span="4">
              <el-form-item label="产品名称" prop="name">
                <el-input v-model="queryParams2.name" placeholder="请输入名称" clearable style="width: 100%"
                  @keyup.enter="handleQuery" />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="出入库地点" label-width="90px">
                <el-select v-model="queryParams2.locationId" placeholder="出入库地点" clearable style="width: 100%">
                  <el-option v-for="dict in addressOptions" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="类型" prop="operationType" label-width="48">
                <el-select v-model="queryParams2.operationType" placeholder="类型" clearable style="width: 100%">
                  <el-option label="出库" :value="1" />
                  <el-option label="入库" :value="0" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="操作日期">
                <el-date-picker v-model="dateRange" value-format="YYYY-MM-DD" type="daterange" range-separator="-"
                  start-placeholder="开始日期" end-placeholder="结束日期" style="width: 100%"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
                <el-button type="primary" plain icon="download" @click="handleRecordsExport"> 导出 </el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-table v-loading="loading" :data="tableData2" :height="tableHeight" highlight-current-row
          style="width: 100%">
          <el-table-column label="产品编码" align="center" prop="code" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="产品名称" align="center" prop="name" :show-overflow-tooltip="true"></el-table-column>
          <el-table-column label="规格型号" align="center" prop="specs"></el-table-column>
          <el-table-column label="数量" align="center" prop="amount" :show-overflow-tooltip="true" />
          <el-table-column label="出入库地点" align="center" prop="location" :show-overflow-tooltip="true" />
          <el-table-column label="类型" align="center" prop="status">
            <template #default="scope">
              <span>{{ scope.row.status == 1 ? "出库" : "入库" }}</span>
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
          <el-table-column label="出入库时间" align="center" prop="movementDate" :show-overflow-tooltip="true" />
          <el-table-column label="创建时间" align="center" prop="createTime">
            <template #default="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total2 > 0" :total="total2" v-model:page="queryParams2.pageNum"
          v-model:limit="queryParams2.pageSize" @pagination="getRecordList" />
      </el-tab-pane>
    </el-tabs>

    <!-- 新增编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogOpen" width="700px" append-to-body>
      <el-form :model="currentForm" :rules="rules" ref="userRef" label-width="150px">
        <el-row>
          <el-col :span="dialogTitle == '新增产品' || dialogTitle == '编辑产品' ? 20 : 12">
            <el-form-item label="产品名称:" prop="name">
              <el-input v-if="dialogTitle == '新增产品' || dialogTitle == '编辑产品'" v-model="currentForm.name"
                placeholder="请输入产品名称" maxlength="30" />
              <div v-else>{{ currentForm.name }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="dialogTitle == '新增产品' || dialogTitle == '编辑产品' ? 20 : 12">
            <el-form-item label="产品编码:" prop="code">
              <el-input v-if="dialogTitle == '新增产品' || dialogTitle == '编辑产品'" v-model="currentForm.code"
                placeholder="请输入产品编码" maxlength="30" />
              <div v-else>{{ currentForm.code }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="dialogTitle == '新增产品' || dialogTitle == '编辑产品' ? 20 : 12">
            <el-form-item label="规格型号:" prop="specs">
              <el-input v-if="dialogTitle == '新增产品' || dialogTitle == '编辑产品'" v-model="currentForm.specs"
                placeholder="请输入规格型号" maxlength="30" />
              <div v-else>{{ currentForm.specs }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="dialogTitle == '新增产品' || dialogTitle == '编辑产品' ? 20 : 12">
            <el-form-item label="库存地点:" prop="address">
              <div style="display: flex" v-if="dialogTitle == '新增产品' || dialogTitle == '编辑产品'">
                <el-select v-model="currentForm.address" placeholder="请选择库存地点" size="" style="width: 200px">
                  <el-option v-for="dict in addressOptions" :key="dict.value" :label="dict.label"
                    :value="dict.value"></el-option>
                </el-select>
                <el-button style="margin-left: 12px" type="primary" @click="handleAddAddress">去新建</el-button>
              </div>
              <div v-else>{{ currentForm.addressName }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="dialogTitle == '新增产品' || dialogTitle == '编辑产品' ? 20 : 12">
            <el-form-item :label="dialogTitle == '新增产品' || dialogTitle == '编辑产品' ? '库存数量:' : '当前库存:'" prop="amount">
              <el-input-number v-if="dialogTitle == '新增产品' || dialogTitle == '编辑产品'" :min="0" :step="1"
                v-model="currentForm.amount" placeholder="请输入库存数量" maxlength="30" />
              <div v-else>{{ currentForm.amount }}</div>
            </el-form-item>
          </el-col>
          <el-col :span="20" v-if="dialogTitle == '出库'">
            <el-form-item label="出库到" prop="outBound">
              <el-select v-model="currentForm.outBound" placeholder="请选择出库到哪里" size="" style="width: 200px">
                <el-option v-for="dict in addressOptions" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
              <el-button style="margin-left: 12px" type="primary" @click="handleAddAddress">去新建</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="20" v-if="dialogTitle != '新增产品' && dialogTitle != '编辑产品'">
            <el-form-item :label="dialogTitle + '数量:'" prop="editCount">
              <el-input-number style="width: 200px" :min="0" :max="dialogTitle == '出库' ? currentForm.amount : 999999"
                :step="1" v-model="currentForm.editCount" placeholder="请输入数量" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="20" v-if="dialogTitle != '编辑产品'">
            <el-form-item :label="dialogTitle == '出库' ? '出库时间:' : '入库时间:'" prop="operateTime">
              <el-date-picker v-model="currentForm.operateTime" type="date" format="YYYY-MM-DD"
                value-format="YYYY-MM-DD" clearable :placeholder="dialogTitle == '出库' ? '请选择出库时间' : '请选择入库时间'"
                :disabled-date="disabledDate" />
            </el-form-item>
          </el-col>
          <el-col :span="20" v-if="dialogTitle != '入库'">
            <el-form-item label="备注:" prop="remark">
              <el-input type="textarea" v-model="currentForm.remark" placeholder="请输入备注" maxlength="200" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancelForm">取 消</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 新建库存地点 -->
    <el-drawer title="地点列表" v-model="addressVisible" direction="rtl">
      <el-button type="primary" @click="handleAddrPush()" style="margin-bottom: 12px"> 新增地点 </el-button>
      <el-button type="primary" icon="Refresh" @click="getAllAddressData" style="margin-bottom: 12px" />
      <el-table :data="addressOptions" border>
        <el-table-column property="name" label="地点" width="200">
          <template #default="scope">
            <el-input v-if="scope.row.isEdit" v-model="scope.row.label"></el-input>
            <span v-else>{{ scope.row.label }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-button v-if="!scope.row.isEdit" link type="primary" @click="handleAddrEdit(scope.row)">
              编辑
            </el-button>
            <el-button v-else link type="primary" @click="handleAddrSave(scope.row)"> 保存 </el-button>
            <el-button link type="danger" @click="handleAddrDelete(scope.row, scope.$index)"> 删除 </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-drawer>
  </div>
</template>

<script setup name="Store">
import {
  listStore,
  addStore,
  editPart,
  updateStore,
  deleteStore,
  listOperationRecords,
  listAddress,
  addAddress,
  updateAddress,
  deleteAddress,
} from "@/api/manage/store"
import { listSite } from "@/api/manage/site"
import { reactive, ref } from "vue"

const router = useRouter()
const { proxy } = getCurrentInstance()

const tableHeight = ref(window.innerHeight - 270)
const tableData = ref([])
const tableData2 = ref([])
const loading = ref(false)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(1)
const total2 = ref(1)
const dateRange = ref([])

// tabs
const activeTabKey = ref("first")
function changeTabs(e) {
  activeTabKey.value = e.props.name
  console.log("changeTabs", e.props.name)
  handleQuery()
}

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    type: undefined,
  },
  form2: {},
  queryParams2: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    type: undefined,
  },
})

const { queryParams, form, queryParams2, form2 } = toRefs(data)
const disabledDate = (current) => {
  console.log(current)
  // Can not select days before today and today
  return current && new Date(current).getTime() > new Date().getTime()
}

/** 查询用户列表 */
function getList() {
  loading.value = true
  let params = proxy.addDateRange(queryParams.value, dateRange.value)
  console.log(params)
  listStore({ ...params, ...params.params }).then((res) => {
    loading.value = false
    tableData.value = res.rows
    total.value = res.total
  })
}
function getRecordList() {
  loading.value = true
  let params = proxy.addDateRange(queryParams2.value, dateRange.value, "Time")
  console.log(params)
  listOperationRecords({ ...params, startDate: params.params.beginTime, endDate: params.params.endTime }).then(
    (res) => {
      loading.value = false
      tableData2.value = res.rows
      total2.value = res.total
    }
  )
}
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
      console.log("siteOptions.value", siteOptions.value)
    }
  })
}

//库存地点
const addressOptions = ref([])
const addressVisible = ref(false)
function getAllAddressData() {
  listAddress().then((res) => {
    console.log(res.data)
    addressOptions.value = res.data.map((item) => {
      return { ...item, label: item.location, value: item.id, isEdit: false }
    })
    console.log(addressOptions.value)
  })
}
function handleAddAddress() {
  addressVisible.value = true
}
const handleAddrPush = () => {
  let hasEdit =
    addressOptions.value.filter((item) => {
      return item.isEdit
    }).length !== 0
  if (hasEdit) {
    proxy.$modal.msgError("请先保存完成后，再操作！")
    return
  }
  addressOptions.value.push({
    label: "",
    isEdit: true,
  })
}
const handleAddrEdit = (row) => {
  let hasEdit =
    addressOptions.value.filter((item) => {
      return item.isEdit
    }).length !== 0
  if (hasEdit) {
    proxy.$modal.msgError("请先保存完成后，再操作！")
    return
  }
  row.isEdit = true
}
const handleAddrSave = (row) => {
  if (row.label == "" || !row.label) {
    proxy.$modal.msgWarning("请输入库存地点")
    return
  }
  console.log(row)
  let params = {
    ...row,
    location: row.label,
  }
  if (row.id) {
    updateAddress(params).then((response) => {
      proxy.$modal.msgSuccess("编辑成功")
      dialogOpen.value = false
      getAllAddressData()
    })
  } else {
    addAddress(params).then((response) => {
      proxy.$modal.msgSuccess("新增成功")
      dialogOpen.value = false
      getAllAddressData()
    })
  }
}
const handleAddrDelete = (row, index) => {
  console.log(row, index)
  if (!row.id) {
    addressOptions.value.splice(index, 1)
    return
  }
  const jobIds = row.id
  proxy.$modal
    .confirm("是否确认删除该库存地点?")
    .then(function () {
      return deleteAddress(jobIds)
    })
    .then(() => {
      getAllAddressData()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => { })
}

/** 搜索按钮操作 */
function handleQuery() {
  console.log("activeTabKey.value", activeTabKey.value)
  if (activeTabKey.value == "first") {
    queryParams.value.pageNum = 1
    getList()
  } else {
    queryParams2.value.pageNum = 1
    getRecordList()
  }
}
/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = []
  proxy.resetForm("queryRef")
  proxy.resetForm("queryRef2")
  handleQuery()
}

/** 新增编辑按钮操作 */
const dialogOpen = ref(false)
const dialogTitle = ref("")
const currentForm = ref({})
const rules = ref({
  name: [{ required: true, message: "产品名称不能为空", trigger: "blur" }],
  code: [{ required: true, message: "产品编码不能为空", trigger: "blur" }],
  specs: [{ required: true, message: "规格型号不能为空", trigger: "blur" }],
  editCount: [{ required: true, message: "数量不能为空", trigger: "blur" }],
  amount: [{ required: true, message: "数量不能为空", trigger: "blur" }],
  operateTime: [{ required: true, message: "请选择时间", trigger: "blur" }],
  address: [{ required: true, message: "请选择库存地点", trigger: "blur" }],
  outBound: [{ required: true, message: "请选择出库地点", trigger: "blur" }],
})
function handleAdd() {
  dialogTitle.value = "新增产品"
  dialogOpen.value = true
  currentForm.value = {}
  proxy.$refs["userRef"].resetFields()
}

function handleEdit(row, type) {
  if (type == 1) {
    dialogTitle.value = "入库"
  } else if (type == 2) {
    dialogTitle.value = "出库"
  } else {
    dialogTitle.value = "编辑产品"
  }
  dialogOpen.value = true
  currentForm.value = {
    ...row,
    remark: type !== 3 ? "" : row.remark,
    addressName: row.location,
    address: row.locationId,
    outBound: row.locationId,
    operateTime: "",
  }
  proxy.$refs["userRef"].resetFields()
}

/** 删除按钮操作 */
function handleDelete(row) {
  const jobIds = row.id
  proxy.$modal
    .confirm("是否确认删除该产品?")
    .then(function () {
      return deleteStore(jobIds)
    })
    .then(() => {
      getList()
      proxy.$modal.msgSuccess("删除成功")
    })
    .catch(() => { })
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download(
    "parts/export",
    {
      ...queryParams.value,
    },
    `备品备件_${new Date().getTime()}.xlsx`
  )
  getList();
}

/** 导出按钮操作 */
function handleRecordsExport() {
  proxy.download(
    "parts/exportRecords",
    {
      ...queryParams2.value,
    },
    `备品备件出入库记录_${new Date().getTime()}.xlsx`
  )
  getRecordList();
}

/** 选择条数  */
function handleSelectionChange(selection) {
  ids.value = selection.map((item) => item.userId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function cancelForm() {
  dialogOpen.value = false
  currentForm.value = {}
  proxy.$refs["userRef"].resetFields()
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["userRef"].validate((valid) => {
    if (valid) {
      let location = "";
      let locationId = dialogTitle.value == "出库" ? currentForm.value.outBound : currentForm.value.address;
      for (let i = 0; i < addressOptions.value.length; i++) {
        let value = dialogTitle.value == "出库" ? currentForm.value.outBound : currentForm.value.address;
        if (addressOptions.value[i].value == value) {
          location = addressOptions.value[i].label
        }
      }
      let params = {
        id: currentForm.value.id,
        // ...currentForm.value,
        code: currentForm.value.code,
        name: currentForm.value.name,
        specs: currentForm.value.specs,
        amount: currentForm.value.editCount || currentForm.value.amount,
        powerStationId: currentForm.value.powerStationId || "",
        remark: currentForm.value.remark,
        locationId:locationId,
        location:location,
        movementDate: currentForm.value.operateTime,
      }
      if (dialogTitle.value == "出库") {
        updateStore(params).then((response) => {
          proxy.$modal.msgSuccess("出库成功")
          dialogOpen.value = false
          getList()
        })
      } else if (dialogTitle.value == "编辑产品") {
        editPart(params).then((response) => {
          proxy.$modal.msgSuccess("编辑成功")
          dialogOpen.value = false
          getList()
        })
      } else {
        addStore(params).then((response) => {
          proxy.$modal.msgSuccess(dialogTitle.value == "入库" ? "入库成功" : "新增成功")
          dialogOpen.value = false
          getList()
        })
      }
    }
  })
}

onMounted(() => {
  getAllAddressData()
  getSiteData()
  handleQuery()
})
</script>

<style lang="scss" scoped>
:deep .el-form--inline .el-form-item {
  margin-right: 0;
  width: 100%;
}
</style>
