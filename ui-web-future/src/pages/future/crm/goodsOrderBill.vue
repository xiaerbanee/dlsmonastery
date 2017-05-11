<template>
  <div>
    <head-tab active="goodsOrderBill"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderBill.store')" prop="storeId">
              <el-select v-model="inputForm.storeId" clearable filterable @change="billChange">
                <el-option v-for="item in billFormProperty.stores" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.billDate')" prop="billDate">
              <el-date-picker v-model="inputForm.billDate" align="right" :placeholder="$t('goodsOrderBill.selectDateRange')" :picker-options="pickerDateOption"></el-date-picker>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.expressCompany')" prop="expressCompanyId">
              <el-select v-model="inputForm.expressOrder.expressCompanyId" clearable  >
                <el-option v-for="item in billFormProperty.expressCompanys" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.synToCloud')" prop="syn">
              <el-radio-group v-model="inputForm.syn" @change="billChange">
                <el-radio v-for="(value,key) in billFormProperty.bools" :key="key" :label="value">{{key | bool2str}} </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.goodsOrderRemarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.contact')" prop="contator">
              <el-input v-model="inputForm.expressOrder.contator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.address')" prop="address">
              <el-input v-model="inputForm.expressOrder.address"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.mobilePhone')" prop="mobilePhone">
              <el-input v-model="inputForm.expressOrder.mobilePhone"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shouldGet')" prop="remarks" >
              {{billForm.shop.extendMap.shouldGet}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopRemarks')" prop="remarks">
              {{billForm.shop.remarks}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.credit')" prop="title">
              {{billForm.shop.credit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.goodsDeposit')" prop="title">
              {{billForm.shop.goodsDeposit}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderBill.shipType')" prop="createdDate">
              {{billForm.shipType}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.carrierCodes')" prop="id">
              {{billForm.carrierCodes}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.carrierDetails')" prop="title">
              {{billForm.carrierDetails}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.formatId')" prop="remarks">
              {{billForm.formatId}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.areaName')" prop="title">
              {{billForm.shop.area.name}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopName')" prop="title">
              {{billForm.shop.name}} <div style="color:red;font-size:16px">{{billForm.shop.areaType}}</div>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.parentName')"  prop="remarks" v-if="billForm.shop.parent">
              {{billForm.shop.parent.name}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopCredit')" prop="title">
              {{billForm.shop.credit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.remarks')" prop="remarks">
              {{billForm.remarks}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.title')" prop="title">
              {{billForm.title}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.summery')" prop="summery">
              {{$t('goodsOrderBill.totalQty')}}:{{this.totalQty}},{{$t('goodsOrderBill.totalPrice')}}:{{this.totalPrice}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.goodsOrderRebateRule')" prop="remarks">
              {{billForm.goodsOrderRebateRule}}
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderBill.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-input v-model="productName" @change="searchDetail" :placeholder="$t('goodsOrderBill.selectTowKey')" style="width:200px;"></el-input>
      <el-table :data="filterGoodsOrderDetailList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('goodsOrderBill.loading')" stripe border>
        <el-table-column  prop="product.name" :label="$t('goodsOrderBill.productName')" sortable width="200"></el-table-column>
        <el-table-column prop="areaQty" sortable :label="$t('goodsOrderBill.areaQty')"></el-table-column>
        <el-table-column prop="storeQty" :label="$t('goodsOrderBill.stock')"></el-table-column>
        <el-table-column prop="allowBill" :label="$t('goodsOrderBill.allowBill')">
          <template scope="scope">
            <el-tag :type="scope.row.product.allowBill? 'primary' : 'danger'">{{scope.row.product.allowBill | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="qty" :label="$t('goodsOrderBill.qty')"></el-table-column>
        <el-table-column prop="billQty" :label="$t('goodsOrderBill.billQty')" >
          <template scope="scope">
            <input type="text" v-model="scope.row.billQty" @change="getBill()" class="el-input__inner"/>
          </template>
        </el-table-column>
        <el-table-column prop="price" :label="$t('goodsOrderBill.price')"></el-table-column>
        <el-table-column prop="hasIme" :label="$t('goodsOrderBill.hasIme')" >
          <template scope="scope">
            <el-tag :type="scope.row.product.hasIme ? 'primary' : 'danger'">{{scope.row.product.hasIme | bool2str}}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        productName:"",
        filterGoodsOrderDetailList:[],
        inputForm:{
          id:this.$route.query.id,
          storeId:"",
          billDate: util.currentDate(),
          shop:{
            name:'',
            goodsDeposit:"",
            credit:"",
            remarks:"",
            type:'',
          },
          remarks:"",
          expressOrder:{
          expressCompanyId:"",
          contator:"",
          address:"",
          mobilePhone:"",
          },
          syn:"1",
          goodsOrderDetailList:[],
          summary:""
        },
        billForm:{
            shop:{
              extendMap:{shouldGet:" "},
              area:{name:""},
              parent:{name:""},
            }
        },
        rules: {},
        billFormProperty:{},
        pageLoading:false,
        pickerDateOption:util.pickerDateOption,
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
          if(this.billFormProperty.proxys.indexOf(this.inputForm.shop.type)==-1){
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
          var val=this.productName;
         var tempList=new Array();
          for(var index in this.inputForm.goodsOrderDetailList){
            var detail=this.inputForm.goodsOrderDetailList[index];
            if(util.isNotBlank(detail.billQty)){
              tempList.push(detail)
             }
          }
         for(var index in this.inputForm.goodsOrderDetailList){
           var detail=this.inputForm.goodsOrderDetailList[index];
           if((val.length>=1 && util.contains(detail.product.name,val)) && util.isBlank(detail.billQty)){
             tempList.push(detail)
           }
         }
         this.filterGoodsOrderDetailList = tempList;
       },getFormProperty(){
      axios.get('/api/crm/goodsOrder/getBillFormProperty',{params: {id:this.$route.query.id}}).then((response)=>{
        this.billFormProperty=response.data;
        this.billForm.goodsOrderRebateRule=response.data.goodsOrderRebateRule.name;
      });
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
      axios.get('/api/crm/goodsOrder/bill',{params: {id:this.$route.query.id}}).then((response)=>{
        util.copyValue(response.data,this.inputForm);
        this.billForm=response.data;
        if(response.data.goodsOrderDetailList){
          for(var index in response.data.goodsOrderDetailList) {
            response.data.goodsOrderDetailList[index].billQty = response.data.goodsOrderDetailList[index].qty;
          }
          this.inputForm.goodsOrderDetailList=response.data.goodsOrderDetailList;
          this.filterGoodsOrderDetailList=response.data.goodsOrderDetailList;
          this.getBill();
        }
        if(response.data.store){
          this.billFormProperty.stores=new Array(response.data.store);
          this.inputForm.storeId=response.data.store.id;
        }
        if(response.data.expressOrder){
          this.billFormProperty.expressCompanys=new Array(response.data.expressOrder.expressCompanyId);
          this.inputForm.expressCompanyId=response.data.expressOrder.expressCompanyId;
        }
        this.getFormProperty();
      })
    }
  }
</script>
