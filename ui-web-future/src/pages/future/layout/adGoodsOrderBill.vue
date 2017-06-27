<template>
  <div>
    <head-tab active="adGoodsOrderBill"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="10">
            <el-form-item :label="$t('adGoodsOrderBill.code')">
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
              总开单数{{totalBillQty}}，总开单金额{{totalBillPrice.toFixed(2)}}，形象押金金额 ：{{imageDeposit.toFixed(2)}} 门店应收：{{depotShouldGet.toFixed(2)}} 开单后应收 ：{{(depotShouldGet+totalBillPrice).toFixed(2)}}
            </el-form-item>
            </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('adGoodsOrderBill.adStore')" prop="storeId">
              <el-select v-model="inputForm.storeId" clearable filterable @input="refreshCloudQty">
                <el-option v-for="item in inputForm.extra.adStoreList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-col :span="12">
            </el-col>
            <el-form-item :label="$t('adGoodsOrderBill.contact')" prop="expressOrderContator">
              <el-input v-model="inputForm.expressOrderContator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.address')" prop="expressOrderAddress">
              <el-input v-model="inputForm.expressOrderAddress"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.mobilePhone')" prop="expressOrderMobilePhone">
              <el-input v-model="inputForm.expressOrderMobilePhone"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderBill.expressCompany')" prop="expressOrderExpressCompanyId">
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
      <el-table :data="filterAdGoodsOrderDetailList" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('adGoodsOrderBill.loading')"  stripe border >
        <el-table-column prop="productCode" :label="$t('adGoodsOrderBill.code')" ></el-table-column>
        <el-table-column prop="productName" :label="$t('adGoodsOrderBill.productName')" ></el-table-column>
        <el-table-column prop="cloudQty" :label="$t('adGoodsOrderBill.stock')"></el-table-column>
        <el-table-column prop="confirmQty" :label="$t('adGoodsOrderBill.confirmQty')"></el-table-column>
        <el-table-column prop="billQty" :label="$t('adGoodsOrderBill.billQty')" >
          <template scope="scope">
            <el-input v-model.number="scope.row.billQty" @input="refreshSummary()"></el-input>
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
          billDate: [{required: true, message: this.$t('adGoodsOrderBill.prerequisiteMessage')}],
          storeId: [{required: true, message: this.$t('adGoodsOrderBill.prerequisiteMessage')}],
          expressOrderContator: [{required: true, message: this.$t('adGoodsOrderBill.prerequisiteMessage')}],
          expressOrderAddress: [{required: true, message: this.$t('adGoodsOrderBill.prerequisiteMessage')}],
          expressOrderMobilePhone: [{required: true, message: this.$t('adGoodsOrderBill.prerequisiteMessage')}],
          expressOrderExpressCompanyId: [{required: true, message: this.$t('adGoodsOrderBill.prerequisiteMessage')}],
          splitBill: [{required: true, message: this.$t('adGoodsOrderBill.prerequisiteMessage')}],
          syn: [{required: true, message: this.$t('adGoodsOrderBill.prerequisiteMessage')}],
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
        let validateMsg = this.customValidate();
        if(util.isNotBlank(validateMsg)){
          this.$alert(validateMsg);
          return;
        }

        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            let submitData = util.deleteExtra(this.inputForm);
            submitData.adGoodsOrderDetailList = this.getDetailListForSubmit();

            axios.post('/api/ws/future/layout/adGoodsOrder/bill',qs.stringify(submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              this.submitDisabled = false;
              if(response.data.success){
                this.$router.push({name:'adGoodsOrderList',query:util.getQuery("adGoodsOrderList"), params:{_closeFrom:true}})
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
        let totalBillQty=0;
        let totalBillPrice=0;
        for(let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList){
          if(Number.isInteger(adGoodsOrderDetail.billQty)){
            totalBillQty=totalBillQty+adGoodsOrderDetail.billQty;
            totalBillPrice=totalBillPrice+adGoodsOrderDetail.billQty * (adGoodsOrderDetail.productPrice2*1);
          }
        }
        this.totalBillQty=totalBillQty;
        this.totalBillPrice=totalBillPrice;
      },getDetailListForSubmit(){
        let tempList = [];
        for (let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList) {
          if (util.isNotBlank(adGoodsOrderDetail.id) || (Number.isInteger(adGoodsOrderDetail.billQty) && adGoodsOrderDetail.billQty > 0)) {
            tempList.push(adGoodsOrderDetail)
          }
        }

        return tempList;
      },customValidate(){
        let totalBillQty = 0;
        for(let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList){
          if(util.isBlank(adGoodsOrderDetail.billQty)){
            continue;
          }

          if(!Number.isInteger(adGoodsOrderDetail.billQty) || adGoodsOrderDetail.billQty < 0){
            return '产品：'+adGoodsOrderDetail.productName+'的开单数不是一个大于等于0的整数';
          }

          totalBillQty += adGoodsOrderDetail.billQty;
        }
        if(totalBillQty<=0){
          return "总开单数要大于0";
        }

        return null;
      },refreshCloudQty(){
          if(util.isBlank(this.inputForm.storeId)){
            for(let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList){
              adGoodsOrderDetail.cloudQty = null;
            }
            return;
          }
        axios.get('/api/ws/future/basic/depot/getCloudQtyMap', {params: {storeId: this.inputForm.storeId}}).then((response) => {
          for(let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList){
            if(response.data[adGoodsOrderDetail.productOutId] !== undefined) {
              adGoodsOrderDetail.cloudQty = response.data[adGoodsOrderDetail.productOutId];
            }
          }
        });

      }
    },created(){
      axios.get('/api/ws/future/layout/adGoodsOrder/getBillForm').then((response)=>{
        this.inputForm = response.data;

        axios.get('/api/ws/future/layout/adGoodsOrder/findDetailListForBill', {params: {adGoodsOrderId: this.$route.query.id}}).then((response) => {
          this.setAdGoodsOrderDetailList(response.data ? response.data : [] );
        });

        axios.get('/api/ws/future/layout/adGoodsOrder/findDto',{params:{id:this.$route.query.id}}).then((response)=> {
          this.adGoodsOrder =response.data;
          this.inputForm.id = this.adGoodsOrder.id;
          this.inputForm.billDate = this.adGoodsOrder.billDate;
          if(!this.inputForm.billDate){
            this.inputForm.billDate = this.inputForm.extra.defaultBillDate;
          }
          this.inputForm.storeId = this.adGoodsOrder.storeId;
          if(util.isBlank(this.inputForm.storeId)){
            this.inputForm.storeId = this.inputForm.extra.defaultStoreId;
          }
          this.inputForm.expressOrderExpressCompanyId = this.adGoodsOrder.expressOrderExpressCompanyId;
          if(util.isBlank(this.inputForm.expressOrderExpressCompanyId)){
            this.inputForm.expressOrderExpressCompanyId = this.inputForm.extra.defaultExpressCompanyId;
          }
          this.inputForm.expressOrderContator = this.adGoodsOrder.expressOrderContator;
          this.inputForm.expressOrderAddress = this.adGoodsOrder.expressOrderAddress;
          this.inputForm.billAddress = this.adGoodsOrder.billAddress;
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
