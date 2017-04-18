<template>
  <div>
    <head-tab :active="$t('imeAllotForm.imeAllotForm') "></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item  :label="$t('imeAllotForm.imeStr')" prop="imeStr">
              <el-input type="textarea" :rows="6" v-model="inputForm.imeStr" :placeholder="$t('imeAllotForm.inputIme')" @change="onchange"></el-input>
            </el-form-item>
            <el-form-item :label="$t('imeAllotForm.toDepotId')" prop="toDepotId" v-if="inputForm.imeStr !==''">
              <el-select v-model="inputForm.toDepotId"  filterable remote :placeholder="$t('imeAllotForm.inputWord')" :remote-method="remoteDepot" :loading="remoteLoading" :clearable=true >
                <el-option v-for="depot in depots" :key="depot.id" :label="depot.name" :value="depot.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('imeAllotForm.remarks')" prop="remarks" v-if="inputForm.imeStr !==''">
              <el-input type="textarea" :rows="2" v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()" v-if="inputForm.imeStr !==''">{{$t('imeAllotForm.save')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="16" v-if="inputForm.imeStr !==''">
            <template>
              <el-alert :title="message" type="error" show-icon v-if="message !==''"> </el-alert>
              <el-table :data="searchData.products" style="width: 100%" border>
                <el-table-column prop="name" :label="$t('imeAllotForm.name')"></el-table-column>
                <el-table-column prop="value" :label="$t('imeAllotForm.qty')"></el-table-column>
              </el-table>
            </template>
            <template>
              <el-table :data="searchData.productImes" style="width: 100%" border>
                <el-table-column prop="ime" :label="$t('imeAllotForm.imeStr')"></el-table-column>
                <el-table-column prop="depot.name":label="$t('imeAllotForm.depotName')"></el-table-column>
                <el-table-column prop="product.name" :label="$t('imeAllotForm.productName')"></el-table-column>
                <el-table-column prop="retailDate" :label="$t('imeAllotForm.retailDate')"></el-table-column>
                <el-table-column prop="productImeSale.created.fullName":label="$t('imeAllotForm.saleCreatedFullName')"></el-table-column>
                <el-table-column prop="productImeSale.createdDate" :label="$t('imeAllotForm.saleCreatedDate')"></el-table-column>
                <el-table-column prop="productImeSale.shop.name" :label="$t('imeAllotForm.uploadShopName')"></el-table-column>
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
            depots:[],
            remoteLoading:false,
            inputForm:{
              imeStr:'',
              toDepotId:"",
              remarks:''
            },
            rules: {
              imeStr: [{ required: true, message: this.$t('imeAllotForm.prerequisiteMessage')}],
              toDepotId: [{ required: true, message: this.$t('imeAllotForm.prerequisiteMessage')}]
            },
            searchData:{},
            message:''
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          if(this.message !==''){
            alert("请处理异常数据后{{$t('imeAllotForm.save')}}");
            form.resetFields();
            return
          }
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/crm/imeAllot/save',qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'imeAllotList',query:util.getQuery("imeAllotList")})
                }
              });
            }
          })
        },remoteDepot(query){
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/crm/depot/search',{params:{name:query}}).then((response)=>{
              this.depots=response.data;
              this.remoteLoading = false;
            })
          }
        },onchange(){
            this.message = '';
            axios.get('/api/crm/imeAllot/searchImeMap',{params:{imeStr:this.inputForm.imeStr}}).then((response)=>{
              this.searchData=response.data;
              this.message = response.data.message;
            })
        }
      },created(){
      }
    }
</script>
