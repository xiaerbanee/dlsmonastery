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
          <depot-select v-model="inputForm.shopId" type="SHOP" @input="refreshGoodsOrderDetailList"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('goodsOrderForm.parentShop')" prop="parentId">
          <depot-select :disabled="false" v-model="inputForm.parentId" type="SHOP"></depot-select>
        </el-form-item>
        <el-form-item :label="$t('goodsOrderForm.netType')" prop="netType">
          <el-select v-model="inputForm.netType"  filterable  clearable :placeholder="$t('goodsOrderForm.inputWord')" @change="refreshGoodsOrderDetailList">
            <el-option v-for="item in inputForm.netTypeList" :key="item":label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('goodsOrderForm.isUseTicket')" prop="isUseTicket">
          <bool-radio-group v-model="inputForm.isUseTicket"></bool-radio-group>
        </el-form-item>
        <el-form-item :label="$t('goodsOrderForm.shipType')" prop="shipType">
          <el-select v-model="inputForm.shipType" filterable clearable :placeholder="$t('goodsOrderForm.inputKey')" @change="refreshGoodsOrderDetailList">
            <el-option v-for="item in inputForm.shipTypeList" :key="item":label="item" :value="item"></el-option>
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
              {{inputForm.shopType}}
          </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.priceSystem')" prop="pricesystem">
              {{inputForm.priceSystemName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderForm.summary')" prop="summary">
              {{inputForm.summary}}
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderForm.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-input v-model="productName" @change="searchProduct" :placeholder="$t('goodsOrderForm.selectTowKey')" style="width:200px;"></el-input>
      <el-table :data="filterGoodsOrderDetailList" v-loading="pageLoading" :element-loading-text="$t('goodsOrderForm.loading')" stripe border style="margin-top:5px;" >
        <el-table-column  prop="productName" :label="$t('goodsOrderForm.productName')"></el-table-column>
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
  import depotSelect from 'components/future/depot-select'

  export default{
    components:{
      depotSelect,

    },
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,

        pageLoading:false,
        alertError:false,

        productName:"",
        depots:[],
        carrierShops:[],
        filterGoodsOrderDetailList:[],
        inputForm:{},
        submitData:{
          id:'',
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
            util.copyValue(this.inputForm,this.submitData);
            axios.post('/api/ws/future/crm/goodsOrder/save', qs.stringify(this.submitData)).then((response)=> {
              this.$message(response.data.message);
              if(this.inputForm.create){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'goodsOrderList',query:util.getQuery("goodsOrderList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },refreshGoodsOrderDetailList(){
        axios.get('/api/ws/future/crm/goodsOrder/getGoodsOrderDetail',{params: {id:this.inputForm.id,shopId:this.inputForm.shopId,netType:this.inputForm.netType,shipType:this.inputForm.shipType}}).then((response)=>{
          if(!response.data.errors){
            this.inputForm.goodsOrderDetailList=response.data;
            this.filterGoodsOrderDetailList=response.data;
          }else{
            this.alertError=true;
            this.error=response.data.errors.id.message
          }
        })
      },searchProduct(){
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
      axios.get('/api/ws/future/crm/goodsOrder/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
      })
    }
  }
</script>
