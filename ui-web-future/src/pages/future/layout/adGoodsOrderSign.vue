<template>
  <div>
    <head-tab active="adGoodsOrderSign"></head-tab>
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
              {{adGoodsOrder.outShopName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderSign.shopName')">
              {{adGoodsOrder.shopName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderSign.expressCodes')">
              {{adGoodsOrder.expressOrderExpressCodes}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('adGoodsOrderSign.billRemarks')" >
              {{adGoodsOrder.billRemarks}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderSign.createdByName')">
              {{adGoodsOrder.createdByName}}
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
        <el-table :data="adGoodsOrderDetailList" style="margin-top:5px;"  stripe border >
          <el-table-column prop="productCode" :label="$t('adGoodsOrderSign.code')" ></el-table-column>
          <el-table-column prop="productName" :label="$t('adGoodsOrderSign.productName')" ></el-table-column>
          <el-table-column prop="qty" :label="$t('adGoodsOrderSign.orderQty')"></el-table-column>
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
        adGoodsOrder:{},
        adGoodsOrderDetailList:[],
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        axios.post('/api/ws/future/layout/adGoodsOrder/sign?id='+this.$route.query.id).then((response)=> {
          this.$message(response.data.message);
          this.submitDisabled = false;
          if(response.data.success){
            this.$router.push({name:'adGoodsOrderList',query:util.getQuery("adGoodsOrderList"), params:{_closeFrom:true}});
          }
        }).catch( () => {
          this.submitDisabled = false;
        });
      }, initPage(){
        axios.get('/api/ws/future/layout/adGoodsOrder/findDto',{params: {id:this.$route.query.id}}).then((response)=>{
          this.adGoodsOrder = response.data;
        });
        axios.get('/api/ws/future/layout/adGoodsOrder/findDetailListByAdGoodsOrderId',{params: {adGoodsOrderId:this.$route.query.id}}).then((response)=>{
          this.adGoodsOrderDetailList = response.data;
        });
      }
    },created(){
      this.initPage();
    }
  }
</script>
