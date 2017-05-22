<template>
  <div>
    <head-tab active="goodsOrderForm"></head-tab>
    <el-row v-if="alertError">
      <el-col :span="24">
        <el-alert v-html="error" title="" type="error" :closable="true"></el-alert>
      </el-col>
    </el-row>
    <div >
      <el-form :model="submitData" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderForm.shop')" prop="shopId">
              <depot-select :disabled="!isCreate" v-model="goodsOrder.shopId" category="directShop" @input="shopChanged"></depot-select>
            </el-form-item>
            <el-form-item  :label="$t('goodsOrderForm.clientName')"  prop="clientName">
              {{shop.clientName}}
            </el-form-item>
            <el-form-item  :label="$t('goodsOrderForm.isUseTicket')" prop="isUseTicket">
              <bool-radio-group v-model="goodsOrder.isUseTicket"></bool-radio-group>
            </el-form-item>

            <div v-show="goodsOrder.shopId">

              <el-form-item :label="$t('goodsOrderForm.netType')" prop="netType">
                <el-select  :disabled="!isCreate" v-model="goodsOrder.netType"    clearable :placeholder="$t('goodsOrderForm.inputWord')" @change="refreshDetailList">
                  <el-option v-for="item in inputProperty.netTypeList" :key="item":label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="$t('goodsOrderForm.shipType')" prop="shipType" >
                <el-select   v-model="goodsOrder.shipType"  clearable :placeholder="$t('goodsOrderForm.inputKey')" @change="refreshDetailList" >
                  <el-option v-for="item in inputProperty.shipTypeList" :key="item":label="item" :value="item"></el-option>
                </el-select>

              </el-form-item>
              <el-form-item :label="$t('goodsOrderForm.remarks')" prop="remarks">
                <el-input type="textarea" v-model="goodsOrder.remarks"></el-input>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="12">
            <div v-show="goodsOrder.shopId">
              <el-form-item :label="$t('goodsOrderForm.shopType')" prop="type">
                {{shop.depotType}}
              </el-form-item>
              <el-form-item :label="$t('goodsOrderForm.priceSystem')" prop="pricesystem">
                {{shop.pricesystemName}}
              </el-form-item>

              <el-form-item :label="$t('goodsOrderForm.summary')" prop="summary">
                {{summary}}
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderForm.save')}}</el-button>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
      </el-form>
      <div v-show="goodsOrder.shopId">
        <el-input v-model="productName" @input="filterProducts" :placeholder="$t('shopAllotForm.selectTowKey')" style="width:200px;"></el-input>
        <el-table :data="filterDetailList" border stripe v-loading="pageLoading" >
          <el-table-column  prop = "productName" :label="$t('goodsOrderForm.productName')" ></el-table-column>
          <el-table-column prop="productHasIme" :label="$t('goodsOrderForm.hasIme')" width="70">
            <template scope="scope">
              <el-tag   :type="scope.row.productHasIme ? 'primary' : 'danger'">{{scope.row.productHasIme | bool2str}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="price" :label="$t('goodsOrderForm.price')" width="100"></el-table-column>
          <el-table-column prop="qty" :label="$t('goodsOrderForm.qty')">
            <template scope="scope">
              <input   type="text" v-model="scope.row.qty" class="el-input__inner"/>
            </template>
          </el-table-column>
          <el-table-column prop="productAllowOrderAndBill" :label="$t('goodsOrderForm.allowOrderAndBill')">
            <template scope="scope">
              <el-tag  :type="scope.row.productAllowOrderAndBill? 'primary' : 'danger'">{{scope.row.productAllowOrderAndBill | bool2str}}</el-tag>
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
  import productSelect from 'components/future/product-select'
  import boolRadioGroup from 'components/common/bool-radio-group'

  export default{
    components:{
      depotSelect,
      productSelect,
      boolRadioGroup,

    },
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        pageLoading:false,
        alertError:false,
        productName:'',
        goodsOrder:{},
        filterDetailList:[],
        goodsOrderDetailList:[],
        shop:{},
        summary:'',
        inputProperty:{},
        submitData:{
          id:'',
          shopId:'',
          netType:'',
          shipType:'',
          isUseTicket:'',
          remarks:'',
          goodsOrderDetailList:[],
        },
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
            this.initSubmitDataBeforeSubmit();

            axios.post('/api/ws/future/crm/goodsOrder/save', qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
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
      },filterProducts(){

        if(!this.goodsOrderDetailList){
          this.filterDetailList = [];
          return;
        }
        let val=this.productName;
        let tempList=[];

        for(let goodsOrderDetail of this.goodsOrderDetailList){

          if(util.isNotBlank(goodsOrderDetail.qty)){
            tempList.push(goodsOrderDetail);
          }
        }
        for(let goodsOrderDetail of this.goodsOrderDetailList){
          if(util.contains(goodsOrderDetail.productName, val) && util.isBlank(goodsOrderDetail.qty)){
            tempList.push(goodsOrderDetail);
          }
        }
        this.filterDetailList = tempList;

      }, initSubmitDataBeforeSubmit(){
        this.submitData.id = this.goodsOrder.id;
        this.submitData.shopId = this.goodsOrder.shopId;
        this.submitData.netType = this.goodsOrder.netType;
        this.submitData.shipType = this.goodsOrder.shipType;
        this.submitData.remarks = this.goodsOrder.remarks;
        this.submitData.isUseTicket = this.goodsOrder.isUseTicket;
        if(this.goodsOrderDetailList){
          let tempList=[];
          for(let goodsOrderDetail of this.goodsOrderDetailList){
            if(util.isNotBlank(goodsOrderDetail.id) || util.isNotBlank(goodsOrderDetail.qty)){
              tempList.push(goodsOrderDetail);
            }
          }
          this.submitData.goodsOrderDetailList = tempList;
        }else{
          this.submitData.goodsOrderDetailList = [];
        }

      },shopChanged(){

        axios.get('/api/ws/future/basic/depot/findById',{params: {id:this.goodsOrder.shopId}}).then((response)=>{
          this.shop = response.data;
        });
        this.refreshDetailList();

      },refreshDetailList(){

          if(!this.isCreate){
              return ;  //修改时不能改变detail列表，只能修改detail里每条记录的数量
          }

        if(this.goodsOrder.shopId&&this.goodsOrder.netType) {
            this.pageLoading = true;
            axios.get('/api/ws/future/crm/goodsOrder/findDetailListForNew', {params: {shopId:this.goodsOrder.shopId, netType: this.goodsOrder.netType}}).then((response)=>{
              this.setGoodsOrderDetailList(response.data);
              this.pageLoading = false;
            });
        }else{
          this.setGoodsOrderDetailList([]);
        }

      },setGoodsOrderDetailList(list){
        this.goodsOrderDetailList = list;
        this.filterProducts();
      }
    }, created(){

      axios.get('/api/ws/future/crm/goodsOrder/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputProperty = response.data;
      });

      if(!this.isCreate){
        axios.get('/api/ws/future/crm/goodsOrder/findDetailListForEdit',{params: {id:this.$route.query.id}}).then((response)=>{
          this.setGoodsOrderDetailList(response.data);
        });
      }else{
        this.setGoodsOrderDetailList([]);
      }

      axios.get('/api/ws/future/crm/goodsOrder/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
        this.goodsOrder = response.data;
      });
      axios.get('/api/ws/future/basic/depot/findShopByGoodsOrderId',{params: {goodsOrderId:this.$route.query.id}}).then((response)=>{
        this.shop = response.data;
      });
    }
  }
</script>
