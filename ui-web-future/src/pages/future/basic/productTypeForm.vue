<template>
  <div>
    <head-tab active="productTypeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('productTypeForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.reportName')" prop="reportName" >
          <el-input v-model="inputForm.reportName"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.product')" prop="productIdList">
          <product-select v-model="inputForm.productIdList" multiple ></product-select>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.code')" prop="code">
          <el-input v-model="inputForm.code"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.baokaPrice')" prop="baokaPrice">
          <el-input v-model.number="inputForm.baokaPrice"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.scoreType')" prop="scoreType" >
          <bool-select v-model="inputForm.scoreType"></bool-select>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.price1')" prop="price1">
          <el-input v-model.number="inputForm.price1"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.price2')" prop="price2">
          <el-input v-model.number="inputForm.price2"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.price3')" prop="price3">
          <el-input v-model.number="inputForm.price3"></el-input>
        </el-form-item>
        <el-form-item :label="$t('productTypeForm.remarks')" prop="remarks">
          <el-input  type="textarea" v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('productTypeForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import productSelect from 'components/future/product-select'
  import boolSelect from 'components/common/bool-select'

  export default{
    components:{
      productSelect,
      boolSelect,
    },
      data(){
        return this.getData()
      },
    methods:{
      getData() {
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            inputForm:{
                extra:{},
            },
            rules: {
              name: [{ required: true, message: this.$t('productTypeForm.prerequisiteMessage'),trigger:"blur"}],
              reportName: [{ required: true, message: this.$t('productTypeForm.prerequisiteMessage'),trigger:"blur"}],
              code: [{ required: true, message: this.$t('productTypeForm.prerequisiteMessage'),trigger:"blur"}],
              scoreType: [{ type:"boolean",required: true, message: this.$t('productTypeForm.prerequisiteMessage'),trigger: 'blur'}],
              baokaPrice: [{type: 'number', required: true, message: this.$t('productTypeForm.inputLegalValue'),trigger:"blur"}],
              price1: [{type: 'number', required: true, message: this.$t('productTypeForm.inputLegalValue'),trigger:"blur"}],
              price2: [{type: 'number', required: true, message: this.$t('productTypeForm.inputLegalValue'),trigger:"blur"}],
              price3: [{type: 'number', required: true, message: this.$t('productTypeForm.inputLegalValue'),trigger:"blur"}],
            },
            remoteLoading:false,
            products:[]
          }
      },
        formSubmit(){

          this.submitDisabled = true;
          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/ws/future/basic/productType/save',qs.stringify(util.deleteExtra(this.inputForm))).then((response)=> {
                this.$message(response.data.message);
                if(response.data.success) {
                    if (this.isCreate) {
                      Object.assign(this.$data, this.getData());
                      this.initPage();
                    }else{
                      this.submitDisabled = false;
                      this.$router.push({name: 'productTypeList', query: util.getQuery("productTypeList")})
                    }
                }
              }).catch( ()=>{
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          });
        },initPage(){
        axios.get('/api/ws/future/basic/productType/getForm').then((response)=>{
          this.inputForm=response.data;
          if(!this.isCreate){
            axios.get('/api/ws/future/basic/productType/findDto', {params: {id:this.$route.query.id}}).then((response)=>{
              util.copyValue(response.data, this.inputForm);
            });
          }
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
