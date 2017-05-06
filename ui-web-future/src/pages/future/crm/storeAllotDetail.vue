<template>
  <div>
    <head-tab active="storeAllotDetail"></head-tab>
    <div>
      <el-form :model="detailForm" ref="inputForm" :rules="rules" label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('storeAllotDetail.businessId')" prop="businessId">
              {{detailForm.businessId}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.fromStore')" prop="fromStoreName">
              {{detailForm.fromStoreName}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.toStore')" prop="toStoreName">
              {{detailForm.toStoreName}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.outCode')" prop="outCode">
              {{detailForm.outCode}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('storeAllotDetail.createdBy')" prop="createdByName">
              {{detailForm.createdByName}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.createdDate')" prop="createdDate">
              {{detailForm.createdDate}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.expressOrder')" prop="expressOrderCodes">
              {{detailForm.expressOrderCodes}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.remarks')" prop="remarks">
              {{detailForm.remarks}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="detailForm.storeAllotDetailDtoList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('storeAllotDetail.loading')" stripe border >
          <el-table-column  prop="productName" :label="$t('storeAllotDetail.productName')" ></el-table-column>
          <el-table-column prop="billQty" :label="$t('storeAllotDetail.billQty')" ></el-table-column>
          <el-table-column prop="shippedQty" :label="$t('storeAllotDetail.shippedQty')" ></el-table-column>
        </el-table>
        <el-table :data="detailForm.storeAllotImeDtoList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('storeAllotDetail.loading')" stripe border >

          <el-table-column  prop="productName" :label="$t('storeAllotDetail.productName')" ></el-table-column>
          <el-table-column  prop="productIme" :label="$t('storeAllotDetail.productIme')" ></el-table-column>
          <el-table-column  prop="productMeid" :label="$t('storeAllotDetail.productMeid')" ></el-table-column>
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
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        detailForm:{},
        rules: {},
        pageLoading:false
      }
    },
    methods:{
      findOne(){
        axios.get('/api/ws/future/crm/storeAllot/findForOverview',{params:{id:this.$route.query.id}} ).then((response)=>{
          this.detailForm=response.data;
          console.log(response.data)
        })
      }
    },created(){
      this.findOne();
    }
  }
</script>
