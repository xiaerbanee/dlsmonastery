<template>
  <div>
    <head-tab active="dutyPublicFreeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('dutyPublicFreeList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('dutyPublicFreeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="7">
            <el-col :span="12">
              <el-form-item :label="formLabel.dutyDate.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.dutyDate"></date-range-picker>
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
          dutyDate:'',
        },
        submitData:{
          page:0,
          size:25,
          dutyDate:'',
        },
        formLabel:{
          dutyDate:{label:this.$t('dutyPublicFreeList.freeDate')},
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("dutyPublicFreeList",this.submitData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/hr/dutyPublicFree?'+qs.stringify(this.submitData)).then((response) => {
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

