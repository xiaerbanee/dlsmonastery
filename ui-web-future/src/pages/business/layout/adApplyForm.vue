<template>
  <div>
    <head-tab :active="$t('adApplyForm.adApplyForm')"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('adApplyForm.billType')" prop="billType">
          <el-select v-model="inputForm.billType" :placeholder="$t('adApplyForm.selectInput')" :clearable=true @change="onchange">
            <el-option v-for="billType in formProperty.billTypes"  :key="billType" :label="billType" :value="billType"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('adApplyForm.shopName')" prop="shopId">
          <el-select v-model="inputForm.shopId" filterable remote :placeholder="$t('adApplyForm.inputWord')" :remote-method="remoteShop" :loading="remoteLoading" :clearable=true @change="getShopTypeLabel(inputForm.shopId)">
            <el-option v-for="shop in shops" :key="shop.id" :label="shop.name" :value="shop.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('adApplyForm.shopType')" prop="shopTypeLabel" v-if="shopTypeLabel">
          {{shopTypeLabel}}
        </el-form-item>
        <el-form-item :label="$t('adApplyForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks" type="textarea"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adApplyForm.save')}}</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20" style="margin-bottom:20px;float:right">
        <span>{{$t('adApplyForm.search')}}</span>
        <el-input v-model="productName" @change="searchDetail" :placeholder="$t('adApplyForm.inputTowKey')" style="width:200px;margin-right:10px"></el-input>
      </el-row>
      <el-table :data="inputForm.adApplyList"  stripe border>
        <el-table-column prop="product.code" :label="$t('adApplyForm.productCode')"></el-table-column>
        <el-table-column prop="applyQty" :label="$t('adApplyForm.applyQty')">
          <template scope="scope">
            <el-input v-model="scope.row.applyQty"></el-input>
          </template>
        </el-table-column>
        <el-table-column prop="product.name" :label="$t('adApplyForm.productName')"></el-table-column>
        <el-table-column prop="product.expiryDateRemarks" sortable :label="$t('adApplyForm.expiryDateRemarks')"></el-table-column>
        <el-table-column prop="product.remarks" sortable :label="$t('adApplyForm.remarks')"></el-table-column>
        <el-table-column prop="product.price2" :label="$t('adApplyForm.price2')"></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        submitDisabled:false,
        formProperty:{billTypes:[]},
        shopTypeLabel:'',
        filterAdApplyList:[],
        productName:"",
        shops:{},
        inputForm:{
          billType:'',
          adApplyList:[],
          shopId:'',
          remarks:'',
        },
        rules: {
          billType: [{ required: true, message: this.$t('adApplyForm.prerequisiteMessage')}],
          shopId: [{ required: true, message: this.$t('adApplyForm.prerequisiteMessage')}]
        },
        remoteLoading:false
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.inputForm.adApplyList=this.filterAdApplyList
            axios.post('/api/crm/adApply/save',qs.stringify(this.inputForm,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'adApplyList',query:util.getQuery("adApplyList")})
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },
       searchDetail(){
         var val=this.productName;
         var tempList=new Array();
          for(var index in this.inputForm.adApplyList){
            var detail=this.inputForm.adApplyList[index];
            if(util.isNotBlank(detail.applyQty)){
              tempList.push(detail)
             }
          }
         for(var index in this.inputForm.adApplyList){
           var detail=this.inputForm.adApplyList[index];
           if((val.length>=1 && util.contains(detail.product.name,val)) && util.isBlank(detail.applyQty)){
             tempList.push(detail)
           }
         }
         this.filterAdApplyList = tempList;
       },remoteShop(query) {
        if (query !== '') {
          this.remoteLoading = true;
            axios.get('/api/crm/depot/adShop',{params:{adShopName:query,billType:this.inputForm.billType}}).then((response)=>{
            this.shops = response.data;
            this.remoteLoading = false;
           })
        }
      },onchange(){
          this.submitDisabled = true;
          axios.get('api/crm/adApply/getAdApplyList',{params:{shopId:this.inputForm.shopId,billType:this.inputForm.billType}}).then((response) =>{
          this.inputForm.adApplyList = response.data;
          this.submitDisabled = false;
          });
      },getShopTypeLabel(id) {
        var arr = this.shops;
        if(id){
         for(var i in arr){
            if(arr[i].id === id){
               this.shopTypeLabel =  arr[i].typeLabel;
               return;
            }
         }
       }
     }
    },created () {
        this.pageHeight = window.outerHeight -320;
        axios.get('api/crm/adApply/getFormProperty').then((response) =>{
        this.formProperty.billTypes = response.data.billTypes;
      });
    }
  }
</script>
