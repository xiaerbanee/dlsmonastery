<template>
  <div>
    <head-tab active="payableReport"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'cloud:payableReport:view'">过滤</el-button>
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
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="拼命加载中....." @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" label="供应商名称" sortable width="150"></el-table-column>
        <el-table-column prop="category" label="门店名称"></el-table-column>
        <el-table-column prop="value" label="期初应付"></el-table-column>
        <el-table-column prop="remarks" label="应付金额"></el-table-column>
        <el-table-column prop="createdByName" label="实付金额"></el-table-column>
        <el-table-column prop="createdDate" label="期末应付"></el-table-column>
        <el-table-column fixed="right" label="操作" width="120">
          <template scope="scope">
            <el-button size="small">详细</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
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
        this.formData.createdDateBTW=util.formatDateRange(this.formData.createdDate);
        util.getQuery("dictEnumList");
        util.copyValue(this.formData,this.submitData);
        util.setQuery("dictEnumList",this.submitData);
        axios.get('/api/basic/sys/dictEnum',{params:this.submitData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },search() {
        this.formVisible = false;
        this.pageRequest();
      }
    },created () {
      var that = this;
      that.formData = that.submitData;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/basic/sys/dictEnum/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
        that.pageRequest();
      });
    }
  };
</script>

