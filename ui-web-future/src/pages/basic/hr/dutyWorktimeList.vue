<template>
  <div>
    <head-tab active="dutyWorktimeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:dutyWorktime:edit'">{{$t('dutyWorktimeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'hr:dutyWorktime:view'">{{$t('dutyWorktimeList.filter')}}</el-button>
        <el-button type="primary" @click="exportVisible = true" icon="upload" v-permit="'hr:dutyWorktime:edit'">{{$t('dutyWorktimeList.export')}}</el-button>
        <search-tag  :submitData="submitData" :formLabel = "formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('dutyWorktimeList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.dutyDate.label" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.dutyDate"></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dutyWorktimeList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-dialog :title="$t('dutyWorktimeList.export')" v-model="exportVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.formatMonth.label" :label-width="formLabelWidth">
                <el-date-picker v-model="formData.month" type="month" align="right" :placeholder="$t('dutyWorktimeList.selectMonth')" ></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>

        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportData()">{{$t('dutyWorktimeList.export')}}</el-button>
        </div>
      </el-dialog>

      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dutyWorktimeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="dutyDate" :label="$t('dutyWorktimeList.dutyDate')" sortable></el-table-column>
        <el-table-column prop="week" :label="$t('dutyWorktimeList.week')"></el-table-column>
        <el-table-column prop="dutyTime" :label="$t('dutyWorktimeList.dutyTime')" ></el-table-column>
        <el-table-column prop="employeeName" :label="$t('dutyWorktimeList.employeeName')"></el-table-column>
        <el-table-column prop="type" :label="$t('dutyWorktimeList.type')"></el-table-column>
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
          month:'',
          formatMonth:'',
        },
        submitData:{
          page:0,
          size:25,
          dutyDate:'',
          month:'',
          formatMonth:'',
        },formLabel:{
          dutyDate:{label: this.$t('dutyWorktimeList.dutyDate')},
          formatMonth:{label: this.$t('dutyWorktimeList.yearMonth')},
        },
        formLabelWidth: '120px',
        formVisible: false,
        exportVisible:false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("dutyWorktimeList",this.submitData);
        util.copyValue(this.formData,this.submitData);
        axios.get('/api/basic/hr/dutyWorktime?'+qs.stringify(this.submitData)).then((response) => {
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
      },itemAdd(){
        this.$router.push({ name: 'dutyWorktimeForm'})
      },exportData(){
        this.exportVisible = false;
        this.formData.formatMonth = util.formatLocalMonth(this.formData.month);
        window.location.href="/api/basic/hr/dutyWorktime/export?formatMonth="+this.formData.formatMonth;
			}
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData)
      this.pageRequest();
    }
  };
</script>

