<template>
  <div>
    <head-tab active="adGoodsOrderForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="8">
            <el-form-item :label="$t('adGoodsOrderForm.outShopId')" prop="outShopId">
              <depot-select :disabled="afterBill" v-model="inputForm.outShopId" category="directShop" @input="outShopChanged"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.shopId')" prop="shopId" v-if="isDelegateShop">
              <depot-select :disabled="afterBill" v-model="inputForm.shopId" category="shop" @input="shopChanged"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.recentSaleQty')" >
              {{recentSaleDescription}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.imageDeposit')" >
              {{imageDeposit}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.investInCause')" prop="investInCause">
              <el-input v-model="inputForm.investInCause" type="textarea"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.employeeName')" prop="employeeId">
              <employee-select v-model="inputForm.employeeId" ></employee-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.expressCompany')" prop="expressOrderExpressCompanyId">
              <express-company-select v-model="inputForm.expressOrderExpressCompanyId"></express-company-select>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.address')" prop="expressOrderAddress">
              <el-input v-model="inputForm.expressOrderAddress" type="textarea"></el-input>
            </el-form-item>
            <div v-if="processStatus==='待签收' && hasShipPermission">
              <el-form-item :label="$t('adGoodsOrderForm.expressCodes')" prop="expressOrderExpressCodes">
                <el-input v-model="inputForm.expressOrderExpressCodes" type="textarea" :placeholder="$t('adGoodsOrderForm.enter')"></el-input>
              </el-form-item>
            </div>
            </el-col>
            <el-col :span="8">
            <el-form-item :label="$t('adGoodsOrderForm.contact')" prop="expressOrderContator">
              <el-input v-model="inputForm.expressOrderContator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderForm.mobilePhone')" prop="expressOrderMobilePhone">
              <el-input v-model="inputForm.expressOrderMobilePhone"></el-input>
            </el-form-item>
              <el-form-item :label="$t('adGoodsOrderForm.remarks')" prop="remarks">
                <el-input v-model="inputForm.remarks" type="textarea"></el-input>
              </el-form-item>
              <el-form-item :label="$t('adGoodsOrderForm.summery')">
                <div style="color:red" >{{$t('adGoodsOrderForm.totalQty')}}：{{totalQty}},{{$t('adGoodsOrderForm.totalAmount')}}：{{totalPrice.toFixed(2)}}</div>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adGoodsOrderForm.save')}}</el-button>
              </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-input v-model="productName" @input="searchDetail" :placeholder="$t('adGoodsOrderForm.inputTwoKey')" style="width:200px;"></el-input>
      <el-table :data="filterAdGoodsOrderDetailList" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('adGoodsOrderForm.loading')" stripe border >
        <el-table-column  prop="productCode" :label="$t('adGoodsOrderForm.code')" >
          <template scope="scope">
            <div class="action" v-if="scope.row.productImage !== null&&scope.row.productImage !==''" v-permit="'crm:adGoodsOrder:view'"><el-button type="text" @click="itemAction(scope.row.productImage,'showImage')">{{scope.row.productCode}}</el-button>
              <el-dialog v-model="dialogVisible" size="tiny">
                <img width="100%" :src="dialogImageUrl" alt="">
              </el-dialog>
            </div>
            <div v-if="scope.row.productImage === null||scope.row.productImage ===''" v-permit="'crm:adGoodsOrder:view'">{{scope.row.productCode}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="qty" :label="$t('adGoodsOrderForm.qty')">
          <template scope="scope">
            <div v-if="afterBill">{{scope.row.qty}}</div>
            <div v-else><el-input v-model.number="scope.row.qty" @input="refreshSummary()"></el-input></div>
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
    computed:{
      hasShipPermission: function () {
        return util.isPermit("crm:adGoodsOrder:ship");
      },
      afterBill : function(){
          return !this.isCreate && this.processStatus && this.processStatus.indexOf('审核') < 0 && this.processStatus !== '待开单';
      }
    },
    methods: {
      getData(){
        function phoneFormatter(rule, value, callback){
          if(!value){
            return callback(new Error('必填信息'));
          }else if(value.length != 11){
            return callback(new Error('请输入11位手机号(不包含空格或-)！'));
          }else if(/[^\d]/.test(value)){
            return callback(new Error('请输入纯数字手机号!'));
          }else{
            return callback();
          }
        }

        return {
          isCreate: this.$route.query.id == null,
          recentSaleDescription:'',
          imageDeposit:0,
          submitDisabled: false,
          productName: "",
          filterAdGoodsOrderDetailList: [],
          isDelegateShop: false,
          pageLoading: false,
          processStatus: '',
          inputForm: {
              extra:{},
          },
          rules: {
            outShopId: [{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
            shopId: [{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
            investInCause: [{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
            employeeId: [{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
            expressOrderExpressCompanyId: [{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
            expressOrderAddress: [{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
            expressOrderContator: [{required: true, message: this.$t('adGoodsOrderForm.prerequisiteMessage')}],
            expressOrderMobilePhone: [{required: true,validator:phoneFormatter}],
          },
          totalQty: 0,
          totalPrice: 0,
          dialogImageUrl:'',
          dialogVisible:false,
        }
      },
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
            axios.post('/api/ws/future/layout/adGoodsOrder/save', qs.stringify(submitData, {allowDots: true})).then((response) => {
                this.$message(response.data.message);
                if(response.data.success){
                  if(this.isCreate){
                    Object.assign(this.$data, this.getData());
                    this.initPage();
                  }else{
                    this.$router.push({name: 'adGoodsOrderList', query: util.getQuery("adGoodsOrderList"), params:{_closeFrom:true}})
                  }
                }
            }).catch(() => {
              this.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
      }, outShopChanged(){
          if(util.isBlank(this.inputForm.outShopId)){
            this.isDelegateShop = false;
            this.inputForm.shopId = null;
            this.recentSaleDescription='';
            this.imageDeposit =0;
            return;
          }

        axios.get('/api/ws/future/basic/depot/findByIds' + '?idStr=' + this.inputForm.outShopId).then((response) => {
              if(response.data[0].jointType === '代理'){
                this.isDelegateShop = true;
                this.inputForm.shopId = null;
                this.recentSaleDescription='';
                this.imageDeposit =0;
              }else{
                axios.get('/api/ws/future/basic/depot/findByClientId' + '?clientId=' + response.data[0].clientId).then((response) => {
                    if(response.data.length > 1){
                      this.isDelegateShop = true;
                      this.inputForm.shopId = null;
                      this.recentSaleDescription='';
                      this.imageDeposit =0;
                    }else{
                      this.isDelegateShop = false;
                      this.inputForm.shopId = this.inputForm.outShopId;
                      this.refreshRecentMonthSaleAmount();
                    }
                });
              }
          /*if (response.data[0].jointType === '代理') {
            this.isDelegateShop = true;
            this.inputForm.shopId = null;
            this.recentSaleDescription='';
            this.imageDeposit =0;
          }else{
            this.isDelegateShop = false;
            this.inputForm.shopId = this.inputForm.outShopId;
            this.refreshRecentMonthSaleAmount();
          }*/
        });
      },shopChanged(){
        this.refreshRecentMonthSaleAmount();
      },itemAction:function(productImage,action){
          if(action=="showImage"){
              axios.get('/api/general/sys/folderFile/findByIds',{params: {ids:productImage}}).then((response)=>{
                if(response.data == ""){
                  this.$message("未找到该货品的图片");
                }else{
                  this.dialogVisible = true;
                  this.dialogImageUrl = response.data[0].url;
                }
              });
          }
      },
      refreshRecentMonthSaleAmount(){
        if(util.isBlank(this.inputForm.shopId)){
          this.recentSaleDescription='';
          this.imageDeposit =0;
          return;
        }

        axios.get('/api/ws/future/basic/depot/getRecentMonthSaleAmount' , {params: {depotId: this.inputForm.shopId, monthQty:3}}).then((response) => {
          if(response.data){
            let tmp = '';
            for(let key in response.data){
              tmp = tmp + key +"销量："+  response.data[key] +"台；";
            }
            this.recentSaleDescription=tmp;
          }else{
            this.recentSaleDescription='';
          }
        });

        axios.get('/api/ws/future/layout/shopDeposit/findLeftAmount', {params: {type: '形象保证金', depotId: this.inputForm.shopId}}).then((response) => {
          this.imageDeposit = response.data;
        });
      },customValidate(){
        let totalQty = 0;
        for(let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList){
          if(util.isBlank(adGoodsOrderDetail.qty)){
            continue;
          }

          if(!Number.isInteger(adGoodsOrderDetail.qty) || adGoodsOrderDetail.qty < 0){
              return '产品：'+adGoodsOrderDetail.productName+'的订货数不是一个大于等于0的整数';
          }

          totalQty += adGoodsOrderDetail.qty;
        }
        if(totalQty<=0){
          return "总订货数要大于0";
        }
        return null;
      },searchDetail(){
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
          if ((util.contains(adGoodsOrderDetail.productName, val)||util.contains(adGoodsOrderDetail.productCode, val)) && util.isBlank(adGoodsOrderDetail.qty)) {
            tempList.push(adGoodsOrderDetail)
          }
        }
        this.filterAdGoodsOrderDetailList = tempList;
      }, refreshSummary(){
        let tmpTotalQty = 0;
        let tmpTotalPrice = 0;
        for (let adGoodsOrderDetail of  this.inputForm.adGoodsOrderDetailList) {

          if(Number.isInteger(adGoodsOrderDetail.qty)){
            tmpTotalQty = tmpTotalQty + adGoodsOrderDetail.qty;
            tmpTotalPrice = tmpTotalPrice +  adGoodsOrderDetail.qty * (adGoodsOrderDetail.productPrice2*1);
          }
        }
        this.totalQty = tmpTotalQty;
        this.totalPrice = tmpTotalPrice;
      },setAdGoodsOrderDetailList(list){
        this.inputForm.adGoodsOrderDetailList = list;
        this.searchDetail();
        this.refreshSummary();
      },getDetailListForSubmit(){
        let tempList = [];
        for (let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList) {
          if (util.isNotBlank(adGoodsOrderDetail.id) || (Number.isInteger(adGoodsOrderDetail.qty) && adGoodsOrderDetail.qty > 0)) {
            tempList.push(adGoodsOrderDetail)
          }
        }
        return tempList;
      }, initPage(){

        axios.get('/api/ws/future/layout/adGoodsOrder/getForm').then((response) => {
          this.inputForm = response.data;
          axios.get('/api/ws/future/layout/adGoodsOrder/findDetailListForNewOrEdit', {params: {adGoodsOrderId: this.$route.query.id, includeNotAllowOrderProduct: false}}).then((response) => {
            this.setAdGoodsOrderDetailList(response.data);
          });
          if(!this.isCreate){
            axios.get('/api/ws/future/layout/adGoodsOrder/findDto', {params: {id: this.$route.query.id}}).then((response) => {
              this.inputForm.id = response.data.id;
              this.inputForm.outShopId = response.data.outShopId;
              this.inputForm.shopId = response.data.shopId;
              this.inputForm.investInCause = response.data.investInCause;
              this.inputForm.employeeId = response.data.employeeId;
              this.inputForm.expressOrderExpressCompanyId = response.data.expressOrderExpressCompanyId;
              this.inputForm.expressOrderAddress = response.data.expressOrderAddress;
              this.inputForm.expressOrderExpressCodes = response.data.expressOrderExpressCodes;
              this.inputForm.expressOrderContator = response.data.expressOrderContator;
              this.inputForm.expressOrderMobilePhone = response.data.expressOrderMobilePhone;
              this.inputForm.remarks = response.data.remarks;
              this.processStatus = response.data.processStatus;

              this.refreshRecentMonthSaleAmount();
              axios.get('/api/ws/future/basic/depot/findByIds' + '?idStr=' + this.inputForm.outShopId).then((response) => {
                this.isDelegateShop = (response.data[0].jointType === '代理');
              });

            });
          }
        });
      }
    }, created(){
      this.initPage();
    }
  }
</script>
