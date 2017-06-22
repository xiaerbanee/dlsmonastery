<template>
  <div>
    <head-tab active="adGoodsOrderShip"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('adGoodsOrderShip.shipCode')">
              {{adGoodsOrder.id}}
          </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.remarks')">
              {{adGoodsOrder.remarks}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.smallQty')" prop="smallQty" >
              <el-input v-model.number="inputForm.smallQty" @input="refreshExpressOrderMoney()"></el-input>
              <span> {{$t('adGoodsOrderShip.smallPrice')}} {{ysyfMap.smallPrice }}</span>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.mediumQty')" prop="mediumQty" >
              <el-input v-model.number="inputForm.mediumQty" @input="refreshExpressOrderMoney()"></el-input>
              <span> {{$t('adGoodsOrderShip.mediumPrice')}} {{ysyfMap.mediumPrice }}</span>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.largeQty')" prop="largeQty">
              <el-input v-model.number="inputForm.largeQty" @input="refreshExpressOrderMoney()"></el-input>
              <span> {{$t('adGoodsOrderShip.largePrice')}} {{ysyfMap.largePrice }}</span>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.expressCodes')" prop="expressOrderExpressCodes">
              <el-input type="textarea" v-model="inputForm.expressOrderExpressCodes" :placeholder="$t('adGoodsOrderShip.enter')"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.expressCompany')" prop="expressOrderExpressCompanyId">
              <express-company-select v-model="inputForm.expressOrderExpressCompanyId"></express-company-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adGoodsOrderShip.ship')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('adGoodsOrderShip.outShopName')">
              {{adGoodsOrder.outShopName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.shopName')">
              {{adGoodsOrder.shopName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.saleContact')" >
              {{employee.officeName}}_{{employee.positionName}}_{{employee.name}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.saleMobile')">
              {{employee.mobilePhone}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.billRemarks')">
              {{adGoodsOrder.billRemarks}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.contact')">
              {{expressOrder.contator}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.address')">
              {{expressOrder.address}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.mobilePhone')">
              {{expressOrder.mobilePhone}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.shouldPay')">
              {{inputForm.expressOrderShouldPay}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.shouldGet')" prop="expressOrderShouldGet">
              <el-input v-model.number="inputForm.expressOrderShouldGet"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.realPay')" prop="expressOrderRealPay">
              <el-input v-model.number="inputForm.expressOrderRealPay"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-input v-model="productName" @change="searchDetail" :placeholder="$t('adGoodsOrderBill.inputTwoKey')" style="width:200px;"></el-input>
      <el-table :data="filterAdGoodsOrderDetailList" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('adGoodsOrderShip.loading')" stripe border >
        <el-table-column prop="productCode" :label="$t('adGoodsOrderShip.code')" sortable width="200"></el-table-column>
        <el-table-column prop="productName" :label="$t('adGoodsOrderShip.productName')"></el-table-column>
        <el-table-column prop="billQty" :label="$t('adGoodsOrderShip.billQty')"></el-table-column>
        <el-table-column prop="shippedQty" :label="$t('adGoodsOrderShip.shippedQty')"></el-table-column>
        <el-table-column prop="waitShipQty" :label="$t('adGoodsOrderShip.waitShipQty')" ></el-table-column>
        <el-table-column prop="shipQty" :label="$t('adGoodsOrderShip.shipQty')" >
          <template scope="scope">
            <el-input v-model.number="scope.row.shipQty"  @input="refreshExpressOrderMoney()"> </el-input>
          </template>
        </el-table-column>
      </el-table>
      <process-details v-model="adGoodsOrder.processInstanceId"></process-details>

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
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        adGoodsOrder:{},
        ysyfMap:{
            priceMap:{},
        },
        employee:{},
        expressOrder:{},
        productName:'',
        filterAdGoodsOrderDetailList:[],
        inputForm:{
            extra:{},
        },
        rules: {
          smallQty: [{required: true,  type:"number", message: this.$t('adGoodsOrderShip.prerequisiteAndNumberMessage')}],
          mediumQty: [{required: true,  type:"number",  message: this.$t('adGoodsOrderShip.prerequisiteAndNumberMessage')}],
          largeQty: [{required: true,   type:"number",  message: this.$t('adGoodsOrderShip.prerequisiteAndNumberMessage')}],
          expressOrderExpressCodes: [{required: true, message: this.$t('adGoodsOrderShip.prerequisiteMessage')}],
          expressOrderExpressCompanyId: [{required: true, message: this.$t('adGoodsOrderShip.prerequisiteMessage')}],
          expressOrderShouldGet: [{required: true, type:"number",  message: this.$t('adGoodsOrderShip.prerequisiteMessage')}],
          expressOrderRealPay: [{required: true,  type:"number",   message: this.$t('adGoodsOrderShip.prerequisiteMessage')}],
        },
        pageLoading:false,
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
            axios.post('/api/ws/future/layout/adGoodsOrder/ship',qs.stringify(util.deleteExtra(this.inputForm), {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              this.submitDisabled = false;
              if(response.data.success){
                this.$router.push({name:'adGoodsOrderList',query:util.getQuery("adGoodsOrderList"), params:{_closeFrom:true}});
              }
            }).catch( () => {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },refreshExpressOrderMoney(){

        let smallQty = Number.isInteger(this.inputForm.smallQty) ? this.inputForm.smallQty : 0 ;
        let mediumQty = Number.isInteger(this.inputForm.mediumQty) ? this.inputForm.mediumQty : 0 ;
        let largeQty = Number.isInteger(this.inputForm.largeQty) ? this.inputForm.largeQty : 0 ;

        let shipPrice= smallQty * this.ysyfMap.smallPrice + mediumQty * this.ysyfMap.mediumPrice + largeQty * this.ysyfMap.largePrice;

        this.inputForm.expressOrderShouldGet = this.ysyfMap.ysAmount + shipPrice;
        this.inputForm.expressOrderShouldPay = this.ysyfMap.yfAmount + shipPrice;

			  let realPay = shipPrice; //实付运费(实际面单金额)
        for(let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList){

            let shipQty = Number.isInteger(adGoodsOrderDetail.shipQty) ? adGoodsOrderDetail.shipQty : 0;
            let shippedQty = adGoodsOrderDetail.shippedQty *1;

            let price = (this.ysyfMap.priceMap[adGoodsOrderDetail.productId]  ? this.ysyfMap.priceMap[adGoodsOrderDetail.productId]  : 0);
            realPay += (shipQty+shippedQty) * price;

        }
			  this.inputForm.expressOrderRealPay=realPay;

      },customValidate(){

        let totalShipQty = 0;
        for(let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList){
          if(util.isBlank(adGoodsOrderDetail.shipQty)){
            continue;
          }

          if(!Number.isInteger(adGoodsOrderDetail.shipQty) || adGoodsOrderDetail.shipQty < 0){
            return '产品：'+adGoodsOrderDetail.productName+'的发货数不是一个大于等于0的整数';
          }
          if(adGoodsOrderDetail.shipQty > adGoodsOrderDetail.waitShipQty*1){
            return '产品：'+adGoodsOrderDetail.productName+'的发货数大于待发货数';
          }

          totalShipQty += adGoodsOrderDetail.shipQty;
        }
        if(totalShipQty<=0){
          return "每次总发货数要大于0";
        }
        return null;
      }
      , searchDetail(){
        let val = this.productName;
        if(!val){
          this.filterAdGoodsOrderDetailList = this.inputForm.adGoodsOrderDetailList;
          return;
        }
        let tempList = [];
        for (let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList) {
          if (util.contains(adGoodsOrderDetail.productName, val)) {
            tempList.push(adGoodsOrderDetail)
          }
        }
        this.filterAdGoodsOrderDetailList = tempList;

      },initPage(){

        axios.get('/api/ws/future/layout/adGoodsOrder/getYsyfMap', {params: {adGoodsOrderId: this.$route.query.id}}).then((response) => {
          this.ysyfMap = response.data;

          this.inputForm.expressOrderShouldGet = this.ysyfMap.initShouldGet;
          this.inputForm.expressOrderRealPay = this.ysyfMap.initRealPay;
          this.inputForm.expressOrderShouldPay = this.ysyfMap.initShouldPay;
        });


        axios.get('/api/ws/future/layout/adGoodsOrder/getShipForm').then((response)=>{
          this.inputForm=response.data;

          axios.get('/api/ws/future/layout/adGoodsOrder/findDetailListByAdGoodsOrderId', {params: {adGoodsOrderId: this.$route.query.id}}).then((response) => {
              let tmpList = response.data;
            if( tmpList){
                for(let each of tmpList){
                  each.shipQty = each.waitShipQty;
                }
            }
            this.inputForm.adGoodsOrderDetailList = tmpList;
            this.searchDetail();
          });

          axios.get('/api/ws/future/layout/adGoodsOrder/findDto',{params:{id:this.$route.query.id}}).then((response)=> {
            this.adGoodsOrder =response.data;
            this.inputForm.id = this.adGoodsOrder.id;
            this.inputForm.smallQty = this.adGoodsOrder.smallQty;
            this.inputForm.mediumQty = this.adGoodsOrder.mediumQty;
            this.inputForm.largeQty = this.adGoodsOrder.largeQty;
            this.inputForm.expressOrderExpressCompanyId = this.adGoodsOrder.expressOrderExpressCompanyId;
            this.inputForm.expressOrderExpressCodes = this.adGoodsOrder.expressOrderExpressCodes;

            axios.get('/api/ws/future/crm/expressOrder/findDto',{params: {id:this.adGoodsOrder.expressOrderId}}).then((response)=>{
              this.expressOrder = response.data;

            });
            axios.get('/api/basic/hr/employee/findOne',{params: {id:this.adGoodsOrder.employeeId}}).then((response)=>{
              this.employee = response.data;
            });
          });
        });
      }
    },created(){
        this.initPage();
    }
  }
</script>

