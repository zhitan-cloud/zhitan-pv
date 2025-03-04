<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-row :gutter="12">
        <el-col :span="8">
          <el-form-item label="日期检索">
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
        <el-col :span="4">
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" class="operation-view">
          <el-form-item>
            <el-button type="primary" plain icon="edit" @click="handleAdd"> 时段配置 </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-table
      v-loading="loading"
      :data="tableData"
      border
      :height="tableHeight"
      highlight-current-row
      style="width: 100%"
      row-key="id"
      :expand-row-keys="expandRowKeys"
      @expand-change="onExpandChange"
    >
      <el-table-column type="expand">
        <template #default="props">
          <div class="expand-view">
            <div class="header">
              <h3>时段电价配置</h3>
              <el-button type="success" plain icon="plus" @click="handlePriceAdd(props.row)"> 电价配置 </el-button>
            </div>
            <el-table :data="props.row.peaksValleys" border>
              <el-table-column label="时段类别" prop="typeName" />
              <el-table-column label="开始时间" prop="beginTime" />
              <el-table-column label="结束时间" prop="endTime" />
              <el-table-column label="电价(元)" prop="electricityPrice" />
              <el-table-column label="备注" prop="remark" />
              <!-- <el-table-column
                label="操作"
                align="center"
                fixed="right"
                width="120"
                class-name="small-padding fixed-width"
              >
                <template #default="scope">
                  <el-button link type="danger" @click="handleDelete(scope.row)"> 删除 </el-button>
                </template>
              </el-table-column> -->
            </el-table>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="生效开始日期" prop="beginTime" :show-overflow-tooltip="true" />
      <el-table-column label="生效结束日期" prop="endTime" :show-overflow-tooltip="true" />
      <el-table-column label="备注" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" prop="createTime" width="160">
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
    <!-- 新增编辑 时段 -->
    <el-dialog :title="dialogTitle" v-model="dialogOpen" width="700px" append-to-body>
      <el-form :model="dateForm" :rules="rules" ref="dateFormRef" label-width="150px">
        <el-row>
          <el-col :span="20">
            <el-form-item label="生效日期:" prop="dateRange">
              <el-date-picker
                v-model="dateFormDateRange"
                value-format="YYYY-MM-DD"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="width: 300px"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="备注:" prop="remark">
              <el-input type="textarea" v-model="dateForm.remark" placeholder="请输入备注" maxlength="200" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitDateForm">确 定</el-button>
          <el-button @click="cancelDateForm">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 电价配置 -->
    <el-dialog title="电价配置" v-model="dialogPriceOpen" width="1200px" append-to-body>
      <el-form :model="priceForm" :rules="rules" ref="priceFormRef" label-width="90px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="生效日期:" prop="dateRange" style="width: 350px">
              <el-date-picker
                readonly
                v-model="dateFormDateRange"
                value-format="YYYY-MM-DD"
                type="daterange"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <template v-for="(item, index) in priceForm.itemList" :key="Math.random">
            <el-col :span="5">
              <el-form-item
                label="类别名称:"
                :prop="'itemList.' + index + '.type'"
                :rules="{
                  required: true,
                  message: '请选择类别名称',
                  trigger: 'blur',
                }"
              >
                <el-select v-model="item.type" placeholder="" clearable style="width: 100%">
                  <el-option v-for="dict in typeOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item
                label="时段电价:"
                :prop="'itemList.' + index + '.electricityPrice'"
                :rules="[
                  {
                    required: true,
                    message: '请输入时段电价',
                    trigger: 'blur',
                  },
                  // { validator: callBack, trigger: 'blur' },
                ]"
              >
                <el-input-number v-model="item.electricityPrice" :precision="2" :step="0.1" :max="99" />
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item
                label="开始时间:"
                :prop="'itemList.' + index + '.beginTime'"
                :rules="[
                  {
                    required: true,
                    message: '请选择开始时间',
                    trigger: 'change',
                  },
                ]"
              >
                <el-time-select v-model="item.beginTime" start="00:00" end="24:00" step="00:30" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item
                label="结束时间:"
                :prop="'itemList.' + index + '.endTime'"
                :rules="[
                  {
                    required: true,
                    message: '请选择结束时间',
                    trigger: 'change',
                  },
                ]"
              >
                <el-time-select v-model="item.endTime" start="00:00" end="24:00" step="00:30" placeholder="" />
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item label="备注:" prop="remark" label-width="54">
                <el-input v-model="item.remark" placeholder="" maxlength="100" style="width: 60%; margin-right: 6px" />
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

