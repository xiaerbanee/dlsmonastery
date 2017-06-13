<template>
  <div>
    <head-tab active="productImeUploadBackForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" label-width="120px" class="form input-form">
        <el-row >
          <el-col :span="21">
            <el-form-item  >
              <su-alert :text="$t('productImeUploadBackForm.instruction')" type="success"></su-alert>
            </el-form-item>
            <el-form-item>
              <su-alert :text="errMsg" type="danger"> </su-alert>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item :label="$t('productImeUploadBackForm.ime')" prop="imeStr">
              <el-input type="textarea" :rows="6" v-model="inputForm.imeStr" :placeholder="$t('productImeUploadBackForm.inputIme')" ></el-input>
            </el-form-item>
            <el-form-item >
              <el-button  type="primary" @click.native="onImeStrChange">{{$t('productImeUploadBackForm.search')}}</el-button>
              <el-button  type="primary" @click.native="reset">{{$t('productImeUploadBackForm.reset')}}</el-button>
            </el-form-item>
            <el-form-item  v-if="searched" >
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('productImeUploadBackForm.uploadBack')}}</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="18" v-if="searched">
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
  import suAlert from 'components/common/su-alert'

  export default{
    components:{
      suAlert,

    },
    data(){
      return this.getData()
    },
    methods:{
      getData() {
      return{
        isInit:false,
        searched:false,

        inputForm:{
            imeStr:'',
        },
        submitDisabled:false,

        productImeList:[],
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

            axios.post('/api/ws/future/crm/productImeUpload/uploadBack', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
              this.$message(response.data.message);
              Object.assign(this.$data, this.getData());
              this.$router.push({name:'productImeUploadList',query:util.getQuery("productImeUploadList")})

            }).catch( () => {
              this.submitDisabled = false;
            });
          }
        });
      },onImeStrChange(){
        this.searched = true;
        axios.get('/api/ws/future/crm/productImeUpload/checkForUploadBack',{params:{imeStr:this.inputForm.imeStr}}).then((response)=>{
          this.errMsg=response.data;
        });

        axios.get('/api/ws/future/crm/productIme/findDtoListByImes',{params:{imeStr:this.inputForm.imeStr}}).then((response)=>{
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
      },reset(){
        this.searched = false;
        this.inputForm.imeStr = '';

        this.errMsg='';
        this.productImeList=[];
        this.productQtyList = [];
        this.$refs["inputForm"].resetFields();
      }
    },activated(){

      if(!this.$route.query.headClick || !this.isInit) {
        Object.assign(this.$data, this.getData());

        axios.get('/api/ws/future/crm/productImeUpload/getUploadBackForm').then((response)=>{
          this.inputForm = response.data;
        });
      }
      this.isInit = true;
    }
  }
</script>
