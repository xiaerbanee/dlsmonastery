<template>
  <div>
    <head-tab active="goodsOrderBill"></head-tab>
    <div>
      <el-form :model="submitData" ref="inputForm" :rules="rules" label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderBill.store')" prop="storeId">
              <el-select v-model="inputForm.storeId" clearable filterable @change="storeChange">
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
              <bool-radio-group v-model="inputForm.syn"  ></bool-radio-group>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shipType')" >
              {{inputForm.shipType}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.goodsOrderRemarks')" prop="remarks">
              <el-input type="textarea" v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.contact')" prop="contator">
              <el-input v-model="inputForm.contator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.address')" prop="address">
              <el-input type="textarea" v-model="inputForm.address"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.mobilePhone')" prop="mobilePhone">
              <el-input v-model="inputForm.mobilePhone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderBill.areaName')" >
              {{shop.areaName}}
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
              {{shouldGet}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopRemarks')" prop="remarks">
              {{shop.remarks}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.credit')" prop="title">
              {{shop.credit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.summary')" prop="summary">
                {{summary}}
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderBill.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-input v-model="filterValue" @change="filterProducts" :placeholder="$t('goodsOrderBill.selectTowKey')" style="width:200px;"></el-input>
      <el-table :data="filterDetailList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('goodsOrderBill.loading')" stripe border>
        <el-table-column  prop="productName" :label="$t('goodsOrderBill.productName')" sortable width="200"></el-table-column>
        <el-table-column prop="areaQty" sortable :label="$t('goodsOrderBill.areaBillQty')"></el-table-column>
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
            <input type="text" v-model="scope.row.price" @change="initSummary()" class="el-input__inner"/>
          </template>
        </el-table-column>
        <el-table-column prop="hasIme" :label="$t('goodsOrderBill.hasIme')" >
          <template scope="scope">
            <el-tag :type="scope.row.hasIme ? 'primary' : 'danger'">{{scope.row.hasIme | bool2str}}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  import boolRadioGroup from 'components/common/bool-radio-group'
  export default{
    components:{
      boolRadioGroup
    },
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        filterValue:"",
        filterDetailList:[],
        inputForm:{},
        shop:{},
        shouldGet:null,
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
        summary:"",
        rules: {},
        pageLoading:false
      }
    },
    methods:{
      formSubmit(){

      },filterProducts(){
        let val=this.filterValue;
        if(util.isBlank(val)) {
          this.filterDetailList = this.inputForm.goodsOrderBillDetailFormList;
          return;
        }
        let tempList=[];
        for(let goodsOrderDetail of this.goodsOrderDetailList){
          if(util.isNotBlank(goodsOrderDetail.qty)){
            tempList.push(goodsOrderDetail);
          }
        }
        for(let goodsOrderDetail of this.goodsOrderDetailList){
          if(util.contains(goodsOrderDetail.productName, val) && util.isBlank(goodsOrderDetail.qty)){
            tempList.push(goodsOrderDetail);
          }
        }
        this.filterDetailList = tempList;
      },initSummary(){

      },storeChange(){

      }
    },created(){
      axios.get('/api/ws/future/crm/goodsOrder/getBillForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
        this.filterProducts()
        axios.get('/api/ws/future/basic/depot/findOne',{params: {id:this.inputForm.shopId}}).then((response)=>{
          this.shop = response.data;
        });
      });
    }
  }
</script>
