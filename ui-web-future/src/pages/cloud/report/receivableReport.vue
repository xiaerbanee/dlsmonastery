<template>
  <div>
    <head-tab active="receivableReport"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog title="过滤" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.payableSummaryList" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="拼命加载中....." stripe border>
        <el-table-column fixed prop="primaryGroupName" label="客户分组" sortable width="150"></el-table-column>
        <el-table-column prop="customerName" label="客户名称"></el-table-column>
        <el-table-column prop="beginAmount" label="期初应收"></el-table-column>
        <el-table-column prop="receivableAmount" label="应收金额"></el-table-column>
        <el-table-column prop="actualReceivable" label="实收金额"></el-table-column>
        <el-table-column prop="endAmount" label="期末应收"></el-table-column>
        <el-table-column fixed="right" label="操作" width="120">
          <template scope="scope">
            <el-button size="small">详细</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        page:{},
        formData:{},
        submitData:{
        },
        formLabel:{
        },
        pickerDateOption:util.pickerDateOption,
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW = util.formatDateRange(this.formData.createdDate);
        util.getQuery("receivableReport");
        util.copyValue(this.formData,this.submitData);
        util.setQuery("receivableReport",this.submitData);
        axios.get('/api/global/cloud/report/receivableReport/summaryList',{params:this.submitData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },search() {
        this.formVisible = false;
        this.pageRequest();
      }
    },created () {
      var that = this;
      that.pageHeight = window.outerHeight -320;
      that.pageRequest();
    }
  };
</script>

