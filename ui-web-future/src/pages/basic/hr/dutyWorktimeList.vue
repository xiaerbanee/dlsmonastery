<template>
  <div>
    <head-tab active="dutyWorktimeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'hr:dutyWorktime:edit'">{{$t('dutyWorktimeList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'hr:dutyWorktime:view'">{{$t('dutyWorktimeList.filter')}}</el-button>
        <el-button type="primary" @click="exportVisible = true" icon="upload" v-permit="'hr:dutyWorktime:edit'">{{$t('dutyWorktimeList.export')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('dutyWorktimeList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('dutyWorktimeList.dutyDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.dutyDate" ></date-range-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dutyWorktimeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <search-dialog :title="$t('dutyWorktimeList.export')" v-model="exportVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('dutyWorktimeList.yearMonth')" :label-width="formLabelWidth">
                <el-date-picker v-model="month" type="month" :placeholder="$t('dutyWorktimeList.selectMonth')"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportData()">{{$t('dutyWorktimeList.export')}}</el-button>
        </div>
      </search-dialog>

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
  import SearchDialog from "../../../components/common/search-dialog.vue";

  export default {
    components: {SearchDialog},
    data() {
      return {
        page:{},
        formData:{
          extra:{},
          formatMonth:""
        },
        month:"",
        searchText:"",
        formLabelWidth: '120px',
        initPromise:{},
        formVisible: false,
        exportVisible:false,
        pageLoading: false
      };
    },
    methods: {
      setSearchText(){
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        });
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("dutyWorktimeList",submitData);
        axios.get('/api/basic/hr/dutyWorktime?'+qs.stringify(submitData)).then((response) => {
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
        this.formData.formatMonth = util.formatLocalMonth(this.month);
        axios.get('/api/basic/hr/dutyWorktime/export?month='+this.formData.formatMonth).then((response)=> {
          window.location.href="/api/general/sys/folderFile/download?id="+response.data;
        });
			}
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/basic/hr/dutyWorktime/getQuery').then((response)=> {
        this.formData = response.data;
        util.copyValue(this.$route.query, this.formData);
      });

    },activated() {
      this.initPromise.then(()=> {
        this.pageRequest();
      });
    }
  };
</script>

