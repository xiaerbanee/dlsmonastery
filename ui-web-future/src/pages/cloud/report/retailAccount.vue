<style>
</style>
<template>
  <div>
    <head-tab active="retailAccount"></head-tab>
    <div>
      <el-row>
        <el-button type="primary"@click="formVisible = true" icon="search">过滤</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible = false" title="过滤" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="7">
            <el-col :span="12">
              <el-form-item :label="formLabel.startMonth.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.startMonth" type="month" placeholder="选择开始月份"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.endMonth.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.endMonth" type="month" placeholder="选择结束月份"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.departmentNumbers.label" :label-width="formLabelWidth">
                <el-select v-model="formData.departmentNumbers" filterable multiple remote placeholder="请输入关键词，可多选" :remote-method="remoteDepartment" :loading="remoteLoading">
                  <el-option v-for="item in departmentList" :key="item.number" :label="item.name" :value="item.number"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
      </search-dialog>
      <div id="grid" ref="handsontable" style="width:100%;height:600px;overflow:hidden;margin-top: 20px;"></div>
    </div>
  </div>
</template>
<style>
  @import "~handsontable/dist/handsontable.full.css";
</style>
<script>
  import Handsontable from 'handsontable/dist/handsontable.full.js';
  export default {
    data() {
      return {
        submitDisabled:false,
        departmentList:{},
        table:null,
        settings: {
          data: {},
          height: 650,
          rowHeaders: true,
          colHeaders: false,
          readOnly:true,
          currentRowClassName: 'currentRow',
          currentColClassName: 'currentCol',
          fixedColumnsLeft: 1,
          contextMenu: true,
          autoColumnSize:true,
          nestedHeaders:[[{"label":"","colspan":0},{"label":"00","colspan":8},{"label":"BM000000","colspan":8},{"label":"BM000001","colspan":8}],
            [{"label":"","colspan":0},{"label":"所有部门合计","colspan":8},{"label":"其他","colspan":8},{"label":"南昌八一大道万达店体验店直营（LX）","colspan":8}],
            [{"label":"","colspan":0},{"label":"2017-02","colspan":2},{"label":"2017-03","colspan":2},{"label":"2017-04","colspan":2},{"label":"累计","colspan":2},{"label":"2017-02","colspan":2},{"label":"2017-03","colspan":2},{"label":"2017-04","colspan":2},{"label":"累计","colspan":2},{"label":"2017-02","colspan":2},{"label":"2017-03","colspan":2},{"label":"2017-04","colspan":2},{"label":"累计","colspan":2}],
            [{"label":"科目名称","colspan":0},{"label":"金额","colspan":0},{"label":"占比","colspan":0},{"label":"金额","colspan":0},{"label":"占比","colspan":0},{"label":"金额","colspan":0},{"label":"占比","colspan":0},{"label":"金额","colspan":0},{"label":"占比","colspan":0},{"label":"金额","colspan":0},{"label":"占比","colspan":0},{"label":"金额","colspan":0},{"label":"占比","colspan":0},{"label":"金额","colspan":0},{"label":"占比","colspan":0},{"label":"金额","colspan":0},{"label":"占比","colspan":0},{"label":"金额","colspan":0},{"label":"占比","colspan":0},{"label":"金额","colspan":0},{"label":"占比","colspan":0},{"label":"金额","colspan":0},{"label":"占比","colspan":0},{"label":"金额","colspan":0},{"label":"占比","colspan":0}]],
          stretchH: 'all',
          hiddenColumns: true,
          collapsibleColumns: true
        },
        formData: {
          startMonth: '',
          endMonth:'',
          departmentNumbers:'',
        },
        formLabel:{
          startMonth:{label:"开始月份"},
          endMonth:{label:"结束月份"},
          departmentNumbers:{label:"部门"},
        },
        formLabelWidth: '120px',
        formVisible: false,
        remoteLoading:false,
      };
    },
    mounted () {
      axios.get("/api/global/cloud/report/retailAccount/list").then((response)=>{
        this.settings.data = response.data.retailAccount;
//        this.settings.nestedHeaders = response.data.nestedHeader;
        this.formData.startMonth = response.data.startMonth;
        this.formData.endMonth = response.data.endMonth;
        this.table = new Handsontable(this.$refs["handsontable"], this.settings)
      })
    },
    methods: {
      search(){
        this.formVisible = false;
        util.getQuery("retailAccount");
        util.setQuery("retailAccount",this.formData);
        util.copyValue(this.formData,this.submitData);
        axios.get("/api/global/cloud/report/retailAccount/report",{params:this.submitData}).then((response)=>{
          this.settings.data = response.data.consignmentDtoList;
          this.formData.dateRange = response.data.dateRange;
        })
      },
      remoteDepartment(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/global/cloud/input/bdDepartment/getByNameLike',{params:{name:query}}).then((response)=>{
            this.departmentList = response.data;
            this.remoteLoading = false;
          })
        } else {
          this.departmentList = {};
        }
      }
    },created () {

    }
  };
</script>

