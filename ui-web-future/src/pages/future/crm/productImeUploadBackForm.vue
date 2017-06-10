<template>
  <div>
    <head-tab active="productImeUploadBackForm"></head-tab>
    <div>
      <el-form :model="productImeUpload" ref="inputForm" label-width="120px" class="form input-form">
        <el-row >
          <el-col :span="21">
            <el-form-item>
              <el-alert  v-show="errMsg"  :closable=false  title=""  :description="errMsg" type="error"> </el-alert>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('productImeUploadBackForm.ime')" prop="imeStr">
              <el-input type="textarea" :rows="6" v-model="imeStr" :placeholder="$t('productImeUploadBackForm.inputIme')" @change="imeStrChanged"></el-input>
            </el-form-item>
            <div v-if="imeStr !==''">

              <el-form-item>
                <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('productImeUploadBackForm.uploadBack')}}</el-button>
              </el-form-item>
            </div>

          </el-col>
          <el-col :span="18" v-if="imeStr !==''">
            <template>
              <el-table :data="productQtyList" style="width: 100%" border>
                <el-table-column prop="productName" :label="$t('productImeUploadBackForm.productName')"></el-table-column>
                <el-table-column prop="qty" :label="$t('productImeUploadBackForm.qty')"></el-table-column>
              </el-table>
            </template>
            <template>
              <el-table :data="productImeList" style="width: 100%" border>
                <el-table-column prop="ime" :label="$t('productImeUploadBackForm.ime')"></el-table-column>
                <el-table-column prop="productName" :label="$t('productImeUploadBackForm.productName')"></el-table-column>
                <el-table-column prop="retailDate" :label="$t('productImeUploadBackForm.retailDate')"></el-table-column>
                <el-table-column prop="productImeSaleEmployeeName" :label="$t('productImeUploadBackForm.saleCreatedFullName')"></el-table-column>
                <el-table-column prop="productImeSaleCreatedDate" :label="$t('productImeUploadBackForm.saleCreatedDate')"></el-table-column>
                <el-table-column prop="productImeSaleShopName" :label="$t('productImeUploadBackForm.saleShopName')"></el-table-column>
                <el-table-column prop="productImeUploadCreatedByName" :label="$t('productImeUploadBackForm.updateDate')"></el-table-column>
                <el-table-column prop="productImeUploadCreatedDate" :label="$t('productImeUploadBackForm.updateDate')"></el-table-column>
                <el-table-column prop="productImeUploadStatus" :label="$t('productImeUploadBackForm.productImeUploadStatus')"></el-table-column>
                <el-table-column prop="productImeUploadShopName" :label="$t('productImeUploadBackForm.uploadShopName')"></el-table-column>
                <el-table-column prop="productImeUploadEmployeeName" :label="$t('productImeUploadBackForm.saleEmployee')"></el-table-column>
              </el-table>
            </template>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<style>
  .el-table { margin-bottom: 50px;}
</style>
<script>

  export default{

    data(){
      return this.getData()
    },
    methods:{
      getData() {
      return{
        isInit:false,
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        productImeList:[],
        imeStr:'',
        errMsg:'',
        productQtyList:[],
        rules: {
          imeStr: [{ required: true, message: this.$t('productImeUploadBackForm.prerequisiteMessage')}],
        },
      }
    },
      formSubmit(){

        if (this.errMsg) {
          this.$alert( this.$t('productImeUploadBackForm.formInvalid'), this.$t('productImeUploadBackForm.notify'));
          return;
        }

        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.submitDisabled = true;

            axios.post('/api/ws/future/crm/productImeUpload/uploadBack',{params:{imeStr:this.imeStr}}).then((response)=> {
              this.$message(response.data.message);
              Object.assign(this.$data, this.getData());
              if(response.data.success){
                if(!this.isCreate){
                  this.$router.push({name:'productImeUploadList',query:util.getQuery("productImeUploadList")})
                }
              }
            }).catch( () => {
              this.submitDisabled = false;
            });
          }
        });
      },imeStrChanged(){
        axios.get('/api/ws/future/crm/productImeUpload/checkForUploadBack',{params:{imeStr:this.imeStr}}).then((response)=>{
          this.errMsg=response.data;
        });

        axios.get('/api/ws/future/crm/productIme/findDtoListByImes',{params:{imeStr:this.imeStr}}).then((response)=>{
          this.productImeList=response.data;

          let tmpMap = new Map();
          if(this.productImeList){
            for(let productIme of this.productImeList ){
              if(!tmpMap.has(productIme.productId)){
                tmpMap.set(productIme.productId, {productName:productIme.productName, qty:0});
              }
              tmpMap.get(productIme.productId).qty+=1;
            }
          }
          let tmpList = [];
          for(let key of tmpMap.keys()){
            tmpList.push(tmpMap.get(key));
          }
          this.productQtyList = tmpList;
        });
      }
    }
  }
</script>
