<template>
  <div>
    <head-tab active="goodsOrderForm"></head-tab>
    <el-row v-if="alertError">
      <el-col :span="24">
        <el-alert v-html="error" title="" type="error" :closable="true"></el-alert>
      </el-col>
    </el-row>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row>
          <el-col :span="12">
        <el-form-item :label="$t('goodsOrderForm.shop')" prop="shopId">
          <depot-select :disabled="!isCreate" v-model="inputForm.shopId" category="directShop" @input="refreshForm"></depot-select>
        </el-form-item>

        <el-form-item :label="$t('goodsOrderForm.netType')" prop="netType">
          <el-select  :disabled="!isCreate" v-model="inputForm.netType"    clearable :placeholder="$t('goodsOrderForm.inputWord')" @change="refreshForm">
            <el-option v-for="item in inputForm.netTypeList" :key="item":label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('goodsOrderForm.shipType')" prop="shipType" >
          <el-select  :disabled="!isCreate" v-model="inputForm.shipType"  clearable :placeholder="$t('goodsOrderForm.inputKey')" @change="refreshForm" >
            <el-option v-for="item in inputForm.shipTypeList" :key="item":label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>

            <el-form-item v-if="inputForm.shopId&&inputForm.netType&&inputForm.shipType" :label="$t('goodsOrderForm.isUseTicket')" prop="isUseTicket">
              <bool-radio-group v-model="inputForm.isUseTicket"></bool-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="12">
          <div v-if="inputForm.shopId&&inputForm.netType&&inputForm.shipType">

            <el-form-item  :label="$t('goodsOrderForm.clientName')"  prop="clientName">
              {{inputForm.clientName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.shopType')" prop="type">
              {{inputForm.shopType}}
          </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.priceSystem')" prop="pricesystem">
              {{inputForm.priceSystemName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.remarks')" prop="remarks">
              <el-input type="textarea" v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.summary')" prop="summary">
              {{inputForm.summary}}
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderForm.save')}}</el-button>
            </el-form-item>
          </div>
          </el-col>
        </el-row>
      </el-form>
      <div v-if="inputForm.shopId&&inputForm.netType&&inputForm.shipType">
      <el-input v-model="productName" @input="filterProducts" :placeholder="$t('shopAllotForm.selectTowKey')" style="width:200px;"></el-input>
      <el-table :data="filterDetailList" border stripe v-loading="pageLoading" >
        <el-table-column  prop = "productName" :label="$t('goodsOrderForm.productName')" ></el-table-column>
        <el-table-column prop="productHasIme" :label="$t('goodsOrderForm.hasIme')" width="70">
          <template scope="scope">
            <el-tag   :type="scope.row.productHasIme ? 'primary' : 'danger'">{{scope.row.productHasIme | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="price" :label="$t('goodsOrderForm.price')" width="100"></el-table-column>
        <el-table-column prop="qty" :label="$t('goodsOrderForm.qty')">
          <template scope="scope">
            <input   type="text" v-model="scope.row.qty" class="el-input__inner"/>
          </template>
        </el-table-column>
        <el-table-column prop="productAllowOrderAndBill" :label="$t('goodsOrderForm.allowOrderAndBill')">
          <template scope="scope">
            <el-tag  :type="scope.row.productAllowOrderAndBill? 'primary' : 'danger'">{{scope.row.productAllowOrderAndBill | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="areaQty" :label="$t('goodsOrderForm.areaQty')" ></el-table-column>
      </el-table>
      </div>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  import productSelect from 'components/future/product-select'
  import boolRadioGroup from 'components/common/bool-radio-group'

  export default{
    components:{
      depotSelect,
      productSelect,
      boolRadioGroup,

    },
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        pageLoading:false,
        alertError:false,
        productName:'',
        filterDetailList:[],

        inputForm:{

        },

        submitData:{
          id:'',
          shopId:'',
          netType:'',
          shipType:'',
          isUseTicket:'',
          remarks:'',

          detailFormList:[],
        },
        rules: {
        }
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
           this.initSubmitDataBeforeSubmit();

            axios.post('/api/ws/future/crm/goodsOrder/save', qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.inputForm.create){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'goodsOrderList',query:util.getQuery("goodsOrderList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },filterProducts(){

        let val=this.productName;
        let tempList=new Array();
        for(let each of this.inputForm.detailFormList){

          if(util.isNotBlank(each.qty)){
            tempList.push(each);
          }
        }
        for(let each of this.inputForm.detailFormList){
          if(util.contains(each.productName, val) && util.isBlank(each.qty)){
            tempList.push(each);
          }
        }
        this.filterDetailList = tempList;

      }, initSubmitDataBeforeSubmit(){
        util.copyValue(this.inputForm,this.submitData);

        let tempList=new Array();
        for(let each of this.inputForm.detailFormList){
          if(util.isNotBlank(each.qty)){
            tempList.push(each);
          }
        }
        this.submitData.detailFormList = tempList;
      },refreshForm(){

        if(this.inputForm.shopId&&this.inputForm.netType&&this.inputForm.shipType) {
          this.pageLoading = true;
          axios.get('/api/ws/future/crm/goodsOrder/findForm', {params: {id:this.$route.query.id, shopId:this.inputForm.shopId, netType: this.inputForm.netType, shipType:this.inputForm.shipType}}).then((response)=>{

            this.inputForm = response.data;
            this.filterProducts();
            this.pageLoading = false;

          });
        }
      }
    }, created(){

      axios.get('/api/ws/future/crm/goodsOrder/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
        this.filterProducts();

      })
    }
  }
</script>
