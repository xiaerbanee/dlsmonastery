<template>
  <div>
    <head-tab active="kingdeeBookList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="formVisible = true" icon="search" v-permit="'cloud:payableReport:view'">过滤</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog title="过滤" v-model="formVisible" size="tiny" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="24">
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="拼命加载中....." @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="companyId" label="公司ID" sortable width="150"></el-table-column>
        <el-table-column prop="name" label="账套"></el-table-column>
        <el-table-column prop="type" label="账套类型"></el-table-column>
        <el-table-column prop="kingdeeUrl" label="URL"></el-table-column>
        <el-table-column prop="kingdeePostUrl" label="POST-URL"></el-table-column>
        <el-table-column prop="kingdeeUsername" label="用户名"></el-table-column>
        <el-table-column prop="kingdeePassword" label="密码"></el-table-column>
        <el-table-column prop="kingdeeDbid" label="DBID"></el-table-column>
        <el-table-column fixed="right" label="操作" width="120">
          <template scope="scope">
            <el-button size="small">修改</el-button>
            <el-button size="small">删除</el-button>
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
        },
        formLabel:{
        },
        pickerDateOption:util.pickerDateOption,
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.getQuery("kingdeeBookList");
        util.copyValue(this.formData,this.submitData);
        util.setQuery("kingdeeBookList",this.submitData);
        axios.get('/api/global/cloud/sys/kingdeeBook/list',{params:this.submitData}).then((response) => {
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
        this.$router.push({ name: 'kingdeeBookForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'kingdeeBookForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/global/cloud/sys/kingdeeBook/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      var that = this;
      that.formData = that.submitData;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/global/cloud/sys/kingdeeBook/getQuery').then((response) =>{
        that.formData = response.data;
        that.pageRequest();
      });
    }
  };
</script>

