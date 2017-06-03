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
              <el-form-item :label="formLabel.accountId.label" :label-width="formLabelWidth">
                <el-input v-model="formData.accountId" auto-complete="off" placeholder="请输入"></el-input>
              </el-form-item>
            </el-row>
            <el-row :gutter="4">
              <el-form-item :label="formLabel.username.label" :label-width="formLabelWidth">
                <el-input v-model="formData.username" auto-complete="off" placeholder="模糊匹配搜索"></el-input>
              </el-form-item>
            </el-row>
            <el-row :gutter="4">
              <el-form-item :label="formLabel.companyId.label" :label-width="formLabelWidth">
                <el-input v-model="formData.companyId" auto-complete="off" placeholder="请输入"></el-input>
              </el-form-item>
            </el-row>
            <el-row :gutter="4">
            <el-form-item :label="formLabel.kingdeeBookName.label" :label-width="formLabelWidth">
              <el-select v-model="formData.kingdeeBookName" filterable clearable placeholder="请选择">
                <el-option v-for="name in formData.kingdeeBookNameList" :key="name" :label="name" :value="name"></el-option>
              </el-select>
            </el-form-item>
            </el-row>
            <el-row :gutter="4">
              <el-form-item :label="formLabel.kingdeeBookType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.kingdeeBookType" filterable clearable placeholder="请选择">
                  <el-option v-for="type in formData.kingdeeBookTypeList" :key="type" :label="type" :value="type"></el-option>
                </el-select>
              </el-form-item>
            </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="拼命加载中....." @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="accountId" label="accountId" sortable width="120"></el-table-column>
        <el-table-column prop="username" label="账套登入名"></el-table-column>
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
          accountId:'',
          username:'',
          kingdeeBookName:'',
          kingdeeBookType:'',
        },
        formLabel:{
          companyId:{label: "公司ID"},
          accountId:{label: "用户ID"},
          username:{label: "金蝶账户"},
          kingdeeBookName:{label: "账套名称"},
          kingdeeBookType:{label: "账套类型"},
        },
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.getQuery("accountKingdeeBookList");
        util.copyValue(this.formData,this.submitData);
        util.setQuery("accountKingdeeBookList",this.submitData);
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
        this.$router.push({ name: 'accountKingdeeBookForm'})
      },itemAction:function(id,action){
        if(action=="修改") {
          this.$router.push({ name: 'accountKingdeeBookForm', query: { id: id }})
        } else if(action=="删除") {
          axios.get('/api/global/cloud/sys/accountKingdeeBook/delete',{params:{id:id}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
        }
      }
    },created () {
      var that = this;
      that.formData = that.submitData;
      that.pageHeight = window.outerHeight -320;
      axios.get('/api/global/cloud/sys/accountKingdeeBook/queryProperty').then((response) =>{
        that.formData = response.data;
        that.pageRequest();
      });
    }
  };
</script>

