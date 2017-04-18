<template>
  <div>
    <head-tab :active="$t('goodsOrderSreturn.goodsOrderSreturn') "></head-tab>
    <div>
      <el-form  ref="inputForm" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderSreturn.storeName')" >
              {{inputForm.store.name}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSreturn.billDate')">
              {{inputForm.billDate}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSreturn.synToCloud')" prop="syn">
              <el-radio-group v-model="inputForm.syn">
                <el-radio v-for="(value,key) in sreturnFormProperty.bools" :key="key" :label="value">{{key | bool2str}} </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSreturn.isUseTicket')" >
              {{inputForm.isUseTicket | bool2str}}
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="formSubmit()">{{$t('goodsOrderSreturn.returnProduct')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderSreturn.areaName')">
              {{inputForm.shop.area.name}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSreturn.shopName')">
              {{inputForm.shop.name}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSreturn.remarks')">
              {{inputForm.remarks}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSreturn.credit')">
              {{inputForm.shop.credit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSreturn.shouldGet')" >
              {{inputForm.shouldGet}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="inputForm.goodsOrderDetailList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('goodsOrderSreturn.loading')" stripe border >
          <el-table-column  prop="product.name" :label="$t('goodsOrderSreturn.productName')"></el-table-column>
          <el-table-column  prop="qty" :label="$t('goodsOrderSreturn.qty')"></el-table-column>
          <el-table-column  prop="billQty" :label="$t('goodsOrderSreturn.billQty')"></el-table-column>
          <el-table-column prop="returnQty" :label="$t('goodsOrderSreturn.returnQty')">
            <template scope="scope">
              <el-input  v-model="scope.row.returnQty"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="price" :label="$t('goodsOrderSreturn.price')"></el-table-column>
          <el-table-column prop="hasIme" :label="$t('goodsOrderSreturn.hasIme')" width="120">
            <template scope="scope">
              <el-tag :type="scope.row.product.hasIme ? 'primary' : 'danger'">{{scope.row.product.hasIme | bool2str}}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
      sreturnFormProperty:{},
        pageLoading:false,
         inputForm:{
          id:this.$route.query.id,
          store:{name:""},
          isUseTicket:"1",
          syn:"",
          shop:{name:"",area:{name:""},credit:""},
          remarks:"",
          shouldGet:"",
          goodsOrderDetailList:[],
          billDate:"",
        },
        goodsOrder:"",
      }
    },
    methods:{
       formSubmit(){
        axios.post('/api/crm/goodsOrder/sreturn',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
            this.$message(response.data.message);
            this.$router.push({name:'goodsOrderList',query:util.getQuery("goodsOrderList")})
          });
      },
      findOne(){
        axios.get('/api/crm/goodsOrder/sreturn',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
          this.inputForm.goodsOrderDetailList=response.data.goodsOrderDetailList
        })
      },
      getFormProperty(){
        axios.get('/api/crm/goodsOrder/sreturnFormProperty').then((response)=>{
          this.sreturnFormProperty=response.data;
        });
      }
    },created(){
      this.getFormProperty()
      this.findOne();
    }
  }
</script>
