<template>
  <div>
    <head-tab active="depotAccountDetail"></head-tab>
    <div>
      <el-table :data="depotAccountDetailList"  style="margin-top:5px;"  :element-loading-text="$t('depotAccountDetail.loading')" stripe border :row-class-name="tableRowClassName">
        <el-table-column fixed prop="billType" :label="$t('depotAccountDetail.billType')" width="330px"></el-table-column>
        <el-table-column prop="billNo" :label="$t('depotAccountDetail.billNo')"></el-table-column>
        <el-table-column prop="date" :label="$t('depotAccountDetail.date')"></el-table-column>
        <el-table-column prop="materialName" :label="$t('depotAccountDetail.materialName')"></el-table-column>
        <el-table-column prop="qty" :label="$t('depotAccountDetail.qty')"></el-table-column>
        <el-table-column prop="price" :label="$t('depotAccountDetail.price')"sortable></el-table-column>
        <el-table-column prop="amount" :label="$t('depotAccountDetail.amount')"></el-table-column>
        <el-table-column prop="receiveAmount" :label="$t('depotAccountDetail.receiveAmount')"></el-table-column>
        <el-table-column prop="shouldGet" :label="$t('depotAccountDetail.shouldGet')"></el-table-column>
        <el-table-column prop="endShouldGet" :label="$t('depotAccountDetail.endShouldGet')" sortable></el-table-column>
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
          console.log(row)
        if (row.infoLevel === "danger") {
          return 'danger-row';
        }
        return '';
      }
    },
    created(){
      axios.get('/api/ws/future/basic/depot/depotAccountDetailList',{params: {id:this.$route.query.id,dutyDateRange:this.$route.query.dutyDateRange}}).then((response)=>{

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
