<template>
  <div>
    <head-tab active="storeAllotDetail"></head-tab>
    <div>
      <el-form :model="storeAllot" ref="inputForm"   label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('storeAllotDetail.businessId')">
              {{storeAllot.formatId}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.fromStore')">
              {{storeAllot.fromStoreName}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.toStore')">
              {{storeAllot.toStoreName}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.outCode')">
              {{storeAllot.outCode}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('storeAllotDetail.createdBy')">
              {{storeAllot.createdByName}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.createdDate')">
              {{storeAllot.createdDate}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.expressOrder')">
              {{storeAllot.expressOrderExpressCodes}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.remarks')">
              {{storeAllot.remarks}}
            </el-form-item>
          </el-col>
        </el-row>
        <div style="width:100%;height:50px;text-align:center;margin-top:25px;font-size:20px">{{$t('storeAllotDetail.billDetail')}}</div>
        <el-table :data="storeAllotDetailList"  v-loading="storeAllotDetailLoading" :element-loading-text="$t('storeAllotDetail.loading')" stripe border >
          <el-table-column  prop="productName" :label="$t('storeAllotDetail.productName')" ></el-table-column>
          <el-table-column prop="billQty" :label="$t('storeAllotDetail.billQty')" ></el-table-column>
          <el-table-column prop="shippedQty" :label="$t('storeAllotDetail.shippedQty')" ></el-table-column>
        </el-table>
        <div style="width:100%;height:50px;text-align:center;margin-top:25px;font-size:20px">{{$t('storeAllotDetail.shipDetail')}}</div>
        <el-table :data="storeAllotImeList" v-loading="storeAllotImeLoading" :element-loading-text="$t('storeAllotDetail.loading')" stripe border >
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
