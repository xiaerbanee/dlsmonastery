<template>
  <div>
    <head-tab :active="$t('adGoodsOrderForm.adGoodsOrderForm')"></head-tab>
    <el-row v-if="alertError">
      <el-col :span="24">
        <el-alert v-html="error" title="" type="error" :closable="true"></el-alert>
      </el-col>
    </el-row>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="8">
            <el-form-item :label="$t('adGoodsOrderForm.outShopId')" prop="outShopId">
              <el-select v-model="inputForm.outShopId" clearable filterable remote :placeholder="$t('adGoodsOrderForm.selectKeyShow20time')" :remote-method="remoteAdDepotBac" @change="outShopChange(inputForm.outShopId)" :loading="remoteLoading">
                <el-option v-for="shop in adDepotBacs" :key="shop.id" :label="shop.name" :value="shop.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.outShopId')" prop="outShopId" v-if="isAdShop">
              <el-select v-model="inputForm.adShop" clearable filterable remote :placeholder="$t('adGoodsOrderForm.selectKeyShow20time')" :remote-method="remoteAdDepot"  :loading="remoteLoading">
                <el-option v-for="shop in adShops" :key="shop.id" :label="shop.name" :value="shop.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.employeeName')" prop="employeeId">
              <el-select v-model="inputForm.employeeId" filterable remote :placeholder="$t('adGoodsOrderForm.inputWord')" :remote-method="remoteEmployee" :loading="remoteLoading" :clearable=true>
                <el-option v-for="employee in employees" :key="employee.id" :label="employee.name" :value="employee.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.expressCompany')" prop="expressCompanyId">
              <el-select v-model="inputForm.expressOrder.expressCompanyId" clearable  >
                <el-option v-for="item in formProperty.expressCompanys" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.address')" prop="address">
              <el-input v-model="inputForm.expressOrder.address"></el-input>
            </el-form-item>
            </el-col>
            <el-col :span="8">
            <el-form-item :label="$t('adGoodsOrderForm.contact')" prop="contator">
              <el-input v-model="inputForm.expressOrder.contator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.mobilePhone')" prop="mobilePhone">
              <el-input v-model="inputForm.expressOrder.mobilePhone"></el-input>
            </el-form-item>
              <el-form-item :label="$t('adGoodsOrderForm.remarks')" prop="remarks">
                <el-input v-model="inputForm.remarks" type="textarea"></el-input>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderForm.summery')" prop="summery">
                <div style="color:red" v-show="this.totalQty">{{$t('adGoodsOrderForm.totalQty')}}：{{this.totalQty}},{{$t('adGoodsOrderForm.totalAmount')}}：{{this.totalPrice}}</div>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adGoodsOrderForm.save')}}</el-button>
              </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-input v-model="productName" @change="searchDetail" :placeholder="$t('adGoodsOrderForm.inputTwoKey')" style="width:200px;"></el-input>
      <el-table :data="filterAdGoodsOrderDetailList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('adGoodsOrderForm.loading')" stripe border >
        <el-table-column  prop="product.code" :label="$t('adGoodsOrderForm.code')" ></el-table-column>
        <el-table-column prop="qty" :label="$t('adGoodsOrderForm.qty')">
          <template scope="scope">
            <el-input  v-model="scope.row.qty" @blur="getSummery()"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="product.name" :label="$t('adGoodsOrderForm.productName')"></el-table-column>
        <el-table-column prop="price" :label="$t('adGoodsOrderForm.price')"></el-table-column>
        <el-table-column prop="product.remarks" :label="$t('adGoodsOrderForm.remarks')"></el-table-column>
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
        alertError:false,
        error:"",
        productName:"",
        filterAdGoodsOrderDetailList:[],
        remoteLoading:false,
        isAdShop:false,
        pageLoading:'',
        inputForm:{
           id:this.$route.query.id,
          outShopId:'',
          employeeId:'',
          expressOrder:{
            id:"",
            expressCompanyId:'',
             address:'',
            contator:'',
            mobilePhone:'',
          },
          remarks:'',
          adGoodsOrderDetailList:[],
        },
        rules: {
          outShopId:[{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
          employeeId:[{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
          address:[{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
          contator:[{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
          mobilePhone:[{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}]
        },
        adShops:[],
        adDepotBacs:[],
        employees:[],
        rules:{},
        formProperty:{},
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
          this.inputForm.adGoodsOrderDetailList=this.filterAdGoodsOrderDetailList
            axios.post('/api/crm/adGoodsOrder/save',qs.stringify(this.inputForm,{allowDots:true})).then((response)=> {
              if(!response.data.errors){
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'adGoodsOrderList',query:util.getQuery("adGoodsOrderList")})
                }
              }else{
                 this.alertError=true;
                 this.error=response.data.errors.id.message
                 this.submitDisabled = false;
              }

            });
          }else{
            this.submitDisabled = false;
          }
        })
      },searchDetail(){
          var val=this.productName;
         var tempList=new Array();
          for(var index in this.inputForm.adGoodsOrderDetailList){
            var detail=this.inputForm.adGoodsOrderDetailList[index];
            if(util.isNotBlank(detail.qty)){
              tempList.push(detail)
             }
          }
         for(var index in this.inputForm.adGoodsOrderDetailList){
           var detail=this.inputForm.adGoodsOrderDetailList[index];
           if((val.length>=1 && util.contains(detail.product.name,val)) && util.isBlank(detail.qty)){
             tempList.push(detail)
           }
         }
         this.filterAdGoodsOrderDetailList = tempList;
       },remoteAdDepot(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/crm/depot/proxyShop', {params: {name: query}}).then((response)=> {
            this.adShops = response.data;
            this.remoteLoading = false;
          })
        } else {
          this.adShops = [];
        }
      },remoteAdDepotBac(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/crm/depot/adShopBsc',{params:{key:query}}).then((response)=>{
            this.adDepotBacs=response.data;
            this.remoteLoading = false;
          })
        }
      },remoteEmployee(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/hr/employee/search',{params:{key:query}}).then((response)=>{
            this.employees=response.data;
            this.remoteLoading = false;
          })
        }
      },outShopChange(outShopId){
        axios.get('/api/crm/adGoodsOrder/outShopChange',{params:{outShopId:outShopId,id:this.$route.query.id}}).then((response)=> {
          this.inputForm.adGoodsOrderDetailList=response.data.adGoodsOrder.adGoodsOrderDetailList
          this.getSummery()
          this.filterAdGoodsOrderDetailList=response.data.adGoodsOrder.adGoodsOrderDetailList
          this.isAdShop=response.data.adShop
        });
      },getSummery(){
      let list=this.inputForm.adGoodsOrderDetailList;
      let totalQty=0;
      let totalPrice=0;
      for(let item in list){
        if(list[item].qty){
          totalQty=totalQty+parseInt(list[item].qty);
          totalPrice=totalPrice+parseInt(list[item].qty)*parseInt(list[item].price);
        }
      }
      this.totalQty=totalQty;
      this.totalPrice=totalPrice;
    },getFormProperty(){
        axios.get('/api/crm/adGoodsOrder/getFormProperty').then((response)=> {
          this.formProperty = response.data;
        });
      },findOne(){
        axios.get('/api/crm/adGoodsOrder/findOne',{params:{id:this.$route.query.id}}).then((response)=> {
          util.copyValue(response.data,this.inputForm);
          if(response.data.outShop){
             this.shops=new Array(response.data.outShop);
          }
          if(response.data.employee){
             this.employees=new Array(response.data.employee)
          }
        });
      }
    },created(){
      this.findOne();
      this.getFormProperty();
    }
  }
</script>
