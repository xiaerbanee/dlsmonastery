<template>
  <div>
    <head-tab active="productImeSaleForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('productImeSaleForm.ime')" prop="ime">
              <el-input type="textarea" :rows="6" v-model="inputForm.ime" :placeholder="$t('productImeSaleForm.inputIme')" @change="onchange"></el-input>
            </el-form-item>
            <el-form-item :label="$t('productImeSaleForm.shopId')" prop="shopId" v-if="inputForm.ime !==''">
              <el-select v-model="inputForm.shopId"  filterable remote :placeholder="$t('productImeSaleForm.inputWord')" :remote-method="remoteDepot" :loading="remoteLoading" :clearable=true >
                <el-option v-for="depot in depots" :key="depot.id" :label="depot.name" :value="depot.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('productImeSaleForm.buyer')" prop="buyer" v-if="inputForm.ime !==''">
              <el-input  v-model="inputForm.buyer"></el-input>
            </el-form-item>
            <el-form-item :label="$t('productImeSaleForm.buyerAge')" prop="buyerAge" v-if="inputForm.ime !==''">
              <el-input  v-model="inputForm.buyerAge"></el-input>
            </el-form-item>
            <el-form-item :label="$t('productImeSaleForm.buyerSex')" prop="buyerSex"  v-if="inputForm.ime !==''">
              <el-radio-group v-model="inputForm.buyerSex">
                <el-radio :label="$t('productImeSaleForm.man')"></el-radio>
                <el-radio :label="$t('productImeSaleForm.women')"></el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item  :label="$t('productImeSaleForm.buyerPhone')" prop="buyerPhone" v-if="inputForm.ime !==''">
              <el-input  v-model="inputForm.buyerPhone"></el-input>
            </el-form-item>
            <el-form-item :label="$t('productImeSaleForm.buyerSchool')" prop="buyerSchool" v-if="inputForm.ime !=='' && companyName=='JXIMOO'">
              <el-input  v-model="inputForm.buyerSchool"></el-input>
            </el-form-item>
            <el-form-item :label="$t('productImeSaleForm.buyerGrade')" prop="buyerGrade" v-if="inputForm.ime !=='' && companyName=='JXIMOO'">
              <el-input  v-model="inputForm.buyerGrade"></el-input>
            </el-form-item>
            <el-form-item :label="$t('productImeSaleForm.remarks')" prop="remarks" v-if="inputForm.ime !==''">
              <el-input type="textarea" :rows="2" v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" v-if="inputForm.ime !==''">{{$t('productImeSaleForm.save')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="18" v-if="inputForm.ime !==''">
            <template>
              <el-table :data="searchData.productQty" style="width: 100%" border>
                <el-table-column prop="name" :label="$t('productImeSaleForm.name')"></el-table-column>
                <el-table-column prop="value" :label="$t('productImeSaleForm.qty')"></el-table-column>
              </el-table>
            </template>
            <template>
              <el-table :data="searchData.productImeList" style="width: 100%" border>
                <el-table-column prop="ime" :label="$t('productImeSaleForm.ime')"></el-table-column>
                <el-table-column prop="depot.name" :label="$t('productImeSaleForm.depotName')"></el-table-column>
                <el-table-column prop="product.name"  :label="$t('productImeSaleForm.productType')"></el-table-column>
                <el-table-column prop="retailDate" :label="$t('productImeSaleForm.baokaDate')"></el-table-column>
                <el-table-column prop="productImeSale.shop.name" :label="$t('productImeSaleForm.shopName')"></el-table-column>
                <el-table-column prop="productImeSale.created.fullName" :label="$t('productImeSaleForm.createdFullName')"></el-table-column>
                <el-table-column prop="productImeSale.createdDate" :label="$t('productImeSaleForm.createdDate')"></el-table-column>
              </el-table>
            </template>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<style>
  .el-table { margin-bottom: 100px;}
</style>
<script>
    export default{
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            formProperty:{},
            companyName:'',
            remoteLoading:false,
            depots:[],
            inputForm:{
              ime:'',
              shopId:'',
              buyer:"",
              buyerAge:"",
              buyerSex:"",
              buyerPhone:"",
              buyerGrade:'',
              buyerSchool:'',
              remarks:''
            },
            rules: {
              ime: [{ required: true, message: this.$t('productImeSaleForm.prerequisiteMessage')}],
            },
            searchData:'',
            message:''
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/crm/productImeSale/save',qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                this.submitDisabled = false;
                if(response.data.success){
                  if(this.isCreate){
                    form.resetFields();
                  } else {
                    this.$router.push({name:'imeAllotList',query:util.getQuery("imeAllotList")})
                  }
                }
              }).catch(function () {
                this.submitDisabled = false;
              });
            }
          })
        },onchange(){
            this.message = '';
            axios.get('/api/crm/productIme/search',{params:{imeStr:this.inputForm.ime}}).then((response)=>{
              this.searchData=response.data;
            })
        },remoteDepot(query){
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/crm/depot/search',{params:{name:query}}).then((response)=>{
              this.depots=response.data;
              this.remoteLoading = false;
            })
          }
        }
      },created(){
        axios.get('/api/crm/productImeSale/getForm').then((response)=>{
          this.companyName=response.data.companyName;
        })
      }
    }
</script>
