<template>
  <div>
    <head-tab active="batchUnitsForm"></head-tab>
    <div>
      <el-form :model="formData" ref="inputForm"  label-width="120px" class="form input-form">
        <el-row>
        <el-form-item label="考核区域">
          <office-select v-model="formData.officeId"></office-select>
        </el-form-item>
        <el-form-item label="机构查看"></el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">保存</el-button>
          </el-form-item>
        </el-row>
        <el-row>
              <div ref="handsontable" style="width:800px;height:600px;overflow:hidden;"></div>
        </el-row>
      </el-form>
    </div>

  </div>
</template>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js'
  import officeSelect from "components/basic/office-select"
  export default {
    components:{officeSelect},
    data() {
      return {
        formData:{officeId:''},
        submitDisabled:false,
        table:null,
        settings: {
          colHeaders: ["机构ID", "上级名称", "名称","类型", "修改后名称"],
          rowHeaders: true,
          minSpareRows: 10,
          startRows: 10,
          startCols: 5,
          manualRowResize: true,
          manualColumnResize: true,
          contextMenu: true,
          columns: [
            {
              data: "idStr",
              readOnly: true,
              width: 100
            }, {
              data: "parentName",
              readOnly: true,
              width: 150
            }, {
              data: "name",
              readOnly: true,
              width: 200
            }, {
              data: "type",
              readOnly: true,
              width: 100
            }, {
              data: "name",
              width: 200
            }

          ]
        }
      }
    },
    mounted () {
      this.table = new Handsontable(this.$refs["handsontable"], this.settings)
    },

    methods: {},
  }
</script>

<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>

