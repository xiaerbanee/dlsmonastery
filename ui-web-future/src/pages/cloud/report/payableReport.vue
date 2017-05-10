<template>
  <div>
    <head-tab active="payableReport"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search" >过滤</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog title="过滤" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="7">
            <el-col :span="12">
              <el-form-item :label="formLabel.dateRange.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.dateRange" ></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
      </el-dialog>
      <el-dialog v-model="detailVisible" size="large">
        <el-table :data="detail" :row-class-name="tableRowClassName" v-loading="detailLoading" element-loading-text="拼命加载中....." border>
          <el-table-column prop="billType" label="业务类型"></el-table-column>
          <el-table-column prop="billNo" label="单据编号"></el-table-column>
          <el-table-column prop="date" label="单据日期"></el-table-column>
          <el-table-column prop="materialName" label="商品名称"></el-table-column>
          <el-table-column prop="quantity" label="数量"></el-table-column>
          <el-table-column prop="price" label="单价"></el-table-column>
          <el-table-column prop="amount" label="金额"></el-table-column>
          <el-table-column prop="payableAmount" label="应付"></el-table-column>
          <el-table-column prop="actualPayAmount" label="实付"></el-table-column>
          <el-table-column prop="endAmount" label="期末"></el-table-column>
          <el-table-column prop="note" label="摘要"></el-table-column>
        </el-table>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportDetail()">导出</el-button>
        </div>
      </el-dialog>
      <el-table :data="summary.payableSummaryList" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="拼命加载中....." stripe border>
        <el-table-column fixed prop="supplierName" label="供应商名称" sortable width="200"></el-table-column>
        <el-table-column prop="departmentName" label="门店名称"></el-table-column>
        <el-table-column prop="beginAmount" label="期初应付"></el-table-column>
        <el-table-column prop="payableAmount" label="应付金额"></el-table-column>
        <el-table-column prop="actualPayAmount" label="实付金额"></el-table-column>
        <el-table-column prop="endAmount" label="期末应付"></el-table-column>
        <el-table-column fixed="right" label="操作" width="120">
          <template scope="scope">
            <el-button size="small" @click="detailAction(scope.row.supplierId,scope.row.departmentId)">详细</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<style>
  .el-table .info-row {
    background: #c9e5f5;
  }

  .el-table .danger-row {
    background: #FF8888;
  }

  .el-table .warning-row {
    background: #FFEE99;
  }
</style>
<script>
  export default {
    data() {
      return {
        summary: {},
        detail: {},
        formData: {
          dateRange: '',
          supplierIds: [],
        },
        submitData: {
          dateRange: '',
          supplierIds: [],
        },
        submitDetail: {
          dateRange: '',
          supplierId: '',
          departmentId: ''
        },
        formLabel:{
          dateRange:{label:"日期"},
          supplierIds:{label:"供应商名称",value:""},
        },
        formLabelWidth: '120px',
        formVisible: false,
        detailVisible:false,
        pageLoading: false,
        detailLoading:false,
        pageHeight:'',
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.getQuery("payableReport");
        util.setQuery("payableReport",this.formData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/global/cloud/report/payableReport/summaryList',{params:this.submitData}).then((response) => {
          this.summary = response.data;
          this.formData.dateRange = response.data.dateRange;
          this.pageLoading = false;
        })
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },detailAction:function(supplierId,departmentId){
        this.detailLoading = true;
        if(supplierId !== null) {
            util.copyValue(this.formData,this.submitDetail);
            this.submitDetail.supplierId = supplierId;
            this.submitDetail.departmentId = departmentId;
            axios.get('/api/global/cloud/report/payableReport/detailList',{params:this.submitDetail}).then((response) =>{
              this.detail = response.data;
              this.detailLoading = false;
              this.detailVisible = true;
            })
        }
      },exportDetail(){
        axios.get('/api/global/cloud/report/payableReport/exportDetail',{params:this.submitDetail}).then((response) =>{});

      },tableRowClassName(row, index) {
        if (row.css === "info") {
            return "info-row";
        }else if(row.css === "danger"){
            return "danger-row"
        }else if(row.css === "warning"){
            return "warning-row"
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.pageRequest();
    }
  };
</script>

