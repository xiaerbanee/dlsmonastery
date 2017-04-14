<template>
  <div>
    <head-tab :active="$t('dutyPublicFreeList.dutyPublicFreeList') "></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('dutyPublicFreeList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('dutyPublicFreeList.filter')" v-model="formVisible" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="7">
            <el-col :span="12">
              <el-form-item :label="formLabel.dutyDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.dutyDate" type="daterange" align="right" :placeholder="$t('dutyPublicFreeList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dutyPublicFreeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dutyPublicFreeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="freeDate":label="$t('dutyPublicFreeList.freeDate')"></el-table-column>
        <el-table-column prop="dateType" :label="$t('dutyPublicFreeList.dateType')"></el-table-column>
        <el-table-column prop="status" :label="$t('dutyPublicFreeList.status')">
          <template scope="scope">
            <el-tag :type="scope.row.status == '申请中' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" :label="$t('dutyPublicFreeList.remarks')"></el-table-column>
        <el-table-column prop="created.loginName" :label="$t('dutyPublicFreeList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('dutyPublicFreeList.createdDate')"></el-table-column>
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
        formData:{
          page:0,
          size:25,
           dutyDate:'',
          dutyDateBTW:'',
        },
        formLabel:{
          dutyDateBTW:{label:this.$t('dutyPublicFreeList.freeDate')},
        },
        pickerDateOption:util.pickerDateOption,
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("dutyPublicFreeList",this.formData);
         this.formData.dutyDateBTW = util.formatDateRange(this.formData.dutyDate);
        axios.get('/api/basic/hr/dutyPublicFree',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

