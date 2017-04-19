<template>
  <div>
    <head-tab active="productImeUploadForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('productImeUploadForm.ime')" prop="ime">
              <el-input type="textarea" :rows="6" v-model="inputForm.ime" :placeholder="$t('productImeUploadForm.inputIme')" @change="onchange"></el-input>
            </el-form-item>
            <el-form-item :label="$t('productImeUploadForm.month')" prop="month" v-if="inputForm.ime !==''">
              <el-date-picker  v-model="inputForm.month" type="month" align="left" :placeholder="$t('productImeUploadForm.selectDate')" format="yyyy-MM" ></el-date-picker>
            </el-form-item>
            <el-form-item :label="$t('productImeUploadForm.shopId')" prop="shopId" v-if="inputForm.ime !==''">
              <el-select v-model="inputForm.shopId"  filterable remote :placeholder="$t('productImeUploadForm.inputWord')" :remote-method="remoteDepot" :loading="remoteLoading" :clearable=true >
                <el-option v-for="depot in depots" :key="depot.id" :label="depot.name" :value="depot.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('productImeUploadForm.saleEmployee')" prop="employeeId" v-if="inputForm.ime !==''">
              <el-select v-model="inputForm.employeeId" filterable remote :placeholder="$t('productImeUploadForm.inputWord')" :remote-method="remoteEmployee" :loading="remoteLoading" :clearable=true>
                <el-option v-for="employee in employees" :key="employee.id" :label="employee.name" :value="employee.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('productImeUploadForm.remarks')" prop="remarks" v-if="inputForm.ime !==''">
              <el-input type="textarea" :rows="2" v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" v-if="inputForm.ime !==''">{{$t('productImeUploadForm.save')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="18" v-if="inputForm.ime !==''">
            <template>
              <el-table :data="searchData.productQty" style="width: 100%" border>
                <el-table-column prop="name":label="$t('productImeUploadForm.name')"></el-table-column>
                <el-table-column prop="value" :label="$t('productImeUploadForm.qty')"></el-table-column>
              </el-table>
            </template>
            <template>
              <el-table :data="searchData.productImeList" style="width: 100%" border>
                <el-table-column prop="ime" :label="$t('productImeUploadForm.ime')"></el-table-column>
                <el-table-column prop="depot.name" :label="$t('productImeUploadForm.depotName')"></el-table-column>
                <el-table-column prop="product.name" :label="$t('productImeUploadForm.productName')"></el-table-column>
                <el-table-column prop="retailDate" :label="$t('productImeUploadForm.retailDate')"></el-table-column>
                <el-table-column prop="productImeSale.shop.name" :label="$t('productImeUploadForm.saleShopName')"></el-table-column>
                <el-table-column prop="productImeSale.created.fullName" :label="$t('productImeUploadForm.saleCreatedFullName')"></el-table-column>
                <el-table-column prop="productImeSale.createdDate" :label="$t('productImeUploadForm.saleCreatedDate')"></el-table-column>
                <el-table-column prop="productImeUpload.shop.name" :label="$t('productImeUploadForm.uploadShopName')"></el-table-column>
                <el-table-column prop="productImeUpload.createdDate" :label="$t('productImeUploadForm.updateDate')"></el-table-column>
                <el-table-column prop="productImeUpload.created.fullName" :label="$t('productImeUploadForm.saleEmployee')"></el-table-column>
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
            employees:[],
            remoteLoading:false,
            depots:[],
            inputForm:{
              ime:'',
              shopId:'',
              month:"",
              employeeId:"",
              remarks:''
            },
            rules: {
              ime: [{ required: true, message: this.$t('productImeUploadForm.prerequisiteMessage')}],
              shopId: [{ required: true, message: this.$t('productImeUploadForm.prerequisiteMessage')}],
              month: [{ required: true, message: this.$t('productImeUploadForm.prerequisiteMessage')}],
              employeeId: [{ required: true, message: this.$t('productImeUploadForm.prerequisiteMessage')}],
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
              this.inputForm.month=util.formatDate( this.inputForm.month,'yyyy-MM');
              axios.post('/api/crm/productImeUpload/save',qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                this.submitDisabled = false;
                if(response.data.success){
                  if(this.isCreate){
                    form.resetFields();
                  } else {
                    this.$router.push({name:'productImeUploadList',query:util.getQuery("productImeUploadList")})
                  }
                }
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
        },remoteEmployee(query) {
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/hr/employee/search',{params:{key:query}}).then((response)=>{
              this.employees=response.data;
              this.remoteLoading = false;
            })
          }
        }
      },created(){
      }
    }
</script>
