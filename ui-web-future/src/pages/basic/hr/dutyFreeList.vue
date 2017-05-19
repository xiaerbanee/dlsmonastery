<template>
  <div>
    <head-tab active="dutyFreeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('dutyFreeList.filter')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('dutyFreeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
              <el-form-item :label="formLabel.dutyDate.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.dutyDate"></date-range-picker>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dutyFreeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dutyFreeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="freeDate" :label="$t('dutyFreeList.freeDate')"></el-table-column>
        <el-table-column prop="dateType" :label="$t('dutyFreeList.dateType')" ></el-table-column>
        <el-table-column prop="remarks" :label="$t('dutyFreeList.dutyFreeRemarks')"></el-table-column>
        <el-table-column prop="status" :label="$t('dutyFreeList.status')" >
          <template scope="scope">
            <el-tag :type="scope.row.status == '已通过' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
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
        formData:{
          dutyDate:'',
        },
        submitData:{
          page:0,
          size:25,
          dutyDate:'',
        },
        formLabel:{
          dutyDate:{label:this.$t('dutyFreeList.freeDate')},
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("dutyFreeList",this.submitData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/hr/dutyFree?'+qs.stringify(this.submitData)).then((response) => {
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

