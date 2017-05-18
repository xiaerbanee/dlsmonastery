<template>
  <div>
    <head-tab active="productImeDetail"></head-tab>
    <div>
      <el-form :model="productIme" ref="productIme"  label-width="120px" class="form input-form">
        <el-row :gutter="4">
          <el-col :span="12">
            <el-form-item :label="$t('productImeDetail.depotName')">
              {{productIme.depotName}}
            </el-form-item>
            <el-form-item :label="$t('productImeDetail.inputType')">
              {{productIme.inputType}}
            </el-form-item>
            <el-form-item :label="$t('productImeDetail.createdDate')"  >
              {{productIme.createdDate}}
            </el-form-item>

          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('productImeDetail.productName')"  >
              {{productIme.productName}}
            </el-form-item>
            <el-form-item :label="$t('productImeDetail.outType')"  >
            </el-form-item>
            <el-form-item :label="$t('productImeDetail.remarks')">
              {{productIme.remarks}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="productImeHistoryList"  style="margin-top:5px;"   stripe border >
          <el-table-column prop="id" :label="$t('productImeDetail.productImeHistoryId')"></el-table-column>
          <el-table-column prop="type" :label="$t('productImeDetail.productImeHistoryType')"></el-table-column>
          <el-table-column prop="fromDepotName" :label="$t('productImeDetail.productImeHistoryFromName')"></el-table-column>
          <el-table-column prop="toDepotName" :label="$t('productImeDetail.productImeHistoryToName')"></el-table-column>
          <el-table-column prop="createdByName" :label="$t('productImeDetail.productImeHistoryCreatedByName')"></el-table-column>
          <el-table-column prop="createdDate" :label="$t('productImeDetail.productImeHistoryCreatedDate')"></el-table-column>
          <el-table-column prop="remarks" :label="$t('productImeDetail.productImeHistoryRemarks')"></el-table-column>
        </el-table>
      </el-form>
    </div>
  </div>
</template>
<script>

  export default{

    data(){
      return {
        productImeHistoryList:[],
        productIme:{},
      }
    },created(){

      axios.get('/api/ws/future/crm/productIme/getProductImeDetail', {params: {id: this.$route.query.id}}).then((response) => {
        this.productIme = response.data;
      });
      axios.get('/api/ws/future/crm/productIme/getProductImeHistoryList', {params: {id: this.$route.query.id}}).then((response) => {
        this.productImeHistoryList = response.data;
      });
    }
  }
</script>
