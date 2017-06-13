<template>
  <div>
    <head-tab active="dutyRestList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search">{{$t('dutyRestList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('dutyRestList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
              <el-form-item :label="$t('dutyRestList.dutyDate')" :label-width="formLabelWidth">
                <date-range-picker v-model="formData.dutyDate"></date-range-picker>
              </el-form-item>
              <el-form-item :label="$t('dutyRestList.type')" :label-width="formLabelWidth">
                <el-select v-model="formData.type" filterable clearable :placeholder="$t('dutyRestList.inputKey')">
                  <el-option v-for="item in formData.extra.restList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('dutyRestList.dateType')" :label-width="formLabelWidth">
                <el-select v-model="formData.dateType" filterable clearable :placeholder="$t('dutyRestList.inputKey')">
                  <el-option v-for="item in formData.extra.dateList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dutyRestList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dutyRestList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="dutyDate" :label="$t('dutyRestList.dutyDate')"></el-table-column>
        <el-table-column prop="type" :label="$t('dutyRestList.type')" ></el-table-column>
        <el-table-column prop="dateType" :label="$t('dutyRestList.dateType')"></el-table-column>
        <el-table-column prop="timeStart" :label="$t('dutyRestList.timeStart')"></el-table-column>
        <el-table-column prop="timeEnd" :label="$t('dutyRestList.timeEnd')" ></el-table-column>
        <el-table-column prop="hour" :label="$t('dutyRestList.hour')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('dutyRestList.dutyRestRemarks')"></el-table-column>
        <el-table-column prop="status" :label="$t('dutyRestList.status')">
          <template scope="scope">
            <el-tag :type="scope.row.status == '申请中' ? 'primary' : 'danger'">{{scope.row.status}}</el-tag>
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
          extra:{},
          dutyDate:'',
        },
        initPromise:{},
        searchText:"",
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        });
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("dutyRestList",submitData);
        axios.get('/api/basic/hr/dutyRest?'+qs.stringify(submitData)).then((response) => {
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
      this.initPromise = axios.get('/api/basic/hr/dutyRest/getQuery').then((response) =>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
      });
    },activated() {
      this.initPromise.then(()=> {
        this.pageRequest();
      })
    }
  };
</script>

