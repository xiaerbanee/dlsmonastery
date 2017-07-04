<template>
  <div>
    <head-tab active="goodsOrderMallForm"></head-tab>
    <el-row v-if="alertError">
      <el-col :span="24">
        <el-alert v-html="error" title="" type="error" :closable="true"></el-alert>
      </el-col>
    </el-row>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderForm.shop')" prop="shopId">
              <depot-select  v-model="inputForm.shopId" category="shop" @input="shopChange(inputForm.shopId)"></depot-select>
            </el-form-item>
            <el-form-item  label="总店">
              {{shop.name}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.netType')" prop="netType">
              <el-select   v-model="inputForm.netType"    clearable :placeholder="$t('goodsOrderForm.inputWord')" @change="refreshDetailList">
                <el-option v-for="item in inputForm.extra.netTypeList" :key="item":label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.shipType')" prop="shipType" >
              <el-select   v-model="inputForm.shipType"  clearable :placeholder="$t('goodsOrderForm.inputKey')" @change="refreshDetailList" >
                <el-option v-for="item in inputForm.extra.shipTypeList" :key="item":label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.remarks')"  prop="remarks  ">
              <el-input type="textarea" v-model="inputForm.remarks"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商城门店" prop="carrierShopId">
              <depot-select  v-model="inputForm.carrierShopId" category="shop"></depot-select>
            </el-form-item>
            <el-form-item label="商城单号" prop="carrierCodes">
              <el-input  v-model="inputForm.carrierCodes" readonly></el-input>
            </el-form-item>
            <el-form-item label="商城订单信息" prop="detailJson">
              <el-input type="textarea"  :autosize="{ minRows: 2, maxRows: 6}" v-model="inputForm.detailJson" @blur="checkDetailJson(inputForm.detailJson)"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.shopType')" prop="type">
              {{shop.depotType}}
            </el-form-item>
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
        </el-row>
      </el-form>
      <div v-show="inputForm.shopId">
        <el-input v-model="filterValue" @input="filterProducts" :placeholder="$t('shopAllotForm.selectTowKey')" style="width:200px;"></el-input>
        <el-table :data="filterDetailList" border stripe v-loading="pageLoading" style="margin-top:10px;">
          <el-table-column  prop = "productName" :label="$t('goodsOrderForm.productName')" ></el-table-column>
          <el-table-column prop="hasIme" :label="$t('goodsOrderForm.hasIme')" width="70">
            <template scope="scope">
              <el-tag   :type="scope.row.hasIme ? 'primary' : 'danger'">{{scope.row.hasIme | bool2str}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="price" :label="$t('goodsOrderForm.price')" width="100"></el-table-column>
          <el-table-column prop="qty" :label="$t('goodsOrderForm.qty')">
            <template scope="scope">
              <input  v-model="scope.row.qty" class="el-input__inner" @change="initSummary"/>
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
  export default{
    components:{
      depotSelect
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
          alertError:false,
          filterValue:'',
          goodsOrder:{},
          filterDetailList:[],
          goodsOrderDetailList:[],
          shop:{},
          summary:'',
          inputForm:{
            extra:{}
          },
          rules: {},
          productMap:{}
        }
      },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            var  goodsOrderDetailFormList = new Array();
            for(var index in this.filterDetailList) {
              var filterDetail = this.filterDetailList[index];
              if(util.isNotBlank(filterDetail.id) || util.isNotBlank(filterDetail.qty)) {
                goodsOrderDetailFormList.push(filterDetail);
              }
            }
            var submitData= util.deleteExtra(this.inputForm);
            submitData.goodsOrderDetailFormList = goodsOrderDetailFormList;
            axios.post('/api/ws/future/crm/goodsOrder/save', qs.stringify(submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(that.isCreate){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }else{
                this.$router.push({name:'goodsOrderList',query:util.getQuery("goodsOrderList"), params:{_closeFrom:true}})
              }
            }).catch(()=> {
              that.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },checkDetailJson(item){
        var that=this;
        if(item){
          axios.get('/api/ws/future/api/carrierOrder/checkDetailJsons',{params: {detailJson:item,checkColor:true}}).then((response)=>{
            if(response.data.success){
              this.$message(response.data.message);
              this.inputForm.carrierCodes=response.data.extra.carrierCodes;
              this.getProductDetail(response.data.extra.productQtyMap);
            }else {
              that.submitDisabled = false;
              this.$message({
                showClose: true,
                message: response.data.message,
                type: 'error'
              });
            }
          });
        }
      },filterProducts(){
        if(!this.goodsOrderDetailList){
          this.filterDetailList = [];
          return;
        }
        let val=_.trim(this.filterValue);
        let tempList=[];
        let tempPostList=[];
        for(let goodsOrderDetail of this.goodsOrderDetailList){
          if(util.isNotBlank(goodsOrderDetail.qty)){
            tempList.push(goodsOrderDetail);
          }else if(util.contains(goodsOrderDetail.productName, val) && util.isNotBlank(val)){
            tempPostList.push(goodsOrderDetail);
          }
        }
        this.filterDetailList = tempList.concat(tempPostList).slice(0, util.MAX_DETAIL_ROW);
      },shopChange(id){
        axios.get('/api/ws/future/basic/depot/findOne',{params: {id:id}}).then((response)=>{
          this.shop = response.data;
        });
      },refreshDetailList(){
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
      },setGoodsOrderDetailList(list){
        this.goodsOrderDetailList = list;
        this.filterProducts();
      },initSummary() {
        let totalQty = 0;
        let totalAmount = 0;
        let totalProductQty = 0;
        let totalProductAmount = 0;

        for(let detail of this.filterDetailList) {
          if(util.isNotBlank(detail.qty)) {
            totalQty  = totalQty + detail.qty*1;
            totalAmount = totalAmount + (detail.qty*1)*(detail.price*1);
            if(detail.hasIme){
              totalProductQty  = totalProductQty + detail.qty*1;
              totalProductAmount = totalProductAmount + (detail.qty*1)*(detail.price*1);
            }
          }
        }
        this.summary = "总开单数："+totalQty+"，总开单金额："+totalAmount+"，总货品数：" + totalProductQty + "，总货品金额：" + totalProductAmount;
      },initPage(){
        axios.get('/api/ws/future/crm/goodsOrder/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
        });
        axios.get('/api/ws/future/crm/goodsOrder/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
          if(!this.isCreate) {
            this.shopChange(this.inputForm.shopId);
            axios.get('/api/ws/future/crm/goodsOrder/findDetailList',{params: {id:this.$route.query.id}}).then((response)=>{
              this.setGoodsOrderDetailList(response.data);
              this.initSummary();
            });
          }
        });
      },getProductDetail(productMap){
        for(var item in this.goodsOrderDetailList){
          var product=this.goodsOrderDetailList[item];
          var key=product.productId;
          if(productMap.hasOwnProperty(key)){
            product.qty=productMap[key]
          }
        }
        this.filterProducts();
        this.initSummary();
      }
    },created () {
      this.initPage();
    }
  }
</script>
