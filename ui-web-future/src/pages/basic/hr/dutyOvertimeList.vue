<template>
  <div>
    <head-tab active="dutyOvertimeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary"@click="formVisible = true" icon="search">{{$t('dutyOvertimeList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('dutyOvertimeList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData"  :label-width="formLabelWidth">
              <el-form-item :label="$t('dutyOvertimeList.dutyDate')">
                <date-range-picker v-model="formData.dutyDate"></date-range-picker>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dutyOvertimeList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dutyOvertimeList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column prop="dutyDate" :label="$t('dutyOvertimeList.dutyDate')"></el-table-column>
        <el-table-column prop="timeStart" :label="$t('dutyOvertimeList.timeStart')"></el-table-column>
        <el-table-column prop="timeEnd" :label="$t('dutyOvertimeList.timeEnd')" ></el-table-column>
        <el-table-column prop="hour" :label="$t('dutyOvertimeList.hour')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('dutyOvertimeList.remarks')"></el-table-column>
        <el-table-column prop="status" :label="$t('dutyOvertimeList.status')">
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
        },
        initPromise:{},
        searchText:"",
        formLabelWidth: '25%',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      setSearchText() {
        this.$nextTick(function () {
          this.searchText = util.getSearchText(this.$refs.searchDialog);
        })
      },
      pageRequest() {
        this.pageLoading = true;
        this.setSearchText();
        var submitData = util.deleteExtra(this.formData);
        util.setQuery("dutyOvertimeList",submitData);
        axios.get('/api/basic/hr/dutyOvertime?'+qs.stringify(submitData)).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      }
    },created () {
       this.pageHeight = 0.75*window.innerHeight;
      this.initPromise = axios.get('/api/basic/hr/dutyOvertime/getQuery').then((response)=> {
        this.formData = response.data;
        util.copyValue(this.$route.query,this.formData);
      })
    },activated() {
      this.initPromise.then(()=> {
        this.pageRequest();
      })
    }
  };
</script>

