<template>
  <div>
    <head-tab active="dutyFreeList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary"@click="formVisible = true" icon="search">{{$t('dutyFreeList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('dutyFreeList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
              <el-form-item :label="$t('dutyFreeList.freeDate')" >
                <date-range-picker v-model="formData.dutyDate"></date-range-picker>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('dutyFreeList.sure')}}</el-button>
        </div>
      </search-dialog>
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
          extra:{}
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
        util.setQuery("dutyFreeList",submitData);
        axios.get('/api/basic/hr/dutyFree?'+qs.stringify(submitData)).then((response) => {
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
      this.initPromise = axios.get('/api/basic/hr/dutyFree/getQuery').then((response)=> {
        this.formData = response.data;
        util.copyValue(this.$route.query,this.formData);
      });
    },activated() {
      this.initPromise.then(() => {
        this.pageRequest();
      });
    }
  };
</script>

