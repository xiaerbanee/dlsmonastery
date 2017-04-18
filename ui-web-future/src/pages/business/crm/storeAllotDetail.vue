<template>
  <div>
    <head-tab :active="$t('storeAllotDetail.storeAllotDetail') "></head-tab>
    <div>
      <el-form :model="detailForm" ref="inputForm" :rules="rules" label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('storeAllotDetail.businessId')" prop="createdBy">
              {{detailForm.businessId}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.fromStore')" prop="createdDate">
              {{detailForm.fromStore.name}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.toStore')" prop="id">
              {{detailForm.toStore.name}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.outCode')" prop="title">
              {{detailForm.outcode}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('storeAllotDetail.createdBy')" prop="createdBy">
              {{detailForm.created.loginName}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.createdDate')" prop="createdDate">
              {{detailForm.createdDate}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.expressOrder')" prop="id">
              {{detailForm.expressOrder}}
            </el-form-item>
            <el-form-item :label="$t('storeAllotDetail.remarks')" prop="title">
              {{detailForm.remarks}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="detailForm.storeAllotDetailList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('storeAllotDetail.loading')" stripe border >
          <el-table-column  prop="product.name" :label="$t('storeAllotDetail.productName')" ></el-table-column>
          <el-table-column prop="billQty" :label="$t('storeAllotDetail.billQty')" width="200"></el-table-column>
          <el-table-column prop="shippedQty" :label="$t('storeAllotDetail.shippedQty')" width="200"></el-table-column>
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
        detailForm:{
          toStore:{name:''},
          fromStore:{name:''},
          created:{loginName:''},
          storeAllotDetailList:[]
        },
        rules: {
        },
        pageLoading:false
      }
    },
    methods:{
      findOne(){
        axios.get('/api/crm/storeAllot/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.detailForm=response.data;
          console.log(response.data)
        })
      }
    },created(){
      this.findOne();
    }
  }
</script>
