<template>
  <div>
    <head-tab active="goodsOrderBill"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderBill.store')" prop="storeId">
              <el-select v-model="inputForm.storeDto" clearable filterable @change="billChange">
                <el-option v-for="item in inputForm.storeList" :key="item.id" :label="item.name" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.billDate')" prop="billDate">
              <date-picker v-model="inputForm.billDate"  ></date-picker>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.expressCompany')" prop="expressCompanyId">
              <el-select v-model="inputForm.expressOrderDto.expressCompanyId" clearable  >
                <el-option v-for="item in inputForm.expressCompanyList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.synToCloud')" prop="syn">
             <boolRadioGroup v-model="inputForm.syn" @input="billChange" ></boolRadioGroup>
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
              <el-input v-model="inputForm.expressOrderDto.contator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.address')" prop="address">
              <el-input v-model="inputForm.expressOrderDto.address"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.mobilePhone')" prop="mobilePhone">
              <el-input v-model="inputForm.expressOrderDto.mobilePhone"></el-input>
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
            <input type="text" v-model="scope.row.billQty" @change="getBill()" class="el-input__inner"/>
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
        inputForm:{},
        submitData:{
          id:'',
          storeId:"",
          billDate: '',
          expressCompanyId:"",
          contator:"",
          address:"",
          mobilePhone:"",
          syn:"1",
          remarks:"",
          goodsOrderDetailList:[],
        },

        rules: {},

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
             this.inputForm.billDate=util.formatLocalDate(this.inputForm.billDate)
              this.inputForm.goodsOrderDetailList=this.filterGoodsOrderDetailList
              axios.post('/api/crm/goodsOrder/bill',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
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
        }, billChange(){
          if(this.inputForm.proxys.indexOf(this.inputForm.shop.type)==-1){
           axios.get('/api/crm/goodsOrder/billChange',{params: {id:this.$route.query.id,storeId:this.inputForm.storeId,syn:this.inputForm.syn}}).then((response)=>{
             for(let index in response.data.goodsOrderDetailList) {
              response.data.goodsOrderDetailList[index].billQty = response.data.goodsOrderDetailList[index].qty;
            }
              this.inputForm.goodsOrderDetailList=response.data.goodsOrderDetailList;
              this.filterGoodsOrderDetailList=response.data.goodsOrderDetailList;
              this.getBill();
            });
          }
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
       },getBill(){
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
      }
    },created(){
      axios.get('/api/ws/future/crm/goodsOrder/getBillForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
        this.searchDetail();
      })

    }
  }
</script>
