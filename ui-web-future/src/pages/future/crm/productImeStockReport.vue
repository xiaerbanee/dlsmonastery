<template>
  <div>
    <head-tab active="productImeStockReport"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'crm:bank:view'">过滤</el-button>
        <el-dropdown  @command="exportData">
          <el-button type="primary">导出<i class="el-icon-caret-bottom el-icon--right"></i></el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="按数量导出">按数量导出</el-dropdown-item>
            <el-dropdown-item command="按合计导出">按合计导出</el-dropdown-item>
            <el-dropdown-item command="按串码导出">按串码导出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-button type="primary" @click="saleReportGrid()" icon="document">明细</el-button>
        <el-button type="primary" @click="preLevel()" v-show="officeIds.length">返回</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog title="过滤" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.sumType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.sumType" clearable filterable placeholder="请选择">
                  <el-option v-for="item in formData.sumTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.outType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.outType" clearable filterable placeholder="请选择">
                  <el-option v-for="item in formData.outTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.areaType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.areaType" clearable filterable placeholder="请选择">
                  <el-option v-for="item in formData.areaTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.townType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.townType" clearable filterable placeholder="请选择">
                  <el-option v-for="item in formData.townTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.date.label" :label-width="formLabelWidth">
                <date-picker v-model="formData.date"></date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.scoreType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.scoreType" clearable filterable placeholder="请选择">
                  <el-option v-for="item in formData.boolMap" :key="item" :label="item | bool2str" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.productIds.label" :label-width="formLabelWidth">
                <el-select v-model="formData.productIds" clearable filterable placeholder="请选择">
                  <el-option v-for="item in formData.productIdsList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">确定</el-button>
        </div>
      </el-dialog>
      <el-table :data="page"  style="margin-top:5px;" v-loading="pageLoading" element-loading-text="加载中" @sort-change="sortChange" @row-click="nextLevel" stripe border>
        <el-table-column fixed prop="officeName" label="门店" sortable width="300"></el-table-column>
        <el-table-column prop="qty" label="数量"  sortable></el-table-column>
        <el-table-column prop="percent" label="占比(%)"></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        pageLoading: false,
        page:[],
        formData:{},
        submitData:{
          page:0,
          size:25,
          sort:"id,DESC",
          sumType:'',
          outType:'',
          areaType:'',
          townType:'',
          date:util.currentDate(),
          scoreType:null,
          productIds:'',
          officeId:''
        },formLabel:{
          sumType:{label:"汇总"},
          outType:{label:"查看"},
          areaType:{label:"区域类型"},
          townType:{label:"乡镇"},
          date:{label:"日期"},
          scoreType:{label:"打分型号"},
          productIds:{label:"货品"}
        },
        formLabelWidth: '120px',
        formVisible: false,
        officeIds:[]
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("productImeSaleReport",this.formData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/ws/future/crm/productIme/productImeReport?type=库存报表',{params:this.submitData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },sortChange(column) {
        this.formData.sort=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },nextLevel(	row, event, column){
        this.officeIds.push(row.officeId);
        this.submitData.officeId=this.officeIds[this.officeIds.length-1];
        this.pageRequest();
      },preLevel(){
        this.officeIds.pop();
        this.submitData.officeId=this.officeIds[this.officeIds.length-1];
        this.pageRequest();
      },saleReportGrid(){

      },exportData(command) {
      }
    },created () {
      axios.get('/api/ws/future/crm/productIme/getReportQuery').then((response) => {
        this.formData = response.data;
        console.log(this.formData)
        util.copyValue(this.$route.query, this.formData);
        this.pageRequest();
      })
    }
  };
</script>

