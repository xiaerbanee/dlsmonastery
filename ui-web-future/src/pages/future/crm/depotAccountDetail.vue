<template>
  <div>
    <head-tab active="depotAccountDetail"></head-tab>
    <div>
      <el-table :data="depotAccountDetailList"  style="margin-top:5px;"  :element-loading-text="$t('depotAccountDetail.loading')" stripe border :row-class-name="tableRowClassName">
        <el-table-column fixed prop="billType" :label="$t('depotAccountDetail.billType')" width="330px"></el-table-column>
        <el-table-column prop="billNo" :label="$t('depotAccountDetail.billNo')"></el-table-column>
        <el-table-column prop="billDate" :label="$t('depotAccountDetail.date')"></el-table-column>
        <el-table-column prop="materialName" :label="$t('depotAccountDetail.materialName')"></el-table-column>
        <el-table-column prop="qty" :label="$t('depotAccountDetail.qty')"></el-table-column>
        <el-table-column prop="price" :label="$t('depotAccountDetail.price')"></el-table-column>
        <el-table-column prop="totalAmount" :label="$t('depotAccountDetail.amount')"></el-table-column>
        <el-table-column prop="realGet" :label="$t('depotAccountDetail.receiveAmount')"></el-table-column>
        <el-table-column prop="shouldGet" :label="$t('depotAccountDetail.shouldGet')"></el-table-column>
        <el-table-column prop="endShouldGet" :label="$t('depotAccountDetail.endShouldGet')"></el-table-column>
        <el-table-column prop="remarks" :label="$t('depotAccountDetail.remarks')"></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        depotAccountDetailList:[],
        }
    },
    methods:{
      tableRowClassName(row, index) {

        if (row.billStatus !== "C") {
          return 'danger-row';
        }
        return '';
      }
    },
    created(){

      axios.get('/api/global/cloud/report/customerReceive/list',{params: {customerIdList:[this.$route.query.clientOutId], dateRange:this.$route.query.dutyDateRange}}).then((response)=>{

          if(response.data){
            this.depotAccountDetailList=response.data;
          }else{
            this.depotAccountDetailList=[];
          }
        })
    }
  }
</script>
<style>
  .el-table .danger-row {
    background: #DC143C;
  }
</style>
