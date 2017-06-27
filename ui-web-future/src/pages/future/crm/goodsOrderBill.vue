<template>
  <div>
    <head-tab active="goodsOrderBill"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="150px" class="form input-form">
        <el-row >
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderBill.store')" prop="storeId">
              <el-select v-model="inputForm.storeId" clearable filterable @change="storeChange">
                <el-option v-for="item in inputForm.extra.storeList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.billDate')" prop="billDate">
              <date-picker v-model="inputForm.billDate"  ></date-picker>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.expressCompany')" prop="expressCompanyId">
              <el-select v-model="inputForm.expressCompanyId" clearable  >
                <el-option v-for="item in inputForm.extra.expressCompanyList" :key="item.id" :label="item.name" :value="item.id"></el-option>
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
            <el-form-item :label="$t('goodsOrderBill.contact')" prop="contator">
              <el-input v-model="inputForm.contator"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.address')" prop="address">
              <el-input type="textarea" v-model="inputForm.address"></el-input>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.mobilePhone')" prop="mobilePhone">
              <el-input v-model="inputForm.mobilePhone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('goodsOrderBill.areaName')" >
              {{shop.areaName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopName')" >
              {{shop.name}} <div style="color:red;font-size:16px">{{shop.areaType}}</div>
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.parentName')"  >
              {{shop.clientName}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopCredit')" prop="title">
              {{shop.credit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shouldGet')" prop="remarks" >
              {{shouldGet}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.shopRemarks')" prop="remarks">
              {{shop.remarks}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.credit')" prop="title">
              {{shop.credit}}
            </el-form-item>
            <el-form-item :label="$t('goodsOrderBill.summary')" prop="summary">
                {{summary}}
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('goodsOrderBill.save')}}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-input v-model="filterValue" @change="filterProducts" :placeholder="$t('goodsOrderBill.selectTowKey')" style="width:200px;"></el-input>
      <el-table :data="filterDetailList" style="margin-top:5px;" border v-loading="pageLoading" :element-loading-text="$t('goodsOrderBill.loading')" stripe border>
        <el-table-column  prop="productName" :label="$t('goodsOrderBill.productName')" sortable width="200"></el-table-column>
        <el-table-column prop="areaQty" sortable :label="$t('goodsOrderBill.areaBillQty')"></el-table-column>
        <el-table-column prop="storeQty" :label="$t('goodsOrderBill.stock')"></el-table-column>
        <el-table-column prop="allowBill" :label="$t('goodsOrderBill.allowBill')">
          <template scope="scope">
            <el-tag :type="scope.row.allowBill? 'primary' : 'danger'">{{scope.row.allowBill | bool2str}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="qty" :label="$t('goodsOrderBill.qty')"></el-table-column>
        <el-table-column prop="billQty" :label="$t('goodsOrderBill.billQty')" >
          <template scope="scope">
            <input type="text" v-model="scope.row.billQty" @change="initSummary()" class="el-input__inner"/>
          </template>
        </el-table-column>
        <el-table-column prop="price" :label="$t('goodsOrderBill.price')">
          <template scope="scope">
            <input type="text" v-model="scope.row.price" @change="initSummary()" class="el-input__inner"/>
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
  export default{
    components:{
      boolRadioGroup
    },
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        filterValue:"",
        filterDetailList:[],
        inputForm:{
          extra:{}
        },
        shop:{},
        goodsOrder:{},
        shouldGet:null,
        summary:"",
        rules: {},
        pageLoading:false
      }
    },
    methods:{
      formSubmit(){
        var that=this;
        that.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            var submitData = util.deleteExtra(this.inputForm);
            var  goodsOrderBillDetailFormList = new Array();
            for(var index in this.filterDetailList) {
              var filterDetail = this.filterDetailList[index];
              if(util.isNotBlank(filterDetail.goodsOrderDetailId) || util.isNotBlank(filterDetail.billQty)) {
                goodsOrderBillDetailFormList.push(filterDetail);
              }
            }
            submitData.goodsOrderBillDetailFormList = goodsOrderBillDetailFormList;
            axios.post('/api/ws/future/crm/goodsOrder/bill', qs.stringify(submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              this.submitDisabled = false;
              this.$router.push({name:'goodsOrderList',query:util.getQuery("goodsOrderList"), params:{_closeFrom:true}})
            }).catch(function () {
              that.submitDisabled = false;
            });
          }else{
            that.submitDisabled = false;
          }
        })

      },filterProducts(){
        let val=this.filterValue;
        if(util.isBlank(val)) {
          this.filterDetailList = this.inputForm.goodsOrderDetailList;
          return;
        }
        let tempList=[];
        for(let detail of this.inputForm.goodsOrderDetailList){
          if(util.isNotBlank(detail.billQty)){
            tempList.push(detail);
          }
        }
        for(let detail of this.inputForm.goodsOrderDetailList){
          if(util.contains(detail.productName, val) && util.isBlank(detail.billQty)){
            tempList.push(detail);
          }
        }
        this.filterDetailList = tempList;
      },initSummary(){
        var totalQty = 0;
        var totalBillQty = 0;
        var totalAmount = 0;
        var totalBillAmount = 0;
        for(var index in this.filterDetailList) {
          var filterDetail = this.filterDetailList[index];
          if(util.isNotBlank(filterDetail.qty)) {
            totalQty  = totalQty + filterDetail.qty*1;
            totalAmount = totalAmount + (filterDetail.qty*1)*(filterDetail.price*1);
          }
          if(util.isNotBlank(filterDetail.billQty)) {
            totalBillQty  = totalBillQty + filterDetail.billQty*1;
            totalBillAmount = totalBillAmount + (filterDetail.billQty*1)*(filterDetail.price*1);
          }
        }
        this.summary = "总开单数为：" + totalBillQty + "，总开单金额为：" + totalBillAmount + ",总订货数为：" + totalQty + ",总订货金额为：" + totalAmount;
      },storeChange(){
          if(!this.inputForm.goodsOrderDetailList){
              return;
          }
          //设置库存
          if(util.isBlank(this.inputForm.storeId)){
            for(let goodsOrderDetail of this.inputForm.goodsOrderDetailList){
              goodsOrderDetail.storeQty = null;
            }
            return;
          }
          axios.get('/api/ws/future/basic/depot/getCloudQtyMap', {params: {storeId: this.inputForm.storeId}}).then((response) => {
            for(let goodsOrderDetail of this.inputForm.goodsOrderDetailList){
              if(response.data[goodsOrderDetail.productOutId] !== undefined) {
                  goodsOrderDetail.storeQty = response.data[goodsOrderDetail.productOutId];
                }
            }
          });
      }
    },created(){
      axios.get('/api/ws/future/crm/goodsOrder/getBillForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
        axios.get('/api/ws/future/crm/goodsOrder/getBill',{params: {id:this.$route.query.id}}).then((response)=>{
          this.goodsOrder = response.data;
          util.copyValue(response.data,this.inputForm);
          this.inputForm.goodsOrderDetailList = response.data.goodsOrderDetailDtoList;
          this.filterProducts();
          this.initSummary();
          axios.get('/api/ws/future/basic/depot/findOne',{params: {id:response.data.shopId}}).then((response)=>{
            this.shop = response.data;
          });
        });
      });
    }
  }
</script>
