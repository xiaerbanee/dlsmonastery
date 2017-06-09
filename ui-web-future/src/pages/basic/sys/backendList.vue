<template>
  <div>
    <head-tab active="backendList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" >{{$t('backendList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" >{{$t('backendList.filter')}}</el-button>
        <span v-html="searchText"></span>
      </el-row>
      <search-dialog :title="$t('backendList.filter')" v-model="formVisible" size="tiny" class="search-form" z-index="1500" ref="searchDialog">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="$t('backendModuleList.name')" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" placeholder="输入关键字搜索"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('backendList.sure')}}</el-button>
        </div>
      </search-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('backendList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('backendList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="code" :label="$t('backendList.code')"></el-table-column>
        <el-table-column prop="name" :label="$t('backendList.name')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('backendList.remarks')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('backendList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('backendList.createdDate')"></el-table-column>
        <el-table-column fixed="right" :label="$t('backendList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'edit')">修改</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'delete')">删除</el-button>
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
        formLabelWidth: '120px',
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
        var submitData= util.deleteExtra(this.formData);
        util.setQuery("backendList",submitData);
        axios.get('/api/basic/sys/backend?'+qs.stringify(submitData)).then((response) => {
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
        this.$router.push({ name: 'backendForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'backendForm', query: { id: id }})
        } else if(action=="delete") {
          util.confirmBeforeDelRecord(this).then(() => {
          axios.get('/api/basic/sys/backend/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          });
        }).catch(()=>{});
        }
      }
    },created () {
      var that = this;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/basic/sys/backend/getQuery').then((response) =>{
          that.formData=response.data;
          util.copyValue(that.$route.query,that.formData);
          that.pageRequest();
      });
    }
  };
</script>

