<template>
  <div>
    <head-tab active="goodsOrderSign"></head-tab>
    <div>
      <el-form  ref="inputForm" label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderSign.businessId')">
              {{goodsOrder.businessId}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSign.remarks')" >
              {{goodsOrder.remarks}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSign.storeName')" >
              {{goodsOrder.store.name}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSign.shopName')" >
              {{goodsOrder.shop.name}}
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="formSubmit()">{{$t('goodsOrderSign.signIn')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="goodsOrder.goodsOrderDetailList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('goodsOrderSign.loading')" stripe border >
          <el-table-column  prop="product.name" :label="$t('goodsOrderSign.productName')"></el-table-column>
          <el-table-column prop="shippedQty" :label="$t('goodsOrderSign.shippedQty')"></el-table-column>
        </el-table>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        pageLoading:false,
         inputForm:{
          id:this.$route.query.id,
        },
        goodsOrder:{
         store:{name:""},
         shop:{name:""},
         remarks:"",
         businessId:"",
         goodsOrderDetailList:[]
        },
      }
    },
    methods:{
       formSubmit(){
        axios.post('/api/crm/goodsOrder/sign',qs.stringify(this.inputForm)).then((response)=> {
            this.$message(response.data.message);
            this.$router.push({name:'goodsOrderList',query:util.getQuery("goodsOrderList")})
          }).catch(function () {
           this.submitDisabled = false;
         });
      },
      findOne(){
        axios.get('/api/crm/goodsOrder/sign',{params: {id:this.$route.query.id}}).then((response)=>{
          this.goodsOrder=response.data;
        })
      }
    },created(){
      this.findOne();
    }
  }
</script>
