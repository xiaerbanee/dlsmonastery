<template>
  <div>
    <head-tab :active="$t('adGoodsOrderSign.adGoodsOrderSign') "></head-tab>
    <div>
      <el-form :model="adGoodsOrder" ref="adGoodsOrder" label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('adGoodsOrderSign.orderId')">
              {{adGoodsOrder.id}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderSign.orderRemarks')" >
              {{adGoodsOrder.remarks}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderSign.outShopName')">
              {{adGoodsOrder.outShop.name}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderSign.shopName')">
              {{adGoodsOrder.shop.name}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderSign.expressCodes')">
              {{adGoodsOrder.expressOrder.expressCodes}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('adGoodsOrderSign.billRemarks')" >
              {{adGoodsOrder.billRemarks}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderSign.createdByName')">
              {{adGoodsOrder.createdBy.name}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderSign.createdDate')" >
              {{adGoodsOrder.createdDate}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderSign.billDate')">
              {{adGoodsOrder.billDate}}
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adGoodsOrderSign.materialSignIn')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="adGoodsOrder.adGoodsOrderDetailList" style="margin-top:5px;"  stripe border >
          <el-table-column prop="product.code" :label="$t('adGoodsOrderSign.code')" ></el-table-column>
          <el-table-column prop="product.name" :label="$t('adGoodsOrderSign.productName')" ></el-table-column>
          <el-table-column prop="orderQty" :label="$t('adGoodsOrderSign.orderQty')"></el-table-column>
          <el-table-column prop="confirmQty" :label="$t('adGoodsOrderSign.confirmQty')"></el-table-column>
          <el-table-column prop="billQty" :label="$t('adGoodsOrderSign.billQty')"></el-table-column>
          <el-table-column prop="shippedQty" :label="$t('adGoodsOrderSign.shippedQty')"></el-table-column>
        </el-table>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        adGoodsOrder:"",
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
          if (valid) {
            axios.post('/api/crm/adGoodsOrder/sign',{id:this.$route.query.id}).then((response)=> {
              this.$message(response.data.message);
              this.$router.push({name:'adGoodsOrderList',query:util.getQuery("adGoodsOrderList")})
            });
          }else{
            this.submitDisabled = false;
          }
      }, findOne(){
        axios.get('/api/crm/adGoodsOrder/sign',{params: {id:this.$route.query.id}}).then((response)=>{
          this.adGoodsOrder=response.data.adGoodsOrder;
        })
      }
    },created(){
      this.findOne();
    }
  }
</script>
