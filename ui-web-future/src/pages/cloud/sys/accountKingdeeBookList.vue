<template>
  <div>
    <head-tab active="accountKingdeeBookList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" >添加</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
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
        <el-table-column fixed prop="accountId" label="outId" sortable width="100"></el-table-column>
        <el-table-column prop="username" label="账套登入名"></el-table-column>
        <el-table-column prop="password" label="账套登入密码"></el-table-column>
        <el-table-column prop="kingdeeBookName" label="账套名称"></el-table-column>
        <el-table-column prop="kingdeeBookType" label="账套类型"></el-table-column>
        <el-table-column prop="companyId" label="companyId"></el-table-column>
        <el-table-column fixed="right" label="操作" width="150">
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
          companyId:'',
          name:'',
          type:'',
        },
        formLabel:{
          companyId:{label: "公司ID"},
          name:{label: "账套"},
          type:{label: "账套类型"}
        },
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
        axios.get('/api/global/cloud/sys/accountKingdeeBook',{params:this.submitData}).then((response) => {
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
      that.pageRequest();
    }
  };
</script>

