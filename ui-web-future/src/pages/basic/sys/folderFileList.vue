<template>
  <div>
    <head-tab active="folderFileList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('folderFileList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('folderFileList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.createdDateBTW.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.createdDate" type="daterange" align="right" :placeholder="$t('folderFileList.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('folderFileList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('folderFileList.loading')" @sort-change="sortChange"  stripe border>
        <el-table-column  prop="id" :label="$t('folderFileList.id')" sortable width="150"></el-table-column>
        <el-table-column  prop="folderName" :label="$t('folderFileList.folderName')" ></el-table-column>
        <el-table-column  prop="name" :label="$t('folderFileList.name')" ></el-table-column>
        <el-table-column  prop="contentType" :label="$t('folderFileList.contentType')" ></el-table-column>
        <el-table-column  prop="size" :label="$t('folderFileList.size')" ></el-table-column>
        <el-table-column  prop="createdByName" :label="$t('folderFileList.createdBy')" ></el-table-column>
        <el-table-column  prop="createdDate" :label="$t('folderFileList.createdDate')" ></el-table-column>
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
          createdDate:'',
          createdDateBTW:'',},
        submitData:{
          page:0,
          size:25,
          createdDate:'',
          createdDateBTW:'',
        },formLabel:{
          createdDateBTW:{label: this.$t('folderFileList.createdDate')}
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        pickerDateOption:util.pickerDateOption
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        this.formData.createdDateBTW=util.formatDateRange(this.formData.createdDate);
        util.getQuery("folderFileList");
        util.setQuery("folderFileList",this.formData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/sys/folderFile',{params:this.submitData}).then((response) => {
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

