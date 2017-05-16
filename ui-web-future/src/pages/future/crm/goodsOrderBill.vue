<template>
  <div>
    <head-tab active="goodsOrderBill"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderBill.store')" prop="storeId">
              <el-select v-model="inputForm.storeId" clearable filterable @change="billChange">
                <el-option v-for="item in inputForm.storeList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.billDate')" prop="billDate">
              <date-picker v-model="inputForm.billDate"  ></date-picker>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.expressCompany')" prop="expressCompanyId">
              <el-select v-model="inputForm.expressCompanyId" clearable  >
                <el-option v-for="item in inputForm.expressCompanyList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.synToCloud')" prop="syn">
             <boolRadioGroup v-model="inputForm.syn"  ></boolRadioGroup>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shipType')" >
              {{inputForm.shipType}}
            </el-form-item>

            <el-form-item :label="$t('goodsOrderBill.goodsOrderRemarks')" prop="remarks">
              <el-input type="textarea" v-model="inputForm.remarks"></el-input>
            </el-form-item>

            <el-form-item :label="$t('goodsOrderBill.formatId')" >
              {{inputForm.goodsOrderDto.formatId}}
            </el-form-item>

          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderBill.contact')" prop="contator">
              <el-input v-model="inputForm.expressContator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.address')" prop="address">
              <el-input v-model="inputForm.expressAddress"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.mobilePhone')" prop="mobilePhone">
              <el-input v-model="inputForm.expressMobilePhone"></el-input>
            </el-form-item>

            <el-form-item :label="$t('goodsOrderBill.areaName')" >
              {{inputForm.goodsOrderDto.shopAreaName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopName')" >
              {{inputForm.shopDto.name}} <div style="color:red;font-size:16px">{{inputForm.shopDto.areaType}}</div>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.parentName')"  v-if="inputForm.shopDto.clientName">
              {{inputForm.shopDto.clientName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopCredit')" prop="title">
              {{inputForm.shopDto.credit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shouldGet')" prop="remarks" >
              {{inputForm.goodsOrderDto.shopShouldGet}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopRemarks')" prop="remarks">
              {{inputForm.shopDto.remarks}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.credit')" prop="title">
              {{inputForm.shopDto.credit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.goodsDeposit')" prop="title">
              {{inputForm.goodsOrderDto.shopGoodsDeposit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.costGoodsDeposit')" prop="remarks">
              {{inputForm.goodsOrderDto.costGoodsDeposit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.leftGoodsDeposit')" prop="title">
              {{inputForm.goodsOrderDto.leftGoodsDeposit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.summary')" prop="summary">
              {{$t('goodsOrderBill.totalQty')}}:{{this.totalQty}},{{$t('goodsOrderBill.totalPrice')}}:{{this.totalPrice}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.goodsOrderRebateRule')" >
              {{inputForm.goodsOrderDto.goodsOrderRebateRuleRemarks}}
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderBill.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-input v-model="productName" @change="searchDetail" :placeholder="$t('goodsOrderBill.selectTowKey')" style="width:200px;"></el-input>
      <el-table :data="filterGoodsOrderDetailList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('goodsOrderBill.loading')" stripe border>
        <el-table-column  prop="productName" :label="$t('goodsOrderBill.productName')" sortable width="200"></el-table-column>
        <el-table-column prop="areaBillQty" sortable :label="$t('goodsOrderBill.areaBillQty')"></el-table-column>
        <el-table-column prop="storeQty" :label="$t('goodsOrderBill.stock')"></el-table-column>
        <el-table-column prop="allowBill" :label="$t('goodsOrderBill.allowBill')">
          <template scope="scope">
            <el-tag :type="scope.row.allowBill? 'primary' : 'danger'">{{scope.row.allowBill | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="qty" :label="$t('goodsOrderBill.qty')"></el-table-column>
        <el-table-column prop="billQty" :label="$t('goodsOrderBill.billQty')" >
          <template scope="scope">
            <input type="text" v-model="scope.row.billQty" @change="refreshSummary()" class="el-input__inner"/>
          </template>
        </el-table-column>
        <el-table-column prop="price" :label="$t('goodsOrderBill.price')"></el-table-column>
        <el-table-column prop="productHasIme" :label="$t('goodsOrderBill.hasIme')" >
          <template scope="scope">
            <el-tag :type="scope.row.productHasIme ? 'primary' : 'danger'">{{scope.row.productHasIme | bool2str}}</el-tag>
          </template>
        </el-table-column>
      </el-table>
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
        productName:"",
        filterGoodsOrderDetailList:[],
        inputForm:{
          goodsOrderDto:{},
          shopDto:{},
        },
        submitData:{
          id:'',
          storeId:"",
          billDate: '',
          expressCompanyId:"",
          expressContator:"",
          expressAddress:"",
          expressMobilePhone:"",
          syn:"1",
          remarks:"",
          detailFormList:[],
        },
        rules: {},
        initForm:false,
        pageLoading:false,

        totalQty:'',
        totalPrice:''
      }
    },
    methods:{
       formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
               this.initSubmitDataBeforeSubmit();
              axios.post('/api/ws/future/crm/goodsOrder/billSave',qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
               this.$message(response.data.message);
                if(response.data.success){
                  if(this.isCreate){
                      form.resetFields();
                      this.submitDisabled = false;
                    } else {
                      this.$router.push({name:'goodsOrderList',query:util.getQuery("goodsOrderList")})
                    }
                  }else{
                    this.submitDisabled = false;
                  }
              }).catch(function () {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        }, billChange(newVal){
        if(newVal=="" || newVal == this.inputForm.storeId || newVal == util.getLabel(this.inputForm.storeList,this.inputForm.storeId,"name")) {
          return;
        }
        this.refreshForm();

        },searchDetail(){
        let val=this.productName;
         let tempList=new Array();
          for(let each of this.inputForm.detailFormList){

            if(util.isNotBlank(each.billQty) || util.isNotBlank(each.qty)){
              tempList.push(each)
             }
          }
         for(let each of this.inputForm.detailFormList){
           if(util.contains(each.productName,val) && util.isBlank(each.billQty) && util.isBlank(each.qty)){
             tempList.push(each);
           }
         }
         this.filterGoodsOrderDetailList = tempList;
       },refreshSummary(){
        let list=this.filterGoodsOrderDetailList;
        let totalQty=0;
        let totalPrice=0;
        for(let item in list){
          if(list[item].billQty){
            totalQty=totalQty+parseInt(list[item].billQty);
            totalPrice=totalPrice+parseInt(list[item].billQty)*parseInt(list[item].price);
          }
        }
        this.totalQty=totalQty;
        this.totalPrice=totalPrice;
      },initSubmitDataBeforeSubmit(){

        util.copyValue(this.inputForm, this.submitData);

        let tempList=new Array();
        for(let each of this.inputForm.detailFormList){

          if(util.isNotBlank(each.qty) || util.isNotBlank(each.billQty)){
            tempList.push(each);
          }
        }
        this.submitData.detailFormList = tempList;
      },refreshForm(){
          if(this.initForm){
              return;
          }
          this.initForm = true;
        axios.get('/api/ws/future/crm/goodsOrder/getBillForm',{params: {id:this.$route.query.id, storeId:this.inputForm.storeId}}).then((response)=>{
          this.inputForm = response.data;
          this.searchDetail();
          this.refreshSummary();
          this.initForm = false;
        })
      }
    },created(){

        this.refreshForm();


    }
  }
</script>
