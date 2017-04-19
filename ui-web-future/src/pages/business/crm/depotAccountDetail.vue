<template>
  <div>
    <head-tab active="depotAccountDetail"></head-tab>
    <div>
      <el-table :data="depotAccountDetail"  style="margin-top:5px;"  :element-loading-text="$t('depotAccountDetail.loading')" stripe border :row-class-name="tableRowClassName">
        <el-table-column fixed prop="billType" :label="$t('depotAccountDetail.billType')" width="330px"></el-table-column>
        <el-table-column prop="billNo" :label="$t('depotAccountDetail.billNo')"></el-table-column>
        <el-table-column prop="date" :label="$t('depotAccountDetail.date')"></el-table-column>
        <el-table-column prop="materialName" :label="$t('depotAccountDetail.materialName')"></el-table-column>
        <el-table-column prop="quantity" :label="$t('depotAccountDetail.quantity')"></el-table-column>
        <el-table-column prop="price" :label="$t('depotAccountDetail.price')"sortable></el-table-column>
        <el-table-column prop="amount" :label="$t('depotAccountDetail.amount')"></el-table-column>
        <el-table-column prop="payableAmount" :label="$t('depotAccountDetail.payableAmount')"></el-table-column>
        <el-table-column prop="actualPayAmount" :label="$t('depotAccountDetail.actualPayAmount')"></el-table-column>
        <el-table-column prop="endAmount" :label="$t('depotAccountDetail.endAmount')" sortable></el-table-column>
        <el-table-column prop="note" :label="$t('expressOrderList.remarks')"></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        depotAccountDetail:[],
        }
    },
    methods:{
      tableRowClassName(row, index) {
          console.log(row)
        if (row.css === "danger") {
          return 'info-row';
        }
        return '';
      }
    },
    created(){
      axios.get('/api/crm/depot/depotAccountDetail',{params: {id:this.$route.query.id,dateRange:this.$route.query.dateRange}}).then((response)=>{
           this.depotAccountDetail=response.data
        })
    }
  }
</script>
<style>
  .el-table .info-row {
    background: #DC143C;
  }
</style>
