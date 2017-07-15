<template>
  <div>
    <head-tab active="folderFileList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary"@click="formVisible = true" icon="search">{{$t('folderFileList.filter')}}</el-button>
        <span  v-html="searchText"></span>
      </el-row>
      <search-dialog @enter="search()" :show="formVisible" @hide="formVisible=false" :title="$t('folderFileList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
              <el-form-item :label="$t('folderFileList.createdDate')">
                <date-range-picker v-model="formData.createdDate"></date-range-picker>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('folderFileList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('folderFileList.loading')" @sort-change="sortChange"  stripe border>
        <el-table-column  prop="id" :label="$t('folderFileList.id')" sortable width="150"></el-table-column>
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
        searchText:"",
        formData:{
          extra:{}
        },
        initPromise:{},
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
        util.setQuery("folderFileList",submitData);
        axios.get('/api/general/sys/folderFile?'+qs.stringify(submitData)).then((response) => {
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
        var that=this;
      that.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/general/sys/folderFile/getQuery').then((response) =>{
        that.formData=response.data;
      util.copyValue(that.$route.query,that.formData);
      that.pageRequest();
    });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      })
    }
  };
</script>

