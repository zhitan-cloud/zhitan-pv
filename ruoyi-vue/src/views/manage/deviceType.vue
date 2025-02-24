<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-row :gutter="12">
        <el-col :span="5">
          <el-form-item label="类型名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入设备类型"
              clearable
              style="width: 100%"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="15" class="operation-view">
          <el-form-item>
            <el-button type="primary" plain icon="plus" @click="handleAdd"> 新增 </el-button>
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
      row-key="id"
      @selection-change="handleSelectionChange"
      :expand-row-keys="expandRowKeys"
      @expand-change="onExpandChange"
    >
      <el-table-column type="expand">
        <template #default="props">
          <div class="expand-view">
            <div class="header">
              <h3>点位模板配置</h3>
              <el-button type="success" plain icon="plus" @click="handlePriceAdd(props.row)"> 编辑配置 </el-button>
            </div>
            <el-table :data="props.row.peaksValleys" border>
              <el-table-column label="点位编码" prop="code" />
              <el-table-column label="点位名称" prop="name" />
              <el-table-column label="点位类型" prop="typeName" />
              <el-table-column label="网关标识" prop="tagKey" />
              <el-table-column label="单位" prop="unit" />
            </el-table>
          </div>
        </template>
      </el-table-column>
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="设备类型" align="center" prop="name" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="描述" align="center" prop="description" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
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
    <el-dialog :title="dialogTitle" v-model="dialogOpen" width="700px" append-to-body>
      <el-form :model="currentForm" :rules="rules" ref="userRef" label-width="150px">
        <el-row>
          <el-col :span="20">
            <el-form-item label="设备类型名称:" prop="name">
              <el-input v-model="currentForm.name" placeholder="请输入设备类型名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="描述:" prop="description">
              <el-input type="textarea" v-model="currentForm.description" placeholder="请输入描述" maxlength="30" />
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

    <!-- 点位模板配置 -->
    <el-dialog title="点位模板配置" v-model="dialogPriceOpen" width="1200px" append-to-body>
      <el-form :model="priceForm" :rules="rules" ref="priceFormRef" label-width="90px">
        <el-row>
          <template v-for="(item, index) in priceForm.itemList" :key="Math.random">
            <el-col :span="5">
              <el-form-item
                label="点位编码:"
                :prop="'itemList.' + index + '.code'"
                :rules="{
                  required: true,
                  message: '请输入点位编码',
                  trigger: 'blur',
                }"
              >
                <el-input v-model="item.code" placeholder="" maxlength="100" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item
                label="点位名称:"
                :prop="'itemList.' + index + '.name'"
                :rules="{
                  required: true,
                  message: '请输入点位名称',
                  trigger: 'blur',
                }"
              >
                <el-input v-model="item.name" placeholder="" maxlength="100" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item
                label="点位类型:"
                :prop="'itemList.' + index + '.indexType'"
                :rules="{
                  required: true,
                  message: '请选择点位类型',
                  trigger: 'blur',
                }"
              >
                <el-select v-model="item.indexType" placeholder="" clearable style="width: 100%">
                  <el-option
                    v-for="dict in indexTypeOptions"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item
                label="网关标识:"
                :prop="'itemList.' + index + '.tagKey'"
                :rules="{
                  required: true,
                  message: '请输入网关标识',
                  trigger: 'blur',
                }"
              >
                <el-input v-model="item.tagKey" placeholder="" maxlength="100" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="单位:" prop="unit" label-width="54">
                <el-input v-model="item.unit" placeholder="" maxlength="100" style="width: 60%; margin-right: 6px" />
                <el-icon style="margin-right: 6px; cursor: pointer" @click="addPriceItem"><CirclePlus /></el-icon>
                <el-icon v-if="priceForm.itemList.length !== 1" style="cursor: pointer" @click="removePriceItem(index)">
                  <Delete />
                </el-icon>
              </el-form-item>
            </el-col>
          </template>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitPriceForm">确 定</el-button>
          <el-button @click="cancelPriceForm">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="SiteManage">
