<template>
  <div>
    <head-tab active="adGoodsOrderShip"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px"  class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('adGoodsOrderShip.shipCode')" prop="id">
              {{adGoodsOrder.id}}
          </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.remarks')" prop="remarks">
              {{adGoodsOrder.remarks}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.smallQty')" prop="smallQty" >
              <el-input v-model="inputForm.smallQty" @input="materialChange()"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.mediumQty')" prop="mediumQty" >
              <el-input v-model="inputForm.mediumQty" @input="materialChange()"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.largeQty')" prop="largeQty">
              <el-input v-model="inputForm.largeQty" @input="materialChange()"></el-input>
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
            <el-form-item :label="$t('adGoodsOrderShip.outShopName')" prop="outShopId">
              {{adGoodsOrder.outShopName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.shopName')" prop="shopId">
              {{adGoodsOrder.shopName}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.saleContact')" >
              {{employee.officeName}}_{{employee.positionName}}_{{employee.name}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.saleMobile')" prop="mobilePhone">
              {{employee.mobilePhone}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.billRemarks')" prop="billRemarks">
              {{adGoodsOrder.billRemarks}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.contact')" prop="expressOrderContator">
              {{expressOrder.contator}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.address')" prop="address">
              {{expressOrder.address}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.mobilePhone')" prop="mobilePhone">
              {{expressOrder.mobilePhone}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.shouldPay')" prop="shouldPay">
              {{inputForm.expressOrderShouldPay}}
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.shouldGet')" prop="expressOrderShouldGet">
              <el-input v-model="inputForm.expressOrderShouldGet"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adGoodsOrderShip.realPay')" prop="expressOrderRealPay">
              <el-input v-model="inputForm.expressOrderRealPay"></el-input>
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
            <el-input v-model="scope.row.shipQty"  @input="materialChange()"> </el-input>
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
        employee:{},
        expressOrder:{},
        productName:'',
        filterAdGoodsOrderDetailList:[],
        inputForm:{
            extra:{},
        },
        rules: {},
        pageLoading:false,
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/layout/adGoodsOrder/ship',qs.stringify(util.deleteExtra(this.inputForm), {allowDots:true})).then((response)=> {
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
      },materialChange(){
      var shouldGet= this.shipForm.shouldGet
      var shouldPay= this.shipForm.shouldPay
      var ysyfAmount=this.shipFormProperty.ysyfAmount;
      var yfyfAmount=this.shipFormProperty.yfyfAmount;
      var smallPrice=this.shipFormProperty.smallPrice;
      var mediumPrice=this.shipFormProperty.mediumPrice;
      var bigPrice=this.shipFormProperty.bigPrice;
      var shipPrice=this.shipForm.smallQty*smallPrice+ this.shipForm.mediumQty*mediumPrice+ this.shipForm.largeQty*bigPrice;
			var pay=0; //实付运费(实际面单金额)
       let list=this.inputForm.adGoodsOrderDetailList;
        for(let item in list){
              console.log(parseInt(list[item].shipQty==null?0:list[item].shipQty)+parseInt(list[item].shippedQty==NaN?0:list[item].shippedQty))
             console.log()
          if(list[item].shipQty|| list[item].shippedQty){
            pay=parseInt(pay)+(parseInt(list[item].shipQty==null?0:list[item].shipQty)+parseInt(list[item].shippedQty==null?0:list[item].shippedQty))*parseInt(list[item].product.price==null?0:list[item].price);
          }
        }
			this.inputForm.expressOrderRealPay=parseInt(pay)+parseInt(shipPrice);
			this.inputForm.expressOrderShouldGet=parseInt(shouldGet)+parseInt(shipPrice);
			this.inputForm.expressOrderShouldPay=parseInt(shouldPay)+parseInt(shipPrice);
      }, searchDetail(){
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
        axios.get('/api/ws/future/layout/adGoodsOrder/getShipForm').then((response)=>{
          this.inputForm=response.data;

          axios.get('/api/ws/future/layout/adGoodsOrder/findDetailListByAdGoodsOrderId', {params: {adGoodsOrderId: this.$route.query.id}}).then((response) => {
            this.inputForm.adGoodsOrderDetailList = response.data;
            if( this.inputForm.adGoodsOrderDetailList){
                for(let adGoodsOrderDetail of this.inputForm.adGoodsOrderDetailList){
                    adGoodsOrderDetail.shipQty = adGoodsOrderDetail.waitShipQty;
                }
            }
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
              this.inputForm.expressOrderShouldGet = this.expressOrder.shouldGet;
              this.inputForm.expressOrderRealPay = this.expressOrder.realPay;
              this.inputForm.expressOrderShouldPay = this.expressOrder.shouldPay;
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

