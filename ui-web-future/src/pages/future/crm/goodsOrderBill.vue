<template>
  <div>
    <head-tab active="goodsOrderBill"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="8">
            <el-form-item :label="$t('goodsOrderBill.store')" prop="storeId">
              <depot-select v-model="inputForm.storeId" category="store" @input="refreshStoreQty"></depot-select>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.billDate')" prop="billDate">
              <date-picker v-model="inputForm.billDate"  ></date-picker>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.expressCompany')" prop="expressCompanyId">
              <el-select v-model="inputForm.expressCompanyId" clearable  >
                <el-option v-for="item in formProperty.expressCompanyList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.synToCloud')" prop="syn">
              <bool-radio-group v-model="inputForm.syn"  ></bool-radio-group>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shipType')" >
              {{goodsOrder.shipType}}
            </el-form-item>
            <el-form-item label="网络制式" >
              {{goodsOrder.netType}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.goodsOrderRemarks')" prop="remarks">
              <el-input type="textarea" v-model="inputForm.remarks"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">

            <el-form-item :label="$t('goodsOrderBill.contact')" prop="contator">
              <el-input v-model="inputForm.contator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.address')" prop="address">
              <el-input type="textarea" v-model="inputForm.address"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.mobilePhone')" prop="mobilePhone">
              <el-input v-model="inputForm.mobilePhone"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.areaName')" >
              {{shopAccount.areaName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopName')" >
              {{shopAccount.name}} <div style="color:red;font-size:16px">{{shopAccount.depotShopAreaType}}</div>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.clientName')"  >
              {{shopAccount.clientName}}
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label="$t('goodsOrderBill.shouldGet')">
              {{shopAccount.qmys}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopRemarks')">
              {{shopAccount.remarks}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.credit')">
              {{shopAccount.credit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.summary')" >
              总开单数{{summaryInfo.totalBillQty }}, 总开单金额{{summaryInfo.totalBillAmount.toFixed(2)}}, 总货品数{{summaryInfo.totalProductBillQty }}, 总货品金额{{summaryInfo.totalProductBillAmount.toFixed(2)}}, 开单后应收{{(summaryInfo.totalBillAmount+shopAccount.qmys).toFixed(2)}}
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderBill.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-input v-model="filterValue" @change="filterProducts" :placeholder="$t('goodsOrderBill.selectTowKey')" style="width:200px;"></el-input>
      <el-table :data="filterDetailList" style="margin-top:5px;" v-loading="pageLoading" :element-loading-text="$t('goodsOrderBill.loading')" stripe border>
        <el-table-column  prop="productName" :label="$t('goodsOrderBill.productName')" sortable width="300"></el-table-column>
        <el-table-column prop="areaQty" sortable :label="$t('goodsOrderBill.areaBillQty')"></el-table-column>
        <el-table-column prop="storeQty" :label="$t('goodsOrderBill.stock')"></el-table-column>
        <el-table-column prop="allowOrder" :label="$t('goodsOrderBill.allowOrder')">
          <template scope="scope">
            <el-tag :type="scope.row.allowOrder? 'primary' : 'danger'">{{scope.row.allowOrder | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="qty" :label="$t('goodsOrderBill.qty')"></el-table-column>
        <el-table-column prop="billQty" :label="$t('goodsOrderBill.billQty')" >
          <template scope="scope">
            <el-input v-model.number="scope.row.billQty" @input="billQtyChanged(scope.row)" ></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="price" :label="$t('goodsOrderBill.price')">
          <template scope="scope">
            <el-input v-model.number="scope.row.price" @input="initSummary(scope.row)" ></el-input>
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
  import depotSelect from 'components/future/depot-select'
  export default{
    components:{
      boolRadioGroup,
      depotSelect,
    },
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        filterValue:"",
        filterDetailList:[],
        formProperty:{
          expressProductId:null,
          expressRuleList:[],
          expressCompanyList:[],
        },
        shopAccount:{},
        inputForm:{
          id:null,
          storeId:null,
          billDate:null,
          expressCompanyId:null,
          syn:null,
          remarks:null,
          contator:null,
          address:null,
          mobilePhone:null,
          goodsOrderBillDetailFormList:[],
        },
        goodsOrder:{},
        summaryInfo:{
          totalBillQty:0,
          totalBillAmount:0,
          totalProductBillQty:0,
          totalProductBillAmount:0,
        },
        rules: {
          storeId: [{ required: true, message: this.$t('goodsOrderBill.prerequisiteMessage')}],
          billDate: [{ required: true, message: this.$t('goodsOrderBill.prerequisiteMessage')}],
          expressCompanyId: [{ required: true, message: this.$t('goodsOrderBill.prerequisiteMessage')}],
          syn: [{ required: true, message: this.$t('goodsOrderBill.prerequisiteMessage')}],
          contator: [{ required: true, message: this.$t('goodsOrderBill.prerequisiteMessage')}],
          address: [{ required: true, message: this.$t('goodsOrderBill.prerequisiteMessage')}],
          mobilePhone: [{ required: true, message: this.$t('goodsOrderBill.prerequisiteMessage')}],
        },
        pageLoading:false
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            let submitData =JSON.parse(JSON.stringify(this.inputForm));
            submitData.goodsOrderBillDetailFormList = this.getDetailListForSubmit();
            axios.post('/api/ws/future/crm/goodsOrder/bill', qs.stringify(submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              this.$router.push({name:'goodsOrderList',query:util.getQuery("goodsOrderList"), params:{_closeFrom:true}})
            }).catch( () => {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })

      },filterProducts(){

        let filterVal = _.trim(this.filterValue);
        let filterValNotBlank = util.isNotBlank(filterVal);
        let tempList=[];
        let tempPostList=[];
        for(let detail of this.inputForm.goodsOrderBillDetailFormList){
          if(util.isNotBlank(detail.billQty) || util.isNotBlank(detail.id) || detail.productId === this.formProperty.expressProductId){
            tempList.push(detail);
          }else if(filterValNotBlank && util.contains(detail.productName, filterVal) ){
            tempPostList.push(detail);
          }
        }
        this.filterDetailList = tempList.concat(tempPostList).slice(0, util.MAX_DETAIL_ROW);
      }
      ,initSummary(row){
        let tmpTotalBillQty = 0;
        let tmpTotalBillAmount = 0;
        let tmpTotalProductBillQty = 0;
        let tmpTotalProductBillAmount = 0;

        for(let detail of this.inputForm.goodsOrderBillDetailFormList) {

          if(util.isNotBlank(detail.billQty)) {
            tmpTotalBillQty  = tmpTotalBillQty + detail.billQty*1;
            tmpTotalBillAmount = tmpTotalBillAmount + (detail.billQty*1)*(detail.price*1);
            if(detail.hasIme){
              tmpTotalProductBillQty  = tmpTotalProductBillQty + detail.billQty*1;
              tmpTotalProductBillAmount = tmpTotalProductBillAmount + (detail.billQty*1)*(detail.price*1);
            }
          }
        }
        this.summaryInfo.totalProductBillQty = tmpTotalProductBillQty;
        this.summaryInfo.totalBillQty = tmpTotalBillQty;
        this.summaryInfo.totalProductBillAmount = tmpTotalProductBillAmount;
        this.summaryInfo.totalBillAmount = tmpTotalBillAmount;

      },refreshStoreQty(){
          if(!this.inputForm.goodsOrderBillDetailFormList){
              return;
          }
          //设置库存
          if(util.isBlank(this.inputForm.storeId)){
            for(let detail of this.inputForm.goodsOrderBillDetailFormList){
              detail.storeQty = null;
            }
            return;
          }
          axios.get('/api/ws/future/basic/depot/getCloudQtyMap', {params: {storeId: this.inputForm.storeId}}).then((response) => {
            for(let detail of this.inputForm.goodsOrderBillDetailFormList){
              if(response.data[detail.productOutId] !== undefined) {
                detail.storeQty = response.data[detail.productOutId];
                }else{
                detail.storeQty = null;
              }
            }
          });
      },
      getDetailListForSubmit(){
        let  tmpList = [];
        for(let detail of this.inputForm.goodsOrderBillDetailFormList) {
          if(util.isNotBlank(detail.id) || util.isNotBlank(detail.billQty)) {
            tmpList.push(detail);
          }
        }
        return tmpList;
      },refreshExpressShouldGet(row){
        //当无运费计算规则，或者发货类型为总部自提时，或者修改的是代收运费这一行，不会触发运费计算
        if(util.isBlank(this.formProperty.expressProductId) || !this.formProperty.expressRuleList || this.goodsOrder.shipType==="总部自提" || (row && row.productId === this.formProperty.expressProductId)){
          return ;
        }

        let expressShouldGetQty = 0;
        let expressShouldGetPrice = 0;

        for(let eachRule of this.formProperty.expressRuleList) {
          if(this.summaryInfo.totalProductBillQty>=eachRule.min && this.summaryInfo.totalProductBillQty<=eachRule.max && eachRule.areatype === this.shopAccount.depotShopAreaType ) {
            expressShouldGetQty = 1;
            expressShouldGetPrice = eachRule.price;
            break;
          }
        }

        for(let detail of this.inputForm.goodsOrderBillDetailFormList){
          if(detail.productId === this.formProperty.expressProductId){
            let expressShouldGetQtyChange = expressShouldGetQty- (util.isNotBlank(detail.billQty) ? detail.billQty*1 : 0);
            let expressShouldGetAmountChange = expressShouldGetQty*expressShouldGetPrice - (util.isNotBlank(detail.billQty) ? detail.billQty*1 : 0) *  (util.isNotBlank(detail.price) ? detail.price*1 : 0);

            detail.billQty = expressShouldGetQty;
            detail.price = expressShouldGetPrice;

            this.summaryInfo.totalBillQty+=expressShouldGetQtyChange;
            this.summaryInfo.totalBillAmount += expressShouldGetAmountChange ;

            return;
          }
        }
      },billQtyChanged(row){
        this.initSummary(row);
        this.refreshExpressShouldGet(row);
      }
    },created(){
      let formPropertyPromise = axios.get('/api/ws/future/crm/goodsOrder/getBillForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.formProperty = response.data.extra;
      });
      let billPromise = axios.get('/api/ws/future/crm/goodsOrder/getBill',{params: {id:this.$route.query.id}}).then((response)=>{
        this.goodsOrder = response.data;

        this.inputForm.id = response.data.id;
        this.inputForm.storeId = response.data.storeId;
        this.inputForm.billDate = response.data.billDate;
        this.inputForm.expressCompanyId = response.data.expressCompanyId;
        this.inputForm.syn = response.data.syn;
        this.inputForm.remarks = response.data.remarks;
        this.inputForm.contator = response.data.contator;
        this.inputForm.address = response.data.address;
        this.inputForm.mobilePhone = response.data.mobilePhone;
        this.inputForm.goodsOrderBillDetailFormList = response.data.goodsOrderDetailDtoList;

        this.filterProducts();
        this.initSummary(null);
        this.refreshStoreQty();
      });
      let shopAccountPromise = axios.get('/api/ws/future/crm/goodsOrder/findShopAccountByGoodsOrderId',{params: {goodsOrderId:this.$route.query.id}}).then((response)=>{
        this.shopAccount = response.data;
      });

      Promise.all([formPropertyPromise, billPromise, shopAccountPromise]).then( () => {
          this.refreshExpressShouldGet(null);
      });
    }
  }
</script>
