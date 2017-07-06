<template>
  <div>
    <head-tab active="clientList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search" >{{$t('clientList.filter')}}</el-button>
        <el-button type="primary" @click="synData"  icon="plus">{{$t('depotList.syn')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('clientList.filter')" v-model="formVisible"  size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-form-item :label="$t('clientList.name')" :label-width="formLabelWidth">
            <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('clientList.likeSearch')"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('clientList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('clientList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="name" :label="$t('clientList.name')" sortable ></el-table-column>
        <el-table-column prop="outId" label="财务编号"  sortable></el-table-column>
        <el-table-column prop="outCode" label="财务编码" ></el-table-column>
        <el-table-column prop="outGroupId" label="财务分组编号"></el-table-column>
        <el-table-column prop="outGroupName" label="财务分组名称"></el-table-column>
        <el-table-column prop="outDate" label="财务同步日期"></el-table-column>
        <el-table-column prop="depotNameStr" label="OA门店"></el-table-column>
      </el-table>
      <pageable :page="page" v-on:pageChange="pageChange"></pageable>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        searchText:"",
        page:{},
        formData:{
            extra:{}
        },
        initPromise:{},
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
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
        util.setQuery("clientList",submitData);
        axios.get('/api/ws/future/basic/client?'+qs.stringify(submitData)).then((response) =>  {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.sort=util.getSort(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },synData(){
        axios.get('/api/ws/future/basic/client/syn').then((response) =>{
          this.$message(response.data.message);
          this.pageRequest();
        })
      }
    },created () {
      let that = this;
      that.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/ws/future/basic/client/getQuery').then((response) =>{
        that.formData=response.data;
        util.copyValue(that.$route.query,that.formData);
      });
    },activated(){
      this.initPromise.then(()=>{
        this.pageRequest();
      })
    }
  };
</script>

