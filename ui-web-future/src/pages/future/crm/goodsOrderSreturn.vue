<template>
  <div>
    <head-tab active="goodsOrderSreturn"></head-tab>
    <div>
      <el-form :model="inputForm"   ref="inputForm" label-width="150px"  :rules="rules" class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderSreturn.storeName')" >
              {{formProperty.storeName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSreturn.billDate')">
              {{formProperty.billDate}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSreturn.synToCloud')" prop="syn">
              <bool-radio-group v-model="inputForm.syn"></bool-radio-group>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSreturn.isUseTicket')" >
              {{formProperty.isUseTicket | bool2str}}
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="formSubmit()">{{$t('goodsOrderSreturn.returnProduct')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderSreturn.areaName')">
              {{formProperty.shopAreaName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSreturn.shopName')">
              {{formProperty.shopName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSreturn.remarks')">
              {{formProperty.remarks}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSreturn.credit')">
              {{formProperty.shopCredit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderSreturn.shouldGet')" >
              {{formProperty.shopShouldGet}}
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="formProperty.goodsOrderDetailDtoList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('goodsOrderSreturn.loading')" stripe border >
          <el-table-column  prop="productName" :label="$t('goodsOrderSreturn.productName')"></el-table-column>
          <el-table-column  prop="qty" :label="$t('goodsOrderSreturn.qty')"></el-table-column>
          <el-table-column  prop="billQty" :label="$t('goodsOrderSreturn.billQty')"></el-table-column>
          <el-table-column  prop="shippedQty" label="已发货数"></el-table-column>
          <el-table-column prop="returnQty" :label="$t('goodsOrderSreturn.returnQty')">
            <template scope="scope">
              <el-input  v-model="scope.row.returnQty" @blur="checkReturnQty(scope.row)"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="price" :label="$t('goodsOrderSreturn.price')"></el-table-column>
          <el-table-column prop="hasIme" :label="$t('goodsOrderSreturn.hasIme')" width="120">
            <template scope="scope">
              <el-tag :type="scope.row.hasIme ? 'primary' : 'danger'">{{scope.row.hasIme | bool2str}}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </div>
  </div>
</template>
<script>
  import boolRadioGroup from 'components/common/bool-radio-group';
  export default{
    components:{boolRadioGroup},
    data(){
      return{
        formProperty:{},
        submitDisabled:false,
        pageLoading:false,
        inputForm:{},
        rules: {
          syn: [{ required: true, message: this.$t('priceChangeForm.prerequisiteMessage')}],
        }
      }
    },
    methods:{
       formSubmit(){
         var that=this;
         that.submitDisabled = true;
         var form = this.$refs["inputForm"];
         form.validate((valid) => {
           if (valid) {
             var submitData = util.deleteExtra(this.inputForm);
             var  goodsOrderDetailFormList = new Array();
             for(var index in this.formProperty.goodsOrderDetailDtoList) {
               var detail = this.formProperty.goodsOrderDetailDtoList[index];
               if(detail.billQty-detail.shippedQty<detail.returnQty){
                 alert("退货数必须小于开单数");
                 return;
               }
               goodsOrderDetailFormList.push(detail);
             }
             submitData.goodsOrderDetailFormList = goodsOrderDetailFormList;
             axios.post('/api/ws/future/crm/goodsOrderShip/sreturn',qs.stringify(submitData, {allowDots:true})).then((response)=> {
               this.$message(response.data.message);
               this.$router.push({name:'goodsOrderShip',query:util.getQuery("goodsOrderShip"), params:{_closeFrom:true}})
             }).catch(function () {
               that.submitDisabled = false;
             });
           }else{
             that.submitDisabled = false;
           }
         })
      }, checkReturnQty(item){
         if(item.billQty-item.shippedQty<item.returnQty){
           alert("退货数必须小于开单数");
         }
      }
    },created(){
      axios.get('/api/ws/future/crm/goodsOrderShip/getForm').then((response)=>{
        this.inputForm=response.data;
        axios.get('/api/ws/future/crm/goodsOrderShip/getSreturn',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
          this.formProperty=response.data
        })
      });
    }
  }
</script>
