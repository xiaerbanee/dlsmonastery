<template>
  <div>
    <head-tab active="backendModuleList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" v-permit="'sys:backendModule:edit'">{{$t('backendModuleList.add')}}</el-button>
        <el-button type="primary"@click="formVisible = true" icon="search" >{{$t('backendModuleList.filter')}}</el-button>
        <span  v-html="searchText"></span>
      </el-row>
      <search-dialog :show="formVisible" @hide="formVisible=false" :title="$t('backendModuleList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData" :label-width="formLabelWidth">
              <el-form-item :label="$t('backendModuleList.name')">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('backendModuleList.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="$t('backendModuleList.backendName')">
                <el-input v-model="formData.backendName" auto-complete="off" :placeholder="$t('backendModuleList.likeSearch')"></el-input>
              </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('backendModuleList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('backendModuleList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('backendModuleList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="code" :label="$t('backendModuleList.code')"></el-table-column>
        <el-table-column prop="name" :label="$t('backendModuleList.name')"></el-table-column>
        <el-table-column prop="backendName" :label="$t('backendModuleList.backendName')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('backendModuleList.remarks')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('backendModuleList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('backendModuleList.createdDate')"></el-table-column>
        <el-table-column fixed="right" :label="$t('backendModuleList.operation')" width="140">
          <template scope="scope">
            <div class="action"> <el-button size="small" @click.native="itemAction(scope.row.id,'edit')"  v-permit="'sys:backendModule:edit'">修改</el-button></div>
            <div class="action"> <el-button size="small" @click.native="itemAction(scope.row.id,'delete')"  v-permit="'sys:backendModule:delete'">删除</el-button></div>
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
        util.setQuery("backendModuleList",submitData);
        axios.get('/api/basic/sys/backendModule?'+qs.stringify(submitData)).then((response) => {
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
      },itemAdd(){
        this.$router.push({ name: 'backendModuleForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'backendModuleForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/basic/sys/backendModule/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          });
        }).catch(()=>{});
        }
      }
    },created () {
      var that = this;
      that.pageHeight = window.outerHeight -320;
      this.initPromise = axios.get('/api/basic/sys/backendModule/getQuery').then((response) =>{
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

