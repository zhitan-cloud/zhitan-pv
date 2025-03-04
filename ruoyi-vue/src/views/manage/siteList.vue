<template>
  <div class="app-container">
    <!--用户数据-->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-row :gutter="12">
        <el-col :span="5">
          <el-form-item label="电站编码" prop="code">
            <el-input
              v-model="queryParams.code"
              placeholder="请输入电站编码"
              clearable
              style="width: 100%"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="电站名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入电站名称"
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
        <el-col :span="10" class="operation-view">
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
      <!-- <el-table-column type="selection" width="55" align="center" /> -->
      <el-table-column label="电站编码" align="center" prop="code" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column label="电站名称" align="center" prop="name" :show-overflow-tooltip="true"></el-table-column>
      <!-- <el-table-column label="逆变器数量" align="center" prop="remark" :show-overflow-tooltip="true" /> -->
      <el-table-column label="经纬度" align="center" prop="lat" width="160">
        <template #default="scope">
          <span>{{ (scope.row.lon || "--") + "," + (scope.row.lat || "--") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="装机容量(MW)" align="center" prop="installedCapacity" :show-overflow-tooltip="true" />
      <el-table-column label="并网电压(V)" align="center" prop="gridVoltage" :show-overflow-tooltip="true" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
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
            <el-form-item label="电站名称:" prop="name">
              <el-input v-model="currentForm.name" placeholder="请输入电站名称" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="电站编码:" prop="code">
              <el-input v-model="currentForm.code" placeholder="请输入电站编码" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="地址经度:" prop="lon">
              <el-input type="number" v-model="currentForm.lon" placeholder="范围是0-180°" maxlength="10" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="地址纬度:" prop="lat">
              <el-input type="number" v-model="currentForm.lat" placeholder="范围是0-90°" maxlength="10" />
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="电站装机容量:" prop="installedCapacity">
              <el-input type="number" v-model="currentForm.installedCapacity" placeholder="请输入电站装机容量">
                <template #append>MW</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="并网电压:" prop="gridVoltage">
              <el-input type="number" v-model="currentForm.gridVoltage" placeholder="请输入并网电压">
                <template #append>V</template>
              </el-input>
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

<script setup name="SiteManage">
import { reactive, ref, onMounted } from "vue"
import { listSite, addSite, updateSite, deleteSite } from "@/api/manage/site"

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

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 20,
    name: undefined,
    code: undefined,
  },
})

const { queryParams, form } = toRefs(data)

/** 查询用户列表 */
function getList() {
  loading.value = true
  let params = queryParams.value
  listSite(params).then((res) => {
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
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 新增编辑按钮操作 */
const dialogOpen = ref(false)
const dialogTitle = ref("")
const currentForm = ref({})
const rules = ref({
  name: [{ required: true, message: "电站名称不能为空", trigger: "blur" }],
  code: [{ required: true, message: "电站编码不能为空", trigger: "blur" }],
})
function handleAdd() {
  dialogTitle.value = "新增电站"
  dialogOpen.value = true
  currentForm.value = {}
}

function handleEdit(row) {
  dialogTitle.value = "编辑电站"
  dialogOpen.value = true
  currentForm.value = row
}

/** 删除按钮操作 */
function handleDelete(row) {
  const jobIds = row.id
  proxy.$modal
    .confirm("是否确认删除该电站?")
    .then(function () {
      return deleteSite(jobIds)
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
        updateSite(currentForm.value).then((response) => {
          proxy.$modal.msgSuccess("修改成功")
          dialogOpen.value = false
          getList()
        })
      } else {
        addSite(currentForm.value).then((response) => {
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
</style>
