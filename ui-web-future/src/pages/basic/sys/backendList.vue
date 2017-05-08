<template>
  <div>
    <head-tab active="backendList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" >{{$t('backendList.add')}}</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search" >{{$t('backendList.filter')}}</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog :title="$t('backendList.filter')" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" placeholder="输入关键字搜索"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('backendList.sure')}}</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('backendList.loading')" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="id" :label="$t('backendList.id')" sortable width="150"></el-table-column>
        <el-table-column prop="code" :label="$t('backendList.code')"></el-table-column>
        <el-table-column prop="name" :label="$t('backendList.name')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('backendList.remarks')"></el-table-column>
        <el-table-column prop="createdByName" :label="$t('backendList.createdBy')"></el-table-column>
        <el-table-column prop="createdDate" :label="$t('backendList.createdDate')"></el-table-column>
        <el-table-column fixed="right" :label="$t('backendList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'修改')">修改</el-button>
            <el-button size="small" @click.native="itemAction(scope.row.id,'删除')">删除</el-button>
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
        formData:{},
        submitData:{
          page:0,
          size:25,
          name:''
        },
        formLabel:{
          name:{label: this.$t('backendModuleList.name')},
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.copyValue(this.formData,this.submitData);
        util.setQuery("backendList",this.submitData);
        axios.get('/api/basic/sys/backend?'+qs.stringify(this.submitData)).then((response) => {
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
        if(action=="修改") {
          this.$router.push({ name: 'backendForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/basic/sys/backend/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      var that = this;
      that.pageHeight = window.outerHeight -320;
      util.copyValue(that.$route.query,that.formData);
      that.pageRequest();
    }
  };
</script>

