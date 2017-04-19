<template>
  <div>
    <head-tab active="shopAllotForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <template>
          <el-alert :title="message" type="error" show-icon v-if="message !==''"></el-alert>
        </template>
        <el-form-item :label="$t('shopAllotForm.fromShop')" prop="fromShopId">
          <el-select v-model="inputForm.fromShopId" filterable remote  :placeholder="$t('shopAllotForm.inputWord')" :remote-method="remoteFromShop" :loading="remoteLoading" @change="onchange" >
            <el-option v-for="item in fromShops" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('shopAllotForm.toShop')" prop="toShopId">
          <el-select v-model="inputForm.toShopId" filterable remote  :placeholder="$t('shopAllotForm.inputWord')" :remote-method="remoteToShop" :loading="remoteLoading" @change="onchange" >
            <el-option v-for="item in toShops" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('shopAllotForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('shopAllotForm.save')}}</el-button>
        </el-form-item>
        <el-input v-model="productName" @change="searchDetail" :placeholder="$t('shopAllotForm.selectTowKey')" style="width:200px;"></el-input>
        <el-table :data="filterShopAllotDetailList"  style="margin-top:5px;"   stripe border >
          <el-table-column prop="product.name" :label="$t('shopAllotForm.productName')"></el-table-column>
          <el-table-column prop="qty" :label="$t('shopAllotForm.qty')">
            <template scope="scope">
              <input type="text" v-model="scope.row.qty" class="el-input__inner"/>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </div>
  </div>
</template>
<script>
    export default{
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            formProperty:{},
            productName:'',
            fromShops:[],
            toShops:[],
            products:[],
            filterShopAllotDetailList:[],
            message:'',
            remoteLoading:false,
            inputForm:{
              id:this.$route.query.id,
              fromShopId:'',
              toShopId:'',
              remarks:'',
              shopAllotDetailList:[]
            },
            rules: {
              fromShopId: [{ required: true, message: this.$t('shopAllotForm.prerequisiteMessage')}],
              toShopId: [{ required: true, message: this.$t('shopAllotForm.prerequisiteMessage')}]
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.inputForm.shopAllotDetailList=this.filterShopAllotDetailList;
              axios.post('/api/crm/shopAllot/save',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                   this.$router.push({name:'shopAllotList',query:util.getQuery("shopAllotList")})
                }
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },remoteFromShop(query){
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/crm/depot/search',{params:{name:query}}).then((response)=>{
              this.fromShops=response.data;
              this.remoteLoading = false;
            })
          }
        },remoteToShop(query){
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/crm/depot/search',{params:{name:query}}).then((response)=>{
              this.toShops=response.data;
              this.remoteLoading = false;
            })
          }
        },onchange(){
          if(this.inputForm.fromShopId!=='' && this.inputForm.toShopId!=='' && this.isCreate){
            this.message='';
            axios.get('/api/crm/shopAllot/getProducts',{params:{fromShopId:this.inputForm.fromShopId,toShopId:this.inputForm.toShopId}}).then((response)=>{
              if(response.data.success){
                this.inputForm.shopAllotDetailList=response.data.shopAllotDetailList;
                this.searchDetail();
              }else{
                this.message=response.data.message;
              }
            })
          }
        },searchDetail(){
          var val=this.productName;
          var tempList=new Array();
          for(var index in this.inputForm.shopAllotDetailList){
            var detail=this.inputForm.shopAllotDetailList[index];
            if(util.isNotBlank(detail.qty)){
              tempList.push(detail)
            }
          }
          for(var index in this.inputForm.shopAllotDetailList){
            var detail=this.inputForm.shopAllotDetailList[index];
            if(util.contains(detail.product.name,val) && util.isBlank(detail.qty)){
              tempList.push(detail)
            }
          }
          this.filterShopAllotDetailList = tempList;
        }
      },created(){
        if(!this.isCreate){
          axios.get('/api/crm/shopAllot/getEditFormData',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            this.inputForm.shopAllotDetailList=response.data.shopAllotDetailList;
            if(response.data.fromShop!=null){
              this.fromShops=new Array(response.data.fromShop);
            }
            if(response.data.toShop!=null){
              this.toShops=new Array(response.data.toShop);
            }
          })
        }
      }
    }
</script>
