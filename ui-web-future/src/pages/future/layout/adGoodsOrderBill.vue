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
            <el-form-item :label="$t('adGoodsOrderBill.outShopName')">
              {{adGoodsOrder.outShopName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.shopName')">
              {{adGoodsOrder.shopName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.saleContact')">
              {{employee.officeName}}_{{employee.positionName}}_{{employee.name}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.mobilePhone')">
              {{employee.mobilePhone}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.summary')" >
              总开单数{{totalBillQty}}，总开单金额{{totalBillPrice}}，形象押金金额 ：{{imageDeposit}} 门店应收：{{depotShouldGet}} 开单后应收 ：{{depotShouldGet+totalBillPrice}}
            </el-form-item>
            </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('adGoodsOrderBill.adStore')" prop="storeId">
              <el-select v-model="inputForm.storeId" clearable filterable>
                <el-option v-for="item in inputForm.extra.adStoreList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-col :span="12">
            </el-col>
            <el-form-item :label="$t('adGoodsOrderBill.contact')" prop="contator">
              <el-input v-model="inputForm.expressOrderContator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.address')" prop="address">
              <el-input v-model="inputForm.expressOrderAddress"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.mobilePhone')" prop="mobilePhone">
              <el-input v-model="inputForm.expressOrderMobilePhone"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.expressCompany')" prop="expressCompanyId">
              <express-company-select v-model="inputForm.expressOrderExpressCompanyId"></express-company-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.splitBill')" prop="splitBill">
              <bool-select v-model="inputForm.splitBill"></bool-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.synToCloud')" prop="syn">
              <bool-radio-group v-model="inputForm.syn"></bool-radio-group>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.billRemarks')" prop="billRemarks">
              <el-input v-model="inputForm.billRemarks" type="textarea"></el-input>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adGoodsOrderBill.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <process-details v-model="adGoodsOrder.processInstanceId"></process-details>

      <el-input v-model="productName" @change="searchDetail" :placeholder="$t('adGoodsOrderBill.inputTwoKey')" style="width:200px;"></el-input>
      <el-table :data="filterAdGoodsOrderDetailList" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('adGoodsOrderBill.loading')" stripe border >
        <el-table-column  prop="productCode" :label="$t('adGoodsOrderBill.code')" ></el-table-column>
        <el-table-column  prop="productName" :label="$t('adGoodsOrderBill.productName')" ></el-table-column>
        <el-table-column prop="stock" :label="$t('adGoodsOrderBill.stock')"></el-table-column>
        <el-table-column prop="confirmQty" :label="$t('adGoodsOrderBill.confirmQty')"></el-table-column>
        <el-table-column prop="billQty" :label="$t('adGoodsOrderBill.billQty')" >
          <template scope="scope">
            <input type="text" v-model="scope.row.billQty" class="el-input__inner"  @input="refreshSummary()"/>
          </template>
        </el-table-column>
        <el-table-column prop="productPrice2" :label="$t('adGoodsOrderBill.price')"></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  import expressCompanySelect from 'components/future/express-company-select'
  import boolSelect from 'components/common/bool-select'
  import boolRadioGroup from 'components/common/bool-radio-group'
  import processDetails from 'components/general/process-details';

  export default{
    components:{expressCompanySelect,boolSelect,boolRadioGroup,processDetails},
    data(){
      return{
        submitDisabled:false,
        adGoodsOrder:{},
        employee:{},
        productName:"",
        filterAdGoodsOrderDetailList:[],
        inputForm:{
            extra:{},
        },
        rules: {
        },
        pageLoading:false,
        imageDeposit:0,
        depotShouldGet:0,
        totalBillQty:0,
        totalBillPrice:0,

      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            let submitData = util.deleteExtra(this.inputForm);
            submitData.adGoodsOrderDetailList = this.getDetailListForSubmit();

            axios.post('/api/ws/future/layout/adGoodsOrder/bill',qs.stringify(submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              this.submitDisabled = false;
              if(response.data.success){
                this.$router.push({name:'adGoodsOrderList',query:util.getQuery("adGoodsOrderList")})
              }
            }).catch( () => {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
       searchDetail(){
         let val = this.productName;
         if(!val){
           this.filterAdGoodsOrderDetailList = this.inputForm.adGoodsOrderDetailList;
           return;
         }
         let tempList = [];
         for (let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList) {
           if (util.isNotBlank(adGoodsOrderDetail.billQty)) {
             tempList.push(adGoodsOrderDetail)
           }
         }
         for (let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList) {
           if (util.contains(adGoodsOrderDetail.productName, val) && util.isBlank(adGoodsOrderDetail.billQty)) {
             tempList.push(adGoodsOrderDetail)
           }
         }
         this.filterAdGoodsOrderDetailList = tempList;

       },setAdGoodsOrderDetailList(list){
        this.inputForm.adGoodsOrderDetailList = list;
        this.searchDetail();
        this.refreshSummary();
       }, refreshSummary(){
        let list=this.inputForm.adGoodsOrderDetailList;
        let totalBillQty=0;
        let totalBillPrice=0;
        for(let item in list){
          if(list[item].billQty){
            totalBillQty=totalBillQty+parseInt(list[item].billQty);
            totalBillPrice=totalBillPrice+parseInt(list[item].billQty)*parseInt(list[item].productPrice2);
          }
        }
        this.totalBillQty=totalBillQty;
        this.totalBillPrice=totalBillPrice;
      },getDetailListForSubmit(){
        let tempList = [];
        for (let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList) {
          if (util.isNotBlank(adGoodsOrderDetail.id) || util.isNotBlank(adGoodsOrderDetail.billQty)) {
            tempList.push(adGoodsOrderDetail)
          }
        }

        return tempList;
      }
    },created(){

      axios.get('/api/ws/future/layout/adGoodsOrder/getBillForm').then((response)=>{
        this.inputForm = response.data;

        axios.get('/api/ws/future/layout/adGoodsOrder/findDetailListForBill', {params: {adGoodsOrderId: this.$route.query.id}}).then((response) => {
          this.setAdGoodsOrderDetailList(response.data);
        });

        axios.get('/api/ws/future/layout/adGoodsOrder/findDto',{params:{id:this.$route.query.id}}).then((response)=> {
          this.adGoodsOrder =response.data;
          this.inputForm.id = this.adGoodsOrder.id;
          this.inputForm.billDate = this.inputForm.extra.defaultBillDate;
          this.inputForm.storeId = this.inputForm.extra.defaultStoreId;
          this.inputForm.expressOrderContator = this.adGoodsOrder.expressOrderContator;
          this.inputForm.expressOrderAddress = this.adGoodsOrder.expressOrderAddress;
          this.inputForm.billAddress = this.adGoodsOrder.billAddress;
          this.inputForm.expressOrderExpressCompanyId = this.adGoodsOrder.expressOrderExpressCompanyId;
          this.inputForm.expressOrderMobilePhone = this.adGoodsOrder.expressOrderMobilePhone;
          this.inputForm.splitBill = this.adGoodsOrder.splitBill;
          this.inputForm.syn = true;
          this.inputForm.billRemarks = this.adGoodsOrder.billRemarks;

          axios.get('/api/basic/hr/employee/findOne',{params: {id:this.adGoodsOrder.employeeId}}).then((response)=>{
            this.employee = response.data;
          });
          axios.get('/api/ws/future/crm/shopDeposit/findLeftAmount',{params: {type:'形象保证金', depotId:this.adGoodsOrder.shopId}}).then((response)=>{
            this.imageDeposit = response.data;
          });

        });

      })
    }
  }
</script>