import {
  listDeviceType,
  addDeviceType,
  updateDeviceType,
  deleteDeviceType,
  indexTemplateList,
  updateTemplate,
} from "@/api/manage/deviceType"
import { onMounted, reactive, ref } from "vue"

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
    description: undefined,
  },
})
const { queryParams, form } = toRefs(data)
const indexTypeOptions = ref([
  { label: `采集点`, value: "COLLECT" },
  { label: `计算点`, value: "CALCULATE" },
])
const typeOptionsObj = ref({
  COLLECT: `采集点`,
  CALCULATE: `计算点`,
})

// 展开行
const expandRowKeys = ref([])
function onExpandChange(row, expanded) {
  loading.value = true
  if (expanded && expanded.length) {
    indexTemplateList(row.id).then((res) => {
      loading.value = false
      let peaksValleys = res.data
        ? res.data.map((i) => {
            return {
              ...i,
              typeName: typeOptionsObj.value[i.indexType],
            }
          })
        : []
      tableData.value = tableData.value.map((item) => {
        return {
          ...item,
          peaksValleys: item.id == row.id ? peaksValleys : item.peaksValleys,
        }
      })
    })
  } else {
    loading.value = false
  }
  expandRowKeys.value = expanded.map((item) => {
    return item.id
  })
}
// 点位模板设置 begin
const priceForm = reactive({
  itemList: [
    {
      name: null,
      code: null,
      indexType: "COLLECT",
      unit: null,
      tagKey: null,
    },
  ],
})
const dialogPriceOpen = ref(false)
function handlePriceAdd(row) {
  currentForm.value = row
  if (row.peaksValleys.length > 0) {
    priceForm.itemList = row.peaksValleys
  }
  dialogPriceOpen.value = true
}

function addPriceItem() {
  priceForm.itemList.push({
    name: null,
    code: null,
    indexType: "COLLECT",
    unit: null,
    tagKey: null,
  })
}
function removePriceItem(index) {
  priceForm.itemList.splice(index, 1)
}

function submitPriceForm() {
  proxy.$refs["priceFormRef"].validate((valid) => {
    if (!valid) {
      return false
    }
  })
  let params = priceForm.itemList
  updateTemplate({
    id: currentForm.value.id,
    data: params,
  }).then((res) => {
    proxy.$modal.msgSuccess("配置成功")
    onExpandChange(currentForm.value, [currentForm.value])
    cancelPriceForm()
  })
}

function cancelPriceForm() {
  dialogPriceOpen.value = false
}
// 点位模板设置 end

/** 查询用户列表 */
function getList() {
  loading.value = true
  listDeviceType(queryParams.value).then((res) => {
    loading.value = false
    tableData.value = res.rows
      ? res.rows.map((item) => {
          return {
            ...item,
            peaksValleys: [],
          }
        })
      : []
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

/** 新增编辑按钮操作 */
const dialogOpen = ref(false)
const dialogTitle = ref("")
const currentForm = ref({})
const rules = ref({
  name: [{ required: true, message: "设备类型名称不能为空", trigger: "blur" }],
  code: [{ required: true, message: "设备类型编码不能为空", trigger: "blur" }],
})
function handleAdd() {
  dialogTitle.value = "新增设备类型"
  dialogOpen.value = true
  currentForm.value = {}
}

function handleEdit(row) {
  dialogTitle.value = "编辑设备类型"
  dialogOpen.value = true
  currentForm.value = row
  //   router.push("/system/user-auth/role/" + userId)
}

/** 删除按钮操作 */
function handleDelete(row) {
  const jobIds = row.id
  proxy.$modal
    .confirm("是否确认删除该设备类型?")
    .then(function () {
      return deleteDeviceType(jobIds)
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
      if (currentForm.value.id != undefined) {
        updateDeviceType(currentForm.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功")
          dialogOpen.value = false
          getList()
        })
      } else {
        addDeviceType(currentForm.value).then((response) => {
          proxy.$modal.msgSuccess("新增成功")
          dialogOpen.value = false
          getList()
        })
      }
    }
  })
}
onMounted(() => {
  handleQuery()
})
</script>

<style lang="scss" scoped>
:deep .el-form--inline .el-form-item {
  margin-right: 0;
  width: 100%;
}
.expand-view {
  padding: 0 16px 0 42px;
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>
