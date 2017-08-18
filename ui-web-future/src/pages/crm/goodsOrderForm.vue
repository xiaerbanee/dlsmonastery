<template>
  <div>
    <head-tab active="goodsOrderForm"></head-tab>

    <div>
      <su-alert  :text="warnMsg"  type="warning"></su-alert>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row>
          <el-col :span="8">
            <el-form-item :label="$t('goodsOrderForm.shop')" prop="shopId">
              <depot-select :disabled="!isCreate" v-model="inputForm.shopId" category="shop" @input="shopChange"></depot-select>
            </el-form-item>
            <el-form-item  :label="$t('goodsOrderForm.clientName')"  prop="clientName">
              {{shop.clientName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.remarks')" prop="remarks">
              <el-input type="textarea" v-model="inputForm.remarks"></el-input>
            </el-form-item>
          </el-col>
          <div v-show="inputForm.shopId">
          <el-col :span="8">
              <el-form-item :label="$t('goodsOrderForm.netType')" prop="netType">
                <el-select  :disabled="!isCreate" v-model="inputForm.netType"    clearable :placeholder="$t('goodsOrderForm.inputWord')" @change="refreshDetailList">
                  <el-option v-for="item in formProperty.netTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <div v-if="inputForm.netType==='联信'">
                <el-form-item :label="$t('goodsOrderForm.lxMallOrder')" prop="lxMallOrder">
                  <bool-select v-model="inputForm.lxMallOrder"></bool-select>
                </el-form-item>
              </div>
              <el-form-item :label="$t('goodsOrderForm.shipType')" prop="shipType" >
                <el-select :disabled="!isCreate" v-model="inputForm.shipType"  clearable :placeholder="$t('goodsOrderForm.inputKey')" @change="refreshDetailList" >
                  <el-option v-for="item in formProperty.shipTypeList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
          </el-col>
          <el-col :span="8">
              <el-form-item :label="$t('goodsOrderForm.priceSystem')" prop="pricesystem">
                {{shop.pricesystemName}}
              </el-form-item>
              <el-form-item :label="$t('goodsOrderForm.summary')" prop="summary" style="color:red;">
                {{summary}}
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderForm.save')}}</el-button>
              </el-form-item>
          </el-col>
          </div>
        </el-row>
      </el-form>
      <div v-show="inputForm.shopId && inputForm.netType && inputForm.shipType">
        <el-input v-model="filterValue" @input="filterProducts" placeholder="货品名称搜索" style="width:200px;"></el-input>
        <el-tooltip placement="top" effect="light">
          <div slot="content">搜索框输入关键字时，下面显示搜索结果，最多100行。<br/>当搜索框为空时，下面显示所有有效订货明细。<br/>页面保存前请清空搜索框，检查明细是否正确。</div>
          <el-button type="text">说明</el-button>
        </el-tooltip>
        <el-table :data="filterDetailList" border stripe v-loading="pageLoading" style="margin-top:10px;">
          <el-table-column  prop = "productName" :label="$t('goodsOrderForm.productName')" width="300"></el-table-column>
          <el-table-column prop="hasIme" :label="$t('goodsOrderForm.hasIme')" width="70">
            <template scope="scope">
              <el-tag   :type="scope.row.hasIme ? 'primary' : 'danger'">{{scope.row.hasIme | bool2str}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="price" :label="$t('goodsOrderForm.price')" width="100"></el-table-column>
          <el-table-column prop="qty" :label="$t('goodsOrderForm.qty')">
            <template scope="scope">
              <input  v-model="scope.row.qty" class="el-input__inner" @input="initSummary"/>
            </template>
          </el-table-column>
          <el-table-column prop="allowOrder" :label="$t('goodsOrderForm.allowOrder')">
            <template scope="scope">
              <el-tag  :type="scope.row.allowOrder? 'primary' : 'danger'">{{scope.row.allowOrder | bool2str}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="areaQty" :label="$t('goodsOrderForm.areaQty')" ></el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>
<script>
  import depotSelect from 'components/future/depot-select'
  import boolSelect from 'components/common/bool-select';

  export default{
    components:{
      depotSelect,
      boolSelect,
    },
    data(){
      return this.getData()
    },
    methods:{
      getData() {
        return{
          isCreate:this.$route.query.id==null,
          submitDisabled:false,
          pageLoading:false,
          filterValue:'',
          warnMsg:'',
          filterDetailList:[],
          formProperty:{},

          shop:{},
          summary:'',
          inputForm:{
            id:null,
            shopId:null,
            shipType:null,
            netType:null,
            lxMallOrder:null,
            remarks:null,
          },
          goodsOrderDetailList:[],
          rules: {
            shopId: [{required: true, message: this.$t('goodsOrderForm.prerequisiteMessage')}],
            netType: [{required: true, message: this.$t('goodsOrderForm.prerequisiteMessage')}],
            shipType: [{required: true, message: this.$t('goodsOrderForm.prerequisiteMessage')}],
          }
        }
      },
      formSubmit(){
        if(util.isNotBlank(this.filterValue)){
          this.$message("请清空货品搜索条件，确认订货明细无误后提交");
          return;
        }
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            let submitData= JSON.parse(JSON.stringify(this.inputForm));
            submitData.goodsOrderDetailFormList = this.getDetailListForSubmit();
            axios.post('/api/ws/future/crm/goodsOrder/save', qs.stringify(submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }else{
                util.closeAndBackToPage(this.$router,'goodsOrderList')
              }
            }).catch(()=> {
                this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
      })
      },filterProducts(){
        if(!this.goodsOrderDetailList){
          this.filterDetailList = [];
          return;
        }

        let filterVal = _.trim(this.filterValue);
        let tempList=[];
        if(util.isNotBlank(filterVal)){
          for(let goodsOrderDetail of this.goodsOrderDetailList){
            if(util.contains(goodsOrderDetail.productName, filterVal)){
              tempList.push(goodsOrderDetail);
            }
          }
          this.filterDetailList = tempList.slice(0, util.MAX_FILTER_DETAIL_ROW);
        }else {
          for (let goodsOrderDetail of this.goodsOrderDetailList) {
            if (util.isNotBlank(goodsOrderDetail.qty)) {
              tempList.push(goodsOrderDetail);
            }
          }
          this.filterDetailList = tempList;
        }
      },shopChange(){
          if(this.isCreate && util.isNotBlank(this.inputForm.shopId)){
            axios.get('/api/ws/future/crm/goodsOrder/validateShop',{params: {shopId:this.inputForm.shopId}}).then((response)=>{
              let tmpMsg = '';
              for(let each of response.data.errors) {
                tmpMsg = tmpMsg + each.message + "<br/>";
              }

              this.warnMsg = tmpMsg;
            });
          }else{
            this.warnMsg='';
          }

        axios.get('/api/ws/future/basic/depot/findOne',{params: {id:this.inputForm.shopId}}).then((response)=>{
          this.shop = response.data;
        });
        this.refreshDetailList();
      }, getDetailListForSubmit(){
        let  tmpList = [];
        for(let detail of this.goodsOrderDetailList) {
          if(util.isNotBlank(detail.id) || util.isNotBlank(detail.qty)) {
            tmpList.push(detail);
          }
        }
        return tmpList;
      }, refreshDetailList(){
        if(this.isCreate ) {
          if(this.inputForm.shopId && this.inputForm.netType && this.inputForm.shipType) {
            this.pageLoading = true;
            axios.get('/api/ws/future/crm/goodsOrder/findDetailList', {params: {shopId:this.inputForm.shopId, netType: this.inputForm.netType,shipType:this.inputForm.shipType}}).then((response)=>{
              this.setGoodsOrderDetailList(response.data);
            this.pageLoading = false;
          });
          }else{
            this.setGoodsOrderDetailList([]);
          }
        }
      }, setGoodsOrderDetailList(list){
        this.goodsOrderDetailList = list;
        this.filterProducts();
      }, initSummary() {
        let totalQty = 0;
        let totalAmount = 0;
        for(let detail of this.goodsOrderDetailList) {
          if(util.isNotBlank(detail.qty)) {
            totalQty  = totalQty + detail.qty*1;
            totalAmount = totalAmount + (detail.qty*1)*(detail.price*1);
          }
        }
        this.summary = "总订货数为：" + totalQty + "，总价格为：" + totalAmount;
      }, initPage(){
        let formPropertyPromise = axios.get('/api/ws/future/crm/goodsOrder/getForm',{params: {id:this.$route.query.id}});
        let goodsOrderPromise = (this.isCreate ? Promise.resolve() : axios.get('/api/ws/future/crm/goodsOrder/findOne',{params: {id:this.$route.query.id}}));
        let goodsOrderDetailPromise = (this.isCreate ? Promise.resolve() : axios.get('/api/ws/future/crm/goodsOrder/findDetailList',{params: {id:this.$route.query.id}}));

        Promise.all([formPropertyPromise, goodsOrderPromise, goodsOrderDetailPromise]).then(axios.spread((formPropertyResponse, goodsOrderResponse, goodsOrderDetailResponse)=>{
          this.formProperty = formPropertyResponse.data.extra;
          if(this.isCreate){
            if(this.formProperty.defaultNetType){
              this.inputForm.netType = this.formProperty.defaultNetType;
            }
            if(this.formProperty.defaultShipType){
              this.inputForm.shipType = this.formProperty.defaultShipType;
            }
          }else{
            axios.get('/api/ws/future/basic/depot/findOne',{params: {id:goodsOrderResponse.data.shopId}}).then((response)=>{
              this.shop = response.data;
            });

            this.inputForm.id = goodsOrderResponse.data.id;
            this.inputForm.shopId = goodsOrderResponse.data.shopId;
            this.inputForm.netType = goodsOrderResponse.data.netType;
            this.inputForm.lxMallOrder = goodsOrderResponse.data.lxMallOrder;
            this.inputForm.shipType = goodsOrderResponse.data.shipType;
            this.inputForm.remarks = goodsOrderResponse.data.remarks;

            this.setGoodsOrderDetailList(goodsOrderDetailResponse.data);
            this.initSummary();
          }
        }));
      }
    }, created () {
      this.initPage();
    }
  }
</script>
