<template>
  <div>
    <head-tab active="goodsOrderShip"></head-tab>
    <el-row v-if="alertError">
      <el-col :span="24">
        <el-alert v-html="error" title="" type="error" :closable="true"></el-alert>
      </el-col>
    </el-row>
    <div>
      <el-form :model="shipForm" ref="inputForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderShip.businessId')" prop="businessId">
              <el-input v-model="inputForm.businessId"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.storeName')" prop="storeId">
              {{shipForm.store.name}}
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
              {{shipForm.shop.name}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.remarks')" prop="remarks">
              {{shipForm.remarks}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderShip.redictView')" prop="redictView">
              <el-radio-group v-model="inputForm.redictView">
                <el-radio v-for="(value,key) in formProperty.bools" :key="key" :label="value">{{key | bool2str}} </el-radio>
              </el-radio-group>
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
        <el-table :data="inputForm.goodsOrderDetailList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('goodsOrderShip.loading')" stripe border >
          <el-table-column  prop="product.name" :label="$t('goodsOrderShip.productName')" sortable width="200"></el-table-column>
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
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        alertError:false,
        error:"",
        formProperty:{},
        shipForm:{
          store:{name:''},
          shop:{name:''},
          remarks:'',
        },
        inputForm:{
          id:this.$route.query.id,
          businessId:'',
          boxImeStr:'',
          expressCodes:'',
          redictView:'',
          imeStr:'',
          shipRemarks:'',
          goodsOrderDetailList:[]
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
                    if(this.isCreate){
                    form.resetFields();
                    this.submitDisabled = false;
                    } else {
                    this.$router.push({name:'goodsOrderList',query:util.getQuery("goodsOrderList")})
                    }
                }
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },shipBoxAndIme(){
        setTimeout(() => {
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
        }, 500);

      }, findOne(){
        axios.get('/api/crm/goodsOrder/ship',{params: {id:this.$route.query.id}}).then((response)=>{
          if(response.data.store){
            this.shipForm.store.name = response.data.store.name;
          }
          this.shipForm.shop.name=response.data.shop.name;
          this.shipForm.remarks=response.data.remarks;
          this.inputForm.businessId=response.data.businessId;
          this.inputForm.goodsOrderDetailList=response.data.goodsOrderDetailList;
        })
      },getFormProperty(){
        axios.get('/api/crm/goodsOrder/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
      }
    },created(){
      this.findOne();
      this.getFormProperty();
    }
  }
</script>

