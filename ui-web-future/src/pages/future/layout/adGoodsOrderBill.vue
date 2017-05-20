<template>
  <div>
    <head-tab active="adGoodsOrderBill"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="10">
            <el-form-item :label="$t('adGoodsOrderBill.code')" prop="id">
              {{inputForm.id}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.billDate')" prop="billDate">
              <date-picker v-model="inputForm.billDate"></date-picker>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.outShopName')" prop="outShopId">
              {{inputForm.outShopName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.shopName')" prop="shopId">
              {{inputForm.shopName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.saleContact')" prop="contator">
              {{inputForm.employeeName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.mobilePhone')" prop="address">
              {{inputForm.employeePhone}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.summery')" prop="summery">
            </el-form-item>
            </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('adGoodsOrderBill.adStore')" prop="storeId">
              <el-select v-model="inputForm.storeId" clearable >
                <el-option v-for="item in billFormProperty.adStores" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-col :span="12">
            </el-col>
            <el-form-item :label="$t('adGoodsOrderBill.contact')" prop="contator">
              <el-input v-model="expressOrderDto.contator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.address')" prop="address">
              <el-input v-model="expressOrderDto.address"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.mobilePhone')" prop="mobilePhone">
              <el-input v-model="expressOrderDto.mobilePhone"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.expressCompany')" prop="expressCompanyId">
              <el-select v-model="expressOrderDto.expressCompanyId" clearable  >
                <el-option v-for="item in billFormProperty.expressCompanys":key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.splitBill')" prop="splitBill">
              <el-select v-model="inputForm.splitBill" clearable  >
                <el-option v-for="(value,key) in billFormProperty.bools" :key="key" :value="value" :label="key | bool2str"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.synToCloud')" prop="syn">
              <el-radio-group v-model="inputForm.syn">
                <el-radio v-for="(value,key) in billFormProperty.bools" :key="key" :label="value">{{key | bool2str}} </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.billRemarks')" prop="billRemarks">
              <el-input v-model="inputForm.billRemarks"></el-input>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adGoodsOrderBill.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-input v-model="productName" @change="searchDetail" :placeholder="$t('adGoodsOrderBill.inputTwoKey')" style="width:200px;"></el-input>
      <el-table :data="adGoodsOrderDetails" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('adGoodsOrderBill.loading')" stripe border >
        <el-table-column  prop="productCode" :label="$t('adGoodsOrderBill.code')" ></el-table-column>
        <el-table-column  prop="productName" :label="$t('adGoodsOrderBill.productName')" ></el-table-column>
        <el-table-column prop="stock" :label="$t('adGoodsOrderBill.stock')"></el-table-column>
        <el-table-column prop="confirmQty" :label="$t('adGoodsOrderBill.confirmQty')"></el-table-column>
        <el-table-column prop="billQty" :label="$t('adGoodsOrderBill.billQty')" >
          <template scope="scope">
            <input type="text" v-model="scope.row.billQty" class="el-input__inner"  @blur="getSummery()"/>
          </template>
        </el-table-column>
        <el-table-column prop="price" :label="$t('adGoodsOrderBill.price')"></el-table-column>
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
        adGoodsOrderDetails:[],
        productName:"",
        expressOrderDto:{
          id:'',
          expressCompanyId:'',
          address:'',
          contator:'',
          mobilePhone:'',
        },
        inputForm:{
          id:this.$route.query.id,
          billDate:"",
          storeId:"",
          shop:{name:""},
          outShop:{name:""},
          expressOrder:{
            expressCompanyId:'',
            contator:'',
            address:'',
            mobilePhone:''
          },
          splitBill:'',
          syn:'',
          billRemarks:'',
        },
        rules: {

        },
        billFormProperty:{},
        pageLoading:false,
        pickerDateOption:util.pickerDateOption,
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.inputForm.adGoodsOrderDetailList=this.filterAdGoodsOrderDetailList
            this.inputForm.billDate=util.formatLocalDate(this.inputForm.billDate)
            axios.post('/api/crm/adGoodsOrder/bill',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'adGoodsOrderList',query:util.getQuery("adGoodsOrderList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
       searchDetail(){
          var val=this.productName;
         var tempList=new Array();
          for(var index in this.inputForm.adGoodsOrderDetailList){
            var detail=this.inputForm.adGoodsOrderDetailList[index];
            if(util.isNotBlank(detail.billQty)){
              tempList.push(detail)
             }
          }
         for(var index in this.inputForm.adGoodsOrderDetailList){
           var detail=this.inputForm.adGoodsOrderDetailList[index];
           if((val.length>=1 && util.contains(detail.product.name,val)) && util.isBlank(detail.billQty)){
             tempList.push(detail)
           }
         }
         this.filterAdGoodsOrderDetailList = tempList;
       },getSummery(){
        let list=this.inputForm.goodsOrderDetailList;
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
      axios.get('/api/ws/future/layout/adGoodsOrder/findOne',{params:{id:this.$route.query.id}}).then((response)=> {
        this.inputForm =response.data;
        if(response.data.expressOrderDto!=null){
          this.expressOrderDto=response.data.expressOrderDto;
        }
      })
      axios.get('/api/ws/future/layout/adGoodsOrder/getForm',{params:{id:this.$route.query.id}}).then((response)=>{
        this.adGoodsOrderDetails = response.data.adGoodsOrderDetails;
      })
    }
  }
</script>
