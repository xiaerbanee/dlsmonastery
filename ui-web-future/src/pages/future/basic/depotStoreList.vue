<template>
  <div>
    <head-tab active="depotStoreList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="itemAdd" icon="plus" >添加</el-button>
        <el-button type="primary" @click="formVisible = true" icon="search">过滤</el-button>
        <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
      </el-row>
      <el-dialog title="过滤" v-model="formVisible"  size="tiny" class="search-form">
        <el-form :model="formData">

        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">过滤</el-button>
        </div>
      </el-dialog>
      <el-table :data="page.content" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" element-loading-text="数据加载中" @sort-change="sortChange" stripe border>
        <el-table-column fixed prop="depotName" label="名称" sortable width="120"></el-table-column>
        <el-table-column prop="type" label="仓库类型"  />
        <el-table-column prop="storeGroup" label="分组"  />
        <el-table-column prop="officeName" label="机构"  />
        <el-table-column prop="taxName" label="税务门店名称"  />
        <el-table-column prop="delegateDepotName" label="寄售门店" />
        <el-table-column prop="remarks" label="备注" />
        <el-table-column fixed="right" label="操作" width="140">
          <template scope="scope">
            <el-button size="small"  v-permit="'crm:expressCompany:edit'" @click.native="itemAction(scope.row.id,'edit')">修改</el-button>
            <el-button size="small"  v-permit="'crm:expressCompany:edit'" @click.native="itemAction(scope.row.id,'delete')">删除</el-button>

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
          page:0,
          size:25,
        },formLabel:{
        },
        formProperty:{},
        formLabelWidth: '120px',
        formVisible: false,
        pageLoading: false,
        remoteLoading:false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("depotStoreList",this.formData);
        axios.get('/api/ws/future/basic/depotStore',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.pageRequest();
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.page=0;
        this.pageRequest();
      },search() {
        this.formVisible = false;
        this.pageRequest();
      },itemAdd(){
        this.$router.push({ name: 'depotStoreForm'})
      },itemAction:function(id,action){
        if(action=="edit") {
          this.$router.push({ name: 'depotStoreForm', query: { id: id }})
        }else if(action=="delete"){
          util.confirmBeforeDelRecord(this).then(() => {
            axios.get('/api/ws/future/basic/depotStore/delete',{params:{id:id}}).then((response) =>{
              this.$message(response.data.message);
              this.pageRequest();
            });
          });
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      util.copyValue(this.$route.query,this.formData);
      this.pageRequest();
    }
  };
</script>

