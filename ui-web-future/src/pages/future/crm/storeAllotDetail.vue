<template>
  <div>
    <head-tab active="storeAllotDetail"></head-tab>
    <div>
      <el-form :model="storeAllot" ref="inputForm"   label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('storeAllotDetail.businessId')" prop="businessId">
              {{storeAllot.formatId}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.fromStore')" prop="fromStoreName">
              {{storeAllot.fromStoreName}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.toStore')" prop="toStoreName">
              {{storeAllot.toStoreName}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.outCode')" prop="outCode">
              {{storeAllot.outCode}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('storeAllotDetail.createdBy')" prop="createdByName">
              {{storeAllot.createdByName}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.createdDate')" prop="createdDate">
              {{storeAllot.createdDate}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.expressOrder')" prop="expressOrderCodes">
              {{storeAllot.expressOrderCodes}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.remarks')" prop="remarks">
              {{storeAllot.remarks}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="storeAllotDetailList" style="margin-top:5px;" v-loading="storeAllotDetailLoading" :element-loading-text="$t('storeAllotDetail.loading')" stripe border >
          <el-table-column  prop="productName" :label="$t('storeAllotDetail.productName')" ></el-table-column>
          <el-table-column prop="billQty" :label="$t('storeAllotDetail.billQty')" ></el-table-column>
          <el-table-column prop="shippedQty" :label="$t('storeAllotDetail.shippedQty')" ></el-table-column>
        </el-table>
        <el-table :data="storeAllotImeList" style="margin-top:5px;" v-loading="storeAllotImeLoading" :element-loading-text="$t('storeAllotDetail.loading')" stripe border >
          <el-table-column  prop="productName" :label="$t('storeAllotDetail.productName')" ></el-table-column>
          <el-table-column  prop="productImeIme" :label="$t('storeAllotDetail.productIme')" ></el-table-column>
          <el-table-column  prop="productImeMeid" :label="$t('storeAllotDetail.productMeid')" ></el-table-column>
          <el-table-column  prop="createdByName" :label="$t('storeAllotDetail.sendByName')" ></el-table-column>
          <el-table-column  prop="createdDate" :label="$t('storeAllotDetail.sendDate')" ></el-table-column>
        </el-table>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        storeAllotImeLoading:false,
        storeAllotDetailLoading:false,
        storeAllot:{},
        storeAllotDetailList:[],
        storeAllotImeList:[],
      }
    },created(){
      axios.get('/api/ws/future/crm/storeAllot/findForView',{params:{id:this.$route.query.id}} ).then((response)=>{
        this.storeAllot=response.data;
      });

      this.storeAllotDetailLoading=true;
      axios.get('/api/ws/future/crm/storeAllotDetail/getDetails',{params:{storeAllotId:this.$route.query.id}} ).then((response)=>{
        this.storeAllotDetailList=response.data;
        this.storeAllotDetailLoading=false;
      });

      this.storeAllotImeLoading=true;
      axios.get('/api/ws/future/crm/storeAllotIme/getImes',{params:{storeAllotId:this.$route.query.id}} ).then((response)=>{
        this.storeAllotImeList=response.data;
        this.storeAllotImeLoading=false;
      });
    }
  }
</script>
