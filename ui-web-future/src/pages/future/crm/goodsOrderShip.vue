<template>
  <div>
    <head-tab active="goodsOrderShip"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderShip.businessId')" prop="businessId">
              <el-input v-model="inputForm.formatId"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.storeName')" prop="storeId">
              {{goodsOrder.storeName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.boxImeStr')" prop="boxImeStr">
              <textarea  v-model="inputForm.boxImeStr" :rows="5" class="el-textarea__inner"
                         @paste="shipBoxAndIme"
                         @keyup.enter="shipBoxAndIme"
                         @keyup.delete="shipBoxAndIme"
                         @keyup.backspace="shipBoxAndIme"
                         @keyup.control="shipBoxAndIme">
              </textarea>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.expressCodes')" prop="expressCodes">
              <el-input type="textarea" v-model="inputForm.expressCodes" ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderShip.shopName')" prop="shopId">
              {{goodsOrder.shopName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.remarks')" prop="remarks">
              {{goodsOrder.remarks}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.redirectView')" prop="redictView">
              <bool-radio-group v-model="inputForm.redirectView"></bool-radio-group>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.imeStr')" prop="imeStr">
              <textarea v-model="inputForm.imeStr"  :rows="5" class="el-textarea__inner"
                        @paste="shipBoxAndIme"
                        @keyup.enter="shipBoxAndIme"
                        @keyup.delete="shipBoxAndIme"
                        @keyup.backspace="shipBoxAndIme"
                        @keyup.control="shipBoxAndIme">
              </textarea>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.shipRemarks')" prop="shipRemarks">
              <el-input type="textarea" v-model="inputForm.shipRemarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderShip.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-table :data="goodsOrder.goodsOrderDetailList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('goodsOrderShip.loading')" stripe border >
          <el-table-column  prop="productName" :label="$t('goodsOrderShip.productName')" sortable width="200"></el-table-column>
          <el-table-column prop="product.hasIme" :label="$t('goodsOrderShip.hasIme')" >
            <template scope="scope">
              <el-tag :type="scope.row.product.hasIme ? 'primary' : 'danger'">{{scope.row.product.hasIme | bool2str}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="billQty"  :label="$t('goodsOrderShip.billQty')"></el-table-column>
          <el-table-column prop="returnQty" :label="$t('goodsOrderShip.returnQty')"></el-table-column>
          <el-table-column prop="shippedQty" :label="$t('goodsOrderShip.shippedQty')"></el-table-column>
          <el-table-column prop="extendMap.waitShipQty" :label="$t('goodsOrderShip.waitShipQty')" ></el-table-column>
          <el-table-column prop="shipQty" :label="$t('goodsOrderShip.shipQty')"></el-table-column>
        </el-table>
      </el-form>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        submitDisabled:false,
        inputForm:{},
        goodsOrder:{},
        submitData:{
          id:'',
          businessId:'',
          boxImeStr:'',
          expressCodes:'',
          redirectView:'',
          imeStr:'',
          shipRemarks:''
        },
        rules: {},
        pageLoading:false,
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/crm/goodsOrder/ship',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
              if(response.data.errors){
                this.error=response.data.errors.id.message
                this.alertError=true;
                this.submitDisabled = false;
              }else{
                this.$message(response.data.message);
                if(response.data.success){
                  this.$router.push({name:'goodsOrderShipList',query:util.getQuery("goodsOrderShipList")})
                }
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },showSummary(isSubmit){
        var boxImeStr=this.inputForm.boxImeStr;
        var imeStr=this.inputForm.imeStr;
        this.pageLoading = true;
        axios.get('/api/crm/goodsOrder/shipBoxAndIme',{params:{id:this.inputForm.id,boxImeStr:boxImeStr,imeStr:imeStr}}).then((response) => {
          if(response.data.errors){
            this.error=response.data.errors.id.message
            this.alertError=true;
            this.submitDisabled = false;
          }else{
            this.inputForm.goodsOrderDetailList=response.data.goodsOrderDetailList;
            this.pageLoading = false;
            this.alertError=false;
          }
        })
      }
    },created(){
      axios.get('/api/ws/future/crm/goodsOrderShip/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
      });
    }
  }
</script>

