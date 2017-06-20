<template>
  <div>
    <head-tab active="adGoodsOrderForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="8">
            <el-form-item :label="$t('adGoodsOrderForm.outShopId')" prop="outShopId">
              <depot-select v-model="inputForm.outShopId" category="adShop" @input="shopChange"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.shopId')" prop="shopId" v-if="isAdShop">
              <depot-select v-model="inputForm.shopId" category="delegateShop"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.employeeName')" prop="employeeId">
              <employee-select v-model="inputForm.employeeId" ></employee-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.expressCompany')" prop="expressCompanyId">
              <express-company-select v-model="inputForm.expressCompanyId"></express-company-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.address')" prop="expressOrderAddress">
              <el-input v-model="inputForm.expressOrderAddress" type="textarea"></el-input>
            </el-form-item>
            </el-col>
            <el-col :span="8">
            <el-form-item :label="$t('adGoodsOrderForm.contact')" prop="expressContator">
              <el-input v-model="inputForm.expressOrderContator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.mobilePhone')" prop="mobilePhone">
              <el-input v-model="inputForm.expressOrderMobilePhone"></el-input>
            </el-form-item>
              <el-form-item :label="$t('adGoodsOrderForm.remarks')" prop="remarks">
                <el-input v-model="inputForm.remarks" type="textarea"></el-input>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderForm.summery')" prop="summery">
                <div style="color:red" >{{$t('adGoodsOrderForm.totalQty')}}：{{totalQty}},{{$t('adGoodsOrderForm.totalAmount')}}：{{totalPrice}}</div>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adGoodsOrderForm.save')}}</el-button>
              </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-input v-model="productName" @input="searchDetail" :placeholder="$t('adGoodsOrderForm.inputTwoKey')" style="width:200px;"></el-input>
      <el-table :data="filterAdGoodsOrderDetailList" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('adGoodsOrderForm.loading')" stripe border >
        <el-table-column  prop="productCode" :label="$t('adGoodsOrderForm.code')" ></el-table-column>
        <el-table-column prop="qty" :label="$t('adGoodsOrderForm.qty')">
          <template scope="scope">
            <el-input  v-model="scope.row.qty" @input="refreshSummary()"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="productName" :label="$t('adGoodsOrderForm.productName')"></el-table-column>
        <el-table-column prop="productPrice2" :label="$t('adGoodsOrderForm.price')"></el-table-column>
        <el-table-column prop="productRemarks" :label="$t('adGoodsOrderForm.remarks')"></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  import employeeSelect from 'components/basic/employee-select';
  import depotSelect from 'components/future/depot-select';
  import expressCompanySelect from 'components/future/express-company-select';
  import productSelect from 'components/future/product-select'

  export default{
    components: {
      employeeSelect,
      depotSelect,
      expressCompanySelect,
      productSelect
    },
    data(){
      return this.getData();
    },
    methods: {
      getData(){
        return {

          isCreate: this.$route.query.id == null,
          submitDisabled: false,
          productName: "",
          filterAdGoodsOrderDetailList: [],
          isAdShop: false,
          pageLoading: false,
          inputForm: {
              extra:{},
          },
          rules: {
            outShopId: [{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
            employeeId: [{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
            expressOrderAddress: [{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
            expressOrderContator: [{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
            expressOrderMobilePhone: [{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}]
          },
          totalQty: '',
          totalPrice: ''
        }
      },
      formSubmit(){

        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            let submitData = util.deleteExtra(this.inputForm);
            submitData.adGoodsOrderDetailList = this.getDetailListForSubmit();
            axios.post('/api/ws/future/layout/adGoodsOrder/save', qs.stringify(submitData, {allowDots: true})).then((response) => {
                this.$message(response.data.message);
                if(response.data.success){
                  if(this.isCreate){
                    Object.assign(this.$data, this.getData());
                    this.initPage();
                  }else{
                    this.submitDisabled = false;
                    this.$router.push({name: 'adGoodsOrderList', query: util.getQuery("adGoodsOrderList")})
                  }
                }
            }).catch(() => {
              this.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
      },
      shopChange(){
        axios.get('/api/ws/future/basic/depot/findByIds' + '?idStr=' + this.inputForm.outShopId).then((response) => {
          if (response.data.jointType === '代理') {
            this.isAdShop = true;
          }
        })
      }, searchDetail(){
        let val = this.productName;
        if(!val){
          this.filterAdGoodsOrderDetailList = this.inputForm.adGoodsOrderDetailList;
          return;
        }
        let tempList = [];
        for (let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList) {
          if (util.isNotBlank(adGoodsOrderDetail.qty)) {
            tempList.push(adGoodsOrderDetail)
          }
        }
        for (let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList) {
          if (util.contains(adGoodsOrderDetail.productName, val) && util.isBlank(adGoodsOrderDetail.qty)) {
            tempList.push(adGoodsOrderDetail)
          }
        }
        this.filterAdGoodsOrderDetailList = tempList;
      }, refreshSummary(){
        let totalQty = 0;
        let totalPrice = 0;
        for (let adGoodsOrderDetail of  this.inputForm.adGoodsOrderDetailList) {
          if (adGoodsOrderDetail.qty && adGoodsOrderDetail.productPrice2) {
            totalQty = totalQty + parseInt(adGoodsOrderDetail.qty);
            totalPrice = totalPrice + parseInt(adGoodsOrderDetail.qty) * parseInt(adGoodsOrderDetail.productPrice2);
          }
        }
        this.totalQty = totalQty;
        this.totalPrice = totalPrice;
      }, initPage(){

        axios.get('/api/ws/future/layout/adGoodsOrder/getForm').then((response) => {
          this.inputForm = response.data;
          axios.get('/api/ws/future/layout/adGoodsOrder/findDetailListForNewOrEdit', {params: {id: this.$route.query.id, includeNotAllowOrderProduct: util.isPermit("crm:adGoodsOrder:bill")}}).then((response) => {
            this.setAdGoodsOrderDetailList(response.data);
          });
          if(!this.isCreate){
            axios.get('/api/ws/future/layout/adGoodsOrder/findDto', {params: {id: this.$route.query.id}}).then((response) => {
              util.copyValue(response.data, this.inputForm);
            });
          }
        });
      },setAdGoodsOrderDetailList(list){
        this.inputForm.adGoodsOrderDetailList = list;
        this.searchDetail();
        this.refreshSummary();
      },getDetailListForSubmit(){
        let tempList = [];
        for (let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList) {
          if (util.isNotBlank(adGoodsOrderDetail.id) || util.isNotBlank(adGoodsOrderDetail.qty)) {
            tempList.push(adGoodsOrderDetail)
          }
        }
        return tempList;
      }
    }, created(){
      this.initPage();
    }
  }
</script>
