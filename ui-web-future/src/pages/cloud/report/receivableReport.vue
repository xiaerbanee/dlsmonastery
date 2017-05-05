<template>
  <div>
    <head-tab active="receivableReport"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog v-model="detailVisible" size="large">
        <el-table :data="detail" :row-class-name="tableRowClassName" v-loading="detailLoading" element-loading-text="拼命加载中....." border>
          <el-table-column prop="billType" label="业务类型"></el-table-column>
          <el-table-column prop="billNo" label="单据编号"></el-table-column>
          <el-table-column prop="date" label="单据日期"></el-table-column>
          <el-table-column prop="materialName" label="商品名称"></el-table-column>
          <el-table-column prop="quantity" label="数量"></el-table-column>
          <el-table-column prop="price" label="单价"></el-table-column>
          <el-table-column prop="amount" label="金额"></el-table-column>
          <el-table-column prop="receivableAmount" label="应收"></el-table-column>
          <el-table-column prop="actualReceivableAmount" label="实收"></el-table-column>
          <el-table-column prop="endAmount" label="期末"></el-table-column>
          <el-table-column prop="note" label="摘要"></el-table-column>
        </el-table>
      </el-dialog>
      <el-dialog title="过滤" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="7">
            <el-col :span="12">
              <el-form-item :label="formLabel.dateRangeBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.dateRange" type="daterange" align="right" placeholder="请选择时间" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
              <el-form-item :label="formLabel.primaryGroupName.label" :label-width="formLabelWidth">
                <el-select v-model="formData.primaryGroupId" placeholder="请选择客户分组">
                  <el-option v-for="item in formData.primaryGroup" :key="item.value" :label="item.name" :value="item.value"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
      </el-dialog>
      <el-table :data="summary.receivableSummaryList" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="拼命加载中....." stripe border>
        <el-table-column fixed prop="primaryGroupName" label="客户分组" sortable width="150"></el-table-column>
        <el-table-column prop="customerName" label="客户名称"></el-table-column>
        <el-table-column prop="beginAmount" label="期初应收"></el-table-column>
        <el-table-column prop="receivableAmount" label="应收金额"></el-table-column>
        <el-table-column prop="actualReceivable" label="实收金额"></el-table-column>
        <el-table-column prop="endAmount" label="期末应收"></el-table-column>
        <el-table-column fixed="right" label="操作" width="120">
          <template scope="scope">
            <el-button size="small" @click="detailAction(scope.row.customerId)">详细</el-button>
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
</style>
<script>
  export default {
    data() {
      return {
        summary: {},
        detail: {},
        formData: {
          dateRange: '',
          dateRangeBTW: '',
          primaryGroupName:'',
          primaryGroup:{},
        },
        submitData: {
          dateRangeBTW: '',
        },
        submitDetail: {
          dateRangeBTW: '',
          customerId: '',
        },
        formLabel:{
          dateRangeBTW:{label:"日期"},
          primaryGroupName:{label:"客户分组",value:""},
        },
        pickerDateOption:util.pickerDateOption,
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
        util.getQuery("receivableReport");
        util.setQuery("receivableReport",this.formData);
        this.formData.dateRangeBTW = util.formatDateRange(this.formData.dateRange);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/global/cloud/report/receivableReport/summaryList',{params:this.submitData}).then((response) => {
          this.summary = response.data;
          this.formData.dateRangeBTW = response.data.dateRange;
          this.formData.primaryGroupName = response.data.primaryGroupName;
          this.formData.primaryGroup = response.data.primaryGroup;
          this.pageLoading = false;
        })
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },detailAction:function(customerId){
        this.detailLoading = true;
        if(customerId !== null) {
          util.copyValue(this.formData,this.submitDetail);
          this.submitDetail.customerId = customerId;
          axios.get('/api/global/cloud/report/receivableReport/detailList',{params:this.submitDetail}).then((response) =>{
            this.detail = response.data;
            this.detailLoading = false;
            this.detailVisible = true;
          })
        }
      },tableRowClassName(row, index) {
        if (row.css === "info") {
          return "info-row";
        }else if(row.css === "danger"){
          return "danger-row"
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.pageRequest();
    }
  };
</script>