<script setup name="PeaksValleys">
import {
  listSetting,
  listSettingItem,
  addSetting,
  addSettingItem,
  updateSetting,
  deleteSetting,
} from "@/api/manage/peakSetting"
import { reactive, ref } from "vue"

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

const typeOptions = ref([
  { label: `尖`, value: "tip" },
  { label: `峰`, value: "peak" },
  { label: `平`, value: "flat" },
  { label: `谷`, value: "trough" },
  { label: `深谷`, value: "deep" },
])
const typeOptionsObj = ref({
  tip: `尖`,
  peak: `峰`,
  flat: "平",
  trough: "谷",
  deep: "深谷",
})

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

/** 查询列表 */
function getList() {
  loading.value = true
  let params = proxy.addDateRange(queryParams.value, dateRange.value)
  console.log(params)
  listSetting({ ...params, ...params.params }).then((res) => {
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

// 展开行
const expandRowKeys = ref([])
function onExpandChange(row, expanded) {
  loading.value = true
  if (expanded && expanded.length) {
    listSettingItem(row.id).then((res) => {
      loading.value = false
      let peaksValleys = res.data
        ? res.data.map((i) => {
            return {
              ...i,
              typeName: typeOptionsObj.value[i.type],
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

// 电价设置 begin
const priceForm = reactive({
  itemList: [
    {
      beginTime: null,
      endTime: null,
      electricityPrice: null,
      remark: null,
      type: null,
    },
  ],
})
const dialogPriceOpen = ref(false)
function handlePriceAdd(row) {
  let year = new Date().getFullYear()
  dateFormDateRange.value = [year + "-" + row.beginTime, year + "-" + row.endTime]
  dateForm.value = row
  if (row.peaksValleys.length > 0) {
    priceForm.itemList = row.peaksValleys
  } else {
    priceForm.itemList = [
      {
        beginTime: null,
        endTime: null,
        electricityPrice: null,
        remark: null,
        type: null,
      },
    ]
  }
  dialogPriceOpen.value = true
}

function addPriceItem() {
  priceForm.itemList.push({
    beginTime: null,
    endTime: null,
    electricityPrice: null,
    remark: null,
    type: null,
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
  let params = priceForm.itemList.map((item) => {
    return {
      ...item,
      parentId: dateForm.value.id,
    }
  })
  addSettingItem(params).then((res) => {
    proxy.$modal.msgSuccess("新增成功")
    onExpandChange(dateForm.value, [dateForm.value])
    cancelPriceForm()
  })
}

function cancelPriceForm() {
  dialogPriceOpen.value = false
}
// 电价设置 end

/** 新增编辑按钮操作 */
const dialogOpen = ref(false)
const dialogTitle = ref("")
const dateFormDateRange = ref([])
const dateForm = ref({})
const validateDateRange = (rule, value, callback) => {
  if (
    dateFormDateRange.value == undefined ||
    !dateFormDateRange.value.length ||
    dateFormDateRange.value[0] == "" ||
    dateFormDateRange.value[1] == ""
  ) {
    callback(new Error("生效日期不能为空"))
  } else {
    callback()
  }
}
const rules = ref({
  dateRange: [{ required: true, validator: validateDateRange, trigger: "change" }],
})
function handleAdd() {
  dialogTitle.value = "新增时段"
  dateFormDateRange.value = []
  dialogOpen.value = true
  dateForm.value = {}
}

function handleEdit(row) {
  console.log(row)
  dialogTitle.value = "编辑时段"
  let year = new Date().getFullYear()
  dateFormDateRange.value = [year + "-" + row.beginTime, year + "-" + row.endTime]
  dialogOpen.value = true
  dateForm.value = row
}

/** 删除按钮操作 */
function handleDelete(row) {
  const jobIds = row.id
  proxy.$modal
    .confirm("是否确认删除该时段?")
    .then(function () {
      return deleteSetting(jobIds)
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
function submitDateForm() {
  let params = proxy.addDateRange(dateForm.value, dateFormDateRange.value)
  params = {
    ...params.params,
    id: params.id,
    remark: dateForm.value.remark,
  }
  proxy.$refs["dateFormRef"].validate((valid) => {
    if (valid) {
      if (dateForm.value.id != undefined) {
        updateSetting(params).then((response) => {
          proxy.$modal.msgSuccess("修改成功")
          cancelDateForm()
          getList()
        })
      } else {
        addSetting(params).then((response) => {
          proxy.$modal.msgSuccess("新增成功")
          cancelDateForm()
          getList()
        })
      }
    }
  })
}
function cancelDateForm() {
  dialogOpen.value = false
  dateFormDateRange.value = []
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
