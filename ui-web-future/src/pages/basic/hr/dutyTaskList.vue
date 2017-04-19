<template>
  <div>
    <head-tab active="dutyTaskList"></head-tab>
    <div>
      <el-row>
        <el-button type="primary" @click="batchPass" icon="check">{{$t('dutyTaskList.batchPass')}}</el-button>
      </el-row>
      <el-table :data="page" :height="pageHeight" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('dutyTaskList.loading')" @selection-change="handleSelectionChange" stripe border >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column fixed prop="formatId" :label="$t('dutyTaskList.formatId')" sortable></el-table-column>
        <el-table-column prop="dutyType"  :label="$t('dutyTaskList.dutyType')" ></el-table-column>
        <el-table-column prop="dutyDate" :label="$t('dutyTaskList.dutyDate')"></el-table-column>
        <el-table-column prop="accountName" :label="$t('dutyTaskList.applyAccount')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('dutyTaskList.remarks')"></el-table-column>
        <el-table-column fixed="right" :label="$t('dutyTaskList.operation')" width="140">
          <template scope="scope">
            <el-button size="small" @click.native="itemAction(scope.row.id,'审核')">审核</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        page:[],
        multipleSelection:[],
        formLabelWidth: '120px',
        pageLoading: false
      };
    },
    methods: {
      pageRequest() {
        this.pageLoading = true;
        util.setQuery("dutyTaskList");
        axios.get('/api/basic/hr/duty').then((response) => {
          this.page = response.data;
          this.pageLoading = false;
        })
      },batchPass(){
        axios.get('/api/basic/hr/duty/batchPass',{params:{dutyAuditMap : this.multipleSelection}}).then((response) =>{
            this.$message(response.data.message);
            this.pageRequest();
          })
      },itemAction:function(id,action,dutyType){
        if(action=="审核") {
          this.$router.push({ name: 'dutyTaskForm', query: { id: id, dutyType: dutyType}})
        }
      },handleSelectionChange(selection) {
         this.multipleSelection={};
         for(var key in selection){
           this.multipleSelection[selection[key].id]=selection[key].dutyType
        }
      }
    },created () {
      this.pageHeight = window.outerHeight -320;
      this.pageRequest();
    }
  };
</script>

