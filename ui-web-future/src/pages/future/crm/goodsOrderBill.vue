<template>
  <div>
    <head-tab active="goodsOrderBill"></head-tab>
    <div>
      <el-form :model="submitData" ref="inputForm" :rules="rules" label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderBill.store')" prop="storeId">
              <el-select v-model="goodsOrder.storeId" clearable filterable @change="storeChanged">
                <el-option v-for="item in inputProperty.storeList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.billDate')" prop="billDate">
              <date-picker v-model="goodsOrder.billDate"  ></date-picker>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.expressCompany')" prop="expressCompanyId">
              <el-select v-model="expressOrder.expressCompanyId" clearable  >
                <el-option v-for="item in inputProperty.expressCompanyList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.synToCloud')" prop="syn">
              <bool-radio-group v-model="syn"  ></bool-radio-group>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shipType')" >
              {{goodsOrder.shipType}}
            </el-form-item>

            <el-form-item :label="$t('goodsOrderBill.goodsOrderRemarks')" prop="remarks">
              <el-input type="textarea" v-model="goodsOrder.remarks"></el-input>
            </el-form-item>

            <el-form-item :label="$t('goodsOrderBill.formatId')" >
              {{goodsOrder.formatId}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.contact')" prop="contator">
              <el-input v-model="expressOrder.contator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.address')" prop="address">
              <el-input type="textarea" v-model="expressOrder.address"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.mobilePhone')" prop="mobilePhone">
              <el-input v-model="expressOrder.mobilePhone"></el-input>
            </el-form-item>

          </el-col>
          <el-col :span="12">

            <el-form-item :label="$t('goodsOrderBill.areaName')" >
              {{goodsOrder.shopAreaName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopName')" >
              {{shop.name}} <div style="color:red;font-size:16px">{{shop.areaType}}</div>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.parentName')"  >
              {{shop.clientName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopCredit')" prop="title">
              {{shop.credit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shouldGet')" prop="remarks" >
              {{goodsOrder.shopShouldGet}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopRemarks')" prop="remarks">
              {{shop.remarks}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.credit')" prop="title">
              {{shop.credit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.goodsDeposit')" prop="title">
              {{goodsOrder.shopGoodsDeposit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.costGoodsDeposit')" prop="remarks">
              {{summary.totalProductDeposit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.leftGoodsDeposit')" prop="title">
              {{summary.leftDeposit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.summary')" prop="summary">
              {{$t('goodsOrderBill.totalQty')}}:{{summary.totalQty}},{{$t('goodsOrderBill.totalPrice')}}:{{summary.totalPrice}},{{$t('goodsOrderBill.totalProduct')}}:{{summary.totalProduct}}
              ,{{$t('goodsOrderBill.totalProductPrice')}}:{{summary.totalProductPrice}},{{$t('goodsOrderBill.totalProductDeposit')}}:{{summary.totalProductDeposit}}
              ,{{$t('goodsOrderBill.shopShouldGetAfterBill')}}:{{summary.shopShouldGetAfterBill}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.goodsOrderRebateRule')" >
              {{goodsOrder.goodsOrderRebateRuleRemarks}}
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
        <el-table-column prop="price" :label="$t('goodsOrderBill.price')">
          <template scope="scope">
            <input type="text" v-model="scope.row.price" @change="refreshSummary()" class="el-input__inner"/>
          </template>

        </el-table-column>
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
        inputProperty:{},
        goodsOrder:{},
        shop:{},
        expressOrder:{},
        goodsOrderDetailList:[],
        syn:true,
        submitData:{
          id:'',
          storeId:"",
          billDate: '',
          expressCompanyId:"",
          expressContator:"",
          expressAddress:"",
          expressMobilePhone:"",
          syn:'',
          remarks:"",
          goodsOrderDetailList:[],
        },
        summary:{},
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
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.initSubmitDataBeforeSubmit();
            axios.post('/api/ws/future/crm/goodsOrder/bill',qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
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
      },searchDetail(){
        let val=this.productName;
        let tempList=[];
        for(let goodsOrderDetail of this.goodsOrderDetailList){

          if(util.isNotBlank(goodsOrderDetail.billQty) || util.isNotBlank(goodsOrderDetail.qty)){
            tempList.push(goodsOrderDetail)
          }
        }
        for(let goodsOrderDetail of this.goodsOrderDetailList){
          if(util.contains(goodsOrderDetail.productName,val) && util.isBlank(goodsOrderDetail.billQty) && util.isBlank(goodsOrderDetail.qty)){
            tempList.push(goodsOrderDetail);
          }
        }
        this.filterGoodsOrderDetailList = tempList;
      },refreshSummary(){

        let isDepotStore = true;
        if(this.inputProperty.notDepotStoreIdList && this.submitData.storeId){
          for(let notDepotStoreId of this.inputProperty.notDepotStoreIdList){
            if(notDepotStoreId === this.submitData.storeId){
              isDepotStore = false;
              break;
            }
          }
        }

        let totalQty=0;
        let totalPrice = 0;
        let totalProductQty = 0;
        let totalProductPrice = 0;
        let totalProductDeposit=0;

        for(let goodsOrderDetail of this.filterGoodsOrderDetailList){
          if(goodsOrderDetail.billQty){
            let goodsOrderDetailBillQty = parseInt(goodsOrderDetail.billQty);
            let goodsOrderDetailPrice = parseFloat(goodsOrderDetail.price);

            totalQty += goodsOrderDetailBillQty;
            totalPrice += goodsOrderDetailBillQty * goodsOrderDetailPrice;
            if(goodsOrderDetail.hasIme){
              totalProductQty += goodsOrderDetailBillQty;
              totalProductPrice +=goodsOrderDetailBillQty * goodsOrderDetailPrice;
              if(isDepotStore){
                totalProductDeposit += goodsOrderDetailBillQty*parseFloat(goodsOrderDetail.productDepositPrice);
              }
            }
          }
        }
        if(totalProductDeposit>this.goodsOrder.shopGoodsDeposit){
          totalProductDeposit=this.goodsOrder.shopGoodsDeposit;
        }

        this.summary={
            totalQty :totalQty,
            shopShouldGetAfterBill:this.goodsOrder.shopShouldGet +  (totalPrice-totalProductDeposit),
            totalProductQty:totalProductQty,
            totalPrice:totalPrice.toFixed(2),
            totalProductPrice:totalProductPrice.toFixed(2),
            totalProductDeposit: totalProductDeposit.toFixed(2),
            leftDeposit : (this.goodsOrder.shopGoodsDeposit - totalProductDeposit).toFixed(2),
        };

      },
      storeChanged(){
        axios.get('/api/ws/future/crm/goodsOrder/findDetailListForBill',{params: {id:this.$route.query.id, storeId:this.submitData.storeId}}).then((response)=>{
          this.goodsOrderDetailList = response.data;
          this.searchDetail();
          this.refreshSummary();
        });


      },initSubmitDataBeforeSubmit(){
          this.submitData.id = this.goodsOrder.id;
          this.submitData.storeId = this.goodsOrder.storeId;
          this.submitData.expressCompanyId = this.expressOrder.expressCompanyId;
          this.submitData.expressContator = this.expressOrder.contator;
          this.submitData.expressAddress = this.expressOrder.address;
          this.submitData.expressMobilePhone = this.expressOrder.mobilePhone;
          this.submitData.syn = this.syn;
          this.submitData.remarks = this.goodsOrder.remarks;

          let tempList=[];
          for(let goodsOrderDetail of this.goodsOrderDetailList){

            if(util.isNotBlank(goodsOrderDetail.id) || util.isNotBlank(goodsOrderDetail.qty) || util.isNotBlank(goodsOrderDetail.billQty)){
              tempList.push(goodsOrderDetail);
            }
          }
          this.submitData.goodsOrderDetailList = tempList;
      }
    },created(){
      axios.get('/api/ws/future/crm/goodsOrder/getBillForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputProperty = response.data;
      });
      axios.get('/api/ws/future/crm/goodsOrder/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
        this.goodsOrder = response.data;
      });
      axios.get('/api/ws/future/basic/depot/findShopByGoodsOrderId',{params: {goodsOrderId:this.$route.query.id}}).then((response)=>{
        this.shop = response.data;
      });
      axios.get('/api/ws/future/crm/expressOrder/findByGoodsOrderId',{params: {goodsOrderId:this.$route.query.id}}).then((response)=>{
        this.expressOrder = response.data;
      });
      axios.get('/api/ws/future/crm/goodsOrder/findDetailListForBill',{params: {id:this.$route.query.id}}).then((response)=>{
        this.goodsOrderDetailList = response.data;
        this.searchDetail();
      });
    }
  }
</script>
