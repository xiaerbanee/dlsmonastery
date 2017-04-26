<template>
  <div>
    <head-tab active="goodsOrderForm"></head-tab>
    <el-row v-if="alertError">
      <el-col :span="24">
        <el-alert v-html="error" title="" type="error" :closable="true"></el-alert>
      </el-col>
    </el-row>
    <div >
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row>
          <el-col :span="12">
        <el-form-item :label="$t('goodsOrderForm.shop')" prop="shopId">
          <el-select v-model="inputForm.shopId" clearable filterable remote :placeholder="$t('goodsOrderForm.selectWord')" :remote-method="remoteDepot" @change="getDepot(inputForm.shopId)" :loading="remoteLoading">
            <el-option v-for="item in depots" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('goodsOrderForm.parentShop')" prop="parentId">
          {{labelForm.parentId}}
        </el-form-item>
        <el-form-item :label="$t('goodsOrderForm.netType')" prop="netType">
          <el-select v-model="inputForm.netType"  filterable  clearable :placeholder="$t('goodsOrderForm.inputWord')" @change="getProduct">
            <el-option v-for="item in formProperty.netTypes" :key="item":label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('goodsOrderForm.shipType')" prop="shipType">
          <el-select v-model="inputForm.shipType" filterable clearable :placeholder="$t('goodsOrderForm.inputKey')" @change="getProduct">
            <el-option v-for="item in formProperty.shipTypes" :key="item":label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('goodsOrderForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderForm.carrierShop')" prop="carrierShopId">
              <el-select v-model="inputForm.carrierShopId" clearable filterable remote ::placeholder="$t('goodsOrderForm.selectWord')" :remote-method="remoteCarrierShop" :loading="remoteLoading">
                <el-option v-for="item in carrierShops" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.carrierCodes')" prop="carrierCodes">
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.carrierDetails')"  prop="carrierDetails">
              <el-input type="textarea" v-model="inputForm.carrierDetails" ></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.shopType')" prop="type">
              {{labelForm.typeLabel}}
          </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.priceSystem')" prop="pricesystem">
              {{labelForm.pricesystemId}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.summary')" prop="summary">
              {{labelForm.summary}}
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-input v-model="productName" @change="searchDetail" :placeholder="$t('goodsOrderForm.selectTowKey')" style="width:200px;"></el-input>
      <el-table :data="filterGoodsOrderDetailList" v-loading="pageLoading" :element-loading-text="$t('goodsOrderForm.loading')" stripe border style="margin-top:5px;" >
        <el-table-column  prop="product.name" :label="$t('goodsOrderForm.productName')"></el-table-column>
        <el-table-column prop="hasIme" :label="$t('goodsOrderForm.hasIme')" width="120">
          <template scope="scope">
            <el-tag :type="scope.row.product.hasIme ? 'primary' : 'danger'">{{scope.row.product.hasIme | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="price" :label="$t('goodsOrderForm.price')"></el-table-column>
        <el-table-column prop="qty" :label="$t('goodsOrderForm.qty')">
          <template scope="scope">
            <input type="text" v-model="scope.row.qty" class="el-input__inner"/>
          </template>
        </el-table-column>
        <el-table-column prop="allowOrderAndBill" :label="$t('goodsOrderForm.allowOrderAndBill')">
          <template scope="scope">
            <el-tag :type="scope.row.product.allowOrder? 'primary' : 'danger'">{{scope.row.product.allowOrder | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="areaQty" :label="$t('goodsOrderForm.areaQty')" ></el-table-column>
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
        remoteLoading:false,
        pageLoading:false,
        alertError:false,
        formProperty:{},
        productName:"",
        depots:[],
        carrierShops:[],
        filterGoodsOrderDetailList:[],
        inputForm:{
          id:this.$route.query.id,
          shopId:'',
          netType:'',
          shipType:'',
          remarks:'',
          carrierShopId:'',
          carrierCodes:'',
          carrierDetails:'',
          goodsOrderDetailList:[],
        },
        labelForm:{},
        rules: {
        }
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.inputForm.goodsOrderDetailList=this.filterGoodsOrderDetailList;
            axios.post('/api/crm/goodsOrder/save',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(response.data.success){
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'goodsOrderList',query:util.getQuery("goodsOrderList")})
                }
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },remoteDepot(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/crm/depot/search', {params: {name: query, type: "SHOP"}}).then((response)=> {
            this.depots = response.data;
            this.remoteLoading = false;
          })
        } else {
          this.depots = [];
        }
      },remoteCarrierShop(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/api/carrierShop/search', {params: {name: query}}).then((response)=> {
            this.carrierShops = response.data;
            this.remoteLoading = false;
          })
        } else {
          this.depots = [];
        }
      },getDepot(shopId){
        if(shopId){
          axios.get('/api/crm/depot/findOne',{params: {id:shopId}}).then((response)=>{
            this.labelForm.pricesystemId =response.data.pricesystem.name;
            this.labelForm.typeLabel=response.data.typeLabel;
            if(response.data.parent){
              this.labelForm.parentId=response.data.parent.name;
            }else{
              this.labelForm.parentId="";
            }
          })
        }
      },getProduct(){
        axios.get('/api/crm/goodsOrder/getGoodsOrderDetail',{params: {id:this.inputForm.id,shopId:this.inputForm.shopId,netType:this.inputForm.netType,shipType:this.inputForm.shipType}}).then((response)=>{
          if(!response.data.errors){
            this.inputForm.goodsOrderDetailList=response.data;
             this.filterGoodsOrderDetailList=response.data;
          }else{
            this.alertError=true;
            this.error=response.data.errors.id.message
          }
        })
      },getFormProperty(){
        axios.get('/api/crm/goodsOrder/getFormProperty').then((response)=>{
          this.formProperty=response.data;
          if(this.formProperty.netTypes.indexOf("全网通")!=-1){
             this.inputForm.netType="全网通";
          }
        });
      },searchDetail(){
        var val=this.productName;
        var tempList=new Array();
        for(var index in this.inputForm.goodsOrderDetailList){
          var detail=this.inputForm.goodsOrderDetailList[index];
          if(util.isNotBlank(detail.qty)){
            tempList.push(detail)
          }
        }
        for(var index in this.inputForm.goodsOrderDetailList){
          var detail=this.inputForm.goodsOrderDetailList[index];
          if(util.contains(detail.product.name,val) && util.isBlank(detail.qty)){
            tempList.push(detail)
          }
        }
        this.filterGoodsOrderDetailList = tempList;
      }
    },created(){
      this.getFormProperty();
      if(!this.isCreate){
        axios.get('/api/crm/goodsOrder/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
          this.depots=new Array(response.data.shop)
        })
      }
    }
  }
</script>
